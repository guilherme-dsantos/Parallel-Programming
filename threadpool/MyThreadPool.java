
import java.util.concurrent.ConcurrentLinkedDeque;

public class MyThreadPool {

    private MyThread[] threads;

    private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<Runnable>();

    public MyThreadPool(int i) {
        threads = new MyThread[i];
        start();
    }

    private void start() {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThread(queue);
            threads[i].start();
        }
    }

    public void submit(Runnable runnable) {
        queue.add(runnable);
    }

    public void join() throws InterruptedException {
        for (MyThread t : threads) {
            t.shutdown();
            t.join();
        }
    }

}
