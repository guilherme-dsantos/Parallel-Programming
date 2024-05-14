package myapplication;

import library.Actor;
import library.Address;
import library.messages.Message;
import myapplication.messages.IncreaseMessage;
import myapplication.messages.StartMessage;

public class ClientActor extends Actor {

    private Address globalCounterAddress;

    public ClientActor(Address globalCounterAddress) {
        this.globalCounterAddress = globalCounterAddress;
    }

    @Override
    public void processMessage(Message m) {
        if (m instanceof StartMessage) {
            // smth, smth
            this.send(new IncreaseMessage(1, m.getSenderAddress()), globalCounterAddress);
        }

    }

}