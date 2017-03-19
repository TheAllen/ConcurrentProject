package GamePanel;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args){
		
		init();
	}
	
	private static void init(){
		JFrame window = new JFrame("Concurrent Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.setVisible(true);
		window.pack();
		window.setResizable(false);
	}
}
