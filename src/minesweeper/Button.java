package minesweeper;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton {
	Button() {
		super();
		this.setBackground(Color.black);
		ImageIcon icon = new ImageIcon("Images/facingDown.png");
		this.setIcon(icon);
	}
}
