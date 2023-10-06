import java.util.concurrent.Phaser;

public class ParallelHeat {
	public static final int NX = 2048;
	public static final int NY = 2048;
	public static final int ITERATIONS = 50;
	private double[][] oldm = new double[NX][NY];
	private double[][] newm = new double[NX][NY];
	int nCores = 8;
	Thread[] threads = new Thread[nCores];
	int chunkSize = NX / nCores;

	public void run() {
		final Phaser phaser = new Phaser() {
			protected boolean onAdvance(int phase, int registeredParties) {
				// Swap the matrices
				double[][] temp = oldm;
				oldm = newm;
				newm = temp;
				return phase >= ITERATIONS || registeredParties == 0;
			}
		};

		// Everything else is 0
		oldm[NX / 2][NY / 2] = 100000;

		phaser.register();

		for (int ti = 0; ti < nCores; ti++) {
			int start = chunkSize * ti + 1;
			int end = (ti < nCores - 1) ? chunkSize * (ti + 1) : NX - 1;

			phaser.register();

			threads[ti] = new Thread(() -> {

				do {
					double[][] t_oldm = oldm;
					double[][] t_newm = newm;
					for (int i = start; i < end; i++) {
						for (int j = 1; j < NY - 1; j++) {

							t_newm[i][j] = (t_oldm[i][j] + t_oldm[i - 1][j] + t_oldm[i + 1][j] + t_oldm[i][j - 1]
									+ t_oldm[i][j + 1])
									/ 5;
						}
					}
					phaser.arriveAndAwaitAdvance();
				} while (!phaser.isTerminated());
			});
		}

		for (int i = 0; i < nCores; i++) {
			threads[i].start();
		}

		phaser.arriveAndDeregister();

		for (int i = 0; i < nCores; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		System.out.println(newm[NX / 2 - 10][NY / 2 - 10]); // ~4.9365
	}

	public static void main(String[] args) {
		ParallelHeat ph = new ParallelHeat();
		ph.run();
	}
}
