# Assembly Language for Emulated CPU

Assembly language for Emulated CPU project was created with idea of creating simple,
full operational language. 

This is cause why it only contains 4 addressing modes and 26 OP codes.

## == ADDRESSING MODES ==

List of addressing modes:
(number of addressing mode - name)
```
0 - CONST 
1 - REGISTER
2 - ADDRESS
3 - ADDRESS_IN_REGISTER
```
`CONST` - simply an integer literal, doesn't have special syntax

`REGISTER` - this is how value in register is retrieved. If register address is out of range
exception is thrown.
Syntax:
`[n]`, where n is register address.

`ADDRESS` - used as memory reference. 
Syntax: 
`&n`, where n is memory address.

`ADDRESS_IN_REGISTER` - used for getting value from memory address which is stored 
in specific register.
Syntax:
`&[n]`, where n is register address containing a memory address.

## == OP CODES ==

Every command (with few exceptions) is constructed like so:
command with no parameters: `COMMAND`
command with one parameter: `COMMAND <addr_mode except CONST mode>`
command with two parameters: `COMMAND <addr_mode except CONST mode>,
				      <addr_mode>`


Additionally, every `CONST` value can be replaced by `LABEL` which points at some 
place in code. 
For example:
```
loop:
INC [1]
JMP loop
```
`loop` will be converted to `0` after compilation, because `MOV` is located at first address
of a memory (`0x0`). Therefore, this code could be written as:
```
INC [1]
JMP 0
```
But it raises compilation error, as `CONST` values cannot be used as first arguments.

List of OP codes:
(OP code number - mnemonic - arguments count)
```
0 - NOP    (0)
1 - HLT    (0)
2 - MOV    (2)
3 - JMP    (1, label)
4 - JE	   (1, label)
5 - JNE    (1, label)
6 - JL     (1, label)
7 - JLE    (1, label)
8 - JG     (1, label)
9 - JGE    (1, label)
10 - PUSH  (1)
11 - POP   (1)
12 - CALL  (1, label)
13 - INC   (1)
14 - DEC   (1)
15 - ADD   (2)
16 - SUB   (2)
17 - MUL   (2)
18 - DIV   (2)
19 - AND   (2)
20 - OR    (2)
21 - XOR   (2)
22 - NOT   (1)
23 - RSHFT (2)
24 - LSHFT (2)
25 - CMP   (2)
```

NOP   - no operation, mostly used as placeholder  
HLT   - stops processor. Sets isStopped flag  
MOV   - moves data from one place to another  
JMP   - jumps to specified label  
JE    - jumps to specified label if `ZERO` flag is set  
JNE   - jumps to specified label if `ZERO` flag is cleared  
JL    - jumps to specified label if `NEGATIVE` flag is set  
JLE   - jumps to specified label if `NEGATIVE` or `ZERO` flag is set  
JG    - jumps to specified label if `CARRY` flag is set  
JGE   - jumps to specified label if `CARRY` or `ZERO` flag is set  
PUSH  - pushes data to stack  
POP   - pops data from stack  
CALL  - pushes instruction pointer to stack and jumps to specified label  
INC   - increments data from provided address  
DEC   - decrements data from provided address  
ADD   - adds data from one address to data from other address and stores 
	result in first address  
SUB   - subtracts data from one address from data from other address and
	stores result in first address  
MUL   - multiplies data from one address by data from other address and
	stores result in first address  
DIV   - divides data from one address by data from other address and
	stores result in first address  
AND   - performs bitwise AND on corresponding bits from two data addresses  
OR    - performs bitwise OR on corresponding bits from two data addresses  
XOR   - performs bitwise XOR on corresponding bits from two data addresses  
NOT   - negates all bits in data from provided address  
RSHFT - shifts all bits in data from provided address `n` times to right  
LSHFT - shifts all bits in data from provided address `n` times to left  
CMP   - compares data from one address with data from other address and sets 
	flags in ALU  
Every jump OP code modifies instruction pointer.  

ALU flags:
```
ZERO flag
NEGATIVE flag
CARRY flag
```
Other:
```
IS_STOPPED flag
INSTRUCTION_POINTER
```

