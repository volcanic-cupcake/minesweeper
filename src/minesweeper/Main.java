package minesweeper;

public class Main {

	public static void main(String[] args) {
		Map map = new Map(9, 9, 10);
		map.generate(new Cell(0, 0));
	}

}
