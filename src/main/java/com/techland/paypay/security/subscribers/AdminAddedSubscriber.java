package com.techland.paypay.security.subscribers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techland.paypay.contracts.EventSubscriber;
import com.techland.paypay.contracts.PayPayEvent;
import com.techland.paypay.contracts.TechLandSubscriber;
import com.techland.paypay.persistence.EventFailure;
import com.techland.paypay.persistence.EventFailureRepository;
import com.techland.paypay.security.helper.Email;
import com.techland.paypay.security.helper.Settings;
import com.techland.paypay.security.impl.FlutterWaveEngine;

@Component
@TechLandSubscriber(events = { "AdminAddedEvent" }, isstate = false)
public class AdminAddedSubscriber implements EventSubscriber {

	@Autowired
	FlutterWaveEngine fwEngine;
	@Autowired
	private EventFailure failure;
	@Autowired
	private EventFailureRepository failureRepo;

	@Override
	public boolean isState() {
		return false;
	}

	@Override
	public <T extends PayPayEvent> boolean process(T event) {

		boolean ret = false;
		try {

			ret = registerAccount(event.toString(), false, event.getObiquitusName());

			if (ret)
				sendMessage();

			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public <T extends PayPayEvent> boolean reProcess(T event) {

		boolean ret = false;
		try {

			ret = registerAccount(event.toString(), true, event.getObiquitusName());

			if (ret)
				sendMessage();

			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	private boolean registerAccount(final String userEventString, boolean isReprocess, String name) {

		System.out.print("Saving Account");

		try {
			boolean ret = fwEngine.saveAccount();

			if (!ret)
				handleError(userEventString, failure, failureRepo, name);

			return ret;
		} catch (Exception e) {
			// e.printStackTrace();
			if (!isReprocess)
				handleError(userEventString, failure, failureRepo, name);
			return false;
		}
	}

	private boolean sendMessage() {
		return false;
	}

	@Override
	public void handleError(String eventString, EventFailure failure, EventFailureRepository failureRepo,
			String failureName) {

		failure.setFailureEvent(eventString);
		failure.setEventSubscriber(this.getClass().getSimpleName());
		failure.setEventFailureId(Settings.aggregateTag());
		failure.setFailureName(failureName);
		failureRepo.save(failure);
	}

}
