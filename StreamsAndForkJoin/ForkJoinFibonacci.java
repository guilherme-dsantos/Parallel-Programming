import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinFibonacci extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;
    private int n;

    public ForkJoinFibonacci(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n == 0 || n == 1)
            return 1;

        ForkJoinFibonacci f1 = new ForkJoinFibonacci(n - 1);
        f1.fork();

        ForkJoinFibonacci f2 = new ForkJoinFibonacci(n - 2);
        f2.fork();

        int a = f1.join();
        int b = f2.join();

        return a + b;
    }

    // ------------------------------------------

    public static int seqFib(int n) {
        if (n == 0 || n == 1)
            return 1;
        return seqFib(n - 1) + seqFib(n - 2);
    }

    public static int parFib(int n) {
        ForkJoinFibonacci f2 = new ForkJoinFibonacci(n);
        return f2.compute();
    }

    public static void main(String[] args) {
        int sf = seqFib(47);
        System.out.println(sf);
        ForkJoinFibonacci pf = new ForkJoinFibonacci(47);
        ForkJoinPool pool = new ForkJoinPool(10);
        pool.invoke(pf);

        System.out.println(pf.join());
        pool.close();
    }

}
