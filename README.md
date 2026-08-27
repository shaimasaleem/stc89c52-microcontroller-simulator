# stc89c52-microcontroller-simulator
Software simulator for the STC89C52 microcontroller developed as a team project.
## 1. Project Objective

The objective of this project is to develop an educational simulator for the **STC89C52 8-bit microcontroller**. The simulator will demonstrate instruction execution, memory and peripheral operations, process management, and CPU scheduling.

## 2. Problem Statement

The project aims to design a software-based simulator that models the basic components of the STC89C52, including registers, memory, stack, instructions, GPIO, timers, and interrupts. It will also support multiple programs as processes using PCB, ready queues, context switching, and scheduling algorithms such as **FCFS, Round Robin, and Priority Scheduling**.

## 3. Project Scope

The simulator will include:

* CPU and register simulation
* Program and data memory
* Stack operations
* Selected instruction execution
* GPIO, timer, and interrupt simulation
* Process management using PCB
* Ready queue and context switching
* FCFS, Round Robin, and Priority Scheduling
* Program loading, running, resetting, and single-step execution
* Performance measures such as waiting time, turnaround time, and CPU utilization

## 4. Microcontroller Being Simulated

**STC89C52 – 8-bit 8051-compatible microcontroller**

The project will simulate the main features required for educational purposes rather than the complete hardware of the microcontroller.

## 5. Team Members

| Student                 | Name            |
| ----------------------- | --------------- |
| Student 1 – Team Leader | Shaima Saleem   |
| Student 2               | Niranjana S     |
| Student 3               | Amra Fathima    |
| Student 4               | Thejes Santhosh |

## 6. Team Responsibilities

| Member          | Primary Responsibility               | Supporting Responsibility |
| --------------- | ------------------------------------ | ------------------------- |
| Shaima Saleem   | CPU & Instruction Execution          | Integration & GitHub      |
| Niranjana S     | Memory & Stack                       | CPU Support               |
| Amra Fathima    | Data Structures & Process Management | Testing                   |
| Thejes Santhosh | OS Scheduling & Context Switching    | UI & Integration          |

## 7. Selected Programming Language

**Java**

Java is selected because it supports object-oriented programming and provides suitable data structures for implementing the CPU, memory, processes, PCB, scheduling algorithms, and user interface.

## 8. Initial System Architecture

<img width="1402" height="1122" alt="ChatGPT Image Aug 27, 2026, 08_59_09 PM" src="https://github.com/user-attachments/assets/af3e07b4-1623-45b5-a8c5-6e573ea9c33d" />


## 9. Initial Development Plan

### Week 1 – Planning

* Form the team and assign responsibilities.
* Study the STC89C52 architecture.
* Select Java.
* Set up GitHub and project documentation.
* Design the initial system architecture.

### Week 2 – CPU and Memory

* Implement registers, PC, flags, memory, and stack.
* Design the instruction representation.
* Implement selected instructions.

### Week 3 – Processes and Scheduling

* Implement PCB and process states.
* Implement ready queue and context switching.
* Implement FCFS, Round Robin, and Priority Scheduling.

### Week 4 – Peripherals and UI

* Add GPIO, timer, and interrupt simulation.
* Develop the user interface.
* Add program loading, run, reset, and single-step features.

### Final Phase – Testing and Integration

* Integrate all components.
* Test instruction execution and scheduling.
* Calculate performance metrics.
* Fix issues and prepare final documentation.

## 10. Expected Outcome

The final simulator will allow users to **load and execute programs, observe microcontroller operations, manage multiple processes, and compare different CPU scheduling algorithms** in an interactive educational environment.
