package org;

import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class Trap extends Weapons
{
	private int timer;
	private Image open;
	private Image close;
	private boolean activated, ready;
	private Sound kwic;
	
	public Trap(double x, double y, double power, Element source)
	{
		super(x, y, power, 0, source);
		timer=900;
		ready=false;
		open=getImage("piege");
		close=getImage("piege2");
		base=5;
		activated=false;
		type = Type.TRAP;
		kwic=getSound("piege");
	}
	
	@Override
	public void preUpdate()
	{
		
	}

	@Override
	public void update()
	{
		if(ready)
		{
			timer--;
			LinkedList<Element> elementTouch= collisionElementList((int)x, (int)y, Type.BOMBERMAN, false);
			if(!elementTouch.isEmpty() && !activated)
			{
					activated=true;
					pushEvent(elementTouch.get(0),"trap", power*base);
					timer=120;
					if(timerDeath==0  && onView()) kwic.play(1,3f);
			}
			LinkedList<Element> elementTouch2= collisionElementList((int)x, (int)y, Type.ROBOT, false);
			if(!elementTouch2.isEmpty() && !activated)
			{
					activated=true;
					pushEvent(elementTouch2.get(0),"trap", power*base);
					timer=120;
					if(timerDeath==0  && onView()) kwic.play(1,3f);
			}
			
			
			if(timer<=0)
			{
				destroy();
			}
		}
		else if(placeFree())
			ready=true;
		
		/*Event ev;
		
		while(eventExist())
		{
			ev = popEvent();
			
			if(ev.getName().equals("explode"))
			{
				
				destroy(); 
			}
			
		}*/
	}

	@Override
	public void postUpdate()
	{

	}


	public void preRender()
	{
		if(!activated) drawImage(open, (int)x, (int)y, 0);
		
		else drawImage(close, (int)x, (int)y, 0);
	}

	@Override
	public void render()
	{

	}

	@Override
	public void postRender()
	{

	}
}
