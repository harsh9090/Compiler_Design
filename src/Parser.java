import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;



public class Parser {
    private static String[] prev = new String[3];
    private static ArrayList<TreeNode> reverseList(ArrayList<TreeNode> list){
        int x = list.size();
        ArrayList<TreeNode> rev = new ArrayList<>();
        for(int i=0;i<x;i++) {
            rev.add(list.remove(list.size()-1));
        }
        return rev;
    }
    private static TreeNode fNode = new TreeNode();
    private static final Stack<TreeNode> astSt = new Stack<>();
    private static String tok = "";
    private static String inp = "";
    private static final Stack<String> st = new Stack<>();
    private static int numb=-1;
    private static String[][] token;
    static String[][] rules = {{"ST $","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","ST $","","","","","","","","ST $","",""},
            {"A5 REPTPROG C10","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","A5 REPTPROG C10","","","","","","","","A5 REPTPROG C10","",""},
            {"","id A4 STATMTIDNEST ; B2","","while ( RELEXPR ) A17 STATBLK ; D9","","","read ( VAR ) ;","write ( EXPR ) F1 ;","return ( EXPR ) F3 ;","if ( RELEXPR ) A16 then STATBLK E3 else STATBLK E4 ; B4","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","( APRMS ) A5 STATID2 B9 B3","","","","","","","",". id A4 STATMTIDNEST D8","ASSOP EXPR E6","","","","","","","","","","","","","","","","","","A5 INDICE REPTIDNEST V3 STATID3 B5","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","",". id A4 STATMTIDNEST D8","ASSOP EXPR V19","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","=","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","ARIEXPR RELOP A6 ARIEXPR V5","","","ARIEXPR RELOP A6 ARIEXPR V5","","","","","","","","","","","","","","","","","","","","ARIEXPR RELOP A6 ARIEXPR V5","ARIEXPR RELOP A6 ARIEXPR V5","","","ARIEXPR RELOP A6 ARIEXPR V5","ARIEXPR RELOP A6 ARIEXPR V5","ARIEXPR RELOP A6 ARIEXPR V5","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","neq","lt","gt","leq","geq","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","eq"},
            {"","STATMT","EPS A5","STATMT","","","STATMT","STATMT","STATMT","STATMT","","EPS A5","","","","","","","","A5 { REPTBLK1 } V6","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","STATMT REPTBLK1","","STATMT REPTBLK1","","","STATMT REPTBLK1","STATMT REPTBLK1","STATMT REPTBLK1","STATMT REPTBLK1","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","id A4 VAR2 V18","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","( APRMS ) VARID V11","A5 REPTIDNEST V3 A5 REPTVAR V1 V11","","","","","","","A5 REPTIDNEST V3 A5 REPTVAR V1 V11","","","","","","","","","","","","","","","","","","","A5 REPTIDNEST V3 A5 REPTVAR V1 V11","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","EPS","","","","","","","VARID REPTVAR","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","",". id A4 VARID2 V16","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","( APRMS ) VARID V17","A5 REPTIDNEST V3","","","","","","","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","","","","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","","","",""},
            {"","A5 EXPR REPTAPRM V8","","","A5 EXPR REPTAPRM V8","EPS A5","","","","","","","","","","","","","","","","","","","A5 EXPR REPTAPRM V8","A5 EXPR REPTAPRM V8","","","A5 EXPR REPTAPRM V8","A5 EXPR REPTAPRM V8","A5 EXPR REPTAPRM V8","","","","","","","","","","","","","","","","","","","",""},
            {"","A5 ARIEXPR EXPR2 V9","","","A5 ARIEXPR EXPR2 V9","","","","","","","","","","","","","","","","","","","","A5 ARIEXPR EXPR2 V9","A5 ARIEXPR EXPR2 V9","","","A5 ARIEXPR EXPR2 V9","A5 ARIEXPR EXPR2 V9","A5 ARIEXPR EXPR2 V9","","","","","","","","","","","","","","","","","","","",""},
            {"","","EPS","","","EPS","","","","","","","","","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","RELOP A6 ARIEXPR"},
            {"","A5 TERM RIGARIEXPR V2","","","A5 TERM RIGARIEXPR V2","","","","","","","","","","","","","","","","","","","","A5 TERM RIGARIEXPR V2","A5 TERM RIGARIEXPR V2","","","A5 TERM RIGARIEXPR V2","A5 TERM RIGARIEXPR V2","A5 TERM RIGARIEXPR V2","","","","","","","","","","","","","","","","","","","",""},
            {"","A5 FACTOR RIGTEM V13","","","A5 FACTOR RIGTEM V13","","","","","","","","","","","","","","","","","","","","A5 FACTOR RIGTEM V13","A5 FACTOR RIGTEM V13","","","A5 FACTOR RIGTEM V13","A5 FACTOR RIGTEM V13","A5 FACTOR RIGTEM V13","","","","","","","","","","","","","","","","","","","",""},
            {"","","EPS","","","EPS","","","","","","","EPS","EPS","EPS","EPS","EPS","EPS","EPS","","","EPS","EPS","EPS","EPS","EPS","EPS","EPS","","","","INDICE REPTIDNEST","EPS","","","","","","","","","","","","","","","","","","EPS"},
            {"","","EPS","","","EPS","","","","","","","IDNEST REPTVARORFUNCALL","","EPS","EPS","EPS","EPS","EPS","","","EPS","EPS","EPS","EPS","EPS","EPS","EPS","","","","","EPS","","","","","","","","","","","","","","","","","","EPS"},
            {"","","","","","","","","","","","",". id A4 IDNEST2 B6","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","A5 REPTIDNEST V3","","( APRMS )","A5 REPTIDNEST V3","","","","","","","A5 REPTIDNEST V3","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","","","A5 REPTIDNEST V3"},
            {"","","EPS","","","EPS","","","","","","","","","EPS","EPS","EPS","EPS","EPS","","","MULOP A15 FACTOR B7 RIGTEM","MULOP A15 FACTOR B7 RIGTEM","MULOP A15 FACTOR B7 RIGTEM","EPS","EPS","EPS","EPS","","","","","EPS","","","","","","","","","","","","","","","","","","EPS"},
            {"","","","","","","","","","","","","","","","","","","","","","*","/","and","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","EPS","","","EPS","","","","","","","","","EPS","EPS","EPS","EPS","EPS","","","","","","ADDOP A14 TERM B8 RIGARIEXPR","ADDOP A14 TERM B8 RIGARIEXPR","ADDOP A14 TERM B8 RIGARIEXPR","EPS","","","","","EPS","","","","","","","","","","","","","","","","","","EPS"},
            {"","","","","","","","","","","","","","","","","","","","","","","","","+","-","or","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","APRMTAIL REPTAPRM","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","",", EXPR","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","EPS A5","","","","","","","","","",". id A4 STATMTIDNEST D8","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","id A4 FACTOR2 A5 REPTVARORFUNCALL V7 V14","","","( ARIEXPR )","","","","","","","","","","","","","","","","","","","","SIGN A3 FACTOR V10","SIGN A3 FACTOR V10","","","intLit A7","floatLit E2","not FACTOR E5","","","","","","","","","","","","","","","","","","","",""},
            {"","","A5 REPTIDNEST V3","","( APRMS )","A5 REPTIDNEST V3","","","","","","","A5 REPTIDNEST V3","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","","","A5 REPTIDNEST V3"},
            {"","","","","","","","","","","","","","","","","","","","","","","","","+","-","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","[ ARIEXPR ]","","","","","","","","","","","","","","","","","","",""},
            {"","STATMT","","STATMT","","","STATMT","STATMT","STATMT","STATMT","","","","","","","","","","","","","","","","","","","","","","","","LOCVARDEC","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","localvar id A4 : TYPE A1 AROBJ ; C3","","","","","","","","","","","","","","","","",""},
            {"","id","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","integer","float","","","","","","","","","","","","","",""},
            {"","","A5 REPTARSIZ C2","","( APRMS )","","","","","","","","","","","","","","","","","","","","","","","","","","","A5 REPTARSIZ C2","","","","","","A5 REPTARSIZ C2","","","","","","","","","","","","",""},
            {"","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","ARRSIZE REPTARSIZ","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","[ ARRSIZE2","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","intlit A7 ]","","","","F4 ]","","","","","","","","","","","","","","","","","",""},
            {"","LOCVARSTAT REPTVARSTAT","","LOCVARSTAT REPTVARSTAT","","","LOCVARSTAT REPTVARSTAT","LOCVARSTAT REPTVARSTAT","LOCVARSTAT REPTVARSTAT","LOCVARSTAT REPTVARSTAT","","","","","","","","","","","EPS","","","","","","","","","","","","","LOCVARSTAT REPTVARSTAT","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","A5 { REPTVARSTAT } C1","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","FUNHEAD FUNBODY C11","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","function A11 id A4 FUNHEADTAIL","","","","","","","","","",""},
            {"","","","","( FPARM ) arrow RETTYPE A13 C13","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","sr FUNHEADMEMTAIL","","","","","","","","",""},
            {"","id A4 ( FPARM ) arrow RETTYPE A13 C14","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","constructor ( FPARM ) D6","","","","","","",""},
            {"","TYPE","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","TYPE","TYPE","","","","","","","","void","","","","","",""},
            {"","A5 id A4 : TYPE A1 A5 REPTPRM3 C2 E7 REPTPRM4 C5","","","","EPS A5","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","EPS","","","","ARRSIZE REPTPRM3","","","","","","ARRSIZE REPTPRM3","","","","","","","","","","","","",""},
            {"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","FPARMTAIL REPTPRM4","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","",", id A4 : TYPE A1 A5 REPTPRMTAIL C2 C4","","","","","","","","","","","","","","","","","","","","","","",""},
            {"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","EPS","","","","ARRSIZE REPTPRMTAIL","","","","","","ARRSIZE REPTPRMTAIL","","","","","","","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","MEMFUNHEAD ;","","","MEMFUNHEAD ;","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","function id A4 : ( FPARM ) arrow RETTYPE A13 C7","","","constructor : ( FPARM ) D6","","","","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","MEMFUNDEC","","","MEMFUNDEC","","","","MEMVARDECL","","",""},
            {"","","","","","","","","","","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","VIS A1 MEMDECL D1 REPTMEMDECL","VIS A12 MEMDECL D1 REPTMEMDECL","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","public","private","","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","attribute E1 id A4 : TYPE A1 A5 REPTARSIZ C2 ; D3","","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","class A10 id A4 OPTITS A5 { REPTMEMDECL } C8 ; C9","",""},
            {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","FUNDEF","","","","","","","","CLSDEC","",""},
            {"EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","CLSFUNDEC REPTPROG","","","","","","","","CLSFUNDEC REPTPROG","",""},
            {"","","","","","","","","","","","","","","","","","","","EPS A5","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","isa A5 id A4 REPTLIST D2",""},
            {"","","","","","","","","","","","","","","","","","","","EPS","","","","","","","",", id A4 REPTLIST","","","","","","","","","","","","","","","","","","","","","","",""}};



    private static int getCol(String s) {
        String[] str = {"S","ST","STATMT","STATMTIDNEST","STATID3","ASSOP","RELEXPR","RELOP","STATBLK","REPTBLK1","VAR","VAR2","REPTVAR","VARID","VARID2","APRMS","EXPR","EXPR2","ARIEXPR","TERM","REPTIDNEST","REPTVARORFUNCALL","IDNEST","IDNEST2","RIGTEM","MULOP","RIGARIEXPR","ADDOP","REPTAPRM","APRMTAIL","STATID2","FACTOR","FACTOR2","SIGN","INDICE","LOCVARSTAT","LOCVARDEC","TYPE","AROBJ","REPTARSIZ","ARRSIZE","ARRSIZE2","REPTVARSTAT","FUNBODY","FUNDEF","FUNHEAD","FUNHEADTAIL","FUNHEADMEMTAIL","RETTYPE","FPARM","REPTPRM3","REPTPRM4","FPARMTAIL","REPTPRMTAIL","MEMFUNDEC","MEMFUNHEAD","MEMDECL","REPTMEMDECL","VIS","MEMVARDECL","CLSDEC","CLSFUNDEC","REPTPROG","OPTITS","REPTLIST"};
        for(int i=0;i<str.length;i++) {
            if(str[i].equalsIgnoreCase(s)) {
                return i;
            }
        }
        return -1;
    }
    private static boolean isAST(String s) {
        String[] st = StringList();
        for (String value : st) {
            if (value.equals(s)) {
                return true;
            }
        }
        return false;
    }
    private static int getAST(String s) {
        String[] st = StringList();
        for(int i=0;i<st.length;i++) {
            if(st[i].equals(s)) {
                return i;
            }
        }
        return 0;
    }

    private static String[] StringList() {
        return new String[]{"D1","D2","C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","V1","V2","V3","V4","V5","V6","V7","V8","V9","V10","V11","V12","V13","V14","V15","V16","V17","V18","V19","B1","B2","B3","B4","B5","B6","B7","B8","B9","A1","A3","A4","A5","A6","A7","A8","A9","A10","A11","D3","D4","A2","D5","D6","A12","D8","D9","E1","E2","E3","E4","E5","E6","E7","F1","F2","F3","A13","F4","A14","A15","A16","A17"};
    }

    private static void giveNode(TreeNode t) {
        for(int i=0;i<t.children.size();i++) {
            if(t.children.get(i).type.equals("integer") || t.children.get(i).type.equals("float")) {
                t.type = t.children.get(i).type;
            }
        }
    }
    private static void addRule(String s) {
        int x =getAST(s);
        String[] str = {"push(createsubtree(Member,pop,pop))","push(createsubtree(isa,popuntillEPS))","push(createsubtree(Body,popuntillEPS))","push(createsubtree(ARRSIZE,popuntillEPS))","push(createsubtree(localvar,pop,pop,pop))","push(createsubtree(FParms,pop,pop,pop))","push(createsubtree(FParmsList,popuntillEPS))","push(createsubtree(FParms,pop,pop,pop,pop))","push(createsubtree(function,pop,pop,pop))","push(createsubtree(MemberList,popuntillEPS))","push(createsubtree(ClassDec,pop,pop,pop,pop))","push(createsubtree(Program,popuntillEPS))","push(createsubtree(FunctionDec,pop,pop,pop,pop))","push(createsubtree(List,popuntillEPS))","push(createsubtree(FunctionTail,pop,pop))","push(createsubtree(FunctionMemberTail,pop,pop,pop))","push(createsubtree(VariableList,popuntillEPS))","push(createsubtree(ArithExpr,popuntillEPS))","push(createsubtree(IndiceList,popuntillEPS))","push(createsubtree(ArithExpr,pop,pop))","push(createsubtree(relop,pop,pop,pop))","push(createsubtree(StatmentList,popuntillEPS))","push(createsubtree(IDNEST,popuntillEPS))","push(createsubtree(AParmList,popuntillEPS))","push(createsubtree(EXPR,popuntillEPS))","push(createsubtree(Factor,pop,pop))","push(createsubtree(Variable,pop,pop))","push(createsubtree(Term,pop,pop))","push(createsubtree(Term,popuntillEPS))","push(createsubtree(Factor,pop,pop,pop))","push(createsubtree(Aparms,pop,pop))","push(createsubtree(dot,pop,pop))","push(createsubtree(VarId2,pop,pop))","push(createsubtree(read,pop,pop))","push(createsubtree(=,pop))","push(createsubtree(StatmentIdnest,popuntillEPS))","push(createsubtree(Statment,pop,pop))","push(createsubtree(StatmentIdnest,pop,pop,pop))","push(createsubtree(if,pop,pop,pop))","push(createsubtree(StatmentIdnest,pop,pop))","push(createsubtree(dot,pop,pop))","push(createsubtree(multop,pop,pop))","push(createsubtree(addop,pop,pop))","push(createsubtree(StatIdnest2,popuntillEPS))","push(createLeaf(type))","push(createLeaf(sign))","push(createLeaf(id))","push(EPS)","push(createLeaf(relOp))","push(createLeaf(intLit))","push(createLeaf(isa))","push(createLeaf(constructor))","push(createLeaf(class))","push(createLeaf(function))","push(createsubtree(VarDec,pop,pop,pop,pop))","push(createsubtree(functionMemberHead,pop,pop))","push(createLeaf(not))","push(createsubtree(EXPR,pop,pop))","push(createsubtree(constructor,pop))","push(createLeaf(visiblity))","push(createsubtree(dot,pop,pop))","push(createsubtree(while,pop,pop))","push(createLeaf(attribute))","push(createLeaf(floatLit))","push(createsubtree(then,pop))","push(createsubtree(else,pop))","push(createsubtree(not,pop))","push(createsubtree(=,pop))","push(createsubtree(FParm,pop,pop,pop))","push(createsubtree(write,pop))","push(createsubtree(read,pop))","push(createsubtree(returnFromFunction,pop))","push(createLeaf(return))","push(createLeaf(null))","push(createLeaf(add))","push(createLeaf(mult))","push(createsubtree(ifTree,pop))","push(createsubtree(whileTree,pop))"};
        s = str[x];
        if(s.contains("push(createLeaf")) {
            s = s.substring(16);
            s = s.split("\\)")[0];
            astSt.push(new TreeNode(s,s));
            if(astSt.peek().val.equals("intLit")) {
                astSt.peek().type = "integer";
            }
            if(astSt.peek().val.equals("floatLit")) {
                astSt.peek().type = "float";
            }
            astSt.peek().name= prev[1];
            astSt.peek().vis = prev[1];
            astSt.peek().lineNumber = token[numb][2];
        }
        else if(s.contains("push(createsubtree(")) {
            s = s.substring(19);
            String[] part = s.split(",");
            fNode = new TreeNode(part[0],part[0]);
            if(part[1].contains("popuntillEPS")) {
                while(!Objects.equals(astSt.peek().val, "EPS")){
                    TreeNode temp = astSt.pop();
                    temp.parent = fNode;
                    fNode.children.add(temp);
                }
                fNode.children = reverseList(fNode.children);
                giveNode(fNode);
                fNode.lineNumber = token[numb][2];
                astSt.pop();
                astSt.push(fNode);

            }
            else {
                for(int i=1;i<part.length;i++) {
                    TreeNode temp = astSt.pop();
                    part[i] = temp.val;
                    temp.parent = fNode;
                    fNode.children.add(temp);
                }
                fNode.children = reverseList(fNode.children);
                giveNode(fNode);
                fNode.lineNumber = token[numb][2];
                astSt.push(fNode);
            }
        }
        else {
            astSt.push(new TreeNode("EPS",""));
        }

    }
    private static void inSPush(String sr) {
        String[] ad = sr.split(" ");
        for(int i=ad.length-1;i>=0;i--) {
            st.push(ad[i]);
        }
    }
    private static int getLine(String s) {
        String[] st =tokenList();
        for(int i=0;i<st.length;i++) {
            if(st[i].equalsIgnoreCase(s)) {
                return i;
            }
        }
        return -1;
    }
    private static boolean isTerminal(String s) {
        String[] st = tokenList();

        for (String value : st) {
            if (value.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
    private static String nextToken() {
        String[] st = tokenList();
        numb++;
        while(numb<token.length && token[numb][0] != null && (token[numb][0].equalsIgnoreCase("inlinecmt") || token[numb][0].equalsIgnoreCase("blockcmt"))) {
            numb++;
            try {
                BufferedWriter out = new BufferedWriter(
                        new FileWriter(inp+".tokenization", true));
                tok += token[numb][1]+ " ";
                out.write("Comment: - "+token[numb-1][1]+ " ");
                out.write("\n");
                out.close();
            }
            catch (IOException e) {
                System.out.println("exception occurred" + e);
            }
        }
        if(numb>=token.length || token[numb][0]==null) return null;
        for (String s : st) {
            if (s.equalsIgnoreCase(token[numb][1])) {
                return token[numb][1];
            }
        }
        return token[numb][0];
    }

    private static String[] tokenList() {
        String[] st;
        st = new String[]{"$","id",";","while","(",")","read","write","return","if","then","else",".","=","neq","lt","gt","leq","geq","{","}","*","/","and","+","-","or",",","intLit","floatLit","not","[","]","localvar",":","integer","float","lsqbr","intlit","rsqbr","function","sr","arrow","constructor","void","public","private","attribute","class","isa","eq"};
        return st;
    }

    private static String getLines(int s) {
        String[] st = {"$","id",";","while","(",")","read","write","return","if","then","else",".","=","neq","lt","gt","leq","geq","{","}","*","/","and","+","-","or",",","intLit","floatLit","not","[","]","localvar",":","integer","float","lsqbr","intlit","rsqbr","function","sr","arrow","constructor","void","private","private","attribute","class","isa","eq"};
        return st[s];
    }
    private static String retStr(String s) {
        String xa="";
        int c = getCol(s);
        for(int i=0;i<rules[0].length;i++) {
            if(rules[c][i].equals("") && rules[c][i].equals("EPS")) {
                xa+=getLines(i)+" ";
            }
        }
        return xa;
    }
    private static void parse(){
        int k=0;
        st.push("S");
        String a = nextToken();
        while ((!st.isEmpty()) && (!st.peek().equalsIgnoreCase("$"))){
            String x = st.peek();
            if ( isTerminal(x) ) {
                if  ( x.equalsIgnoreCase(a) ) {
                    try {
                        BufferedWriter out = new BufferedWriter(
                                new FileWriter(inp+".tokenization", true));
                        tok += token[numb][1]+ " ";
                        out.write(tok+ " ");
                        out.write("\n");
                        out.close();
                    }
                    catch (IOException e) {
                        System.out.println("exception occurred" + e);
                    }
                    st.pop();
                }
                else {
                    if(a==null) break;
                    try {
                        BufferedWriter out = new BufferedWriter(
                                new FileWriter(inp+".outsyntaxerrors", true));
                        out.write("Error in line " + token[numb][2]+ " Expected : [ " + x + "  ], got : "+ a);
                        out.write("\n");
                        out.close();
                    }
                    catch (IOException e) {
                        System.out.println("exception occurred" + e);
                    }
                    System.out.println("Error in line " + token[numb][2]+ " Expected : " + x + ", got : "+ a);
                    k=1;

                }
                prev = token[numb];
                a = nextToken();
            }
            else if(isAST(x)){
                addRule(x);
                st.pop();
                continue;
            }
            else {
                int b = getLine(a);
                int c = getCol(x);
                if (b!=-1 && c != -1 && !rules[c][b].equalsIgnoreCase("")) {
                    st.pop() ;
                    if(rules[c][b].contains("EPS")) {
                        if(rules[c][b].substring(3).length()>0) {
                            addRule(rules[c][b].substring(4));
                        }
                        continue;
                    }
                    inSPush(rules[c][b]);
                }
                else {
                    if(a==null) break;
                    try {
                        BufferedWriter out = new BufferedWriter(
                                new FileWriter(inp+".outsyntaxerrors", true));
                        out.write("Error in line " + token[numb][2]+ " Expected : " + x + ", got : "+ a);
                        out.write("\n");
                        out.close();
                    }
                    catch (IOException e) {
                        System.out.println("exception occurred" + e);
                    }
                    System.out.println("Error in line " + token[numb][2] + " Expected : [ " + retStr(x) + " ], got : "+ a);
                    while(true) {
                        a=nextToken();
                        b = getLine(a);
                        c = getCol(x);
                        if (b!=-1 && c != -1 && !rules[c][b].equalsIgnoreCase("")) {
                            st.pop() ;
                            if(rules[c][b].contains("EPS")) {
                                continue;
                            }
                            inSPush(rules[c][b]);
                            break;
                        }
                    }
                    k=1;

                }
            }
            try {
                BufferedWriter out = new BufferedWriter(
                        new FileWriter(inp+".outderivation", true));
                String[] stack = new String[10000];
                st.copyInto(stack);
                for(int i=0;stack[i]!= null;i++) {
                    out.write(stack[i]+ " ");
                }
                out.write("\n");
                out.close();
            }
            catch (IOException e) {
                System.out.println("exception occurred" + e);
            }
        }
        while(!st.isEmpty()) {
            a = st.pop();
            int b = getCol(a);
            if (b == -1 || !rules[b][0].equals("EPS")) {
                if(isAST(a)) {
                    addRule(a);
                }
                else break;
            }
        }
        assert a != null;
        if (!(a.equals("$")) || k==1){
            System.out.println("Parsing Failed! ");
        }
        else {
            getPrivate(fNode,"public");
        }
    }
    private static void getPrivate(TreeNode t,String vis) {
        if(t==null || t.children==null) return;
        t.vis=vis;
        if(!t.children.isEmpty() && (t.children.get(0).vis.equals("private") || t.vis.equals("private"))) {
            for(int i=0;i<t.children.size();i++) {
                getPrivate(t.children.get(i),"private");
            }
        }
        else {
            for(int i=0;i<t.children.size();i++) {
                getPrivate(t.children.get(i),"public");
            }
        }
    }
    public static ArrayList<Object> parser(String inps) {
        inp = inps;
        token = LexicalDriver.lexical(inp+".src");
        ArrayList<Object> ans = new ArrayList<>();
        File file = new File(inp+".outlexerrors");
        if(file.length()>0) {
            ans.add("1");
            ans.add("2");
            ans.add(inp);
            try {
                BufferedWriter out = new BufferedWriter(
                        new FileWriter(inp+".outsyntaxerrors"));
                out.write("Error in lexical analysis!");
                out.close();
            }
            catch (IOException e) {
                System.out.println("exception occurred" + e);
            }
            return ans;
        }

        parse();

        ans.add(token);
        ans.add(astSt);
        ans.add(inp);

        return ans;
    }
}
