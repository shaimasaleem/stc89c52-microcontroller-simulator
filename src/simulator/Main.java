package simulator;

public class Main {
    public static void main(String[] args) {

        System.out.println("STC89C52 Simulator Started");

        CPU cpu = new CPU();

        Instruction[] program = {
            new Instruction("MOV", new String[]{"A", "#10"}),
            new Instruction("MOVC", new String[]{"A", "10"}),
            new Instruction("ADD", new String[]{"A", "#5"}),
            new Instruction("SUB", new String[]{"A", "#2"}),
            new Instruction("ANL", new String[]{"A", "#3"}),
            new Instruction("INC", new String[]{"A"}),
            new Instruction("SJMP", new String[]{"7"}),
            new Instruction("HALT", new String[]{})
        };

        cpu.setCodeMemory(10, 3);
        cpu.loadProgram(program);

        cpu.run();

        cpu.displayState();

        System.out.println("\n----- STACK TEST -----");

        cpu.push(0x15);

        System.out.println("After PUSH 15H, SP = "
                + Integer.toHexString(cpu.getStackPointer()).toUpperCase());

        cpu.push(0x20);

        System.out.println("After PUSH 20H, SP = "
                + Integer.toHexString(cpu.getStackPointer()).toUpperCase());

        int value = cpu.pop();

        System.out.println("POP value = "
                + Integer.toHexString(value).toUpperCase());

        System.out.println("After POP, SP = "
                + Integer.toHexString(cpu.getStackPointer()).toUpperCase());
    }
}