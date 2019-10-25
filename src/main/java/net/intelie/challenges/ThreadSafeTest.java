
package net.intelie.challenges;

/**
 *
 * This program demonstrates EventStore read/write operation is thread safe.
 *
 */
public class ThreadSafeTest {

    public static void main(String[] args) {

        EventStore eventStore = new EventStoreImpl();

        new WriteThread("Writer", eventStore).start();

        new ReadThread("Reader", eventStore).start();

    }
}

class WriteThread extends Thread {

    private EventStore eventStore;

    public WriteThread(String name, EventStore store) {
        this.eventStore = store;
        super.setName(name);
    }

    public void run() {
        int count = 1;

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            Event event = new Event("Type" + count, 100L + count);
            eventStore.insert(event);
            count++;

            System.out.println(super.getName() + ": " + event.type() + ", " + event.timestamp() + "\n");
        }
    }
}

class ReadThread extends Thread {
    private EventStore eventStore;

    public ReadThread(String name, EventStore eventStore) {
        this.eventStore = eventStore;
        super.setName(name);
    }

    public void run() {

        while (true) {

            EventIterator eventIterator = eventStore.query(100L, 200L);

            while (eventIterator.moveNext()) {
                Event event = eventIterator.current();
                try {

                    Thread.sleep(100);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                System.out.println(super.getName() + ": " + event.type() + ", " + event.timestamp() + "\n");
            }
        }
    }
}
