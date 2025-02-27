package model;

import java.sql.Timestamp;

public class TelecomActivity {
	private int customerId;
	private String type;
	private int value;
	private Timestamp timestamp;

	public TelecomActivity(int customerId, String type, int value, Timestamp timestamp) {
		this.customerId = customerId;
		this.type = type;
		this.value = value;
		this.timestamp = timestamp;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
