package org;

import org.newdawn.slick.Sound;


public class C4 extends Weapons
{
	private double power;
	private boolean trigger;
	private Animation anim;
	private Sound C4Pose;
	
	public C4(double x, double y, double power, double explosionLength, Element source)
	{
		super(x, y,power, explosionLength, source);
		collisionRadius = 16;
		anim = new Animation(getAnimationData("C4"));
		trigger=false;
		anim.setSpeed(0.05);
		base = 1.5;
		(C4Pose=getSound("C4")).play();
	}
	



	public void preUpdate() {

		Event ev;
		boolean explode = false;
		while(eventExist())
		{
			ev = popEvent();
			if(ev.getName()=="C4" && ev.getSource()==source)
			{
				trigger=true;
			}
			
			if(ev.getName().equals("explode") && !explode)
			{
				explode();
				pushEvent(source, "C4 explode");
				explode = true;
			}
		}
		
	}


	public void update()
	{
		if(trigger==true && anim.getLap()==1)
		{
			explode();
			pushEvent(source, "C4 explode");
		}
		
		if(placeFree()) solid=true;
		
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender() {
		drawImage(anim.getImage(), (int)x, (int)y,0);
		if(anim.getId()>=2 && trigger==false)
		{
			anim.setId(0);
		}
		else if(trigger) anim.setSpeed(0.3);
		anim.update();
		
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
