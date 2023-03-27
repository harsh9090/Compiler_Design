import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
public class CompilerDriver {
	static TreeNode root = null;
	static ArrayList<TreeNode> symbolTable,tokens,mainTable = new ArrayList<>();
	static String code = "";
	static String decs = "";
	private static void traverseNodes(TreeNode root) {
		if(root ==null || root.children== null) return;
		if(root.val.equals("function") && root.name!=null && !root.name.equals("function") && root.name.equals("main")) {
			code="\tentry\n\taddi r14,r0,topaddr\n";
		}
		if(root.val.equals("localvar")) {
			tokens.add(new TreeNode(root));
			int space =0;
			if(root.children.get(0).type.equals("integer")) {
				space=4;
				if(root.children.get(0).dim>0) {
					for(int i=0;i<root.children.get(2).children.size();i++) {
						space*=Integer.parseInt(root.children.get(2).children.get(i).name);
					}
				}
			}
			decs += root.children.get(0).funName+root.children.get(0).name+" res "+ space +"\n";
			code+="\taddi r0,r1,"+root.children.get(0).funName+root.children.get(0).name+"\n";
			System.out.print(code);
		}
		for(int i=0;i<root.children.size();i++) {
			if(root.children.get(i).val.equals("=")) {
				
			}
			traverseNodes(root.children.get(i));
		}
	}
	public static void main(String[] args) {
		tokens=new ArrayList<>();
		ArrayList<Object> arr = tableGenerate.tableGenerator();
		String inp = (String) arr.get(3);
		root = (TreeNode) arr.get(1);
		symbolTable = (ArrayList<TreeNode>) arr.get(2);
		mainTable = (ArrayList<TreeNode>) arr.get(0);
		traverseNodes(root);
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
