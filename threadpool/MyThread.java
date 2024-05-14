
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MyThread extends Thread {
    private ConcurrentLinkedDeque<Runnable> queue;
    private volatile boolean alive = false;

    public MyThread(ConcurrentLinkedDeque<Runnable> queue) {
        this.queue = queue;
    }

    public void run() {
        System.out.println("Started: " + this.getName());
        alive = true;
        while (alive || !queue.isEmpty()) {
            try {
                Runnable r = queue.pop();
                r.run();
            } catch (NoSuchElementException e) {
                // nothing
            }
        }
        System.out.println("Stopped: " + this.getName());

    }

    public void shutdown() {
        alive = false;
    }
}
