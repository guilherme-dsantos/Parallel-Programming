import java.util.concurrent.RecursiveTask;

public class ForkJoinFibonacciGC extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;
    private int n;
    private int depth;

    public ForkJoinFibonacciGC(int n, int depth) {
        this.n = n;
        this.depth = depth;
    }

    @Override
    protected Integer compute() {
        if (n == 0 || n == 1)
            return 1;

        // Specific for fibonacci
        // if (n <= 16) return seqFib(n);

        // Max level:
        // if ( depth >= 20 ) return seqFib(n);

        // Max tasks: if the total number of tasks >= T * #cores.
        // if ( this.getQueuedTaskCount() > 4 *
        // Runtime.getRuntime().availableProcessors() ) return seqFib(n);

        // Surplus: if the current queue has more than 2 tasks than the average
        if (RecursiveTask.getSurplusQueuedTaskCount() > 25)
            return seqFib(n);

        ForkJoinFibonacciGC f1 = new ForkJoinFibonacciGC(n - 1, depth + 1);
        f1.fork();

        ForkJoinFibonacciGC f2 = new ForkJoinFibonacciGC(n - 2, depth + 1);
        // f2.fork();

        int b = f2.compute();
        int a = f1.join();

        return a + b;
    }

    // ------------------------------------------

    public static int seqFib(int n) {
        if (n == 0 || n == 1)
            return 1;
        return seqFib(n - 1) + seqFib(n - 2);
    }

    /*
     * public static int parFib(int n) {
     * ForkJoinFibonacciGC f2 = new ForkJoinFibonacciGC(n, 0);
     * return f2.compute();
     * }
     */

    public static void main(String[] args) {
        int sf = seqFib(47);
        System.out.println(sf);
        ForkJoinFibonacciGC pf = new ForkJoinFibonacciGC(47, 0);

        /*
         * ForkJoinPool pool = new
         * ForkJoinPool(Runtime.getRuntime().availableProcessors());
         * pool.invoke(pf);
         */
        pf.fork();

        System.out.println(pf.join());
    }

}
