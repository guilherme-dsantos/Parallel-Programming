
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class WorkStealingRuntime {

    private WorkStealingThread[] threads;

    private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<Runnable>();
    private AtomicInteger taskCounter = new AtomicInteger(0);

    public ConcurrentLinkedDeque<Runnable> getQueue() {
        return queue;
    }

    public WorkStealingRuntime(int i) {
        threads = new WorkStealingThread[i];
        start();
    }

    private void start() {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WorkStealingThread(this);
            threads[i].setName("WST #" + i);
            threads[i].start();
        }
    }

    public void submit(Runnable runnable) {
        taskCounter.incrementAndGet();
        queue.add(runnable);
    }

    public void join() throws InterruptedException {
        for (WorkStealingThread t : threads) {
            t.shutdown();
            t.join();
        }
    }

    public <T> WorkStealingFuture<T> fork(Supplier<T> computation) {
        WorkStealingFuture<T> f = new WorkStealingFuture<>();
        Runnable r = () -> {
            T v = computation.get();
            f.setValue(v);

        };
        Thread t = Thread.currentThread();
        if (t instanceof WorkStealingThread wst) {
            wst.getQueue().push(r);
        } else {
            queue.push(r);
        }
        return f;
    }

    public ConcurrentLinkedDeque<Runnable> getRandomQueue() {
        int index = ThreadLocalRandom.current().nextInt(threads.length);
        return threads[index].getQueue();
    }
}
