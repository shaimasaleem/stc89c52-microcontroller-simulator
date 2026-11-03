# Educational Microcontroller Simulator with Process Scheduling

## Week 3 – Memory, Stack and FIFO Queue Integration

### 1. Project Overview

The Educational Microcontroller Simulator is a Java-based project developed to simulate the basic working of the **STC89C52 (8051 family) microcontroller**.

The project combines concepts from Microprocessor Architecture, Data Structures and Operating Systems to demonstrate instruction execution and internal data handling.

In Week 2, the basic CPU simulator was developed with instruction execution, registers, flags and a user interface.

**Week 3 focuses on extending the simulator by implementing and integrating Data Memory, Stack Memory and FIFO Queue operations.**

The simulator supports Assembly-based operations and displays the updated CPU, Memory, Stack and Queue states through an HTML interface.

---

### 2. Week 3 Objectives

- Implement memory read and write operations.
- Implement Stack Memory with Stack Pointer management.
- Support PUSH and POP operations.
- Implement FIFO Queue using a circular array.
- Support ENQUEUE and DEQUEUE operations.
- Integrate Memory, Stack and FIFO Queue with CPU execution.
- Update the user interface to display the internal state of the simulator.
- Validate the implementation using Assembly programs and test cases.

---

### 3. Technologies Used

| Component | Technology |
|---|---|
| Programming Language | Java |
| Microcontroller | STC89C52 |
| Frontend | HTML, CSS, JavaScript |
| Backend | Java HTTP Server |
| IDE | Visual Studio Code |
| Platform | Windows |
| Version Control | Git and GitHub |

---

### 4. Week 3 Implementation

#### 4.1 Data Memory

Data Memory was implemented using a 256-byte array to represent the simulator's internal memory.

Features:
- Memory read and write operations.
- Address validation.
- Storage of 8-bit values.
- Memory reset functionality.
- Integration with CPU and Stack operations.
- Memory visualization in hexadecimal format.

#### 4.2 Stack Memory

Stack Memory was implemented to demonstrate Last In, First Out (LIFO) behavior.

Features:
- Stack Pointer initialized to `07H`.
- PUSH operation increments SP and stores data.
- POP operation retrieves data and decrements SP.
- Stack overflow and underflow handling.
- Stack contents and Stack Pointer displayed in the UI.

#### 4.3 FIFO Queue

A circular FIFO Queue was implemented to demonstrate First In, First Out data processing.

Features:
- ENQUEUE operation to insert elements.
- DEQUEUE operation to remove elements.
- Circular indexing for efficient storage.
- Queue full and empty conditions.
- Queue capacity of five elements.
- Queue size and status displayed in the UI.

#### 4.4 CPU Integration

The CPU was extended to support the new operations.

The instruction execution cycle follows:

**FETCH → DECODE → EXECUTE**

The CPU interacts with Data Memory, Stack Memory and FIFO Queue while executing the supported instructions.

#### 4.5 User Interface

The HTML-based interface was updated to include:

- Assembly Program Editor
- LOAD, STEP, RUN and RESET controls
- CPU Registers and Flags
- Data Memory Display
- Stack Contents and Stack Pointer
- FIFO Queue Contents and Status
- Execution Trace

Stack and Queue operations are performed through Assembly instructions entered in the program editor.

---

### 5. Supported Instructions

| Category | Instructions |
|---|---|
| Data Transfer | MOV, MOVC |
| Arithmetic | ADD, SUB |
| Logical | ANL |
| Increment | INC |
| Control Flow | SJMP |
| Termination | HALT |
| Stack Operations | PUSH, POP |
| Queue Operations | ENQUEUE, DEQUEUE |

*HALT, ENQUEUE and DEQUEUE are simulator-supported operations used for educational purposes.*

---

### 6. Assembly Test Programs

#### Stack Test Program

```asm
PUSH #10
PUSH #20
PUSH #30
POP A
HALT
```

Expected result:

