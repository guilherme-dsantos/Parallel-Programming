public class Waiter extends Thread {

	public static void main(String[] args) {
		Waiter w = new Waiter();
		w.start();
		new Enlightment(w).start();
	}

	private volatile boolean happy = false;

	@Override
	public void run() {
		// beHappy();
		meditate();
	}

	public synchronized void meditate() {
		while (!happy) {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}
		System.out.println("Zen achieved");
	}

	public void beHappy() {
		while (!happy) {
		}
		System.out.println("Clap along if you feel like a room without a roof");
	}

	static class Enlightment extends Thread {

		private Waiter w;

		public Enlightment(Waiter w) {
			this.w = w;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			w.happy = true;
			synchronized (w) {
				w.notifyAll();
			}
			System.out.println("Produced happiness");
		}
	}

}

/*
 * public void meditate() {
 * while(!happy) {
 * try {
 * this.wait();
 * } catch (InterruptedException e) {}
 * }
 * System.out.println("Zen achieved");
 * }
 * 
 * synchronized
 * 
 * static class Enlightment extends Thread {
 * 
 * private Waiter w;
 * 
 * public Enlightment(Waiter w) { this.w = w; }
 * 
 * @Override
 * public void run() {
 * try {
 * Thread.sleep(3000);
 * } catch (InterruptedException e) {
 * e.printStackTrace();
 * }
 * w.happy = true;
 * synchronized (w) {
 * w.notifyAll();
 * }
 * System.out.println("Produced happiness");
 * }
 * }
 * 
 * new Enlightment(w).start();
 */
