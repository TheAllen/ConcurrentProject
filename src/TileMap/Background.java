package TileMap;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background{
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;

	public Background(String path, double ms){
		
		try{			
			image = ImageIO.read(getClass().getResourceAsStream(path));
			moveScale = ms;
			//we set the move scroll to 1
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y){
		
		this.x = (x * moveScale) % GamePanel.width;
		this.y = (y * moveScale) % GamePanel.height;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update(){
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g){
		//draw background
		g.drawImage(image, (int)x, (int)y, null);
		//make sure it is always filling the screen
		
		if(x < 0){
			g.drawImage(image, (int)x + GamePanel.width, (int)y, null);
			return;
		}
		if(x > 0){
			g.drawImage(image, (int)x - GamePanel.width, (int)y, null);
		}
		
	}

}
