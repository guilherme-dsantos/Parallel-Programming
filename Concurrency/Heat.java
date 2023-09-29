import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class Heat {
	public static final int NX = 2048;
	public static final int NY = 2048;
	public static final int ITERATIONS = 50;

	static void startTasks(List<Runnable> tasks, final int iterations) {

		final Phaser phaser = new Phaser() {
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= iterations || registeredParties == 0;
			}
		};

		 
		phaser.register(); // register for the first barrier with parties = 1
		for (final Runnable task : tasks) {
			phaser.register();
			new Thread() {
				public void run() {
					do {
						task.run();
						phaser.arriveAndAwaitAdvance();
					} while (!phaser.isTerminated());
				}
			}.start();
		}
		phaser.arriveAndDeregister(); // releases the first barrier
		
	}

	public static void main(String[] args) {
		double[][] oldm = new double[NX][NY];
		double[][] newm = new double[NX][NY];

		// Everything else is 0
		oldm[NX / 2][NY / 2] = 100000;

		List<Runnable> l = new ArrayList<>();
		l.add(() -> {
			double[][] thread_oldm = oldm;
			double[][] thread_newm = newm;
			for (int timestep = 0; timestep <= ITERATIONS; timestep++) {
				for (int i = 1; i < NX - 1; i++) {
					for (int j = 1; j < NY - 1; j++) {
						thread_newm[i][j] = (thread_oldm[i][j] + thread_oldm[i - 1][j] + thread_oldm[i + 1][j] + thread_oldm[i][j - 1] + thread_oldm[i][j + 1])
								/ 5;
					}
				}
				// Swap matrices
				double[][] tmp = thread_newm;
        		thread_newm = thread_oldm;
        		thread_oldm = tmp;
			}
		});

		startTasks(l,ITERATIONS);
		
		System.out.println(newm[NX / 2 - 10][NY / 2 - 10]); // ~3.77

	}
}