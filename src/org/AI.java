package org;


import java.util.LinkedList;
import java.util.Vector;

public class AI extends Bomberman
{
	public static final int MAX_DIST_EVALUATION = 30;
	public static final int DIST_COEF = 1;
	public static final int MIN_VALUE = -50;
	public static final int MAX_RAND = 1;
	
	private static int numberAI=0;
	
	private Vector<Element> rivals;
	
	private int IADirection, c4x, c4y, numAI;
	private Point target;
	private boolean rivalsUpdated = false;
	private int timerReset = 120;
	private IAThread iat;
	
	private class IAThread extends Thread
	{
		public void run()
		{
			while(alive)
			{
				boolean canRun = false;
				if(alignedToGrid((int)potentialSpeed))
				{
					Point bestPoint = getBestPoint();
					IADirection = bestPoint.group;
					if(bestPoint.x != (int)(x/32.0) && bestPoint.y != (int)(y/32.0))
						canRun = true;
				}
				//resetCommand();
				switch(IADirection)
				{
					case LEFT:
						commandLeft= true;
						commandRight = false;
						commandUp = false;
						commandDown = false;
						break;
					case RIGHT : 
						commandRight = true;
						commandLeft = false;
						commandUp = false;
						commandDown = false;
						break;
					case UP : 
						commandUp = true;
						commandLeft = false;
						commandRight = false;
						commandDown = false;
						break;
					case DOWN : 
						commandDown = true;
						commandLeft = false;
						commandRight = false;
						commandUp = false;
						break;
					default : 
						commandLeft = false;
						commandRight = false;
						commandUp = false;
						commandDown = false;
						break;
				}
              /*  if(nbC4 > 0 && !C4Placed)
                {
                    commandC4 = true;
                    c4x=(int)(x/32);
                    c4y=(int)(y/32);
                }
                else if(C4Placed && seeRival(c4x, c4y))
                {
                	commandC4=true;
                }
                else
                	commandC4 = false;*/
				if(((aICalculator.getEvaluationMap((int)x/32, (int)y/32)-AICalculator.BOMBERMAN_VALUE >= AICalculator.DESTROYABLE_WALL_VALUE && IADirection != -1) && bombExplode))
				{
					commandBomb = true;
				}
				else if(seeRival())
					commandBomb = true;
				else
				{
					commandBomb = false;
					commandTrap = false;
					
				}
			}
		}
	}
	
	public String getName()
	{
		return "AI"+numAI;
	}
	
	public AI(double x, double y)
	{
		super(x, y);
		numberAI++;
		numAI = numberAI;
		rivals = new Vector<Element>();
		AIs.add(this);
	}
	
	private boolean seeRival()
	{
		return seeRival((int)(x/32), (int)(y/32));
	}
	
	
	private boolean seeRival(int x, int y)
	{
		Element e;
		for(int i = 0 ; i < rivals.size(); i++)
		{
			
			e = rivals.get(i);
			
			if(!e.alive)
				rivals.remove(i);
			else
			{
				int rx = (int)(e.x/32.0), ry = (int)(e.y/32.0);

				if(aICalculator.lineFree(x,y,rx,ry) && this.explosionLength >= distanceToElement(e)/32.0)
					return true;
			}
		}
		return false;
	}
	
	private boolean isRivalNextToPlacedC4()
	{
	    if(C4Placed)
	    {
	        Element e = null;
	        int C4X = 0, C4Y = 0;
	        for(int i = 0; i < listElements.size(); i++)
	        {
	            try
	            {
	                e = listElements.get(i);
	            }
	            catch(ArrayIndexOutOfBoundsException ex)
	            {
	                
	            }
	            if(e instanceof C4 && e.source == this)
	            {
	                C4X = (int)(e.x/32.0);
	                C4Y = (int)(e.y/32.0);
	                break;
	            }
	        }
	        
	        Element currentRival;
	        for(int i = 0; i < rivals.size(); i++)
	        {
	            currentRival = rivals.get(i);
	            if(currentRival.alive)
	            {
	                int rivalX = (int)(currentRival.x/32.0);
	                int rivalY = (int)(currentRival.y/32.0);

	                if(e != null && aICalculator.lineFree(C4X, C4Y, rivalX, rivalY) && this.explosionLength >= distanceToElement(currentRival.x, currentRival.y, e)/32.0)
	                    return true;
	            }
	        }
	    }
	    return false;
	}
	
