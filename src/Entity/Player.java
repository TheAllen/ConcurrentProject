package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import TileMap.TileMap;

public class Player extends MapObject{
	
	private Animation animation;
	
	//player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTime;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	private boolean charging;
	
	//fireBall
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	//scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	//gliding
	private boolean gliding;
	
	//animations
	private ArrayList<BufferedImage[]> sprites;
	
	//originally 2 sprites for idle and 8 for walking
	private final int[] numFrames = {
		4, 10, 1, 1, 4, 2, 6
	};
	
	private final int[] frameWidths = {
		45, 45, 45, 45, 60, 45, 80
	};
	
	private final int[] frameHeights = {
		50, 50, 50, 50, 50, 50, 67	
	};
	
	//animation actions
	//enums
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	//private static final int DASHING = 7;
	
	//hashMap
	//string is the key and AudioPlayer is the value
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm){
		super(tm);
		//width and height originally 30
		// cwidth and cheight was 20
		width = 45;
		height = 50;
		cwidth = 30;
		cheight = 50;
		
		moveSpeed = 0.3;
		//originally 1.6
		maxSpeed = 2.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -3.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3.0;
		
		facingRight = true;
		
		health = maxHealth = 2;
		fire = maxFire = 25;
		
		fireCost = 2;
		fireBallDamage = 5;
		//damage
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 50;
		
		
		//load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/MegaManZeroF.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			
			
			for(int i = 0; i < 7; i++){
				//since 7 animation actions
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				//use bi to convert it to array of BufferedImages
				for(int j = 0; j < numFrames[i]; j++){
					//draw through all the elements in each of the numFrames
					if(i == 5){
						//added 47
						width = 47;	
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					}
					else if(i == 6){
						width = 80;
						
						//height = 68; width = 154
						bi[j] = spritesheet.getSubimage(j * width, i * height + 19, width, height);
					}				
					else if(i == 4){
						width = 60; //width = 60
					    bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					}
					else{

						bi[j] = spritesheet.getSubimage(j * width, i * height, width , height);
					}
				}
				sprites.add(bi);
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public int getFire(){
		return fire;
	}
	
	public int getMaxFire(){
		return maxFire;
	}
	
	public void setFiring(){
		firing = true;
	}
	
	public void setScratching(){
		scratching = true;
	}
	
	public void setGliding(boolean b){
		//we have gliding as boolean because we can stop whenever
		gliding = b;
	}
	
	public void setJumping(boolean b){
		//had !jumping
		if(b  && falling && !alreadyDoubleJump){
			doubleJump = true;
		}
		jumping = b;
	}
	
	
	public void checkAttack(ArrayList<Enemy> enemies){
		
		for(int i = 0; i < enemies.size(); i++){
			
			Enemy e = enemies.get(i);
			
			//scratch attack
			if(scratching){
				if(facingRight){
					if(e.getX() > x && 
					   e.getX() < x + scratchRange && 
					   e.getY() > y - height / 2 &&
					   e.getY() < y + height / 2)
					{	
					     e.hit(scratchDamage);				
					}
				}
				else{
					if(e.getX() < x &&
					   e.getX() > x - scratchRange &&
					   e.getY() > y - height / 2 &&
					   e.getY() < y + height / 2)
					        {
								e.hit(scratchDamage);
							}
				}
			}
			
			//fireBalls
			for(int j = 0; j < fireBalls.size(); j++){
				if(fireBalls.get(j).intersects(e)){
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
			//check enemy collision
			if(intersects(e)){
				hit(e.getDamage());
			}
		}

	}
	
	public void hit(int damage){
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTime = System.nanoTime();
	}
	
	private void getNextPosition(){
		
		//movement
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		else if (right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		else{
			if(dx > 0){
				dx -= stopSpeed;
				if(dx < 0){
					dx = 0;
				}
			}
		else if (dx < 0){
				dx += stopSpeed;
				if(dx > 0){
					dx = 0;
				}
			}
		}
		
		//cannot move while attacking
		if(currentAction == SCRATCHING || currentAction == FIREBALL && !(jumping || falling)){
			dx = 0;
			//added
			//dy = 0;
		}
		
		//jumping
		if(jumping && !falling){
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
					
		}
		
		//doubleJump
		if(doubleJump){
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
		}
		
		if(!falling){
			alreadyDoubleJump = false;
		}
		
		//falling
		if(falling){
			
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;

		}
		
	}
	
	public void update(){
		
		//Animation animation = new Animation();
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check attack has stopped
		if(currentAction == SCRATCHING){
			if(animation.hasPlayedOnce()) scratching = false;
		}
		
		if(currentAction == FIREBALL){
			if(animation.hasPlayedOnce()) firing= false;
		}
		
		//fireball attack
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL){
			if(fire > fireCost){
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x,y);
				fireBalls.add(fb);
			}
		}
		
		//update fireballs
		for(int i = 0; i < fireBalls.size(); i++){
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()){
				fireBalls.remove(i);
				i--;
			}
		}
		
		//check done flinching
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed > 1000){
				flinching = false;
			}
		}
		
		
		//set animation 
		if(scratching){
			if(currentAction != SCRATCHING){
				sfx.get("scratch").play();
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(20);
				width = 80;
				//setting cheight to 50
				//cheight = 50;
			}
		}
		else if (firing){
			if(currentAction != FIREBALL){
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				//originally 30
				width = 45;
			}
			
		}
		else if(dy > 0){
			if(gliding){
				if(currentAction != GLIDING){
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(80);
					width = 60;
					//was 30
				}
			}
			else if(currentAction != FALLING){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 45;
			}
		}
		else if(dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 45;
			}
		}
		else if(left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				//width = 30
				width = 45;
			}
		}
		else{
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 45;
			}
		}
		
		animation.update();
		
		//set direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL){
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		//draw fireballs
		for(int i = 0; i < fireBalls.size(); i++){
			fireBalls.get(i).draw(g);
		}
		
		//draw player
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed / 100 % 2 == 0){
				return;
			}
		}
		
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
					
		
		
			
		}
		
	}
	
	
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

