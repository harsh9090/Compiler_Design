import java.util.ArrayList;

class TreeNode{
	String vis= "public";
    String val;
    String type;
    String name;
    ArrayList<TreeNode> children = new ArrayList<>();
    TreeNode(String data,String type){
        val = data;
        this.type = type;
    }
    TreeNode(String data,String name,String type,String vis){
        val = data;
        this.name = name;
        this.type = type;
        this.vis = vis;
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
		return "TreeNode [ type= "+ type +" , name = "+name+" ]";
//		return " [name = "+name +" ] ";
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
				ast+= root.val +"\n";
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