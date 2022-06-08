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
	public void generate() {
		JPanel panel = new JPanel();
		PanelSize panelSize = calculatePanelSize();

		int hgap = 5;
		int vgap = 5;

		int hgapNum = this.width - 1;
		int vgapNum = this.height - 1;

		int totalHgap = hgapNum * hgap;
		int totalVgap = vgapNum * vgap;

		int buttonWidth = (panelSize.width() - totalHgap) / this.width;
		int buttonHeight = (panelSize.height() - totalVgap) / this.height;
		Button.width = buttonWidth;
		Button.height = buttonHeight;

		panel.setLayout(new GridLayout(this.height, this.width, hgap, vgap));
		panel.setBounds(25, 25, panelSize.width(), panelSize.height());
		
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

							cell.button().setIcon( cell.revealedIcon().path );
							List<Cell> neighbors = getNeighbors(cell);
							for (Cell neighbor : neighbors) {
								if (neighbor.type() == CellType.blank) chainBlanks(neighbor);
							}
							break;
						case MouseEvent.BUTTON3:
							CellPicture newIcon = cell.isFlagged() ? CellPicture.facingDown : CellPicture.flagged;
							cell.toggleIsFlagged();
							cell.button().setIcon(newIcon.path);
							break;
						}
					}
				});
				cells.add(cell);
				cell.button().setIcon(CellPicture.facingDown.path);
				panel.add(cell.button());
			}
		}
		Frame frame = new Frame(panelSize.width() + 100, panelSize.height() + 100);
		frame.add(panel);
		frame.setVisible(true);
		
	}
	private PanelSize calculatePanelSize() {
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
		PanelSize size = new PanelSize(panelWidth, panelHeight);
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

			neighbor.button().setIcon( neighbor.revealedIcon().path );
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
				rndCell.setRevealedIcon(CellPicture.mine);
				rndCell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if (rndCell.isFlagged() || rndCell.isClicked()) return;
							
							JOptionPane.showMessageDialog(null, "you have lost");
							for (Cell cell : cells) {
								if (cell.isFlagged()) continue;

								CellPicture revealedIcon = cell.revealedIcon();
								cell.button().setIcon(revealedIcon.path);
							}
							rndCell.setIsClicked(true);
							break;
						case MouseEvent.BUTTON3:
							CellPicture newIcon = rndCell.isFlagged() ? CellPicture.facingDown : CellPicture.flagged;
							rndCell.toggleIsFlagged();
							rndCell.button().setIcon(newIcon.path);
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
				cell.setRevealedIcon(CellPicture.c0);
				cell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if (cell.isFlagged() || cell.isClicked()) return;
							
							cell.setIsClicked(true);
							chainBlanks(cell);
							break;
						case MouseEvent.BUTTON3:
							CellPicture newIcon = cell.isFlagged() ? CellPicture.facingDown : CellPicture.flagged;
							cell.toggleIsFlagged();
							cell.button().setIcon(newIcon.path);
							break;
						}
					}
				});
			}
			else {
				cell.setType(CellType.normal);
				cell.setValue(value);
				switch(value) {
				case 1:
					cell.setRevealedIcon(CellPicture.c1);
					break;
				case 2:
					cell.setRevealedIcon(CellPicture.c2);
					break;
				case 3:
					cell.setRevealedIcon(CellPicture.c3);
					break;
				case 4:
					cell.setRevealedIcon(CellPicture.c4);
					break;
				case 5:
					cell.setRevealedIcon(CellPicture.c5);
					break;
				case 6:
					cell.setRevealedIcon(CellPicture.c6);
					break;
				case 7:
					cell.setRevealedIcon(CellPicture.c7);
					break;
				case 8:
					cell.setRevealedIcon(CellPicture.c8);
					break;
				}
				cell.button().setMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if (cell.isFlagged() || cell.isClicked()) return;
							
							cell.setIsClicked(true);
							cell.button().setIcon( cell.revealedIcon().path );
							List<Cell> neighbors = getNeighbors(cell);
							for (Cell neighbor : neighbors) {
								if (neighbor.type() == CellType.blank) chainBlanks(neighbor);
							}
							break;
						case MouseEvent.BUTTON3:
							CellPicture newIcon = cell.isFlagged() ? CellPicture.facingDown : CellPicture.flagged;
							cell.toggleIsFlagged();
							cell.button().setIcon(newIcon.path);
							break;
						}
					}
				});
			}
		}
		this.print(); //delete this later
	}
}
