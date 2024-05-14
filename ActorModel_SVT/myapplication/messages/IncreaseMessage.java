package myapplication.messages;

import library.Address;
import library.messages.Message;

public class IncreaseMessage extends Message {
    private int quantity;
    private Address replyTo;

    public IncreaseMessage(int qt, Address replyTo) {
        this.quantity = qt;
        this.replyTo = replyTo;
    }

    public int getQuantity() {
        return quantity;
    }

    public Address getReplyTo() {
        return replyTo;
    }
}
