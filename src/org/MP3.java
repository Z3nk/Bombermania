package org;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

/**
 * Cette classe permet de jouer des musiques MP3. Il est possible de démarrer la lecture, de l'arrêter, et la lecture en boucle est possible.
 * @author Martin Wetterwald
 *
 */
public class MP3
{
    private String filename;
    private Player player;
    private boolean isPlaying = false;

    /**
     * Initialise un nouveau fichier MP3 dont la lecture n'est pas lancée.
     * @param filename Le chemin vers le fichier MP3
     */
    public MP3(String filename)
    {
        this.filename = filename;
    }

    /**
     * Arrête la lecture d'un fichier MP3 précédemment lancé par la méthode play(boolean loop)
     */
    public void stop()
    {
    	if (player != null && isPlaying)
    	{
    		player.close();
    		isPlaying = false;
    	}
    }

    /**
     * Lance la lecture d'un fichier MP3.
     * @param loop Jouer la musique en boucle ?
     */
    public void play(final boolean loop)
    {
        try
        {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            isPlaying = true;
        }
        catch (Exception e)
        {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        new Thread()
        {
            public void run()
            {
                try
                {
                	Thread.currentThread().setName("Musique");
                	player.play();
                	if(loop && isPlaying)
                		play(loop);
                }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
    }
}

