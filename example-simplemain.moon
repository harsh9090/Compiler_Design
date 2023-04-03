	%INITIALIZE CODE FROM MAIN 
	entry
	addi r14,r0,topaddr
	%MULTIPLY OR DIVIDE
	addi r1,r0,2
	addi r2,r0,3
	mul r1,r1,r2
	sw temp0(r0),r1
	%ADD OR SUB VALUES
	addi r1,r0,1
	lw r2,temp0(r0)
	add r1,r1,r2
	sw temp1(r0),r1
	%ASSIGN VALUE
	lw r1,temp1(r0)
	sw mainy(r0),r1
	%READ VARIABLE VALUE
	addi    r1,r0,readx
	sw -8(r14),r1
	jl r15,putstr
	addi r1,r0,mainx
	sw -8(r14),r1
	jl r15,getstr
	addi r13,r0,mainx
	sw -8(r14),r13
    addi r1,r0, buf
    sw -12(r14),r1
    jl r15, strint
    sw mainx(r0),r13
	%ADD OR SUB VALUES
	lw r1,mainy(r0)
	addi r2,r0,10
	add r1,r1,r2
	sw temp0(r0),r1
	%RELATIONAL OPERATIONAL
	lw r2,mainx(r0)
	lw r3,temp0(r0)
	cgt r1,r2,r3
	bz r1,else0
	%ADD OR SUB VALUES
	lw r1,mainx(r0)
	addi r2,r0,10
	add r1,r1,r2
	sw temp1(r0),r1
	%WRITE EXPRESSION
	lw r13,temp1(r0)
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
	j endif0
else0 	%ADD OR SUB VALUES
	lw r1,mainx(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp2(r0),r1
	%WRITE EXPRESSION
	lw r13,temp2(r0)
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
endif0 	%ASSIGN VALUE
	addi r1,r0,0
	sw mainz(r0),r1
	%WHILE LOOP STARTS 
gowhile0 	%RELATIONAL OPERATIONAL
	lw r2,mainz(r0)
	addi r3,r0,10
	cle r1,r2,r3
	bz r1,endwhile0
	%WRITE EXPRESSION
	lw r13,mainz(r0)
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
	lw r1,mainz(r0)
	addi r2,r0,1
	add r1,r1,r2
	sw temp0(r0),r1
	%ASSIGN VALUE
	lw r1,temp0(r0)
	sw mainz(r0),r1
	j gowhile0
	%WHILE LOOP ENDS
endwhile0 	hlt
	%TAG LIST
buf res 20
tm db " , ",0
	%DECLARE VARIABLE
mainx res 4
	%DECLARE VARIABLE
mainy res 4
	%DECLARE VARIABLE
mainz res 4
temp0 res 4
temp1 res 4
readx db "read x=",0
temp2 res 4
