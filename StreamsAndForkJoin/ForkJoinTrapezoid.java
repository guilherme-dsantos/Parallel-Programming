import java.lang.Math;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTrapezoid extends RecursiveTask<Double> {

    private double x_max;
    private double x_min;

    public ForkJoinTrapezoid(double x_min, double x_max) {
        this.x_min = x_min;
        this.x_max = x_max;

    }

    public static double f(double x) {
        return x * (x - 1);
    }

    // calculate the area of the trapezoid
    public static double area(double x1, double x2) {
        double h1 = f(x1);
        double h2 = f(x2);
        return (x2 - x1) * (h1 + h2) / 2;
    }

    @Override
    protected Double compute() {

        if (RecursiveTask.getSurplusQueuedTaskCount() > 2)
            return area(x_min, x_max);

        double x_mid = (x_max + x_min) / 2;

        if (Math.abs(area(x_min, x_mid) + area(x_mid, x_max) - area(x_min, x_max)) < Math.pow(10, -7)) {
            return area(x_min, x_max);
        }

        ForkJoinTrapezoid f1 = new ForkJoinTrapezoid(x_min, x_mid);
        f1.fork();
        ForkJoinTrapezoid f2 = new ForkJoinTrapezoid(x_mid, x_max);
        f2.fork();

        double b = f2.join();
        double a = f1.join();

        return a + b;
    }

    public static void main(String[] args) {
        double x_min = 0;
        double x_max = 1000;

        // create a new class with the defined values
        ForkJoinTrapezoid f1 = new ForkJoinTrapezoid(x_min, x_max);
        f1.fork();
        // print out final value
        System.out.println(f1.join());
    }

}
