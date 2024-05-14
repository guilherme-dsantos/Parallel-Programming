package example;

import java.util.Random;

import library.Actor;
import library.Address;
import library.Message;

public class Employee extends Actor  {

	public Employee(Address address) {
		super(address);
	}

	@Override
	protected void handleMessage(Message m) {
		System.out.println("Employee " + this + " received " + m);
		
		
		if (m instanceof WorkOnProblemMessage) {
			WorkOnProblemMessage m2 = ((WorkOnProblemMessage) m);
			
			if (new Random().nextBoolean()) {
				System.out.println("Employee " + this + " crashed!");
				throw new MessageNotProcessedException(m2);
			}
			
			m2.getReplyTo().sendMessage(new ResponseMessage(m2.getValue() * 2));
		}
		
	}

}
