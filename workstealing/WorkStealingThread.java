
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WorkStealingThread extends Thread {
    private WorkStealingRuntime runtime;
    private ConcurrentLinkedDeque<Runnable> deque = new ConcurrentLinkedDeque<>();
    private volatile boolean alive = false;

    public WorkStealingThread(WorkStealingRuntime runtime) {
        this.runtime = runtime;
    }

    public void innerLoop() {
        while (!deque.isEmpty() || !runtime.getQueue().isEmpty()) {
            // First, let us address current queues:

            try {
                // head -> [ t5, t4, t3 ] <- tail
                Runnable t = this.deque.pop();
                t.run();
            } catch (NoSuchElementException e) {
                // Nothing
            }

            // Then, global queue
            try {
                Runnable r = runtime.getQueue().pop();
                r.run();
            } catch (NoSuchElementException ane) {
                // nothing
            }

            // Then, other queues:

            try {
                Runnable r = runtime.getRandomQueue().removeLast();
                System.out.println("STOLEN!");
                r.run();
            } catch (NoSuchElementException ane) {
                // nothing
            }

        }
    }

    public void run() {
        alive = true;
        while (alive) {
            innerLoop();
        }
    }

    public void shutdown() {
        alive = false;

    }

    public ConcurrentLinkedDeque<Runnable> getQueue() {
        return deque;
    }

    public WorkStealingRuntime getRuntime() {
        return runtime;
    }
}