import java.io.*;
import java.util.Scanner;
import java.util.Stack;

public class Parser {
	public static String tok = "";
	public static String inp = "";
	public static Stack<String> st = new Stack<>();
	public static int numb=-1;
	public static String[][] token;
	static String[][] rules = {{"ST $","","","","","","","","","ST $","","","","","","","","","","","ST $","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"REPTST","","","","","","","","","REPTST","","","","","","","","","","","REPTST","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","EXPR REPTAPARM","EXPR REPTAPARM","","","EXPR REPTAPARM","","","","EXPR REPTAPARM","","","","","EXPR REPTAPARM","EPS","","EXPR REPTAPARM","EXPR REPTAPARM","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"",", EXPR","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","+","-","or","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","[ A","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","intLit ]","]","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","=","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","VARIABLE ASSOP EXPR","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","class id OPTCLSDEC2 { REPTCLSDEC4 } ;","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","CLSDEC","","","","","","","","","","","FUNDEF","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","ARITHEXPR B","ARITHEXPR B","","","ARITHEXPR B","","","","ARITHEXPR B","","","","","ARITHEXPR B","","","ARITHEXPR B","ARITHEXPR B","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","EPS","","","","","","","","","","","","EPS","","","EPS","","","","","","","","","","","","","","RELOP ARITHEXPR","RELOP ARITHEXPR","RELOP ARITHEXPR","RELOP ARITHEXPR","RELOP ARITHEXPR","RELOP ARITHEXPR","","","","","","","","","","","",""},
			{"","","ARITHEXPR RELOP ARITHEXPR","ARITHEXPR RELOP ARITHEXPR","","","ARITHEXPR RELOP ARITHEXPR","","","","ARITHEXPR RELOP ARITHEXPR","","","","","ARITHEXPR RELOP ARITHEXPR","","","ARITHEXPR RELOP ARITHEXPR","ARITHEXPR RELOP ARITHEXPR","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","TERM RIGARITHEXPR","TERM RIGARITHEXPR","","","TERM RIGARITHEXPR","","","","TERM RIGARITHEXPR","","","","","TERM RIGARITHEXPR","","","TERM RIGARITHEXPR","TERM RIGARITHEXPR","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","id : TYPE REPTFPARM3 REPTFPARM4","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"",", id : TYPE REPTFPARMTAIL4","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","SIGN FACTOR","SIGN FACTOR","","","intLit","","","","id REPTVAR0 F","","","","","( ARITHEXPR )","","","floatLit","not FACTOR","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","REPTVAR2","REPTVAR2","REPTVAR2","REPTVAR2","REPTVAR2","","REPTVAR2","","","REPTVAR2","","","REPTVAR2","","( APARM )","REPTVAR2","","","","","","","","","","REPTVAR2","REPTVAR2","REPTVAR2","","REPTVAR2","REPTVAR2","REPTVAR2","REPTVAR2","REPTVAR2","REPTVAR2","","","","","","","","","","","",""},
			{"","EPS","EPS","EPS","EPS","EPS","","EPS","EPS","","","","","EPS","","EPS","EPS","IDNEST REPTVAR0","","","","","","","","","EPS","EPS","EPS","","EPS","EPS","EPS","EPS","EPS","EPS","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","",". F id","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","EPS","EPS","EPS","EPS","INDICE REPTVAR2","","EPS","EPS","","EPS","","","EPS","","","EPS","","","","","","","","","","EPS","EPS","EPS","","EPS","EPS","EPS","EPS","EPS","EPS","","","","","","","","","","","",""},
			{"","","","","","[ ARITHEXPR ]","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","id REPTVAR0 REPTVAR2","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","id REPTVAR0 ( APARM )","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","{ REPTFUNBOD1 }","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","FUNCHEAD FUNBOD","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","function D","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","id G","","","","","","","","","","","constructor ( FPARM )","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","( FPARM ) arrow RETTYPE","","","","","","","sr H","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","id ( FPARM ) arrow RETTYPE","","","","","","","","","","","constructor ( FPARM )","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","localVar id : TYPE C","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","REPTLOCVAR4 ;","","","","","","","","REPTLOCVAR4 ;","","( APARM ) ;","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","STATM","","","","","","","","","","","","","","LOCVARDEC","","","","","","","","","","","","","STATM","","","STATM","STATM","STATM","STATM","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","MEMFUNDEC","MEMFUNDEC","","","","MEMVARDEC","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","function id : ( FPARM ) arrow RETTYPE ;","constructor : ( FPARM ) ;","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","attribute id : TYPE REPTMEMVAR4 ;","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","*","/","and","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","isa id REPTOPTCLSDEC22","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","eq","neq","lt","gt","leq","geq","","","","","","","","","","","",""},
			{"EPS","","","","","","","","","CLSDECORFUNCDEF REPTST","","","","","","","","","","","CLSDECORFUNCDEF REPTST","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","APARMTAIL REPTAPARM","","","","","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","EPS","","","","","","","","VISBTY MEMDEC REPTCLSDEC4","VISBTY MEMDEC REPTCLSDEC4","","","","VISBTY MEMDEC REPTCLSDEC4","","","","","","","","","","","","","","","","","","","","","VISBTY MEMDEC REPTCLSDEC4","VISBTY MEMDEC REPTCLSDEC4"},
			{"","EPS","","","","ARRSIZE REPTFPARM3","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","FPARMTAIL REPTFPARM4","","","","","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","EPS","","","","ARRSIZE REPTFPARMTAIL4","","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","LOCVARORSTDEC REPTFUNBOD1","","EPS","","","","","","","","","","","","LOCVARORSTDEC REPTFUNBOD1","","","","","","","","","","","","","LOCVARORSTDEC REPTFUNBOD1","","","LOCVARORSTDEC REPTFUNBOD1","LOCVARORSTDEC REPTFUNBOD1","LOCVARORSTDEC REPTFUNBOD1","LOCVARORSTDEC REPTFUNBOD1","","","",""},
			{"","","","","","INDICE REPTIND1","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","ARRSIZE REPTLOCVAR4","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","ARRSIZE REPTMEMVAR4","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"",", id REPTOPTCLSDEC22","","","","","","","","","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","STATM REPTSTBLK1","","EPS","","","","","","","","","","","","","","","","","","","","","","","","","STATM REPTSTBLK1","","","STATM REPTSTBLK1","STATM REPTSTBLK1","STATM REPTSTBLK1","STATM REPTSTBLK1","","","",""},
			{"","","","","","","","","","","TYPE","","","","","","","","","","","","","","","","","","","","","","","","","","void","","","","","","","","TYPE","TYPE","",""},
			{"","EPS","ADDOP TERM RIGARITHEXPR","ADDOP TERM RIGARITHEXPR","ADDOP TERM RIGARITHEXPR","","","EPS","","","","","","EPS","","","EPS","","","","","","","","","","","","","","EPS","EPS","EPS","EPS","EPS","EPS","","","","","","","","","","","",""},
			{"","EPS","EPS","EPS","EPS","","","EPS","","","","","","EPS","","","EPS","","","","","","","","","","MULOP FACTOR RIGTERM","MULOP FACTOR RIGTERM","MULOP FACTOR RIGTERM","","EPS","EPS","EPS","EPS","EPS","EPS","","","","","","","","","","","",""},
			{"","","+","-","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","STATM","{ REPTSTBLK1 }","","EPS","","","","","","","","","","","","","","","","","","","","","","","","STATM","","EPS","STATM","STATM","STATM","STATM","","","",""},
			{"","","","","","","","","","","id X","","","","","","","","","","","","","","","","","","","","","","","","","","","if ( RELEXPR ) then STATBLK else STATBLK ;","","","while ( RELEXPR ) STATBLK ;","read ( VARIABLE ) ;","write ( EXPR ) ;","return ( EXPR ) ;","","","",""},
			{"","","","","","REPTVAR0 REPTVAR2 Y","","","REPTVAR0 REPTVAR2 Y","","","","","","","( APARM ) ;","","REPTVAR0 REPTVAR2 Y","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","FACTOR RIGTERM","FACTOR RIGTERM","","","FACTOR RIGTERM","","","","FACTOR RIGTERM","","","","","FACTOR RIGTERM","","","FACTOR RIGTERM","FACTOR RIGTERM","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","id","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","integer","float","",""},
			{"","","","","","","","","","","","","","","","","","","","","EPS","EPS","","","","EPS","","","","","","","","","","","","","","","","","","","","","public","private"},
			{"","","","","","","","","ASSOP EXPR ;","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""}};
	
	
	
	public static int getCol(String s) {
		String[] str = {"S","ST","APARM","APARMTAIL","ADDOP","ARRSIZE","A","ASSOP","ASSSTAT","CLSDEC","CLSDECORFUNCDEF","EXPR","B","RELEXPR","ARITHEXPR","FPARM","FPARMTAIL","FACTOR","F","REPTVAR0","IDNEST","REPTVAR2","INDICE","VARIABLE","FUNCALL","FUNBOD","FUNDEF","FUNCHEAD","D","G","H","LOCVARDEC","C","LOCVARORSTDEC","MEMDEC","MEMFUNDEC","MEMVARDEC","MULOP","OPTCLSDEC2","RELOP","REPTST","REPTAPARM","REPTCLSDEC4","REPTFPARM3","REPTFPARM4","REPTFPARMTAIL4","REPTFUNBOD1","REPTIND1","REPTLOCVAR4","REPTMEMVAR4","REPTOPTCLSDEC22","REPTSTBLK1","RETTYPE","RIGARITHEXPR","RIGTERM","SIGN","STATBLK","STATM","X","TERM","TYPE","VISBTY","Y"};
		for(int i=0;i<str.length;i++) {
			if(str[i].equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}
	public static void inSPush(String sr) {
		String[] ad = sr.split(" ");
		for(int i=ad.length-1;i>=0;i--) {
			st.push(ad[i]);
		}
	}
	public static int getLine(String s) {
		String[] st = {"$",",","+","-","or","[","intLit","]","=","class","id","{","}",";",":","(",")",".","floatLit","not","function","constructor","sr","arrow","localVar","attribute","*","/","and","isa","eq","neq","lt","gt","leq","geq","void","if","then","else","while","read","write","return","integer","float","public","private"};
		for(int i=0;i<st.length;i++) {
			if(st[i].equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}
	public static boolean isTerminal(String s) {
		String[] st = {"$",",","+","-","or","[","intLit","]","=","class","id","{","}",";",":","(",")",".","floatLit","not","function","constructor","sr","arrow","localVar","attribute","*","/","and","isa","eq","neq","lt","gt","leq","geq","void","if","then","else","while","read","write","return","integer","float","public","private"};
		
		for(int i=0;i<st.length;i++) {
			if(st[i].equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
	public static String nextToken() {
		String[] st = {"$",",","+","-","or","[","intLit","]","=","class","id","{","}",";",":","(",")",".","floatLit","not","function","constructor","sr","arrow","localVar","attribute","*","/","and","isa","eq","neq","lt","gt","leq","geq","void","if","then","else","while","read","write","return","integer","float","public","private"};
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
	        else {
	        	int b = getLine(a);
	        	int c = getCol(x);
	            if (b!=-1 && c != -1 && !rules[c][b].equalsIgnoreCase("")) {
	            	st.pop() ; 
	            	if(rules[c][b].equalsIgnoreCase("EPS")) {
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
	            	k=1;
	            	a=nextToken();
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
	    while(true) {
	    	if(st.isEmpty()) break;
	    	a = st.pop();
	    	int b = getCol(a);
            if (b!=-1 && rules[b][0].equals("EPS")) {
            	continue;
            }
            else break;
	    }
	    if (!(a.equals("$")) || k==1){ 
	        System.out.println("Parsing Failed! ");
	    }
	    else 
	        System.out.println("Parsed Successfully");
	    }

	public static void parser(String inps) {
		inp = inps;
		token = LexicalDriver.lexical(inp+".src");
		parse();
	    }
}
