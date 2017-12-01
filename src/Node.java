
public class Node implements Comparable{

	State state;
	Node parent;
	char operator; //operator applied to generate this node (N,W,E,S)
	int depth; //of node in the tree
	int pathCost; // from the root
	int eval;//pathcost(UC) or heuristic(GR) or heuristic+pathcost(A*)
	public Node(State state, Node parent, char operator, int depth, int pathCost, int eval) {
		this.state = state;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
		this.eval =eval;
	} 
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public char getOperator() {
		return operator;
	}
	public void setOperator(char operator) {
		this.operator = operator;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getPathCost() {
		return pathCost;
	}
	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}
	public State getState() {
		return state;
	}
	public void setState(R2D2State state) {
		this.state = state;
	}


	
	@Override
	public int compareTo(Object o) { //compare with evals 
		if(this.eval == ((Node) o).eval)
			return 0;
		if(this.eval > ((Node) o).eval)
			return 1;
		else 
			return -1;
	}
	
	
}
