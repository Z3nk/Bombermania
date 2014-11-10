package org;

public class Event
{
	private String name;
	private Element source;
	private double value;
	
	public Event(String name, Element source)
	{
		this.name = name;
		this.source = source;
		value = 0.0;
	}

	public Event(String name, Element source, double value)
	{
		this.name = name;
		this.source = source;
		this.value = value;
	}

	public double getValue()
	{
		return value;
	}

	public String getName()
	{
		return name;
	}
	
	public Element getSource()
	{
		return source;
	}
}
