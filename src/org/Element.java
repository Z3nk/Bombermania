package org;

import java.awt.Point;
import java.util.LinkedList;

public abstract class Element extends Global
{
	protected Type type;
	protected int collisionRadius, curDir, wanDir, targetX, targetY;
	protected double x, y, xprevious, yprevious, speed, direction, hspeed, vspeed;
	protected boolean alive, solid, atTarget;
	protected Element source;
	
	private LinkedList<Event> events;
	
	public abstract void preUpdate();
	public abstract void update();
	public abstract void postUpdate();
	
	public abstract void preRender();
	public abstract void render();
	public abstract void postRender();
	
	public Element(double x, double y)
	{
		type = Type.UNDEFINED;
		this.x = x; this.y = y;
		speed = 0.0;
		direction = 0.0;
		hspeed = 0.0;
		vspeed = 0.0;
		alive = true;
		solid = false;
		collisionRadius = 0;
		xprevious = 0.0;
		yprevious = 0.0;
		events = new LinkedList<Event>();
		curDir = NOTHING;
		wanDir = NOTHING;
		atTarget=true;
	}
	
	//deplacement et collision
	
	protected boolean isMoving()
	{
		return ((x != xprevious) || (y != yprevious));
	}
	
	protected void updateHVSpeed()
	{
		hspeed = Math.cos(direction*Math.PI/180.0)*speed;
		vspeed = Math.sin(direction*Math.PI/180.0)*speed;
	}
	
	private boolean collisionMap(int x, int y)
	{
		return map.isWall((x-collisionRadius), (y-collisionRadius)) ||
			   map.isWall((x+collisionRadius), (y-collisionRadius)) ||
			   map.isWall((x+collisionRadius), (y+collisionRadius)) ||
			   map.isWall((x-collisionRadius), (y+collisionRadius));
	}
	
	protected boolean collisionElementRect(int x, int y, Element elem)
	{
		return (x+collisionRadius >= (int)elem.x-elem.collisionRadius &&
				x-collisionRadius <= (int)elem.x+elem.collisionRadius-1 &&
				y+collisionRadius >= (int)elem.y-elem.collisionRadius &&
				y-collisionRadius <= (int)elem.y+elem.collisionRadius-1);
	}
	
	protected boolean collisionElementCircle(int x, int y, Element elem)
	{
		return distanceToElementPow(x, y, elem) <= (collisionRadius+elem.collisionRadius)*(collisionRadius+elem.collisionRadius);
	}
	
	/**
	 * Test la collision entre l'élément et tout les autres. Retourne un vecteur contenant tout les éléments avec lequels on est en collision.
	 * @param x Position x de l'élément pour les test de collision
	 * @param y Position y de l'élément pour les test de collision
	 * @param t Type d'élément avec lesquels on souhaite tester la collision, Type.ALL signifie tout types confondue
	 * @param onlySolid Permet d'indiquer si oui ou non on souhaite ne prendre en compte que les éléments définis comme solid
	 * @return Vecteur contenant tout les éléments vérifiant les conditions et en collision
	 */
	protected LinkedList<Element> collisionElementList(int x, int y, Type t, boolean onlySolid)
	{
		LinkedList<Element> result = new LinkedList<Element>();
		int n = listElements.size();
		Element elem;
		
		for(int i = 0; i < n; i++)
		{
			elem = listElements.get(i);
			
			if( (elem != this) &&
				(t == Type.ALL || elem.type == t) &&
				(elem.solid || !onlySolid) &&
				collisionElementRect(x, y, listElements.get(i)) )
			{
				result.add(listElements.get(i));
			}
		}
		
		return result;
	}
	
	/**
	 * Fait avancer l'élément avec son hspeed et vspeed. Avance pixel par pixel et s'arr�te lorsqu'un �l�ment solide est détecté, 
	 * nécéssite un speed cohérant avec hspeed et vspeed. 
	 * @param stopWhenCollision permet d'indiquer si on s'arr�te dés qu'il y a collision (true), ou si on continue � glisser (false).
	 * @return true si le déplacement s'est effectué complétement ou false s'il y a eu collision.
	 */
	protected boolean forwardStepByStep(boolean stopWhenCollision)
	{
		double hstep = hspeed/speed;
		double vstep = vspeed/speed;
		boolean noCollision = true;
		
		for(int i = 0; i < (int) speed; i++)
		{
			if(!(noCollision = forwardOneStep(hstep, 0.0)) && stopWhenCollision)
				return false;
			if(!(noCollision = forwardOneStep(0.0, vstep)) && stopWhenCollision)
				return false;
		}
		
		hstep = hstep*(double)(speed-(int)speed);
		vstep = vstep*(double)(speed-(int)speed);
		
		if(!(noCollision = forwardOneStep(hstep, 0.0)) && stopWhenCollision)
			return false;
		if(!(noCollision = forwardOneStep(0.0, vstep)) && stopWhenCollision)
			return false;
		
		return noCollision;
	}
	
