# EventStore Implementation

This repository contains classes that implement EventStore read/write operations. 

* EventIterator: event iterator interface
* EventIteratoImpl: implementation of event iterator
* EventStore: event store interface
* EventStoreImpl: implementation of EventStore operations
* TestEventStore: program class to test EventStore operations (similar to junit test cases)
* EventTest.java: junit test cases 
* ThreadSafeTest: program class to use one thread to execute write operation while use another thread to execute read operations simultaneously

Note: 

To make the implementation thread-safe, synchronized ArrayList is created to store the Events.

There are two ways to create Synchronized ArrayList.
1. Using Collections.synchronizedList() method
2. Using CopyOnWriteArrayList

Option #1 has a limitation. All of its read and write methods are synchronized on the list object itself. That means if a thread is executing Write operation, it blocks other threads which want to get the iterator to access elements in the list. Also, only one thread can iterate the list’s elements at a time, which is inefficient. If lots of read operations are executed concurrently, the Read performance will be an issue.

Option #2 is designed to enable sequential write and concurrent reads features. For every write operation (i.e, insert, or removeAll) it makes a new copy of the elements in the list. That means the read operations (i.e, query) work on a different copy. But copying elements is costly. If many Write operations are performed, the approach will affect performance.

For the current implementation of the EventStore, I chose Option #2 (CopyOnWriteArrayList).
* Multiple threads executing read operations concurrently.
* Only one thread can execute write operation while other threads can execute read operations simultaneously.

A testing program (ThreadSafeTest.java) is implemented to demonstrate the above features by using one thread to execute write operation while using another thread to execute read operations simultaneously. 
