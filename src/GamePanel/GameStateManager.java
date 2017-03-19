package GamePanel;

import java.awt.Graphics;
import java.awt.Graphics2D;

import GameStates.MenuState;

public class GameStateManager {
	
	private GameState[] gameState;
	private int currentState;
	
	public static final int NUMOFSTATES = 2;
	public static final int MENUSTATE = 0;
	public static final int LEVELONE = 1;
	
	public GameStateManager(){
		gameState = new GameState[NUMOFSTATES];
		currentState = MENUSTATE;
	}
	
	private void loadState(int state){
		
		switch(state){
		case 0:
			gameState[MENUSTATE] = new MenuState(this);
			break;
		case 1:
			gameState[LEVELONE] = new LevelOne(this);
			break;
		default:
			break;
				
		}
	}
	
	private void unloadState(int state){
		gameState[state] = null;
	}
	
	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void update(){
		gameState[currentState].update();
	}
	
	public void draw(Graphics2D g){
		try{			
			gameState[currentState].draw(g);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void keyPressed(int key){
		gameState[currentState].keyPressed(key);
	}
	
	public void keyReleased(int key){
		gameState[currentState].keyReleased(key);
	}
}
