import java.io.*;
import java.util.Scanner;

public class Parserdriver {

	public static void main(String[] args) {
		System.out.println("Enter file name:");
		Scanner sc = new Scanner(System.in);
		String inp = sc.next();
		inp = inp.substring(0,inp.length()-4);
		try {
			 File my = new File(inp+".src");
			 if(!my.exists()) {
				 System.out.print("file not found");
				 return;
			 }
		} catch (Exception e) {
			System.out.print("file not found");
			return;
		}
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(inp+".outsyntaxerrors"));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(inp+".outderivation"));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(inp+".tokenization"));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		Parser.parser(inp);
	}

}
