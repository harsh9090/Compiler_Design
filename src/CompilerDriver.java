import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
public class CompilerDriver {
    static TreeNode root=null;
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
            if(t.children.get(2).val.equals("FunctionMemberTail")) {
                funcName += t.children.get(2).children.get(0).name;
            }
            if(t.children.get(2).val.equals("constructor")) {
                funcName += "constructor";
            }
        }
        if(t.val.equals("localvar") || t.val.equals("FParm") || t.val.equals("FParms")) {

            int space =0;
            String name =funcName;
            name += t.children.get(0).name;

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
                        if(t.children.get(0).dim.size()>0) {
                            for(int j=0;j<t.children.get(2).children.size();j++) {
                                space*=Integer.parseInt(t.children.get(2).children.get(j).name);
                            }
                        }
                        name = funcName +t.children.get(0).name;
                        for(int x=0;x<mainTable.get(mainPin).size();x++) {
                            if(mainTable.get(mainPin).get(x).name.equals(t.children.get(0).name)) {
                                mainTable.get(mainPin).get(x).store = symbolTable.get(i).get(0).store;
                            }
                        }
                    }
                }
            }
            decs += "\t%DECLARE VARIABLE\n";
            if(!decs.contains(name + " res"))
                decs += name+" res "+ space +"\n";
            if(t.children.get(2).val.equals("AParmList")) {
                int x=0;
                for(x=0;x<parmList.size();x++) {
                    if(parmList.get(x).contains(t.children.get(0).type+"constructor")) {
                        break;
                    }
                }
                getParams(t,x);
                countPm=0;
                tempList.clear();
                code +="jl r15,"+t.children.get(1).name+"constructor\n";
                code += "\taddi r4,r0,0\n";
                alignConst(t.children.get(0).name,t.children.get(1).name);
                for(int s=0;s<statmt.size();s++) {
                    code += statmt.get(s);
                }
                statmt.clear();
            }
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
//				traverseNodes(t.children.get(i+1));
                if(t.children.get(i).name.equals("build")) {
                    TreeNode x = t;
                    while(!x.val.equals("=")) {
                        x=x.parent;
                    }
                    if(x.parent.children.get(0).val.equals("IndiceList")) {
                        x = x.parent.parent.children.get(0);
                        int y=0;
                        for(y=0;y<parmList.size();y++) {
                            if(parmList.get(y).contains(x.type+"constructor")) {
                                break;
                            }
                        }
                        getParams(t,y);
                        countPm=0;
                        tempList.clear();

                        code +="jl r15,"+x.type+"constructor\n";
                        getArray2(x);
                        code+= "\tadd r4,r0,r3\n";
                        alignConst(x.name,x.type);
                        for(int s=0;s<statmt.size();s++) {
                            code += statmt.get(s);
                        }
                        statmt.clear();
                        return;
                    }
                    else {
//						write code for f1 = build();
                    }

                    return;
                }
                int x=0;
                for(x=0;x<parmList.size();x++) {
                    if(parmList.get(x).contains(t.children.get(i).name)) {
                        break;
                    }
                }
                getParams(t.children.get(i+1),x);
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

                return;
            }
            traverseNodes(t.children.get(i));

            if(t.children.get(i).val.equals("returnFromFunction")) {
                if(!decs.contains(funcName+"ret "))
                    decs +=funcName+"ret res 4\n";
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
                    }

                }
                else {
                    code += "\tlw r1,"+tempList.pop()+"(r0)\n";
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
                TreeNode x=t1,y=t2;
                if(t1.val.equals("multop") || t1.val.equals("addop")) {
                    t1 =t1.children.get(1);
                }
                if(t1.val.equals("ArithExpr") || (t1.val.equals("Factor") && t1.conTemp)) {
//					containing expression at t1
                    if(t2.val.equals("ArithExpr") || (t2.val.equals("Factor") && t2.conTemp)) {
                        code += "\tlw r2,"+tempList.pop()+"(r0)\n"
                                + "\tlw r1,"+tempList.pop()+"(r0)\n";
                    }
                    else {
                        code += "\tlw r1,"+tempList.pop()+"(r0)\n";
                        if(t2.val.equals("Factor")) {
                            code += "\tlw r2,"+funcName + t2.children.get(0).name+"(r0)\n";
                        }
                        else {
                            code += "\taddi r2,r0,"+t2.name+"\n";
                        }

                    }

                }
                else {
                    if(t1.val.equals("Factor")) {
                        code += "\tlw r1,"+funcName + t1.children.get(0).name+"(r0)\n";
                    }
                    else {
                        code += "\taddi r1,r0,"+t1.name+"\n";
                    }
//					not containing exp at t1
                    if(t2.val.equals("ArithExpr") || (t2.val.equals("Factor") && t2.conTemp)) {
                        code += "\tlw r2,"+tempList.pop()+"(r0)\n";
                    }
                    else {
                        if(t2.val.equals("Factor")) {
                            code += "\tlw r2,"+funcName + t2.children.get(0).name+"(r0)\n";
                        }
                        else {
                            code += "\taddi r2,r0,"+t2.name+"\n";
                        }
                    }

                }
                code += "\t"+op+" r1,r1,r2\n";
                if(!temp.contains("temp"+numb)) {
                    temp.add("temp"+numb);
                    decs += "temp"+numb+" res 4\n";
                }
                code += "\tsw temp"+numb+"(r0),r1\n";
                tempList.push("temp"+numb);
                numb++;
                if(x.parent.parent.val.equals("ArithExpr")) {
                    x.parent.parent.conTemp=true;
                    x.parent.parent.parent.conTemp= true;
                }
                x.conTemp=true;
                x.parent.conTemp = true;
                y.conTemp = true;
                y.parent.conTemp = true;

            }
