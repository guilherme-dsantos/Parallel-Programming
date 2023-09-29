package bugs;
public class Bug1 {
	
	private int a = 0;
	//private Object o = new Object();
	
	public void up() {
		a++;
	}

	public void down() {
		a--;
	}
	
	public static void main(String[] args) {
		Bug1 b = new Bug1();
		
		Thread t1 = new Thread() {
			public void run() {
				synchronized (b) {
					for (int i=0; i < 5000; i++) {					
						b.up();
					}
				}
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				synchronized (b) {
					for (int i=0; i < 5000; i++) {
						b.down();
					}
				}
			}
		};
		
		
		t2.start();
		t1.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(b.a);
	}
	
}
