import java.lang.Math;

public class ParallelTrapezoid {

  final static double STEP = Math.pow(10, -7);
  final static double X_MIN = 0;
  final static double X_MAX = 1;

  // arbitrary function to integrate
  public static double f(double x) {
    return x * (x - 1);
  }

  // calculate the area of the trapezoid
  public static double area(double step, double h1, double h2) {
    return step * (h1 + h2) / 2;
  }

  public static double trapezoid(double step, double x_min, double x_max) {
    // dynamic values
    double x_1 = x_min;
    double x_2 = x_min + step;
    double integral = 0;

    // begin the integration
    while (x_2 < x_max) {
      integral += area(step, f(x_1), f(x_2));
      x_1 += step;
      x_2 += step;
    }

    // integrate the last bit ignoring step size
    double diff = x_max - x_1;
    integral += area(diff, f(x_1), f(x_max));

    // return final value
    return integral;
  }

  /**************** main ****************/
  public static void main(String[] args) {

    int nCores = Runtime.getRuntime().availableProcessors();
    Thread[] threads = new Thread[nCores];
    double[] results = new double[nCores];
    double chunckSize = (X_MAX - X_MIN) / nCores;
    for (int i = 0; i < nCores; i++) {
      final int index = i;
      double start = X_MIN + chunckSize * i;
      double end = (i < nCores - 1) ? (i + 1) * chunckSize : X_MAX;
      threads[i] = new Thread(() -> {
        results[index] += ParallelTrapezoid.trapezoid(STEP, start, end);
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

    double integral = 0;
    for (int i = 0; i < nCores; i++) {
      integral += results[i];
    }

    System.out.println(integral);
  }
}
