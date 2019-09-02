package com.bext;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {  //message driven POJO

	@JmsListener(destination="mailbox", containerFactory="miFactory")
	public void receiveMessage(Email email) {
		System.out.println("Recivido <" + email +">");
	}
}
