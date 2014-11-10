package org;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Menu extends Global
{
	private int menuStart=windowHeight;
	private int widthRec=200;
	private int heightRec=50;
	private int posX=windowWidth/2-widthRec/2;
	private int posY=windowHeight/2-heightRec/2-heightRec*2;

	private Image[] NewGame=new Image[4];
	private Image[] NewGamePressed=new Image[4];
	private boolean loadingFinish=false,menu=false;
	private int C=-1;
	
	
	public Menu()
	{
		for(int i=1;i<=4;i++)
		{
		    NewGame[i-1]=getImage("bouton"+i);
		    NewGamePressed[i-1]=getImage("bouton"+i+"-clic");
		}
	}
	
	public void update() throws SlickException
	{
		if(loadingFinish && C==0)
		{
			editorMode = false;
            onGame=true;
            game.start("data/map/default.bmmap");
		}
		if(loadingFinish && C==1)
		{
			 mapEditorSave.setEnabled(false);
             currentMapEditorSaveTarget = new String();
             mapEditorSaveAs.setEnabled(true);
             editorMode = true;
             onGame = false;
             game.start("data/map/empty.bmmap");
		}
		if(loadingFinish && C==2)
		{
			menu=true;
		}
		
		if(loadingFinish && C==3)
		{
			WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		}
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			if(onButton(0))
			{
				C=0;					
				loadingFinish=true;
			}
			if(onButton(1))
			{
				C=1;
				loadingFinish=true;
			}
			if(onButton(2))
			{
				C=2;
				loadingFinish=true;
			}
			if(onButton(3))
			{
				C=3;
				loadingFinish=true;
			}
		}
	}
	
	
	public void render()
	{
		if(!menu)
		{
    		/*graphics.setColor(Color.red);
    		graphics.drawRect((windowWidth/2)-widthRec/2-2, (windowHeight/2)-heightRec/2-2,  widthRec+3, heightRec+3);
    		graphics.setColor(Color.gray);
    		graphics.fillRect((windowWidth/2)-widthRec/2, (windowHeight/2)-heightRec/2,  widthRec, heightRec);
    		graphics.setColor(Color.black);
    		graphics.drawString("Nouvelle Partie",windowWidth/2-widthRec/2, windowHeight/2-heightRec/2);*/
		    if(!onGame)
		    {
		        for(int i=0;i<=3;i++)
		            graphics.drawImage(NewGame[i], posX, posY+(heightRec+5)*i);		
		    }
		    if(loadingFinish)
		    {
		        graphics.drawImage(NewGamePressed[C], posX, posY+(heightRec+5)*C);
		    }
		}
		else
		{
			graphics.setColor(Color.white);
		    graphics.drawString("Ce jeu a été fait par des étudiants de deuxième année de DUT informatique ..", windowWidth/2-140, menuStart);
		    graphics.drawString("Développeurs :", windowWidth/2-40, menuStart+100);
		    graphics.drawString("Gaillard Damien", windowWidth/2-40, menuStart+125);
		    graphics.drawString("Gardize Joannick", windowWidth/2-40, menuStart+150);
		    graphics.drawString("Tirmarche Etienne", windowWidth/2-40, menuStart+175);
		    graphics.drawString("Sinnaeve Alexis", windowWidth/2-40, menuStart+200);
		    graphics.drawString("Wetterwald Martin", windowWidth/2-40, menuStart+225);
		    menuStart--;
		    if(input.isKeyPressed(Input.KEY_ESCAPE))m=new Menu();
		}
	}
	
	private boolean onButton(int button)
	{
		int cliqX=input.getMouseX();
		int cliqY=input.getMouseY();
	
		
		if(cliqX>=posX && cliqX<=posX+widthRec && cliqY>=posY+heightRec*button && cliqY<=posY+heightRec*button+heightRec)
			return true;		
		else return false;
	}
}
