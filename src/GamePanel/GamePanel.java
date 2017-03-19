package GamePanel;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	//Dimension
	public static final int WIDTH = 320;
	public static final int HEIGHT = 280;
	public static final int SCALE = 3;
	
	//Thread
	private Thread thread;
	private int FPS = 60;
	private int targetTime = 1000 / FPS;
	private boolean running;
	
	//image
	private BufferedImage image;
	private Graphics g;
	
	//game state manager
	private GameStateManager gsm;
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	public void init(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics();
		
		running = true;
		
		//initialized gsm
		gsm = new GameStateManager();
		
	}
	
	public void run(){
		
		init();
		
		while(running){
			
			long startTime = System.nanoTime();
			update();
			draw();
			drawToScreen();
			long elapsedTime = System.nanoTime() - startTime;
			long delay = targetTime - (elapsedTime / 1000000);
			
			
			try{
				Thread.sleep(delay);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void update(){
		gsm.update();
	}
	
	private void draw(){
		gsm.draw(g);
	}
	
	private void drawToScreen(){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key){
		//unused
	}
	
	public void keyPressed(KeyEvent key){
		gsm.keyPressed(key.getKeyCode());
	}
	
	public void keyReleased(KeyEvent key){
		gsm.keyReleased(key.getKeyCode());
	}
	
}
