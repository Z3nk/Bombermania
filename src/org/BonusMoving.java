package org;

import java.util.LinkedList;

import org.newdawn.slick.Image;

public class BonusMoving extends Element {
	private boolean activated, destruction;
	private double xWanted, yWanted;
	private int id, timer;
	private Image img;
	public BonusMoving(double x, double y, int direction, int id) {
		super(x, y);
		this.id=id;
		activated=false;
		timer=(int)(Math.random()*25+10);
		this.direction=direction;
		img=getImage("bonus"+(id));
		speed = 12;
		updateHVSpeed();
		destruction=false;
		// TODO Auto-generated constructor stub
	}

	private boolean placeFree()
	{
		if(collisionElementList((int)x, (int)y, Type.BOMBERMAN, false).isEmpty())
			return true;
		else
			return false;
	}
	
	@Override
	public void preUpdate() {
		if(destruction) destroy();
		if(x<=16 || x>=map.getMapWidth()-16 ) hspeed=-hspeed;
		if( y<=16 || y>=map.getMapHeight()-16) vspeed=-vspeed;
		
		
		if(!activated && placeFree()  ) {
			
			activated=true;
		}
		// TODO Auto-gene rated method stub
	
		}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		x+=hspeed;
		y+=vspeed;
		timer--;
		

		if(activated && timer<=0 && positionFree( (int)x,  (int)y) /*&& alignedToGrid(32)*/){
			speed=0;
			x=((int)(x/map.getBlockWidth()))*map.getBlockWidth()+16;
			y=((int)(y/32.0))*32+16;
			listElements.addElement(new Bonus(x,y, id));
			destruction=true;
		}
		
		//if(timer<=-600) destruction=true;
		

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
