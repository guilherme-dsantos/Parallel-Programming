import java.lang.Math;

public class ParallelDarts {

  static final int N = 100000000;

  // rand function
  public static double rand(double min, double max) {
    return (max - min) * Math.random() + min;
  }

  /**************** main ****************/
  public static void main(String[] args) {

    // radius of circle - automatic sets bounds for random number
    int radius = 1;
    int min = -radius;
    int max = radius;

    int nCores = Runtime.getRuntime().availableProcessors();
    Thread[] threads = new Thread[nCores];
    int chunckSize = N / nCores;
    int[] results = new int[nCores];

    for (int i = 0; i < nCores; i++) {
      int start = chunckSize * i;
      int end = (i < nCores - 1) ? (i + 1) * chunckSize : N;
      results[i] = 0;
      final int index = i;
      threads[i] = new Thread(() -> {
        for (int j = start; j < end; j++) {
          double x = rand(min, max);
          double y = rand(min, max);
          if (x * x + y * y < radius) {
            results[index]++;
          }
        }
      });
      threads[i].start();
    }

    for (int i = 0; i < nCores; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    double counter_circle = 0;
    for (int i = 0; i < nCores; i++) {
      counter_circle += results[i];
    }

    System.out.println(counter_circle);

    // print estimate of π
    double pi = 4 * (counter_circle / N);
    System.out.println(pi);
  }
}