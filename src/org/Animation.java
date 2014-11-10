package org;

import org.newdawn.slick.Image;

public class Animation extends Global
{
	private double speed, id;
	private int lap;
	private boolean playing;
	private AnimationData animationData;
	
	public Animation(AnimationData animationData)
	{
		this.animationData = animationData;
		speed = 1.0;
		id = 0.0;
		playing = true;
		lap=0;
	}
	
	public void play()
	{
		playing = true;
	}
	
	public void pause()
	{
		playing = false;
	}
	
	public void stop()
	{
		playing = false;
		id = 0.0;
		lap=0;
	}
	
	public void update()
	{
		if(playing)
		{
			id+=speed;
			if(id >=animationData.getSize())
			{
				id %= animationData.getSize();
				lap++;
			}
		}
	}
	
	public void setId(double id)
	{
		this.id = id;
		this.id %= animationData.getSize();
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double getId()
	{
		return id;
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
	
	public Image getImage()
	{
		return animationData.getImage((int)id);
	}
	
	public int getLap()
	{
		return lap;
	}
}
