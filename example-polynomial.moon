POLYNOMIALevaluate add r12,r0,r15
	%RETURN EXPRESSION
	addi r1,r0,0
	sw POLYNOMIALevaluateret(r0),r1
%return from POLYNOMIALevaluate
	add r15,r0,r12
	jr r15

LINEARconstructor add r12,r0,r15
	%ASSIGN VALUE
	lw r1,LINEARconstructorA(r0)
	sw LINEARconstructora(r0),r1
	%ASSIGN VALUE
	lw r1,LINEARconstructorB(r0)
	sw LINEARconstructorb(r0),r1
	add r15,r0,r12
	jr r15

LINEARevaluate add r12,r0,r15
	%ASSIGN VALUE
	addi r1,r0,0
	sw LINEARevaluateresult(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,LINEARevaluatea(r0)
	lw r2,LINEARevaluatex(r0)
	mul r1,r1,r2
	sw temp0(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp0(r0)
	lw r2,LINEARevaluateb(r0)
	add r1,r1,r2
	sw temp1(r0),r1
	%ASSIGN VALUE
	lw r1,temp1(r0)
	sw LINEARevaluateresult(r0),r1
	%RETURN EXPRESSION
	lw r1,LINEARevaluateresult(r0)
	sw LINEARevaluateret(r0),r1
%return from LINEARevaluate
	add r15,r0,r12
	jr r15

	%INITIALIZE CODE FROM MAIN 
	entry
	addi r14,r0,topaddr
	addi r1,r0,14
	sw LINEARconstructorA(r0),r1
	addi r1,r0,10
	sw LINEARconstructorB(r0),r1
jl r15,LINEARconstructor
	addi r4,r0,0
	addi r3,r4,0
	lw r1,LINEARconstructora(r0)
	sw mainf2(r3),r1
	addi r3,r4,4
	lw r1,LINEARconstructorb(r0)
	sw mainf2(r3),r1
	%ASSIGN VALUE
	addi r1,r0,1
	sw maincounter(r0),r1
	addi r1,r0,2
	sw LINEARconstructorA(r0),r1
	addi r1,r0,2
	sw LINEARconstructorB(r0),r1
jl r15,LINEARconstructor
	add r5,r0,r0
	addi r6,r0,1
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,8
	add r4,r0,r3
	addi r3,r4,0
	lw r1,LINEARconstructora(r0)
	sw mainf1(r3),r1
	addi r3,r4,4
	lw r1,LINEARconstructorb(r0)
	sw mainf1(r3),r1
	%ASSIGN VALUE
	addi r1,r0,4
	sw LINEARconstructorA(r0),r1
	addi r1,r0,4
	sw LINEARconstructorB(r0),r1
jl r15,LINEARconstructor
	add r5,r0,r0
	addi r6,r0,2
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,8
	add r4,r0,r3
	addi r3,r4,0
	lw r1,LINEARconstructora(r0)
	sw mainf1(r3),r1
	addi r3,r4,4
	lw r1,LINEARconstructorb(r0)
	sw mainf1(r3),r1
	%ASSIGN VALUE
	addi r1,r0,3
	sw LINEARconstructorA(r0),r1
	addi r1,r0,3
	sw LINEARconstructorB(r0),r1
jl r15,LINEARconstructor
	add r4,r0,r0
	addi r3,r4,0
	lw r1,LINEARconstructora(r0)
	sw mainf3(r3),r1
	addi r3,r4,4
	lw r1,LINEARconstructorb(r0)
	sw mainf3(r3),r1
	%ASSIGN VALUE
	%WHILE LOOP STARTS 
gowhile0 	%RELATIONAL OPERATIONAL
	addi r3,r0,10
	lw r2,maincounter(r0)
	cle r1,r2,r3
	bz r1,endwhile0
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
	%ADD OR SUB VALUES
	lw r1,maincounter(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp2(r0),r1
	%ASSIGN VALUE
	lw r1,temp2(r0)
	sw maincounter(r0),r1
	j gowhile0
	%WHILE LOOP ENDS
endwhile0 	add r5,r0,r0
	addi r6,r0,2
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,8
	lw r1,mainf1(r3)
	sw temp3(r0),r1
	add r5,r0,r0
	addi r6,r0,2
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,8
	add r4,r3,r0
	addi r3,r4,0
	lw r1,mainf1(r3)
	sw LINEARevaluatea(r0),r1
	addi r3,r4,4
	lw r1,mainf1(r3)
	sw LINEARevaluateb(r0),r1
	lw r1,maincounter(r0)
	sw LINEARevaluatex(r0),r1
jl r15,LINEARevaluate
	lw r1,LINEARevaluatex(r0)
	sw maincounter(r0),r1
	lw r1,LINEARevaluateret(r0)
	sw temp4(r0),r1
	%WRITE EXPRESSION
	lw r13,temp4(r0)
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
	addi r3,r0,0
	lw r1,mainf3(r3)
	sw temp5(r0),r1
	%WRITE EXPRESSION
	lw r13,temp5(r0)
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
	addi r3,r0,4
	lw r1,mainf3(r3)
	sw temp6(r0),r1
	%WRITE EXPRESSION
	lw r13,temp6(r0)
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
	addi r4,r0,0
	addi r3,r4,0
	lw r1,mainf3(r3)
	sw LINEARevaluatea(r0),r1
	addi r3,r4,4
	lw r1,mainf3(r3)
	sw LINEARevaluateb(r0),r1
	addi r1,r0,5
	sw LINEARevaluatex(r0),r1
jl r15,LINEARevaluate
	lw r1,LINEARevaluateret(r0)
	sw temp7(r0),r1
	%WRITE EXPRESSION
	lw r13,temp7(r0)
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
	addi r3,r0,0
	lw r1,mainf2(r3)
	sw temp8(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,maincounter(r0)
	addi r2,r0,3
	mul r1,r1,r2
	sw temp9(r0),r1
	%ADD OR SUB VALUES
	lw r2,temp9(r0)
	lw r1,mainf2(r0)
	add r1,r1,r2
	sw temp10(r0),r1
	%ADD OR SUB VALUES
	addi r1,r0,3
	addi r2,r0,4
	add r1,r1,r2
	sw temp11(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,maincounter(r0)
	lw r2,temp11(r0)
	mul r1,r1,r2
	sw temp12(r0),r1
	%ADD OR SUB VALUES
	lw r2,temp12(r0)
	lw r1,temp10(r0)
	add r1,r1,r2
	sw temp13(r0),r1
	%WRITE EXPRESSION
	lw r13,temp13(r0)
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
	%DECLARE VARIABLE
POLYNOMIALevaluatex res 4
POLYNOMIALevaluateret res 4
	%DECLARE VARIABLE
LINEARconstructorfunction res 0
	%DECLARE VARIABLE
LINEARconstructorA res 4
	%DECLARE VARIABLE
LINEARconstructorB res 4
	%DECLARE VARIABLE
LINEARconstructora res 4
	%DECLARE VARIABLE
LINEARconstructorb res 4
	%DECLARE VARIABLE
LINEARevaluatex res 4
	%DECLARE VARIABLE
LINEARevaluateresult res 4
	%DECLARE VARIABLE
LINEARevaluatea res 0
temp0 res 4
	%DECLARE VARIABLE
LINEARevaluateb res 0
temp1 res 4
LINEARevaluateret res 4
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainf1 res 40
	%DECLARE VARIABLE
mainf3 res 8
	%DECLARE VARIABLE
mainf2 res 8
	%DECLARE VARIABLE
maincounter res 4
temp2 res 4
temp3 res 4
temp4 res 4
	%DECLARE VARIABLE
maina res 0
temp5 res 4
	%DECLARE VARIABLE
mainb res 0
temp6 res 4
temp7 res 4
temp8 res 4
temp9 res 4
temp10 res 4
temp11 res 4
temp12 res 4
temp13 res 4
