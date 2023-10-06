import java.lang.Math;

public class Darts {
  // rand function
  public static double rand(double min, double max) {
    return (max - min) * Math.random() + min;
  }

  /**************** main ****************/
  public static void main(String[] args) {
    // number of numbers generated
    int N = 1000000;

    // radius of circle - automatic sets bounds for random number
    int radius = 1;
    int min = -radius;
    int max = radius;

    // number of dots in circle
    double counter_circle = 0;

    // generate N random numbers
    for (int i = 0; i < N; i++) {
      double x = rand(min, max);
      double y = rand(min, max);

      // check if they fell inside circle
      if (x * x + y * y < radius) {
        counter_circle += 1;
      }
    }

    // print estimate of π
    double pi = 4 * (counter_circle / N);
    System.out.println(pi);
  }
}
