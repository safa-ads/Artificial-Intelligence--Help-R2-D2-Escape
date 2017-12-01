import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class HelpR2D2 extends SearchProblem{

	private static final int MAX_CELLS = 2;
	static Random random = new Random();
	static int rocksNumber; //pressure pad number = rocks number
	static int obstaclesNumber;
	R2D2State initialState1; 
	static int m =3;//random.nextInt(MAX_CELLS)+3; //rows
	static int n =3;//random.nextInt(MAX_CELLS)+3; //columns
	static int noOfExpandedNodes=0;
	public HelpR2D2(R2D2State initialState, int pathCost, char operators) {
		super(initialState, pathCost, operators);
	}

	public R2D2State getInitialState1() {
		return initialState1;
	}

	public void setInitialState1(R2D2State initialState1) {
		this.initialState1 = initialState1;
	}
	
	
	/**
	 * Expand: We expand in NESW order, where we move the Agent accordingly. The node that is inputed is the one expanded.
	 */

	public ArrayList<Node> Expand(Node node) {
		ArrayList<Node> possibleNodes = new ArrayList<Node>();
		int prevDepth = node.getDepth();
		int pathCost = node.getPathCost();
		
		//4 grids are made of the original one to avoid referencing problem
		String[][] grid = ((R2D2State)node.getState()).getGrid();
		String[][] newGN = new String [m][n];
		String[][] newGE = new String [m][n];
		String[][] newGW = new String [m][n];
		String[][] newGS = new String [m][n];
		for(int i =0;i<grid.length;i++){
			for(int j=0; j<grid[i].length;j++){
				newGN[i][j]=grid[i][j];
			}
		}

		for(int i =0;i<grid.length;i++){
			for(int j=0; j<grid[i].length;j++){
				newGE[i][j]=grid[i][j];
			}
		}
		for(int i =0;i<grid.length;i++){
			for(int j=0; j<grid[i].length;j++){
				newGW[i][j]=grid[i][j];
			}
		}

		for(int i =0;i<grid.length;i++){
			for(int j=0; j<grid[i].length;j++){
				newGS[i][j]=grid[i][j];
			}
		}
		String[][] gridN = newGN;
		String[][] gridS = newGS;
		String[][] gridE = newGE;
		String[][] gridW = newGW;
		
		//Check the position of Agent where it could be Agent or AP or AT
		int agentRow =0;
		int agentColumn =0;
		for(int i =0;i<grid.length;i++){
			for(int j=0; j<grid[i].length;j++){
				if(grid[i][j] == "Agent" || grid[i][j] == "AP" || grid[i][j] == "AT"  ){   //It can either be agent or agent with pressure pad
					agentRow=i;
					agentColumn=j;
				}
			}
		}

		/**
		 * A documentation of how the movements were implemented is in North Expansion, the rest will
		 * therefore follow.
		 */
		
////////////North Expansion//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Check that the agent is not leaving the grid and that it's not moving north(row-1) on an Obstacle
		if((0<=agentRow-1 && agentRow-1<m && gridN[agentRow-1][agentColumn]!="Obstacle")){
			//Check if it's an Agent or AP(Agent on Pressure Pad) or AT(Agent on Teleportal)
			if(gridN[agentRow][agentColumn] == "Agent" ||gridN[agentRow][agentColumn]  == "AP" || gridN[agentRow][agentColumn]  == "AT"){
				/**
				 * Case1: If it is moving towards a null cell then update the cell to Agent and the previous cell 
				 * the Agent was on to null or Pressure pad or Teleportal depending on whether it was an
				 * Agent or AP or AT. Then we create a new state and node and add it to possibleNodes arraylist
				 */
				if(gridN[agentRow-1][agentColumn] == "null"){
					gridN[agentRow-1][agentColumn] = "Agent";
					if(gridN[agentRow][agentColumn] == "Agent")
						gridN[agentRow][agentColumn] = "null";						
					else if(gridN[agentRow][agentColumn] == "AP")
						gridN[agentRow][agentColumn] = "Pressure pad";
					else if(gridN[agentRow][agentColumn] == "AT")								
						gridN[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateN = new R2D2State(gridN);
					Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeN);
				}
				/**
				 * Case2: If it is moving towards a Rock then check if it pushed the rock will it leave the grid
				 * by agentRow-2 checks or that another rock/Obstacle/Teleportal/PR is north to the Rock 
				 * I am attempting to push because then it can NOT be pushed. It can pe pushed it the cell North 
				 * to the Rock is Pressure pad or null.
				 */
				else if(gridN[agentRow-1][agentColumn] == "Rock")
				{
					if((0<=agentRow-2 && agentRow-2<m) && gridN[agentRow-2][agentColumn]!="Rock" && gridN[agentRow-2][agentColumn]!="Teleportal" && gridN[agentRow-2][agentColumn]!="Obstacle" && gridN[agentRow-2][agentColumn]!="PR"){
						if(gridN[agentRow-2][agentColumn] == "Pressure pad"){ //case pressure pad north to Rock
							gridN[agentRow-2][agentColumn] = "PR"; //Pressure pad becomes PR
							gridN[agentRow-1][agentColumn] = "Agent"; //Agent moves north at Rock's place

							//Previous cell is updated because Agent moved
							if(gridN[agentRow][agentColumn] == "Agent")
								gridN[agentRow][agentColumn] = "null";						

							else if(gridN[agentRow][agentColumn] == "AP")
								gridN[agentRow][agentColumn] = "Pressure pad";

							else if(gridN[agentRow][agentColumn] == "AT")								
								gridN[agentRow][agentColumn] = "Teleportal";
							
							//New node is made of the state of the grid and added to possibleNodes
							R2D2State r2d2stateN = new R2D2State(gridN);
							Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeN);
						}else{
							//case null north to Rock
							gridN[agentRow-1][agentColumn] = "Agent";
							gridN[agentRow-2][agentColumn] = "Rock";

							//Previous cell is updated because Agent moved
							if(gridN[agentRow][agentColumn] == "Agent")
								gridN[agentRow][agentColumn] = "null";						

							else if(gridN[agentRow][agentColumn] == "AP")
								gridN[agentRow][agentColumn] = "Pressure pad";

							else if(gridN[agentRow][agentColumn] == "AT")								
								gridN[agentRow][agentColumn] = "Teleportal";
							//New node is made of the state of the grid and added to possibleNodes
							R2D2State r2d2stateN = new R2D2State(gridN);
							Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeN);
						}
					}

				}
				/**
				 * Case3: If it is moving towards a Pressure pad then Agent moves on pressure pad to become
				 * AP 
				 */
				else if(gridN[agentRow-1][agentColumn] == "Pressure pad")
				{
					gridN[agentRow-1][agentColumn]="AP";  //Agent and pressure pad on the same cell
					
					//Previous cell is updated because Agent moved
					if(gridN[agentRow][agentColumn] == "Agent")
						gridN[agentRow][agentColumn] = "null";						

					else if(gridN[agentRow][agentColumn] == "AP")
						gridN[agentRow][agentColumn] = "Pressure pad";

					else if(gridN[agentRow][agentColumn] == "AT")								
						gridN[agentRow][agentColumn] = "Teleportal";
					
					//New node is made of the state of the grid and added to possibleNodes
					R2D2State r2d2stateN = new R2D2State(gridN);
					Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeN);			
				}
				/**
				 * Case4: If it is moving towards a Teleportal then Agent moves on Teleportal to become
				 * AT.
				 */
				else if(gridN[agentRow-1][agentColumn] == "Teleportal"){
					gridN[agentRow-1][agentColumn] ="AT";
					//Previous cell is updated because Agent moved
					if(gridN[agentRow][agentColumn] == "Agent")
						gridN[agentRow][agentColumn] = "null";						

					else if(gridN[agentRow][agentColumn] == "AP")
						gridN[agentRow][agentColumn] = "Pressure pad";

					else if(gridN[agentRow][agentColumn] == "AT")								
						gridN[agentRow][agentColumn] = "Teleportal";
					
					//New node is made of the state of the grid and added to possibleNodes
					R2D2State r2d2stateN = new R2D2State(gridN);
					Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeN);	

				}
				/**
				 * Case5: If it is moving towards a PR(Rock on pressure pad) then Agent 
				 * moves to push the Rock north under the constraints above mentioned about pushing
				 * a rock-the range, checks of obstacle, PR, Rock, Teleportal.
				 */
				else if(gridN[agentRow-1][agentColumn] == "PR"){
					if(0<=agentRow-2 && agentRow-2<m){
						if(gridN[agentRow-2][agentColumn]!="Rock" && gridN[agentRow-2][agentColumn]!="Obstacle" && gridN[agentRow-2][agentColumn]!="PR"  && gridN[agentRow-2][agentColumn]!="Teleportal"){ // Rock not next to a rock and rock not next to an obstacle
							if(gridN[agentRow-2][agentColumn]=="Pressure pad"){//Case pressure pad above PR
								gridN[agentRow-2][agentColumn]="PR";//becomes PR
								gridN[agentRow-1][agentColumn]="AP";//Agent moves on Pressure pad that had the rock on it, becomes AP
								
								//Previous cell is updated because Agent moved
								if(gridN[agentRow][agentColumn] == "Agent")
									gridN[agentRow][agentColumn] = "null";						

								else if(gridN[agentRow][agentColumn] == "AP")
									gridN[agentRow][agentColumn] = "Pressure pad";

								else if(gridN[agentRow][agentColumn] == "AT")								
									gridN[agentRow][agentColumn] = "Teleportal";

								//New node is made of the state of the grid and added to possibleNodes
								R2D2State r2d2stateN = new R2D2State(gridN);
								Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeN);

							}
							else{ //case null
								gridN[agentRow-2][agentColumn]="Rock";//Rock moves to the North cell
								gridN[agentRow-1][agentColumn]="AP";//Agent moves on pressure pad, becomes AP

								//Previous cell is updated because Agent moved
								if(gridN[agentRow][agentColumn] == "Agent")
									gridN[agentRow][agentColumn] = "null";						

								else if(gridN[agentRow][agentColumn] == "AP")
									gridN[agentRow][agentColumn] = "Pressure pad";

								else if(gridN[agentRow][agentColumn] == "AT")								
									gridN[agentRow][agentColumn] = "Teleportal";

								//New node is made of the state of the grid and added to possibleNodes
								R2D2State r2d2stateN = new R2D2State(gridN);
								Node newNodeN = new Node(r2d2stateN, node,'N', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeN);
							}
						}
					}
				}
			}
			/**
			 * For printing the node for testing purposes
			 * for(int i =0;i<gridN.length; i++){
				ArrayList<String> row = new ArrayList<String>();
				for (int j=0;j<gridN[i].length;j++){
					row.add(gridN[i][j]);
				}
				System.out.println(row);
				}
			System.out.println(".............bye north...............");
			 */
		
		}

