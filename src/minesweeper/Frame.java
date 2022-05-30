package minesweeper;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Frame extends JFrame {
	JButton button;
	JButton button2;
	
	Frame(int width, int height) {

		/*button2 = new JButton();
		button2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
				}
			}
		});*/
		super();
		this.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(width, height);

	}
}
