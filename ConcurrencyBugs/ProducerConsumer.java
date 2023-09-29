package bugs;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer {
	static class Producer extends Thread {
		private Queue<String> channel;

		public Producer(Queue<String> q) {
			channel = q;
		}
		public void run() {
			String[] args = "Pippin : We've had one, yes. What about second breakfast? Merry : I don't think he knows about second breakfast, Pip.".split(" ");
			for (String arg : args) {
				channel.add(arg);
			}
		}
		
	}
	
	static class Consumer extends Thread {
		private Queue<String> channel;
		
		public Consumer(Queue<String> q) {
			channel = q;
		} 
		
		public void run() {
			channel.forEach((String word) -> {
				System.out.print(word.toUpperCase() + " ");
			});
			System.out.println();
			System.out.println("done");			
		}
		
	}
	
	public static void main(String[] args) {
		Queue<String> q = new LinkedBlockingQueue<String>();
		Producer p = new Producer(q);
		Consumer c = new Consumer(q);
		
		p.start();
		c.start();
	}
}












/*
channel.forEach( (String word) -> {
	System.out.print(word.toUpperCase() + " ");
});
System.out.println();
System.out.println("done");
*/