	/**
	 * Fait avancer l'élément avec son hspeed et vspeed, si c'est possible il avance, sinon il n'avance pas.
	 * @return true si il a avancé, false s'il n'a pas bougé.
	 */
	private boolean forwardOneStep()
	{
		if(!collisionMap((int)(x+hspeed), (int)(y+vspeed)) &&
		   collisionElementList((int)(x+hspeed), (int)(y+vspeed), Type.ALL, true).isEmpty())
		{
			x+=hspeed;
			y+=vspeed;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Fait avancer l'élément avec le hspeed et le vspeed passé en paramétre, si c'est possible il avance, sinon il n'avance pas.
	 * @param hspeed déplacement horizontal voulu
	 * @param vspeed déplacement vertical voulu
	 * @return  true si il a avancé, false s'il n'a pas bougé.
	 */
	private boolean forwardOneStep(double hspeed, double vspeed)
	{
		if(!collisionMap((int)(x+hspeed), (int)(y+vspeed)) &&
		   collisionElementList((int)(x+hspeed), (int)(y+vspeed), Type.ALL, true).isEmpty())
		{
			x+=hspeed;
			y+=vspeed;
			return true;
		}
		else
			return false;
	}
	
	protected boolean alignedToGrid(int tolerance)
	{
		
		int gx = ((int)(x/map.getBlockWidth()))*map.getBlockWidth()+map.getBlockWidth()/2;
		int gy = ((int)(y/map.getBlockHeight()))*map.getBlockHeight()+map.getBlockHeight()/2;
		if(Math.abs(gx-x) <tolerance && Math.abs(gy-y) <tolerance)
			return true;
		else
			return false;
	}
	
	protected boolean positionFree(int x, int y)
	{
		return (collisionElementList(x, y, Type.ALL, true).isEmpty() && !collisionMap(x, y));
	}
	
	
	private boolean moveToPoint(int px, int py, boolean align)
	{
		forwardOneStep();
		if(distanceToElementPow(px, py, this) <= (hspeed+vspeed)*(hspeed+vspeed))
		{
			if(align)
			{
				x=px; y=py;
			}
			return true;
		}
		else return false;
	}
	
	private Point getNextCase(int direction)
	{
		int width = map.getBlockWidth(), height = map.getBlockHeight();
		int xx = (int)(x/width)*width+width/2, yy = (int)(y/height)*height+height/2;
		
		switch(direction)
		{
		case RIGHT:
			xx+=width;
			break;
		case LEFT:
			xx-=width;
			break;
		case UP:
			yy-=height;
			break;
		case DOWN:
			yy+=height;
			break;
		}
		return new Point(xx, yy);
	}
	
	private boolean nextCaseFree(int direction)
	{
		Point p = getNextCase(direction);
		
		if(positionFree((int)p.getX(), (int)p.getY()))
			return true;
		else return false;
	}
	
	public static boolean inverseDirection(int dir1, int dir2)
	{
		return( (dir1 == RIGHT && dir2 == LEFT) || (dir2 == RIGHT && dir1 == LEFT)
				|| (dir1 == UP && dir2 == DOWN) || (dir2 == UP && dir1 == DOWN) );
	}
	
	protected void setWanDirCommand( boolean commandLeft, boolean commandRight, boolean commandUp, boolean commandDown)
	{
		if(commandRight && commandUp)
		{
			if(nextCaseFree(curDir==UP?RIGHT:UP))
				wanDir = curDir==UP?RIGHT:UP;
			else
				wanDir = curDir==UP?UP:RIGHT;
		}
		else if(commandRight && commandDown)
		{
			if(nextCaseFree(curDir==RIGHT?DOWN:RIGHT))
				wanDir = curDir==RIGHT?DOWN:RIGHT;
			else
				wanDir = curDir==RIGHT?RIGHT:DOWN;
		}
		else if(commandLeft && commandUp)
		{
			if(nextCaseFree(curDir==LEFT?UP:LEFT))
				wanDir = curDir==LEFT?UP:LEFT;
			else
				wanDir = curDir==LEFT?LEFT:UP;
		}
		else if(commandLeft && commandDown)
		{
			if(nextCaseFree(curDir==LEFT?DOWN:LEFT))
				wanDir = curDir==LEFT?DOWN:LEFT;
			else
				wanDir = curDir==LEFT?LEFT:DOWN;
		}
		else if(commandRight) wanDir = RIGHT;
		else if(commandLeft) wanDir = LEFT;
		else if(commandDown) wanDir = DOWN;
		else if(commandUp) wanDir = UP;
		else wanDir = NOTHING;
	}
	
	protected void moveGrid()
	{
		if(atTarget /*|| inverseDirection(wanDir, curDir) */)
		{
			curDir=NOTHING;
			if(nextCaseFree(wanDir))
				curDir = wanDir;
			Point p = getNextCase(curDir);
			targetX = (int)p.getX();
			targetY = (int)p.getY();
			
		}
		switch(curDir)
		{
			case RIGHT:
				hspeed=speed;
				vspeed = 0;
				break;
			case LEFT:
				hspeed=-speed;
				vspeed = 0;
				break;
			case DOWN:
				vspeed=speed;
				hspeed = 0;
				break;
			case UP:
				vspeed=-speed;
				hspeed = 0;
				break;
			default:
				hspeed = 0;
				vspeed = 0;
				speed = 0;
				break;
		}
		if(curDir != NOTHING)
		{
			atTarget = moveToPoint(targetX, targetY, true);
		}
	}
	
	
	//Calculs directions, distances nombre d'elements
	
	public int numberOf(Type type)
	{
		int n = listElements.size(), result = 0;
		
		for(int i = 0; i < n; i++)
		{
			if(listElements.get(i).type == type)
			{
				result++;
			}
		}
		return result;
	}
	
	public double distanceToElementPow(Element elem)
	{
		return (x-elem.x)*(x-elem.x) + (y-elem.y)*(y-elem.y);
	}
	
	public double distanceToElement(Element elem)
	{
	    return (x-elem.x) + (y-elem.y);
	}
	
	public static double distanceToElementPow(double x, double y, Element elem)
	{
		return (x-elem.x)*(x-elem.x) + (y-elem.y)*(y-elem.y);
	}
	
	public static double distanceToElement(double x, double y, Element elem)
    {
        return Math.abs(x-elem.x) + Math.abs(y-elem.y);
    }
	
	public double directionToPoint(double xx, double yy)
	{
		double direction;

		if(xx-x != 0)
		{
			direction = Math.atan((yy-y)/(xx-x))*180.0/Math.PI;
			if(xx < x)
				direction+=180.0;
		}
		else
		{
			if(yy > y)
				direction = 90.0;
			else
				direction = 270.0;
		}
		
		return direction;
	}
	
	public double directionToPoint(int x, int y)
	{
		double direction;

		if(x-this.x != 0)
		{
			direction = Math.atan((y-this.y)/(x-this.x))*180.0/Math.PI;
			if(x <this. x)
				direction+=180.0;
		}
		else
		{
			if(y > this.y)
				direction = 90.0;
			else
				direction = 270.0;
		}
		
		return direction;
	}
	
	public double directionToElement(Element elem)
	{
		return directionToPoint(elem.x, elem.y);
	}
	
	public double directionToMouse()
	{
		return directionToPoint(input.getMouseX()+view.getX(), input.getMouseY()+view.getY());
	}
	
	// geteurs
	
	public boolean isSolid()
	{
		return solid;
	}
	
	public int getEvaluationAI()
	{
		return 0;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	
	public boolean alive()
	{
		return alive;
	}
	
	public Event popEvent()
	{
		return events.removeFirst();
	}
	
	public boolean eventExist()
	{
		return !events.isEmpty();
	}
	
	//seteur
	public boolean onView()
	{
		if(x > view.getX() && x < view.getX()+windowWidth && y>view.getY() && y<view.getY()+windowHeight) return true;
		else return false;
	}
	
	/*public float getCoefViewSound()
	{
		if(onView()) return 1;
		else
		{
			int centerX=view.getX()+windowWidth;
			int centerY=view.getY()+windowHeight;
			if((maxSound*maxSound)-(centerX*centerY)<0) return 0;
			else return (float) Math.sqrt(((maxSound*maxSound)-(centerX*centerY))/(maxSound*maxSound));
		}
	}*/
	
	public void destroy()
	{
		alive = false;
	}
	
	public void pushEvent(Element elem, String name)
	{
		elem.events.addFirst(new Event(name, this));
	}
	
	public void pushEvent(Element elem, String name, double value)
	{
		elem.events.addFirst(new Event(name, this, value));
	}
	
}
