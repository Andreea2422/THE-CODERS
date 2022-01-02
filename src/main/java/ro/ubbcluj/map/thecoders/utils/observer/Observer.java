package ro.ubbcluj.map.thecoders.utils.observer;


import ro.ubbcluj.map.thecoders.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}