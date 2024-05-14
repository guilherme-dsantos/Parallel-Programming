package example;

public class Main {
	public static void main(String[] args) {
		
			
		
		Manager alice = new Manager();
		
		Customer c = new Customer(alice.getAddress());
		
		c.getAddress().sendMessage(new BootstrapMessage());
		// The Customer decides when to shutdown everything.
		
	}
}
