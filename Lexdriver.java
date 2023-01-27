import java.io.*;
import java.util.Scanner;
public class Lexdriver {
static String[] reserved;
static int comment=0;
static int lineNumber = 1,already=0;
static String s="",errorFile=".outlexerrors",tokenFile=".outlextokens";
static char[] symbols = new char[] {'<','>','(',')','{','}','[',']','+','-','/','*','=',';',':',','};

public static void tokenAdd(String word, String type) {
	try {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(tokenFile, true));
        out.write("[ "+type+" , "+ word +" , "+ lineNumber + " ], ");
        out.close();
    }
    catch (IOException e) {
        System.out.println("exception occurred" + e);
    }
}
//if token has error
public static void errorMsg(String word, String type) {
	try {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(errorFile, true));
        out.write("Lexical error: Invalid "+ type +": "+word+": line "+lineNumber+". \n");
        out.close();
    }
    catch (IOException e) {
        System.out.println("exception occurred" + e);
    }
}

//if the token is reserved
public static void addReserved(String word) {
	try {
      BufferedWriter out = new BufferedWriter(
          new FileWriter(tokenFile, true));
      out.write("[ " + word +" , "+ word +" , "+ lineNumber + " ], ");
      out.close();
  }
  catch (IOException e) {
      System.out.println("exception occurred" + e);
  }

}

