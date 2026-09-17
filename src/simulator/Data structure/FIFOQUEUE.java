package simulator;

public class FIFOQueue {
    private int[] queue;
    private int front;
    private int rear;
    private int count;

    // Constructor
    public FIFOQueue(int size) {
        queue = new int[size];
        front = 0;
        rear = -1;
        count = 0;
    }

    // ENQUEUE
    public void enqueue(int value) {

        if (count == queue.length) {
            System.out.println("Queue Full");
            return;
        }

        rear = (rear + 1) % queue.length;
        queue[rear] = value;
        count++;

        System.out.println("Enqueued: " + value);
    }

    // DEQUEUE
    public int dequeue() {

        if (count == 0) {
            System.out.println("Queue Empty");
            return -1;
        }

        int value = queue[front];

        front = (front + 1) % queue.length;
        count--;


        return value;
    }

    // DISPLAY QUEUE
    public void display() {

        if (count == 0) {
            System.out.println("Queue Empty");
            return;
        }

        System.out.println("Queue:");

        for (int i = 0; i < count; i++) {

            int index = (front + i) % queue.length;

            System.out.print(queue[index] + " ");
        }

        System.out.println();
    }

    // Check status
    public void status() {

        if (count == 0) {
            System.out.println("Queue is Empty");
        } 
        else if (count == queue.length) {
            System.out.println("Queue is Full");
        } 
        else {
            System.out.println("Queue has " + count + " elements");
        }
    }
    public int getCount() {
    return count;
}
public int[] getQueueValues() {
    int[] values = new int[count];

    for (int i = 0; i < count; i++) {
        int index = (front + i) % queue.length;
        values[i] = queue[index];
    }

    return values;
}
public void reset() {
    front = 0;
    rear = -1;
    count = 0;
}
    
}
