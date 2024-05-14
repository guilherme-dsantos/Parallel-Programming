
public class WorkStealingFuture<T> {
    protected T value;

    private volatile boolean done = false;

    public WorkStealingFuture() {

    }

    public T getValue() {
        while (!done) {
            if (Thread.currentThread() instanceof WorkStealingThread wst) {
                wst.innerLoop();
            }
        }
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        done = true;
    }

}
