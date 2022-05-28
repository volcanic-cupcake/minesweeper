package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private List<Cell> cells = new ArrayList<Cell>();
	private int width;
	private int height;
	private int mines;
	
	public Map(int width, int height, int mines) {
		this.width = width;
		this.height = height;
		this.mines = mines;
	}
	
	public int width() {
		return this.width;
	}
	public int height() {
		return this.height;
	}
	public int mines() {
		return this.mines;
	}
	
	public void generate() {
		
	}
}
