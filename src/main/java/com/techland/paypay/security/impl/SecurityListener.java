package com.techland.paypay.security.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.techland.paypay.Constants;
import com.techland.paypay.PayPayPayLoad;
import com.techland.paypay.contracts.PayPayEvent;
import com.techland.paypay.contracts.Subscriber;
import com.techland.paypay.messaging.PayPayListener;
import com.techland.paypay.security.factories.EventFactory;
import com.techland.paypay.security.factories.SubscriberFactory;
import com.techland.paypay.security.helper.Settings;
import com.techland.paypay.security.impl.SecurityState;
import com.techland.paypay.util.LogFeed;

@Component
public class SecurityListener {
	private PayPayListener listenerTools;
	private SubscriberFactory subscriberFactory;

	public SecurityListener(final PayPayListener listenerTools, final SubscriberFactory subscriberFactory) {
		this.listenerTools = listenerTools;
		this.subscriberFactory = subscriberFactory;
	}

	@StreamListener(target = Constants.IN)
	public void handleEvent(@Payload PayPayPayLoad payload) {
		System.out.println("Security listening .......");
		
		try {
			boolean isSelfOriginated = false;
			if (payload.getDomain().equals(Settings.DOMAIN))
				isSelfOriginated = true;
		//	System.out.println(payload.getEvent());
			PayPayEvent event = EventFactory.getEvent(payload.getEvent(), payload.getEventName(),isSelfOriginated?null:Settings.aggregateTag());
			
			ConcurrentHashMap<String, List<? extends Subscriber>> subscribers = subscriberFactory.getInstance(event);
			listenerTools.handleEvent(payload, subscribers, event, new SecurityState());

		} catch (Exception e) {
			e.printStackTrace();
			LogFeed logfeed = new LogFeed.LogProcessor().setInfo(Constants.SERVER_ERROR).setClazz(PayPayListener.class)
					.setDomain(Settings.DOMAIN).setOtherInfo(payload.toString()).build();
			logfeed.process();

		}

	}
}
