package Entity;


import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class MapObject{
	
	//tile size
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	
	//positions and vectors
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimension
	protected int width;
	protected int height;
	
	//collision box
	protected int cwidth;
	protected int cheight;
	
	//collision
	protected int currCol;
	protected int currRow;
	protected double xtemp;
	protected double ytemp;
	protected double xdest;
	protected double ydest;
	
	//boundaries
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//direction
	protected boolean facingRight;
	
	//movements
	protected boolean left;
	protected boolean right;
	protected boolean down;
	protected boolean up;
	protected boolean falling;
	protected boolean jumping;
	
	//movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	
	public MapObject(TileMap tileMap){
		this.tileMap = tileMap;
		tileSize = tileMap.getTileSize();
	}
	
	public boolean intersects(MapObject object){
		Rectangle r1 = getRectangle();
		Rectangle r2 = object.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle(){
		return new Rectangle((int)x - cwidth / 2, (int)y - cheight / 2, cwidth, cheight);
	}
	
	public void calculateCorners(double x, double y){
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 + 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 + 1) / tileSize;
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	public void checkTileMapCollision(){
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xtemp = x;
		ytemp = y;
		
		xdest = x + dx;
		ydest = y + dy;
		
		calculateCorners(xdest, y);
		
		if(dx < 0){
			if(bottomLeft || topLeft){
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}else{
				xtemp += dx;
			}
		}
		
		if(dx > 0){
			if(bottomRight || topRight){
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}else{
				xtemp += dx;
			}
		}
		
		calculateCorners(x, ydest);
		
		if(dy > 0){
			if(bottomLeft || bottomRight){
				dy = 0;
				falling = false;
				ytemp = currRow * tileSize - cheight / 2;
			}else{
				ytemp += dy;
			}
		}
		
		if(dy < 0){
			if(topLeft || topRight){
				dy = 0;
				ytemp = (currRow + 1) * tileSize + cheight / 2;
			}else{
				ytemp += dy;
			}
		}
		
		if(!falling){
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight){
				falling = true;
			}
		}
		
		
	}
	
	public int getX(){
		return (int)x;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getCWidth(){
		return cwidth;
	}
	
	public int getCHeight(){
		return cheight;
	}
	
	public void setLeft(boolean left){
		this.left = left;
	}
	
	public void setRight(boolean right){
		this.right = right;
	}
	
	public void setDown(boolean down){
		this.down = down;
	}
	
	public void setUp(boolean up){
		this.up = up;
	}
	
	public void setFalling(boolean falling){
		this.falling = falling;
	}
	
	public void setJumping(boolean jumping){
		this.jumping = jumping;
	}
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void setVectors(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition(){
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	public boolean notOnScreen(){
		return (x + xmap + width < 0 
			|| x + xmap - width > GamePanel.WIDTH
			|| y + ymap + height < 0
			|| y + ymap - height > GamePanel.HEIGHT);
	}
	
	public void draw(Graphics2D g){
		
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
	}
}