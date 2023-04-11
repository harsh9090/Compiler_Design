import java.util.ArrayList;

class TreeNode{
	String vis= "public";
    String val;
    String type;
    String name;
    String lineNumber;
    int store;
    String funName="";
    boolean conTemp = false;
    ArrayList<Integer> dim=new ArrayList<>();
    TreeNode parent;
    ArrayList<TreeNode> children = new ArrayList<>();
    TreeNode(String data,String type){
        val = data;
        this.type = type;
    }
    TreeNode(TreeNode t){
    	val = t.val;
        this.name = t.name;
        this.type = t.type;
        this.vis = t.vis;
        this.lineNumber = t.lineNumber;
        this.funName = t.funName;
    }
    TreeNode(String data,String name,String type,String vis,String line){
        val = data;
        this.name = name;
        this.type = type;
        this.vis = vis;
        this.lineNumber = line;
    }
    
    TreeNode(String data,String name,String type,String vis,String line,String funName){
        val = data;
        this.name = name;
        this.type = type;
        this.vis = vis;
        this.lineNumber = line;
        this.funName = funName;
    }
    TreeNode(String data,String name,String type,String vis,String line,String funName,TreeNode t){
        val = data;
        this.name = name;
        this.type = type;
        this.vis = vis;
        this.lineNumber = line;
        this.funName = funName;
        this.parent =t;
    }
    TreeNode(String data,String name,String type){
        val = data;
        this.name = name;
        this.type = type;
    }
    TreeNode(String data,ArrayList<TreeNode> child,String type){
        val = data;
        children = child;
    }
    TreeNode(){
    	
    }
    void removeNode(TreeNode n1){
    	this.children.remove(n1);
    }

	@Override
	public String toString() {
		return "TreeNode [val=" + val + ", name=" + name + " ]";
	}
	public static String ast = "";
	public static int index =0;
	public static String printList(TreeNode root) {
		if(root==null) {
			return "";
		}
		else {
			if(!root.val.equals("EPS")) {
				for(int i=0;i<index;i++) {
					ast+= "| ";
				}
				ast+= root.val+", " + root.name +"\n";
//				ast+= root.val + root.name+"\n";
			}
			index++;
			for(int i=0;i<root.children.size();i++){
                printList(root.children.get(i));
            }
			index--;
            return ast;
		}
	}
}