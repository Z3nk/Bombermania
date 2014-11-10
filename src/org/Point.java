package org;

public class Point
{
	public final int x;
	public final int y;
	public final int value;
	public final int group;
	public final int distance;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.value = 0;
		this.group = 0;
		this.distance = 0;
	}
	
	public Point(int x, int y, int value, int group, int distance)
	{
		this.x = x;
		this.y = y;
		this.value = value;
		this.group = group;
		this.distance = distance;
	}
}