//			ADD OPERATION
            if(t.children.get(i).val.equals("addop")) {
                code += "\t%ADD OR SUB VALUES\n";
                TreeNode t1 = t.children.get(i-1);
                TreeNode t2 = t.children.get(i).children.get(1);
                String op = t.children.get(i).children.get(0).name;
                if(!t1.val.equals("Term")) {
                    t1 = t1.children.get(1);
                }
                TreeNode x = t1,y=t2;

                if(op.equals("+")) {
                    op = "add";
                }
                else op="sub";
                if(t1.conTemp) {
                    if(t2.conTemp) {
                        code += "\tlw r2,"+tempList.pop()+"(r0)\n"
                                + "\tlw r1,"+tempList.pop()+"(r0)\n";
                    }
                    else {
                        while(!(t2.val.equals("id") || t2.val.equals("intLit") || t2.val.equals("floatLit")) && t2.children.size()>0) {
                            t2 = t2.children.get(0);
                        }
                        code += "\tlw r1,"+tempList.pop()+"(r0)\n";
                        if(t2.val.equals("id")) {
                            code += "\tlw r2,"+funcName+t2.name+"(r0)\n";
                        }
                        else {
                            code += "\taddi r2,r0,"+t2.name+"\n";
                        }
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
                    if(t2.conTemp) {
                        code += "\tlw r2,"+tempList.pop()+"(r0)\n";
                        if(t1.val.equals("id")) {
                            code += "\tlw r1,"+funcName +t1.name+"(r0)\n";
                        }
                        else code += "\taddi r1,r0,"+t1.name+"\n";

                    }
                    else {

                        while(!(t2.val.equals("id") || t2.val.equals("intLit") || t2.val.equals("floatLit")) && t2.children.size()>0) {
                            t2 = t2.children.get(0);
                        }
                        if(t1.val.equals("id")) {
                            code += "\tlw r1,"+funcName +t1.name+"(r0)\n";
                        }
                        else code += "\taddi r1,r0,"+t1.name+"\n";
                        if(t2.val.equals("id")) {
                            code += "\tlw r2,"+funcName +t2.name+"(r0)\n";
                        }
                        else code += "\taddi r2,r0,"+t2.name+"\n";
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
                if(x.parent.val.equals("ArithExpr")) {
                    x.parent.conTemp=true;
                    x.parent.parent.conTemp= true;
                }

                x.conTemp=true;
                x.parent.conTemp = true;
                y.conTemp = true;
                y.parent.conTemp = true;
            }


            if(t.children.get(i).val.equals("=")) {
                code += "\t%ASSIGN VALUE\n";
                TreeNode tmp1 = t.children.get(i);
                while(!(tmp1.val.equals("id") || tmp1.val.equals("intLit") || tmp1.val.equals("floatLit")) && tmp1.children.size()>0) {
                    tmp1 = tmp1.children.get(0);
                }
                if(tmp1.name.equals("build")) {
                    return;
                }
//				ALL MEMBERS WITH x = .....
                if(t.children.get(i-1).val.equals("id")) {
                    if(tempList.isEmpty()) {
                        if(!tmp1.val.equals("id")) {
                            code += "\taddi r1,r0,"+tmp1.name+"\n";
                        }
                        else {
                            if(!tmp1.val.equals("intLit") && tmp1.parent.children.get(1).children.size()>0 && !tmp1.parent.children.get(1).val.equals("AParmList")) {
                                getArray(tmp1);
                                code += "\tlw r1,"+funcName+tmp1.name+"(r3)\n";
                            }
                            else code += "\tlw r1,"+funcName +tmp1.name+"(r0)\n";
                        }
                    }
                    else {
                        if(!tmp1.val.equals("intLit") && tmp1.parent.children.get(1).children.size()>0 && !tmp1.parent.children.get(1).val.equals("AParmList") && !(contains(tmp1.parent, "multop") ||contains(tmp1.parent.parent, "addop"))) {

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
                    if(!t.children.get(i-1).parent.val.equals("dot")) {
                        code += "\tsw "+funcName+t.children.get(i-1).name+"(r0),r1\n";
                    }
                    else {
                        if(t.parent.children.get(0).name!=null && t.parent.children.get(0).name.equals("self")) {
                            String clsName = funcName.replace("constructor", "");
                            int x = getClass(clsName);
                            for(int k=0;k<mainTable.get(x).size();k++) {
                                if(mainTable.get(x).get(k).name.equals(t.children.get(0).name)) {
                                    code += "\tsw "+funcName+t.children.get(0).name+"(r0),r1\n";
                                }
                            }
                        }
                        else {
                            boolean found = false;
                            if(t.parent.children.get(0).val.equals("id")) {
                                int cls = getClass(t.parent.children.get(0).type);
                                for(int a=0;a<mainTable.get(cls).size();a++) {

                                    if(mainTable.get(cls).get(a).name.equals(t.children.get(i-1).name)) {
                                        found=true;
                                        code += "\taddi r3,r0,"+mainTable.get(cls).get(a).store +"\n"
                                                + "\tsw "+funcName +t.parent.children.get(0).name+"(r3),r1\n";
                                    }
                                }
                                if(!found) {
                                    System.out.println("defined member is not available" + t.lineNumber);
                                }
                            }
                            else {
                                int cls = getClass(t.children.get(0).type);
                                for(int a=0;a<mainTable.get(cls).size();a++) {
                                    if(mainTable.get(cls).get(a).name.equals(t.children.get(1).children.get(0).name)) {
                                        found=true;
                                        code += "\taddi r3,r0,"+mainTable.get(cls).get(a).store +"\n"
                                                + "\tsw "+funcName +t.parent.children.get(0).name+"(r3),r1\n";
                                    }
                                }
                                if(!found) {
                                    System.out.println("defined member is not available");
                                }
                            }
                        }
//						code += "code here for assign class.variable!\n";
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
//						x is LHS and v is RHS
                        TreeNode x = t;
                        getArray2(x);
                        TreeNode v = x;
                        v=v.children.get(1);
                        while(!(v.val.equals("Term") || v.val.equals("Factor"))) {
                            v = v.children.get(0);
                        }
                        x = x.parent.children.get(0);
                        if(v.conTemp) {
                            code += "\tlw r1,"+tempList.pop()+"(r0)\n"
                                    + "\tsw "+funcName+x.name+"(r3),r1\n";
                        }
                        else {
                            while(!(v.val.equals("id")|| v.val.equals("intLit") || v.val.equals("floatLit"))) {
                                v=v.children.get(0);
                            }
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
                tempList.clear();
            }
            if(t.children.get(i).val.equals("statment")) {
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
                if(t2.parent.conTemp) {
                    code += "\tlw r3,"+tempList.pop()+"(r0)\n";
                }
                else {
                    if(!t2.val.equals("intLit")) {
                        t2 =t2.children.get(0);
                    }
                    if(t2.val.equals("id"))
                        code += "\tlw r3,"+funcName+t2.name+"(r0)\n";
                    else {
                        code += "\taddi r3,r0,"+t2.name+"\n";
                    }
                }
                if(t1.conTemp) {
                    code += "\tlw r2,"+tempList.pop()+"(r0)\n";
                }
                else {
                    if(!t1.val.equals("intLit")) {
                        t1 =t1.children.get(0);
                    }
                    if(t1.val.equals("intLit")) {
                        code += "\taddi r2,r0,"+t1.name+"\n";
                    }
                    else if(t1.val.equals("id")) {
                        code += "\tlw r2,"+funcName+t1.name+"(r0)\n";
                    }
                    else {
                        getArray(t1);
                        code += "\tlw r2,"+funcName+t1.name+"(r3)\n";
                    }
                }
                code +="\t"+op+" r1,r2,r3\n";
            }

            if(t.children.get(i).val.equals("id")) {
                if(t.children.get(i).name.equals("self")) {
                }
                else if(!decs.contains(funcName+t.children.get(i).name)) {
                    int space =0;
                    String name =funcName+t.children.get(0).name;
                    if(t.val.equals("FParm") || t.val.equals("FParms")) {
                        parmList.add(name);
                    }
                    if(t.children.get(0).type.equals("integer")) {
                        space=4;
                        if(t.children.get(0).dim.size()>0) {
                            for(int k=0;k<t.children.get(2).children.size();k++) {
                                space*=Integer.parseInt(t.children.get(2).children.get(k).name);
                            }
                        }
                    }
                    else if(t.children.get(0).type.equals("float")) {
                        space=8;
                        if(t.children.get(0).dim.size()>0) {
                            for(int k=0;k<t.children.get(2).children.size();k++) {
                                space*=Integer.parseInt(t.children.get(2).children.get(k).name);
                            }
                        }
                    }
                    else {
                        for(int k=0;k<symbolTable.size();k++) {
                            if(symbolTable.get(k).get(0).name.equals(t.children.get(0).type)) {
                                space = symbolTable.get(k).get(0).store;
                                if(t.children.get(0).dim.size()>0) {
                                    for(int j=0;j<t.children.get(2).children.size();j++) {
                                        space*=Integer.parseInt(t.children.get(2).children.get(j).name);
                                    }
                                }
                                name = t.children.get(0).type +t.children.get(0).name;
                            }
                        }
                    }
                    if(!decs.contains(name+" res ")) {
                        decs += "\t%DECLARE VARIABLE\n";
                        decs += name+" res "+ space +"\n";
                    }

                }
            }
//			Member function call
            if(t.children.get(i).val.equals("dot")) {
                int x=0;
                for(x=0;x<parmList.size();x++) {
                    if(parmList.get(x).contains(t.children.get(i).children.get(0).name)) {
                        break;
                    }
                }
                if(t.children.get(i).children.get(1).val.equals("AParmList")) {
                    for(x=0;x<parmList.size();x++) {
                        if(parmList.get(x).contains(t.parent.children.get(0).type+t.children.get(i).children.get(0).name)) {
                            break;
                        }
                    }
                    if(t.parent.children.get(1).children.size()>0) {
                        getArray(t.parent.children.get(0));
                        code += "\tadd r4,r3,r0\n";
                    }
                    else {
                        code += "\taddi r4,r0,0\n";
                    }
                    alignFunction(t.parent,t.parent.children.get(0).type);
                    getParams(t,x);
                    countPm=0;
                    tempList.clear();
                    code +="jl r15,"+t.parent.children.get(0).type+t.children.get(i).children.get(0).name+"\n";
                    for(int s=0;s<statmt.size();s++) {
                        code += statmt.get(s);
                    }
                    statmt.clear();
                    if(decs.contains(t.parent.children.get(0).type+t.children.get(i).children.get(0).name+"ret res")) {
                        if(!temp.contains("temp"+numb)) {
                            temp.add("temp"+numb);
                            decs += "temp"+numb+" res 4\n";
                        }
                        code += "\tlw r1,"+t.parent.children.get(0).type+t.children.get(i).children.get(0).name+"ret(r0)\n";
                        code += "\tsw temp"+numb+"(r0),r1\n";
                        tempList.push("temp"+numb);
                        numb++;
                    }
                }
                else {
                    if(t.children.get(0).name!=null && t.children.get(0).name.equals("self")) {

                    }
                    else {

                        boolean found = false;
                        if(t.parent.children.get(0).val.equals("id")) {
                            int cls = getClass(t.parent.children.get(0).type);
                            for(int a=0;a<mainTable.get(cls).size();a++) {
                                if(mainTable.get(cls).get(a).name.equals(t.children.get(0).children.get(0).name)) {
                                    found=true;
                                    code += "\taddi r3,r0,"+mainTable.get(cls).get(a).store +"\n"
                                            + "\tlw r1,"+funcName +t.parent.children.get(0).name+"(r3)\n";
                                    if(!contains(t,"=")) {
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
                            if(!found) {
                                System.out.println("defined member is not available");
                            }
                        }
                        else {
                            int cls = getClass(t.children.get(0).type);
                            for(int a=0;a<mainTable.get(cls).size();a++) {
                                if(mainTable.get(cls).get(a).name.equals(t.children.get(1).children.get(0).name)) {
                                    found=true;
                                    code += "\taddi r3,r0,"+mainTable.get(cls).get(a).store +"\n"
                                            + "\tlw r1,"+funcName + t.children.get(0).name+"(r3)\n";
                                    if(!contains(t,"=")) {
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
                            if(!found) {
                                System.out.println("defined member is not available");
                            }
                        }
                    }
                }
            }


            if(i>0 && t.children.get(i-1).val.equals("id")) {
                if(t.children.get(i).children.size()>0) {
                    if(t.children.get(i).val.equals("IndiceList")) {
                        getArray(t.children.get(i-1));
                        code += "\tlw r1,"+funcName+t.children.get(i-1).name+"(r3)\n";
                        if(!temp.contains("temp"+numb)) {
                            temp.add("temp"+numb);
                            decs += "temp"+numb+" res 4\n";
                        }
                        code += "\tsw temp"+numb+"(r0),r1\n";
                        tempList.push("temp"+numb);
                        numb++;
                        TreeNode x = t;
                        while(!x.val.equals("Factor")) {

                            x=x.parent;

                        }
                        x.conTemp = true;
                        x.parent.conTemp = true;
                        t.children.get(i-1).conTemp =true;
                    }
                }
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
                tempList.clear();
            }
        }
    }
    static int countPm=0;
    static ArrayList<String> statmt = new ArrayList<>();
    static void getParams(TreeNode t,int x) {
        if(t==null || t.children==null) return;
        for(int i=0;i<t.children.size();i++) {
            if(t.children.get(i).val.equals("Term")) {
                if(contains(t.parent,"addop") || contains(t.parent,"multop")) {
                    code += "\t lw r1,"+tempList.pop()+"(r0)\n"
                            + "\t sw "+parmList.get(x+countPm)+"(r0),r1\n";
                    countPm++;
                }
                else {
                    while(t.children.size()>0 && !(t.val.equals("id") ||t.val.equals("intLit") || t.val.equals("floatLit"))) {
                        if(t.children.get(0).val.equals("sign")) {
                            t=t.children.get(1);
                            t.name = t.parent.children.get(0).name + t.name;
                        }
                        else {
                            t=t.children.get(0);
                        }
                    }
                    if((t.val.equals("id") || t.val.equals("param")) && t.parent.children.size()>1) {
                        if(t.parent.children.get(1).children.size()==0) {
                            code += "\tlw r1,"+funcName+t.name+"(r0)\n"
                                    + "\tsw "+parmList.get(x+countPm)+"(r0),r1\n";
                            statmt.add("\tlw r1,"+parmList.get(x+countPm)+"(r0)\n"
                                    + "\tsw "+funcName+t.name+"(r0),r1\n");
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
            getParams(t.children.get(i),x);
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
    public static int getClass(String name) {
        for(int i=0;i<mainTable.size();i++) {
            if(mainTable.get(i).get(0).name.equals(name)) {
                return i;
            }
        }
        return -1;
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
        int stor = 4;
        for(int j=0;j<mainTable.get(mainPin).size();j++) {
            if(mainTable.get(mainPin).get(j).name.equals(x.name)) {
                dim = mainTable.get(mainPin).get(j);
                stor = dim.store;
                if(stor<4) stor=4;
            }
        }
        code += "\tadd r5,r0,r0\n";
        for(int k=0;k<x.parent.children.get(1).children.size();k++) {
            TreeNode tem= x.parent.children.get(1).children.get(k).children.get(0).children.get(0);
            if(!tem.val.equals("Term")) {
                tem=tem.parent;
            }
            if(contains(tem.parent, "addop") || contains(tem.parent,"multop")) {

                code += "\taddi r6,r0,0\n";
                code += "\tlw r4,"+tempList.pop()+"(r0)\n";
                code += "\tadd r6,r6,r4\n";
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
        code +="\tmuli r3,r5,"+stor+"\n";
    }
    static void getArray2(TreeNode x) {
        TreeNode dim = null;
        int stor= 4;
        x=x.parent.children.get(0);
        for(int j=0;j<mainTable.get(mainPin).size();j++) {
            if(mainTable.get(mainPin).get(j).name.equals(x.name)) {
                dim = mainTable.get(mainPin).get(j);
                stor=dim.store;
                if(stor<4) stor=4;
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
        code +="\tmuli r3,r5,"+stor+"\n";
    }
    static int mainPin = 0;
    static void findMain() {
        for(int i=0;i<symbolTable.size();i++) {
            if(symbolTable.get(i).get(0).name.equals("main") && symbolTable.get(i).get(0).type.equals("function")) {
                mainPin = i;
            }
        }
    }
    static void alignConst(String constr,String clsName) {
        int x = getClass(clsName);
        for(int i=0;i<mainTable.get(x).size();i++) {
            if(mainTable.get(x).get(i).val.equals("data")) {
                code += "\taddi r3,r4,"+(mainTable.get(x).get(i).store)+"\n"
                        + "\tlw r1,"+mainTable.get(x).get(0).name+"constructor"+mainTable.get(x).get(i).name+"(r0)\n"
                        + "\tsw "+funcName+constr+"(r3),r1\n";
            }
        }
    }
    static void alignFunction(TreeNode constr,String clsName) {
        int x = getClass(clsName);
        for(int i=0;i<mainTable.get(x).size();i++) {
            if(mainTable.get(x).get(i).val.equals("data")) {
                code += "\taddi r3,r4,"+mainTable.get(x).get(i).store+"\n"
                        + "\tlw r1,"+funcName+constr.children.get(0).name+"(r3)\n"
                        + "\tsw "+mainTable.get(x).get(0).name+constr.children.get(2).children.get(0).children.get(0).name+mainTable.get(x).get(i).name+"(r0),r1\n";
            }
        }
    }
    static void getMainNode(TreeNode t){
        if(t==null || t.children==null) return;
        if(t.val.equals("function") && t.name!=null && t.name.equals("main")) {
            main = t;
        }
        else if(t.val.equals("function") && t.name!=null && !t.name.equals("main") && !t.name.equals("function")) {
            if(!t.parent.val.equals("FunctionDec")) {
                if(t.parent.parent.val.equals("FunctionDec")) {
                    if(contains(t.parent.parent,"class")) {
                        code += t.parent.parent.children.get(1).name;
                    }
                    code += t.parent.children.get(0).name+" add r12,r0,r15\n";
                    traverseNodes(t.parent.parent);
                    code+="\tadd r15,r0,r12\n"
                            + "\tjr r15\n\n";
                }
            }
            else {
                code += t.name+" add r12,r0,r15\n";
                traverseNodes(t.parent);
                code+="\tadd r15,r0,r12\n"
                        + "\tjr r15\n\n";
            }
        }
        else if(t.val.equals("constructor") && t.parent.children.size()>1 && t.parent.children.get(0).val.equals("function")) {
            code += t.parent.children.get(1).name+"constructor add r12,r0,r15\n";
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
