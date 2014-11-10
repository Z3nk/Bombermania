package org;

import java.util.Vector;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class Robot extends Alive{
	private double xPos, yPos;
	private int dataErrorTimer=0;
	private static int speedrobot=2;
	private double hp;
	private Animation canon, pied;
	private Image corps, canon1, piedArret;
	private boolean onBattle=false;
	private Vector<Element> ennemy;
	private int timerTrap;
	private boolean underTrap;
	private Sound target, dataError;
	private Element lastTarget;
	
	
	public Robot(double x, double y) {
		super(x, y);
		dataError=getSound("robot");
		target=getSound("robot2");
		timerTrap=0;
		underTrap=false;
		hp = 600;
		type = Type.ROBOT;
		collisionRadius = 15;
		canon=new Animation(getAnimationData("canon"));
		canon.setSpeed(1);
		pied=new Animation(getAnimationData("pied"));

		corps=getImage("corps");
		canon1=getImage("canon1");
		piedArret=getImage("pied1");
		pied.setSpeed(0.5);
		ennemy = new Vector<Element>();
		lastTarget=null;
		Element e;
		for(int i = 0; i < listElements.size(); i++)
		{
			
			e = listElements.get(i);
			if(e.getType() == Type.BOMBERMAN || e.getType()==Type.MONSTER)
			{
				
				ennemy.add(e);
			}
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preUpdate() {
	Event ev;
	boolean expDamage = false;
	
	while(eventExist())
	{
		
		ev = popEvent();
		
		if(ev.getName().equals("explosion"))
		{
			if(!expDamage)
			{
				hp-=5;
				expDamage = true;
				
				if(hp <= 0 && alive)
				{
					destroy();
					
				}
			}
		}
		if(ev.getName().equals("trap")){
			timerTrap=120;
			underTrap=true;
			hp-=ev.getValue();
		}
	}	
}


	@Override
	public void update() {
		//if(alignedToGrid((int)6))
	//	{
			if(!underTrap){
			Element best = null;
			double bestDist=0;
			for(int i = 0; i < ennemy.size(); i++)
			{
				if(!ennemy.get(i).alive){
					dataError.play();
					dataErrorTimer=90;
					ennemy.remove(i);
				}
					
				else
				{
					double dist = distanceToElementPow(ennemy.get(i));
					if(dist < bestDist || best==null){
						best = ennemy.get(i);
						bestDist = dist;					
					}
					
				}
			}
			if(best != null)
			{
				
				/*boolean commandLeft=false, commandRight=false, commandUp=false, commandDown=false;
				if(best.x < x) commandLeft = true;
				else if(best.x > x) commandRight = true;
				if(best.y < y) commandUp = true;
				else if(best.y > y) commandDown = true;
				
				setWanDirCommand(commandLeft, commandRight, commandUp, commandDown);*/
				try{
					direction=(int) directionToElement(best);
				}catch(Exception e){
				}
			}
			
			onBattle=true;
		
				
			
			if(dataErrorTimer>0)dataErrorTimer--;
			
			//else onBattle=false;
	//	}
			if(timerDeath==0  && onView() && lastTarget!=best && dataErrorTimer==0) {
				lastTarget=best;
				target.play(1, 1);
			}
			/*if(timerDeath==0  && onView() && best==null) {
			dataError.play();
			}*/
		speed = speedrobot;
		//moveGrid();
		
		updateHVSpeed();
		forwardStepByStep(false);
		}
		
	}
	
	/*public boolean roadFree(int x, int y)
	{
		int signeX = 0;
		int signeY = 0;
		if(curDir == RIGHT)
			signeX=1;
		if(curDir == DOWN)
			signeY=-1;
		if(curDir == LEFT)
			signeY=-1;
		if(curDir == UP)
			signeY=1;
		
		if((int)(this.x/32)==x || (int)(this.y/32)==y){
			for(int i=0;i<10;i++){
				if(map.isWall((int)((this.x+32*i*signeX)/32), (int)((this.y+32*signeY*i)/32))) return false;
				}
		}
		return true;
		
	
	}*/
	@Override
	public void postUpdate() {
		if(!underTrap){
		xPos = 12*Math.cos((direction-90.0)/180*Math.PI);
		yPos = 12*Math.sin((direction-90.0)/180*Math.PI);
		if(onBattle && canon.getId()==2){

			listElements.addElement(new Bullet(x-xPos,y-yPos, direction));
		}
		if(onBattle && canon.getId()==6){
			
			listElements.addElement(new Bullet(x+xPos,y+yPos, direction));
		}
		// TODO Auto-generated method stub
		}
		else {
			onBattle=false;
			if(timerTrap>0)timerTrap--;
			else underTrap=false;
		}
	}

	@Override
	public void preRender() {
		if(isMoving()){
			if(onBattle){
				drawImage(pied.getImage(), (int)x, (int)y, (float)direction);
				pied.update();
				
				}
			else drawImage(piedArret, (int)x, (int)y, (float)direction);
		}
	}

	@Override
	public void render() {
	
		/*if(curDir == RIGHT)
			rotation = 0;
		else if(curDir == DOWN)
			rotation = 90;
		if(curDir == LEFT)
			rotation = 180;
		if(curDir == UP)
			rotation = 270;*/
		
		if(onBattle){
		drawImage(canon.getImage(), (int)x, (int)y,(float)direction);
		canon.update();
		drawImage(corps, (int)x, (int)y, (float)direction);
		}
		else{
			
			drawImage(canon1, (int)x, (int)y, (float)direction);
			drawImage(corps, (int)x, (int)y, (float)direction);
		}
		
	}

	@Override
	public void postRender() {
		// TODO Auto-generated method stub
		
	}

}
