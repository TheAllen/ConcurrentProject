package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Slugger extends Enemy{
	
	private BufferedImage[] sprites;
	
	public Slugger(TileMap tileMap){
		super(tileMap);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 25;
		
		health = maxHealth = 2;
		damage = 1;
		
		//load Sprites
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/slugger.gif"));
			
			sprites = new BufferedImage[3];
			
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(200);
		
		right = true;
		facingRight = true;
	}

	private void getNextPosition(){
		
		//moving back and forth
		if(left){
			
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		
		else if(right){
			
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		
		//falling
		if(falling){
			dy += fallSpeed;
		}
	}
	
	public void update(){
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check flinching
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400){
				flinching = false;
			}
		}
		
		//if hits walls it goes back
		if(right && dx == 0){
			right = false;
			left = true;
			facingRight = false;
		}
		
		else if(left && dx == 0){
			
			right = true;
			left = false;
			facingRight = true;
		}
		
		//update animation
		animation.update();
		
		
	}
	
	public void draw(Graphics2D g){
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
		
		/*
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
		*/
	}
}
