package org;

import org.newdawn.slick.Input;

public class Player extends Bomberman
{
	public Player(double x, double y)
	{
		super(x, y);
		player = this;
	}
	
	public String getName()
	{
		return "Joueur";
	}

	public void update()
	{
	    if(!editorMode)
	    {
	        commandLeft = gameContainer.getInput().isKeyDown(Input.KEY_LEFT);
	        commandRight = gameContainer.getInput().isKeyDown(Input.KEY_RIGHT);
	        commandUp = gameContainer.getInput().isKeyDown(Input.KEY_UP);
	        commandDown = gameContainer.getInput().isKeyDown(Input.KEY_DOWN);
		
	        commandC4=gameContainer.getInput().isKeyPressed(30);
	        commandBomb=gameContainer.getInput().isKeyDown(44);
	        commandTrap=gameContainer.getInput().isKeyPressed(18);
	        cacheraufond=gameContainer.getInput().isKeyPressed(197);
	        
	        if(gameContainer.getInput().isKeyPressed(50)) stopMusic = !stopMusic;
	        super.update();
	        if(hp<=0) playerDeath=true;
	    }
	}
}
