package Main;

import javax.swing.JFrame;

public class Game{
	//Main class that runs everything
	public static void main(String[] args){
		JFrame frame = new JFrame("Game");
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}