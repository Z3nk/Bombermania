package org;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AnimationData
{
	private Image[] images;	
	
	public AnimationData(String[] imagesName) throws SlickException
	{
		images = new Image[imagesName.length];
		
		for(int i = 0; i < imagesName.length; i++)
		{
			images[i] = new Image("data/images/"+imagesName[i]+".png");
		}	
	}
	
	public int getSize()
	{
		return images.length;
	}
	
	public Image getImage(int i)
	{
		return images[i];
	}
}
