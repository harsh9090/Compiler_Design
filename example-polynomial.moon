POLYNOMIALevaluate add r12,r0,r15
	%RETURN EXPRESSION
	addi r1,r0,0
	sw POLYNOMIALevaluateret(r0),r1
%return from POLYNOMIALevaluate
	add r15,r0,r12
	jr r15

QUADRATICevaluate add r12,r0,r15
	%ASSIGN VALUE
	lw r1,QUADRATICevaluatea(r0)
	sw QUADRATICevaluateresult(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,QUADRATICevaluateresult(r0)
	lw r2,QUADRATICevaluatex(r0)
	mul r1,r1,r2
	sw temp0(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp0(r0)
	lw r2,QUADRATICevaluateb(r0)
	add r1,r1,r2
	sw temp1(r0),r1
	%ASSIGN VALUE
	lw r1,temp1(r0)
	sw QUADRATICevaluateresult(r0),r1
	%MULTIPLY OR DIVIDE
	lw r1,QUADRATICevaluateresult(r0)
	lw r2,QUADRATICevaluatex(r0)
	mul r1,r1,r2
	sw temp2(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp2(r0)
	lw r2,QUADRATICevaluatec(r0)
	add r1,r1,r2
	sw temp3(r0),r1
	%ASSIGN VALUE
	lw r1,temp3(r0)
	sw QUADRATICevaluateresult(r0),r1
	%RETURN EXPRESSION
	lw r1,QUADRATICevaluateresult(r0)
	sw QUADRATICevaluateret(r0),r1
%return from QUADRATICevaluate
	add r15,r0,r12
	jr r15

QUADRATICconstructor add r12,r0,r15
	%ASSIGN VALUE
	lw r1,QUADRATICconstructorA(r0)
	sw QUADRATICconstructora(r0),r1
	%ASSIGN VALUE
	lw r1,QUADRATICconstructorB(r0)
	sw QUADRATICconstructorb(r0),r1
	%ASSIGN VALUE
	lw r1,QUADRATICconstructorC(r0)
	sw QUADRATICconstructorc(r0),r1
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
	sw temp4(r0),r1
	%ADD OR SUB VALUES
	lw r1,temp4(r0)
	lw r2,LINEARevaluateb(r0)
	add r1,r1,r2
	sw temp5(r0),r1
	%ASSIGN VALUE
	lw r1,temp5(r0)
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
	addi r1,r0,-2
	sw QUADRATICconstructorA(r0),r1
	addi r1,r0,1
	sw QUADRATICconstructorB(r0),r1
	addi r1,r0,0
	sw QUADRATICconstructorC(r0),r1
jl r15,QUADRATICconstructor
	addi r4,r0,0
	addi r3,r4,0
	lw r1,QUADRATICconstructora(r0)
	sw mainf2(r3),r1
	addi r3,r4,4
	lw r1,QUADRATICconstructorb(r0)
	sw mainf2(r3),r1
	addi r3,r4,8
	lw r1,QUADRATICconstructorc(r0)
	sw mainf2(r3),r1
	addi r1,r0,3
	sw LINEARconstructorA(r0),r1
	addi r1,r0,4
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
	%ASSIGN VALUE
	addi r1,r0,1
	sw maincounter(r0),r1
	addi r1,r0,6
	sw LINEARconstructorA(r0),r1
	addi r1,r0,7
	sw LINEARconstructorB(r0),r1
jl r15,LINEARconstructor
	add r5,r0,r0
	addi r6,r0,0
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
	addi r1,r0,9
	sw LINEARconstructorA(r0),r1
	addi r1,r0,10
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
	add r5,r0,r0
	addi r6,r0,2
	muli r6,r6,1
	add r5,r5,r6
	muli r3,r5,8
	lw r1,mainf1(r3)
	sw temp6(r0),r1
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
	addi r4,r0,0
	addi r3,r4,0
	lw r1,mainf2(r3)
	sw QUADRATICevaluatea(r0),r1
	addi r3,r4,4
	lw r1,mainf2(r3)
	sw QUADRATICevaluateb(r0),r1
	addi r3,r4,8
	lw r1,mainf2(r3)
	sw QUADRATICevaluatec(r0),r1
	lw r1,maincounter(r0)
	sw QUADRATICevaluatex(r0),r1
jl r15,QUADRATICevaluate
	lw r1,QUADRATICevaluatex(r0)
	sw maincounter(r0),r1
	lw r1,QUADRATICevaluateret(r0)
	sw temp8(r0),r1
	%WRITE EXPRESSION
	lw r13,temp8(r0)
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
QUADRATICevaluatex res 4
	%DECLARE VARIABLE
QUADRATICevaluateresult res 4
	%DECLARE VARIABLE
QUADRATICevaluatea res 0
temp0 res 4
	%DECLARE VARIABLE
QUADRATICevaluateb res 0
temp1 res 4
temp2 res 4
	%DECLARE VARIABLE
QUADRATICevaluatec res 0
temp3 res 4
QUADRATICevaluateret res 4
	%DECLARE VARIABLE
QUADRATICconstructorfunction res 0
	%DECLARE VARIABLE
QUADRATICconstructorA res 4
	%DECLARE VARIABLE
QUADRATICconstructorB res 4
	%DECLARE VARIABLE
QUADRATICconstructorC res 4
	%DECLARE VARIABLE
QUADRATICconstructora res 4
	%DECLARE VARIABLE
QUADRATICconstructorb res 4
	%DECLARE VARIABLE
QUADRATICconstructorc res 4
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
temp4 res 4
	%DECLARE VARIABLE
LINEARevaluateb res 0
temp5 res 4
LINEARevaluateret res 4
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainf1 res 24
	%DECLARE VARIABLE
mainf2 res 12
	%DECLARE VARIABLE
maincounter res 4
temp6 res 4
temp7 res 4
temp8 res 4
