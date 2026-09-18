package simulator;

public class Week3Test {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("     STC89C52 WEEK 3 TESTING");
        System.out.println("=================================");

        CPU cpu = new CPU();

        // Test 1: Memory
        System.out.println("\nTEST 1: MEMORY READ/WRITE");

        cpu.writeMemory(0x20, 0x55);
        int memoryValue = cpu.readMemory(0x20);

        System.out.println("Written Value : " + Integer.toHexString(0x55).toUpperCase() + "H");
        System.out.println("Read Value    : " + Integer.toHexString(memoryValue).toUpperCase() + "H");

        if (memoryValue == 0x55) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        // Test 2: Stack
        System.out.println("\nTEST 2: STACK PUSH/POP");

        cpu.push(0x10);
        cpu.push(0x20);
        cpu.push(0x30);

        System.out.println("SP after PUSH : "
                + Integer.toHexString(cpu.getStackPointer()).toUpperCase() + "H");

        int pop1 = cpu.pop();
        int pop2 = cpu.pop();
        int pop3 = cpu.pop();

        System.out.println("POP 1 : "
                + Integer.toHexString(pop1).toUpperCase() + "H");
        System.out.println("POP 2 : "
                + Integer.toHexString(pop2).toUpperCase() + "H");
        System.out.println("POP 3 : "
                + Integer.toHexString(pop3).toUpperCase() + "H");

        if (pop1 == 0x30 && pop2 == 0x20 && pop3 == 0x10) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        // Test 3: FIFO Queue
        System.out.println("\nTEST 3: FIFO QUEUE");

        cpu.enqueue(0x10);
        cpu.enqueue(0x20);
        cpu.enqueue(0x30);

        System.out.println("Queue after ENQUEUE:");

        int[] queueValues = cpu.getFIFOQueue().getQueueValues();

        for (int value : queueValues) {
            System.out.print(
                Integer.toHexString(value).toUpperCase() + "H "
            );
        }

        System.out.println();

        int dequeue1 = cpu.dequeue();
        int dequeue2 = cpu.dequeue();
        int dequeue3 = cpu.dequeue();

        System.out.println("DEQUEUE 1 : "
                + Integer.toHexString(dequeue1).toUpperCase() + "H");
        System.out.println("DEQUEUE 2 : "
                + Integer.toHexString(dequeue2).toUpperCase() + "H");
        System.out.println("DEQUEUE 3 : "
                + Integer.toHexString(dequeue3).toUpperCase() + "H");

        if (dequeue1 == 0x10 &&
            dequeue2 == 0x20 &&
            dequeue3 == 0x30) {

            System.out.println("FIFO RESULT: PASS");

        } else {
            System.out.println("FIFO RESULT: FAIL");
        }

        // Test 4: Assembly Stack
        System.out.println("\nTEST 4: STACK ASSEMBLY");

        Instruction[] stackProgram = {
            new Instruction("PUSH", new String[]{"#10"}),
            new Instruction("PUSH", new String[]{"#20"}),
            new Instruction("PUSH", new String[]{"#30"}),
            new Instruction("POP", new String[]{"A"}),
            new Instruction("HALT", new String[]{})
        };

        cpu.reset();
        cpu.loadProgram(stackProgram);
        cpu.run();

        System.out.println("Accumulator after POP : "
                + Integer.toHexString(cpu.getAccumulator()).toUpperCase() + "H");

        if (cpu.getAccumulator() == 0x30) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        // Test 5: Assembly FIFO
        System.out.println("\nTEST 5: FIFO ASSEMBLY");

        Instruction[] queueProgram = {
            new Instruction("ENQUEUE", new String[]{"#10"}),
            new Instruction("ENQUEUE", new String[]{"#20"}),
            new Instruction("ENQUEUE", new String[]{"#30"}),
            new Instruction("DEQUEUE", new String[]{}),
            new Instruction("ENQUEUE", new String[]{"#40"}),
            new Instruction("ENQUEUE", new String[]{"#50"}),
            new Instruction("DEQUEUE", new String[]{}),
            new Instruction("ENQUEUE", new String[]{"#60"}),
            new Instruction("DEQUEUE", new String[]{}),
            new Instruction("HALT", new String[]{})
        };

        cpu.reset();
        cpu.loadProgram(queueProgram);
        cpu.run();

        int[] finalQueue = cpu.getFIFOQueue().getQueueValues();

        System.out.println("Final Queue:");

        for (int value : finalQueue) {
            System.out.print(
                Integer.toHexString(value).toUpperCase() + "H "
            );
        }

        System.out.println();

        if (finalQueue.length == 3 &&
            finalQueue[0] == 0x28 &&
            finalQueue[1] == 0x32 &&
            finalQueue[2] == 0x3C) {

            System.out.println("RESULT: PASS");

        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println("\n=================================");
        System.out.println("        TESTING COMPLETED");
        System.out.println("=================================");
    }
}
