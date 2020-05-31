package com.techland.paypay.security.events;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.Immutable;

import com.techland.paypay.contracts.PayPayEvent;
import com.techland.paypay.contracts.TechLandEvent;
import com.techland.paypay.security.helper.Settings;

@Immutable
@TechLandEvent(externalName = "Admin.SecurityCheckEvent")
public class AdminAddedEvent implements PayPayEvent, Serializable {

	private static final long serialVersionUID = 1L;
	private final String id;
	private final String sourceIP;
	private final Timestamp timestamp;
	private final String eventId;

	
	public AdminAddedEvent(String id, String sourceIP, Timestamp timestamp,
			String eventId) {
		this.id = id;
		this.sourceIP = sourceIP;
		this.timestamp = timestamp;
		this.eventId = eventId;
	}

	@Override
	public String getId() {
		return id;
	}


	@Override
	public Timestamp getTimestamp() {
		return timestamp;
	}

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public String getObiquitusName() {
		return Settings.DOMAIN + "." + this.getClass().getSimpleName();
	}

	public String getSourceIP() {
		return sourceIP;
	}

	@Override
	public String toString() {
		return "{\"class\":\"AdminAddedEvent\",\"id\":\"" + id + "\", \"sourceIP\":\"" + sourceIP
				+ "\", \"timestamp\":\"" + timestamp + "\", \"eventId\":\"" + eventId + "\"}";
	}

	

	

}
