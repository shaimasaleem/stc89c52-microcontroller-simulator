package simulator;

public class CPU {
    private int accumulator;
    private int bRegister;
    private int[] registers = {0, 0, 0, 0, 0, 0, 0, 0};
    private int programCounter=0;
    private int stackPointer = 0x07;
    private DataMemory dataMemory;
    private StackMemory stackMemory;
    private FIFOQueue fifoQueue;
    private boolean CY=false;
    private boolean AC=false;
    private boolean OV=false;
    private boolean P=false;
    private boolean halted = false;
    private int[] codeMemory = new int[256];
    public CPU() {
    accumulator = 0;
    bRegister = 0;

    dataMemory = new DataMemory();
    stackMemory = new StackMemory(dataMemory);
    fifoQueue = new FIFOQueue(5);
}
   private ProgramMemory programMemory = new ProgramMemory();
    Instruction fetch(){
    if (programCounter >= programMemory.size()) {
        return null;
    }

    return programMemory.read(programCounter);
}
    Instruction decode(Instruction instruction){
        String operation=instruction.getOperation();
        String[] operands=instruction.getOperands();
        return instruction;
    }
    void execute(Instruction instruction) {

    String operation = instruction.getOperation();
    String[] operands = instruction.getOperands();

    switch (operation) {

     case "MOV":
    try {

        if (operands.length < 2) {
            System.out.println("ERROR: MOV requires 2 operands.");
            break;
        }

        if (operands[0].equals("A")) {
            accumulator = Integer.parseInt(operands[1].substring(1));
        }

        else if (operands[0].equals("B")) {
            bRegister = Integer.parseInt(operands[1].substring(1));
        }

        else if (operands[0].startsWith("R")) {
            int registerNumber =
                    Integer.parseInt(operands[0].substring(1));

            if (registerNumber < 0 || registerNumber > 7) {
                System.out.println("ERROR: Invalid register "
                        + operands[0]);
                break;
            }

            registers[registerNumber] =
                    Integer.parseInt(operands[1].substring(1));
        }

        else if (operands[0].endsWith("H")) {

            int address = Integer.parseInt(
                    operands[0].substring(
                            0, operands[0].length() - 1), 16);

            if (address < 0 || address > 0xFF) {
                System.out.println(
                        "ERROR: Invalid memory address "
                        + operands[0]);
                break;
            }

            int value =
                    Integer.parseInt(operands[1].substring(1));

            dataMemory.write(address, value);
        }

        else {
            System.out.println(
                    "ERROR: Invalid MOV destination "
                    + operands[0]);
        }

    } catch (NumberFormatException e) {
        System.out.println("ERROR: Invalid value in MOV.");
    }

    break;
        case "MOVC":
    if (operands[0].equals("A")) {
        int address = Integer.parseInt(operands[1]);
        accumulator = codeMemory[address];
    }
    break;
       case "ADD":
    if (operands[0].equals("A")) {

        int value = Integer.parseInt(operands[1].substring(1));
        int result = accumulator + value;

        CY = result > 255;
        OV = (accumulator < 128 && value < 128 && result >= 128)
                || (accumulator >= 128 && value >= 128 && result < 256);

        accumulator = result & 0xFF;
    }
    break;
        case "SUB":
    if (operands[0].equals("A")) {

        int value = Integer.parseInt(operands[1].substring(1));
        int result = accumulator - value;

        CY = result < 0;

        accumulator = result & 0xFF;
    }
    break;

        case "ANL":
            if(operands[0].equals("A")) {
                accumulator &= Integer.parseInt(operands[1].substring(1));
            }
    break;
        case "INC":
            if (operands[0].equals("A")) {
                 accumulator++;
    }
    break;
        case "SJMP":
            programCounter = Integer.parseInt(operands[0]);
    break;
        case "HALT":
            halted = true;
    break;
        case "PUSH":
            int pushValue = Integer.parseInt(operands[0].substring(1));
            stackMemory.push(pushValue);
    break;

        case "POP":
    if (operands[0].equals("A")) {
        accumulator = stackMemory.pop();
    }
    break;
        case "ENQUEUE":
            int enqueueValue = Integer.parseInt(operands[0].substring(1));
            fifoQueue.enqueue(enqueueValue);
    break;

        case "DEQUEUE":
            fifoQueue.dequeue();
    break;
        default:
            System.out.println("Invalid instruction " + operation);
            break;
    }
}

