package GameStates;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import GamePanel.GamePanel;

public class Background {

	
	private BufferedImage image;
	
	//position
	private int x;
	private int y;
	
	//vectors
	private int dx;
	private int dy;
	private int moveScale;
	
	public Background(String path, int moveScale){
		this.moveScale = moveScale;
		try{
			image = ImageIO.read(getClass().getResourceAsStream(path));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPosition(int x, int y){
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(int dx, int dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update(){
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g){
		
		g.drawImage(image, x, y, null);
		
		if(x < 0){
			g.drawImage(image, x + GamePanel.WIDTH, y, null);
		}
		
		else if(x > 0){
			g.drawImage(image, x - GamePanel.HEIGHT, y, null);
		}
	}
	
}
