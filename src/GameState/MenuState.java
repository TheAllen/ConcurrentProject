package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class MenuState extends GameState{
	
	//we need the background for the menu
	private Background bg;
	
	private int currentChoice = 0;
	//keep track of our current choice
	private String[] options = {"Start", "Help", "Quit"};
		
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	
	public MenuState(GameStateManager gsm){
		
		this.gsm = gsm;
		
        try{
			
			bg = new Background("/Backgrounds/grassbg1.gif", 10);
			bg.setVector(0.3, 0);
			
			titleColor = new Color(0, 128, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Century Gothic", Font.PLAIN, 12);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void init(){
		
		
	}
	
	public void update(){
		bg.update();
	}
	
	public void draw(Graphics2D g){
		
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("MEGAMAN X", 80, 70);
		
		//draw menu options
		g.setFont(font);
		//use loop to loop all the options
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){
				g.setColor(Color.YELLOW);
			}
			else{
				g.setColor(Color.BLACK);
			}
			
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}
	
	private void select(){
		if(currentChoice == 0){
			//if selected start, go to the level1State
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1){
			//Help
		}
		if(currentChoice == 2){
			//Quit
			System.exit(0);
		}
		
	}
	
	public void keyPressed(int k){
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		
		if(k == KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1){
				currentChoice = options.length -1;
			}
		}
		
		if(k == KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == options.length){
				currentChoice = 0;
			}
		}
	}
	
	public void keyReleased(int k){
		
	}
}