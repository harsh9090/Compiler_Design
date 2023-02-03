import java.io.*;
import java.util.Scanner;

public class LexicalDriver {
	public static int line=0;
	public static String errorFile,tokenFile;
public static void tokenAdd(String word) {
	try {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(tokenFile, true));
        out.write(word);
        out.close();
    }
    catch (IOException e) {
        System.out.println("exception occurred" + e);
    }
}
//if token has error
public static void errorMsg(String word) {
	try {
        BufferedWriter out = new BufferedWriter(
            new FileWriter(errorFile, true));
        out.write(word);
        out.close();
    }
    catch (IOException e) {
        System.out.println("exception occurred" + e);
    }
}
	public static String s = "";
	public static void main(String[] args) {
		System.out.println("Enter file name:");
		Scanner sc = new Scanner(System.in);
		String inp = sc.next();
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
			if(str[0].length()>0) {
				String st2="";
				String[] st1 = str[0].split(",");
				if(st1.length>2) {
					st2 = st1[st1.length-2];
				int x =Integer.parseInt(st2.substring(1).split(" ")[0]);
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
				}
			}
			if(str[2].equals("e")) {
				errorMsg(str[0]);
			}
			else {
				tokenAdd(str[0]);
			}
			
			if(str[1].length()>0)
				s = str[1].substring(1);
			else s="";
		}
		System.out.print("end");
		sc.close();
	}

}
