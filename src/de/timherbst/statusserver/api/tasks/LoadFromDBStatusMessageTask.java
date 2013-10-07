package de.timherbst.statusserver.api.tasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.timherbst.statusserver.api.AbstractStatusMessageTask;
import de.timherbst.statusserver.model.MessageType;
import de.timherbst.statusserver.model.Property;
import de.timherbst.statusserver.model.StatusMessage;

public class LoadFromDBStatusMessageTask extends AbstractStatusMessageTask {

	private String driver = "";
	private String jdbcString = "";
	private String user = "user";
	private String pass = "pass";
	private String messageColumn = "MESSAGE";
	private String typeColumn = "TYPE";
	private String infoValue = "4";
	private String okValue = "3";
	private String actionValue = "2";
	private String warnValue = "1";
	private String errorValue = "0";
	private String sql = "";

	@Override
	public List<StatusMessage> getStatusMessages(List<Property> properties) {
		List<StatusMessage> messages = new ArrayList<StatusMessage>();
		readProperties(properties);

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			messages.add(new StatusMessage(MessageType.ERROR, getBereich(),
					getKunde(), "Could not load DriverClass " + driver));
		}

		Connection con = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.user);
		connectionProps.put("password", this.pass);
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(jdbcString, connectionProps);

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String message = rs.getString(messageColumn);
				String type = rs.getString(typeColumn);
				messages.add(new StatusMessage(getMessageType(type),
						getBereich(), getKunde(), message));
			}
		} catch (SQLException e) {
			messages.add(new StatusMessage(MessageType.ERROR, getBereich(),
					getKunde(), "SQLException: " + e.getMessage()));
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					messages.add(new StatusMessage(MessageType.ERROR,
							getBereich(), getKunde(), "SQLException: "
									+ e.getMessage()));
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					messages.add(new StatusMessage(MessageType.ERROR,
							getBereich(), getKunde(), "SQLException: "
									+ e.getMessage()));
				}
			}
		}

		return messages;
	}

	private MessageType getMessageType(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase(infoValue))
				return MessageType.INFO;
			if (type.equalsIgnoreCase(okValue))
				return MessageType.OK;
			if (type.equalsIgnoreCase(actionValue))
				return MessageType.ACTION;
			if (type.equalsIgnoreCase(warnValue))
				return MessageType.WARN;
			if (type.equalsIgnoreCase(errorValue))
				return MessageType.ERROR;
		}
		return MessageType.ERROR;
	}

	private void readProperties(List<Property> properties) {
		for (Property p : properties) {
			if ("driver".equalsIgnoreCase(p.getKey()))
				driver = p.getValue();
			if ("jdbcString".equalsIgnoreCase(p.getKey()))
				jdbcString = p.getValue();
			if ("user".equalsIgnoreCase(p.getKey()))
				user = p.getValue();
			if ("pass".equalsIgnoreCase(p.getKey()))
				pass = p.getValue();
			if ("messageColumn".equalsIgnoreCase(p.getKey()))
				messageColumn = p.getValue();
			if ("typeColumn".equalsIgnoreCase(p.getKey()))
				typeColumn = p.getValue();
			if ("infoValue".equalsIgnoreCase(p.getKey()))
				infoValue = p.getValue();
			if ("okValue".equalsIgnoreCase(p.getKey()))
				okValue = p.getValue();
			if ("actionValue".equalsIgnoreCase(p.getKey()))
				actionValue = p.getValue();
			if ("warnValue".equalsIgnoreCase(p.getKey()))
				warnValue = p.getValue();
			if ("errorValue".equalsIgnoreCase(p.getKey()))
				errorValue = p.getValue();
			if ("sql".equalsIgnoreCase(p.getKey()))
				sql = p.getValue();
		}
	}

}
