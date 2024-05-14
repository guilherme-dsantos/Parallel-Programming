package myapplication;

import library.Actor;
import library.messages.Message;
import library.messages.StopMessage;
import myapplication.messages.PrintMessage;
import myapplication.messages.ResponseMessage;
import myapplication.messages.StartMessage;

public class MainActor extends Actor {
    private Actor gc = launchActor(new GlobalCounterActor());
    private Actor a1 = launchActor(new ClientActor(gc.getAddress()));
    private Actor a2 = launchActor(new ClientActor(gc.getAddress()));
    private int responsesReceived = 0;

    @Override
    public void processMessage(Message m) {
        if (m instanceof StartMessage) {
            this.send(new StartMessage(), a1.getAddress());
            this.send(new StartMessage(), a2.getAddress());
        } else if (m instanceof ResponseMessage) {
            responsesReceived++;
            if (responsesReceived == 2) {
                this.send(new PrintMessage(), gc.getAddress());
                this.send(new StopMessage(), this.getAddress());
            }
        }

    }
}
