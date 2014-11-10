package org;

import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class DestroyableWall extends Background
{
	private Image img;
	private int popBonus;
	private LinkedList<Element> elementTouch;
	private Sound bouftou;
	
	public DestroyableWall(double x, double y)
	{
		super(x, y);
		type = Type.DESTROYABLE_WALL;
		solid = true;
		collisionRadius=16;
		img = getImage("mur");
		popBonus=0;
		bouftou=getSound("bouftou");
	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		
		// TODO Auto-generated method stub
		Event ev;
		while(eventExist())
		{
			ev = popEvent();
			if(ev.getName().equals("destroy"))
			{
				if(alive())
				{
					popBonus=(int) (Math.random()*100);
					if(popBonus < 50)
						listElements.addElement(new Bonus(x,y));
					else if(popBonus < 55 && timerBoufTou<=0){
						
						listElements.addElement(new MonsterBouftou(x,y));
						//listElements.addElement(new robot(x,y));
						if(timerDeath==0 && onView()) bouftou.play(3, 2);
					}
				}
				destroy();
			}
		}
	}

	@Override
	public void postUpdate() {
		
		
	}

	@Override
	public void preRender() {
		drawImage(img, (int)x, (int)y, 0);
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postRender() {
		// TODO Auto-generated method stub
		
	}

}
