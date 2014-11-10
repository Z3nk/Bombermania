package org;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;

public abstract class Global
{
	protected static double alpha = 0;
	protected static final int RIGHT = 0;
	protected static final int DOWN = 1;
	protected static final int LEFT = 2;
	protected static final int UP = 3;
	protected static final int NOTHING = 4;
	protected static int timerBoufTou=1200;
	protected static boolean playerDeath=false;
	protected static int timerDeath=0;
	protected static int maxSound=300;
	protected static int windowWidth, windowHeight;
	protected static boolean win = false;
	protected static Menu m;
	
	protected static Hashtable<String, Image> images = new Hashtable<String, Image>();
	protected static Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();
	protected static Hashtable<String, AnimationData> animationsData = new Hashtable<String, AnimationData>();
	
	protected static ListElements listElements;
	protected static Map map;
	protected static View view;
	protected static AICalculator aICalculator;
	protected static Vector<AI> AIs = new Vector<AI>();
	protected static Player player;
	
	protected static JFrame frame;
	protected static CanvasGameContainer canvas;
	protected static GameContainer gameContainer;
	protected static Input input;
	protected static Graphics graphics;
	
	protected static MP3 musicPlayer;
	


	protected static boolean onGame = false;
	protected static BomberManiaGame game;
	protected static boolean editorMode = false;
	protected static boolean leave=false;
	protected static int TimeToChuckNorris;

    protected final static int NBBLOCKX = 40;
    protected final static int NBBLOCKY = 30;
    protected static boolean justLaunch=false;
    protected static boolean stopMusic=false;

	
    protected static JMenuItem close = new JMenuItem("Fermer", new ImageIcon("data/images/menu/door.png"));
    protected static JMenuItem mapEditorSave = new JMenuItem("Enregistrer", new ImageIcon("data/images/menu/disk.png"));
    protected static JMenuItem mapEditorSaveAs = new JMenuItem("Enregistrer sous...", new ImageIcon("data/images/menu/disk.png"));
    protected static String currentMapEditorSaveTarget = new String();
    
    protected static JFileChooser chooser = new JFileChooser();
    
	protected void drawImage(Image im, int x, int y, float rotation)
	{
		im.setRotation(rotation);
		graphics.drawImage(im, x-im.getWidth()/2-view.getX(), y-im.getHeight()/2-view.getY());
	}
	
	
	protected void addImage(String name) throws SlickException
	{
		images.put(name, new Image("data/images/"+name+".png"));
	}
	
	protected Image getImage(String name)
	{
		return images.get(name);
	}
	
	protected void addSound(String name) throws SlickException
	{
		sounds.put(name, new Sound("data/sounds/"+name+".wav"));
	}
	
	protected Sound getSound(String name)
	{
		return sounds.get(name);
	}
	
	protected void addAnimationData(String name, String... imagesName) throws SlickException
	{
		animationsData.put(name, new AnimationData(imagesName));
	}
	
	protected AnimationData getAnimationData(String name)
	{
		return animationsData.get(name);
	}
}
