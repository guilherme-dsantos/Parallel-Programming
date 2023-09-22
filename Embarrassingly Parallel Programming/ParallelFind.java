public class ParallelFind {

	public static void main(String[] args) {

		int[] haystack = new int[] { 10, 20, 30, 40, 50, 55, 56, 58, 123, 32, 43, 12, 54, 21, 11 };

		int nCores = 4;
		int chunkSize = haystack.length / nCores; // dividir o array em partes iguais para cada core

		int[] results = new int[nCores]; // quantos resultados cada thread encontra
		Thread[] ts = new Thread[nCores]; // paralelismo
		for (int i = 0; i < nCores; i++) {
			int start = i * chunkSize;
			int end = (i < nCores - 1) ? (i + 1) * chunkSize : haystack.length;

			results[i] = 0;
			final int index = i;
			ts[i] = new Thread(() -> {
				for (int j = start; j < end; j++) {

					if (haystack[j] % 3 == 0) {
						System.out.println("Found " + haystack[j]);
						results[index]++;
					}

				}

			});
			ts[i].start();
		}

		for (Thread t : ts) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int sum = 0;
		for (int i : results) {
			sum += i;
		}

		System.out.println("Right: I found " + sum + " elements.");

	}
}