package org;

import org.newdawn.slick.Input;

public class View extends Global
{
	private double x, y;
	private Element focus;
	
	public View()
	{
		x=0; y=0;
		focus = null;
	}
	
	public View(Element focus)
	{
		this.focus = focus;
		update();
	}
	
	public void setFocus(Element focus)
	{
		this.focus = focus;
	}
	
	public void update()
	{
		
		if(focus != null && focus.alive())
		{
				x = focus.getX() - windowWidth/2;
				y = focus.getY() - windowHeight/2;
				mapExcess();
		}
		else if(playerDeath || focus == null)
		{
			if(gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) x -= 20;
			if(gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) x += 20;
			if(gameContainer.getInput().isKeyDown(Input.KEY_UP)) y -= 20;
			if(gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) y += 20;
			mapExcess();
		}
		
	}
	
	public int getX()
	{
		return (int)x;
	}
	
	public int getY()
	{
		return (int)y;
	}
	
	private void mapExcess()
	{
		if(x+windowWidth > map.getMapWidth())
			x = map.getMapWidth() - windowWidth;
		else if(x < 0)
			x = 0;
		if(y+windowHeight > map.getMapHeight())
			y = map.getMapHeight() - windowHeight;
		else if(y < 0)
			y = 0;
	}
}
