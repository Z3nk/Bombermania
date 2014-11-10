package org;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


public class BomberManiaGame extends Global
{
	private boolean popBoss=false, musicStopLaunch = false;
	private boolean displayEvaluation = false;
	private String msg, title;
	
	
	public BomberManiaGame() throws SlickException
	{
		listElements = new ListElements();
		map = new Map(NBBLOCKX, NBBLOCKY);
        showIntro();
	}
	
	public void update()
	{
		if(!editorMode && !onGame)
		{
			try {
				m.update();
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(stopMusic)
			{
				musicPlayer.stop();
				musicStopLaunch=true;
			}
			if(!stopMusic && musicStopLaunch)
			{
				musicPlayer.play(true);
				musicStopLaunch=false;
			}
			if(TimeToChuckNorris==0 &&!popBoss)
			{
				
				listElements.addElement(new Robot(map.getBlockWidth()*19+16,map.getBlockHeight()*14+16));
				popBoss=true;
			}
			aICalculator.evaluateMap();
			if(onGame)
			{
				if(TimeToChuckNorris>0) {
					System.out.println(TimeToChuckNorris);
					TimeToChuckNorris--;
				}
				if(timerBoufTou>0) timerBoufTou--;	
				if(timerDeath>0)timerDeath--;
				if(Bomberman.numberBomberman == 1)
				{
					win = true;
				//	onGame=false;
					if(player.alive())
					{
						title = "Victoire";
						msg = "Vous avez gagné !"; 
					}
					else
					{
						for(int i =0; i < AIs.size(); i++)
						{
							if(AIs.get(i).alive())
							{
								title = "Défaite";
								msg = AIs.get(i).getName()+" a gagné !";
							}
						}
					}
				}
			}
			
			listElements.update();
			view.update();
			if(editorMode)
				map.updateEditor();
		}
	}
	
	public void render()
	{
		if(!editorMode && !onGame)
		{
			m.render();
		}
		else
		{
			map.render();
			graphics.resetTransform();
			listElements.render();
			if(displayEvaluation) displayEvaluation();
		}
	}
	
	private void displayEvaluation()
	{
       graphics.setColor(Color.white);

        for(int i = 0; i < NBBLOCKX; i++)
        {
            for(int j = 0; j < NBBLOCKY; j++)
            {
                if(aICalculator.getEvaluationMap(i, j) != AICalculator.WALL_VALUE)
                    graphics.drawString((new Integer(aICalculator.getEvaluationMap(i, j))).toString(), i*32+10-view.getX(), j*32+10-view.getY());
                else
                    graphics.drawString("M", i*32+3-view.getX(), j*32+10-view.getY());
            }
        }
	}
	
	protected void showIntro()
	{
		playerDeath=false;
		editorMode = false;
		onGame = false;
	    close.setEnabled(false);
	    mapEditorSave.setEnabled(false);
	    mapEditorSaveAs.setEnabled(false);
	    reset();
	    /*try
	    {
	        map.loadMap("data/map/empty.bmmap");
	        map.setMapImage();
	        for(int i = 0; i < 200; i++)
	        {
	            if(i % 20 == 0)
	                listElements.addElement(new ExempleMonstre(32+Math.random()*(windowWidth-32*2), 32+Math.random()*(windowHeight-32*2)));
	            listElements.addElement(new ExempleBonhomme(32+Math.random()*(windowWidth-32*2), 32+Math.random()*(windowHeight-32*2)));
	        }
	    }
	    catch(InvalidMapFileException ex)
	    {

	    }*/
	    m=new Menu();

	}
	
	public void start(String mapFilePath)
	{
		popBoss=false;
		win=false;
		canvas.requestFocus();
		view = new View();
		timerDeath=0;
		timerBoufTou=900;
		TimeToChuckNorris=9*20*5*10;/*0*20*5*/;
		
	    close.setEnabled(true);
	    if(!editorMode)
	    {
	        mapEditorSaveAs.setEnabled(false);
	        mapEditorSave.setEnabled(false);
	    }
	    
	    reset();
	    try
        {
            map.loadMap(mapFilePath);
            map.setMapImage();   
            if(!editorMode)
            {
                try
                {
                	
                    (new Sound("data/sounds/startBattle.wav")).play();
                }
                catch (SlickException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                musicPlayer = new MP3("data/musics/musique-fond.mp3");
                musicPlayer.play(true);
                
                //view.setFocus(player);
                //Démarrage des IAs.
                for(int i = 0; i < AIs.size(); i++)
                    AIs.get(i).start();
            }
            else
            {
                musicPlayer = new MP3("data/musics/musique-editor.mp3");
                musicPlayer.play(true);
            }
        }
        catch (InvalidMapFileException e1)
        {
            JOptionPane.showMessageDialog(frame, "Le fichier de carte est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            game.showIntro();
        }
	}
	
	private void reset()
	{
	    //Extinction du thread de la musique
	    if(musicPlayer != null)
	        musicPlayer.stop();
	    
	    //Extinction de tous les threads des IAs.
	    for(int i = 0; i < listElements.size(); i++)
	    {
	        Element e = listElements.get(i);
	        if(e.type == Type.BOMBERMAN)
	            e.alive = false;
	    }
	    
	    aICalculator = new AICalculator();
	   // view = new View();
	    listElements = new ListElements();
	}
}
