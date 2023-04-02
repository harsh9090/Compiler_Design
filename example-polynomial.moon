printArray 	%WRITE EXPRESSION
	lw r13,printArraysize(r0)
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
	%WRITE EXPRESSION
	add r5,r0,r0
	addi r6,r0,0
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r13,printArrayarr(r3)
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
	%ASSIGN VALUE
	lw r1,printArraysize(r0)
	sw printArrayn(r0),r1
	%ASSIGN VALUE
	addi r1,r0,0
	sw printArrayi(r0),r1
	%WHILE LOOP STARTS 
gowhile0 	%RELATIONAL OPERATIONAL
	lw r2,printArrayi(r0)
	lw r3,printArrayn(r0)
	clt r1,r2,r3
	bz r1,endwhile0
	%ADD OR SUB VALUES
	lw r1,printArrayi(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp0(r0),r1
	%ASSIGN VALUE
	lw r1,temp0(r0)
	sw printArrayi(r0),r1
	j gowhile0
	%WHILE LOOP ENDS
endwhile0 jl r15,endprintArray

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
	addi r1,r0,90
	sw mainarr(r3),r1
	%WRITE EXPRESSION
	add r5,r0,r0
	addi r6,r0,6
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r13,mainarr(r3)
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
	addi r3,r0,16
	lw r1,mainarr(r3)
	sw printArrayarr(r3),r1
	lw r13,mainarr(r3)
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
	addi r1,r0,7
	sw printArraysize(r0),r1
jl r15,printArray
endprintArray 	hlt
	%DECLARE VARIABLE
printArrayarr res 28
	%DECLARE VARIABLE
printArraysize res 4
	%DECLARE VARIABLE
printArrayn res 4
	%DECLARE VARIABLE
printArrayi res 4
temp0 res 4
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainarr res 28
