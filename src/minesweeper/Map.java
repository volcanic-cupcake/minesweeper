package minesweeper;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import minesweeper.Cell.CellType;

public class Map {
	private List<Cell> cells;
	private int area;
	private int width;
	private int height;
	private int mines;
	
	public Map(int width, int height, int mines) {
		this.mines = mines;
		this.width = width;
		this.height = height;
		
		this.area = width * height;
		
	}
	
	public int area() {
		return this.area;
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

	public Cell getCell(int x, int y) {
		for (Cell cell : cells) {
			boolean identicalX = cell.x() == x;
			boolean identicalY = cell.y() == y;
			if (identicalX && identicalY) return cell;
		}
		return null;
	}
	public void generate(Cell startingCell) {
		Frame frame = new Frame();
		
		cells = new ArrayList<Cell>();
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				Cell cell = new Cell(x, y);
				cells.add(cell);
				frame.add(cell.button());
			}
		}
		frame.setLayout(new GridLayout(this.height, this.width, 5, 5));
		frame.setSize(1000, 700);
		frame.setVisible(true);
		
		generateMines(startingCell);
		generateValues();
	}
	public void print() {
		int testint = 0;
		String output = "";
		for (Cell cell : cells) {
			if (cell.type() == CellType.mine) output += "* | ";
			else if (cell.type() == CellType.blank) output += "  | ";
			else output += cell.value() + " | ";
			
			testint++;
			if (testint >= this.width) {
				testint = 0;
				output += "\n";
			}
		}
		System.out.println(output);
	}
	private void generateMines(Cell startingCell) {
		Random random = new Random();
		while (Cell.mineNum() != this.mines) {
			int rndX = random.nextInt(0, this.width);
			int rndY = random.nextInt(0, this.height);
			Cell rndCell = getCell(rndX, rndY);
			boolean isStartingCell = rndCell.equals(startingCell);
			boolean isMine = rndCell.isMine();

			if (!isMine && !isStartingCell) rndCell.setType(CellType.mine);
		}
	}
	private void generateValues() {
		for (Cell cell : cells) {
			if (cell.type() == CellType.mine) continue;
			
			int x = cell.x();
			int y = cell.y();
			Cell[] neighbors = {
				getCell(x-1, y-1),
				getCell(x, y-1),
				getCell(x+1, y-1),
				getCell(x-1, y),
				getCell(x+1, y),
				getCell(x-1, y+1),
				getCell(x, y+1),
				getCell(x+1, y+1)
			};
			
			int value = 0;
			for (Cell neighbor : neighbors) {
				if (neighbor == null) continue;
				if (neighbor.type() == CellType.mine) value++;
			}
			
			if (value == 0) {
				cell.setType(CellType.blank);
			}
			else {
				cell.setType(CellType.normal);
				cell.setValue(value);
			}
		}
	}
}
