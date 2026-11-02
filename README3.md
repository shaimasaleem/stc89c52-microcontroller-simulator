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

<img width="732" height="496" alt="image" src="https://github.com/user-attachments/assets/fd4932c4-0095-46b3-bfb8-783ee473afe1" />


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
<img width="867" height="597" alt="image" src="https://github.com/user-attachments/assets/8a8d7b36-425e-438b-9fe5-b2bc622d3ee9" />

*HALT, ENQUEUE and DEQUEUE are simulator-supported operations used for educational purposes.*

---

### 6. Assembly Test Programs

#### Stack Test Program

<img width="142" height="185" alt="image" src="https://github.com/user-attachments/assets/b6b9f5a1-d7f5-4a08-8efc-5c19d39fdf45" />


Expected result:

- Three values are pushed onto the stack.
- POP retrieves the most recently pushed value.
- Accumulator becomes `1EH` (30 decimal).
- Stack Pointer updates correctly.

#### FIFO Queue Test Program

<img width="297" height="341" alt="image" src="https://github.com/user-attachments/assets/06a56bb3-5af1-4cb7-b7e6-8bd5cafa8210" />


Expected final queue:

<img width="310" height="42" alt="image" src="https://github.com/user-attachments/assets/d0f4d763-1ce3-4787-8cfa-0cc4c6ff26b6" />


This program verifies FIFO ordering through multiple ENQUEUE and DEQUEUE operations.

**Note:** The current simulator interprets immediate values such as `#10` as decimal values and displays results in hexadecimal format.

---

### 7. Week 3 Testing

A separate `Week3Test.java` file was created to verify the implemented components.
<img width="902" height="335" alt="image" src="https://github.com/user-attachments/assets/96f12db1-62b5-46f9-8b18-3db1b4dc8439" />


*Test 4 initially reported FAIL because the test expected `30H` instead of `1EH`. The observed accumulator value was correct for the simulator's decimal immediate format. The test assertion was corrected.*

The tests verify memory operations, LIFO behavior, FIFO ordering and Assembly instruction integration.

---

### 8. Project Structure

<img width="495" height="732" alt="image" src="https://github.com/user-attachments/assets/95baaeaf-2f9f-436a-8fe3-a9011dee87e9" />

### 9. How to Run the Project

**Step 1: Open the project folder in VS Code.**

**Step 2: Compile the Java source files.**

<img width="416" height="55" alt="image" src="https://github.com/user-attachments/assets/fec97420-7e9d-4c02-9dc2-881256e359ae" />


**Step 3: Start the simulator server.**

<img width="590" height="47" alt="image" src="https://github.com/user-attachments/assets/427b8f94-4b06-4d0c-9643-ca300e6ec4ce" />

**Step 4: Open `ui/index.html` in a web browser.**

Use the Assembly editor and simulator controls to execute instructions and observe the CPU, Memory, Stack and FIFO Queue.

#### Running Week 3 Tests

Compile:

<img width="722" height="55" alt="image" src="https://github.com/user-attachments/assets/eec444f9-d36b-4f7a-b4b5-ef562ae7c7a1" />


Run:

<img width="377" height="50" alt="image" src="https://github.com/user-attachments/assets/c2ea5254-f10a-44ee-adcd-d2a39f026a3a" />


The terminal displays the result of each test.

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
