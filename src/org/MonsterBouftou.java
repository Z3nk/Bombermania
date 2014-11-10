package org;

import java.util.LinkedList;
import java.util.Vector;

import org.newdawn.slick.Sound;

public class MonsterBouftou extends Alive
{
	//public static final int MAX_DIST_EVALUATION = 10;
	public static final double DAMAGE = 1;
	
	public static final int potentialSpeed = 3;
	
	private int rotation = 0, timerAttack=0;
	private double hp;
	//private int moveTimer;
	
	private Vector<Element> rivals;
	
	private Animation anim;
	private Sound attack;
	
	private boolean isFighting = false;
	
	//private boolean rivalsUpdated = false;
	
	/*class Calculator extends Thread
	{
		public void run()
		{
			while(alive)
			{
				if(alignedToGrid((int)potentialSpeed))
					wanDir = getDirection();
			}
		}
	}*/
	
	/*public int getDirection()
	{
		
		int xm = (int)(x/32.0), ym = (int)(y/32.0);
		
		boolean[][] explored = new boolean[map.getNbBlockX()][map.getNbBlockY()];
		
		LinkedList<Point> list = new LinkedList<Point>();
		
		explored[xm][ym] = true;
		
		boolean positiveFound = false;
		
		if(aICalculator.getEvaluationMap(xm-1, ym) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm-1, ym,0,LEFT, 1));
			explored[xm-1][ym] = true;
		}
		if(aICalculator.getEvaluationMap(xm+1, ym) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm+1, ym,0,RIGHT,1));
			explored[xm+1][ym] = true;
		}
		if(aICalculator.getEvaluationMap(xm, ym-1) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm, ym-1,0,UP,1));
			explored[xm][ym-1] = true;
		}
		if(aICalculator.getEvaluationMap(xm, ym+1) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm, ym+1,0,DOWN, 1));
			explored[xm][ym+1] = true;
		}
		
		Point p;
		while(!list.isEmpty())
		{
			p = list.remove();
			
			for(int i = 0; i < rivals.size(); i++)
			{
				System.out.println(i);
				if(!rivals.get(i).alive)
					rivals.remove(i);
				if((int)(rivals.get(i).x/32.0) == p.x && (int)(rivals.get(i).y/32.0) == p.y)
				{
					return p.group;
				}
			}
			
			if(p.distance < MAX_DIST_EVALUATION)
			{
				if(aICalculator.getEvaluationMap(p.x-1, p.y)  != AICalculator.WALL_VALUE && !explored[p.x-1][p.y])
				{
					list.add(new Point(p.x-1, p.y, 0, p.group, p.distance+1));
					explored[p.x-1][p.y] = true;
				}
				if(aICalculator.getEvaluationMap(p.x+1, p.y)  != AICalculator.WALL_VALUE && !explored[p.x+1][p.y])
				{
					list.add(new Point(p.x+1, p.y, 0, p.group, p.distance+1));
					explored[p.x+1][p.y] = true;
				}
				if(aICalculator.getEvaluationMap(p.x, p.y-1)  != AICalculator.WALL_VALUE && !explored[p.x][p.y-1])
				{
					list.add(new Point(p.x, p.y-1, 0, p.group, p.distance+1));
					explored[p.x][p.y-1] = true;
				}
				if(aICalculator.getEvaluationMap(p.x, p.y+1)  != AICalculator.WALL_VALUE && !explored[p.x][p.y+1])
				{
					list.add(new Point(p.x, p.y+1, 0, p.group, p.distance+1));
					explored[p.x][p.y+1] = true;
				}
			}
		}
		return NOTHING;
	}*/

