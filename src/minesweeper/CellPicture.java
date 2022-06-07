package minesweeper;

public enum CellPicture {
	c0("Images/0.png"),
	c1("Images/1.png"),
	c2("Images/2.png"),
	c3("Images/3.png"),
	c4("Images/4.png"),
	c5("Images/5.png"),
	c6("Images/6.png"),
	c7("Images/7.png"),
	c8("Images/8.png"),
	mine("Images/mine.png"),
	facingDown("Images/facingDown.png"),
	flagged("Images/flagged.png");
	
	String path;
	CellPicture(String path) {
		this.path = path;
	}
}
