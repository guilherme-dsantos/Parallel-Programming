package example;

import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;

public class Customer extends Actor  {

	private static final int NUMBER_OF_MESSAGES = 10;
	
	private Address serverAddress;
	private int messagesReceived = 0;
	
	public Customer(Address address) {
		serverAddress = address;
	}

	@Override
	protected void handleMessage(Message m) {
		if (m instanceof BootstrapMessage) {
			System.out.println("Starting Customer");
			for (int i=0;i<NUMBER_OF_MESSAGES;i++) {
				serverAddress.sendMessage(new WorkOnProblemMessage(i, this.getAddress()));
			}
		} else if (m instanceof ResponseMessage) {
			ResponseMessage m2 = (ResponseMessage) m;
			System.out.println("Customer received " + m2.getValue());
			messagesReceived++;
			if (messagesReceived == NUMBER_OF_MESSAGES) {
				serverAddress.sendMessage(new SystemKillMessage());
				this.getAddress().sendMessage(new SystemKillMessage());
			}
		}
	}


	

}
