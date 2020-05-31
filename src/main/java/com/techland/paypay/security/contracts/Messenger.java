package com.techland.paypay.security.contracts;

public interface Messenger {
	
	void sendMessage(final String message ,final String to,final String from,final String subject);

}