	public MonsterBouftou(double x, double y)
	{
		super(x, y);
		
		hp = 100;
		type = Type.MONSTER;
		collisionRadius = 15;
		anim = new Animation(getAnimationData("bouftou"));
		//moveTimer = 0;
		
		rivals = new Vector<Element>();
		Element e;
		for(int i = 0; i < listElements.size(); i++)
		{
			
			e = listElements.get(i);
			if(e.getType() == Type.BOMBERMAN)
			{
				pushEvent(e, "new monstre");
				rivals.add(e);
			}
		}
		
		attack=getSound("bouftou2");
		//rivals = new Vector<Element>();
		
		
		/*Calculator c = new Calculator();
		c.setPriority(Thread.MIN_PRIORITY);
		c.start();*/
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
						pushEvent(ev.getSource().source, "monster xp");
						for(int i = 0; i < 10; i ++)
						{
							listElements.addElement(new MonsterBouftouBallHeal(x+Math.random()*30-15, y+Math.random()*30-15, ev.getSource().source));
						}
					}
				}
			}
			if(ev.getName().equals("robot"))
			{
				destroy();
			}
		}	
	}

	@Override
	public void update()
	{
		/*if(!rivalsUpdated)
		{
			Element e;
			for(int i = 0; i < listElements.size(); i++)
			{
				
				e = listElements.get(i);
				if(e.getType() == Type.BOMBERMAN)
					rivals.add(e);
			}
			rivalsUpdated = true;
		}*/
		LinkedList<Element> list = collisionElementList((int)x, (int)y, Type.BOMBERMAN, false);
		isFighting = false;
		while(!list.isEmpty())
		{
		    isFighting = true;
			if(onView() && timerAttack==0){ 
			attack.play(3,1);
			timerAttack=90;
			}
			else timerAttack--;
//			
			this.pushEvent(list.remove(), "bouftou", DAMAGE);
			
		}
		
		if(alignedToGrid((int)potentialSpeed))
		{
			Element best = null;
			double bestDist=0;
			for(int i = 0; i < rivals.size(); i++)
			{
				if(!rivals.get(i).alive)
					rivals.remove(i);
				else
				{
					double dist = distanceToElementPow(rivals.get(i));
					if(best == null)
					{
						best = rivals.get(i);
						bestDist = dist;
					}
					else if(dist < bestDist)
					{
						best = rivals.get(i);
						bestDist = dist;
					}
				}
			}
			if(best != null)
			{
				boolean commandLeft=false, commandRight=false, commandUp=false, commandDown=false;
				if(best.x < x) commandLeft = true;
				else if(best.x > x) commandRight = true;
				if(best.y < y) commandUp = true;
				else if(best.y > y) commandDown = true;
				
				setWanDirCommand(commandLeft, commandRight, commandUp, commandDown);
			}
		}
		
		/*moveTimer--;
		if(moveTimer <= 0)
		{
			moveTimer = 15+(int)(Math.random()*60.0);
			Vector<Integer> vec = new Vector<Integer>();
			if(aICalculator.getEvaluationMap((int)(x/32.0)-1, (int)(y/32.0)) != AICalculator.WALL_VALUE)
				vec.add(LEFT);
			if(aICalculator.getEvaluationMap((int)(x/32.0)+1, (int)(y/32.0)) != AICalculator.WALL_VALUE)
				vec.add(RIGHT);
			if(aICalculator.getEvaluationMap((int)(x/32.0), (int)(y/32.0)-1) != AICalculator.WALL_VALUE)
				vec.add(UP);
			if(aICalculator.getEvaluationMap((int)(x/32.0), (int)(y/32.0)+1) != AICalculator.WALL_VALUE)
				vec.add(DOWN);
			
			if(vec.size() > 0)
				wanDir = vec.get((int)(Math.random()*(double)vec.size()));
	
		}*/
		
		
		speed = potentialSpeed;
		moveGrid();
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
	public void render() {
		if(curDir == RIGHT)
			rotation = 0;
		else if(curDir == DOWN)
			rotation = 90;
		if(curDir == LEFT)
			rotation = 180;
		if(curDir == UP)
			rotation = 270;
		drawImage(anim.getImage(), (int)x, (int)y, rotation);
		anim.update();
	}

	@Override
	public void postRender() {
		// TODO Auto-generated method stub
		
	}
	
}
