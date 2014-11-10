package org;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;

public abstract class Weapons extends Background
{
	protected double power;
	protected double base;
	private double explosionLength;
	private Sound explosion;

	
	public boolean placeFree()
	{
		if(collisionElementList((int)x, (int)y, Type.BOMBERMAN, false).isEmpty())
			return true;
		else
			return false;
	}
	
	public void explode()
	{
		boolean l=false, r=false, t=false, b=false;
		Explosion ex=null;
		LinkedList<Element> list;
		for(int i = 0; i <= explosionLength; i++)
		{
			if(!r && !map.isWall((int)(x+32*i), (int)(y)))
			{
				ex = new Explosion(x+32*i,y,power*base,0.0, source);
				listElements.addElement(ex);
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.DESTROYABLE_WALL, false);
				if(!list.isEmpty())
				{
					if(list.get(0).alive)
					{
						pushEvent(list.get(0), "destroy");
						pushEvent(source, "wall xp");
					}
					r=true;
					ex.setBorder(true);
				}
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.WEAPONS, false);
				if(!list.isEmpty() && list.get(0) != this)
				{
					r=true;
					ex.setBorder(true);
				}
				if(i == explosionLength)
					ex.setBorder(true);
			}
			else
				r=true;
			if(!l && !map.isWall((int)(x-32*i), (int)(y)))
			{
				ex = new Explosion(x-32*i,y,power*base,180.0, source);
				listElements.addElement(ex);
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.DESTROYABLE_WALL, false);
				if(!list.isEmpty())
				{
					if(list.get(0).alive)
					{
						pushEvent(list.get(0), "destroy");
						pushEvent(source, "wall xp");
					}
					l=true;
					ex.setBorder(true);
				}
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.WEAPONS, false);
				if(!list.isEmpty() && list.get(0) != this)
				{
					l=true;
					ex.setBorder(true);
				}
				if(i == explosionLength)
					ex.setBorder(true);
			}
			else
				l=true;
			if(!t && !map.isWall((int)(x), (int)(y+32*i)))
			{
				ex = new Explosion(x,y+32*i,power*base,90.0, source);
				listElements.addElement(ex);
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.DESTROYABLE_WALL, false);
				if(!list.isEmpty())
				{
					if(list.get(0).alive)
					{
						pushEvent(list.get(0), "destroy");
						pushEvent(source, "wall xp");
					}
					t=true;
					ex.setBorder(true);
				}
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.WEAPONS, false);
				if(!list.isEmpty() && list.get(0) != this)
				{
					t=true;
					ex.setBorder(true);
				}
				if(i == explosionLength)
					ex.setBorder(true);
			}
			else
				t=true;
			if(!b && !map.isWall((int)(x), (int)(y-32*i)))
			{
				ex = new Explosion(x,y-32*i,power*base,270.0, source);
				listElements.addElement(ex);
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.DESTROYABLE_WALL, false);
				if(!list.isEmpty())
				{
					if(list.get(0).alive)
					{
						pushEvent(list.get(0), "destroy");
						pushEvent(source, "wall xp");
					}
					b=true;
					ex.setBorder(true);
				}
				list = ex.collisionElementList((int)ex.x, (int)ex.y, Type.WEAPONS, false);
				if(!list.isEmpty() && list.get(0) != this)
				{
					b=true;
					ex.setBorder(true);
				}
				if(i == explosionLength)
					ex.setBorder(true);
			}
			else
				b=true;
		}
		pushEvent(source,"MoreBomb");
		if(timerDeath==0 && onView()) {			
			explosion.play(1,0.5f);
		}
	
		destroy();
	}
	
	
	
	
	
	public Weapons(double x, double y, double power, double explosionLength, Element source) {
		super(x, y);
		type = Type.WEAPONS;
		this.power = power;
		this.explosionLength = explosionLength;
		this.source=source;
		this.explosion=getSound("explo");
	
		
	}	
}