- Three values are pushed onto the stack.
- POP retrieves the most recently pushed value.
- Accumulator becomes `1EH` (30 decimal).
- Stack Pointer updates correctly.

#### FIFO Queue Test Program

```asm
ENQUEUE #10
ENQUEUE #20
ENQUEUE #30
DEQUEUE
ENQUEUE #40
ENQUEUE #50
DEQUEUE
ENQUEUE #60
DEQUEUE
HALT
```

Expected final queue:

```text
28H 32H 3CH
```

This program verifies FIFO ordering through multiple ENQUEUE and DEQUEUE operations.

**Note:** The current simulator interprets immediate values such as `#10` as decimal values and displays results in hexadecimal format.

---

### 7. Week 3 Testing

A separate `Week3Test.java` file was created to verify the implemented components.

| Test | Description | Result |
|---|---|---|
| Test 1 | Memory Read/Write | PASS |
| Test 2 | Stack PUSH/POP | PASS |
| Test 3 | FIFO Queue Operations | PASS |
| Test 4 | Stack Assembly Execution | PASS* |
| Test 5 | FIFO Assembly Execution | PASS |

*Test 4 initially reported FAIL because the test expected `30H` instead of `1EH`. The observed accumulator value was correct for the simulator's decimal immediate format. The test assertion was corrected.*

The tests verify memory operations, LIFO behavior, FIFO ordering and Assembly instruction integration.

---

### 8. Project Structure

```text
Educational-Microcontroller-Simulator/
│
├── src/
│   └── simulator/
│       ├── CPU.java
│       ├── DataMemory.java
│       ├── StackMemory.java
│       ├── FIFOQueue.java
│       ├── Instruction.java
│       ├── ProgramMemory.java
│       ├── SimulatorServer.java
│       └── Main.java
│
├── Testing/
│   └── Week3Test.java
│
├── ui/
│   └── index.html
│
├── out/
│
└── README.md
```

---

### 9. How to Run the Project

**Step 1: Open the project folder in VS Code.**

**Step 2: Compile the Java source files.**

```powershell
javac src\simulator\*.java -d out
```

**Step 3: Start the simulator server.**

```powershell
java -cp out simulator.SimulatorServer
```

**Step 4: Open `ui/index.html` in a web browser.**

Use the Assembly editor and simulator controls to execute instructions and observe the CPU, Memory, Stack and FIFO Queue.

#### Running Week 3 Tests

Compile:

```powershell
javac src\simulator\*.java Testing\Week3Test.java -d out
```

Run:

```powershell
java -cp out simulator.Week3Test
```

The terminal displays the result of each test.

---

### 10. Team Contributions

| Team Member | Responsibility |
|---|---|
| Shaima Saleem | CPU Integration and Overall Integration |
| Niranjana S | Data Memory and Stack Implementation |
| Amra Fathima | FIFO Queue Implementation |
| Thejes Santhosh | User Interface, Testing and Documentation |

---

### 11. Challenges Encountered

- Integrating Memory, Stack and FIFO Queue with the existing CPU.
- Synchronizing backend state with the HTML interface.
- Displaying memory and queue values in hexadecimal format.
- Validating Stack and FIFO operations through Assembly instructions.
- Correcting expected values in test cases to match decimal immediate interpretation.

These issues were addressed through code adjustments and component testing.

---

### 12. Week 3 Outcome

Week 3 extended the basic simulator by adding Memory, Stack and FIFO Queue functionality.

The implemented components support Assembly-driven operations and display the updated states through the simulator interface.

Memory read/write, Stack LIFO behavior, FIFO ordering and CPU integration were verified through test execution.

### 13. Next Phase – Week 4

The next phase focuses on extending the simulator architecture with:

- Independent UI, Core and Logging processes.
- Inter-process communication.
- Integration of the three processes.
- System testing and performance evaluation.

---

**Project:** Educational Microcontroller Simulator with Process Scheduling  
**Microcontroller:** STC89C52  
**Development Language:** Java  
**Current Phase:** Week 3 – Memory, Stack and FIFO Queue Integration
