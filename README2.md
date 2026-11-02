# Week 2 – CPU Prototype and Instruction Execution

## Educational Microcontroller Simulator (STC89C52)

### Objective

Develop a working Java-based simulator prototype for the STC89C52 microcontroller, implementing CPU operations, instruction execution, and the Fetch–Decode–Execute cycle.

### Features Implemented

- CPU registers: Accumulator, R0–R7, Program Counter (PC), Stack Pointer (SP)
- CPU status flags: CY, AC, OV, P
- Program memory and instruction representation
- Fetch–Decode–Execute cycle
- Step-by-step and continuous execution
- Load, Run, Step, and Reset functionality
- Basic user interface with CPU state and execution trace
- Java backend integration using an HTTP server

### Instruction Set

Eight instructions are included in the prototype.

| Category | Instructions |
|---|---|
| Data Transfer | MOV, MOVC |
| Arithmetic | ADD, SUB |
| Logical | ANL |
| Increment/Decrement | INC |
| Control Flow | SJMP |
| Termination | HALT |

*SUB and HALT are simplified simulator-defined operations.*

### Fetch–Decode–Execute Cycle

1. **Fetch:** Retrieves the instruction from program memory using the Program Counter.
2. **Decode:** Identifies the instruction and operands.
3. **Execute:** Performs the operation and updates the CPU state.

### Sample Program

<img width="248" height="275" alt="image" src="https://github.com/user-attachments/assets/dffa48dc-8a40-4f19-a89d-e6c33b22cb31" />


### Project Files

| File | Description |
|---|---|
| CPU.java | CPU registers, flags, and instruction execution |
| Instruction.java | Instruction representation |
| Main.java | Simulator initialization and demonstration |
| ProgramMemory.java | Instruction storage |
| DataMemory.java | Data memory operations |
| StackMemory.java | Stack operations |
| SimulatorServer.java | Backend communication with the UI |

### How to Run

Compile the Java files from the project root:

<img width="592" height="47" alt="image" src="https://github.com/user-attachments/assets/4f752222-0541-4469-910e-a2c6038c5490" />

Run the console simulator:

<img width="346" height="56" alt="image" src="https://github.com/user-attachments/assets/4351f70c-6927-4b54-8405-c56b3f88ccae" />


Or start the backend server for the UI:

<img width="412" height="46" alt="image" src="https://github.com/user-attachments/assets/570135eb-03a1-4237-a16f-be3fadc3ce1c" />


Server address: `http://localhost:8080`

### Week 2 Outcome

The initial simulator prototype supports eight selected instructions, CPU state monitoring, Fetch–Decode–Execute execution, and basic user interface integration. It establishes the foundation for further simulator development.
