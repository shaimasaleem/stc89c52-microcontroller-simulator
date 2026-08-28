# stc89c52-microcontroller-simulator
Software simulator for the STC89C52 microcontroller developed as a team project.
## 1. Project Objective

The aim of this project is the development of educational simulator for **STC89C52 8-bit microcontroller**. The simulator will illustrate instruction execution, memory and peripheral operations, process management, and CPU scheduling.

## 2. Problem Statement

This project is aimed at the development of software based simulator which will emulate the fundamental blocks of the STC89C52, like registers, memory, stack, instructions, GPIO, timers, interrupts, and multiple programs as processes using PCB, ready queues, context switching, and scheduling algorithms like **FCFS, Round Robin and Priority Scheduling**.

## 3. Project Scope

The simulator will comprise:

* CPU and register emulation
* Program and data memory
* Stack operations
* Execution of selected instructions
* GPIO, timer and interrupt emulation
* Process management by means of PCB
* Ready queue and context switching
* FCFS, Round Robin, and Priority Scheduling
* Program loading, execution, reset and single step execution
* Performance evaluation by means of waiting time, turnaround time, CPU utilization and other measures

## 4. Microcontroller Being Simulated

**STC89C52 – 8-bit 8051 compatible microcontroller**

For this project we are going to simulate only main blocks required for educational purposes and not complete microcontroller hardware.

## 5. Team Members

| Student                 | Name             |
| ----------------------- | ---------------- |
| Student 1 – Team Leader | Shaima Saleem    |
| Student 2               | Niranjana S      |
| Student 3               | Amra Fathima     |
| Student 4               | Thejes Santhosh  |

## 6. Team Responsibilities

| Member              | Main Responsibility                    | Backup Responsibility          |
| ------------------- | ------------------------------------- | ------------------------------ |
| Shaima Saleem      | CPU & Instruction Execution           | Integration & GitHub           |
| Niranjana S        | Memory & Stack                        | CPU Support                    |
| Amra Fathima       | Data Structures & Process Management  | Testing                        |
| Thejes Santhosh    | OS Scheduling & Context Switching    | UI & Integration               |

## 7. Selected Programming Language

**Java**

We select this language because it supports OOP and offers suitable data structures for realization of CPU, memory, processes, PCB, scheduling algorithms and user interface.

## 8. Initial System Architecture

<img width="1214" height="1295" alt="cpu arch" src="https://github.com/user-attachments/assets/79fb5536-6027-44b5-b65f-f03fb51ae17b" />



## 9. Initial Development Plan

### Week 1 - Planning

* Form the team and distribute responsibilities.
* Understand the architecture of STC89C52.
* Choose Java as the language.
* Configure GitHub and create project documentation.
* Create a design of the initial system architecture.

### Week 2 - CPU and Memory

* Implementation of registers, PC, flags, memory, and stack.
* Creating the format of the instruction.
* Implementing some instructions.

### Week 3 - Processes and Scheduling

* Implementation of PCB and process states.
* Implementing ready queue and context switching.
* Implementing FCFS, Round Robin and Priority Scheduling.

### Week 4 - Peripherals and UI

* Adding GPIO, timer and interrupts simulation.
* Development of the User Interface.
* Program loading, running, resetting and stepping.

### Final Phase – Testing and Integration

* All components integration.
* Instruction execution and scheduling testing.
* Calculating of the performance parameters.
* Fixing problems and creating final documentation.

## 10. Expected Outcome

The expected result is that the final emulator will be able to **load and execute programs, simulate microcontroller operations, handle multiple processes and various CPU scheduling algorithms**.
