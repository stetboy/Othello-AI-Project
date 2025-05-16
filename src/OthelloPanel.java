import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class OthelloPanel  extends JFrame{
	
	private static final int WINDOW_WIDTH = 500;   // pixels
	private static final int WINDOW_HEIGHT = 500;  // pixels

	private JButton[][] panels = new JButton[10][10];
	private JPanel mainPanel = new JPanel();
	private ImageIcon bCircle, wCircle;
    
	public OthelloPanel()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300,300);

		bCircle = new ImageIcon("blackCircle.png");
		wCircle = new ImageIcon("whiteCircle.png");
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setLayout(new GridLayout(10,10,2,2));
		for(int r=0; r<10; r++)
			for(int c=0; c<10; c++) {
				panels[r][c] = new JButton();
				panels[r][c].setBackground(Color.WHITE);
				mainPanel.add(panels[r][c]);
			}
		panels[4][4].setIcon(bCircle);
		panels[5][5].setIcon(bCircle);
		panels[5][4].setIcon(wCircle);
		panels[4][5].setIcon(wCircle);
		for(int c=1; c<=8; c++) {
			panels[0][c].setText(""+c);
			panels[9][c].setText(""+c);
			panels[c][0].setText(""+c);
			panels[c][9].setText(""+c);
		}
		add(mainPanel);
		setTitle("Othello");
	}
	
	public void setSquare(int r, int c, int color)
	{
		if (color == OthelloBoard.BLACK)
			panels[r][c].setIcon(bCircle);
		else
			panels[r][c].setIcon(wCircle);
		repaint();
	}
	
	public static void main(String [] args)
	{
		OthelloPanel gui = new OthelloPanel();
		gui.setVisible(true);
	}
}
