package de.timherbst.statusserver.api.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.timherbst.statusserver.api.AbstractStatusMessageTask;
import de.timherbst.statusserver.model.MessageType;
import de.timherbst.statusserver.model.Property;
import de.timherbst.statusserver.model.StatusMessage;

public class TextStatusMessageTask extends AbstractStatusMessageTask {

	HashMap<String, String> props = new HashMap<String, String>();

	@Override
	public List<StatusMessage> getStatusMessages(List<Property> properties) {
		List<StatusMessage> messages = new ArrayList<StatusMessage>();
		try {
			readProperties(properties);

			messages.add(new StatusMessage(MessageType.INFO, getBereich(),
					getKunde(), props.get("text")));
		} catch (Throwable e) {
			messages.add(new StatusMessage(MessageType.ERROR, getBereich(),
					getKunde(), "Unbekannter Fehler: " + e.getMessage()));
		}
		return messages;
	}

	private void readProperties(List<Property> properties) {
		for (Property p : properties) {
			props.put(p.getKey(), p.getValue());
		}
	}

}
