#!/usr/bin/env python
import inquirer
import os

OPCodes = [
    {"name": "NOP", "requiredArgs": 0},
    {"name": "HLT", "requiredArgs": 0},
    {"name": "MOV", "requiredArgs": 2},
    {"name": "JMP", "requiredArgs": 1},
    {"name": "JE", "requiredArgs": 1},
    {"name": "JNE", "requiredArgs": 1},
    {"name": "JL", "requiredArgs": 1},
    {"name": "JLE", "requiredArgs": 1},
    {"name": "JG", "requiredArgs": 1},
    {"name": "JGE", "requiredArgs": 1},
    {"name": "PUSH", "requiredArgs": 1},
    {"name": "POP", "requiredArgs": 0},
    {"name": "INC", "requiredArgs": 1},
    {"name": "DEC", "requiredArgs": 1},
    {"name": "ADD", "requiredArgs": 2},
    {"name": "SUB", "requiredArgs": 2},
    {"name": "MUL", "requiredArgs": 2},
    {"name": "DIV", "requiredArgs": 2},
    {"name": "AND", "requiredArgs": 2},
    {"name": "OR", "requiredArgs": 2},
    {"name": "XOR", "requiredArgs": 2},
    {"name": "NOT", "requiredArgs": 1},
    {"name": "RSHFT", "requiredArgs": 2},
    {"name": "LSHFT", "requiredArgs": 2},
    {"name": "CMP", "requiredArgs": 2}
]

ADDR_TYPE = [
    "CONST",
    "REG",
    "ADDR",
    "ADDR_IN_REGISTER"
]

options = [
    "Convert from command to hex",
    "Convert from hex to command",
    "Convert from old (2 bit) addressing system to new (3 bit)",
    "Quit"
]


def numberToCommand(hex):
    return OPCodes[hex >> 6]["name"], ADDR_TYPE[(hex >> 3) & 0x7],  ADDR_TYPE[hex & 0x7]


def commandToNumber(opCode, arg1, arg2):
    opCodeValue = next(OPCodes.index(x)
                       for x in OPCodes if x["name"] == opCode)

    addrType1 = ADDR_TYPE.index(arg1) if arg1 != None else 0
    addrType2 = ADDR_TYPE.index(arg2) if arg2 != None else 0
    return opCodeValue << 6 | addrType1 << 3 | addrType2


def twoBitAddressingModeToThreeBitAddressingMode(fullOpCode):
    opCode = fullOpCode >> 4
    addrType1 = (fullOpCode >> 2) & 0x3
    addrType2 = fullOpCode & 0x3
    return opCode << 6 | addrType1 << 3 | addrType2


def clearScreen():
    os.system('cls' if os.name == 'nt' else 'clear')


def printOptions(message, choices):
    option = [
        inquirer.List(name="choice",
                      message=message,
                      choices=choices)
    ]

    answer = inquirer.prompt(option)["choice"]
    clearScreen()
    return answer


def getCommandFromUser():
    opCodeName = printOptions(
        "Choose OP code", (list(x["name"] for x in OPCodes)))
    argumentsNumber = next(x["requiredArgs"]
                           for x in OPCodes if x["name"] == opCodeName)
    arg1 = None
    arg2 = None

    if argumentsNumber == 2:
        arg1 = printOptions("Choose first argument", ADDR_TYPE)
        arg2 = printOptions("Choose second argument", ADDR_TYPE)
    if argumentsNumber == 1:
        arg1 = printOptions("Choose first argument", ADDR_TYPE)
    return opCodeName, arg1, arg2


def getNumberFromUser():
    print("Write command code in hex: ")
    return int(input(), 16)
    #***


print("====> Emulated CPU command converter <====\n")
answer = printOptions("Options", options)
clearScreen()
if answer == options[0]:
    opCode, arg1, arg2 = getCommandFromUser()
    print(hex(commandToNumber(opCode, arg1, arg2)))
elif answer == options[1]:
    print(numberToCommand(getNumberFromUser()))
elif answer == options[2]:
    print(hex(twoBitAddressingModeToThreeBitAddressingMode(getNumberFromUser())))
elif answer == options[3]:
    exit()
