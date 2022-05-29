package minesweeper;

public class Main {

	public static void main(String[] args) {
		Map map = new Map(30, 15, 90);
		map.generate(new Cell(0, 0));
		map.print();
		new Frame();
	}

}
