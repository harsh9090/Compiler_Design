import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class tableGenerate {
	private static ArrayList<ArrayList<TreeNode>> ans = new ArrayList<>();
	private static ArrayList<TreeNode> temp = new ArrayList<>();
	private static int count=1;
	private static String className = "";
	public static void getClassTable(TreeNode t) {
		if(t==null) return;
		else {
			for(int i=0;i<t.children.size();i++) {
				int skip=0;
				if(t.children.get(i).val.equals("isa")) {
					temp.add(new TreeNode("inherit",t.children.get(i).children.get(0).name,"inherit",t.children.get(i).vis,t.children.get(i).children.get(0).lineNumber));
					skip=1;
					}
				if(t.children.get(i).val.equals("id")) {
					temp.add(new TreeNode("data",t.children.get(i).name,t.children.get(i+1).name,t.children.get(i).vis,t.children.get(i).lineNumber));
				}
				if(t.children.get(i).val.equals("function")) {
					temp.add(new TreeNode("function",t.children.get(i).children.get(0).name,"function",t.children.get(i).vis,t.children.get(i).lineNumber));
					int xb = temp.size();
					getFunctionDetails(t.children.get(i));
					for(int k=xb;k<temp.size();k++) {
						temp.get(k).val = "param";
					}
					continue;
				}
				if(t.children.get(i).val.equals("constructor")) {
					temp.add(new TreeNode("constructor","build","constructor",t.children.get(i).vis,t.children.get(i).lineNumber));
					int xb = temp.size();
					getFunctionDetails(t.children.get(i));
					for(int k=xb;k<temp.size();k++) {
						temp.get(k).val = "param";
					}
					continue;
				}
				if(skip==0) getClassTable(t.children.get(i));
			}
		}
	}
	public static void getFunctionParameters(TreeNode t) {
		if(t==null || t.children==null) return;
		for(int i=0;i<t.children.size();i++) {
			getFunctionParameters(t.children.get(i));
			if(i<t.children.size() && t.children.get(i).val.equals("id") && t.children.get(i+1).val.equals("type")) {
				t.children.get(i).type = t.children.get(i+1).name;
				temp.add(new TreeNode("param",t.children.get(i).name,t.children.get(i+1).name,t.children.get(i).vis,t.children.get(i).lineNumber));
				if(t.children.get(i+2)!=null && t.children.get(i+2).val.equals("ARRSIZE")) {
					t.children.get(i+2).name = t.children.get(i).name;
					t.children.get(i).dim = t.children.get(i+2).children.size();
					temp.get(temp.size()-1).dim = t.children.get(i+2).children.size();
				}
			}
			if(t.children.get(i).val.equals("return")) {
				
				temp.add(new TreeNode("return","return",t.children.get(i).name,t.children.get(i).vis,t.children.get(i).lineNumber));
			}
		}
	}
	private static void getFunctionDetails(TreeNode t) {
		changeType(root);
		if(t==null || t.children==null) return;
		else {
			for(int i=0;i<t.children.size();i++) {
				getFunctionDetails(t.children.get(i));
				if(t.children.get(i).val.equals("return")) {
					
					temp.add(new TreeNode("return","return",t.children.get(i).name,t.children.get(i).vis,t.children.get(i).lineNumber));
				}
				else if(t.children.get(i).val.equals("returnFromFunction")) {
					for(int k=0;k<temp.size();k++) {
						if(temp.get(k).name.equals("return")) {
							if(!temp.get(k).type.equals(t.children.get(i).type)) {
								String er= temp.get(k).type + " and " +t.children.get(i).type +" " + "Return type doesn't match! on line - " + t.children.get(i).lineNumber +"\n";
								System.out.print(temp.get(k).type + " and " +t.children.get(i).type +" ");
								System.out.println("Return type doesn't match! on line - " + t.children.get(i).lineNumber);
								try {
						            BufferedWriter out = new BufferedWriter(
						                new FileWriter(error,true));
						            out.write(er);
						            out.close();
						        }
						        catch (IOException e) {
						            System.out.println("exception occurred" + e);
						        }
								
							}
						}
					}
				}
				else if(i<t.children.size() && t.children.get(i).val.equals("id") && t.children.get(i+1).val.equals("type")) {
					if(!(t.children.get(i+1).name.equals("float") || t.children.get(i+1).name.equals("integer"))) {
						checkConstructor(t.children.get(i+2),t.children.get(i+1));
						if(!className.contains(t.children.get(i+1).name)) {
							System.out.println("Mentioned class not available! line Number : "+ t.children.get(i+1).lineNumber);
						}
					}
					t.children.get(i).type = t.children.get(i+1).name;
					temp.add(new TreeNode("local",t.children.get(i).name,t.children.get(i+1).name,t.children.get(i).vis,t.children.get(i).lineNumber));
					if(t.children.get(i+2)!=null && t.children.get(i+2).val.equals("ARRSIZE")) {
						t.children.get(i+2).name = t.children.get(i).name;
						t.children.get(i).dim = t.children.get(i+2).children.size();
						temp.get(temp.size()-1).dim = t.children.get(i+2).children.size();
					}
				}
				else if(t.children.get(i).val.equals("id") && t.children.get(i).type.equals("id")) {
					ArrayList<TreeNode> ar = new ArrayList<>();
					if(t.children.get(i).name.equals("self")) {
						for(int j=0;j<ans.size();j++) {
							if(ans.get(j).get(0).name.equals(temp.get(1).name)) {
								ar = ans.get(j);
							}
						}
						for(int j=0;j<ar.size();j++) {
							if(t.children.get(i+1).children.size()>0 && ar.get(j).name.equals(t.children.get(i+1).children.get(0).name) && ar.get(j).val.equals("data")) {
								t.children.get(i+1).children.get(0).type = ar.get(j).type;
							}
						}
						
					}
					String[] fNam = fNames.split(" ");
					for(int a=0;a<fNam.length;a++) {
						if(t.children.get(i).name!=null && fNam[a].equals(t.children.get(i).name)) {
							t.children.get(i).type=t.children.get(i).name;
							continue;
						}
					}
					for(int j=0;j<temp.size();j++) {
						if(temp.get(j).name.equals(t.children.get(i).name)) {
							t.children.get(i).type = temp.get(j).type;
							
						}
					}
				}
			}
		}
	}
	private static void checkConstructor(TreeNode t,TreeNode classNam) {
		ArrayList<String> typeList = new ArrayList<>();
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).name.equals(classNam.name)) {
				for(int j=0;j<ans.get(i).size();j++) {
					if(ans.get(i).get(j).type.equals("constructor")) {
						j++;
						while(ans.get(i).get(j).val.equals("param")) {
							typeList.add(ans.get(i).get(j).type);
							j++;
						}
					}
				}
			}
		}
		int ak=0;
		if(t.children.size()!= typeList.size()) {
			System.out.println("please enter correct parameters to pass in constructor. Line number -" + t.lineNumber);
			return;
		}
		for(int i=0;i<t.children.size();i++) {
			if(i>typeList.size()) {
				System.out.println("too many members passing to constructor. Line number -" + t.children.get(0).lineNumber);
				return;
			}
			if(!typeList.get(ak).equals(t.children.get(i).type)) {
				System.out.println("Type is not matching with constructor values Line number -" + t.children.get(0).lineNumber);
				return;
			}
			ak++;
		}
	}
	private static void getFunctionTable(TreeNode t) {
		if(t.children.get(2).val.equals("FunctionMemberTail")) {
//			t.children.get(2).children.get(0).type = "funtion";
			t.children.get(1).val="class";
//			t.children.get(2).children.get(0).val = "funtion";
			t.children.get(1).type="class";
			temp.add(new TreeNode("function",t.children.get(2).children.get(0).name,"function",t.children.get(2).children.get(0).vis,t.children.get(2).children.get(0).lineNumber));
			temp.add(new TreeNode("class",t.children.get(1).name,"class",t.children.get(0).vis,t.children.get(1).lineNumber));
			getFunctionParameters(t.children.get(2));
		}
		else if(t.children.get(2).val.equals("FunctionTail")) {
			t.children.get(1).type = "function";
			t.children.get(1).val = "function";
			temp.add(new TreeNode("function",t.children.get(1).name,"function",t.children.get(1).vis,t.children.get(1).lineNumber));
			getFunctionParameters(t.children.get(2));
		}
		else if(t.children.get(2).val.equals("constructor")) {
			
			temp.add(new TreeNode("constructor","build","constructor",t.children.get(2).vis,t.children.get(2).lineNumber));
			temp.add(new TreeNode("class",t.children.get(1).name,"class",t.children.get(0).vis,t.children.get(1).lineNumber));
			getFunctionParameters(t.children.get(2));
		}
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).val.equals("Body")) {
				getFunctionDetails(t.children.get(i));
			}
		}
		
	}
	private static String fNames = "";
	private static void mainTable(TreeNode t1){
		for(int i=0;i<t1.children.size();i++) {
			if(t1.children.get(i).val.equals("ClassDec")) {
				temp.clear();
				getClassTable(t1.children.get(i));
				temp.get(0).type="class";
				temp.get(0).val = "class";
				className += temp.get(0).name+ " ";
				ans.add(new ArrayList<TreeNode>(temp));
			}
			if(t1.children.get(i).val.equals("FunctionDec")) {
				temp.clear();
				getFunctionTable(t1.children.get(i));
				fNames += temp.get(0).name + " ";
				ans.add(new ArrayList<TreeNode>(temp));
			}
		}
	}
	
	
	private static void checkClassMembers() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("class")) {
				for(int j=0;j<ans.get(i).size();j++) {
					if(ans.get(i).get(j).type.equals("function") || ans.get(i).get(j).type.equals("constructor")) {
						if(!checkFunctionInClass(ans.get(i).get(0),ans.get(i).get(j))) {
							String er = ans.get(i).get(j).name + " - the class member of "+ans.get(i).get(0).name+" has no implemantation\n";
							try {
					            BufferedWriter out = new BufferedWriter(
					                new FileWriter(error,true));
					            out.write(er);
					            out.close();
					        }
					        catch (IOException e) {
					            System.out.println("exception occurred" + e);
					        }
							System.out.println(ans.get(i).get(j).name + " - the class member of "+ans.get(i).get(0).name+" has no implemantation");
						};
					}
				}
			}
		}
	}
	private static boolean checkFunctionInClass(TreeNode t,TreeNode t2) {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("function") || ans.get(i).get(0).type.equals("constructor")) {
				if(ans.get(i).get(0).name!=null && ans.get(i).get(0).name.equals(t2.name)) {
					if(ans.get(i).get(1).name.equals(t.name) && ans.get(i).get(1).type.equals("class")) {
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
		boolean x = false;
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).size() > 1 && ans.get(i).get(0).equals(t) && ans.get(i).get(1).type.equals("inherit")) {
				for(int j=0;j<ans.size();j++) {
					if(ans.get(j).get(0).type.equals("class") && ans.get(j).get(0).name.equals(ans.get(i).get(1).name)) {
						x = checkInherit(ans.get(j).get(0),t1);
						for(int k=0;k<ans.get(j).size();k++) {
							if(ans.get(j).get(k).val.equals("data")) {
								if(ans.get(i).contains(ans.get(j).get(k))) continue;
								ans.get(i).add(ans.get(j).get(k));
							}
						}
						return x;
					}
				}
			}
		}
		return true;
	}
	private static void roundClass() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("class")) {
				if(ans.get(i).size()>1 && ans.get(i).get(1).type.equals("inherit")) {
					for(int j=0;j<ans.size();j++) {
						if(ans.get(i).get(1).name.equals(ans.get(j).get(0).name) && ans.get(j).get(0).type.equals("class")) {
							for(int k=0;k<ans.get(j).size();k++) {
								if(ans.get(j).get(k).val.equals("data")) {
									int same =0;
									for(int l=0;l<ans.get(i).size();l++) {
										if(ans.get(i).get(l).val.equals("data") && ans.get(i).get(l).name.equals(ans.get(j).get(k).name)) {
											same=1;
											break;
										}
									}
									if(same==0) ans.get(i).add(ans.get(j).get(k));
								}
							}
							if(!checkInherit(ans.get(j).get(0),ans.get(i).get(0))) {
								String er = "Cycle in class\n";
								try {
						            BufferedWriter out = new BufferedWriter(
						                new FileWriter(error,true));
						            out.write(er);
						            out.close();
						        }
						        catch (IOException e) {
						            System.out.println("exception occurred" + e);
						        }
								System.out.println("Cycle in class");
							}
						}
					}
				}
			}
		}
	}
		private static void checkSameNameClass() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("class")) {
				for(int j=i+1;j<ans.size();j++) {
					if(ans.get(j).get(0).name!=null && ans.get(j).get(0).name.equals(ans.get(i).get(0).name)) {
						if(ans.get(i).get(0).type.equals("class")) {
							String er = "Same Name of class\n";
							try {
					            BufferedWriter out = new BufferedWriter(
					                new FileWriter(error,true));
					            out.write(er);
					            out.close();
					        }
					        catch (IOException e) {
					            System.out.println("exception occurred" + e);
					        }
							System.out.println("Same Name of class. Name :" +ans.get(i).get(0).name);
						}
					}
				}
			}
		}
	}
		private static void mergeFunction(TreeNode t1, TreeNode t2) {
			int i=0,j=0;
			for(i=0;i<ans.size();i++) {
				if(ans.get(i).get(0).name.equals(t1.name) && ans.get(i).get(0).val.equals("class")) {
					break;
				}
					
			}
			for(j=0;j<ans.size();j++) {
				if(ans.get(j).get(0).name.equals(t2.name) && ans.get(j).get(1).name.equals(t1.name)) {
					break;
				}
			}
			for(int k=1;k<ans.get(i).size();k++) {
				int x=0;
				if(!ans.get(i).get(k).val.equals("data")) continue;
				for(int l=1;l<ans.get(j).size();l++) {
					if(ans.get(i).get(k).name.equals(ans.get(j).get(l).name) && (ans.get(j).get(l).val.equals("data"))) {
						x=1;
					}
						
				}
				if(x==0)
					ans.get(j).add(ans.get(i).get(k));
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
	private static ArrayList<TreeNode> params;
	private static int paramCount=0;
	private static void checkParams(TreeNode t) {
		if(t==null) return;
		if(t.val.equals("id")) {
			if(paramCount<params.size() && !t.type.equals(params.get(paramCount).type)) {
				System.out.println("param type is not matching ! on line - " + t.lineNumber + "\n");
				String er = "param type is not matching ! on line - " + t.lineNumber + "\n";
				try {
		            BufferedWriter out = new BufferedWriter(
		                new FileWriter(error,true));
		            out.write(er);
		            out.close();
		        }
		        catch (IOException e) {
		            System.out.println("exception occurred" + e);
		        }
			}
			else {
				t.val="param";
			}
			paramCount++;
		}
		for(int i=0;i<t.children.size();i++)
			checkParams(t.children.get(i));
	}
	private static void operational(TreeNode t,int x) {
		if(t==null || t.children==null) return;
		for(int i=0;i<t.children.size();i++) {
			
			if(t.children.get(i).val.equals("dot")) {
				String fName = t.children.get(i).children.get(0).name;
				for(int j=0;j<ans.size();j++) {
					if(ans.get(j).get(0).name!=null && ans.get(j).get(0).name.equals(fName) && ans.get(j).get(1).name.equals(ans.get(x).get(0).name)) {
						t.children.get(i).children.get(0).val = "function";
						for(int k=0;k<ans.get(j).size();k++) {
							if(ans.get(j).get(k).val.equals("param")) {
								params.add(ans.get(j).get(k));
							}
						}
						checkParams(t.children.get(i).children.get(1));
					}
				}
			}
			operational(t.children.get(i),x);
		}
	}
	private static void validateMember(TreeNode t,TreeNode parent,int j) {
		if(t == null) return;
		if(t.val.equals("id") && !t.name.equals("self")) {
			if(!className.contains(t.type) && !fNames.contains(t.name)) {
				int check=0;
				for(int i=0;i<ans.get(j).size();i++) {
					if(ans.get(j).get(i).name!=null && ans.get(j).get(i).name.equals(t.name)) {
						check++;
						break;
					}
				}
				if(check==0) {
					String er = "variable "+t.name+" not defined!" + " Line Number := " + t.lineNumber +"\n";
					try {
			            BufferedWriter out = new BufferedWriter(
			                new FileWriter(error,true));
			            out.write(er);
			            out.close();
			        }
			        catch (IOException e) {
			            System.out.println("exception occurred" + e);
			        }
					System.out.println("variable "+t.name+" not defined!" + " Line Number := " + t.lineNumber);
				}
			}
			else {
				int i=0;
				for(i=0;i<ans.size();i++) {
					if(ans.get(i).get(0).name.equals(parent.children.get(0).type)) {
						params=new ArrayList<>();
						operational(parent,i);
						if(parent.children.get(2).children.get(0).children.size()>1) {
							if(parent.children.get(2).children.get(0).children.get(1).children.size()!=params.size()) {
								System.out.println("enter correct parameters. Line Number -" +parent.children.get(2).children.get(0).children.get(1).lineNumber );
							}
						}
							
						break;
					}
				}
				
			}
		}
		if(t.name!=null && t.name.equals("self")) {
			int coun=0;
			for(int i=0;i<parent.children.size();i++) {
				if(parent.children.get(i).val.equals("dot")) {
					coun=1;
				}
			}
			if(coun==0) {
				String er = "self is not correctly written. Line - " +t.lineNumber;
				try {
		            BufferedWriter out = new BufferedWriter(
		                new FileWriter(error,true));
		            out.write(er);
		            out.close();
		        }
		        catch (IOException e) {
		            System.out.println("exception occurred" + e);
		        }
				System.out.println("self is not correctly written. Line - " +t.lineNumber);
			}
		}
		for(int i=0;i<t.children.size();i++)
			validateMember(t.children.get(i),t,j);
	}
	private static void validateFunction(TreeNode t) {
		int j=0;
		if(t == null || t.children ==null) return;
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).val.equals("FunctionDec")) {
				int cnt =0;
				for(j=0;j<ans.size();j++) {
					if((ans.get(j).get(0).val.equals("function") || ans.get(j).get(0).val.equals("constructor"))) {
						cnt++;
						if(cnt==count) {
							count++; 
							break;
						}
					}
				}
				validateMember(t.children.get(i),t,j);
			}
		}
	}
	private static void checkFunctionFromClass() {
		for(int i=0;i<ans.size();i++) {
			if(ans.get(i).get(0).type.equals("function") || ans.get(i).get(0).type.equals("constructor")) {
				if(ans.get(i).size()>1 && ans.get(i).get(1).type.equals("class") && !checkClass(ans.get(i).get(1),ans.get(i).get(0))) {
					String er="";
					if(ans.get(i).get(0).name!=null) {
						er = ans.get(i).get(0).name + " is not available in " + ans.get(i).get(1).name + " class. Definition provided for undeclared member function.\n";
						System.out.println(ans.get(i).get(0).name + " is not available in " + ans.get(i).get(1).name + " class. Definition provided for undeclared member function");
					}
					else {
						er=ans.get(i).get(1).name + " is not available. Definition provided for undeclared member function";
						System.out.println(ans.get(i).get(1).name + " is not available. Definition provided for undeclared member function");
					}
					try {
			            BufferedWriter out = new BufferedWriter(
			                new FileWriter(error,true));
			            out.write(er);
			            out.close();
			        }
			        catch (IOException e) {
			            System.out.println("exception occurred" + e);
			        }
				}
			}
		}
	}
	private static void changeType(TreeNode t) {
		if(t== null || t.children==null) return;
		for(int i=0;i<t.children.size();i++) {
			if(!t.children.get(i).val.equals("ARRSIZE") || t.children.get(i).val.equals("IndiceList")) {
				changeType(t.children.get(i));
				giveNode(t);
			}
		}
	}
	
	private static void giveNode(TreeNode t) {
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).val.equals("ARRSIZE") || t.children.get(i).val.equals("IndiceList")) {
				t.children.get(i).type = "ARRSIZE";
				
				continue;
			}
			if(t.children.get(i).type.equals("integer") || t.children.get(i).type.equals("float")) {
				t.type = t.children.get(i).type;
			}
		}
	}
	private static int arrcount=0;
	private static void checkParam(TreeNode t) {
		if(t==null || t.children==null) return;
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).type.equals("float")) {
				String er = "Float type for dimension is incorrect. line -" + t.children.get(i).lineNumber;
				try {
		            BufferedWriter out = new BufferedWriter(
		                new FileWriter(error,true));
		            out.write(er);
		            out.close();
		        }
		        catch (IOException e) {
		            System.out.println("exception occurred" + e);
		        }
				System.out.println("Float type for dimension is incorrect. line -" + t.children.get(i).lineNumber);
				continue;
			}
			if(t.children.get(i).type.equals("integer")) {
				arrcount++;
				continue;
			}
			checkParam(t.children.get(i));
			
		}
	}
	private static String idName = "";
	private static boolean checkValues(TreeNode t,int num) {
		if(t==null) return true;
		if(t.val.equals("IndiceList") || t.val.equals("ARRSIZE")) {
			arrcount=0;
			checkParam(t);
			for(int j=0;j<ans.get(num).size();j++) {
			}
			return true;
		}
		if(t.val.equals("id")) {
			idName = t.name;
		}
		for(int i=0;i<t.children.size();i++) {
			if(t.children.get(i).val.equals("IndiceList") && t.children.get(i).children.size()!=0) {
				arrcount=0;
				checkParam(t.children.get(i));
				for(int j=0;j<ans.get(num).size();j++) {
					if(ans.get(num).get(j).name.equals(idName)) {
						if(ans.get(num).get(j).dim!=arrcount) {
							String er = "Enter correct Dimensions. Line Number- " + t.lineNumber+"\n";
							try {
					            BufferedWriter out = new BufferedWriter(
					                new FileWriter(error,true));
					            out.write(er);
					            out.close();
					        }
					        catch (IOException e) {
					            System.out.println("exception occurred" + e);
					        }
							System.out.println("Please Enter correct Dimensions. Line Number- " + t.lineNumber);
						}
					}
				}
				return true;
			}
			if((t.children.get(i).type.equals("integer") || t.children.get(i).type.equals("float"))) {
				if(!t.children.get(i).type.equals(t.type)) {
					String er = "type doesn't match on line - " + t.children.get(i).lineNumber + ". " +t.children.get(i).name + " and " + t.type +"\n";
					try {
			            BufferedWriter out = new BufferedWriter(
			                new FileWriter(error,true));
			            out.write(er);
			            out.close();
			        }
			        catch (IOException e) {
			            System.out.println("exception occurred" + e);
			        }
					System.out.println("type doesn't match on line - " + t.children.get(i).lineNumber);
					System.out.println(t.children.get(i).type + " and " + t.type);
					return false;
				}
					
			}
			if(!checkValues(t.children.get(i),num)) return false;
		}
		return true;
	}
	private static void checkNodeByPart(TreeNode t) {
		int j=0;
		for(int i=0;i<t.children.size();i++) {
			checkNode(t.children.get(i),j);
			j++;
		}
	}
	private static void checkNode(TreeNode t,int ax) {
		if(t==null) return;
		if(t.val.equals("Statment")){
			checkValues(t,ax);
		}
		else if(t.val.equals("while") || t.val.equals("if")) {
			checkValues(t.children.get(0),ax);
		}
		for(int i=0;i<t.children.size();i++)
			checkNode(t.children.get(i),ax);
	}
	private static void createTable(ArrayList<TreeNode> arr,String table) {
		String members = "------------------------------------------------------------------------------\n";
		for(int i=0;i<arr.size();i++) {
			if(i==0 && arr.get(i).name!=null )
				members += "| "+ arr.get(i).type +" " +arr.get(i).name.toUpperCase() + "\n";
			else members+= "| "+ arr.get(i) + "\n";
		}
		members += "------------------------------------------------------------------------------\n\n";
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(table,true));
            out.write(members);
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
	}
	private static TreeNode root;
	private static String inps,error;
	
	private static void duplicateFunction() {
		for(int i=0;i<ans.size();i++) {
			for(int j=i+1;j<ans.size();j++) {
				if(ans.get(i).get(0).name.equals(ans.get(j).get(0).name) && ans.get(i).get(1).name.equals(ans.get(j).get(1).name) && ans.get(i).get(0).type.equals("function")) {
					int parm=0;
					for(int k=0;k<ans.get(i).size();k++) {
						if(ans.get(i).get(k).val.equals("param")) parm++;
					}
					
					for(int k=0;k<ans.get(j).size();k++) {
						if(ans.get(j).get(k).val.equals("param")) parm--;
					}
					if(parm==0) {
						String er = "same functions. Name :" +ans.get(j).get(0).name + "\n";
						try {
				            BufferedWriter out = new BufferedWriter(
				                new FileWriter(error,true));
				            out.write(er);
				            out.close();
				        }
				        catch (IOException e) {
				            System.out.println("exception occurred" + e);
				        }
						System.out.println("same functions. Name :" +ans.get(j).get(0).name);
					}
					else {
						System.out.println("WARNING: same function name but different parameters. Name :" +ans.get(j).get(0).name);
					}
				}
			}
		}
	}
	public static void editMainFunction() {
		if(!fNames.contains("main")) {
			System.out.println("Main function not available!");
		}
		else {
			String[] classList = className.split(" ");
			String[] funcList = fNames.split(" ");
			for(int i=0;i<ans.size();i++) {
				if(ans.get(i).get(0).type.equals("function") && ans.get(i).get(0).name.equals("main")) {
					for(int j=0;j<funcList.length;j++) {
						if(funcList[j].equals("")) continue;
						ans.get(i).add(new TreeNode("function",funcList[j],"function"));
					}
					for(int j=0;j<classList.length;j++) {
						if(classList[j].equals("")) continue;
						ans.get(i).add(new TreeNode("class",classList[j],"class"));
					}
				}
			}
		}
	}
	public static void main(String[] args) {
		ArrayList<Object> list = Parserdriver.parseDriver();
		root = ((Stack<TreeNode>) list.get(1)).peek();
		inps = ((String) list.get(2));
		String table = inps+ ".outsymboltables";
		error = inps + ".outsemanticerrors";
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(error));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		mainTable(root);
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(table));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		temp.clear();
		temp.add(new TreeNode("MAIN TABLE","MAIN TABLE","MAIN TABLE"));
		for(int i=0;i<ans.size();i++) {
			temp.add(ans.get(i).get(0));
		}
		createTable(temp,table);
		temp.clear();
		for(int i=0;i<ans.size();i++) {
			createTable(ans.get(i),table);
		}
		roundClass();
		checkClassMembers();
		checkSameNameClass();
		checkFunctionFromClass();
		editMainFunction();
		
		validateFunction(root);
		try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(inps+".outast"));
            out.write(TreeNode.printList(root));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
		checkNodeByPart(root);
		duplicateFunction();
		checkMembers();
	}
	private static void checkMembers() {
		for(int i=0;i<ans.size();i++) {
			for(int j=0;j<ans.get(i).size();j++) {
				for(int k=j+1;k<ans.get(i).size();k++) {
					if(ans.get(i).get(j).name.equals(ans.get(i).get(k).name)) {
						if(ans.get(i).get(j).val.equals(ans.get(i).get(k).val) && (ans.get(i).get(k).val.equals("params")|| ans.get(i).get(k).val.equals("data") || ans.get(i).get(k).val.equals("local") ) ) {
							System.out.println("duplicate members. Line number -" +ans.get(i).get(k).lineNumber );
						}
					}
				}
			}
		}
	}
}
