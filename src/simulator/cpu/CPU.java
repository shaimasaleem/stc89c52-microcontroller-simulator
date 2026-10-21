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

        String destination = operands[0].trim().toUpperCase();
        String source = operands[1].trim().toUpperCase();

        int value;

        // Find source value
        if (source.startsWith("#")) {
            value = Integer.parseInt(source.substring(1));
        }
        else if (source.equals("A")) {
            value = accumulator;
        }
        else if (source.equals("B")) {
            value = bRegister;
        }
        else if (source.matches("R[0-7]")) {
            int sourceRegister =
                    Integer.parseInt(source.substring(1));

            value = registers[sourceRegister];
        }
        else {
            System.out.println(
                    "ERROR: Invalid MOV source " + source);
            break;
        }

        value = value & 0xFF;

        // Store value in destination
        if (destination.equals("A")) {
            accumulator = value;
        }
        else if (destination.equals("B")) {
            bRegister = value;
        }
        else if (destination.matches("R[0-7]")) {

            int registerNumber =
                    Integer.parseInt(destination.substring(1));

            registers[registerNumber] = value;
        }
        else if (destination.endsWith("H")) {

            int address = Integer.parseInt(
                    destination.substring(
                            0, destination.length() - 1), 16);

            dataMemory.write(address, value);
        }
        else {
            System.out.println(
                    "ERROR: Invalid MOV destination "
                    + destination);
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
    try {
        String destination = operands[0].trim().toUpperCase();
        String source = operands[1].trim().toUpperCase();

        int destValue = getRegisterValue(destination);
        int sourceValue = getOperandValue(source);

        int result = destValue + sourceValue;

        CY = result > 255;
        OV = ((destValue ^ result) & (sourceValue ^ result) & 0x80) != 0;

        setRegisterValue(destination, result & 0xFF);

    } catch (Exception e) {
        System.out.println("ERROR: Invalid ADD instruction.");
    }
    break;
        case "SUB":
    try {
        String destination = operands[0].trim().toUpperCase();
        String source = operands[1].trim().toUpperCase();

        int destValue = getRegisterValue(destination);
        int sourceValue = getOperandValue(source);

        int result = destValue - sourceValue;

        CY = result < 0;

        setRegisterValue(destination, result & 0xFF);

    } catch (Exception e) {
        System.out.println("ERROR: Invalid SUB instruction.");
    }
    break;

        case "ANL":
    try {
        String destination = operands[0].trim().toUpperCase();
        String source = operands[1].trim().toUpperCase();

        int destValue = getRegisterValue(destination);
        int sourceValue = getOperandValue(source);

        setRegisterValue(
            destination,
            (destValue & sourceValue) & 0xFF
        );

    } catch (Exception e) {
        System.out.println("ERROR: Invalid ANL instruction.");
    }
    break;
        case "INC":
    try {
        String destination = operands[0].trim().toUpperCase();

        int value = getRegisterValue(destination);

        setRegisterValue(
            destination,
            (value + 1) & 0xFF
        );

    } catch (Exception e) {
        System.out.println("ERROR: Invalid INC instruction.");
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

    private int getRegisterValue(String register) {

    register = register.trim().toUpperCase();

    if (register.equals("A")) {
        return accumulator;
    }

    if (register.equals("B")) {
        return bRegister;
    }

    if (register.matches("R[0-7]")) {
        int registerNumber =
                Integer.parseInt(register.substring(1));

        return registers[registerNumber];
    }

    throw new IllegalArgumentException(
            "Invalid register: " + register);
}


private int getOperandValue(String operand) {

    operand = operand.trim().toUpperCase();

    if (operand.startsWith("#")) {
        return Integer.parseInt(operand.substring(1));
    }

    return getRegisterValue(operand);
}


private void setRegisterValue(String register, int value) {

    register = register.trim().toUpperCase();
    value = value & 0xFF;

    if (register.equals("A")) {
        accumulator = value;
    }
    else if (register.equals("B")) {
        bRegister = value;
    }
    else if (register.matches("R[0-7]")) {

        int registerNumber =
                Integer.parseInt(register.substring(1));

        registers[registerNumber] = value;
    }
    else {
        throw new IllegalArgumentException(
                "Invalid register: " + register);
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
