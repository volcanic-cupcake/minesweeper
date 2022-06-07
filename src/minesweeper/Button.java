package minesweeper;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton {
	static int width;
	static int height;
	Button() {
		super();
		//this.setBackground(Color.black);
	}
	public void setIcon(String path) {
		Image image = new ImageIcon(path)
				.getImage()
				.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(image);
		super.setIcon(icon);
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
