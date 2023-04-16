	%INITIALIZE CODE FROM MAIN 
	entry
	addi r14,r0,topaddr
	%ASSIGN VALUE
	addi r1,r0,1
	sw mainx(r0),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,3
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,4
	sw mainarr(r3),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,2
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	addi r1,r0,10
	sw mainarr(r3),r1
	%MULTIPLY OR DIVIDE
	addi r1,r0,2
	addi r2,r0,3
	mul r1,r1,r2
	sw temp0(r0),r1
	%ADD OR SUB VALUES
	lw r2,temp0(r0)
	addi r1,r0,1
	add r1,r1,r2
	sw temp1(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp1(r0)
	addi r2,r0,4
	add r1,r1,r2
	sw temp2(r0),r1
	%ASSIGN VALUE
	lw r1,temp2(r0)
	sw maincounter(r0),r1
	%WRITE EXPRESSION
	lw r13,maincounter(r0)
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
	addi r1,r0,1
	sw maincounter(r0),r1
	%ADD OR SUB VALUES
	addi r1,r0,2
	addi r2,r0,1
	add r1,r1,r2
	sw temp3(r0),r1
	%ADD OR SUB VALUES
	lw r1,mainx(r0)
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
	lw r1,mainarr(r3)
	sw temp5(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,mainx(r0)
	addi r2,r0,3
	mul r1,r1,r2
	sw temp6(r0),r1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp6(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,mainarr(r3)
	sw temp7(r0),r1
	%MULTIPLY OR DIVIDE
	lw r2,temp7(r0)
	lw r1,temp5(r0)
	mul r1,r1,r2
	sw temp8(r0),r1
	%ADD OR SUB VALUES
	lw r1,mainx(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp9(r0),r1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp9(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,mainarr(r3)
	sw temp10(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,mainx(r0)
	addi r2,r0,3
	mul r1,r1,r2
	sw temp11(r0),r1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp11(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,mainarr(r3)
	sw temp12(r0),r1
	%ADD OR SUB VALUES
	lw r2,temp12(r0)
	lw r1,temp10(r0)
	add r1,r1,r2
	sw temp13(r0),r1
	%MULTIPLY OR DIVIDE
	addi r1,r0,100
	lw r2,temp13(r0)
	mul r1,r1,r2
	sw temp14(r0),r1
	%ADD OR SUB VALUES
	lw r2,temp14(r0)
	lw r1,temp8(r0)
	add r1,r1,r2
	sw temp15(r0),r1
	%ASSIGN VALUE
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp3(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,temp15(r0)
	sw mainarr(r3),r1
	%MULTIPLY OR DIVIDE
	addi r1,r0,1
	addi r2,r0,3
	mul r1,r1,r2
	sw temp16(r0),r1
	add r5,r0,r0
	addi r6,r0,0
	lw r4,temp16(r0)
	add r6,r6,r4
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,mainarr(r3)
	sw temp17(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp17(r0)
	addi r2,r0,4
	add r1,r1,r2
	sw temp18(r0),r1
	%ASSIGN VALUE
	lw r1,temp18(r0)
	sw maincounter(r0),r1
	add r5,r0,r0
	addi r6,r0,3
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,4
	lw r1,mainarr(r3)
	sw temp19(r0),r1
	%WRITE EXPRESSION
	lw r13,temp19(r0)
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
	hlt
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainarr res 28
	%DECLARE VARIABLE
maincounter res 4
	%DECLARE VARIABLE
mainx res 4
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
temp12 res 4
temp13 res 4
temp14 res 4
temp15 res 4
temp16 res 4
temp17 res 4
temp18 res 4
temp19 res 4
