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
   public void displayStack() {

        System.out.println("----- STACK -----");

        System.out.println("SP = " +
                Integer.toHexString(sp).toUpperCase());

        if (sp == 0x07) {
            System.out.println("Stack is empty.");
            return;
        }

        for (int i = sp; i > 0x07; i--) {

            System.out.println("Address " +
                    Integer.toHexString(i).toUpperCase() +
                    " = " +
                    Integer.toHexString(memory.read(i)).toUpperCase());
        }
    }
}