////////////////East Expansion//////////////////////////////////////////////////////////////////////////////////////////////////////
		if((0<=agentColumn+1 && agentColumn+1<m && gridE[agentRow][agentColumn+1]!="Obstacle")){
			if(gridE[agentRow][agentColumn] == "Agent" ||gridE[agentRow][agentColumn]  == "AP" || gridE[agentRow][agentColumn]  == "AT"){
				if(gridE[agentRow][agentColumn+1] == "null"){
					gridE[agentRow][agentColumn+1] = "Agent";
					if(gridE[agentRow][agentColumn] == "Agent")
						gridE[agentRow][agentColumn] = "null";						
					else if(gridE[agentRow][agentColumn] == "AP")
						gridE[agentRow][agentColumn] = "Pressure pad";
					else if(gridE[agentRow][agentColumn] == "AT")								
						gridE[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateE = new R2D2State(gridE);
					Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeE);
				}
				else if(gridE[agentRow][agentColumn+1] == "Rock")
				{
					if((0<=agentColumn+2 && agentColumn+2<m) && gridE[agentRow][agentColumn+2]!="Rock" && gridE[agentRow][agentColumn+2]!="Teleportal" && gridE[agentRow][agentColumn+2]!="Obstacle" && gridE[agentRow][agentColumn+2]!="PR"){
						if(gridE[agentRow][agentColumn+2] == "Pressure pad"){ //case pressure pad
							gridE[agentRow][agentColumn+2] = "PR";
							gridE[agentRow][agentColumn+1] = "Agent";

							if(gridE[agentRow][agentColumn] == "Agent")
								gridE[agentRow][agentColumn] = "null";						

							else if(gridE[agentRow][agentColumn] == "AP")
								gridE[agentRow][agentColumn] = "Pressure pad";

							else if(gridE[agentRow][agentColumn] == "AT")								
								gridE[agentRow][agentColumn] = "Teleportal";

							R2D2State r2d2stateE = new R2D2State(gridE);
							Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeE);
						}
						else{//case null 
							gridE[agentRow][agentColumn+1] = "Agent";
							gridE[agentRow][agentColumn+2] = "Rock";

							if(gridE[agentRow][agentColumn] == "Agent")
								gridE[agentRow][agentColumn] = "null";						

							else if(gridE[agentRow][agentColumn] == "AP")
								gridE[agentRow][agentColumn] = "Pressure pad";

							else if(gridE[agentRow][agentColumn] == "AT")								
								gridE[agentRow][agentColumn] = "Teleportal";
							R2D2State r2d2stateE = new R2D2State(gridE);
							Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeE);
						}
					}

				}
				else if(gridE[agentRow][agentColumn+1] == "Pressure pad")
				{
					gridE[agentRow][agentColumn+1]="AP";  //Agent and pressure pad on the same cell 
					if(gridE[agentRow][agentColumn] == "Agent")
						gridE[agentRow][agentColumn] = "null";						

					else if(gridE[agentRow][agentColumn] == "AP")
						gridE[agentRow][agentColumn] = "Pressure pad";

					else if(gridE[agentRow][agentColumn] == "AT")								
						gridE[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateE = new R2D2State(gridE);
					Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeE);			
				}
				else if(gridE[agentRow][agentColumn+1] == "Teleportal"){
					gridE[agentRow][agentColumn+1] ="AT";
					if(gridE[agentRow][agentColumn] == "Agent")
						gridE[agentRow][agentColumn] = "null";						

					else if(gridE[agentRow][agentColumn] == "AP")
						gridE[agentRow][agentColumn] = "Pressure pad";

					else if(gridE[agentRow][agentColumn] == "AT")								
						gridE[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateE = new R2D2State(gridE);
					Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeE);	

				}
				else if(gridE[agentRow][agentColumn+1] == "PR"){
					if(0<=agentColumn+2 && agentColumn+2<m){
						if(gridE[agentRow][agentColumn+2]!="Rock" && gridE[agentRow][agentColumn+2]!="Obstacle" && gridE[agentRow][agentColumn+2]!="PR"  && gridE[agentRow][agentColumn+2]!="Teleportal"){ // Rock not next to a rock and rock not next to an obstacle
							if(gridE[agentRow][agentColumn+2]=="Pressure pad"){
								gridE[agentRow][agentColumn+2]="PR";
								gridE[agentRow][agentColumn+1]="AP";

								if(gridE[agentRow][agentColumn] == "Agent")
									gridE[agentRow][agentColumn] = "null";						

								else if(gridE[agentRow][agentColumn] == "AP")
									gridE[agentRow][agentColumn] = "Pressure pad";

								else if(gridE[agentRow][agentColumn] == "AT")								
									gridE[agentRow][agentColumn] = "Teleportal";

								R2D2State r2d2stateE = new R2D2State(gridE);
								Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeE);

							}
							else{ //case null
								gridE[agentRow][agentColumn+2]="Rock";
								gridE[agentRow][agentColumn+1]="AP";

								if(gridE[agentRow][agentColumn] == "Agent")
									gridE[agentRow][agentColumn] = "null";						

								else if(gridE[agentRow][agentColumn] == "AP")
									gridE[agentRow][agentColumn] = "Pressure pad";

								else if(gridE[agentRow][agentColumn] == "AT")								
									gridE[agentRow][agentColumn] = "Teleportal";

								R2D2State r2d2stateE = new R2D2State(gridE);
								Node newNodeE = new Node(r2d2stateE, node,'E', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeE);
							}
						}
					}
				}
			}
			/**
			 * For printing the node for testing purposes
			 * for(int i =0;i<gridE.length; i++){
				ArrayList<String> row = new ArrayList<String>();
				for (int j=0;j<gridE[i].length;j++){
					row.add(gridE[i][j]);
				}
				System.out.println(row);
				}
			System.out.println(".............Bye East...............");
			 */
		}


