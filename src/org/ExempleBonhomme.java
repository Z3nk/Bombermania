package org;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class ExempleBonhomme extends Element
{
	private double taille;
	private double resistance;
	private int bonus;
	private Animation anim;
	private Image img, imgBonus;
	private int compteur;
	private Element focus;
	private boolean fuite, jeSoigne;
	
	
	public ExempleBonhomme(double x, double y)
	{
		super(x, y);
		taille = 1.0;
		anim = new Animation(getAnimationData("bonhomme_cour"));
		img = getImage("bonhomme_fixe");
		type = Type.BOMBERMAN;
		solid = true;
		collisionRadius = 9;
		speed = 6;
		fuite = false;
		
		compteur = 0;
		
		if(Math.random() < 0.1)
		{
			imgBonus = images.get("bonhomme_armure");
			bonus = 2;
			resistance=0.8;
		}
		else if(Math.random() < 0.2)
		{
			imgBonus = images.get("bonhomme_soin");
			bonus = 1;
			resistance=0.0;
		}
		else
		{
			imgBonus = null;
			bonus = 0;
			resistance=0.0;
		}
	}

	public void preUpdate()
	{
		Event ev;
		
		double minTaille = 1.0;
		focus = null;
		
		while(eventExist())
		{
			ev = popEvent();
			if(ev.getName().equals("jveux te boufer !"))
			{
				fuite=true;
			}
			else if(ev.getName().equals("jte soigne !") && !fuite && bonus != 1)
			{
				focus = ev.getSource();
			}
			else if(ev.getName().equals("taille-"))
			{
				taille -= 0.1*(1.0-resistance);
				fuite = true;
				if(bonus != 2)
				{
					direction = directionToElement(ev.getSource())+180.0;
					
					compteur+=20;
				}
				else
				{
					direction = directionToElement(ev.getSource());
					if(taille <= 0.5)
						direction+=180.0;
				}
				
				if(taille <= 0.3)
				{
					pushEvent(ev.getSource(), "mort");
					destroy();
				}
			}
			
			else if(ev.getName().equals("soin"))
			{
				taille+=0.01*(1.0-resistance);
				taille+=0.01;
			}
			
			else if(bonus == 1 && !fuite)
			{
				if(ev.getName().equals("need soin !") && distanceToElementPow(ev.getSource()) < 150*150 && ev.getValue() < minTaille)
				{
					focus = ev.getSource();
					minTaille = ev.getValue();
				}
			}
		}
		
		
		if(focus != null)
		{
			direction = directionToElement(focus);
		}
	}

	public void update()
	{
		compteur--;
		if(compteur <= 0)
		{
			fuite = false;
			direction = Math.random()*360.0;
			compteur=10+(int)(Math.random()*20);
		}
		
		if(taille < 1.0)
		{
			int n = listElements.size();
			Element e;
			
			if(!fuite )
			{
				for(int i = 0; i < n; i++)
				{
					e = listElements.get(i);
					if(e.type == Type.BOMBERMAN && e != this)
					{
						pushEvent(e, "need soin !", taille);
					}
				}
			}
		}
		
		jeSoigne=false;
		if(focus != null && focus.alive() && bonus == 1)
		{
			pushEvent(focus, "jte soigne !");
			if(collisionElementRect((int)(x+hspeed), (int)(y+vspeed), focus))
			{
				jeSoigne = true;
				pushEvent(focus, "soin");
				taille+=0.01;
				if(taille > 1)
					taille = 1;
			}
		}
		
		updateHVSpeed();
		forwardStepByStep(false);
	}

	public void postUpdate()
	{
		
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			direction = directionToMouse();
	}

	public void preRender()
	{
	}

	public void render()
	{
		if(isMoving())
		{
			anim.play();
			drawImage(anim.getImage().getScaledCopy((float)taille), (int)x, (int)y, (float)direction);
			anim.update();
		}
		else
		{
			anim.stop();
			drawImage(img.getScaledCopy((float)taille), (int)x, (int)y, (float)direction);
		}
		
		if(imgBonus != null)
			drawImage(imgBonus.getScaledCopy((float)taille), (int)x, (int)y, (float)direction);
	}

	public void postRender()
	{
		if(focus != null && focus.alive() && bonus == 1)
		{
			if(jeSoigne)
				graphics.setColor(Color.green);
			else
				graphics.setColor(Color.red);
			graphics.drawLine((int)x-view.getX(), (int)y-view.getY(), (int)focus.x-view.getX(), (int)focus.y-view.getY());
		}
	}

}
