import java.time.Duration;
import java.util.function.Function;

public class Demo {

    public static void benchmark(String name, Function<Runnable, Thread> threadCreator) throws InterruptedException {
        long time = System.nanoTime();
        int N = 100_000;
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep(Duration.ofSeconds(1L));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            threads[i] = threadCreator.apply(runnable);
            threads[i].start();
            // if (i % 1000 == 0) {
            // System.out.println("Created " + i + " threads");
            // }
        }
        for (Thread t : threads) {
            t.join();
        }

        long etime = System.nanoTime() - time;
        System.out.println(name + " took " + (etime / 1_000_000) + " miliseconds");
    }

    public static void main(String[] args) throws InterruptedException {
        benchmark("Threads", Thread::new);
        benchmark("Virtual Threads", Thread.ofVirtual()::unstarted);
    }

}
