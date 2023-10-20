import java.lang.Math;

public class Trapezoid {

  final static double STEP = Math.pow(10, -7);
  final static double X_MIN = 0;
  final static double X_MAX = 1000;

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

    return integral;
  }

  /**************** main ****************/
  public static void main(String[] args) {

    double integral = Trapezoid.trapezoid(STEP, X_MIN, X_MAX);
    System.out.println(integral);
  }
}
