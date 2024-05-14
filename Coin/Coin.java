import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Coin extends RecursiveTask<Integer> {

	public static final int LIMIT = 999;

	private int[] coins;
	private int index;
	private int accumulator;

	public Coin(int[] coins, int index, int accumulator) {
		this.coins = coins;
		this.index = index;
		this.accumulator = accumulator;
	}

	public static int[] createRandomCoinSet(int N) {
		int[] r = new int[N];
		for (int i = 0; i < N; i++) {
			if (i % 10 == 0) {
				r[i] = 400;
			} else {
				r[i] = 4;
			}
		}
		return r;
	}

	public static void main(String[] args) {
		int nCores = Runtime.getRuntime().availableProcessors();

		int[] coins = createRandomCoinSet(30);

		int repeats = 40;
		for (int i = 0; i < repeats; i++) {
			long seqInitialTime = System.nanoTime();
			int rs = seq(coins, 0, 0);
			long seqEndTime = System.nanoTime() - seqInitialTime;
			System.out.println(nCores + ";Sequential;" + seqEndTime);

			long parInitialTime = System.nanoTime();
			Coin c = new Coin(coins, 0, 0);
			ForkJoinPool pool = new ForkJoinPool(24);
			pool.invoke(c);
			int rp = c.join();
			long parEndTime = System.nanoTime() - parInitialTime;
			System.out.println(nCores + ";Parallel;" + parEndTime);
			pool.close();
			if (rp != rs) {
				System.out.println("Wrong Result!");
				System.exit(-1);
			}
		}

	}

	private static int seq(int[] coins, int index, int accumulator) {

		if (index >= coins.length) {
			if (accumulator < LIMIT) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > LIMIT) {
			return -1;
		}
		int a = seq(coins, index + 1, accumulator);
		int b = seq(coins, index + 1, accumulator + coins[index]);
		return Math.max(a, b);
	}

	@Override
	protected Integer compute() {

		if (index >= coins.length) {
			if (accumulator < LIMIT) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > LIMIT) {
			return -1;
		}

		// if (RecursiveTask.getSurplusQueuedTaskCount() > 2)
		// return seq(coins, index, accumulator);

		if (index >= coins.length / 2)
			return seq(coins, index, accumulator);

		Coin c1 = new Coin(coins, index + 1, accumulator);
		c1.fork();
		Coin c2 = new Coin(coins, index + 1, accumulator + coins[index]);

		int b = c2.compute();
		int a = c1.join();

		return Math.max(a, b);
	}
}
