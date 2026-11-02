# STC89C52 Microcontroller Simulator

## Project Overview

The **STC89C52 Microcontroller Simulator** is an educational software project designed to demonstrate the basic working of an STC89C52 microcontroller.

It provides a graphical interface where users can enter assembly-like instructions, execute them step by step or continuously, and observe changes in CPU registers, memory, stack, queue, and execution status.

The project aims to help students understand microcontroller concepts through interactive simulation.

## Features

- **Program Input:** Enter instructions in the program editor.
- **Load:** Load the entered program into the simulator.
- **Reset:** Reset the CPU state.
- **Step Execution:** Execute one instruction at a time.
- **Run Execution:** Execute instructions continuously.
- **Stop:** Stop continuous execution.
- **Register Display:** View PC, SP, accumulator (A), B register, and R0–R7.
- **Data Memory:** Display memory addresses and their values.
- **Stack:** Visualize stack contents and stack pointer.
- **FIFO Queue:** Display queue elements, size, and status.
- **Execution Trace:** Show the Fetch, Decode, and Execute stages.
- **Flags Display:** Monitor CY, AC, OV, and P flags.
- **Error Panel:** Display unsupported instructions and syntax errors.
- **Execution Timing:** Display instruction execution time and total program execution time.

## Technologies Used

- HTML
- CSS
- JavaScript
- Java (Backend)
- Data Structures (Stack and FIFO Queue)

## System Architecture

The project follows a frontend–backend architecture.

### Frontend

The frontend is developed using HTML, CSS, and JavaScript. It provides the user interface and displays the simulator's execution results.

### Backend

The Java backend handles program execution and maintains the simulator state, including CPU registers, program counter, memory, stack, and FIFO queue.

### API Communication

The frontend communicates with the Java backend through HTTP requests using JavaScript's Fetch API.

The backend is expected to run at:

`http://localhost:8080`

The frontend uses the following API endpoints:

| API Endpoint | Method | Purpose |
|---|---|---|
| `/api/state` | GET | Retrieve the current simulator state |
| `/api/load` | POST | Load a program |
| `/api/reset` | POST | Reset the simulator |
| `/api/step` | POST | Execute one instruction |

## Supported Instructions

The frontend's program validation includes the following instructions:

| Instruction | Purpose |
|---|---|
| MOV | Move data |
| MOVC | Move code-memory data |
| ADD | Addition |
| SUB | Subtraction |
| ANL | Logical AND |
| INC | Increment |
| SJMP | Short jump |
| PUSH | Push data onto stack |
| POP | Pop data from stack |
| ENQUEUE | Insert an element into FIFO queue |
| DEQUEUE | Remove an element from FIFO queue |
| HALT | Stop program execution |

## How to Run

1. Clone or download the project repository.
2. Open the project folder.
3. Start the Java backend using the instructions provided with the backend source code.
4. Ensure that the backend is running on port `8080`.
5. Open `index.html` in a web browser.
6. Enter a program in the program input section.
7. Click **LOAD** to load the program.
8. Use **STEP** to execute individual instructions or **RUN** to execute continuously.
9. Observe the changes in registers, memory, stack, queue, flags, and execution trace.

## Project Objective

The main objective is to provide a simple and interactive learning environment for understanding microcontroller architecture, instruction execution, memory management, and basic data structures.

## Future Enhancements

- Support for additional 8051 instruction set operations.
- Improved memory visualization.
- Interrupt handling.
- Timer and counter simulation.
- GPIO simulation.
- More detailed execution reports.
  
