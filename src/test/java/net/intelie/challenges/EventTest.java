package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTest {

    @Test
    public void TestInsert() {

        EventStore eventStore = new EventStoreImpl();

        // Insert 10 'TypeA' events with timestamps between 100 to 190
        for (int i = 0; i < 10; i++)
            eventStore.insert(new Event("TypeA", 100L + i * 10));

        // Insert 5 'TypeB' events with timestamps 120, 140, 160, 180, 200
        for (int i = 0; i < 5; i++)
            eventStore.insert(new Event("TypeB", 120L + i * 20));

        // Insert 3 'TypeC' events with timestamps 180, 210, 240
        for (int i = 0; i < 3; i++)
            eventStore.insert(new Event("TypeC", 180L + i * 30));

        // Retrieve the events with timestamps between 100 to 250
        // It should return all 18 events
        EventIterator eventIterator = eventStore.query(100L, 250L);
        assertEquals(18, getSize(eventIterator));
    }

    @Test
    public void TestQuery() throws Exception {

        EventStore eventStore = new EventStoreImpl();

        // Insert 10 'TypeA' events with timestamps between 100 to 190
        for (int i = 0; i < 10; i++)
            eventStore.insert(new Event("TypeA", 100L + i * 10));

        // Insert 5 'TypeB' events with timestamps 120, 140, 160, 180, 200
        for (int i = 0; i < 5; i++)
            eventStore.insert(new Event("TypeB", 120L + i * 20));

        // Insert 3 'TypeC' events with timestamps 180, 210, 240
        for (int i = 0; i < 3; i++)
            eventStore.insert(new Event("TypeC", 180L + i * 30));

        // Get 'TypeA' events with timestamps between 115 to 160
        // It should return 4 'TypeA' events with timestamps 120, 130, 140, 150
        // (160 excluded)
        EventIterator eventIterator1 = eventStore.query("TypeA", 115L, 160L);
        assertEquals(4, getSize(eventIterator1));

        // Get 'TypeB' events with timestamps between 140 to 190
        // It should return 3 'TypeB' events with timestamps 140 (included), 160, 180
        EventIterator eventIterator2 = eventStore.query("TypeB", 140L, 190L);
        assertEquals(3, getSize(eventIterator2));

        // Get 'TypeC' events with timestamps between 190 to 220
        // It should return 1 'TypeC' events with timestamp 210
        EventIterator eventIterator3 = eventStore.query("TypeC", 190L, 220L);
        // the timestamp is 210
        assertEquals(210, eventIterator3.current().timestamp());
        // the above current() reads the current event and moves its cursor to the next
        // since only 1 event is found, there is no next event, the size should be 0
        assertEquals(0, getSize(eventIterator3));
    }

    @Test
    public void TestRemoveAll() throws Exception {

        EventStore eventStore = new EventStoreImpl();

        // Insert 10 'TypeA' events with timestamps between 100 to 190
        for (int i = 0; i < 10; i++)
            eventStore.insert(new Event("TypeA", 100L + i * 10));

        // Insert 5 'TypeB' events with timestamps 120, 140, 160, 180, 200
        for (int i = 0; i < 5; i++)
            eventStore.insert(new Event("TypeB", 120L + i * 20));

        // Insert 3 'TypeC' events with timestamps 180, 210, 240
        for (int i = 0; i < 3; i++)
            eventStore.insert(new Event("TypeC", 180L + i * 30));

        // Remove all 'TypeA' events
        eventStore.removeAll("TypeA");

        // Retrieve the events with timestamps between 100 to 250 again
        // It should return 8 events back (5 TypeB events and 3 TypeC events)
        EventIterator eventIterator5 = eventStore.query(100L, 250L);
        assertEquals(8, getSize(eventIterator5));
    }

    private int getSize(EventIterator iterator) {
        int size = 0;
        while (iterator.moveNext()) {
            iterator.current();
            size++;
        }
        return size;
    }
}
