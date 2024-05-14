package example;

import library.Message;

public class ResponseMessage extends Message {
	private int value;
	
	public ResponseMessage(int i) {
		value = i;
	}

	public int getValue() {
		return value;
	}
}
