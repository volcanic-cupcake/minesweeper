package minesweeper;

public class PanelSize {
	private int width;
	private int height;
	
	PanelSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int width() {
		return this.width;
	}
	public int height() {
		return this.height;
	}
}
