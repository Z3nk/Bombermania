package org;

import java.util.LinkedList;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

public class ExempleMonstre extends Element
{
	private double taille;
	private Element focus;
	
	private Animation anim;
	
	public ExempleMonstre(double x, double y)
	{
		super(x, y);

		taille = 1.0;
		type = Type.MONSTER;
		anim = new Animation(getAnimationData("bouftou"));
		anim.setSpeed(0.7);
		collisionRadius = 9;
		speed = 5;
		solid = true;
	}

	public void preUpdate()
	{
	}

	public void update()
	{
		int n;
		double min = 9999999999999.0;
		double dist = 0.0;
		Element e;
		focus = null;
		
		n = listElements.size();
		for(int i = 0; i < n; i++)
		{
			e = listElements.get(i);
			if(e.type == Type.BOMBERMAN && (dist = distanceToElementPow(e)) < min)
			{
				focus = e;
				pushEvent(focus, "jveux te boufer !");
				min = dist;
			}
		}
		if(focus != null && focus.alive())
		{
			direction = directionToElement(focus);
			if(collisionElementRect((int)(x+hspeed), (int)(y+vspeed), focus))
				pushEvent(focus, "taille-");
		}
		
		
		updateHVSpeed();
		forwardStepByStep(false);
	}

	public void postUpdate()
	{
		Event ev;
		
		while(eventExist())
		{
			ev = popEvent();
			
			if(ev.getName().equals("mort"))
				taille +=0.02;
		}
	}

	public void preRender()
	{
		
	}

	public void render()
	{

		drawImage(anim.getImage().getScaledCopy((float)taille), (int)x, (int)y, (float)direction);
		anim.update();
	}

	public void postRender()
	{
	}

}
