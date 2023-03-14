public class Lex_Analyzer {

static int comment=0;
static int lineNumber = 1,already=0;
static String s="",errorFile=".outlexerrors",tokenFile=".outlextokens";
static char[] symbols = new char[] {'<','>','(',')','{','}','[',']','+','-','/','*','=',';',':',',','.'};
static String[] reserved = new String[] {"==","+","or","(",";","integer","while","localvar","<>","-","and",")",",","float","if","constructor","<","*","not","{",".","void","then","attribute",">","/","}",":","class","else","function","<=","=","[","=>","read","public",">=","]","::","isa","write","private","return"};



//adding operators
public static String[] addOperators(String word) {
	
	String[] stans = new String[3];
      if(word.equals("==")){
    	  s = s.substring(1);
    	  stans[0] = "eq"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("+")) {
    	  stans[0] = "Plus"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("(")) {
    	  stans[0] = "Openpar"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals(";")) {
    	  stans[0] = "Semi"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals(":")) {
    	  stans[0] = "Colon"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
    	  
      }
      else if(word.equals("<>"))	  {
    	  s = s.substring(1);
    	  stans[0] = "NotEq"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("-")) {
    	  stans[0] = "Minus"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals(")")) {
    	  stans[0] = "Closepar"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans; 
      }
      else if(word.equals(",")) {
    	  stans[0] = "Comma"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals(".")) {
    	  stans[0] = "dot"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("{")) {
    	  stans[0] = "Opencubr"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("}")) {
    	  stans[0] = "Closecubr"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("[")) {
    	  stans[0] = "Opensqbr"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("]")) {
    	  stans[0] = "Closesqbr"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("<")) {
    	  stans[0] = "Lt"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals(">")) {
    	  stans[0] = "gt"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("<=")) 	  {
    	  s = s.substring(1);
    	  stans[0] = "leq"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals(">=")) 	  {
    	  s = s.substring(1);
    	  stans[0] = "geq"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("=")) 	  {
    	  stans[0] = "assign"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("::")) 	  {
    	  s = s.substring(1);
    	  stans[0] = "sr"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("=>")) 	  {
    	  s = s.substring(1);
    	  stans[0] = "arrow"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("*")) {
    	  stans[0] = "Mult"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else if(word.equals("/")) {
    	  stans[0] = "Div"; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }
      else {
    	  stans[0] = word; stans[1]=word;stans[2] = String.valueOf(lineNumber);
    	  return stans;
      }

}

//get the word until space of any word breakers
public static String[] getWord() {
	String[] an = new String[3];
	already=0;
	int i=0;
	String str="";
	while(i<s.length() && s.charAt(i)=='\n') {
		lineNumber++;
		i++;
		s = s.substring(i);
		i=0;
	}
	while(i<s.length() && s.charAt(i)==' ') {
		i++;
		s = s.substring(i);
		i=0;
	}
	s = s.substring(i);
	i=0;
	if(s.equals("")) {
		return an;
	}
	while(i<s.length() && !s.equals("") && s.charAt(i)=='\n' || s.charAt(i)==' ') {
		if(s.charAt(i)=='\n') {
			lineNumber++;
		}
		i++;
		s = s.substring(i);
		i=0;
	}
	s = s.substring(i);
	if(i<s.length()-1 && s.charAt(0)=='/' && s.charAt(1)=='/') {
		i=2;
		while(s.charAt(i)!='\n') {
			str+=s.charAt(i);
			i++;
			s = s.substring(i);
			i=0;
		}
		an[0]="inlinecmt";
		an[1]=str;
		an[2]=String.valueOf(lineNumber);
		return an;
		
	}
	str="";
	while(i<s.length() && !s.equals("") && s.charAt(i)=='\n' || s.charAt(i)==' ') {
		if(s.charAt(i)=='\n') {
			lineNumber++;
		}
		i++;
		s = s.substring(i);
		i=0;
	}
	s = s.substring(i);
	int xa=0;
	if(i<s.length()-2 && s.charAt(i)=='/' && s.charAt(i+1)=='*') {
		xa = lineNumber;
		comment++;
		i=2;
	}
	while(comment>0) {
		if(s.charAt(i)!='\n')	str+=s.charAt(i);
		if(s.charAt(i)=='\n') {
			lineNumber++;
		}
		if(i<s.length()-1 && s.charAt(i)=='/' && s.charAt(i+1)=='*') {
			comment++;
		}
		if(i<s.length()-2 && s.charAt(i)=='*' && s.charAt(i+1)=='/') {
			if(comment>1) str+=s.charAt(i+1);
			s = s.substring(i+1);
			i=0;
			comment--;
			if(comment==0) {
				s = s.substring(1);
				an[0]="Blockcmt";
				an[1]=str;
				an[2]=String.valueOf(xa);
				return an;
			}
		}
		i++;
		s = s.substring(i);
		i=0;
	}
	
	if(i<s.length() && s.charAt(i)=='/') {
		s = s.substring(1);
	}
	while(i<s.length() && s.charAt(i)==' ') {
		i++;
	}
	if(i<s.length()-1 && s.charAt(i)=='\n') {
		lineNumber++;
		i++;
		s = s.substring(i);
		i=0;
	}
	while(i<s.length() && (s.charAt(i)!=' ' && s.charAt(i)!= '\t' && s.charAt(i)!='\n')){
		for(int j=0;j<symbols.length;j++) {
			if(s.charAt(i)==symbols[j]) {
				if((s.charAt(i)=='-' ||s.charAt(i)=='+') && i>0 && s.charAt(i-1)=='e') {
					break;
				}
				if(s.charAt(i)=='.' && str.length()>0 && isDigit(str.charAt(0))) {
					break;
				}
				if(str.length()>0) {
					s = s.substring(str.length()-1);
					return isToken(str);
				}
				if(i<s.length()-1 && s.charAt(i)=='=' && s.charAt(i+1)=='=') {
					i++;
					return addOperators("==");
				}
				else if(i<s.length()-1 && s.charAt(i)=='<' && s.charAt(i+1)=='>') {
					i++;
					return addOperators("<>");
				}
				else if(i<s.length()-1 && s.charAt(i)=='<' && s.charAt(i+1)=='=') {
					i++;
					return addOperators("<=");
				}
				else if(i<s.length()-1 && s.charAt(i)=='>' && s.charAt(i+1)=='=') {
					i++;
					return addOperators(">=");
				}
				else if(i<s.length()-1 && s.charAt(i)=='=' && s.charAt(i+1)=='>') {
					i++;
					return addOperators("=>");
				}
				else if(i<s.length()-1 && s.charAt(i)==':' && s.charAt(i+1)==':') {
					i++;
					return addOperators("::");
				}
				else {
					return addOperators(String.valueOf(s.charAt(i)));
				}
			}
		}
		str+=s.charAt(i);
		i++;
	}
	if(already!=1 && str.length()>0) {
		s = s.substring(str.length());
		return isToken(str);
	}
	an[0]="";
	an[1]=str;
	an[2]=String.valueOf(lineNumber);
	return an;
}

public static String[] nextToken(String s1,String error,String token) {
	errorFile = error;
	tokenFile = token;
	s=s1;
	String[] ret = new String[4];
	String[] st = getWord();
	if(s.length()>0 && s.charAt(0)=='\n') {
		lineNumber++;
	}
	if(st[0]==null) {
		return ret;
	}
	if(st[0].startsWith("Lexical error")) {
		ret[2]="e";
	}
	else {
		ret[2]="l";
	}
	ret[0] = st[0];
	ret[1] = st[1];
	ret[2] = st[2];
	ret[3] = s;
	return ret;
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


public static String[] isToken(String word) {
	String[] an = new String[3];
	for(int ab=0;ab<reserved.length;ab++) {
		if(reserved[ab].equals(word)) {
			
			if(word.equals(".")) {
				an[0]="dot";
				an[1]=word;
				an[2]=String.valueOf(lineNumber);
				return an;
			}
			an[0]=word;
			an[1]=word;
			an[2]=String.valueOf(lineNumber);
			return an;
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
				an[0]="Lexical error: Invalid id:";
				an[1]=word;
				an[2]=String.valueOf(lineNumber)+".\n";
				return an;
			}
			i++;
		}
		an[0]="id";
		an[1]=word;
		an[2]=String.valueOf(lineNumber);
		return an;
	}
	else if(word.charAt(i)>=48 && word.charAt(i)<59) {
		if(word.charAt(0)=='0' && word.length()==1) {
			an[0]="intLit";
			an[1]=word;
			an[2]=String.valueOf(lineNumber);
			return an;
		}
		else if(word.charAt(0)=='0' && word.length()>1 && isDigit(word.charAt(1))) {
			an[0]="Lexical error: Invalid Number:";
			an[1]=word;
			an[2]=String.valueOf(lineNumber);
			return an;
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
				an[0]="Lexical error: Invalid Number:";
				an[1]=word;
				an[2]=String.valueOf(lineNumber);
				return an;
			}
		}
		if(inte==0) {
			an[0]="intLit";
			an[1]=word;
			an[2]=String.valueOf(lineNumber);
			return an;
		}
		else {
			
			if((word.charAt(0)=='0' && word.charAt(1)!='.') || word.charAt(0)=='.') {
				an[0]="Lexical error: Invalid Float Number:";
				an[1]=word;
				an[2]=String.valueOf(lineNumber);
				return an;
			}
			else {
				i=0;
				while(word.charAt(i)!='.') {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						an[0]="Lexical error: Invalid Float Number:";
						an[1]=word;
						an[2]=String.valueOf(lineNumber);
						return an;
					}
					i++;
				}
				if(i==word.length()-2 && word.charAt(i+1)=='0') {
//					tokenAdd(word,"Float");
					an[0]="floatLit";
					an[1]=word;
					an[2]=String.valueOf(lineNumber);
					return an;
				}
				i++;
				while(i<word.length() && word.charAt(i)!='e') {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						an[0]="Lexical error: Invalid Float Number:";
						an[1]=word;
						an[2]=String.valueOf(lineNumber);
						return an;
					}
					i++;
				}
				if(i>=word.length()) {
					if(word.charAt(i-1)=='0') {
						an[0]="Lexical error: Invalid Float Number:";
						an[1]=word;
						an[2]=String.valueOf(lineNumber);
						return an;
				}
					else {
						an[0]="floatLit";
						an[1]=word;
						an[2]=String.valueOf(lineNumber);
						return an;
					}
				};
				if(i>1 && word.charAt(i-1)=='0' && word.charAt(i-2)!='.') {
					an[0]="Lexical error: Invalid Float Number:";
					an[1]=word;
					an[2]=String.valueOf(lineNumber);
					return an;
				}
				i++;
				if(word.charAt(i)=='-' || word.charAt(i)=='+') {
					i++;
				}
				if(i<word.length()-1) {
					if(word.charAt(i)=='0') {
						an[0]="Lexical error: Invalid Float Number:";
						an[1]=word;
						an[2]=String.valueOf(lineNumber);
						return an;
					}
				}
				while(i<word.length()) {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						an[0]="Lexical error: Invalid Float Number:";
						an[1]=word;
						an[2]=String.valueOf(lineNumber);
						return an;
					}
					i++;
				}
				an[0]="floatLit";
				an[1]=word;
				an[2]=String.valueOf(lineNumber);
				return an;
			}
		}
	}
	else {
		an[0]="Lexical error: Invalid id:";
		an[1]=word;
		an[2]=String.valueOf(lineNumber);
		return an;
	}
}
	
	
}
