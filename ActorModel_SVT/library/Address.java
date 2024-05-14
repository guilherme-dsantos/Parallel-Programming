package library;

import library.messages.Message;

@FunctionalInterface
public interface Address {
    public void receive(Message m);
}
