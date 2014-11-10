package org;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class Bomberman extends Alive {
	
	protected static int bombermanCounter=0, numberBomberman=0;

	protected boolean bombExplode;
	private double resistance;
	private Animation anim_front, anim_right, anim_left, anim_back;
	private Image img, armure;
	private Image[] img_fix;
	protected double potentialSpeed;
	private double power, damage;
	protected int explosionLength, nbTrap, nbBomb, nbC4, maxHp, timerTrap, level, experience, nextLevel, shieldHit, mod5hit, randX, randY;
	private int id,  maxLength, randDegres, sensX, sensY, directionBonus, compteuractivated, resetDamageTimer, prevDir, C4PosX, C4PosY;
	protected boolean C4Placed, underTrap, commandBomb, commandTrap, commandC4, commandLeft, commandRight, commandUp, commandDown, shieldOn, cacheraufond;
	private boolean sendBonus, timeractivated, perteBonus;
	protected double hp, cumul;

	private Sound mort, cri, banzai, canard;


	protected LinkedList<Element> newMonster;
	
	public String getName()
	{
		return "unamed";
	}

	
	protected void resetCommand()
	{
		commandBomb=false;
		commandTrap=false;
		commandC4=false;
		commandLeft=false;
		commandRight=false;
		commandUp=false;
		commandDown=false;
	}
	
	protected void placeBomb()
	{
		bombExplode = false;
		if(nbBomb>=1 && collisionElementList(((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, Type.WEAPONS, false).isEmpty()
				 && collisionElementList(((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, Type.EXPLOSION, false).isEmpty()){
			listElements.addElement(new Bomb( ((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, power, explosionLength, this));
			nbBomb--;
		}
	}
	protected void placeTrap()
	{
		if(nbTrap > 0 && collisionElementList(((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, Type.WEAPONS, false).isEmpty()
				 && collisionElementList(((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, Type.EXPLOSION, false).isEmpty())
		{
			listElements.addElement(new Trap(((int)(x/32))*32+16,((int)(y/32))*32+16,power, this));
			nbTrap--;
		}
	}
	protected void placeC4()
	{
		if(C4Placed)
		{
			int n = listElements.size();
			Element e;
			for(int i = 0; i < n; i++)
			{
				e = listElements.get(i);
				if(e.type == Type.WEAPONS)
					pushEvent(e, "C4");
			}
		}
		else if(nbC4 > 0)
		{
			if(collisionElementList(((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, Type.WEAPONS, false).isEmpty()
					&& collisionElementList(((int)(x/32.0))*32+16, ((int)(y/32.0))*32+16, Type.EXPLOSION, false).isEmpty())
			{
				C4PosX = ((int)(x/32))*32+16;
				C4PosY = ((int)(y/32))*32+16;
				C4Placed=true;
				listElements.addElement(new C4(((int)(x/32))*32+16,((int)(y/32))*32+16,power,explosionLength, this));
				nbC4--;
			}
		}
	}
	
	private void drawLifeBar()
	{
		graphics.setColor(Color.black);
		graphics.fillOval((float)x-30-view.getX(), (float)y-29-view.getY(), 16, 16);
		graphics.setColor(Color.white);
		String s = new Integer(level).toString();
		graphics.drawString(s, (float)x-23-graphics.getFont().getWidth(s)/2-view.getX(), (float)y-23-graphics.getFont().getHeight(s)/2-view.getY());
		if(win)
		{
			graphics.drawString("Victoire", (float)x-28-view.getX(), (float)y-45-view.getY());
		}
		graphics.setColor(Color.black);
		graphics.fillRect((float)x-16-view.getX(), (float)y-25-view.getY(), 32, 8);
		graphics.setColor(Color.green);
		graphics.fillRect((float)x-15-view.getX(), (float)y-24-view.getY(), (float)(hp/maxHp*30), 6);
	}
	
	public Bomberman(double x, double y) {
		super(x, y);
		// Variable de travail
		int var=(int)(Math.random()*4);
		// -------------
		
		numberBomberman++;
		bombermanCounter++;
		if(bombermanCounter > 4)
		    bombermanCounter = 1;
		
		if(var==1)
			mort=getSound("mort");
		else if(var==2) mort=getSound("Taunt");
		else  mort=getSound("Taunt2");
		
		var=(int)(Math.random()*2);
		if(var==1)cri=getSound("cri");
		else cri=getSound("cri2");
		
		newMonster = new LinkedList<Element>();
		bombExplode = true;
		
		canard=getSound("canard");
		
		//TEST
		
		
		img_fix=new Image[4];
		anim_front=new Animation(getAnimationData("J"+bombermanCounter+"_face"));
		anim_front.setSpeed(0.5);
		anim_right=new Animation(getAnimationData("J"+bombermanCounter+"_droite"));
		anim_right.setSpeed(0.5);
		anim_left=new Animation(getAnimationData("J"+bombermanCounter+"_gauche"));
		anim_left.setSpeed(0.5);
		anim_back=new Animation(getAnimationData("J"+bombermanCounter+"_dos"));
		anim_back.setSpeed(0.5);
		img_fix[0]=getImage("J"+bombermanCounter+"_droit_2");
		img_fix[1]=getImage("J"+bombermanCounter+"_face2");
		img_fix[2]=getImage("J"+bombermanCounter+"_gauche_2");
		img_fix[3]=getImage("J"+bombermanCounter+"_dos_2");
		img = getImage("bomberman_fixe");
		armure=getImage("shield");
		hp=50;
		//hp=250;
		resetDamageTimer=0;
		mod5hit=0;
		cumul=0.0;
		shieldOn=false;
		C4Placed = false;
		type = Type.BOMBERMAN;
		solid = false;
		nbTrap=0;
		collisionRadius = 5;
		potentialSpeed=3.0;
		//potentialSpeed=10;
		resistance=0.0;
		explosionLength=1;
		//explosionLength=10;
		nbTrap=0;
		nbC4=0;
		underTrap=false;
		level=1;
		experience=0;
		nextLevel=getExpLevel();
		commandBomb=false;
		commandTrap=false;
		commandC4=false;
		commandLeft=false;
		commandRight=false;
		commandUp=false;
		commandDown=false;
		sendBonus=false;
		nbBomb=1;
		shieldHit=0;
		maxLength=3;
		timeractivated=true;
		updateLevel();
	}
	
	
	private int getExpLevel()
	{
		return (int)(Math.pow(level, 1.5)*40+100);
	}
	
	private void updateLevel()
	{
		int hpp = -maxHp;
		maxHp=50+20*level;
		hpp+=maxHp;
		
		hp+=hpp;
		if(hp > maxHp)
			hp=maxHp;
		
		power=2+level;
	}

	public void preUpdate()
	{
		Event ev;
		boolean expDamage = false;
		
		while(eventExist())
		{
			
			ev = popEvent();
			
			if(ev.getName().equals("IncC4"))
			{
				nbC4++;
			}
			else if(ev.getName().equals("IncTrap")){
				nbTrap++;
			}
			else if(ev.getName().equals("BoostBomb")){
				explosionLength++;
			}
			else if(ev.getName().equals("MoreBomb")){
				bombExplode = true;
				nbBomb++;
			}
			else if(ev.getName().equals("Shield")){ 
				resistance=0.8;
				shieldHit=30;
				shieldOn=true;
			}
			else if(ev.getName().equals("trap"))
			{
				timerTrap=120;
				underTrap=true;
				hp-=ev.getValue()*(1-resistance);
			}
			else if(ev.getName().equals("BoostSpeed")){
				
				potentialSpeed+=0.5;
				if(potentialSpeed > 16)
					potentialSpeed=16;
			}
			else if(ev.getName().equals("Heal") && hp<maxHp){
				hp+=maxHp/5;
				if(hp>maxHp)hp=maxHp;
			}
			else if(!expDamage && ev.getName().equals("explosion"))
			{
				if(!expDamage)
				{
					hp-=ev.getValue()*(1-resistance);
					expDamage = true;
					
							
					
					if(shieldOn){
						shieldHit--;
						if(shieldHit<=0)
						{
							resistance=0;
							shieldOn=false;
						}
					}
					else damage+=ev.getValue()*(1-resistance);
				}
			}
			else if(!expDamage && ev.getName().equals("bouftou"))
			{
				
				hp-=ev.getValue()/100*maxHp*(1-resistance);
				
						
				
				if(shieldOn){
					shieldHit--;
					if(shieldHit<=0)
					{
						resistance=0;
						shieldOn=false;
					}
				}
				else damage+=ev.getValue()/100*maxHp*(1-resistance);
			}
			else if(ev.getName().equals("C4 explode"))
			{
				C4Placed = false;
			}
			else if(ev.getName().equals("wall xp"))
			{
				experience+=25;
				if(experience >= nextLevel)
				{
					experience-=nextLevel;
					level++;
					nextLevel=getExpLevel();
					updateLevel();
				}
			}
			else if(ev.getName().equals("monster xp"))
			{
				experience+=100;
				if(experience >= nextLevel)
				{
					experience-=nextLevel;
					level++;
					nextLevel=getExpLevel();
					updateLevel();
				}
			}
			else if(ev.getName().equals("heal monster"))
			{
				hp+=maxHp/50;
				if(hp>maxHp)hp=maxHp;
			}
			else if(ev.getName().equals("new monstre"))
			{
				System.out.println("ok");
				newMonster.add(ev.getSource());
			}
			else if(ev.getName().equals("robot")){
				hp-=ev.getValue()*(1-resistance);
				
				if(shieldOn){
					shieldHit--;
					if(shieldHit<=0)
					{
						resistance=0;
						shieldOn=false;
					}
				}
			}
		}
		resetDamageTimer++;
		if(damage>=(double)maxHp/12 && !shieldOn) {
			
			depopBonus();
		}
		if(resetDamageTimer>30){
			damage=0;
			resetDamageTimer=0;
		}
		
	}
	
	private void depopBonus()
	{
		if(timerDeath==0  && onView()) cri.play(2, 0.3f);
			while(damage>=(double)maxHp/12){
				randDegres=(int) (Math.random()*360);
				bonusIdDecrement();
				damage-=maxHp/12;
			}
			damage=0.0;
		
	}
	
	private void bonusIdDecrement()
	{
		do
		{
			/*System.out.println("test");
			System.out.println("nbC4 : "+nbC4);
			System.out.println("nbTrap : "+nbTrap);
			System.out.println("nbBomb : "+nbBomb);
			System.out.println("shieldOn : "+shieldOn);
			System.out.println("explosionlength : "+explosionLength);
			System.out.println("potentialSpeed : "+potentialSpeed);*/
			sendBonus=false;
				do{
					id= (int) (Math.random()*Math.random()*7);
					
				}while(id==5);
			
			if(id==0){
				
				if(nbC4>0){
					
					sendBonus=true;
					if(C4Placed){
						placeC4();
						nbC4--;
					}
					else
						nbC4--;
				}
			
			}
			
			else if(id==1){
				
				if(nbTrap>0) {
					sendBonus=true;
					nbTrap--;
					
				}
			}
			
			else if(id==2){
				
				if(nbBomb>1) {
					sendBonus=true;
					nbBomb--;
					
				}
			}
			else if(id==3){
				
				if(explosionLength>1){
					sendBonus=true;
					explosionLength--;
					
				}
			}
			else if(id==4){
				
				if(shieldOn){
				shieldHit=0;
				shieldOn=false;
				resistance=0;
				sendBonus=true;
				
				}
			}
			
			else if(id==6){
				if(potentialSpeed>3)
				{
					potentialSpeed-=0.5;
					sendBonus=true;
					
				}
				
			}
			
			if(sendBonus) {
				
				listElements.addElement(new BonusMoving(x,y, randDegres,id ));
			}
			
			if(potentialSpeed<=3 && !shieldOn && explosionLength<=1 && nbBomb<=1 && nbTrap<=0 && nbC4<=0) sendBonus=true;
			
		}while(!sendBonus);
	}

	public void update()
	{
		
		speed = potentialSpeed;
		
		setWanDirCommand(commandLeft, commandRight, commandUp, commandDown);
		
		if(commandC4)
			placeC4();
	
	
		if(cacheraufond) canard.play();
		//if(cacheraufond) bouftou.play(3, 1);
		if(commandTrap)
			placeTrap();
		if(commandBomb)
			placeBomb();
		
		if(!underTrap)
		{
			speed=potentialSpeed;
			moveGrid();
		}
		else
		{
			timerTrap--;
			if(timerTrap<=0)
			{
				underTrap=false;
			}
		}
		
		if(hp<=0 )
		{
			
			if(TimeToChuckNorris>0){
				timerDeath=90;
				mort.play();
			}
			numberBomberman--;
	
			destroy();
		}
		
		
		
	}
	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub
		
	}

	public void preRender()
	{
	}

	public void render()
	{
		if(isMoving())
		{
			if(hspeed < 0){
				prevDir = LEFT;
				anim_left.play();
				drawImage(anim_left.getImage(), (int)x, (int)y, 0);
				anim_left.update();
			}
			else if(hspeed > 0) {
				prevDir = RIGHT;
				anim_right.play();
				drawImage(anim_right.getImage(), (int)x, (int)y, 0);
				anim_right.update();
			}
			else if(vspeed < 0) {
				prevDir = UP;
				anim_back.play();
				drawImage(anim_back.getImage(), (int)x, (int)y, 0);
				anim_back.update();
			}
			else if(vspeed > 0) {
				prevDir = DOWN;
				anim_front.play();
				drawImage(anim_front.getImage(), (int)x, (int)y, 0);
				anim_front.update();
			}
		
		
		}
		else
		{
			anim_left.stop();
			anim_right.stop();
			anim_front.stop();
			anim_back.stop();
			drawImage(img_fix[prevDir], (int)x, (int)y, 0);
		}
		if(shieldOn) drawImage(armure, (int)x, (int)y, 270);
	}

	public void postRender()
	{
		drawLifeBar();
	}
	
	
}
