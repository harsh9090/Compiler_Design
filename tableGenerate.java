import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class tableGenerate {
	private static ArrayList<ArrayList<TreeNode>> ans = new ArrayList<>();
	private static ArrayList<TreeNode> temp = new ArrayList<>();
	public static void getDetails(TreeNode t) {
		if(t==null) return;
		else {
			
			for(int i=0;i<t.children.size();i++) {
				if(t.children.get(i).val.equals("id")) {
					if(i+1>=t.children.size()) {
						temp.add(new TreeNode("inherit",t.children.get(i).name,"inherit",t.children.get(i).vis));
					}
					else {
						temp.add(new TreeNode("data",t.children.get(i).name,t.children.get(i+1).name,t.children.get(i).vis));
					}
				}
				if(t.children.get(i).val.equals("function")) {
					temp.add(new TreeNode("function",t.children.get(i).children.get(0).name,"function",t.children.get(i).vis));
					int xb = temp.size();
					getDetails1(t.children.get(i));
					for(int k=xb;k<temp.size();k++) {
						temp.get(k).val = "param";
					}
					continue;
				}
				if(t.children.get(i).val.equals("constructor")) {
					
					temp.add(new TreeNode("constructor","build","constructor",t.children.get(i).vis));
					continue;
				}
				getDetails(t.children.get(i));
			}
		}
	}
	private static void getDetails1(TreeNode t) {
		if(t==null || t.children==null) return;
		else {
			for(int i=0;i<t.children.size();i++) {
				getDetails1(t.children.get(i));
				if(i+1<=t.children.size() && t.children.get(i).val.equals("id") && t.children.get(i+1).val.equals("type")) {
					t.children.get(i).type = t.children.get(i+1).name;
					temp.add(new TreeNode("local",t.children.get(i).name,t.children.get(i+1).name,t.children.get(i).vis));
				}
			}
		}
	}
	private static void getFunction(TreeNode t) {
		temp.add(new TreeNode("id",t.children.get(1).name,"parent",t.children.get(0).vis));
		if(!t.children.get(1).name.equals("main")) {
			if(t.children.get(2).val.equals("constructor")) {
				temp.add(new TreeNode("constructor","build","constructor",t.children.get(0).vis));
			}
			else {
				temp.add(new TreeNode("function",t.children.get(2).children.get(0).name,"function",t.children.get(0).vis));
			}
		}
		getDetails1(t);
	}
	private static void mainTable(TreeNode t1){
		for(int i=0;i<t1.children.size();i++) {
			if(t1.children.get(i).val.equals("ClassDec")) {
				temp.clear();
				getDetails(t1.children.get(i));
				temp.get(0).type="class";
				temp.get(0).val = "class";
				ans.add(new ArrayList<TreeNode>(temp));
			}
			if(t1.children.get(i).val.equals("FunctionDec")) {
				temp.clear();
				getFunction(t1.children.get(i));
				if(temp.get(0).name.equals("main")) {
					temp.get(0).type="Main";
				}
				if(temp.size()>1 && !temp.get(0).name.equals("main")) {
					temp.get(0).type="class";
					temp.add(1,temp.remove(0));
				}
				ans.add(new ArrayList<TreeNode>(temp));
			}
		}
	}
	private static boolean checkClass(TreeNode t,TreeNode t2) {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("class")) {
				if(ans.get(i).get(0).name.equals(t.name)) {
					for(int j=0;j<ans.get(i).size();j++) {
						if(ans.get(i).get(j).name.equals(t2.name)) {
							if(ans.get(i).get(j).type.equals(t2.type)) {
								mergeFunction(t,t2);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	private static void checkFunction() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("function") || ans.get(i).get(0).type.equals("constructor")) {
				if(!checkClass(ans.get(i).get(1),ans.get(i).get(0))) {
					System.out.println(ans.get(i).get(0).name + " is not available in " + ans.get(i).get(1).name + " class. Definition provided for undeclared member function");
				}
			}
		}
	}
	private static void checkClass() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("class")) {
				for(int j=0;j<ans.get(i).size();j++) {
					if(ans.get(i).get(j).type.equals("function") || ans.get(i).get(j).type.equals("constructor")) {
						if(!checkFunction(ans.get(i).get(0),ans.get(i).get(j))) {
							System.out.println(ans.get(i).get(j).name + " the class member of "+ans.get(i).get(0).name+" has no implemantation");
						};
					}
				}
			}
		}
	}
	private static boolean checkFunction(TreeNode t,TreeNode t2) {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("function") || ans.get(i).get(0).type.equals("constructor")) {
				if(ans.get(i).get(0).name.equals(t2.name)) {
					if(ans.get(i).get(1).name.equals(t.name)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private static boolean checkInherit(TreeNode t,TreeNode t1) {
		if(t==null || t.children==null) return true;
		if(t.name.equals(t1.name)) return false;
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).equals(t) && ans.get(i).get(1).type.equals("inherit")) {
				for(int j=0;j<ans.size();j++) {
					if(ans.get(j).get(0).type.equals("class") && ans.get(j).get(0).name.equals(ans.get(i).get(1).name)) {
						return checkInherit(ans.get(j).get(0),t1);
					}
				}
			}
		}
		return true;
	}
	private static void roundClass() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("class")) {
				if(ans.get(i).get(1).type.equals("inherit")) {
					for(int j=0;j<ans.size();j++) {
						if(ans.get(i).get(1).name.equals(ans.get(j).get(0).name) && ans.get(j).get(0).type.equals("class")) {
							if(!checkInherit(ans.get(j).get(0),ans.get(i).get(0))) {
								System.out.println("Cycle in class");
							}
						}
					}
				}
			}
		}
	}
	private static void mergeFunction(TreeNode t1, TreeNode t2) {
		int i=0,j=0;
		for(i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).name.equals(t1.name))
				break;
		}
		for(j=0;j<ans.size();j++) {
			if(ans.get(j).get(0).name.equals(t2.name))
				break;
		}
		for(int k=1;k<ans.get(i).size();k++) {
			for(int l=1;l<ans.get(j).size();l++) {
				if(ans.get(i).get(k).name.equals(ans.get(j).get(l).name)) {
					ans.get(j).get(l).val = ans.get(i).get(k).val;
					continue;
				}
			}
		}
		for(int k=1;k<ans.get(i).size();k++) {
			int x=0;
			if(!ans.get(i).get(k).val.equals("data"))
				continue;
			for(int l=1;l<ans.get(j).size();l++) {
				if(ans.get(i).get(k).name.equals(ans.get(j).get(l).name)) {
					x=1;
				}
			}
			if(x==0) {
				ans.get(j).add(ans.get(i).get(k));
			}
		}
	}
	private static void checkFunc() {
		for(int i=0;i<ans.size();i++) {
			for(int j=0;j<ans.size();j++) {
				
			}
		}
	}
	private static String[][] token;
	private static TreeNode root;
	public static void main(String[] args) {
		ArrayList<Object> list = Parserdriver.parseDriver();
		token = (String[][]) list.get(0);
		root = ((Stack<TreeNode>) list.get(1)).peek();
		mainTable(root);
		for(int i=0;i<ans.size();i++)
			System.out.println(ans.get(i));
		checkFunction();
		checkClass();
		roundClass();
		
		checkFunc();
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter("example-polynomial.outast"));
            out.write(TreeNode.printList(root));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
	}
}