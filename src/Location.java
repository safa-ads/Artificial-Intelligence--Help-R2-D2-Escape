
public class Location {
	//class to define the location in the grid by row and column
	int row;
	int column;
	
	
	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getColumn() {
		return column;
	}


	public void setColumn(int column) {
		this.column = column;
	}
	

}
