package simulator;

public class StackMemory {

    private DataMemory memory;
    private int sp = 0x07;

    public StackMemory(DataMemory memory) {
        this.memory = memory;
    }

    public void push(int value) {

        if (sp >= 0xFF) {
            throw new IllegalStateException("Stack overflow");
        }

        sp++;
        memory.write(sp, value);
    }

    public int pop() {

        if (sp <= 0x07) {
            throw new IllegalStateException("Stack underflow");
        }

        int value = memory.read(sp);
        sp--;

        return value;
    }

    public int getSP() {
        return sp;
    }
    public void reset() {
    sp = 0x07;
}
}
