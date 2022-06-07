package minesweeper;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	public List<Cell> getNeighbors(Cell cell) {
			int x = cell.x();
			int y = cell.y();
			Cell[] neighborSquares = {
				getCell(x-1, y-1),
				getCell(x, y-1),
				getCell(x+1, y-1),
				getCell(x-1, y),
				getCell(x+1, y),
				getCell(x-1, y+1),
				getCell(x, y+1),
				getCell(x+1, y+1)
			};

			List<Cell> neighbors = new ArrayList<Cell>();
			for (Cell square : neighborSquares) {
				if (square == null) continue;
				neighbors.add(square);
			}
			return neighbors;
	}
	public void generate(Cell startingCell) {
		JPanel panel = new JPanel();
		int[] panelSize = calculatePanelSize();
		panel.setLayout(new GridLayout(this.height, this.width, 5, 5));
		panel.setBounds(25, 25, panelSize[0], panelSize[1]);
		
		cells = new ArrayList<Cell>();
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				Cell cell = new Cell(x, y);
				cell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch(e.getButton()) {
						case MouseEvent.BUTTON1:
							if (cell.isFlagged()) return;

							generateMines(cell);
							generateValues();
							break;
						case MouseEvent.BUTTON3:
							ImageIcon newIcon = cell.isFlagged() ? new ImageIcon("Images/facingDown.png") : new ImageIcon("Images/flagged.png");
							cell.toggleIsFlagged();
							cell.button().setIcon(newIcon);
							break;
						}
					}
				});
				cells.add(cell);
				panel.add(cell.button());
			}
		}
		Frame frame = new Frame(panelSize[0] + 100, panelSize[1] + 100);
		frame.add(panel);
		frame.setVisible(true);
		
	}
	private int[] calculatePanelSize() {
		final int MAX_WIDTH = 1400;
		final int MAX_HEIGHT = 1020;
		int panelWidth;
		int panelHeight;

		if (this.width > this.height) {
			int scale = this.width / this.height;
			panelWidth = MAX_WIDTH;
			panelHeight = panelWidth / scale;
		}
		else {
			int scale = this.height / this.width;
			panelHeight = MAX_HEIGHT;
			panelWidth = panelHeight / scale;
		}
		int[] size = {panelWidth, panelHeight};
		return size;
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
	private void chainBlanks(Cell cell) {
		List<Cell> neighbors = getNeighbors(cell);
		for (Cell neighbor : neighbors) {
			if (neighbor.isClicked()) continue;

			neighbor.button().setIcon( neighbor.revealedIcon() );
			neighbor.setIsClicked(true);
			if (neighbor.type() == CellType.blank) chainBlanks(neighbor);
		}
	}
	private void generateMines(Cell startingCell) {
		Random random = new Random();
		while (Cell.mineNum() != this.mines) {
			int rndX = random.nextInt(0, this.width);
			int rndY = random.nextInt(0, this.height);
			Cell rndCell = getCell(rndX, rndY);
			boolean isStartingCell = rndCell.equals(startingCell);
			boolean isMine = rndCell.isMine();

			if (!isMine && !isStartingCell) {
				rndCell.setType(CellType.mine);
				rndCell.setRevealedIcon(new ImageIcon("Images/bomb.png"));
				rndCell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if (rndCell.isFlagged()) return;
							
							JOptionPane.showMessageDialog(null, "you have lost");
							for (Cell cell : cells) {
								if (!cell.isFlagged()) {
									ImageIcon revealedIcon = cell.revealedIcon();
									cell.button().setIcon(revealedIcon);
								}
							}
							rndCell.setIsClicked(true);
							break;
						case MouseEvent.BUTTON3:
							ImageIcon newIcon = rndCell.isFlagged() ? new ImageIcon("Images/facingDown.png") : new ImageIcon("Images/flagged.png");
							rndCell.toggleIsFlagged();
							rndCell.button().setIcon(newIcon);
							break;
						}
					}
				});
			}
		}
	}
	private void generateValues() {
		for (Cell cell : cells) {
			if (cell.type() == CellType.mine) continue;
			
			List<Cell> neighbors = getNeighbors(cell);
			int value = 0;
			for (Cell neighbor : neighbors) {
				if (neighbor.type() == CellType.mine) value++;
			}
			
			if (value == 0) {
				cell.setType(CellType.blank);
				cell.setRevealedIcon(new ImageIcon("Images/0.png"));
				cell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if (cell.isFlagged()) return;
							
							cell.setIsClicked(true);
							chainBlanks(cell);
							break;
						case MouseEvent.BUTTON3:
							ImageIcon newIcon = cell.isFlagged() ? new ImageIcon("Images/facingDown.png") : new ImageIcon("Images/flagged.png");
							cell.toggleIsFlagged();
							cell.button().setIcon(newIcon);
							break;
						}
					}
				});
			}
			else {
				cell.setType(CellType.normal);
				cell.setValue(value);
				cell.setRevealedIcon(new ImageIcon("Images/" + value + ".png"));
				cell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if (cell.isFlagged()) return;
							
							cell.setIsClicked(true);
							//stuff when clicked
							break;
						case MouseEvent.BUTTON3:
							ImageIcon newIcon = cell.isFlagged() ? new ImageIcon("Images/facingDown.png") : new ImageIcon("Images/flagged.png");
							cell.toggleIsFlagged();
							cell.button().setIcon(newIcon);
							break;
						}
					}
				});
			}
		}
	}
}
