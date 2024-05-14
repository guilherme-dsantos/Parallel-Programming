package example;

import library.Address;
import library.Message;

public class WorkOnProblemMessage extends Message {
	private int value;
	private Address replyTo;
	
	public WorkOnProblemMessage(int i, Address address) {
		value = i;
		replyTo = address;
	}

	public int getValue() {
		return value;
	}

	public Address getReplyTo() {
		return replyTo;
	}

}
