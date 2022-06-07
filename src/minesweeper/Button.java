package minesweeper;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton {
	Button() {
		super();
		//this.setBackground(Color.black);
		ImageIcon icon = new ImageIcon("Images/facingDown.png");
		this.setIcon(icon);
	}
	public void setMouseListener(MouseListener listener) {
		clearMouseListeners();
		addMouseListener(listener);
	}
	public void clearMouseListeners() {
		MouseListener[] listeners = this.getMouseListeners();
		for (MouseListener listener : listeners) {
			this.removeMouseListener(listener);
		}
	}
}