///////////South Expansion/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if((0<=agentRow+1 && agentRow+1<m && gridS[agentRow+1][agentColumn]!="Obstacle")){
			if(gridS[agentRow][agentColumn] == "Agent" ||gridS[agentRow][agentColumn]  == "AP" || gridS[agentRow][agentColumn]  == "AT"){
				if(gridS[agentRow+1][agentColumn] == "null"){
					gridS[agentRow+1][agentColumn] = "Agent";
					if(gridS[agentRow][agentColumn] == "Agent")
						gridS[agentRow][agentColumn] = "null";						
					else if(gridS[agentRow][agentColumn] == "AP")
						gridS[agentRow][agentColumn] = "Pressure pad";
					else if(gridS[agentRow][agentColumn] == "AT")								
						gridS[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateS = new R2D2State(gridS);
					Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeS);
				}
				else if(gridS[agentRow+1][agentColumn] == "Rock")
				{
					if((0<=agentRow+2 && agentRow+2<m) && gridS[agentRow+2][agentColumn]!="Rock" && gridS[agentRow+2][agentColumn]!="Teleportal" && gridS[agentRow+2][agentColumn]!="Obstacle" && gridS[agentRow+2][agentColumn]!="PR"){
						if(gridS[agentRow+2][agentColumn] == "Pressure pad"){ //case pressure pad
							gridS[agentRow+2][agentColumn] = "PR";
							gridS[agentRow+1][agentColumn] = "Agent";

							if(gridS[agentRow][agentColumn] == "Agent")
								gridS[agentRow][agentColumn] = "null";						

							else if(gridS[agentRow][agentColumn] == "AP")
								gridS[agentRow][agentColumn] = "Pressure pad";

							else if(gridS[agentRow][agentColumn] == "AT")								
								gridS[agentRow][agentColumn] = "Teleportal";

							R2D2State r2d2stateS = new R2D2State(gridS);
							Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeS);
						}
						else {//case null 
							gridS[agentRow+1][agentColumn] = "Agent";
							gridS[agentRow+2][agentColumn] = "Rock";

							if(gridS[agentRow][agentColumn] == "Agent")
								gridS[agentRow][agentColumn] = "null";						

							else if(gridS[agentRow][agentColumn] == "AP")
								gridS[agentRow][agentColumn] = "Pressure pad";

							else if(gridS[agentRow][agentColumn] == "AT")								
								gridS[agentRow][agentColumn] = "Teleportal";
							R2D2State r2d2stateS = new R2D2State(gridS);
							Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeS);
						}
					}

				}
				else if(gridS[agentRow+1][agentColumn] == "Pressure pad")
				{
					gridS[agentRow+1][agentColumn]="AP";  //Agent and pressure pad on the same cell 
					if(gridS[agentRow][agentColumn] == "Agent")
						gridS[agentRow][agentColumn] = "null";						

					else if(gridS[agentRow][agentColumn] == "AP")
						gridS[agentRow][agentColumn] = "Pressure pad";

					else if(gridS[agentRow][agentColumn] == "AT")								
						gridS[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateS = new R2D2State(gridS);
					Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeS);			
				}
				else if(gridS[agentRow+1][agentColumn] == "Teleportal"){
					gridS[agentRow+1][agentColumn] ="AT";
					if(gridS[agentRow][agentColumn] == "Agent")
						gridS[agentRow][agentColumn] = "null";						

					else if(gridS[agentRow][agentColumn] == "AP")
						gridS[agentRow][agentColumn] = "Pressure pad";

					else if(gridS[agentRow][agentColumn] == "AT")								
						gridS[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateS = new R2D2State(gridS);
					Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeS);	

				}
				else if(gridS[agentRow+1][agentColumn] == "PR"){
					if(0<=agentRow+2 && agentRow+2<m){
						if(gridS[agentRow+2][agentColumn]!="Rock" && gridS[agentRow+2][agentColumn]!="Obstacle" && gridS[agentRow+2][agentColumn]!="PR"  && gridS[agentRow+2][agentColumn]!="Teleportal"){ // Rock not next to a rock and rock not next to an obstacle
							if(gridS[agentRow+2][agentColumn]=="Pressure pad"){
								gridS[agentRow+2][agentColumn]="PR";
								gridS[agentRow+1][agentColumn]="AP";

								if(gridS[agentRow][agentColumn] == "Agent")
									gridS[agentRow][agentColumn] = "null";						

								else if(gridS[agentRow][agentColumn] == "AP")
									gridS[agentRow][agentColumn] = "Pressure pad";

								else if(gridS[agentRow][agentColumn] == "AT")								
									gridS[agentRow][agentColumn] = "Teleportal";

								R2D2State r2d2stateS = new R2D2State(gridS);
								Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeS);

							}
							else{ //case null
								gridS[agentRow+2][agentColumn]="Rock";
								gridS[agentRow+1][agentColumn]="AP";

								if(gridS[agentRow][agentColumn] == "Agent")
									gridS[agentRow][agentColumn] = "null";						

								else if(gridS[agentRow][agentColumn] == "AP")
									gridS[agentRow][agentColumn] = "Pressure pad";

								else if(gridS[agentRow][agentColumn] == "AT")								
									gridS[agentRow][agentColumn] = "Teleportal";

								R2D2State r2d2stateS = new R2D2State(gridS);
								Node newNodeS = new Node(r2d2stateS, node,'S', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeS);
							}
						}
					}
				}
			}
		}
		/**
		 * For printing the node for testing purposes
		 * for(int i =0;i<gridS.length; i++){
			ArrayList<String> row = new ArrayList<String>();
			for (int j=0;j<gridS[i].length;j++){
				row.add(gridS[i][j]);
			}
			System.out.println(row);
			}
		System.out.println(".............Bye South...............");
		 */
		
