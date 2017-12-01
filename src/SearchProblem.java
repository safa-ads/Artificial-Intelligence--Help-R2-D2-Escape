import java.util.ArrayList;


public abstract class SearchProblem {
	//the 5-tuple searchProblem class
	State initialState;
	int pathCost; //each move costs one 
	char Operators; //N:North, S:South, W:West, E:East

	public SearchProblem(State initialState, int pathCost, char operators) {
		super();
		this.initialState = initialState;
		this.pathCost = pathCost;
		Operators = operators;
	}

	public abstract ArrayList<Node> Expand(Node n);
	
	public abstract int pathCost(Node n);
	
	public abstract Boolean goalTest(Node n);
}
