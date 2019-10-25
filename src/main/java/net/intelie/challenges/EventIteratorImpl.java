package net.intelie.challenges;

import java.util.Iterator;
import java.util.List;

/**
 * An iterator over an event collection. This class implements EventIterator
 */
public class EventIteratorImpl implements EventIterator {

    private Iterator<Event> iterator = null;
    private List<Event> eventList = null;

    /**
     * Constructor
     *
     */
    public EventIteratorImpl(List<Event> list) {
        eventList = list;
    }

    public Iterator<Event> getIterator() {
        if (eventList == null)
            return null;
        else
            return eventList.iterator();
    }

    /**
     * Move the iterator to the next event, if any.
     *
     * @return false if the iterator has reached the end, true otherwise.
     */
    public boolean moveNext() {
        if (iterator == null)
            iterator = getIterator();

        if (iterator != null)
            return iterator.hasNext();
        else
            return false;
    }

    /**
     * Gets the current event ref'd by this iterator.
     *
     * @return the event itself.
     * @throws IllegalStateException if {@link #moveNext} was never called or its
     *                               last result was {@code false}.
     */
    public Event current() throws IllegalStateException {
        if (iterator == null)
            iterator = getIterator();

        if (iterator != null && iterator.hasNext())
            return iterator.next();
        else
            return null;
    }

    /**
     * Remove current event from its store.
     *
     * @throws IllegalStateException if {@link #moveNext} was never called or its
     *                               last result was {@code false}.
     */
    public void remove() throws IllegalStateException {
        if (iterator == null)
            iterator = getIterator();

        if (iterator != null && iterator.hasNext())
            iterator.remove();
    }

    /**
     * Reset itertor. Move the iterator to the first event.
     */
    public void reset() {
        iterator = eventList.iterator();
    }

    public void close() throws Exception {
        System.out.println(" From Close -  AutoCloseable  ");
    }
}