    public int getAccumulator() {
        return accumulator;
    }
    public int getBRegister() {
    return bRegister;
}
    public int getProgramCounter() {
    return programCounter;
}
    void displayState() {
    System.out.println("----- CPU STATE -----");

    System.out.println("Accumulator = " + accumulator);

    System.out.println("R0 = " + registers[0]);
    System.out.println("R1 = " + registers[1]);
    System.out.println("R2 = " + registers[2]);
    System.out.println("R3 = " + registers[3]);
    System.out.println("R4 = " + registers[4]);
    System.out.println("R5 = " + registers[5]);
    System.out.println("R6 = " + registers[6]);
    System.out.println("R7 = " + registers[7]);

    System.out.println("Program Counter = " + programCounter);
    System.out.println("Stack Pointer = " + stackMemory.getSP());

    System.out.println("CY = " + CY);
    System.out.println("AC = " + AC);
    System.out.println("OV = " + OV);
    System.out.println("P = " + P);

    System.out.println("---------------------");
}
    public void step() {
        if (halted) {
    System.out.println("CPU is halted.");
    return;
}
    Instruction instruction = fetch();

    if (instruction == null) {
        System.out.println("No instruction found.");
        return;
    }

    System.out.println("FETCH: " + instruction.getOperation());

    instruction = decode(instruction);

    System.out.println("DECODE: " + instruction.getOperation());

    execute(instruction);

    System.out.println("EXECUTE: " + instruction.getOperation());

   if (!instruction.getOperation().equals("SJMP")
        && !instruction.getOperation().equals("HALT")) {
    programCounter++;
}
}
    void run() {
    while (!halted) {
        step();
    }

    System.out.println("Program terminated.");
    }
public void loadProgram(Instruction[] program) {
    programMemory.loadProgram(program);
    programCounter = 0;
    halted = false;
}
public int getRegister(int index) {
    return registers[index];
}
public void setRegister(int index, int value) {
    registers[index] = value;
}
public void setCodeMemory(int address, int value) {
    codeMemory[address] = value;
}
public int getCodeMemory(int address) {
    return codeMemory[address];
}
public int getStackPointer() {
    return stackMemory.getSP();
}
public boolean getCY() {
    return CY;
}
public boolean getOV() {
    return OV;
}
public void reset() {
    accumulator = 0;
    bRegister = 0;

    for (int i = 0; i < registers.length; i++) {
        registers[i] = 0;
    }

    programCounter = 0;
    stackMemory.reset();
    fifoQueue.reset();
    dataMemory.reset();
    CY = false;
    AC = false;
    OV = false;
    P = false;

    halted = false;
}
public boolean isHalted() {
    return halted;
}
public boolean getAC() {
    return AC;
}

public boolean getP() {
    return P;
}

public Instruction getCurrentInstruction() {
    if (programCounter >= 0 && programCounter < programMemory.size()) {
        return programMemory.read(programCounter);
    }
    return null;
}
public void push(int value) {
    stackMemory.push(value);
}

public int pop() {
    return stackMemory.pop();
}
public void enqueue(int value) {
    fifoQueue.enqueue(value);
}

public int dequeue() {
    return fifoQueue.dequeue();
}
public FIFOQueue getFIFOQueue() {
    return fifoQueue;
}
public void writeMemory(int address, int value) {
    dataMemory.write(address, value);
}

public int readMemory(int address) {
    return dataMemory.read(address);
}
public int[] getDataMemory() {
    int[] memory = new int[256];

    for (int i = 0; i < 256; i++) {
        memory[i] = dataMemory.read(i);
    }

    return memory;
}
}
