package Entity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import GameState.Level1State;

public class HUD {

	private Player player;
	private Level1State s;
	private BufferedImage image;
	private Font font;
	
	public HUD(Player player, Level1State s){
		this.player = player;
		this.s = s;
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud1.gif"));
			font = new Font("Century Gothics", Font.PLAIN, 14);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g){
		
		g.drawImage(image, 0, 0, null);
		g.setFont(font);
		Thread drawHealth = new Thread(new Runnable() {
			public void run() {
				g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 15);
			}
		});
		Thread drawFire = new Thread(new Runnable() {
			public void run() {
				g.drawString(player.getFire() + "/" + player.getMaxFire(), 20, 35);
			}
		});
		Thread drawEnemyCount = new Thread(new Runnable() {
			public void run() {
				g.drawString(s.getEnemyCount() + "/" + "5", 30, 55);
			}
		});
		
		drawHealth.start();
		drawFire.start();
		drawEnemyCount.start();
		try {
			drawHealth.join();
			drawFire.join();
			drawEnemyCount.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
