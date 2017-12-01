import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class GeneralSearch {
	SearchProblem searchProblem;
	String searchType;
	Node node;
	int limit;

	public GeneralSearch(SearchProblem searchProblem, String searchType, Node node, int limit) {
		super();
		this.searchProblem = searchProblem;
		this.searchType = searchType;
		this.node = node;
		this.limit = limit;
	}
	
	/**
	 * General Search has the search algorithms implemented in.
	 * @param searchProblem: input from search in helpr2d2 indicating the search problem type
	 * @param searchType: input from search indicating which search algorithm
	 * @param limit: limit used in ID mainly
	 * @return a node that is the goal node
	 * Steps:
	 * 1) initializing the initialState and thus the initialNode
	 * 2) LinkedList of Nodes for the search algorithms to use
	 * 3) add the initialNode 
	 * 4) loop infinitely on 1) if nodes is empty then return null(no solution found), 2) remove
	 * the first node and check if it passes the goal test, if it does return it, otherwise expand it
	 * 5) the expanded node is put in an arraylist and the number of expanded nodes is incremented with
	 * its size. 
	 * 6) a switch is made on the different search algorithms
	 */

	public Node GenericSearch(SearchProblem searchProblem,String searchType, int limit){
		State initialState = searchProblem.initialState;
		Node initialNode = new Node((R2D2State) initialState, null,'\0', 0,0,0);
		LinkedList<Node> nodes = new LinkedList<Node>();
		nodes.add(initialNode);
		while(true){
			if(nodes.isEmpty())
				return null;
			else{
				Node n = nodes.removeFirst();
				if(searchProblem.goalTest(n))
					return n;
				ArrayList<Node> expandedNodes = searchProblem.Expand(n);
				((HelpR2D2) searchProblem).noOfExpandedNodes +=expandedNodes.size();
				switch(searchType){
				case"BFS" :
					while(!expandedNodes.isEmpty())
						nodes.addLast(expandedNodes.remove(0));
					break;
				case"DFS" :
					while(!expandedNodes.isEmpty()){
						nodes.addFirst(expandedNodes.remove(0));
					}
					break;

				case"ID":
					expandedNodes = searchProblem.Expand(n);
						while(!expandedNodes.isEmpty()){
							if(expandedNodes.get(0).getDepth()< limit)//if the depth is still less than the limit remove the node and add it to nodes, else just remove it
								nodes.addFirst(expandedNodes.remove(0));
							else expandedNodes.remove(0);
						}
					break;
				case"UC":
					while(!expandedNodes.isEmpty()){
						Node new1 =expandedNodes.remove(0);
						new1.eval = new1.pathCost;
						nodes.addFirst(new1);
					}
					Collections.sort(nodes);break;
				case"GR1": 
					while(!expandedNodes.isEmpty()){
						Node node = heuristic1(expandedNodes.remove(0),"GR1");
						nodes.add(node);	
					}
					Collections.sort(nodes);break;
				case"A*1"://h(n) + pathcost
					while(!expandedNodes.isEmpty()){
						Node node = heuristic1(expandedNodes.remove(0),"A*1");
						nodes.add(node);	
					}
					Collections.sort(nodes);break;
				case"GR2": 
					while(!expandedNodes.isEmpty()){
						Node node = heuristic2(expandedNodes.remove(0),"GR2");
						nodes.add(node);	
					}
					Collections.sort(nodes);break;
				case"A*2"://h(n) + pathcost
					while(!expandedNodes.isEmpty()){
						Node node = heuristic2(expandedNodes.remove(0),"A*2");
						nodes.add(node);	
					}
					Collections.sort(nodes);break;
				default:break;	
				}

			}
		}
	}
	
	/**
	 * Returns heuristic value of Node by calculating the distance 
	 * from the agent to the teleportal assuming no obstacles, pressure pads or rocks
	 * @param node to calculate heuristic of this node
	 * @param type Either it is Greedy Search Algorithm or A* Search Algorithm
	 * 
	 */

	public Node heuristic1(Node node, String type){ //Calculating distance from agent to teleportal assuming no obstacles, pressure pads or rocks
		int h=0;
		if(searchProblem.goalTest(node) == true)
			h=0;
		else{
			int agentRow = 0;
			int agentColumn = 0;
			int teleportalRow = 0;
			int teleportalColumn = 0;
			for(int iP=0;iP<((R2D2State)(node.getState())).getGrid().length;iP++){ //distance between agent and teleportal (assume en mafeesh rocks or pressure pads)
				for(int jP=0;jP<((R2D2State)(node.getState())).getGrid()[iP].length;jP++){
					if(((R2D2State)(node.getState())).getGrid()[iP][jP] == "Agent" || ((R2D2State)(node.getState())).getGrid()[iP][jP] == "AT" || ((R2D2State)(node.getState())).getGrid()[iP][jP] == "AP"){
						agentRow = iP;
						agentColumn = jP;
					}
					if(((R2D2State)(node.getState())).getGrid()[iP][jP] == "Teleportal"){
						teleportalRow = iP;
						teleportalColumn = jP;
					}
				}
			}
			h = Math.abs(teleportalRow-agentRow) + Math.abs(teleportalColumn - agentColumn);
		}
		if(type == "GR1") 
			node.eval=h;
		else if (type == "A*1")
			node.eval=h + node.pathCost;

		return node;
	}
	
	/**
	 * Returns heuristic value of Node by calculating the minimum 
	 * distance between Rocks and Pressure pads and then 1 is added for distance between Agent and Teleportal
	 * @param node to calculate heuristic of this node
	 * @param type Either it is Greedy Search Algorithm or A* Search Algorithm
	 * 
	 */
	
	public Node heuristic2(Node node, String type){ //Minimum distance between R and P is calculated then 1 added for distance between Agent and Teleportal
		int h=0;
		ArrayList<Location> rockLoc = new ArrayList<Location>();
		if(searchProblem.goalTest(node) == true){
			h=0;
		}
		else{
			int pressurePadRow = 0;
			int pressurePadColumn = 0;
			rockLoc = ((R2D2State)node.getState()).getRocksLocation(); //locations of rocks
			Location min = rockLoc.get(0);
			for(int iP=0;iP<((R2D2State)(node.getState())).getGrid().length;iP++){
				for(int jP=0;jP<((R2D2State)(node.getState())).getGrid()[iP].length;jP++){
					if(((R2D2State)(node.getState())).getGrid()[iP][jP] == "Pressure Pad"){ //get minimum distance of each rock with pressure pad
						pressurePadRow = iP;
						pressurePadColumn = jP;
						for (int counter = 0; counter < rockLoc.size(); counter++) { 		      
							if(Math.abs(pressurePadRow-min.row) + Math.abs(pressurePadColumn - min.column) > Math.abs(pressurePadRow-rockLoc.get(counter).row) + Math.abs(pressurePadColumn - rockLoc.get(counter).column) ){
								min=rockLoc.get(counter);
								rockLoc.remove(new Location (rockLoc.get(counter).row,rockLoc.get(counter).column));
							}
						}
						h+=Math.abs(pressurePadRow-min.row) + Math.abs(pressurePadColumn - min.column);
					}

				}
			}
			h++; //between agent to teleportal
		}
		if(type == "GR2") 
			node.eval=h;
		else if (type == "A*2")
			node.eval=h+node.pathCost;

		return node;

	}
}
