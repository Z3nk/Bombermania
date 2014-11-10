package org;

import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class Bonus extends Background
{
	private int id, timer, rand;
	private Image img;
	private boolean activated;
	private Sound banzai, rire;

	public Bonus(double x, double y)
	{
		super(x, y);
		type=Type.BONUS;
	
		rand=(int) (Math.random()*100);
		
		if(rand<5){ //5
			id=4;
		}
		else if(rand<11){ //6
			id=1;
		}
		else if(rand<21){ //9
			id=0;
		}
		else if(rand<36){ //15
			id=6;
		}
		else if(rand<54){ //18
			id=5;
		}
		else if(rand<73){ //19
			id=3;
		}
		else{ //27
			id=2;
		}
		
		
		
		
		
		
		
	
		
		img=getImage("bonus"+(id));
		solid=false;
		timer=0;
		activated=false;
		collisionRadius=10;
		int var=(int)(Math.random()*5);
		if(var==1)banzai=getSound("banzai");
		else if(var==2) banzai=getSound("banzai2");
		else if(var==3) banzai=getSound("banzai4");
		else if(var==4) banzai=getSound("banzai5");		
		else banzai=getSound("banzai6");
		
		rire=getSound("rire");
	}
	public Bonus(double x, double y, int id) {
		super(x, y);
		type=Type.BONUS;
		this.id=id;
		img=getImage("bonus"+(id));
		solid=false;
		timer=0;
		activated=true;
		int var=(int)(Math.random()*5);
		if(var==1)banzai=getSound("banzai");
		else if(var==2) banzai=getSound("banzai2");
		else if(var==3) banzai=getSound("banzai4");
		else if(var==4) banzai=getSound("banzai5");		
		else banzai=getSound("banzai6");
		
		rire=getSound("rire");
	}
	
	public int getEvaluationAI()
	{
		if(id == 5)
			return AICalculator.HEAL_VALUE;
		return 0;
	}

	@Override
	public void preUpdate() {
		
		
		if(activated==false){
			timer++;
			if (timer>=45)
			{
				activated=true;
			}
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		LinkedList<Element> elementTouch = collisionElementList((int)x, (int)y, Type.ALL, false);
		Element elem;
		while(!elementTouch.isEmpty())
		{
			elem = elementTouch.removeFirst();
			if(elem.type == Type.BOMBERMAN){
			if(id==0){
				
				pushEvent(elem,"IncC4");
				destroy();
			
			}
			
			if(id==1){
				pushEvent(elem, "IncTrap");
				destroy();
			}
			
			if(id==2){
				pushEvent(elem, "MoreBomb");
				destroy();
			}
			if(id==3){
				
				pushEvent(elem, "BoostBomb");
				destroy();
			}
			if(id==4){
				pushEvent(elem, "Shield");
				destroy();
			}
			if(id==5){
				if(timerDeath==0 && onView()) rire.play(1, 1);
				pushEvent(elem, "Heal");
				destroy();
			}
			if(id==6){
				if(timerDeath==0  && onView()) banzai.play(1.5f, 1);
				pushEvent(elem, "BoostSpeed");
				destroy();
			}
		
			}
			else if(elem.type==Type.EXPLOSION && activated){
				destroy();
			}
		}
		
		
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender() {
		drawImage(img, (int)x, (int)y, 0);
		// TODO Auto-generated method stub
		
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