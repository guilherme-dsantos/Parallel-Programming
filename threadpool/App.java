
public class App {
    public static void main(String[] args) throws Exception {
        MyThreadPool p = new MyThreadPool(4);

        int N = 10;
        int[] results = new int[N];

        for (int i = 0; i < N; i++) {
            final int i2 = i;
            p.submit(() -> {
                results[i2] = fib(i2 * 5);
                System.out.println("[DEBUG] Computed: " + i2 + " in " + Thread.currentThread().getName());
            });
        }

        p.join();

        for (int i = 0; i < N; i++) {
            System.out.println(i + ": " + results[i]);
        }
    }

    public static int fib(int i) {
        if (i <= 1)
            return 1;
        return App.fib(i - 1) + App.fib(i - 2);
    }

}
