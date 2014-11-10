package org;

import java.util.LinkedList;

import org.newdawn.slick.Image;

public class Explosion extends Element
{
	private Image img;
	private boolean stop;
	private int i,j;
	private Animation anim;
	private Animation animBorder;
	private boolean border;
	private double power;
	
	public void setBorder(boolean border)
	{
		this.border = border;
	}
	
	public Explosion(double x, double y, double power, double direction, Element source) {
		super(x, y);
		type=Type.EXPLOSION;
		img=getImage("explosion2");
		stop=false;
		collisionRadius=15;
		anim = new Animation(getAnimationData("explosion_line"));
		animBorder = new Animation(getAnimationData("explosion_border"));
		anim.setSpeed(0.5);
		animBorder.setSpeed(0.5);
		this.direction=direction;
		border=false;
		this.power = power;
		this.source=source;
	}


	public void preUpdate()
	{
		LinkedList<Element> elementTouch = collisionElementList((int)x, (int)y, Type.ALL, false);
		Element elem;
		while(!elementTouch.isEmpty())
		{
			elem = elementTouch.removeFirst();
			if(elem.type == Type.BOMBERMAN)
				pushEvent(elem,"explosion", power);
			if(elem.type == Type.ROBOT)
				pushEvent(elem,"explosion", power);
			if(elem.type == Type.MONSTER)
				pushEvent(elem,"explosion", power);
			else if(elem.type == Type.WEAPONS)
				pushEvent(elem,"explode");
		}
		
		if(anim.getLap() == 1 || animBorder.getLap() == 1)
			destroy();
	}

	public void update()
	{
		
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender() {
		if(!border)
		{
			drawImage(anim.getImage(), (int)x, (int)y,(float)direction);
			anim.update();
		}
		if(border)
		{
			drawImage(animBorder.getImage(), (int)x, (int)y,(float)direction);
			animBorder.update();
		}
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
