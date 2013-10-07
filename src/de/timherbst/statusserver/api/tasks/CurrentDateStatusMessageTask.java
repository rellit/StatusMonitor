package de.timherbst.statusserver.api.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.timherbst.statusserver.api.AbstractStatusMessageTask;
import de.timherbst.statusserver.model.MessageType;
import de.timherbst.statusserver.model.Property;
import de.timherbst.statusserver.model.StatusMessage;

public class CurrentDateStatusMessageTask extends AbstractStatusMessageTask {

	@Override
	public List<StatusMessage> getStatusMessages(List<Property> properties) {

		List<StatusMessage> messages = new ArrayList<StatusMessage>();
		try {

			messages.add(new StatusMessage(MessageType.INFO, getBereich(),
					getKunde(), new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
							.format(getNow())));
		} catch (Exception e) {
			messages.add(new StatusMessage(MessageType.ERROR, getBereich(),
					getKunde(), "Unbekannter Fehler: " + e.getMessage()));
		}
		return messages;
	}

	private Date getNow() {
		Calendar c = Calendar.getInstance();
		return new Date(c.getTimeInMillis());
	}

}
