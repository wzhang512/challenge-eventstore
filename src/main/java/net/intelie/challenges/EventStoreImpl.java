package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventStoreImpl implements EventStore {

    private List<Event> eventList = null;
    private EventIterator eventIterator = null;

    /**
     * Constructor
     */
    public EventStoreImpl() {
        // create thread-safe event list
        eventList = new CopyOnWriteArrayList<Event>();
        eventIterator = new EventIteratorImpl(eventList);
    }

    /**
     * Stores an event
     *
     * @param event
     */
    public void insert(Event event) {
        eventList.add(event);
    }

    /**
     * Removes all events of specific type.
     *
     * @param type
     */
    public void removeAll(String type) {
        try {
            while (eventIterator.moveNext()) {
                Event event = eventIterator.current();
                if (event.type().equals(type))
                    eventList.remove(event);
            }
            eventIterator.reset();
        } catch (Exception ex) {
            System.out.println("removeAll failed. Reason: " + ex.getMessage());
        }
    }

    /**
     * Retrieves an iterator for events based on their type and timestamp.
     *
     * @param type      The type we are querying for.
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return An iterator where all its events have same type as {@param type} and
     *         timestamp between {@param startTime} (inclusive) and {@param endTime}
     *         (exclusive).
     */
    public EventIterator query(String type, long startTime, long endTime) {
        try {
            ArrayList<Event> queryResultList = new ArrayList<Event>();
            while (eventIterator.moveNext()) {
                Event event = eventIterator.current();
                if (event.type().equals(type) && event.timestamp() >= startTime && event.timestamp() < endTime) {
                    queryResultList.add(event);
                }
            }
            eventIterator.reset();
            return new EventIteratorImpl(queryResultList);
        } catch (Exception ex) {
            System.out.println("query (type) failed. reason: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Retrieves an iterator for events based on their type and timestamp.
     *
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return An iterator where all its events timestamp between {@param startTime}
     *         (inclusive) and {@param endTime} (exclusive).
     */
    public EventIterator query(long startTime, long endTime) {
        try {
            ArrayList<Event> queryResultList = new ArrayList<Event>();
            while (eventIterator.moveNext()) {
                Event event = eventIterator.current();
                if (event.timestamp() >= startTime && event.timestamp() < endTime) {
                    queryResultList.add(event);
                }
            }
            eventIterator.reset();
            return new EventIteratorImpl(queryResultList);
        } catch (Exception ex) {
            System.out.println("query() failed. Reason: " + ex.getMessage());
            return null;
        }
    }

}