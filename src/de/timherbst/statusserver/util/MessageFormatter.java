package de.timherbst.statusserver.util;

import java.util.ArrayList;
import java.util.List;

import de.timherbst.statusserver.model.StatusMessage;

public class MessageFormatter {

	public static String format(List<StatusMessage> messages) {
		String result = "";
		for (StatusMessage m : messages) {
			result += m.toHTML();
		}
		return result;
	}

	public static String format(StatusMessage message) {
		ArrayList<StatusMessage> l = new ArrayList<StatusMessage>();
		l.add(message);
		return format(l);
	}

}
