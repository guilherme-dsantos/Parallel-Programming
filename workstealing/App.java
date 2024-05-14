
public class App {
    public static void main(String[] args) throws Exception {
        WorkStealingRuntime wsr = new WorkStealingRuntime(4);

        int n = 49;

        System.out.println("Fib(" + n + ") = " + fibTask(n, wsr));

        wsr.join();
    }

    public static int fib(int i) {
        if (i <= 1)
            return 1;
        return App.fib(i - 1) + App.fib(i - 2);
    }

    private static int fibTask(int n, final WorkStealingRuntime wsr) {
        System.out.println("Computing fib(" + n + ") on thread + " + Thread.currentThread().getName());
        if (n <= 1)
            return 1;
        else if (n <= 16) {
            return App.fib(n);
        } else {
            WorkStealingFuture<Integer> f1 = wsr.fork(() -> fibTask(n - 1, wsr));
            WorkStealingFuture<Integer> f2 = wsr.fork(() -> fibTask(n - 2, wsr));
            return f1.getValue() + f2.getValue();
        }

    }

}
