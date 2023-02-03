public class Lex_Analyzer {

static int comment=0;
static int lineNumber = 1,already=0;
static String s="",errorFile=".outlexerrors",tokenFile=".outlextokens";
static char[] symbols = new char[] {'<','>','(',')','{','}','[',']','+','-','/','*','=',';',':',',','.'};
static String[] reserved = new String[] {"==","+","or","(",";","integer","while","localvar","<>","-","and",")",",","float","if","constructor","<","*","not","{",".","void","then","attribute",">","/","}",":","class","else","function","<=","=","[","=>","self","read","public",">=","]","::","isa","write","private","return"};



//adding operators
public static String addOperators(String word) {
      if(word.equals("==")){
    	  s = s.substring(1);
    	  return ("[ eq, " + word +", "+ lineNumber + " ], ");
      }
      else if(word.equals("+"))    	  return ("[ plus, " + word +", "+ lineNumber + " ], ");
      else if(word.equals("("))    	  return ("[ openpar, " + word +", "+ lineNumber + " ], ");
      else if(word.equals(";"))   	  return ("[ semi, " + word +", "+ lineNumber + " ], ");
      else if(word.equals(":"))		  return ("[ colon , " + word+", " + lineNumber + " ], ");
      else if(word.equals("<>"))	  {
    	  s = s.substring(1);
    	  return ("[ noteq, " + word +", "+ lineNumber + " ], "); 
      }
      else if(word.equals("-"))	      return ("[ minus, " + word +", "+ lineNumber + " ], ");
      else if(word.equals(")"))   	  return ("[ closepar , "+ word +" , "+ lineNumber + " ], ");  
      else if(word.equals(","))   	  return ("[ comma , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("."))   	  return ("[ dot , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("{"))   	  return ("[ opencubr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("}"))  	  return ("[ closecubr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("["))    	  return ("[ opensqbr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("]"))       return ("[ closesqbr , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("<"))    	  return ("[ lt , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals(">")) 	  return ("[ gt , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("<=")) 	  {
    	  s = s.substring(1);
    	  return ("[ leq , "+ word +" , "+ lineNumber + " ], ");
      }
      else if(word.equals(">=")) 	  {
    	  s = s.substring(1);
    	  return ("[ geq , "+ word +" , "+ lineNumber + " ], ");
      }
      else if(word.equals("=")) 	  {
    	  return ("[ assign , "+ word +" , "+ lineNumber + " ], ");
      }
      else if(word.equals("::")) 	  {
    	  s = s.substring(1);
    	  return ("[ scopeop , "+ word +" , "+ lineNumber + " ], "); 
      }
      else if(word.equals("=>")) 	  {
    	  s = s.substring(1);
    	  return ("[ retruntype , "+ word +" , "+ lineNumber + " ], ");
      }
      else if(word.equals("*"))		  return ("[ mult , "+ word +" , "+ lineNumber + " ], ");
      else if(word.equals("/"))		  return ("[ div , "+ word +" , "+ lineNumber + " ], ");
      else 							  return ("[ " + word +" , "+ word +" , "+ lineNumber + " ], ");

}

//get the word until space of any word breakers
public static String getWord() {
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
		return "";
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
		return "[ inlinecmt , "+ str +" , "+ (lineNumber) + " ], ";
		
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
				return "[ BlockCmt , "+ (str.substring(0,str.length()-1))  +" , "+ xa + " ], ";
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
	return str;
}

public static String[] nextToken(String s1,String error,String token) {
	errorFile = error;
	tokenFile = token;
	s=s1;
	String[] ret = new String[3];
	String st = getWord();
	if(s.length()>0 && s.charAt(0)=='\n') {
		lineNumber++;
	}
	if(st.startsWith("Lexical error")) {
		ret[2]="e";
	}
	else {
		ret[2]="l";
	}
	ret[0] = st;
	ret[1] = s;
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


public static String isToken(String word) {
	for(int ab=0;ab<reserved.length;ab++) {
		if(reserved[ab].equals(word)) {
			
			if(word.equals(".")) {
				return "[ dot , "+ word +" , "+ lineNumber + " ], ";
			}
			return "[ "+word+" , "+ word +" , "+ lineNumber + " ], ";
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
				return  "Lexical error: Invalid id: "+word+": line "+lineNumber+". \n";
			}
			i++;
		}
		return "[ id , "+ word +" , "+ lineNumber + " ], ";
	}
	else if(word.charAt(i)>=48 && word.charAt(i)<59) {
		if(word.charAt(0)=='0' && word.length()==1) {
			return "[ intnum , "+ word +" , "+ lineNumber + " ], ";
		}
		else if(word.charAt(0)=='0' && word.length()>1 && isDigit(word.charAt(1))) {
			return "Lexical error: Invalid Number: "+word+": line "+lineNumber+". \n";
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
				return "Lexical error: Invalid Number: "+word+": line "+lineNumber+". \n";
			}
		}
		if(inte==0) {
			return "[ intnum , "+ word +" , "+ lineNumber + " ], ";
		}
		else {
			
			if((word.charAt(0)=='0' && word.charAt(1)!='.') || word.charAt(0)=='.') {
				return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
			}
			else {
				i=0;
				while(word.charAt(i)!='.') {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
					}
					i++;
				}
				if(i==word.length()-2 && word.charAt(i+1)=='0') {
//					tokenAdd(word,"Float");
					return "[ floatnum , "+ word +" , "+ lineNumber + " ], ";
				}
				i++;
				while(i<word.length() && word.charAt(i)!='e') {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
					}
					i++;
				}
				if(i>=word.length()) {
					if(word.charAt(i-1)=='0') {
						return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
				}
					else {
						return "[ floatnum , "+ word +" , "+ lineNumber + " ], ";
					}
				};
				if(i>1 && word.charAt(i-1)=='0' && word.charAt(i-2)!='.') {
					return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
				}
				i++;
				if(word.charAt(i)=='-' || word.charAt(i)=='+') {
					i++;
				}
				if(i<word.length()-1) {
					if(word.charAt(i)=='0') {
						return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
					}
				}
				while(i<word.length()) {
					if(word.charAt(i)<48 || word.charAt(i)>58) {
						return "Lexical error: Invalid Float Number: "+word+": line "+lineNumber+". \n";
					}
					i++;
				}
				return "[ floatnum , "+ word +" , "+ lineNumber + " ], ";
			}
		}
	}
	else {
		return "Lexical error: Invalid id: "+word+": line "+lineNumber+". \n";
	}
}
	
	
}
