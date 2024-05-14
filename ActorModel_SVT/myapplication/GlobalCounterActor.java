package myapplication;

import library.Actor;
import library.messages.Message;
import myapplication.messages.IncreaseMessage;
import myapplication.messages.PrintMessage;
import myapplication.messages.ResponseMessage;

public class GlobalCounterActor extends Actor {

    private int counter = 0;

    @Override
    public void processMessage(Message m) {
        if (m instanceof IncreaseMessage im) {
            counter += im.getQuantity();
            this.send(new ResponseMessage(), im.getReplyTo());
        } else if (m instanceof PrintMessage pm) {
            System.out.println("Counter:" + counter);
        }

    }

}
