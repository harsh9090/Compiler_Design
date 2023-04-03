import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
public class CompilerDriver {
	static TreeNode root = null;
	static ArrayList<ArrayList<TreeNode>> symbolTable;
	static ArrayList<ArrayList<TreeNode>> mainTable;
	static ArrayList<String> tokens = new ArrayList<>(),parmList=new ArrayList<>();
	static String code = "";
	static String decs = "";
	static Stack<String> tempList = new Stack<>();
	static ArrayList<String> temp = new ArrayList<>();
	static int numb=0;
	static ArrayList<Integer> wLoopList = new ArrayList<>(),iLoopList = new ArrayList<>();
	static Stack<Integer> wLoop = new Stack<>(),iLoop = new Stack<>();
	private static String funcName= "";
	private static void traverseNodes(TreeNode t) {
		if(t ==null || t.children== null) return;
		if(t.val.equals("function") && t.name!=null && t.name.equals("main")) {
			code+="\t%INITIALIZE CODE FROM MAIN \n\tentry\n\taddi r14,r0,topaddr\n";
			decs += "\t%TAG LIST\nbuf res 20\n"
					+ "tm db \" , \",0\n";
		}
		if(t.val.equals("FunctionDec")) {
			funcName = t.children.get(1).name;
		}
		if(t.val.equals("localvar") || t.val.equals("FParm") || t.val.equals("FParms")) {
			int space =0;
			String name =funcName+t.children.get(0).name;
			if(t.val.equals("FParm") || t.val.equals("FParms")) {
				parmList.add(name);
			}
			if(t.children.get(0).type.equals("integer")) {
				space=4;
				if(t.children.get(0).dim.size()>0) {
					for(int i=0;i<t.children.get(2).children.size();i++) {
						space*=Integer.parseInt(t.children.get(2).children.get(i).name);
					}
				}
			}
			else if(t.children.get(0).type.equals("float")) {
				space=8;
				if(t.children.get(0).dim.size()>0) {
					for(int i=0;i<t.children.get(2).children.size();i++) {
						space*=Integer.parseInt(t.children.get(2).children.get(i).name);
					}
				}
			}
			else {
				for(int i=0;i<symbolTable.size();i++) {
					if(symbolTable.get(i).get(0).name.equals(t.children.get(0).type)) {
						space = symbolTable.get(i).get(0).store;
						name = t.children.get(0).type +t.children.get(0).name;
						decs+=name+" res "+symbolTable.get(i).get(0).store+"\n";
					}
				}
			}
			decs += "\t%DECLARE VARIABLE\n";
			decs += name+" res "+ space +"\n";
		}
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).val.equals("whileTree")) {
				if(wLoop.isEmpty()) {
					if(wLoopList.isEmpty()) {
						wLoopList.add(0);
						wLoop.push(0);
					}
					else {
						wLoopList.add(wLoopList.get(wLoopList.size()-1)+1);
						wLoop.push(wLoopList.get(wLoopList.size()-1));
					}
				}
				else {
					wLoop.push(wLoop.peek()+1);
					wLoopList.add(wLoop.peek());
				}
				code += "\t%WHILE LOOP STARTS \n";
				code += "gowhile"+wLoop.peek() + " ";
			}
			if(tokens.contains(t.children.get(i).name)) {
				traverseNodes(t.children.get(i+1));
				int x=0;
				for(x=0;x<parmList.size();x++) {
					if(parmList.get(x).contains(t.children.get(i).name)) {
						break;
					}
				}
				getParams(t.children.get(i+1),t.children.get(i).name,x);
				countPm=0;
				
				tempList.clear();
				code +="jl r15,"+t.children.get(i).name+"\n";
				for(int s=0;s<statmt.size();s++) {
					code += statmt.get(s);
				}
				statmt.clear();
				if(code.contains("%return from "+t.children.get(i).name)) {
					if(!temp.contains("temp"+numb)) {
						temp.add("temp"+numb);
						decs += "temp"+numb+" res 4\n";
					}
					code += "\tlw r1,"+t.children.get(i).name+"ret(r0)\n";
					code += "\tsw temp"+numb+"(r0),r1\n";
					tempList.push("temp"+numb);
					numb++;
				}
				else {
					
				}
				return;
			}
			traverseNodes(t.children.get(i));
			
			if(t.children.get(i).val.equals("returnFromFunction")) {
				if(!decs.contains(funcName+"ret "))
					decs +=funcName+"ret res 4";
				code += "\t%RETURN EXPRESSION\n";
				TreeNode x = t.children.get(i).children.get(0).children.get(0).children.get(0);
				if(tempList.isEmpty()) {
					x=x.children.get(0);
					if(x.children.size()==0) {
						code += "\taddi r1,r0,"+x.name +"\n";
						code += "\tsw "+funcName+"ret(r0),r1\n";
					}
					else if(x.children.get(1).children.size()==0) {
						x=x.children.get(0);
						code += "\tlw r1,"+funcName+x.name +"(r0)\n";
								code += "\tsw "+funcName+"ret(r0),r1\n";
					}
					else {
//						getArray(x);
//						code += "\tlw r13,"+funcName+x.name +"(r3)\n"
//								+ "	sw -8(r14),r13\n"
//								+ "    addi r1,r0, buf\n"
//								+ "    sw -12(r14),r1\n"
//								+ "    jl r15, intstr\n"
//								+	"\tadd r1,r0,r13\n"
//								+ "    sw -8(r14),r1\r\n"
//								+ "    jl r15,putstr\n"
//								+ "\taddi r1,r0,tm\n"
//								+ "\tsw -8(r14),r1\n"
//								+ "\tjl r15,putstr\n";
					}
					
				}
				else {
					String tem = tempList.pop();
					code += "\tlw r1,"+tem+"(r0)\n";
					code += "\tsw "+funcName+"ret(r0),r1\n";
				}
				code+="%return from "+funcName+"\n";
			}
			if(t.children.get(i).val.equals("whileTree")) {
				code +="\tbz r1,endwhile"+wLoop.peek()+"\n";
			}
			if(i>0 && t.children.get(i).val.equals("StatmentList") && t.children.get(i-1).val.equals("whileTree")) {
				
				code += "\tj gowhile"+ wLoop.peek() +"\n"
						+ "\t%WHILE LOOP ENDS\n"
						+ "endwhile"+ wLoop.peek() +" ";
				wLoop.pop();
			}
			if(t.children.get(i).val.equals("read")) {
				if(checkChild(t.children.get(i).children.get(1))) {
					if(!decs.contains("read" +t.children.get(i).children.get(0).name+ " ")) {
						decs+= "read"+ t.children.get(i).children.get(0).name +" db \"read " + t.children.get(i).children.get(0).name + "=\",0\n";
					}
					code += "\t%READ VARIABLE VALUE\n";
					code += "\taddi    r1,r0,read"+t.children.get(i).children.get(0).name+ "\n"
							+ "\tsw -8(r14),r1\r\n"
							+ "\tjl r15,putstr\n";
					code +="\taddi r1,r0,"+funcName+t.children.get(i).children.get(0).name+"\n"
							+ "\tsw -8(r14),r1\r\n"
							+ "\tjl r15,getstr\n"
							+ "\taddi r13,r0,"+funcName+t.children.get(i).children.get(0).name +"\r\n"
							+ "	sw -8(r14),r13\n"
							+ "    addi r1,r0, buf\n"
							+ "    sw -12(r14),r1\n"
							+ "    jl r15, strint\n"
							+ "    sw "+funcName+t.children.get(i).children.get(0).name+"(r0),r13\n";
				}
				else {
					TreeNode x=t.children.get(i).children.get(1);
					getArray2(x);
					x=x.parent.children.get(0);
					code += "\t%READ VARIABLE VALUE\n";
					code +="\tlw r1,"+funcName+x.name+"(r3)\n"
							+ "\tsw -8(r14),r1\r\n"
							+ "\tjl r15,getstr\n"
							+ "\tlw r13,"+funcName+x.name +"(r3)\n"
							+ "\tsw -8(r14),r13\n"
							+ "    addi r1,r0, buf\n"
							+ "    sw -12(r14),r1\n"
							+ "    jl r15, strint\n"
							+ "    sw "+funcName+t.children.get(i).children.get(0).name+"(r3),r13\n";
				}
			}
			
			
			if(t.children.get(i).val.equals("multop")) {
				code += "\t%MULTIPLY OR DIVIDE\n";
				String op = "";
				if(t.children.get(i).children.get(0).name.equals("*")) {
					op = "mul";
				}
				else {
					op = "div";
				}
				
				TreeNode t1 = t.children.get(i-1);
				TreeNode t2 = t.children.get(i).children.get(1);
				if(!(contains(t1,"multop") || contains(t1,"addop"))) {
					if(!contains(t2.parent,"addop")) {
						while(!(t1.val.equals("id") || t1.val.equals("intLit") || t1.val.equals("floatLit")) && t1.children.size()>0) {
							t1 = t1.children.get(0);
						}
						while(!(t2.val.equals("id") || t2.val.equals("intLit") || t2.val.equals("floatLit")) && t2.children.size()>0) {
							t2 = t2.children.get(0);
						}
						if(t1.val.equals("id")) {
							if(t1.parent.children.get(1).children.size()!=0) {
								getArray(t1);
								code += "\tlw r1,"+funcName+t1.name+"(r3)\n";
							}
							else code += "\tlw r1,"+funcName+t1.name+"(r0)\n";
						}
						else {
							code += "\taddi r1,r0,"+t1.name+"\n";
						}
						if(t2.val.equals("id")) {
							if(t2.parent.children.get(1).children.size()!=0) {
								getArray(t2);
								code += "\tlw r2,"+funcName+t2.name+"(r3)\n";
							}
							else {
								code += "\tlw r2,"+funcName+t2.name+"(r0)\n";
							}
						}
						else {
							code += "\taddi r2,r0,"+t2.name+"\n";
						}
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
					else {
						String tem = tempList.pop();
						if(t1.val.equals("id")) {
							if(t1.parent.children.get(1).children.size()!=0) {
								getArray(t1);
								code += "\tlw r1,"+funcName+t1.name+"(r3)\n";
							}
							else code += "\tlw r1,"+funcName+t1.name+"(r0)\n";
						}
						else {
							code += "\taddi r1,r0,"+t1.name+"\n";
						}
						code += "\tlw r2,"+tem+"(r0)\n";
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
				}
				else {
					if(!contains(t2,"addop")) {
						String tem = tempList.pop();
						if(t2.val.equals("id")) {
							if(t2.parent.children.get(1).children.size()!=0) {
								getArray(t2);
								code += "\tlw r1,"+funcName+t2.name+"(r3)\n";
							}
							else {
								code += "\tlw r1,"+funcName+t2.name+"(r0)\n";
							}
						}
						else {
							code += "\taddi r1,r0,"+t2.name+"\n";
						}
						code += "\tlw r2,"+tem+"(r0)\n";
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
					else {
						String tem = tempList.pop();
						String tem2 = tempList.pop();
						code += "\tlw r1,"+tem+"(r0)\n";
						code += "\tlw r2,"+tem2+"(r0)\n";
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
				}
			}
			
//			ADD OPERATION
			if(t.children.get(i).val.equals("addop")) {
				code += "\t%ADD OR SUB VALUES\n";
				TreeNode t1 = t.children.get(i-1).children.get(0);
				TreeNode t2 = t.children.get(i).children.get(1).children.get(0);
				String op = t.children.get(i).children.get(0).name;
				
				if(op.equals("+")) {
					op = "add";
				}
				else op="sub";
				if(!(contains(t1.parent,"multop") || contains(t1.parent,"addop"))) {
					if(!contains(t2.parent,"multop")) {
						while(!(t1.val.equals("id") || t1.val.equals("intLit") || t1.val.equals("floatLit")) && t1.children.size()>0) {
							t1 = t1.children.get(0);
						}
						while(!(t2.val.equals("id") || t2.val.equals("intLit") || t2.val.equals("floatLit")) && t2.children.size()>0) {
							t2 = t2.children.get(0);
						}
						if(t1.val.equals("id")) {
							if(t1.parent.children.get(1).children.size()!=0) {
								getArray(t1);
								code += "\tlw r1,"+funcName+t1.name+"(r3)\n";
							}
							else code += "\tlw r1,"+funcName+t1.name+"(r0)\n";
						}
						else {
							code += "\taddi r1,r0,"+t1.name+"\n";
						}
						if(t2.val.equals("id")) {
							if(t2.parent.children.get(1).children.size()!=0) {
								getArray(t2);
								code += "\tlw r2,"+funcName+t2.name+"(r3)\n";
							}
							else code += "\tlw r2,"+funcName+t2.name+"(r0)\n";
						}
						else {
							code += "\taddi r2,r0,"+t2.name+"\n";
						}
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
					else {
						
						while(!(t1.val.equals("id") || t1.val.equals("intLit") || t1.val.equals("floatLit")) && t1.children.size()>0) {
							t1 = t1.children.get(0);
						}
						while(!(t2.val.equals("id") || t2.val.equals("intLit") || t2.val.equals("floatLit")) && t2.children.size()>0) {
							t2 = t2.children.get(0);
						}
						String tem = tempList.pop();
						if(t1.val.equals("id")) {
							if(t1.parent.children.get(1).children.size()!=0) {
								getArray(t1);
								code += "\tlw r1,"+funcName+t1.name+"(r3)\n";
							}
							else code += "\tlw r1,"+funcName+t1.name+"(r0)\n";
						}
						else {
							code += "\taddi r1,r0,"+t1.name+"\n";
						}
						code += "\tlw r2,"+tem+"(r0)\n";
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
				}
				else {
					if(!contains(t2.parent,"multop")) {
						if(t1.val.equals("add")) {
							t1=t1.parent.children.get(1);
						}
						while(!(t1.val.equals("id") || t1.val.equals("intLit") || t1.val.equals("floatLit")) && t1.children.size()>0) {
							t1 = t1.children.get(0);
						}
						while(!(t2.val.equals("id") || t2.val.equals("intLit") || t2.val.equals("floatLit")) && t2.children.size()>0) {
							t2 = t2.children.get(0);
						}
						String tem = tempList.pop();
						if(t1.val.equals("id")) {
							if(t1.parent.children.get(1).children.size()>0) {
								getArray(t1);
								code += "\tlw r1,"+funcName+t1.name+"(r3)\n";
							}
							else code += "\tlw r2,"+tem+"(r0)\n";
						}
						else code += "\tlw r2,"+tem+"(r0)\n";
						if(t2.val.equals("id")) {
							if(t2.parent.children.get(1).children.size()!=0) {
								getArray(t2);
								code += "\tlw r1,"+funcName+t2.name+"(r3)\n";
							}
							else code += "\tlw r1,"+funcName+t2.name+"(r0)\n";
						}
						else {
							code += "\taddi r1,r0,"+t2.name+"\n";
						}
						code += "\t"+op+" r1,r2,r1\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
					else {
						String tem = tempList.pop();
						String tem2 = tempList.pop();
						code += "\tlw r1,"+tem+"(r0)\n";
						code += "\tlw r2,"+tem2+"(r0)\n";
						code += "\t"+op+" r1,r1,r2\n";
						if(!temp.contains("temp"+numb)) {
							temp.add("temp"+numb);
							decs += "temp"+numb+" res 4\n";
						}
						code += "\tsw temp"+numb+"(r0),r1\n";
						tempList.push("temp"+numb);
						numb++;
					}
				}
				
			}
			if(t.children.get(i).val.equals("=")) {
				code += "\t%ASSIGN VALUE\n";
				TreeNode tmp1 = t.children.get(i);
				while(!(tmp1.val.equals("id") || tmp1.val.equals("intLit") || tmp1.val.equals("floatLit")) && tmp1.children.size()>0) {
					tmp1 = tmp1.children.get(0);
				}
//				ALL MEMBERS WITH x = .....
				if(t.children.get(i-1).val.equals("id")) {
					if(!t.children.get(i-1).parent.val.equals("dot")) {
						if(tempList.isEmpty()) {
							if(!tmp1.val.equals("id")) {
								code += "\taddi r1,r0,"+tmp1.name+"\n";
							}
							else {
								if(!tmp1.val.equals("intLit") && tmp1.parent.children.get(1).children.size()>0) {
									getArray(tmp1);
									code += "\tlw r1,"+funcName+tmp1.name+"(r3)\n";
								}
								else code += "\tlw r1,"+funcName +tmp1.name+"(r0)\n";
							}
						}
						else {
							if(!tmp1.val.equals("intLit") && tmp1.parent.children.get(1).children.size()>0) {
								getArray(tmp1);
								if(tempList.isEmpty()) {
								code += "\tlw r1,"+funcName+tmp1.name+"(r3)\n";
								}
								else {
									code += "\tlw r1,"+tempList.pop()+"(r0)\n";
								}
							}
							else {
								code += "\tlw r1,"+tempList.pop()+"(r0)\n";
							}
						}
						code += "\tsw "+funcName+t.children.get(i-1).name+"(r0),r1\n";
					}
					else {
						code += "code here for assign class.variable!\n";
					}
					
				}
//				ALL MEMBERS WITH ARRAY SIZE ARR[X] or ARR[1] or ARR[x+1] = .....
				else {
					if(tempList.isEmpty()) {
						TreeNode x = t.children.get(i-1).parent;
						getArray2(x);
						TreeNode v = x;
						x=x.parent.children.get(0);
						v=v.children.get(1);
						while(!(v.val.equals("id") || v.val.equals("intLit") || v.val.equals("floatLit")) && v.children.size()>0) {
							v = v.children.get(0);
						}
						if(v.val.equals("id")) {
							code += "\tlw r1,"+funcName+v.name+"(r0)\n"
									+ "\tsw "+funcName+x.name+"(r3),r1\n";
						}
						else {
							code += "\taddi r1,r0,"+v.name+"\n"
									+ "\tsw "+funcName+x.name+"(r3),r1\n";
						}
					}
					else {
						TreeNode x = t.children.get(i-1).parent;
						getArray2(x);
						TreeNode v = x;
						x=x.parent.children.get(0);
						v=v.children.get(1);
						while(!(v.val.equals("id") || v.val.equals("intLit") || v.val.equals("floatLit")) && v.children.size()>0) {
							v = v.children.get(0);
						}
						if(!tempList.isEmpty()) {
							if(v.val.equals("id")) {
								if(v.parent.children.get(1).children.size()>0) {
									code +="\tadd r7,r0,r3\n";
									getArray(v);
									
									code += "\tlw r1,"+funcName+ v.name+"(r3)\n"
											+ "\tsw "+funcName+x.name+"(r7),r1\n";
								}
								else {
									code += "\tlw r1,"+tempList.pop()+"(r0)\n"
											+ "\tsw "+funcName+x.name+"(r3),r1\n";
								}
							}
							else {
								code += "\taddi r1,r0,"+v.name+"\n"
										+ "\tsw "+funcName+x.name+"(r3),r1\n";
							}		
						}
						else {
							if(v.val.equals("id")) {
								code += "\tlw r1,"+funcName +v.name+"(r0)\n"
										+ "\tsw "+funcName+x.name+"(r3),r1\n";
							}
							else {
								code += "\taddi r1,r0,"+v.name+"\n"
										+ "\tsw "+funcName+x.name+"(r3),r1\n";
							}
						}
					}
				}
				numb=0;
				tempList.clear();
			}
			if(t.children.get(i).val.equals("ifTree")) {
				if(iLoop.isEmpty()) {
					if(iLoopList.isEmpty()) {
						iLoopList.add(0);
						iLoop.push(0);
					}
					else {
						iLoopList.add(iLoopList.get(iLoopList.size()-1)+1);
						iLoop.push(iLoopList.size()-1);
					}
				}
				else {
					iLoop.push(iLoop.peek()+1);
					iLoopList.add(iLoop.peek());
				}
				code += "\tbz r1,else"+ iLoop.peek() +"\n";
						
			}
			if(t.children.get(i).val.equals("then")) {
				code += "\tj endif"+iLoop.peek()+"\n";
				code += "else"+iLoop.peek()+" ";
			}
			if(t.children.get(i).val.equals("else")) {
				code += "endif"+iLoop.peek() +" ";
				iLoop.pop();
			}
//			RELATIONAL OPERATIONS
			if(t.children.get(i).val.equals("relop")) {
				code += "\t%RELATIONAL OPERATIONAL\n";
				TreeNode t1,t2;
				String op = t.children.get(i).children.get(1).name;
				String[] lis = {"ceq","==","cne","!=","clt","<","cle","<=","cgt",">","cge",">="};
				for(int k=1;k<lis.length;k=k+2) {
					if(lis[k].equals(op)) {
						op = lis[k-1];
						break;
					}
				}
				t1= t.children.get(i).children.get(0).children.get(0).children.get(0);
				t2= t.children.get(i).children.get(2).children.get(0).children.get(0);
				if(contains(t1.parent.parent,"addop") || contains(t1.parent.parent,"multop")) {
					if(!t1.val.equals("intLit")) {
						t1 =t1.children.get(0);
					}
					code += "\tlw r2,"+tempList.pop()+"(r0)\n";
				}
				else {
					if(!t1.val.equals("intLit")) {
						t1 =t1.children.get(0);
					}
					if(t1.val.equals("intLit")) {
						code += "\taddi r2,r0,"+t1.name+"\n";
					}
					else if(t1.val.equals("id") && t1.parent.children.get(1).children.size()==0) {
						code += "\tlw r2,"+funcName+t1.name+"(r0)\n";
					}
					else {
						getArray(t1);
						code += "\tlw r2,"+funcName+t1.name+"(r3)\n";
					}
				}
				if(contains(t2.parent.parent,"addop") || contains(t2.parent.parent,"multop")) {
					if(!t2.val.equals("intLit")) {
						t2 =t2.children.get(0);
					}
					if(!t2.val.equals("intLit") && t2.parent.children.get(1).children.size()>0) {
						getArray(t2);
						code += "\tlw r3,"+funcName+t2.name+"(r3)\n";
					}
					else {
						code += "\tlw r3,"+tempList.pop()+"(r0)\n";
					}
				}
				else {
					if(!t2.val.equals("intLit")) {
						t2 =t2.children.get(0);
					}
					if(t2.val.equals("id"))
						code += "\tlw r3,"+funcName+t2.name+"(r0)\n";
					else code += "\taddi r3,r0,"+t2.name+"\n";
				}
				code +="\t"+op+" r1,r2,r3\n";
			}
//			WRITE OPERATION
			if(t.children.get(i).val.equals("write")) {
				code += "\t%WRITE EXPRESSION\n";
				TreeNode x = t.children.get(i).children.get(0).children.get(0).children.get(0);
				if(tempList.isEmpty()) {
					x=x.children.get(0);
//					write(1);
					if(x.children.size()==0) {
						code += "\taddi r13,r0,"+x.name +"\n"
								+ "	sw -8(r14),r13\n"
								+ "    addi r1,r0, buf\n"
								+ "    sw -12(r14),r1\n"
								+ "    jl r15, intstr\n"
								+	"\tadd r1,r0,r13\n"
								+ "    sw -8(r14),r1\r\n"
								+ "    jl r15,putstr\n"
								+ "\taddi r1,r0,tm\n"
								+ "\tsw -8(r14),r1\n"
								+ "\tjl r15,putstr\n";
					}
//					write(x);
					else if(x.children.get(1).children.size()==0) {
						x=x.children.get(0);
						code += "\tlw r13,"+funcName+x.name +"(r0)\n"
								+ "	sw -8(r14),r13\n"
								+ "    addi r1,r0, buf\n"
								+ "    sw -12(r14),r1\n"
								+ "    jl r15, intstr\n"
								+	"\tadd r1,r0,r13\n"
								+ "    sw -8(r14),r1\r\n"
								+ "    jl r15,putstr\n"
								+ "\taddi r1,r0,tm\n"
								+ "\tsw -8(r14),r1\n"
								+ "\tjl r15,putstr\n";
					}
//					write(arr[1]);
					else {
						getArray(x.children.get(0));
						code += "\tlw r13,"+funcName+x.children.get(0).name +"(r3)\n"
								+ "	sw -8(r14),r13\n"
								+ "    addi r1,r0, buf\n"
								+ "    sw -12(r14),r1\n"
								+ "    jl r15, intstr\n"
								+	"\tadd r1,r0,r13\n"
								+ "    sw -8(r14),r1\r\n"
								+ "    jl r15,putstr\n"
								+ "\taddi r1,r0,tm\n"
								+ "\tsw -8(r14),r1\n"
								+ "\tjl r15,putstr\n";
					}
					
				}
				else {
//					write(1+x)
					if(x.children.get(0).val.equals("intLit")) {
						code += "\tlw r13,"+tempList.pop()+"(r0)\n"
								+ "	sw -8(r14),r13\n"
								+ "    addi r1,r0, buf\n"
								+ "    sw -12(r14),r1\n"
								+ "    jl r15, intstr\n"
								+	"\tadd r1,r0,r13\n"
								+ "    sw -8(r14),r1\r\n"
								+ "    jl r15,putstr\n"
								+ "\taddi r1,r0,tm\n"
								+ "\tsw -8(r14),r1\n"
								+ "\tjl r15,putstr\n";
					}

					else {
						x=x.children.get(0).children.get(1);
						if(x.children.size()>0) {
							
							x=x.parent.children.get(0);
							getArray(x);
							code += "\tlw r13,"+funcName+x.name+"(r3)\n"
								+ "	sw -8(r14),r13\n"
								+ "    addi r1,r0, buf\n"
								+ "    sw -12(r14),r1\n"
								+ "    jl r15, intstr\n"
								+	"\tadd r1,r0,r13\n"
								+ "    sw -8(r14),r1\r\n"
								+ "    jl r15,putstr\n"
								+ "\taddi r1,r0,tm\n"
								+ "\tsw -8(r14),r1\n"
								+ "\tjl r15,putstr\n";
						}
//						write(x+1);
						else {
							code += "\tlw r13,"+tempList.pop()+"(r0)\n"
									+ "	sw -8(r14),r13\n"
									+ "    addi r1,r0, buf\n"
									+ "    sw -12(r14),r1\n"
									+ "    jl r15, intstr\n"
									+	"\tadd r1,r0,r13\n"
									+ "    sw -8(r14),r1\r\n"
									+ "    jl r15,putstr\n"
									+ "\taddi r1,r0,tm\n"
									+ "\tsw -8(r14),r1\n"
									+ "\tjl r15,putstr\n";
						}
				}
				}
			}
		}
	}
	static int countPm=0;
	static ArrayList<String> statmt = new ArrayList<>();
	static void getParams(TreeNode t,String fn,int x) {
		if(t==null || t.children==null) return;
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).val.equals("Term")) {
				if(contains(t.parent,"addop") || contains(t.parent,"multop")) {
					code += "\t lw r1,"+tempList.pop()+"(r0)\n"
							+ "\t sw "+parmList.get(x+countPm)+"(r0),r1\n";
					countPm++;
				}
				else {
					while(t.children.size()>0 && !(t.children.get(0).val.equals("id") ||t.children.get(0).val.equals("intLit") || t.children.get(0).val.equals("floatLit"))) {
						t=t.children.get(0);
					}
					t = t.children.get(0);
					if(t.val.equals("id")) {
						if(t.parent.children.get(1).children.size()==0) {
						code += "\taddi r1,r0,"+funcName+t.name+"\n"
								+ "\tsw "+parmList.get(x+countPm)+"(r0),r1\n";
						countPm++;
						}
						else {
							t=t.parent.children.get(1);
							statmt.add("\taddi r6,r0,0\n");
							code += "\taddi r6,r0,0\n";
							for(int a=0;a<t.children.size();a++) {
								TreeNode c = t.children.get(0).children.get(0).children.get(0);
								for(int d=0;d<Integer.parseInt(c.name);d++) {
									code += "\tlw r1,"+funcName+ t.parent.children.get(0).name+"(r6)\n"
											+ "\tsw "+parmList.get(x+countPm)+"(r6),r1\n"
													+ "\taddi r6,r6,4\n";
									statmt.add("\tlw r1,"+parmList.get(x+countPm)+"(r6)\n"+
													"\tsw "+funcName+ t.parent.children.get(0).name+"(r6),r1\n"
													+ "\taddi r6,r6,4\n");
									
								}
							}
							countPm++;
						}
					}
					else {
						
						code += "\taddi r1,r0,"+t.name+"\n"
								+ "\tsw "+parmList.get(x+countPm)+"(r0),r1\n";
						countPm++;
					}	
				}
				return;
			}
			getParams(t.children.get(i),fn,x);
		}
	}
	private static boolean checkChild(TreeNode t) {
		if(t==null ||t.children==null) return true;
		boolean x=true;
		for(int i=0;i<t.children.size();i++) {
			
			if(t.children.get(i).val.equals("id")|| t.children.get(i).val.equals("intLit")) {
				return false;
			}
			x=checkChild(t.children.get(i));
			if(!x) return false;
		}
		return true;
	}
	public static boolean contains(TreeNode t,String s) {
		if(t==null || t.children==null) return true;
		if(t.val.equals(s)) {
			return true;
		}
		boolean x=false;
		for(int i=0;i<t.children.size();i++) {
			if(contains(t.children.get(i),s)) {
				x=true;
			}
		}
		return x;
	}
	static void getArray(TreeNode x) {
		TreeNode dim = null;
		for(int j=0;j<mainTable.get(mainPin).size();j++) {
			if(mainTable.get(mainPin).get(j).name.equals(x.name)) {
				dim = mainTable.get(mainPin).get(j);
			}
		}
		code += "\tadd r5,r0,r0\n";
		for(int k=0;k<x.parent.children.get(1).children.size();k++) {
			TreeNode tem= x.parent.children.get(1).children.get(k).children.get(0).children.get(0);
			if(!tem.val.equals("Term")) {
				tem=tem.parent;
			}
			if(contains(tem.parent, "addop") || contains(tem.parent,"multop")) {
				
				String st="";
				if(tempList.size()>1) {
					st=tempList.pop();
					code += "\taddi r6,r0,0\n";
					code += "\tlw r4,"+tempList.pop()+"(r0)\n";
					code += "\tadd r6,r6,r4\n";
					tempList.push(st);
				}
				else {
					code += "\taddi r6,r0,0\n";
					code += "\tlw r4,"+tempList.pop()+"(r0)\n";
					code += "\tadd r6,r6,r4\n";
				}
			}
			else {
				if(tem.val.equals("Term")) {
					tem=tem.children.get(0);
				}
				if(tem.val.equals("intLit")) {
					code += "\taddi r6,r0,"+tem.name+"\n";
				}
				else {
					tem = tem.children.get(0);
					code += "\taddi r6,r0,0\n";
					code += "\tlw r4,"+funcName+tem.name+"(r0)\n";
					code += "\tadd r6,r6,r4\n";	
				}
			}
			int col=1;
			for(int c=k+1;c<dim.dim.size();c++) {
				col*= dim.dim.get(c);
			}
			code +="\tmuli r6,r6,"+col+"\n";
			code +="\tadd r5,r5,r6\n";
		}
		code +="\tmuli r3,r5,4\n";
	}
	static void getArray2(TreeNode x) {
		TreeNode dim = null;
		x=x.parent.children.get(0);
		for(int j=0;j<mainTable.get(mainPin).size();j++) {
			if(mainTable.get(mainPin).get(j).name.equals(x.name)) {
				dim = mainTable.get(mainPin).get(j);
			}
		}
		code += "\tadd r5,r0,r0\n";
		for(int k=0;k<x.parent.children.get(1).children.get(0).children.size();k++) {
			TreeNode tem= x.parent.children.get(1).children.get(k).children.get(0).children.get(0);
			if(contains(tem.parent, "addop") || contains(tem.parent,"multop")) {
				String st="";
				if(tempList.size()>1) {
					st=tempList.pop();
					code += "\taddi r6,r0,0\n";
					code += "\tlw r4,"+tempList.pop()+"(r0)\n";
					code += "\tadd r6,r6,r4\n";
					tempList.push(st);
				}
				else {
					
					code += "\taddi r6,r0,0\n";
					code += "\tlw r4,"+tempList.pop()+"(r0)\n";
					code += "\tadd r6,r6,r4\n";
				}
			}
			else {
				tem= tem.children.get(0);
				if(tem.val.equals("intLit")) {
					code += "\taddi r6,r0,"+tem.name+"\n";
				}
				else {
					tem=tem.children.get(0);
					code += "\taddi r6,r0,0\n";
					code += "\tlw r4,"+funcName+tem.name+"(r0)\n";
					code += "\tadd r6,r6,r4\n";	
				}
			}
			int col=1;
			for(int c=k+1;c<dim.dim.size();c++) {
				col*= dim.dim.get(c);
			}
			code +="\tmuli r6,r6,"+col+"\n";
			code +="\tadd r5,r5,r6\n";
		}
		code +="\tmuli r3,r5,4\n";
	}
	static int mainPin = 0;
	static void findMain() {
		for(int i=0;i<symbolTable.size();i++) {
			if(symbolTable.get(i).get(0).name.equals("main") && symbolTable.get(i).get(0).type.equals("function")) {
				mainPin = i;
			}
		}
	}
	static void getMainNode(TreeNode t){
		if(t==null || t.children==null) return;
		if(t.val.equals("function") && t.name!=null && t.name.equals("main")) {
			main = t;
		}
		else if(t.val.equals("function") && t.name!=null && !t.name.equals("main") && !t.name.equals("function")) {
			code += t.name+" add r12,r0,r15\n";
			traverseNodes(t.parent);
			code+="\tadd r15,r0,r12\n"
					+ "\tjr r15\n\n";
		}
		for(int i=0;i<t.children.size();i++)
			getMainNode(t.children.get(i));
	}
	static boolean containMem(String s) {
		for(int i=1;i<symbolTable.get(mainPin).size();i++) {
			if(symbolTable.get(mainPin).get(i).name.equals(s)) {
				return true;
			}
		}
		return false;
	}
	public static TreeNode main;
	static void getFunctions(TreeNode t) {
		if(t==null || t.children==null) return;
		if(t.val.equals("id") && fNames.contains(" "+t.name+" ")) {
			tokens.add(t.name);
		}
		for(int i=0;i<t.children.size();i++)
			getFunctions(t.children.get(i));
	}
	static String className,fNames;
	public static void main(String[] args) {
		tokens=new ArrayList<>();
		ArrayList<Object> arr = tableGenerate.tableGenerator();
		String inp = (String) arr.get(3);
		root = (TreeNode) arr.get(1);
		symbolTable = (ArrayList<ArrayList<TreeNode>>) arr.get(2);
		mainTable = new ArrayList<>(symbolTable);
		fNames = (String) arr.get(4);
		className = (String) arr.get(5);
		findMain();
		
		File file = new File(inp+".outsemanticerrors");
		if(file.length()>0) {
			System.out.println("error in program. cannot generate code for it");
			return;
		}
		getMainNode(root);
		getFunctions(main.parent);
		traverseNodes(main.parent);
		
		code+= "\thlt";
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(inp+".moon"));
            out.write(code + "\n");
            out.write(decs);
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
	}

}
