# STC89C52 Educational Microcontroller Simulator

## Overview

The **STC89C52 Educational Microcontroller Simulator** is a Java-based software simulator designed to demonstrate the fundamental operation of the STC89C52 8-bit microcontroller.

The simulator provides an interactive environment for executing assembly-style instructions and observing changes in CPU registers, flags, memory, stack and FIFO queue.

In Week 4, the simulator was extended from a standalone application into a **multi-process system** consisting of three independent processes:

- UI Process
- Core Process
- Logging Process

The processes communicate using **TCP socket-based Inter-Process Communication (IPC)**.

---

## Project Objective

The objective of the simulator is to provide an educational environment for understanding:

- CPU instruction execution
- Fetch–Decode–Execute cycle
- Registers and CPU flags
- Program and Data Memory
- Stack operations
- FIFO Queue operations
- Program execution and control
- Processes and threads
- Inter-Process Communication
- Execution logging
- Basic performance evaluation

---

## Microcontroller

The simulator is based on the:

**STC89C52 – 8-bit 8051-compatible microcontroller**

The project implements selected components and operations required for educational simulation. It does not attempt to reproduce every hardware feature of the physical microcontroller.

---

# Week 4 – Multi-Process Simulator

The Week 4 version separates the simulator into three independent processes.

### UI Process

The UI Process provides the interface for entering programs, controlling execution and viewing simulator results.

### Core Process

The Core Process contains the main simulator logic including:

- CPU
- Program Memory
- Data Memory
- Stack
- FIFO Queue
- Instruction execution

### Logging Process

The Logging Process receives execution and error messages and stores them in a log file.

---

## Multi-Process Architecture

The overall communication flow is:

```text
UI PROCESS
     │
     │ TCP Port 5001
     ▼
CORE PROCESS
     │
     ├── CPU
     ├── Program Memory
     ├── Data Memory
     ├── Stack
     └── FIFO Queue
     │
     │ TCP Port 5002
     ▼
LOGGING PROCESS
     │
     ▼
simulator.log
```

The UI can also send relevant log information to the Logging Process through port `5002`.

---

# Inter-Process Communication

The simulator uses **TCP sockets** as the primary IPC mechanism.

Java provides the `Socket` and `ServerSocket` classes used to establish communication between the independent processes.

## Communication Ports

| Communication | Port |
|---|---:|
| UI ↔ Core | `5001` |
| Core/UI → Logger | `5002` |

TCP sockets provide:

- Reliable communication
- Communication between independent processes
- Bidirectional data transfer
- Clear separation of components
- Platform-independent Java support

---

## Internal Thread Communication

The project also uses:

- `BlockingQueue`
- `LinkedBlockingQueue`

These queues are used for **thread-safe communication within a process**.

Therefore:

> **TCP sockets are used between independent processes, while LinkedBlockingQueue is used between threads inside a process.**

---

# CPU Simulation

The simulated CPU contains:

- Accumulator `A`
- Register `B`
- General-purpose registers `R0–R7`
- Program Counter `PC`
- Stack Pointer `SP`
- Carry Flag `CY`
- Auxiliary Carry Flag `AC`
- Overflow Flag `OV`
- Parity Flag `P`

The initial Stack Pointer value is:

```text
SP = 07H
```

---

# Fetch–Decode–Execute Cycle

Instruction execution follows the basic processor cycle:

```text
FETCH → DECODE → EXECUTE → UPDATE CPU STATE
```

### Fetch

The instruction at the current Program Counter is retrieved from Program Memory.

### Decode

The instruction operation and operands are identified.

### Execute

The corresponding CPU operation is performed.

### Update State

Registers, flags, memory, stack, queue and Program Counter are updated as required.

---

# Supported Operations

The simulator supports selected assembly-style instructions and simulator operations.

| Category | Operations |
|---|---|
| Data Transfer | `MOV`, `MOVC` |
| Arithmetic | `ADD`, `SUB` |
| Logical | `ANL` |
| Increment | `INC` |
| Control Flow | `SJMP` |
| Stack | `PUSH`, `POP` |
| FIFO Queue | `ENQUEUE`, `DEQUEUE` |
| Termination | `HALT` |

---

## Example Program

```asm
MOV A,#10
ADD A,#5
INC A
HALT
```

After execution:

```text
A = 16
```

---

# Memory

The simulator contains **256 bytes of Data Memory**.

