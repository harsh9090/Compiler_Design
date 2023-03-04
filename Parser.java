import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

class TreeNode{
    String val;
    ArrayList<TreeNode> children = new ArrayList<>();

    TreeNode(String data){
        val = data;
    }

    TreeNode(String data,ArrayList<TreeNode> child){
        val = data;
        children = child;
    }
    void removeNode(TreeNode n1){
    	this.children.remove(n1);
    }

	@Override
	public String toString() {
		return "TreeNode [val=" + val + "]";
	}
    
}

public class Parser {
	public static void printList(TreeNode root) {
		
		if(root==null) {
			
			return;
		}
		else {
			if(!root.val.equals("EPS")) {
				for(int i=0;i<index;i++) {
					System.out.print("| ");
				}
				System.out.println(root.val+ " ");
			}
			index++;
			for(int i=0;i<root.children.size();i++){
                printList(root.children.get(i));
            }
			index--;
            return;
		}
	}
	public static TreeNode fNode = new TreeNode(null);
	public static int index = 0;
	public static void findNode(TreeNode root,String s) {
        if(root.val==null){
            return;
        }
        else{
        	if(root.val.equals(s)) {
        		fNode = root;
        		return;
        	}
            for(int i=0;i<root.children.size();i++){
                findNode(root.children.get(i),s);
            }
            return;
        }
    }
	public static Stack<TreeNode> astSt = new Stack<>();
	public static String tok = "";
	public static String inp = "";
	public static Stack<String> st = new Stack<>();
	public static int numb=-1;
	public static String[][] token;
	static String[][] rules = {{"ST $","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","ST $","","","","","","","","ST $",""},
			{"A5 REPTPROG C10","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","A5 REPTPROG C10","","","","","","","","A5 REPTPROG C10",""},
			{"","id A4 A5 STATMTIDNEST B1 B2 ;","","while ( RELEXPR ) STATBLK B2 ;","","","read ( VAR ) ;","write ( EXPR ) ;","return ( EXPR ) ;","if ( RELEXPR ) then STATBLK else STATBLK B4 ;","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","( APRMS ) A5 STATID2 B9 B3","","","","","","","",". id A4 STATMTIDNEST B3","ASSOP A3 EXPR B3","","","","","","","","","","","","","","","","","","INDICE A5 REPTIDNEST V3 STATID3 B5","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","",". id A4 STATMTIDNEST V19","ASSOP A3 EXPR V19","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","=","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","ARIEXPR RELOP ARIEXPR V5","","","ARIEXPR RELOP ARIEXPR V5","","","","","","","","","","","","","","","","","","","","ARIEXPR RELOP ARIEXPR V5","ARIEXPR RELOP ARIEXPR V5","","","ARIEXPR RELOP ARIEXPR V5","ARIEXPR RELOP ARIEXPR V5","ARIEXPR RELOP ARIEXPR V5","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","neq","lt","gt","leq","geq","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","STATMT","EPS A5","STATMT","","","STATMT","STATMT","STATMT","STATMT","","EPS A5","","","","","","","","{ A5 REPTBLK1 V6 }","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","STATMT REPTBLK1","","STATMT REPTBLK1","","","STATMT REPTBLK1","STATMT REPTBLK1","STATMT REPTBLK1","STATMT REPTBLK1","","","","","","","","","","","EPS A5","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","id A4 VAR2 V18","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","( APRMS ) VARID V11","A5 REPTIDNEST V3 A5 REPTVAR V1 V11","","","","","","","A5 REPTIDNEST V3 A5 REPTVAR V1 V11","","","","","","","","","","","","","","","","","","","A5 REPTIDNEST V3 A5 REPTVAR V1 V11","","","","","","","","","","","","","","","","","",""},
			{"","","","","","EPS","","","","","","","VARID REPTVAR","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","",". id A4 VARID2 V16","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","( APRMS ) VARID V17","A5 REPTIDNEST V3","","","","","","","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","","","","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","","",""},
			{"","EXPR A5 REPTAPRM V8 V15","","","EXPR A5 REPTAPRM V8 V15","EPS A5","","","","","","","","","","","","","","","","","","","EXPR A5 REPTAPRM V8 V15","EXPR A5 REPTAPRM V8 V15","","","EXPR A5 REPTAPRM V8 V15","EXPR A5 REPTAPRM V8 V15","EXPR A5 REPTAPRM V8 V15","","","","","","","","","","","","","","","","","","",""},
			{"","ARIEXPR A5 EXPR2 V9 D5","","","ARIEXPR A5 EXPR2 V9 D5","","","","","","","","","","","","","","","","","","","","ARIEXPR A5 EXPR2 V9 D5","ARIEXPR A5 EXPR2 V9 D5","","","ARIEXPR A5 EXPR2 V9 D5","ARIEXPR A5 EXPR2 V9 D5","ARIEXPR A5 EXPR2 V9 D5","","","","","","","","","","","","","","","","","","",""},
			{"","","EPS","","","EPS","","","","","","","","","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","RELOP A6 ARIEXPR","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","",""},
			{"","TERM A5 RIGARIEXPR V2 V4","","","TERM A5 RIGARIEXPR V2 V4","","","","","","","","","","","","","","","","","","","","TERM A5 RIGARIEXPR V2 V4","TERM A5 RIGARIEXPR V2 V4","","","TERM A5 RIGARIEXPR V2 V4","TERM A5 RIGARIEXPR V2 V4","TERM A5 RIGARIEXPR V2 V4","","","","","","","","","","","","","","","","","","",""},
			{"","FACTOR A5 RIGTEM V13 V12","","","FACTOR A5 RIGTEM V13 V12","","","","","","","","","","","","","","","","","","","","FACTOR A5 RIGTEM V13 V12","FACTOR A5 RIGTEM V13 V12","","","FACTOR A5 RIGTEM V13 V12","FACTOR A5 RIGTEM V13 V12","FACTOR A5 RIGTEM V13 V12","","","","","","","","","","","","","","","","","","",""},
			{"","","EPS","","","EPS","","","","","","","EPS","EPS","EPS","EPS","EPS","EPS","EPS","","","EPS","EPS","EPS","EPS","EPS","EPS","EPS","","","","INDICE REPTIDNEST","EPS","","","","","","","","","","","","","","","","",""},
			{"","","EPS","","","EPS","","","","","","","IDNEST REPTVARORFUNCALL","","EPS","EPS","EPS","EPS","EPS","","","EPS","EPS","EPS","EPS","EPS","EPS","EPS","","","","","EPS","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","",". id A4 IDNEST2 B6","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","A5 REPTIDNEST V3","","( APRMS )","A5 REPTIDNEST V3","","","","","","","A5 REPTIDNEST V3","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","",""},
			{"","","EPS","","","EPS","","","","","","","","","EPS","EPS","EPS","EPS","EPS","","","MULOP A3 FACTOR B7 RIGTEM","MULOP A3 FACTOR B7 RIGTEM","MULOP A3 FACTOR B7 RIGTEM","EPS","EPS","EPS","EPS","","","","","EPS","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","*","/","and","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","EPS","","","EPS","","","","","","","","","EPS","EPS","EPS","EPS","EPS","","","","","","ADDOP A3 TERM B8 RIGARIEXPR","ADDOP A3 TERM B8 RIGARIEXPR","ADDOP A3 TERM B8 RIGARIEXPR","EPS","","","","","EPS","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","+","-","or","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","APRMTAIL REPTAPRM","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","",", EXPR","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","EPS A5","","","","","","","","","",". id A4 STATMTIDNEST V19","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","id A4 FACTOR2 A5 REPTVARORFUNCALL V7 V14","","","( ARIEXPR )","","","","","","","","","","","","","","","","","","","","SIGN A3 FACTOR V10","SIGN A3 FACTOR V10","","","intLit A7","floatLit A1","not A2 FACTOR V10","","","","","","","","","","","","","","","","","","",""},
			{"","","A5 REPTIDNEST V3","","( APRMS )","A5 REPTIDNEST V3","","","","","","","A5 REPTIDNEST V3","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","A5 REPTIDNEST V3","A5 REPTIDNEST V3","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","+","-","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","[ ARIEXPR ]","","","","","","","","","","","","","","","","","",""},
			{"","STATMT","","STATMT","","","STATMT","STATMT","STATMT","STATMT","","","","","","","","","","","","","","","","","","","","","","","","LOCVARDEC","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","localvar id A4 : TYPE A1 AROBJ C3 ;","","","","","","","","","","","","","","","",""},
			{"","id","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","integer","float","","","","","","","","","","","","",""},
			{"","","A5 REPTARSIZ C2","","( APRMS )","","","","","","","","","","","","","","","","","","","","","","","","","","","A5 REPTARSIZ C2","","","","","","A5 REPTARSIZ C2","","","","","","","","","","","",""},
			{"","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","ARRSIZE REPTARSIZ","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","[ ARRSIZE2","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","intlit A7 ]","","","","]","","","","","","","","","","","","","","","","",""},
			{"","LOCVARSTAT REPTVARSTAT","","LOCVARSTAT REPTVARSTAT","","","LOCVARSTAT REPTVARSTAT","LOCVARSTAT REPTVARSTAT","LOCVARSTAT REPTVARSTAT","LOCVARSTAT REPTVARSTAT","","","","","","","","","","","EPS","","","","","","","","","","","","","LOCVARSTAT REPTVARSTAT","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","{ A5 REPTVARSTAT C1 }","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","FUNHEAD FUNBODY C11","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","function A11 id A4 FUNHEADTAIL","","","","","","","","",""},
			{"","","","","( FPARM ) arrow RETTYPE C13","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","sr FUNHEADMEMTAIL","","","","","","","",""},
			{"","id A4 ( FPARM ) arrow RETTYPE C14","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","constructor A9 ( FPARM ) D6","","","","","",""},
			{"","TYPE A1","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","TYPE A1","TYPE A1","","","","","","","","void A1","","","","",""},
			{"","id A4 : TYPE A1 A5 REPTPRM3 C2 A5 REPTPRM4 C5 C6","","","","EPS A5","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","EPS","","","","ARRSIZE REPTPRM3","","","","","","ARRSIZE REPTPRM3","","","","","","","","","","","",""},
			{"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","FPARMTAIL REPTPRM4","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","",", id A4 : TYPE A1 A5 REPTPRMTAIL C2 C4","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","EPS","","","","ARRSIZE REPTPRMTAIL","","","","","","ARRSIZE REPTPRMTAIL","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","MEMFUNHEAD ;","","","MEMFUNHEAD ;","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","function A11 id A4 : ( FPARM ) arrow RETTYPE C7","","","constructor A9 : ( FPARM ) D4","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","MEMFUNDEC","","","MEMFUNDEC","","","","MEMVARDECL","",""},
			{"","","","","","","","","","","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","VIS A1 MEMDECL D1 REPTMEMDECL","VIS A1 MEMDECL D1 REPTMEMDECL","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","public","private","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","attribute id A4 : TYPE A1 A5 REPTARSIZ C2 D3 ;","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","class A10 id A4 OPTITS { A5 REPTMEMDECL C8 } C9 ;",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","FUNDEF","","","","","","","","CLSDEC",""},
			{"EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","CLSFUNDEC REPTPROG","","","","","","","","CLSFUNDEC REPTPROG",""},
			{"","","","","","","","","","","","","","","","","","","","EPS A5","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","isa A8 id A4 A5 REPTLIST C12 D2"},
			{"","","","","","","","","","","","","","","","","","","","EPS","","","","","","","",", id A4 REPTLIST","","","","","","","","","","","","","","","","","","","","","",""},};
	
	
	
	public static int getCol(String s) {
		String[] str = {"S","ST","STATMT","STATMTIDNEST","STATID3","ASSOP","RELEXPR","RELOP","STATBLK","REPTBLK1","VAR","VAR2","REPTVAR","VARID","VARID2","APRMS","EXPR","EXPR2","ARIEXPR","TERM","REPTIDNEST","REPTVARORFUNCALL","IDNEST","IDNEST2","RIGTEM","MULOP","RIGARIEXPR","ADDOP","REPTAPRM","APRMTAIL","STATID2","FACTOR","FACTOR2","SIGN","INDICE","LOCVARSTAT","LOCVARDEC","TYPE","AROBJ","REPTARSIZ","ARRSIZE","ARRSIZE2","REPTVARSTAT","FUNBODY","FUNDEF","FUNHEAD","FUNHEADTAIL","FUNHEADMEMTAIL","RETTYPE","FPARM","REPTPRM3","REPTPRM4","FPARMTAIL","REPTPRMTAIL","MEMFUNDEC","MEMFUNHEAD","MEMDECL","REPTMEMDECL","VIS","MEMVARDECL","CLSDEC","CLSFUNDEC","REPTPROG","OPTITS","REPTLIST"};
		for(int i=0;i<str.length;i++) {
			if(str[i].equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}
	public static boolean isAST(String s) {
		String[] st = {"D1","D2","C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","V1","V2","V3","V4","V5","V6","V7","V8","V9","V10","V11","V12","V13","V14","V15","V16","V17","V18","V19","B1","B2","B3","B4","B5","B6","B7","B8","B9","A1","A3","A4","A5","A6","A7","A8","A9","A10","A11","D3","D4","A2","D5","D6"};
		for(int i=0;i<st.length;i++) {
			if(st[i].equals(s)) {
				return true;
			}
		}
		return false;
	}
	public static int getAST(String s) {
		String[] st = {"D1","D2","C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","V1","V2","V3","V4","V5","V6","V7","V8","V9","V10","V11","V12","V13","V14","V15","V16","V17","V18","V19","B1","B2","B3","B4","B5","B6","B7","B8","B9","A1","A3","A4","A5","A6","A7","A8","A9","A10","A11","D3","D4","A2","D5","D6"};
		for(int i=0;i<st.length;i++) {
			if(st[i].equals(s)) {
				return i;
			}
		}
		return 0;
	}
	public static void addRule(String s) {
		int x =getAST(s);
		String[] str = {"push(createsubtree(MEMBER,pop,pop))","push(createsubtree(OPTINHERITS,pop,pop,pop))","push(createsubtree(Body,popuntillEPS))","push(createsubtree(ARRSIZE,popuntillEPS))","push(createsubtree(VarDec,pop,pop,pop))","push(createsubtree(FParmTail,pop,pop,pop))","push(createsubtree(FParmTailList,popuntillEPS))","push(createsubtree(FParms,pop,pop,pop,pop))","push(createsubtree(functionHead,pop,pop,pop,pop))","push(createsubtree(MemberList,popuntillEPS))","push(createsubtree(ClassDec,pop,pop,pop,pop))","push(createsubtree(Program,popuntillEPS))","push(createsubtree(FunctionDec,pop,pop,pop,pop))","push(createsubtree(List,popuntillEPS))","push(createsubtree(FunctionTail,pop,pop))","push(createsubtree(FunctionMemberTail,pop,pop,pop))","push(createsubtree(VariableList,popuntillEPS))","push(createsubtree(RightExprList,popuntillEPS))","push(createsubtree(ReptIndice,popuntillEPS))","push(createsubtree(ArithExpr,pop,pop))","push(createsubtree(RelExpr,pop,pop,pop))","push(createsubtree(StatmentList,popuntillEPS))","push(createsubtree(IDNEST,popuntillEPS))","push(createsubtree(AParmList,popuntillEPS))","push(createsubtree(ExprTail,popuntillEPS))","push(createsubtree(Factor,pop,pop))","push(createsubtree(Variable,pop,pop))","push(createsubtree(Term,pop,pop))","push(createsubtree(TermTail,popuntillEPS))","push(createsubtree(Factor,pop,pop,pop))","push(createsubtree(Aparms,pop,pop))","push(createsubtree(VariableTail,pop,pop))","push(createsubtree(VarId2,pop,pop))","push(createsubtree(Variable,pop,pop))","push(createsubtree(StatId,pop,pop))","push(createsubtree(StatmentIdnest,popuntillEPS))","push(createsubtree(Statment,pop,pop))","push(createsubtree(StatmentIdnest,pop,pop))","push(createsubtree(Statment,pop,pop,pop))","push(createsubtree(StatmentIdnest,pop,pop,pop))","push(createsubtree(Idnest2,pop,pop))","push(createsubtree(RightTerm,pop,pop))","push(createsubtree(RightExpr,pop,pop))","push(createsubtree(StatId2,popuntillEPS))","push(createLeaf(type))","push(createLeaf(sign))","push(createLeaf(id))","push(EPS)","push(createLeaf(relop))","push(createLeaf(intLit))","push(createLeaf(isa))","push(createLeaf(constructor))","push(createLeaf(class))","push(createLeaf(function))","push(createsubtree(MemberDec,pop,pop,pop))","push(createsubtree(functionMemberHead,pop,pop))","push(createLeaf(not))","push(createsubtree(EXPR,pop,pop))","push(createsubtree(FunctionMemberTail,pop,pop))"};
		s = str[x];
		if(s.contains("push(createLeaf")) {
			s = s.substring(16);
			s = s.split("\\)")[0];
			astSt.push(new TreeNode(s));
		}
		else if(s.contains("push(createsubtree(")) {
			s = s.substring(19);
			String[] part = s.split(",");
			fNode = new TreeNode(part[0]);
			if(part[1].contains("popuntillEPS")) {
				while(astSt.peek().val!="EPS"){
					TreeNode temp = astSt.pop();
					fNode.children.add(temp);
				}
				Collections.reverse(fNode.children);
				astSt.pop();
				astSt.push(fNode);
			
			}
			else {
				for(int i=1;i<part.length;i++) {
					TreeNode temp = astSt.pop();
					part[i] = temp.val;	
					
					fNode.children.add(temp);
				}
				
				Collections.reverse(fNode.children);
				astSt.push(fNode);
				
			}
		}
		else {
			astSt.push(new TreeNode("EPS"));
		}
		
	}
	public static void inSPush(String sr) {
		String[] ad = sr.split(" ");
		for(int i=ad.length-1;i>=0;i--) {
			st.push(ad[i]);
		}
	}
	public static int getLine(String s) {
		String[] st = {"$","id",";","while","(",")","read","write","return","if","then","else",".","=","neq","lt","gt","leq","geq","{","}","*","/","and","+","-","or",",","intLit","floatLit","not","[","]","localvar",":","integer","float","lsqbr","intlit","rsqbr","function","sr","arrow","constructor","void","public","private","attribute","class","isa"};
		for(int i=0;i<st.length;i++) {
			if(st[i].equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}
	public static boolean isTerminal(String s) {
		String[] st = {"$","id",";","while","(",")","read","write","return","if","then","else",".","=","neq","lt","gt","leq","geq","{","}","*","/","and","+","-","or",",","intLit","floatLit","not","[","]","localvar",":","integer","float","lsqbr","intlit","rsqbr","function","sr","arrow","constructor","void","public","private","attribute","class","isa"};
		
		for(int i=0;i<st.length;i++) {
			if(st[i].equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
	public static String nextToken() {
		String[] st = {"$","id",";","while","(",")","read","write","return","if","then","else",".","=","neq","lt","gt","leq","geq","{","}","*","/","and","+","-","or",",","intLit","floatLit","not","[","]","localvar",":","integer","float","lsqbr","intlit","rsqbr","function","sr","arrow","constructor","void","public","private","attribute","class","isa"};
		numb++;
		while(numb<token.length && token[numb][0] != null && (token[numb][0].toLowerCase().equalsIgnoreCase("inlinecmt") || token[numb][0].toLowerCase().equalsIgnoreCase("blockcmt"))) {
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
		for(int i=0;i<st.length;i++) {
			if(st[i].equalsIgnoreCase(token[numb][1])) {
				return token[numb][1];
			}
		}
		return token[numb][0];
		
	}
	public static void parse(){
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
	                a = nextToken();
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
	                System.out.println("Error in line " + token[numb][2]+ " Expected : " + x + ", got : "+ a);
	            	k=1;
	                a=nextToken();
	            }
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
	            	System.out.println("Error in line " + token[numb][2] + " Expected : " + x + ", got : "+ a);
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
            if(b!=-1 && rules[b][0].equals("EPS")) {
            	continue;
            }
            else if(isAST(a)) {
            	addRule(a);
            }
            else break;
	    }
	    String ans = "";
	    while(!astSt.isEmpty()) {
	    	ans = "";
	    	while(!astSt.isEmpty() && astSt.peek().val!= "EPS") {
	    		ans = astSt.pop().val;
	    		if(ans.contains("\n")) {
					String[] ins1 = ans.split("\n");
					ans="";
					for(int ab=0;ab<ins1.length;ab++) {
						ans += "| " + ins1[ab]+ "\n";
					}
				}
	    	}
	    	if(!astSt.isEmpty()) {
	    		astSt.pop();
	    		astSt.push(new TreeNode(ans));
	    	}
	    }
	    String[] finAns = ans.split("\n");
	    ans = "";
	    for(int i=0;i<finAns.length;i++) {
	    	String an = finAns[i].substring(2);
	    	if((an.length()>0 && an.charAt(an.length()-1)== ' ') || an.equals("") || an.contains("EPS")) {
	    		continue;
	    	}
	    	ans += an+"\n";
	    	
	    }
	    
	    if (!(a.equals("$")) || k==1){ 
	        System.out.println("Parsing Failed! ");
	    }
	    else {
	    	printList(fNode);
	    	System.out.println("Parsed Successfully");
	    }
	    }

	public static void parser(String inps) {
		inp = inps;
		token = LexicalDriver.lexical(inp+".src");
		parse();
	    }
}
