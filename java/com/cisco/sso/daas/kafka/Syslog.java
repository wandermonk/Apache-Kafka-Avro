package com.cisco.sso.daas.kafka;

public class Syslog {

	private long partyID;
	private String applianceID;
	private String message;
	private long severity;
	private String messageType;
	private long eventtime;
	private String ipaddress;
	private String hostname;

	public long getPartyID() {
		return partyID;
	}

	public void setPartyID(long partyID) {
		this.partyID = partyID;
	}

	public String getApplianceID() {
		return applianceID;
	}

	public void setApplianceID(String applianceID) {
		this.applianceID = applianceID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getSeverity() {
		return severity;
	}

	public void setSeverity(long severity) {
		this.severity = severity;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public long getEventtime() {
		return eventtime;
	}

	public void setEventtime(long eventtime) {
		this.eventtime = eventtime;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Syslog syslog = (Syslog) o;
		
		if (ipaddress != null ? !ipaddress.equals(syslog.ipaddress) : syslog.ipaddress != null)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "SyslogMessage [partyID=" + partyID + ", applianceID=" + applianceID + ", message=" + message
				+ ", severity=" + severity + ", messageType=" + messageType + ", eventtime=" + eventtime
				+ ", ipaddress=" + ipaddress + ", hostname=" + hostname + "]";
	}

}
