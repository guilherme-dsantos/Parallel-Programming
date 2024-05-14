
import myapplication.MainActor;
import myapplication.messages.StartMessage;
import library.Actor;

public class ActorModelApp {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        MainActor ma = new MainActor();
        Actor.sendFromMain(new StartMessage(), ma.getAddress());

    }
}