	private Point getBestPoint()
	{
		
		int xm = (int)(x/32.0), ym = (int)(y/32.0);
		
		boolean[][] explored = new boolean[map.getNbBlockX()][map.getNbBlockY()];
		
		Point[] bestPoint = new Point[4];
		for(int i = 0; i < 4; i++)
		{
			bestPoint[i] = new Point(xm,ym, AICalculator.WALL_VALUE, 0, 0);
		}
		
		LinkedList<Point> list = new LinkedList<Point>();
		
		explored[xm][ym] = true;
		
		boolean positiveFound = false;
		
		if(aICalculator.getEvaluationMap(xm-1, ym) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm-1, ym, aICalculator.getEvaluationMap(xm-1, ym)-DIST_COEF+(int)(Math.random()*(double)(MAX_RAND+1)), LEFT, 1));
			explored[xm-1][ym] = true;
			if(aICalculator.getEvaluationMap(xm-1, ym) > MIN_VALUE)
				positiveFound = true;
		}
		if(aICalculator.getEvaluationMap(xm+1, ym) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm+1, ym, aICalculator.getEvaluationMap(xm+1, ym)-DIST_COEF+(int)(Math.random()*(double)(MAX_RAND+1)), RIGHT, 1));
			explored[xm+1][ym] = true;
			if(aICalculator.getEvaluationMap(xm+1, ym) > MIN_VALUE)
				positiveFound = true;
		}
		if(aICalculator.getEvaluationMap(xm, ym-1) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm, ym-1, aICalculator.getEvaluationMap(xm, ym-1)-DIST_COEF+(int)(Math.random()*(double)(MAX_RAND+1)), UP, 1));
			explored[xm][ym-1] = true;
			if(aICalculator.getEvaluationMap(xm, ym-1) > MIN_VALUE)
				positiveFound = true;
		}
		if(aICalculator.getEvaluationMap(xm, ym+1) != AICalculator.WALL_VALUE)
		{
			list.add(new Point(xm, ym+1, aICalculator.getEvaluationMap(xm, ym+1)-DIST_COEF+(int)(Math.random()*(double)(MAX_RAND+1)), DOWN, 1));
			explored[xm][ym+1] = true;
			if(aICalculator.getEvaluationMap(xm, ym+1) > MIN_VALUE)
				positiveFound = true;
		}
		
		if(!positiveFound && aICalculator.getEvaluationMap(xm, ym)+AICalculator.BOMBERMAN_VALUE >= MIN_VALUE)
		{
			goToCurrentCase();
			return new Point(xm,ym, AICalculator.WALL_VALUE, -1, 0);
		}
		
		Point p;
		while(!list.isEmpty())
		{
			p = list.remove();
			
			if(p.value > bestPoint[p.group].value)
			{
				bestPoint[p.group] = p;
				if(p.value > MIN_VALUE)
					positiveFound = true;
			}
			
			if(p.distance < MAX_DIST_EVALUATION)
			{
				if(aICalculator.getEvaluationMap(p.x-1, p.y)  != AICalculator.WALL_VALUE &&
						(aICalculator.getEvaluationMap(p.x-1, p.y) > MIN_VALUE || !positiveFound) && !explored[p.x-1][p.y])
				{
					list.add(new Point(p.x-1, p.y, aICalculator.getEvaluationMap(p.x-1, p.y)-DIST_COEF*(p.distance+1)+(int)(Math.random()*(double)(MAX_RAND+1)), p.group, p.distance+1));
					explored[p.x-1][p.y] = true;
				}
				if(aICalculator.getEvaluationMap(p.x+1, p.y)  != AICalculator.WALL_VALUE &&
						(aICalculator.getEvaluationMap(p.x+1, p.y) > MIN_VALUE || !positiveFound) && !explored[p.x+1][p.y])
				{
					list.add(new Point(p.x+1, p.y, aICalculator.getEvaluationMap(p.x+1, p.y)-DIST_COEF*(p.distance+1)+(int)(Math.random()*(double)(MAX_RAND+1)), p.group, p.distance+1));
					explored[p.x+1][p.y] = true;
				}
				if(aICalculator.getEvaluationMap(p.x, p.y-1)  != AICalculator.WALL_VALUE &&
						(aICalculator.getEvaluationMap(p.x, p.y-1) > MIN_VALUE || !positiveFound) && !explored[p.x][p.y-1])
				{
					list.add(new Point(p.x, p.y-1, aICalculator.getEvaluationMap(p.x, p.y-1)-DIST_COEF*(p.distance+1)+(int)(Math.random()*(double)(MAX_RAND+1)), p.group, p.distance+1));
					explored[p.x][p.y-1] = true;
				}
				if(aICalculator.getEvaluationMap(p.x, p.y+1)  != AICalculator.WALL_VALUE &&
						(aICalculator.getEvaluationMap(p.x, p.y+1) > MIN_VALUE || !positiveFound) && !explored[p.x][p.y+1])
				{
					list.add(new Point(p.x, p.y+1, aICalculator.getEvaluationMap(p.x, p.y+1)-DIST_COEF*(p.distance+1)+(int)(Math.random()*(double)(MAX_RAND+1)), p.group, p.distance+1));
					explored[p.x][p.y+1] = true;
				}
			}
		}
		
		int bestDirection = 0;
		
		for(int i = 1; i < 4; i++)
		{
			if(bestPoint[i].value == bestPoint[bestDirection].value)
			{
				if(Math.random() < 0.5)
					bestDirection = i;
			}
			if(bestPoint[i].value > bestPoint[bestDirection].value)
			{
				bestDirection = i;
			}
		}
		
		target = bestPoint[bestDirection];
		
		return bestPoint[bestDirection];
	}
	
	private void goToCurrentCase()
	{
		resetCommand();
		
		int xcase = ((int)(x/32.0))*32;
		int ycase = ((int)(x/32.0))*32;
		
		if(x < xcase) commandRight = true;
		else if(x > xcase) commandLeft = true;
		if(y < ycase) commandDown = true;
		else if(y > ycase) commandUp = true;
	}
	
	public void update()
	{
		if(!bombExplode)
		{
			timerReset--;
			if(timerReset <= 0)
				bombExplode = true;
		}
		else
			timerReset=120;
		if(!rivalsUpdated)
		{
			rivalsUpdated = true;
			Element e;
			for(int i = 0; i < listElements.size(); i++)
			{
				
				e = listElements.get(i);
				if(e != this && e.getType() == Type.BOMBERMAN)
					rivals.add(e);
			}
		}
		while(!newMonster.isEmpty())
		{
			rivals.add(newMonster.remove());
		}
		super.update();
	}
	
	public void start()
	{
	    iat = new IAThread();
	    iat.setPriority(Thread.MIN_PRIORITY);
	    iat.start();
	}
}


