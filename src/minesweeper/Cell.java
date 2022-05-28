package minesweeper;

public class Cell {

	enum CellType {
		mine, blank, normal 
	 }

	private int x;
	private int y;
	private int value;
	
	public Cell(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public int x() {
		return this.x;
	}
	public int y() {
		return this.y;
	}
	public int value() {
		return this.value;
	}
	
}
