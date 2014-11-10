package org;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class BomberMania extends Global implements Game
{
	private boolean launchMusic=false;
	private int timerMusic=75;
	
	public BomberMania(int ww, int wh)
	{
	    chooser.setCurrentDirectory(new File("data/map"));
	    chooser.addChoosableFileFilter(new MapFilter());
	    chooser.setAcceptAllFileFilterUsed(false);
		windowWidth = ww;
		windowHeight = wh;
	}
	
	public boolean closeRequested()
	{
		return true;
	}

	public String getTitle()
	{
		return "BomberMania";
	}
	
	public void init(GameContainer c) throws SlickException
	{
	    c.setAlwaysRender(true);
		gameContainer = c;
		input = c.getInput();
		graphics = c.getGraphics();		
		InitData();
		game=new BomberManiaGame();
		//m=new Menu();
	}
	
	private void InitData() throws SlickException
	{
		for(int i = 1; i <= 4; i++)
		{
			addAnimationData("J"+i+"_"+"droite", "J"+i+"_"+"droit_1", "J"+i+"_"+"droit_2", "J"+i+"_"+"droit_3", "J"+i+"_"+"droit_2");
			addAnimationData("J"+i+"_"+"gauche", "J"+i+"_"+"gauche_1", "J"+i+"_"+"gauche_2", "J"+i+"_"+"gauche_3", "J"+i+"_"+"gauche_2");
			addAnimationData("J"+i+"_"+"dos", "J"+i+"_"+"dos_1", "J"+i+"_"+"dos_2", "J"+i+"_"+"dos_3", "J"+i+"_"+"dos_2");
			addAnimationData("J"+i+"_"+"face", "J"+i+"_"+"face1", "J"+i+"_"+"face2", "J"+i+"_"+"face3", "J"+i+"_"+"face2");
		}
		
		addAnimationData("canon","canon1", "canon2", "canon3", "canon2","canon1","canon4", "canon5","canon4");
		addAnimationData("bomb","bombe-1", "bombe-2", "bombe-3", "bombe-4");
		
		addAnimationData("explosion_line", "explosion_line1", "explosion_line2", "explosion_line3", "explosion_line4");
		addAnimationData("explosion_border", "explosion_border1", "explosion_border2", "explosion_border3", "explosion_border4");		
		addAnimationData("C4", "C4_1","C4_2", "C4_3","C4_4", "C4_5");
		addAnimationData("explo", "explo1", "explo2", "explo3", "explo4", "explo5", "explo6");
		addAnimationData("pied","pied1","pied2","pied3","pied4","pied3","pied2","pied1","pied5","pied6","pied7");
		
		addSound("robot");
		addSound("robot2");
		addSound("bouftou2");
		addSound("bouftou");
		addSound("rire");
		addSound("banzai");
		addSound("banzai2");
		addSound("canard");
		addSound("banzai4");
		addSound("banzai5");
		addSound("banzai6");
		addSound("cri");
		addSound("cri2");
		addSound("mort");
		addSound("Taunt");
		addSound("Taunt2");
		addSound("C4");
		addSound("piege");
		addSound("explo");
		for(int i = 1; i <= 4; i++)
		{
			addImage("J"+i+"_dos_2");
			addImage("J"+i+"_droit_2");
			addImage("J"+i+"_gauche_2");
			addImage("J"+i+"_face2");
		}
		

		for(int i=1;i<=4;i++){
			
		addImage("bouton"+i);
		addImage("bouton"+i+"-clic");
		}
		
		addImage("pied1");
		addImage("tir_robot");
		addImage("canon1");
		addImage("bouftou_ball");
		addImage("corps");
		addImage("bonus0");
		addImage("bonus1");
		addImage("bonus2");
		addImage("bonus3");
		addImage("bonus4");
		addImage("bonus5");
		addImage("bonus6");
		addImage("explosion2");
		
		
		addImage("mur");
		addImage("piege");
		addImage("piege2");
		addImage("shield");
		
		addAnimationData("bouftou", "bouftou1", "bouftou2", "bouftou3", "bouftou2");
	}

	public void render(GameContainer c, Graphics g) throws SlickException 
	{
		game.render();
	}

	public void update(GameContainer c, int alpha) throws SlickException
	{
			
		game.update();
	}
	
	public static void main(String args[])
	{
		try
		{
	        int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	          
	        windowWidth = (screenWidth < NBBLOCKX*32 ? screenWidth-10 : NBBLOCKX*32);
	        windowHeight = (screenHeight < NBBLOCKY*32 ? screenHeight-100 : NBBLOCKY*32);
		    BomberMania bomberMania = new BomberMania(windowWidth, windowHeight);
		    canvas = new CanvasGameContainer(bomberMania);
		    canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
		    canvas.getContainer().setTargetFrameRate(30);
		    canvas.getContainer().setShowFPS(true);
		    canvas.setBounds(0, 25, windowWidth, windowHeight);
		    
		    JPanel total = new JPanel(null);
		    
		    JMenuItem defaultMap = new JMenuItem("Nouvelle partie", new ImageIcon("data/images/menu/page_white.png"));
		    defaultMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		    defaultMap.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    editorMode = false;
                    onGame=true;
                    game.start("data/map/default.bmmap");
                }
            });
		    
		    JMenuItem loadMap = new JMenuItem("Ouvrir une carte...", new ImageIcon("data/images/menu/folder.png"));
		    loadMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		    loadMap.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int retour = chooser.showOpenDialog(frame);          
                    if(retour == JFileChooser.APPROVE_OPTION)
                    {
                        editorMode = false;
                        onGame = true;
                        game.start(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            });
		    
		    
		    close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		    close.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    game.showIntro();
                }
            });
		    
		    JMenuItem exit = new JMenuItem("Quitter", new ImageIcon("data/images/menu/door_out.png"));
		    exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		    exit.addActionListener(new ActionListener()
		    {
		        @Override
		        public void actionPerformed(ActionEvent e)
		        {
		        	WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		        }
		    });
		    
		    JMenu fichier = new JMenu("Jeu");
		    
		    fichier.add(defaultMap);
		    fichier.add(loadMap);
		    fichier.add(close);
		    fichier.addSeparator();
		    fichier.add(exit);

		    
		    JMenuItem mapEditorNew = new JMenuItem("Nouveau", new ImageIcon("data/images/menu/page_white.png"));
		    mapEditorNew.addActionListener(new ActionListener()
            {
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    mapEditorSave.setEnabled(false);
                    currentMapEditorSaveTarget = new String();
                    mapEditorSaveAs.setEnabled(true);
                    editorMode = true;
                    onGame = false;
                    game.start("data/map/empty.bmmap");
                }
            });
		    
		    JMenuItem mapEditorOpen = new JMenuItem("Ouvrir...", new ImageIcon("data/images/menu/folder.png"));
		    mapEditorOpen.addActionListener(new ActionListener()
	        {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int retour = chooser.showOpenDialog(frame);          
                    if(retour == JFileChooser.APPROVE_OPTION)
                    {
                        editorMode = true;
                        onGame = false;
                        game.start(chooser.getSelectedFile().getAbsolutePath());
                        mapEditorSave.setEnabled(true);
                        mapEditorSaveAs.setEnabled(true);
                        currentMapEditorSaveTarget = chooser.getSelectedFile().getAbsolutePath();
                    }
                }
	        });
		    

		    mapEditorSave.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    map.saveMap(currentMapEditorSaveTarget);
                }
            });
		    mapEditorSave.setEnabled(false);
	    
		    mapEditorSaveAs.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int retour = chooser.showSaveDialog(frame);          
                    if(retour == JFileChooser.APPROVE_OPTION)
                    {
                        String fileName = chooser.getSelectedFile().getAbsolutePath();
                        String extension = Utils.getExtension(new File(fileName));
                        if(extension == null || !extension.equals(new String("bmmap")))
                            fileName += ".bmmap";
                        
                        map.saveMap(fileName);
                        mapEditorSave.setEnabled(true);
                        currentMapEditorSaveTarget = chooser.getSelectedFile().getAbsolutePath();
                    }
                }
            });
		    
		    
		    mapEditorSaveAs.setEnabled(false);
		    
	        JMenu mapEditor = new JMenu("Éditeur de cartes");
		    mapEditor.add(mapEditorNew);
		    mapEditor.add(mapEditorOpen);
		    mapEditor.add(mapEditorSave);
		    mapEditor.add(mapEditorSaveAs);
	        
		    JMenuItem credits = new JMenuItem("Crédits");
		    credits.addActionListener(new ActionListener()
            {
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(frame, "Ce jeu a été développé par des étudiants de deuxième année de DUT informatique :\nGaillard Damien\nGardize Joannick\nTirmarche Étienne\nSinnaeve Alexis\nWetterwald Martin", "Crédits", JOptionPane.INFORMATION_MESSAGE);
                }
            });
		    
	        JMenu help = new JMenu("?");
	        help.add(credits);
		    
		    JMenuBar menuBar = new JMenuBar();
		    menuBar.setPreferredSize(new Dimension(windowWidth, 25));
		    menuBar.add(fichier);
		    menuBar.add(mapEditor);
		    menuBar.add(help);
		    menuBar.setBounds(0, 0, (int)menuBar.getPreferredSize().getWidth(), (int)menuBar.getPreferredSize().getHeight());
		    total.add(menuBar);
		    total.add(canvas);
		    total.setPreferredSize(new Dimension(windowWidth, windowHeight+25));
		    
		    frame = new JFrame();
		    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    frame.setResizable(false);
		    
		    frame.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent arg0) {
					
				}
				
				@Override
				public void windowIconified(WindowEvent arg0) {
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent arg0) {
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent arg0) {
					
				}
				
				@Override
				public void windowClosing(WindowEvent arg0) {
			         canvas.setEnabled(false);
			         canvas.dispose();
					
				}
				
				@Override
				public void windowClosed(WindowEvent arg0) {
					System.exit(0);
					
				}
				
				@Override
				public void windowActivated(WindowEvent arg0) {
					canvas.requestFocus();
				}
			});
		    
		    frame.add(menuBar);
		    frame.add(total);
		    frame.pack();
		    frame.setLocationRelativeTo(null);
		    frame.setVisible(true);
		    canvas.setFocusable(true);
		    canvas.requestFocus();
            canvas.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
