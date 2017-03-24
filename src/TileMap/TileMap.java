package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap{
	
	//position
	private double x;
	private double y;
	
	//bounds
	private int xmin;
	private int xmax;
	private int ymin;
	private int ymax;
	
	//tween
	private double tween;
	
	//map
	private int[][] map;
	private int tileSize;
	private Tile[][] tiles;
	
	//tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private int numCols;
	private int numRows;
	private int width;
	private int height;
	
	//drawing
	private int colOffset;
	private int rowOffset;
	private int numColsToDraw;
	private int numRowsToDraw;
	
	public TileMap(int tileSize){
		this.tileSize = tileSize;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		tween = 0.7d;
	}
	
	public void setTween(double tween){
		this.tween = tween;
	}
	
	public void loadTile(String path){
		try{
			tileset = ImageIO.read(getClass().getResourceAsStream(path));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++){
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadMap(String path){
		try{
			InputStream in = getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++){
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++){
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getTileSize(){
		return tileSize;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getType(int row, int col){
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	private void fixbounds(){
		if(x < xmin) x = xmin;
		if(x > xmax) x = xmax;
		if(y < ymin) y = ymin;
		if(y > ymax) y = ymax;
	}
	
	public void setPosition(double x, double y){
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixbounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	public void draw(Graphics2D g){
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++){
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);												
			}
		}
	}
}