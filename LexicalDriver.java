import java.io.*;
import java.util.Scanner;

public class LexicalDriver {
	public static int line=0;
	public static String errorFile,tokenFile;
public static void tokenAdd(String[] word) {
	try {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(tokenFile, true));
        out.write("["+word[0]+", "+word[1]+", "+word[2]+"], ");
        out.close();
    }
    catch (IOException e) {
        System.out.println("exception occurred" + e);
    }
}
public static void errorMsg(String[] word) {
	try {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(errorFile, true));
        out.write("["+word[0]+" : "+word[1]+" : Line Number :  "+word[2]+" ], ");
        out.close();
    }
    catch (IOException e) {
        System.out.println("exception occurred" + e);
    }
	
}
	public static String s = "";
	public static String[][] token = new String[10000][3];
	public static int k =0;
	
	public static String[][] lexical(String inp) {
		File f1 = new File(inp);
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
		while(s!="") {
			String[] str = Lex_Analyzer.nextToken(s,errorFile,tokenFile);
			if(str[0]!="" && str[2]!=null) {
			int x = Integer.parseInt(str[2]);
			while(x>(line+1)) {
				if(line==x-2) {
					try {
						BufferedWriter out = new BufferedWriter(
						      new FileWriter(tokenFile, true));
						out.write('\n');
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				line++;
			}
			if(str[0].startsWith("Lexical error")) {
				errorMsg(str);
			}
			else {
				token[k][0] = str[0];
				token[k][1] = str[1];
				token[k][2] = str[2];
				k++;
				tokenAdd(str);
			}
			}
			if(str[3]!=null)
				s = str[3].substring(1);
			else s="";
		}
		return token;
	}

}
