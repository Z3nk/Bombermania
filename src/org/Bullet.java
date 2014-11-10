package org;

import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bullet extends Element{
	private Animation anim;
	private Image tir;
	private int rotation=0;
	private boolean destruction, explo;
	private double sourceX, sourceY;
	
	public Bullet(double x, double y, double rotation)
	{
		super(x, y);
		tir=getImage("tir_robot");
		sourceX=x;
		sourceY=y;
		anim=new Animation(getAnimationData("explo"));
		speed=20;
		explo=false;
		collisionRadius = 15;
		anim.setSpeed(1);
		//this.rotation=rotation;
		direction = rotation;
		destruction=false;
		// TODO Auto-generated constructor stub
		
		updateHVSpeed();
	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
		if(x<=16 || x>=map.getMapWidth()-16){
			destruction=true;
		}
		if( y<=16 || y>=map.getMapHeight()-16)
		{
			destruction=true;
		}
		/*	System.out.println("sa mere"+Math.abs(sourceX-x)+" " + Math.abs(sourceY-y) + " " +(Math.abs(sourceX-x)*Math.abs(sourceX-x)+Math.abs(sourceY-y)*Math.abs(sourceY-y)));
		if((Math.abs(x-sourceX)*Math.abs(x-sourceX)+Math.abs(y-sourceY)*Math.abs(y-sourceY))>320){
			destruction=true;
			
		
		}*/
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(destruction){
			
			destroy();
		}
		/*xVitesse = Math.cos(rotation*Math.PI/180.0)*vitesse;
		yVitesse = Math.sin(rotation*Math.PI/180.0)*vitesse;
		vitesse=Math.sqrt(xVitesse*xVitesse+yVitesse*yVitesse);*/
		LinkedList<Element> elementTouch;
		Element elem;
		if(!explo)
		{
			elementTouch= collisionElementList((int)x, (int)y, Type.ALL, false);
			
			if(elementTouch.isEmpty() && !map.isWall((int)x, (int)y) )
			{
				x+=hspeed;
				y+=vspeed;
			}
			else
			{
				
				
				if(map.isWall((int)x, (int)y))
				{
					if(x > 32 && y > 32 &&  x < map.getMapWidth()-32 && y < map.getMapHeight()-32)
					{
						try {
							map.destroyWall((int)x/32,(int)y/32);
						} catch (SlickException e) {
							e.printStackTrace();
						}
					}
					explo=true;
				}
				else
				{
					while(!elementTouch.isEmpty())
					{
						elem = elementTouch.removeFirst();
						if(elem.type == Type.BOMBERMAN){
							explo=true;
							pushEvent(elem,"robot", 20);
							
						}
							
						else if(elem.type == Type.MONSTER)
						{
							explo=true;
							pushEvent(elem,"robot", 1);
						}
							
						/*else if(elem.type == Type.WEAPONS){
							explo=true;
							pushEvent(elem,"explode");
						}*/
							
						else if(elem.type==Type.DESTROYABLE_WALL){
							explo=true;
							pushEvent(elem,"destroy");
						}
					}
				}
				if(!explo)
				{
					x+=hspeed;
					y+=vspeed;
				}
				
				
			}
		}
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender() {
		// TODO Auto-generated method stub
		if(!explo)drawImage(tir, (int)x, (int)y, (float)direction);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		if(explo){
			drawImage(anim.getImage(), (int)x, (int)y, 0);
			anim.update();
		}
		if(anim.getLap()==1)destroy();
		
	}

	@Override
	public void postRender() {
		// TODO Auto-generated method stub
		
	}

}