Memory values can be inspected from the user interface and are updated according to program execution.

Program Memory stores the instructions that are executed by the CPU.

---

# Stack

The simulator provides Stack Memory with support for:

- `PUSH`
- `POP`

Example:

```asm
MOV A,#15
PUSH A
POP R1
HALT
```

The Stack Pointer and stack contents can be viewed from the simulator interface.

---

# FIFO Queue

A FIFO (**First In, First Out**) queue is integrated into the simulator.

Supported operations include:

- `ENQUEUE`
- `DEQUEUE`

Example:

```asm
MOV R0,#10
ENQUEUE R0
DEQUEUE
HALT
```

The UI displays the queue contents, current size and queue status.

---

# User Interface

The simulator provides a visual interface containing:

- Program Input
- Program / Instructions
- Current Instruction
- Registers
- Data Memory
- Stack
- FIFO Queue
- Execution Trace
- Result / Changes
- Flags / Status
- Error Panel
- Instruction Time
- Program Time

## Execution Controls

The interface provides:

```text
LOAD | RESET | STEP | RUN | STOP
```

### STEP

Executes one instruction and displays the updated CPU state.

### RUN

Executes the program continuously using repeated STEP requests.

### STOP

Stops the UI from sending additional STEP requests.

### RESET

Resets the simulator state.

---

# Logging

The independent Logging Process receives execution and error messages through **TCP port 5002**.

The messages are written to:

```text
simulator.log
```

The log can contain information about:

- Program loading
- CPU reset
- Instruction execution
- Program Counter changes
- CPU state changes
- Errors
- HALT and program termination

---

# Testing

The Week 4 system was tested for:

- UI–Core IPC connection
- Program loading
- CPU reset
- STEP execution
- Continuous execution
- STOP behaviour
- HALT handling
- CPU state responses
- Core–Logger communication
- PUSH and POP
- ENQUEUE and DEQUEUE
- CPU flags
- Log-file generation
- Complete multi-process execution

## End-to-End Flow

```text
UI
 ↓
Core
 ↓
CPU / Memory / Stack / FIFO Queue
 ↓
Logger
 ↓
simulator.log
```

A test program used for integrated testing is:

```asm
MOV A,#15
MOV R0,#10
PUSH A
POP R1
ENQUEUE R0
DEQUEUE
ADD A,#1
HALT
```

---

# Performance Evaluation

The Week 4 multi-process implementation was compared with the previous standalone simulator.

The evaluation considers:

- Program execution time
- Instruction execution time
- Memory usage
- CPU usage
- Multi-process execution overhead

The multi-process implementation introduces additional overhead because of:

- TCP communication
- Multiple JVM processes
- Process scheduling
- Logging
- Request/response handling
- UI state updates

However, the multi-process architecture provides improved:

- Modularity
- Component separation
- Process isolation
- Maintainability
- Extensibility

Detailed results are available in the **Benchmark and Performance Analysis** documentation.

---

# Week 4 Documentation

The Week 4 project documentation includes:

- IPC Architecture Diagram
- IPC Mechanism Selection and Justification
- IPC Testing and Test Cases
- Benchmark and Performance Analysis
- Week 4 Decisions
- Week 4 Meeting Minutes
- Week 4 Weekly Status
- UI screenshots and execution evidence

---

# Compile and Run

The simulator can be compiled from the project root directory using PowerShell.

## Compile

```powershell
javac -d out (Get-ChildItem -Path src -Filter *.java -Recurse).FullName
```

## Run

```powershell
java -cp out simulator.Main
```

## Check Running Java Processes

```powershell
jps -l
```

---

# Current Status

The Week 4 simulator successfully integrates:

**UI Process → Core Process → CPU / Memory / Stack / FIFO Queue → Logging Process**

The current implementation demonstrates:

- STC89C52 instruction execution
- Fetch–Decode–Execute cycle
- Registers and CPU flags
- Data Memory
- Stack operations
- FIFO Queue operations
- Program loading and execution
- STEP, RUN, STOP and RESET
- HALT handling
- Three independent processes
- TCP socket-based IPC
- Internal thread-safe communication
- Execution and error logging
- Simulator state visualization
- Execution-time measurement
- Standalone vs multi-process performance evaluation

The project therefore demonstrates both **microcontroller simulation** and fundamental **operating-system concepts such as processes, threads and Inter-Process Communication**.