//////////////West Expansion///////////////////////////////////////////////////////////////////////////////////////////////////////////
		if((0<=agentColumn-1 && agentColumn-1<n && gridW[agentRow][agentColumn-1]!="Obstacle")){
			if(gridW[agentRow][agentColumn] == "Agent" ||gridW[agentRow][agentColumn]  == "AP" || gridW[agentRow][agentColumn]  == "AT"){
				if(gridW[agentRow][agentColumn-1] == "null"){
					gridW[agentRow][agentColumn-1] = "Agent";
					if(gridW[agentRow][agentColumn] == "Agent")
						gridW[agentRow][agentColumn] = "null";						
					else if(gridW[agentRow][agentColumn] == "AP")
						gridW[agentRow][agentColumn] = "Pressure pad";
					else if(gridW[agentRow][agentColumn] == "AT")								
						gridW[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateW = new R2D2State(gridW);
					Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeW);
				}
				else if(gridW[agentRow][agentColumn-1] == "Rock")
				{
					if((0<=agentColumn-2 && agentColumn-2<n) && gridW[agentRow][agentColumn-2]!="Rock" && gridW[agentRow][agentColumn-2]!="Teleportal" && gridW[agentRow][agentColumn-2]!="Obstacle" && gridW[agentRow][agentColumn-2]!="PR"){
						if(gridW[agentRow][agentColumn-2] == "Pressure pad"){ //case pressure pad
							gridW[agentRow][agentColumn-2] = "PR";
							gridW[agentRow][agentColumn-1] = "Agent";

							if(gridW[agentRow][agentColumn] == "Agent")
								gridW[agentRow][agentColumn] = "null";						

							else if(gridW[agentRow][agentColumn] == "AP")
								gridW[agentRow][agentColumn] = "Pressure pad";

							else if(gridW[agentRow][agentColumn] == "AT")								
								gridW[agentRow][agentColumn] = "Teleportal";

							R2D2State r2d2stateW = new R2D2State(gridW);
							Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeW);
						}
						else{//case null 
							gridW[agentRow][agentColumn-1] = "Agent";
							gridW[agentRow][agentColumn-2] = "Rock";

							if(gridW[agentRow][agentColumn] == "Agent")
								gridW[agentRow][agentColumn] = "null";						

							else if(gridW[agentRow][agentColumn] == "AP")
								gridW[agentRow][agentColumn] = "Pressure pad";

							else if(gridW[agentRow][agentColumn] == "AT")								
								gridW[agentRow][agentColumn] = "Teleportal";
							R2D2State r2d2stateW = new R2D2State(gridW);
							Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
							possibleNodes.add(newNodeW);
						}
					}

				}
				else if(gridW[agentRow][agentColumn-1] == "Pressure pad")
				{
					gridW[agentRow][agentColumn-1]="AP";  //Agent and pressure pad on the same cell 
					if(gridW[agentRow][agentColumn] == "Agent")
						gridW[agentRow][agentColumn] = "null";						

					else if(gridW[agentRow][agentColumn] == "AP")
						gridW[agentRow][agentColumn] = "Pressure pad";

					else if(gridW[agentRow][agentColumn] == "AT")								
						gridW[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateW = new R2D2State(gridW);
					Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeW);			
				}
				else if(gridW[agentRow][agentColumn-1] == "Teleportal"){
					gridW[agentRow][agentColumn-1] ="AT";
					if(gridW[agentRow][agentColumn] == "Agent")
						gridW[agentRow][agentColumn] = "null";						

					else if(gridW[agentRow][agentColumn] == "AP")
						gridW[agentRow][agentColumn] = "Pressure pad";

					else if(gridW[agentRow][agentColumn] == "AT")								
						gridW[agentRow][agentColumn] = "Teleportal";
					R2D2State r2d2stateW = new R2D2State(gridW);
					Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
					possibleNodes.add(newNodeW);	

				}
				else if(gridW[agentRow][agentColumn-1] == "PR"){
					//System.out.println("wohooooo");
					if(0<=agentColumn-2 && agentColumn-2<n){
						//System.out.println("wohooooo");
						if(gridW[agentRow][agentColumn-2]!="Rock" && gridW[agentRow][agentColumn-2]!="Obstacle" && gridW[agentRow][agentColumn-2]!="PR"  && gridW[agentRow][agentColumn-2]!="Teleportal"){ // Rock not next to a rock and rock not next to an obstacle
							if(gridW[agentRow][agentColumn-2]=="Pressure pad"){
								gridW[agentRow][agentColumn-2]="PR";
								gridW[agentRow][agentColumn-1]="AP";

								if(gridW[agentRow][agentColumn] == "Agent")
									gridW[agentRow][agentColumn] = "null";						

								else if(gridW[agentRow][agentColumn] == "AP")
									gridW[agentRow][agentColumn] = "Pressure pad";

								else if(gridW[agentRow][agentColumn] == "AT")								
									gridW[agentRow][agentColumn] = "Teleportal";

								R2D2State r2d2stateW = new R2D2State(gridW);
								Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeW);

							}
							else{ //case null
								gridW[agentRow][agentColumn-2]="Rock";
								gridW[agentRow][agentColumn-1]="AP";

								if(gridW[agentRow][agentColumn] == "Agent")
									gridW[agentRow][agentColumn] = "null";						

								else if(gridW[agentRow][agentColumn] == "AP")
									gridW[agentRow][agentColumn] = "Pressure pad";

								else if(gridW[agentRow][agentColumn] == "AT")								
									gridW[agentRow][agentColumn] = "Teleportal";

								R2D2State r2d2stateW = new R2D2State(gridW);
								Node newNodeW = new Node(r2d2stateW, node,'W', prevDepth + 1 ,pathCost + 1,0); 
								possibleNodes.add(newNodeW);
							}
						}
					}
				}
			}

			/**
			 * For printing the node for testing purposes
			 * for(int i =0;i<gridW.length; i++){
				ArrayList<String> row = new ArrayList<String>();
				for (int j=0;j<gridW[i].length;j++){
					row.add(gridW[i][j]);
				}
				System.out.println(row);
				}
			System.out.println(".............Bye West...............");
			 */
		}

		return possibleNodes;
	}



	
	/**
	 * PathCost function used to increment the node's pathcost
	 */
	@Override
	public int pathCost(Node n) {
		return n.getPathCost()+1;
	}

	/**
	 * Goal Test: If there are no inactivated rocks and the Agent is at the Teleportal, then return true
	 */
	@Override
	public Boolean goalTest(Node n) {
		String[][] grid = ((R2D2State)n.getState()).getGrid();
		boolean at=false;
		boolean rock=false;
		for(int i =0;i<grid.length;i++){
			for(int j=0;j<grid[i].length;j++){
				if(grid[i][j] == "Rock"){
					rock=true;
				}
				if(grid[i][j] == "AT")
					at=true;
			}
		}
		if(rock == false && at == true)
			return true; 
		else
			return false;
	}
	
	/**
	 * Search Function: 
	 * @param grid: passed by main method as genGrid()
	 * @param strategy: passed by main method as BFS,DFS,ID,UC,GR1,GR2,A*1,A*2
	 * @param visualize: passed by main method to print each step to goal
	 * @return a StringBuilder that appends the operators that led to the goal and prints goalGrid, cost and number of expanded nodes
	 * It contains:
	 * 1) Makes a new R2D2State of the grid then a new helpr2d2 
	 * 2) Checks on the strategy, Iterative deepening is a special case where a loop on the genericSearch is made 
	 * 	  to expand the initial node everytime till the limit and thus be given the full tree everytime.
	 * 3) If not ID then it goes in normally to the rest in GenericSearch
	 * 4) If there is a solution, the goal test grid is printed then the operators are appended to the stringBuilder
	 * Visualizing will print all the previous nodes to the goal.
	 * 5) Print the sequence of operators, expanded nodes to the goal and cost
	 */

	public static StringBuilder Search(String[][] grid, String strategy, Boolean visualize){
		R2D2State r2d2state = new R2D2State(grid);
		Node node = new Node(r2d2state, null,'\0', 0,0,0);
		HelpR2D2 helpr2d2 = new HelpR2D2(r2d2state, 0, '\0');
		Node result = null;
		int cost=0;
		int limit=0;
		if(strategy.equals("ID")){
			//loop with incrementing limit
			while(true){
				GeneralSearch generalSearch = new GeneralSearch(helpr2d2,"ID",node,limit);
				limit++;
				result=generalSearch.GenericSearch(helpr2d2, "ID",limit);
				if(result != null){
					cost=result.pathCost;
					break;
				}
			}
		}else{
			GeneralSearch generalSearch = new GeneralSearch(helpr2d2,strategy,node,0);
			result=generalSearch.GenericSearch(helpr2d2, strategy,0);
			if(result == null)
				System.out.println("NO SOLUTION");
			if(result != null)
				cost=result.pathCost;
		}
		if(result != null){//Printing Goal state
			System.out.println("Goal Test Grid(in descending order): ");
			for(int i =0 ; i<m;i++){
				ArrayList<String> row = new ArrayList<String>();
				for(int j=0 ; j<n;j++){
					row.add(((R2D2State)result.getState()).getGrid()[i][j]);
				}
				System.out.println(row);
			}
			System.out.println("------------------------");
			System.out.println("\n");
		}

		int expandedNodes=0;
		StringBuilder resultStr = new StringBuilder();
		if(result != null){
			while(result.parent != null){ //getting the sequence of operators that led us to answer
				resultStr.append(result.operator);
				resultStr.append(" ");
				
				if(visualize && result.parent != null){	
					System.out.println("Grids to goal:");
					for(int i =0 ; i<m;i++){
						ArrayList<String> row = new ArrayList<String>();
						for(int j=0 ; j<n;j++){
							row.add(((R2D2State)result.getState()).getGrid()[i][j]);
						}
						System.out.println(row);
					}
					System.out.println("------Agent went "+result.operator+"--------------");
					System.out.println("\n");

				}
				result = result.parent;
				expandedNodes++;
			}
		}
		System.out.println("Sequence of Operators Ascendingly: "+resultStr.reverse());
		System.out.println("Expanded nodes to get Goal: "+expandedNodes);//expanded nodes to the goal test only
		System.out.println("Cost: "+cost);

		return resultStr;

	}
	
	/**
	 * genGrid: produces a randomized grid with specific dimensions m and n where the number of 
	 * rocks = number of pressure pads, 1 agent and 1 teleportal.
	 * @return
	 */
	public static String[][] genGrid(){
		String[][] grid = new String [m][n];

		String[][] gridCom = new String [m][n];
		for (int i =0; i<m; i++){
			for(int j=0 ; j<n ; j++){
				gridCom[i][j]=(i + "," + j);
			}
		}
		String[][] arr2 = shuffle(gridCom);
		grid[Integer.parseInt(arr2[0][0].split(",")[0])][Integer.parseInt(arr2[0][0].split(",")[1])] = "Agent";
		grid[Integer.parseInt(arr2[0][1].split(",")[0])][Integer.parseInt(arr2[0][1].split(",")[1])] = "Teleportal";

		//Number of rocks, pressure pads and obstacles in the grid
		obstaclesNumber = random.nextInt(((m*n)-4)+1) /2; //decrease number of obstacles
		rocksNumber = random.nextInt(m)+1;
		while(rocksNumber> ((m*n)-obstaclesNumber)/2)
			rocksNumber = random.nextInt(m)+2;
		if(obstaclesNumber >=3)
			obstaclesNumber = obstaclesNumber -2;
		if(rocksNumber >=3){
			rocksNumber = rocksNumber-2;
		}
		//pressurePadNumber = rocksNumber

		////generating obstacles
		int counter = 0;
		for(int i =0; i<obstaclesNumber ; i++){
			for(int x = 0; x<arr2.length; x++){
				for(int y =0 ; y<arr2[i].length; y ++){
					if(grid[x][y] !="Agent" && grid[x][y] !="Teleportal" && (counter<obstaclesNumber)){
						grid[x][y] = "Obstacle";
						counter ++;
					}
				}
			}

		}
	////generating rocks
		counter = 0;
		for(int i =0; i<rocksNumber ; i++){
			for(int x = 0; x<arr2.length; x++){
				for(int y =0 ; y<arr2[i].length; y ++){
					if(grid[x][y] !="Agent" && grid[x][y] !="Teleportal" && grid[x][y] !="Obstacle" && (counter<rocksNumber)){
						grid[x][y] = "Rock";
						counter ++;
					}
				}
			}

		}
	////generating pressure pads
		counter = 0 ;
		for(int i =0; i<rocksNumber ; i++){
			for(int x = 0; x<arr2.length; x++){
				for(int y =0 ; y<arr2[i].length; y ++){
					if(grid[x][y] !="Agent" && grid[x][y] !="Teleportal" && grid[x][y] !="Obstacle" && grid[x][y] !="Rock" && (counter<rocksNumber)){
						grid[x][y] = "Pressure pad";
						counter ++;
					}
				}
			}

		}

		/**printing the grid
		for(int i =0;i<m; i++){
			ArrayList<String> row = new ArrayList<String>();
			for (int j=0;j<n;j++){
				row.add(grid[i][j]);
			}
			System.out.println(row);
		}
		 * 
		 */

		return grid;

	}
	
	public static String[][] shuffle(String[][] a) {
		Random random = new Random();

		for (int i = a.length - 1; i > 0; i--) {
			for (int j = a[i].length - 1; j > 0; j--) {
				int m = random.nextInt(i + 1);
				int n = random.nextInt(j + 1);

				String temp = a[i][j];
				a[i][j] = a[m][n];
				a[m][n] = temp;
			}
		}
		return a;
	}
	
	/**
	 * Main testing and printing is here!
	 */
	public static void main(String[] args) {
		System.out.println("				Welcome to Safa and Narihan's implementation of HelpR2D2       ");
		System.out.print( "\n\n\n" );
		
		
		String[][] testGrid = genGrid();
		
		/**Testing with a specific grid for simplification (predefined grid)
		String[][] testGrid = new String[m][n];
		for(int i =0;i<m;i++){
			for(int j=0; j<n;j++){
				testGrid[i][j]=testGrid2[i][j];
			}
		}
		String[][] testGrid = new String[2][2];
		testGrid[0][0] = "Teleportal";
		testGrid[0][1] = "Obstacle";
		testGrid[1][0] = "null";
		testGrid[1][1] = "Agent";


		String[][] testGrid = new String[3][3];
		testGrid[0][0] = "Teleportal";
		testGrid[0][2] = "Pressure pad";
		testGrid[0][1] = "null";
		testGrid[1][0] = "Pressure pad";
		testGrid[1][1] = "Rock";
		testGrid[1][2] = "Rock";
		testGrid[2][0] = "Pressure pad";
		testGrid[2][1] = "Rock";
		testGrid[2][2] = "Agent";
 		* 
 */
		
		System.out.println("Initial Grid: ");
		System.out.print( "\n" );
		for(int i =0;i<m; i++){
			ArrayList<String> row = new ArrayList<String>();
			for (int j=0;j<n;j++){
				row.add(testGrid[i][j]);
			}
			System.out.println(row);
		}
		System.out.println( "----------------------------------------------" );
		
		//Testing Breadth First
				noOfExpandedNodes =0;
				System.out.println("Breadth First Search:");
				System.out.print( "\n" );
				Search(testGrid, "BFS", true);
				System.out.println("expanded nodes:"+noOfExpandedNodes);
				System.out.println( "----------------------------------------------" );
				
				
		//Testing Depth First
		//		noOfExpandedNodes =0;
		//		System.out.println("Depth First Search:");
		//	System.out.print( "\n" );
		//		Search(testGrid, "DFS", false);
		//		System.out.println("expanded nodes:"+noOfExpandedNodes);
//				System.out.print( "----------------------------------------------" );
//				System.out.print( "\n\n\n" );

		//Testing Uniform Cost
				noOfExpandedNodes =0;
				System.out.println("Uniform Cost:");
				System.out.print( "\n" );
				Search(testGrid, "UC", true);
				System.out.println("expanded nodes:"+noOfExpandedNodes);
				System.out.println( "----------------------------------------------" );

		//Testing Iterative Deeping
				noOfExpandedNodes =0;
				System.out.println("Iterative deeping:");
				System.out.print( "\n" );
				Search(testGrid, "ID", true);
				System.out.println("expanded nodes:"+noOfExpandedNodes);
				System.out.println( "----------------------------------------------" );


		//Testing Greedy1 
//				noOfExpandedNodes =0;
//				System.out.println("GR1:");
//				System.out.print( "\n" );
//				Search(testGrid, "GR1", false);
//				System.out.println("expanded nodes:"+noOfExpandedNodes);
//				System.out.print( "----------------------------------------------" );
				

		//Testing A*1 
				noOfExpandedNodes =0;
				System.out.println("A*1:");
				System.out.print( "\n" );
				Search(testGrid, "A*1", true);
				System.out.println("expanded nodes: "+noOfExpandedNodes);
				System.out.println( "----------------------------------------------" );

		//Testing Greedy2 
				noOfExpandedNodes =0;
				System.out.println("GR2:");
				System.out.print( "\n" );
				Search(testGrid, "GR2", true);
				System.out.println("expanded nodes:"+noOfExpandedNodes);
				System.out.println( "----------------------------------------------" );

		//Testing A*2 
				noOfExpandedNodes =0;
				System.out.println("A*2:");
				System.out.print( "\n" );
				Search(testGrid, "A*2", true);
				System.out.println("expanded nodes: "+noOfExpandedNodes);
				System.out.println( "----------------------------------------------" );
				
				
				System.out.print( "\n\n\n" );
				System.out.println( "           Hope You Enjoyed it        " );
				
				
		//Testing expand alone
		//		R2D2State r2d2state = new R2D2State(testGrid);
		//		Node node = new Node(r2d2state, null,'N', 0,0,0);
		//		HelpR2D2 h = new HelpR2D2(r2d2state, 0, 'N');
		//		ArrayList<Node> n= new ArrayList<Node>();
		//		n=h.Expand(node);


	}
}