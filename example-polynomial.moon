	%INITIALIZE CODE FROM MAIN 
	entry
	addi r14,r0,topaddr
	%ASSIGN VALUE
	addi r1,r0,6
	sw maini(r0),r1
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
	addi r1,r0,25
	sw mainarr(r3),r1
	%ADD OR SUB VALUES
	lw r1,maini(r0)
	addi r2,r0,5
	sub r1,r1,r2
	sw temp0(r0),r1
	%ADD OR SUB VALUES
	lw r1,maini(r0)
	addi r2,r0,6
	sub r1,r1,r2
	sw temp1(r0),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp0(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,mainarr(r3)
	sw mainarr(r3),r1
	hlt
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainarr res 28
	%DECLARE VARIABLE
maini res 4
temp0 res 4
temp1 res 4
