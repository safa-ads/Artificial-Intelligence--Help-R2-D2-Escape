import java.util.ArrayList;


public class R2D2State extends State{
//generating grid
	String [][] grid;
	ArrayList<Location> rocksLocation= new ArrayList<Location>();//calculating the positions of rocks in the grid
	
	
	public R2D2State(String[][] grid) {
	super();
	this.grid = grid;
	}

	public String[][] getGrid() {
		return grid;
	}

	public void setGrid(String[][] grid) {
		this.grid = grid;
	}
	
	public ArrayList<Location> getRocksLocation() {
		for(int i=0;i<grid.length;i++){
			for(int j=0; j<grid[i].length;j++){
				if(grid[i][j] == "Rock" || grid[i][j] == "PR"){
					Location l = new Location(i, j);
					rocksLocation.add(l);
				}
			}
		}
		return rocksLocation;
	}

	public void setRocksLocation(ArrayList<Location> rocksLocation) {
		this.rocksLocation = rocksLocation;
	}

	
}
