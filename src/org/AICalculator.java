package org;

public class AICalculator extends Global
{
	public static final int WALL_VALUE = Integer.MIN_VALUE;
	public static final int BONUS_VALUE = 25;
	public static final int DESTROYABLE_WALL_VALUE = 10;
	public static final int BOMB_VALUE = -150;
	public static final int EXPLOSION_VALUE = -200;
	public static final int TRAP_VALUE = -20;
	public static final int MONSTER_VALUE = -300;
	public static final int BOMBERMAN_VALUE = 20;
	public static final int HEAL_VALUE = 20;
	
	private int[][] evaluationMap, prevEvaluationMap;
	private int sizex, sizey;
	private boolean calculating;
	
	public AICalculator()
	{
		sizex = map.getNbBlockX();
		sizey = map.getNbBlockY();
		evaluationMap = new int[sizex][sizey];
		prevEvaluationMap = new int[sizex][sizey];
		calculating = false;
	}
	
	public boolean lineFree(int x1, int y1, int x2, int y2)
	{
		if(x1 == x2)
		{
			int start = Math.min(y1, y2);
			int end = Math.max(y1, y2);
			
			for(int i = start; i <= end; i++)
			{
				if(evaluationMap[x1][i] == WALL_VALUE)
					return false;
			}
			return true;
		}
		else if(y1 == y2)
		{
			int start = Math.min(x1, x2);
			int end = Math.max(x1, x2);
			
			for(int i = start; i <= end; i++)
			{
				if(evaluationMap[i][y1] == WALL_VALUE)
					return false;
			}
			return true;
		}
		return false;
	}
	
	private void saveEvaluation()
	{
		for(int i = 0; i < sizex; i++)
		{
			for(int j = 0; j < sizey; j++)
			{
				prevEvaluationMap[i][j] = evaluationMap[i][j];
			}
		}
	}
	
	public synchronized int getEvaluationMap(int x, int y)
	{
		try
		{
			if(!calculating)
				return evaluationMap[x][y];
			else
				return prevEvaluationMap[x][y];

		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return WALL_VALUE;
		}
	}
	
	private void init()
	{
		for(int i = 0; i < sizex; i++)
		{
			for(int j = 0; j < sizey; j++)
			{
				evaluationMap[i][j] = map.isWall(i*32+16, j*32+16)?WALL_VALUE:0;
			}
		}
		
		int n = listElements.size();
		Element e;
		for(int i = 0; i < n; i++)
		{
			e = listElements.get(i);
			
			if(e.isSolid())
			{
				try
				{
					evaluationMap[(int)(e.getX()/32.0)][(int)(e.getY()/32.0)] = WALL_VALUE;
				}
				catch(ArrayIndexOutOfBoundsException ex) {}
			}
		}
	}
	
	public synchronized void evaluateMap()
	{
		calculating = true;
		
		saveEvaluation();
		init();
		
		int n = listElements.size();
		for(int i = 0; i < n; i++)
		{
			
			Element e = listElements.get(i);
			int ex = (int)(e.getX()/(double)map.getBlockWidth());
			int ey = (int)(e.getY()/(double)map.getBlockHeight());
			
			
			if(e.getType() == Type.MONSTER)
			{
				evaluationMap[ex][ey] = MONSTER_VALUE;
				/*if(evaluationMap[ex+1][ey] != WALL_VALUE) evaluationMap[ex+1][ey]+= MONSTER_VALUE;
				if(evaluationMap[ex-1][ey] != WALL_VALUE) evaluationMap[ex-1][ey]+= MONSTER_VALUE;
				if(evaluationMap[ex][ey+1] != WALL_VALUE) evaluationMap[ex][ey+1]+= MONSTER_VALUE;
				if(evaluationMap[ex][ey-1] != WALL_VALUE) evaluationMap[ex][ey-1]+= MONSTER_VALUE;*/
			}
			else if(e.getType() == Type.DESTROYABLE_WALL)
			{
				evaluationMap[ex][ey] = WALL_VALUE;
				if(evaluationMap[ex+1][ey] != WALL_VALUE) evaluationMap[ex+1][ey]+= DESTROYABLE_WALL_VALUE;
				if(evaluationMap[ex-1][ey] != WALL_VALUE) evaluationMap[ex-1][ey]+= DESTROYABLE_WALL_VALUE;
				if(evaluationMap[ex][ey+1] != WALL_VALUE) evaluationMap[ex][ey+1]+= DESTROYABLE_WALL_VALUE;
				if(evaluationMap[ex][ey-1] != WALL_VALUE) evaluationMap[ex][ey-1]+= DESTROYABLE_WALL_VALUE;
			}
			else if(e.getType() == Type.EXPLOSION)
			{
				evaluationMap[ex][ey]+= EXPLOSION_VALUE;
			}
			else if(e.getType() == Type.BONUS)
			{
				evaluationMap[ex][ey]+= BONUS_VALUE;
				evaluationMap[ex][ey]+= e.getEvaluationAI();
			}
			else if(e.getType() == Type.BOMBERMAN || e.getType() == Type.ROBOT )
			{
				evaluationMap[ex][ey]+= BOMBERMAN_VALUE;
			}
			else if(e.getType() == Type.TRAP)
			{
				evaluationMap[ex][ey]+= TRAP_VALUE;
			}
			else if(e.getType() == Type.WEAPONS)
			{
				
				boolean left=true, right=true, top=true, bottom=true;
				int dist=1;
				evaluationMap[ex][ey] = WALL_VALUE;
				while(left || right || top || bottom)
				{
					if(left)
					{
						if(evaluationMap[ex-dist][ey] !=WALL_VALUE)
							evaluationMap[ex-dist][ey]+=BOMB_VALUE;
						else
							left=false;
					}
					if(right)
					{
						if(evaluationMap[ex+dist][ey] !=WALL_VALUE)
							evaluationMap[ex+dist][ey]+=BOMB_VALUE;
						else
							right=false;
					}
					if(top)
					{
						if(evaluationMap[ex][ey-dist] !=WALL_VALUE)
							evaluationMap[ex][ey-dist]+=BOMB_VALUE;
						else
							top=false;
					}
					if(bottom)
					{
						if(evaluationMap[ex][ey+dist] !=WALL_VALUE)
							evaluationMap[ex][ey+dist]+=BOMB_VALUE;
						else
							bottom=false;
					}
					dist++;
				}
			}
		}
		
		calculating = false;
	}
	
}
