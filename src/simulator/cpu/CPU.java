package simulator;

public class CPU {
    private int accumulator;
    private int[] registers= {0, 0, 0, 0, 0, 0, 0, 0};
    private int programCounter=0;
    private int stackPointer = 0x07;
    private DataMemory dataMemory;
    private StackMemory stackMemory;
    private boolean CY=false;
    private boolean AC=false;
    private boolean OV=false;
    private boolean P=false;
    private boolean halted = false;
    private int[] codeMemory = new int[256];
    public CPU() {
    accumulator = 0;

    dataMemory = new DataMemory();
    stackMemory = new StackMemory(dataMemory);
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
    if (operands[0].equals("A")) {
        accumulator = Integer.parseInt(operands[1].substring(1));
    }
    else if (operands[0].startsWith("R")) {
        int registerNumber = Integer.parseInt(operands[0].substring(1));
        registers[registerNumber] = Integer.parseInt(operands[1].substring(1));
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
        default:
            System.out.println("Invalid instruction " + operation);
            break;
    }
}

    public int getAccumulator() {
        return accumulator;
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

    for (int i = 0; i < registers.length; i++) {
        registers[i] = 0;
    }

    programCounter = 0;
    stackMemory.reset();

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
}
