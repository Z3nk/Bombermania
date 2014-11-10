package org;

import java.util.LinkedList;
import java.util.Vector;

public class ListElements extends Global
{
	private Vector<Element> list;
	private LinkedList<Element> newList;
	private int size;
	
	public ListElements()
	{
		list = new  Vector<Element>();
		newList = new  LinkedList<Element>();
	}
	
	public Vector<Element> getCopy()
	{
		return new Vector<Element>(list);
	}
	
	public void addElement(Element elem)
	{
		list.add(elem);
	}
	
	private void  updateList()
	{
		int i;
		size = list.size();
		
		for(i=0; i < size; i++)
		{
			if(!list.get(i).alive())
			{
				list.remove(i);
				i--;
				size--;
			}
		}
		
		/*while(!newList.isEmpty())
		{
			list.add(newList.removeFirst());
		}*/
	}
	
	public void update()
	{
		
		int i;
		size = list.size();
		
		for(i=0; i < size; i++)
		{
			list.get(i).xprevious = list.get(i).x;
			list.get(i).yprevious = list.get(i).y;
			
			list.get(i).preUpdate();
		}
		
		for(i=0; i < size; i++)
		{
			list.get(i).update();
		}
		
		for(i=0; i < size; i++)
		{
			list.get(i).postUpdate();
		}
		
		updateList();
	}
	
	public void render()
	{
		int i;
		size = list.size();
		
		for(i=0; i < size; i++)
		{
			list.get(i).preRender();
		}
		
		for(i=0; i < size; i++)
		{
			list.get(i).render();
		}
		
		for(i=0; i < size; i++)
		{
			list.get(i).postRender();
		}
	}
	
	public int size()
	{
		return list.size();
	}
	
	public Element get(int i)
	{
		try{
			return list.get(i);
		}catch(NullPointerException e){
			return null;
		}
	}
}