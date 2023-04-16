bubbleSort add r12,r0,r15
	%ASSIGN VALUE
	lw r1,bubbleSortsize(r0)
	sw bubbleSortn(r0),r1
	%ASSIGN VALUE
	addi r1,r0,0
	sw bubbleSorti(r0),r1
	%ASSIGN VALUE
	addi r1,r0,0
	sw bubbleSortj(r0),r1
	%ASSIGN VALUE
	addi r1,r0,0
	sw bubbleSorttemp(r0),r1
	%WHILE LOOP STARTS 
gowhile0 	%ADD OR SUB VALUES
	lw r1,bubbleSortn(r0)
	addi r2,r0,1
	sub r1,r1,r2
	sw temp0(r0),r1
	%RELATIONAL OPERATIONAL
	lw r3,temp0(r0)
	lw r2,bubbleSorti(r0)
	clt r1,r2,r3
	bz r1,endwhile0
	%ASSIGN VALUE
	addi r1,r0,0
	sw bubbleSortj(r0),r1
	%WHILE LOOP STARTS 
gowhile1 	%ADD OR SUB VALUES
	lw r1,bubbleSortn(r0)
	lw r2,bubbleSorti(r0)
	sub r1,r1,r2
	sw temp1(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp1(r0)
	addi r2,r0,1
	sub r1,r1,r2
	sw temp2(r0),r1
	%RELATIONAL OPERATIONAL
	lw r3,temp2(r0)
	lw r2,bubbleSortj(r0)
	clt r1,r2,r3
	bz r1,endwhile1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,bubbleSortj(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,bubbleSortarr(r3)
	sw temp3(r0),r1
	%ADD OR SUB VALUES
	lw r1,bubbleSortj(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp4(r0),r1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp4(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,bubbleSortarr(r3)
	sw temp5(r0),r1
	%RELATIONAL OPERATIONAL
	lw r3,temp5(r0)
	lw r2,temp3(r0)
	cgt r1,r2,r3
	bz r1,else0
	add r5,r0,r0
	addi r6,r0,0
	lw r4,bubbleSortj(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,bubbleSortarr(r3)
	sw temp6(r0),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,0
	lw r4,bubbleSortj(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,temp6(r0)
	sw bubbleSorttemp(r0),r1
	%ADD OR SUB VALUES
	lw r1,bubbleSortj(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp7(r0),r1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp7(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,bubbleSortarr(r3)
	sw temp8(r0),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,0
	lw r4,bubbleSortj(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,temp8(r0)
	sw bubbleSortarr(r3),r1
	%ADD OR SUB VALUES
	lw r1,bubbleSortj(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp9(r0),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp9(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,bubbleSorttemp(r0)
	sw bubbleSortarr(r3),r1
	j endif0
else0 endif0 	%ADD OR SUB VALUES
	lw r1,bubbleSortj(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp10(r0),r1
	%ASSIGN VALUE
	lw r1,temp10(r0)
	sw bubbleSortj(r0),r1
	j gowhile1
	%WHILE LOOP ENDS
endwhile1 	%ADD OR SUB VALUES
	lw r1,bubbleSorti(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp11(r0),r1
	%ASSIGN VALUE
	lw r1,temp11(r0)
	sw bubbleSorti(r0),r1
	j gowhile0
	%WHILE LOOP ENDS
endwhile0 	add r15,r0,r12
	jr r15

printArray add r12,r0,r15
	%ASSIGN VALUE
	lw r1,printArraysize(r0)
	sw printArrayn(r0),r1
	%ASSIGN VALUE
	addi r1,r0,0
	sw printArrayi(r0),r1
	%WHILE LOOP STARTS 
gowhile2 	%RELATIONAL OPERATIONAL
	lw r3,printArrayn(r0)
	lw r2,printArrayi(r0)
	clt r1,r2,r3
	bz r1,endwhile2
	add r5,r0,r0
	addi r6,r0,0
	lw r4,printArrayi(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,printArrayarr(r3)
	sw temp12(r0),r1
	%WRITE EXPRESSION
	lw r13,temp12(r0)
	sw -8(r14),r13
    addi r1,r0, buf
    sw -12(r14),r1
    jl r15, intstr
	add r1,r0,r13
    sw -8(r14),r1
    jl r15,putstr
	addi r1,r0,tm
	sw -8(r14),r1
	jl r15,putstr
	%ADD OR SUB VALUES
	lw r1,printArrayi(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp13(r0),r1
	%ASSIGN VALUE
	lw r1,temp13(r0)
	sw printArrayi(r0),r1
	j gowhile2
	%WHILE LOOP ENDS
endwhile2 	add r15,r0,r12
	jr r15

	%INITIALIZE CODE FROM MAIN 
	entry
	addi r14,r0,topaddr
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,0
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,64
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,1
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,34
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,2
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,25
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,3
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,12
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,22
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,5
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,11
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,6
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,1
	sw mainarr(r3),r1
	addi r6,r0,0
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	addi r1,r0,7
	sw printArraysize(r0),r1
jl r15,printArray
	addi r6,r0,0
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	addi r6,r0,0
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw bubbleSortarr(r6),r1
	addi r6,r6,4
	addi r1,r0,7
	sw bubbleSortsize(r0),r1
jl r15,bubbleSort
	addi r6,r0,0
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,bubbleSortarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	addi r6,r0,0
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	lw r1,mainarr(r6)
	sw printArrayarr(r6),r1
	addi r6,r6,4
	addi r1,r0,7
	sw printArraysize(r0),r1
jl r15,printArray
	addi r6,r0,0
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	lw r1,printArrayarr(r6)
	sw mainarr(r6),r1
	addi r6,r6,4
	hlt
	%DECLARE VARIABLE
bubbleSortarr res 28
	%DECLARE VARIABLE
bubbleSortsize res 4
	%DECLARE VARIABLE
bubbleSortn res 4
	%DECLARE VARIABLE
bubbleSorti res 4
	%DECLARE VARIABLE
bubbleSortj res 4
	%DECLARE VARIABLE
bubbleSorttemp res 4
temp0 res 4
temp1 res 4
temp2 res 4
temp3 res 4
temp4 res 4
temp5 res 4
temp6 res 4
temp7 res 4
temp8 res 4
temp9 res 4
temp10 res 4
temp11 res 4
	%DECLARE VARIABLE
printArrayarr res 28
	%DECLARE VARIABLE
printArraysize res 4
	%DECLARE VARIABLE
printArrayn res 4
	%DECLARE VARIABLE
printArrayi res 4
temp12 res 4
temp13 res 4
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainarr res 28
