# Emulated CPU
[![Build Status](https://travis-ci.org/jakub-gonet/emulated_cpu.svg?branch=master)](https://travis-ci.org/jakub-gonet/emulated_cpu)

## Description
Simple CPU emulator in Java.

## Code examples
[CPU test file](https://github.com/jakub-gonet/emulated_cpu/blob/master/src/test/java/emulated_cpu/cpu/CPUTest.java) contains some examples of working code, for example Fibbonacci numbers and factorial calculator and bubble sort implementation.
Most of code examples have provided assembly program.

### Currently implemented:
* [assembler written in Elixir](https://github.com/jakub-gonet/emulated_cpu-assembler)
* 4 addressing modes (immediate, register, memory and IOInterface)
* 26 OP codes
* memory implementation
* ALU + registers, as well as status register with Z/C/N flags
* MOV instruction which moves data from/to memory and registers.
* jump instructions to branch code
* basic math operators
* stack
* partially support for C based functions (CALL)
* logging with leveling (DEBUG, INFO)
* IO interfaces support (devices like virtual hard drive, CD's, networking cards, graphic cards, etc)

### TODO:
* full multicore design
* offset addressing for arrays
* interrupts and interrupt vector table
* replace some code with Lombok's adnotations

## Contribution
I'm open to all PRs, project is organizated like so:    
src/main/java/emulated_cpu/ - main directory    
Command - manages single command (OP code + args)    

/src/main/java/emulated_cpu/cpu/CPU - entry point    

CPU is divided into three main parts: 
ALU (arithmetic & logic unit) + registers    
CU - central processing unit    
Memory    
