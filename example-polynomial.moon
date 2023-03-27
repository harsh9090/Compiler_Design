	entry
	addi r14,r0,topaddr
	addi r0,r1,mainx
	addi r0,r1,mainy
	addi r0,r1,mainz
	hlt
mainx res 24
mainy res 4
mainz res 4
