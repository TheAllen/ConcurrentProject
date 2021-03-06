package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import Audio.AudioPlayer;
import GameState.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	 
	private static final long serialVersionUID = 1L;
	
	//Dimensions
	public static final int width = 320;
	public static final int height = 240;
	public static final int scale = 2;
	
	//Threads
	private Thread thread;
	private Thread audio;
	private boolean running;
	private int FPS = 60;
	private int targetTime = 1000 / FPS;
	public AtomicBoolean drawn;
	
	//image 
	private BufferedImage image;
	private Graphics2D g;
	
	private GameStateManager gsm;
	
	long start;
	long elapsed;
	long wait;
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(width*scale, height*scale));
		setFocusable(true);
		requestFocus();
		drawn = new AtomicBoolean(true);
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			//audio = new Thread(new AudioPlayer("/Music/MegaMan.mp3"));
			audio = new AudioPlayer("/Music/MegaMan.mp3");
			addKeyListener(this);
			audio.start();
			thread.start();
		}
	}
	
	public void init(){
		//initialize the things we need
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics();
				
		running = true;
		
		gsm = new GameStateManager();
		
	}
	
	public synchronized void update(){
		gsm.update();
	}
	
	public synchronized void draw(){
		gsm.draw(g);
	}
	
	public void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,width*scale, height*scale, null);
		g2.dispose();
	}
	
	public void run(){
		init();
		
		Thread updateThread = new Thread(new Runnable() {
			public void run() {
				while (running) {
					while (!drawn.get()) {};
					start = System.nanoTime();
					update();
					drawn.set(false);
				}
			}
		});
		
		Thread drawThread = new Thread(new Runnable() {
			public void run() {
				while (running) {
					while (drawn.get()) {};
					draw();
					drawn.set(true);
					drawToScreen();
					elapsed = System.nanoTime() - start;
					System.out.println(elapsed);
					wait = targetTime - elapsed / 1000000;
					if(wait < 0) wait = 5;
					try{
						Thread.sleep(wait);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		
		updateThread.start();
		drawThread.start();
		
	}
	
	public void keyTyped(KeyEvent key){
		
		
	}
	
	public void keyPressed(KeyEvent key){
		gsm.keyPressed(key.getKeyCode());
	}
	
	
	public void keyReleased(KeyEvent key){
		gsm.keyReleased(key.getKeyCode());
	}
}
	
	
	
	
	
	
