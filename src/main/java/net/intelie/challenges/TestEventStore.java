package net.intelie.challenges;

public class TestEventStore {
    public static void main(String[] args) {

        EventStore eventStore = new EventStoreImpl();

        // Insert 10 'TypeA' events with timestamps between 100L to 190L
        for (int i = 0; i < 10; i++)
            eventStore.insert(new Event("TypeA", 100L + i * 10));

        // Insert 5 'TypeB' events with timestamps 120L, 140L, 160L, 180L, 200L
        for (int i = 0; i < 5; i++)
            eventStore.insert(new Event("TypeB", 120L + i * 20));

        // Insert 3 'TypeC' events with timestamps 180L, 210L, 240L
        for (int i = 0; i < 3; i++)
            eventStore.insert(new Event("TypeC", 180L + i * 30));

        // Get the events with timestamps between 100 to 250
        // It should return all 18 events back
        EventIterator eventIterator1 = eventStore.query(100L, 250L);
        System.out.println("It should be 18: " + getSize(eventIterator1));

        // Get 'TypeA' events with timestamps between 115 to 160
        // It should return 4 'TypeA' events with timestamps 120, 130, 140, 150 (160
        // excluded)
        EventIterator eventIterator2 = eventStore.query("TypeA", 115L, 160L);
        System.out.println("It should be 4: " + getSize(eventIterator2));

        // Get 'TypeB' events with timestamps between 140 to 190
        // It should return 3 'TypeB' events with timestamps 140 (included), 160, 180
        EventIterator eventIterator3 = eventStore.query("TypeB", 140L, 190L);
        System.out.println("It should be 3: " + getSize(eventIterator3));

        // Get 'TypeC' events with timestamps between 190 to 220
        // It should return 1 'TypeC' events with timestamp 210
        EventIterator eventIterator4 = eventStore.query("TypeC", 190L, 220L);
        System.out.println("It should be 210: " + eventIterator4.current().timestamp());
        // the above current() reads the current event and moves its cursor to the next
        // since only 1 event is found, there is no next event, the size should be 0
        System.out.println("It should be 0: " + getSize(eventIterator4));

        // Remove all 'TypeA' events
        eventStore.removeAll("TypeA");

        // Get the events with timestamps between 100 to 250 again
        // It should return 8 events back (5 TypeB events and 3 TypeC events)
        EventIterator eventIterator5 = eventStore.query(100L, 250L);
        System.out.println("It should be 8: " + getSize(eventIterator5));
    }

    private static int getSize(EventIterator iterator) {
        int size = 0;
        while (iterator.moveNext()) {
            iterator.current();
            size++;
        }

        return size;
    }
}
