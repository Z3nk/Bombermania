package org;

import org.newdawn.slick.Image;

public class MonsterBouftouBallHeal extends Element
{
	private Element cible;
	private Image img;
	public MonsterBouftouBallHeal(double x, double y, Element cible)
	{
		super(x, y);
		this.cible = cible;
		speed = 7+Math.random()*3;
		img = getImage("bouftou_ball");
	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}


	public void update()
	{
		if(cible.alive)
		{
			direction = this.directionToElement(cible);
			updateHVSpeed();
			x+=hspeed;
			y+=vspeed;
			if(this.distanceToElementPow(cible) <= (speed)*(speed))
			{
				pushEvent(cible, "heal monster");
				destroy();
			}
		}
		else
			destroy();
		
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render()
	{
		drawImage(img, (int)x, (int)y, 0);
	}

	@Override
	public void postRender() {
		// TODO Auto-generated method stub
		
	}
	
}
