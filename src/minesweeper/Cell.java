package minesweeper;

public class Cell {

	enum CellType {
		mine, blank, normal 
	}

	private static int mineNum = 0;
	private static int blankNum = 0;
	private static int normalNum = 0;
	
	private int x;
	private int y;
	private Button button = new Button();
	private CellType type = null;
	private int value = 0;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static int mineNum() {
		return mineNum;
	}
	public static int blankNum() {
		return blankNum;
	}
	public static int normalNum() {
		return normalNum;
	}
	
	public int x() {
		return this.x;
	}
	public int y() {
		return this.y;
	}
	public CellType type() {
		return this.type;
	}
	public int value() {
		return this.value;
	}
	public Button button() {
		return this.button;
	}

	public void setType(CellType type) {
		this.type = type;

		switch(type) {
		case mine:
			mineNum++;
			break;
		case blank:
			blankNum++;
			break;
		case normal:
			normalNum++;
			break;
		}
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isMine() {
		return this.type == CellType.mine;
	}
	
	public boolean equals(Cell cell) {
		boolean identicalX = this.x == cell.x();
		boolean identicalY = this.y == cell.y();
		boolean identicalType = this.type == cell.type();
		boolean identicalValue = this.value == cell.value();
		
		return identicalX && identicalY && identicalType && identicalValue;
	}
}
