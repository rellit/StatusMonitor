package de.timherbst.statusserver.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import de.timherbst.statusserver.api.StatusMessageTask;
import de.timherbst.statusserver.model.MessageType;
import de.timherbst.statusserver.model.Property;
import de.timherbst.statusserver.model.StatusMessage;
import de.timherbst.statusserver.model.Task;
import de.timherbst.statusserver.model.Tasks;

public class MessageAggregator {

	private static List<StatusMessage> messages = new ArrayList<StatusMessage>();

	public static List<StatusMessage> getMessages() {
		return messages;
	}

	public static void refreshMessages() {
		List<StatusMessage> list = new ArrayList<StatusMessage>();
		try {
			for (Task t : loadTasks().getTasks()) {
				StatusMessageTask smt = (StatusMessageTask) Class.forName(
						t.getClassName().trim()).newInstance();
				smt.setKunde(t.getKunde());
				smt.setBereich(t.getBereich());
				list.addAll(smt.getStatusMessages(t.getProperties()));
			}
		} catch (Throwable t) {
			t.printStackTrace();
			list.add(new StatusMessage(MessageType.ERROR, "Refresh", "Monitor",
					"Failed: " + t.getMessage()));
		} finally {

			Collections.sort(list);
		}
		messages = list;
	}

	private static Tasks loadTasks() {
		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("StatusServer", Tasks.class);
		xstream.alias("task", Task.class);
		xstream.alias("property", Property.class);

		URL url = Thread.currentThread().getContextClassLoader()
				.getResource("tasks.xml");

		return (Tasks) xstream.fromXML(url);
	}

}