//adding operators
public static void addOperators(String word) {
	try {
      BufferedWriter out = new BufferedWriter(
          new FileWriter(tokenFile, true));
      if(word.equals("=="))     	  {
    	  out.write("[ eq, " + word +", "+ lineNumber + " ], "); s = s.substring(1);
      }
      else if(word.equals("+"))    	  out.write("[ plus, " + word +", "+ lineNumber + " ], ");
      else if(word.equals("("))    	  out.write("[ openpar, " + word +", "+ lineNumber + " ], ");
      else if(word.equals(";"))   	  out.write("[ semi, " + word +", "+ lineNumber + " ], ");
      else if(word.equals(":"))		  out.write("[ colon , " + word+", " + lineNumber + " ], ");
      else if(word.equals("<>"))	  {
    	  out.write("[ noteq, " + word +", "+ lineNumber + " ], "); s = s.substring(1);
      }
      else if(word.equals("-"))	      out.write("[ minus, " + word +", "+ lineNumber + " ], ");
      else if(word.equals(")"))   	  out.write("[ closepar , "+ word +" , "+ lineNumber + " ], ");  
      else if(word.equals(","))   	  out.write("[ comma , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("."))   	  out.write("[ dot , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("{"))   	  out.write("[ opencubr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("}"))  	  out.write("[ closecubr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("["))    	  out.write("[ opensqbr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("]"))       out.write("[ closesqbr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("<"))    	  out.write("[ lt , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals(">")) 	  out.write("[ gt , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("<=")) 	  {
    	  out.write("[ leq , "+ word +" , "+ lineNumber + " ], "); s = s.substring(1);
      }
      else if(word.equals(">=")) 	  {
    	  out.write("[ geq , "+ word +" , "+ lineNumber + " ], "); s = s.substring(1);
      }
      else if(word.equals("=")) 	  {
    	  out.write("[ assign , "+ word +" , "+ lineNumber + " ], ");
      }
      else if(word.equals("::")) 	  {
    	  out.write("[ scopeop , "+ word +" , "+ lineNumber + " ], "); s = s.substring(1);
      }
      else if(word.equals("=>")) 	  {
    	  out.write("[ retruntype , "+ word +" , "+ lineNumber + " ], "); s = s.substring(1);
      }
      else if(word.equals("*"))		  out.write("[ mult , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("/"))		  out.write("[ div , "+ word +" , "+ lineNumber + " ], ");
      else 							  out.write("[ " + word +" , "+ word +" , "+ lineNumber + " ], ");
      out.close();
  }
  catch (IOException e) {
      System.out.println("exception occurred" + e);
  }

}

//get the word until space of any word breakers
public static String getWord() {
	already=0;
	int i=0;
	String str="";
	if(s.charAt(0)=='/' && s.charAt(1)=='/') {
		i=2;
		while(s.charAt(i)!='\n') {
			str+=s.charAt(i);
			i++;
			s = s.substring(i);
			i=0;
		}
		tokenAdd(str,"inlinecmt");
	}
	str="";
	if(s.charAt(0)=='/' && s.charAt(1)=='*') {
		comment++;
		i=2;
	}
	while(comment>0) {
		if(s.charAt(i)!='\n')	str+=s.charAt(i);
		if(s.charAt(i)=='\n') {
			lineNumber++;
		}
		if(i<s.length()-2 && s.charAt(i)=='/' && s.charAt(i+1)=='*') {
			comment++;
		}
		if(i<s.length()-2 && s.charAt(i)=='*' && s.charAt(i+1)=='/') {
			if(comment>1) str+=s.charAt(i+1);
			s = s.substring(i+1);
			i=0;
			comment--;
			if(comment==0) {
				tokenAdd(str.substring(0,str.length()-1),"blockCmt");
				str="";
			}
		}
		i++;
		s = s.substring(i);
		i=0;
	}
	if(s.charAt(i)=='/') {
		s = s.substring(1);
	}
	while(i<s.length() && (s.charAt(i)!=' ' && s.charAt(i)!= '\t' && s.charAt(i)!='\n')){
		int x=0;
		for(int j=0;j<symbols.length;j++) {
			if(s.charAt(i)==symbols[j]) {
				if((s.charAt(i)=='-' ||s.charAt(i)=='+') && i>0 && s.charAt(i-1)=='e') {
					x=0;
					break;
				}
				if(str.length()>0) {
					isToken(str);
					already=1;
				}
				if(i<s.length()-1 && s.charAt(i)=='=' && s.charAt(i+1)=='=') {
					i++;
					addOperators("==");
				}
				else if(i<s.length()-1 && s.charAt(i)=='<' && s.charAt(i+1)=='>') {
					i++;
					addOperators("<>");
				}
				else if(i<s.length()-1 && s.charAt(i)=='<' && s.charAt(i+1)=='=') {
					i++;
					addOperators("<=");
				}
				else if(i<s.length()-1 && s.charAt(i)=='>' && s.charAt(i+1)=='=') {
					i++;
					addOperators(">=");
				}
				else if(i<s.length()-1 && s.charAt(i)=='=' && s.charAt(i+1)=='>') {
					i++;
					addOperators("=>");
				}
				else if(i<s.length()-1 && s.charAt(i)==':' && s.charAt(i+1)==':') {
					i++;
					addOperators("::");
				}
				else {
					addOperators(String.valueOf(s.charAt(i)));
				}
				x=1;
				break;
			}
		}
		if(x==1) {
			break;
		}
		str+=s.charAt(i);
		i++;
	}
	if(already!=1 && str.length()>0) {
		isToken(str);
	}
	if(s.charAt(i)=='\n' && i<s.length()-1) {
		lineNumber++;
		i++;
		try {
			BufferedWriter out = new BufferedWriter(
			      new FileWriter(tokenFile, true));
			out.write('\n');
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return str;
}

public static void nextToken() {
	String word = "a";
	while(!s.equals("")) {
		word = getWord();
		if(word.length()!=s.length())
			s = s.substring(word.length()+1);
		else s = "";
	}
}
public static boolean isDigit(char x) {
	if(x>=48 && x<59) return true;
	return false;
	
}
public static boolean isLetter(char x) {
	if((x>=97 && x<123) || (x>=65 && x<92)) {
		return true;
	}
	return false;
}


public static void isToken(String word) {
	for(int ab=0;ab<reserved.length;ab++) {
		if(reserved[ab].equals(word)) {
			if(word.equals(".")) {
				addOperators(word);
			}
			else addReserved(word);
			return;
		}
	}
	int i=0;
	if(word.toLowerCase().charAt(i)>=97 && word.toLowerCase().charAt(i)<123) {
		i++;
		while(i<word.length()) {
			if(isDigit(word.charAt(i)) || isLetter(word.charAt(i)) || word.charAt(i)=='_') {
				i++;
			}
			else {
				errorMsg(word,"id");
				return;
			}
			i++;
		}
		tokenAdd(word, "id");
	}
	else if(word.charAt(i)>=48 && word.charAt(i)<59) {
		if(word.charAt(0)=='0' && word.length()==1) {
			tokenAdd(word,"intNumber");
			return;
		}
		else if(word.charAt(0)=='0' && word.length()>1 && isDigit(word.charAt(1))) {
			errorMsg(word,"Number");
			return;
		}
		int inte=0;
		while(i<word.length()) {
			if(isDigit(word.charAt(i)) || word.charAt(i)=='e' || word.charAt(i)=='+' || word.charAt(i)=='-' || word.charAt(i)=='.') {
				if(word.charAt(i)=='e' || word.charAt(i)=='+' || word.charAt(i)=='-' || word.charAt(i)=='.') {
					inte=1;
				}
				i++;
			}
			else {
				errorMsg(word,"Number");
				return;
			}
		}
		if(inte==0) {
			tokenAdd(word,"intNumber");
			return;
		}
		else {
			
			if((word.charAt(0)=='0' && word.charAt(1)!='.') || word.charAt(0)=='.') {
				errorMsg(word,"Float Number");
				return;
			}
			else {
				i=0;
				while(word.charAt(i)!='.') {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						errorMsg(word,"Float Number");
						return;
					}
					i++;
				}
				if(i==word.length()-2 && word.charAt(i+1)=='0') {
					tokenAdd(word,"Float");
					return;
				}
				i++;
				while(i<word.length() && word.charAt(i)!='e') {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						errorMsg(word,"Float Number");
						return;
					}
					i++;
				}
				if(i>=word.length()) {
					if(word.charAt(i-1)=='0') {
						errorMsg(word,"Float Number");
						return;
				}
					else {
						tokenAdd(word,"Float");
						return;
					}
				}
				i++;
				if(word.charAt(i)=='-' || word.charAt(i)=='+') {
					i++;
				}
				if(i<word.length()-1) {
					if(word.charAt(i)=='0') {
						System.out.print("here");
						errorMsg(word,"Float Number");
						return;
					}
				}
				while(i<word.length()) {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						errorMsg(word,"Float Number");
						return;
					}
					i++;
				}
				tokenAdd(word,"Float");
				return;
			}
		}
	}
	else {
		errorMsg(word,"id");
	}
}
	public static void main(String[] args) {
		System.out.println("Enter file name:");
		Scanner sc = new Scanner(System.in);
		String inp = sc.next();
		
		File f1 = new File(inp);
		reserved = new String[] {"==","+","or","(",";","integer","while","localvar","<>","-","and",")",",","float","if","constructor","<","*","not","{",".","void","then","attribute",">","/","}",":","class","else","function","<=","=","[","=>","self","read","public",">=","]","::","isa","write","private","return"};
		Scanner myReader = null;
		try {
			myReader = new Scanner(f1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (myReader.hasNextLine()) {
	        s += myReader.nextLine()+'\n';
	      }
		inp = inp.substring(0,inp.length()-4);
		System.out.println(inp);
		errorFile =inp+".outlexerrors";
		tokenFile=inp+".outlextokens";
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(errorFile));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(tokenFile));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		nextToken();
		System.out.print("end");
		sc.close();
	}
	
}
