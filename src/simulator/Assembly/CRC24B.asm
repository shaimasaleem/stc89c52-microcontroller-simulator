ORG 0000H

; CRC-24B
; Generator Polynomial:
; D^24 + D^23 + D^6 + D^5 + D + 1
; Polynomial = 800063H

MOV R2,#00H       ; CRC high byte
MOV R3,#00H       ; CRC middle byte
MOV R4,#00H       ; CRC low byte

MOV R5,#0A5H      ; Sample input data
MOV R7,#08H       ; Number of bits = 8

; XOR input byte with CRC high byte
MOV A,R2
XRL A,R5
MOV R2,A

CRC_LOOP:

; Shift the complete 24-bit CRC one bit to the left
CLR C

MOV A,R4
RLC A
MOV R4,A

MOV A,R3
RLC A
MOV R3,A

MOV A,R2
RLC A
MOV R2,A

; After the shift, C contains the old top bit.
; If C = 0, polynomial XOR is not required.
JNC NO_XOR

; XOR CRC with polynomial 800063H

MOV A,R2
XRL A,#80H
MOV R2,A

MOV A,R3
XRL A,#00H
MOV R3,A

MOV A,R4
XRL A,#63H
MOV R4,A

NO_XOR:

DJNZ R7,CRC_LOOP
; CRC calculation completed
; Final 24-bit CRC is stored in R2:R3:R4
; R2 = CRC high byte
; R3 = CRC middle byte
; R4 = CRC low byte

DONE:
    HALT

END
