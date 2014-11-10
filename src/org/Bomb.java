package org;

public class Bomb extends Weapons
{
	private Animation anim;

	
	public Bomb(double x, double y, double power, double explosionLength, Element source)
	{
		super(x, y, power, explosionLength, source);
		collisionRadius = 16;
		anim = new Animation(getAnimationData("bomb"));		
		anim.setSpeed(0.1);
		base= 1;
	}

	public void preUpdate()
	{
		
	}

	@Override
	public void update()
	{
		if(anim.getLap()==1)
		{
			explode();
		}
		if(placeFree())
			solid = true;
		
		Event ev;
		
		boolean explode = false;
		while(eventExist() && !explode)
		{
			ev = popEvent();
			
			if(ev.getName().equals("explode"))
			{
				
				explode();				
				explode = true;
			}
			
		}
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender() {
		drawImage(anim.getImage(), (int)x, (int)y,0);
		anim.update();
		
	}

	@Override
	public void render() {
		
	}

	@Override
	public void postRender() {
		// TODO Auto-generated method stub
		
	}

}
