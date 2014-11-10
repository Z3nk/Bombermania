package org;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

public class Map extends Global
{
	private int[][] mapTab;
	
	private Image ground, wallLine[], wallEmpty[], mapImage;
	
	private int nbBlockX, nbBlockY, blockWidth, blockHeight, mapWidth, mapHeight;
	
	private final static int GROUND = 0;
	private final static int WALL = 1;
	private final static int DESTROYABLE_WALL = 2;
	private final static int PLAYER = 3;
	private final static int AI = 4;
	
	//variables de travail
	private int i, j, k, l, i2, j2, index;
	private Color c;
	
	public Map(int nbBlockX, int nbBlockY) throws SlickException
	{
		this.nbBlockX = nbBlockX;
		this.nbBlockY = nbBlockY;
		
		ground = new Image("data/images/ground.png");
		
		wallLine = createRotateImages(new Image("data/images/wallLine.png"));

		wallEmpty = createRotateImages(new Image("data/images/wallEmpty.png"));

		
		blockWidth = ground.getWidth();
		blockHeight = ground.getHeight();
		
		mapTab = new int[nbBlockX][nbBlockY];
		
		mapWidth = nbBlockX*blockWidth;
		mapHeight = nbBlockY*blockHeight;
	}
	
	public boolean isWall(int x, int y)
	{
		try 
		{
			return mapTab[x/blockWidth][y/blockHeight] == WALL;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public void destroyWall(int x, int y) throws SlickException
	{
		mapTab[x][y] = GROUND;
		
		Graphics g = mapImage.getGraphics();
		g.drawImage(ground, x*32, y*32);
		g.flush();
	}
	
	public int getMapWidth()
	{
		return mapWidth;
	}
	
	public int getMapHeight()
	{
		return mapHeight;
	}
	
	public int getBlockWidth()
	{
		return blockWidth;
	}
	
	public int getBlockHeight()
	{
		return blockHeight;
	}
	
	public int getNbBlockX()
	{
		return nbBlockX;
	}
	
	public int getNbBlockY()
	{
		return nbBlockY;
	}
	
	
	// TEST
	/*public void genererTestMap()
	{
		for(j=0; j < nbBlockY; j++)
		{
			for(i=0; i < nbBlockX; i++)
			{
				if(j== 0 || j == nbBlockY-1 || i == 0 || i == nbBlockX-1) mapTab[i][j] = WALL;
				else mapTab[i][j] = GROUND;
			}
		}
			
	}
	
	public void genererTestMap2()
	{
		for(j=0; j < nbBlockY; j++)
		{
			for(i=0; i < nbBlockX; i++)
			{
				if(Math.random() < 0.5) mapTab[i][j] = WALL;
				else mapTab[i][j] = GROUND;
			}
		}
			
	}*/
	// FIN TEST
	
	private Image[] createRotateImages(Image im)
	{
		Image[] result = new Image[4];
		ImageBuffer ib = new ImageBuffer(im.getWidth(), im.getHeight());
		
		result[0] = im;
		
		for(i = 0; i < im.getWidth(); i++)
		{
			for(j = 0; j < im.getHeight(); j++)
			{
				c=im.getColor(i, j);
				ib.setRGBA(im.getWidth()-j-1, i, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
			}
		}
		
		result[1] = ib.getImage();
		
		for(i = 0; i < im.getWidth(); i++)
		{
			for(j = 0; j < im.getHeight(); j++)
			{
				c=im.getColor(i, j);
				ib.setRGBA(im.getWidth()-1-i, im.getWidth()-1-j, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
			}
		}
		
		result[2] = ib.getImage();
		
		for(i = 0; i < im.getWidth(); i++)
		{
			for(j = 0; j < im.getHeight(); j++)
			{
				c=im.getColor(i, j);
				ib.setRGBA(j, im.getWidth()-1-i, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
			}
		}
		
		result[3] = ib.getImage();
			
		return result;
	}
	
	public void setMapImage()
	{
		ImageBuffer ib = new ImageBuffer(mapWidth, mapHeight);
		
		for(j=0; j < nbBlockY; j++)
		{
			for(i=0; i < nbBlockX; i++)
			{
				if(mapTab[i][j] == WALL)
				{
					for(index=0; index < 4; index++)
					{
						if(index == 0 || index == 2)
						{
							j2 = 0;
							if(index == 0) i2 = 1;
							else i2 = -1;
						}
						else
						{
							i2 = 0;
							if(index == 1) j2 = 1;
							else j2 = -1;
						}
					
					
						try
						{
							if(mapTab[i+i2][j+j2] == WALL)
							{
								for(l=0; l < blockHeight; l++)
								{
									for(k=0; k < blockWidth; k++)
									{
										c = wallEmpty[index].getColor(k, l);
										if(c.getAlpha() != 0) ib.setRGBA(i*blockWidth+k, j*blockHeight+l, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
									}
								}
							}
							else
							{
								for(l=0; l < blockHeight; l++)
								{
									for(k=0; k < blockWidth; k++)
									{
										c = wallLine[index].getColor(k, l);
										if(c.getAlpha() != 0) ib.setRGBA(i*blockWidth+k, j*blockHeight+l, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
									}
								}
							}
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							for(l=0; l < blockHeight; l++)
							{
								for(k=0; k < blockWidth; k++)
								{
									c = wallLine[index].getColor(k, l);
									if(c.getAlpha() != 0) ib.setRGBA(i*blockWidth+k, j*blockHeight+l, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
								}
							}
						}
					}
				}
				
				
				else
				{
					for(l=0; l < blockHeight; l++)
					{
						for(k=0; k < blockWidth; k++)
						{
							c = ground.getColor(k, l);
							ib.setRGBA(i*blockWidth+k, j*blockHeight+l, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
							
						}
					}
				}
			}
		}
		
		mapImage = ib.getImage();
	}
	
	public void loadMap(String path) throws InvalidMapFileException
	{
		FileReader f;
		try
		{
			f = new FileReader(path);
			int c;
			boolean alreadyPlayer = false;
			AIs = new Vector<AI>();
			for(j=0; j < nbBlockY; j++)
			{
				for(i=0; i < nbBlockX; i++)
				{
					
					c=f.read();
					
					switch(c)
					{
						case '0':
							mapTab[i][j] = GROUND;
							break;
							
						case '1':
							mapTab[i][j] = WALL;
							break;
							
						case '2':
							listElements.addElement(new DestroyableWall(i*blockWidth+16, j*blockHeight+16));
							mapTab[i][j] = DESTROYABLE_WALL;
							break;
							
						case '3':
						    if(alreadyPlayer)
						        throw new InvalidMapFileException("Only one player is allowed.");
						    
						    mapTab[i][j] = PLAYER;
						    player=new Player(i*blockWidth+16,j*blockHeight+16);
                            listElements.addElement(player);
                            alreadyPlayer = true;
                          
                            view.setFocus(player);
						    break;
						
						case '4':
						    mapTab[i][j] = AI;
						    AI ii = new AI(i*blockWidth+16,j*blockHeight+16);
						    AIs.add(ii);
						    listElements.addElement(ii);
						    break;
						    
						default:
						    throw new InvalidMapFileException("Invalid bloc type.");
					}
				}
			}
			f.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		
		}
	}
	
	public void saveMap(String path)
	{
		FileWriter f;
		try
		{
			f = new FileWriter(path);
			
			boolean[][] destWall = new boolean[nbBlockX][nbBlockY];
			
			Element e;
			for(int i = 0; i < listElements.size(); i++)
			{
				e = listElements.get(i);
				
				if(e.getType() == Type.DESTROYABLE_WALL)
				{
					destWall[(int)(e.getX()/32.0)][(int)(e.getY()/32.0)] = true;
				}
			}
			
			for(j=0; j < nbBlockY; j++)
			{
				for(i=0; i < nbBlockX; i++)
				{
				    switch(mapTab[i][j])
				    {
				        case AI:
    				        f.write('4');
    				        break;
    				        
				        case PLAYER:
    				        f.write('3');
    				        break;
    				        
				        case DESTROYABLE_WALL:
    						f.write('2');
    						break;
    						
				        case WALL:
    						f.write('1');
    						break;
    					
				        case GROUND:
    						f.write('0');
    					    break;
				    }
				}

			}
			f.close();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		
		}
	}
	
	public void updateEditor()
	{
		if(input.getMouseX() > 32 && input.getMouseX() < this.mapWidth-32 && input.getMouseY() > 32 && input.getMouseY() < this.mapHeight-32)
		{
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			{
				mapTab[(input.getMouseX()+view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = WALL;
				deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				
				mapTab[(mapWidth-input.getMouseX()-view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = WALL;
				deleteDestroyableWall((mapWidth-input.getMouseX()-view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				deleteBomberman((mapWidth-input.getMouseX()-view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				
				mapTab[(input.getMouseX()+view.getX())/blockWidth][(mapHeight-input.getMouseY()-view.getY())/blockHeight] = WALL;
				deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				
				mapTab[(mapWidth-input.getMouseX()-view.getX())/blockWidth][(mapHeight-input.getMouseY()-view.getY())/blockHeight] = WALL;
				deleteDestroyableWall((mapWidth-input.getMouseX()-view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				deleteBomberman((mapWidth-input.getMouseX()-view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
			}
			if(input.isMouseButtonDown(Input.MOUSE_MIDDLE_BUTTON))
			{
				mapTab[(input.getMouseX()+view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = GROUND;
				deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				
				mapTab[(mapWidth-input.getMouseX()-view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = GROUND;
				deleteDestroyableWall((mapWidth-input.getMouseX()-view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				deleteBomberman((mapWidth-input.getMouseX()-view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				
				mapTab[(input.getMouseX()+view.getX())/blockWidth][(mapHeight-input.getMouseY()-view.getY())/blockHeight] = GROUND;
				deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				
				mapTab[(mapWidth-input.getMouseX()-view.getX())/blockWidth][(mapHeight-input.getMouseY()-view.getY())/blockHeight] = GROUND;
				deleteDestroyableWall((mapWidth-input.getMouseX()-view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				deleteBomberman((mapWidth-input.getMouseX()-view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
			}
			if(input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
			{
			    mapTab[(input.getMouseX()+view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = DESTROYABLE_WALL;
				deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				listElements.addElement(new DestroyableWall(((input.getMouseX()+view.getX())/blockWidth)*blockWidth+16, ((input.getMouseY()+view.getY())/blockHeight)*blockHeight+16));
				
				mapTab[(mapWidth-input.getMouseX()-view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = DESTROYABLE_WALL;
				deleteDestroyableWall((mapWidth-input.getMouseX()-view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				deleteBomberman((mapWidth-input.getMouseX()-view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
				listElements.addElement(new DestroyableWall(((mapWidth-input.getMouseX()-view.getX())/blockWidth)*blockWidth+16, ((input.getMouseY()+view.getY())/blockHeight)*blockHeight+16));
				
				mapTab[(input.getMouseX()+view.getX())/blockWidth][(mapHeight-input.getMouseY()-view.getY())/blockHeight] = DESTROYABLE_WALL;
				deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				listElements.addElement(new DestroyableWall(((input.getMouseX()+view.getX())/blockWidth)*blockWidth+16, ((mapHeight-input.getMouseY()-view.getY())/blockHeight)*blockHeight+16));
				
				mapTab[(mapWidth-input.getMouseX()-view.getX())/blockWidth][(mapHeight-input.getMouseY()-view.getY())/blockHeight] = DESTROYABLE_WALL;
				deleteDestroyableWall((mapWidth-input.getMouseX()-view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				deleteBomberman((mapWidth-input.getMouseX()-view.getX())/blockWidth, (mapHeight-input.getMouseY()-view.getY())/blockHeight);
				listElements.addElement(new DestroyableWall(((mapWidth-input.getMouseX()-view.getX())/blockWidth)*blockWidth+16, ((mapHeight-input.getMouseY()-view.getY())/blockHeight)*blockHeight+16));
			}
			if(input.isKeyPressed(Input.KEY_J))
			{
			    if(player != null)
			    {
			        player.destroy();
			        mapTab[(int)player.getX()/blockWidth][(int)player.getY()/blockHeight] = GROUND;
			    }
			    mapTab[(input.getMouseX()+view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = PLAYER;
			    deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
			    deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
			    listElements.addElement(new Player(((input.getMouseX()+view.getX())/blockWidth)*blockWidth+16, ((input.getMouseY()+view.getY())/blockHeight)*blockHeight+16));
			}
		    if(input.isKeyPressed(Input.KEY_I))
		    {
		        mapTab[(input.getMouseX()+view.getX())/blockWidth][(input.getMouseY()+view.getY())/blockHeight] = AI;
		        deleteDestroyableWall((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
		        deleteBomberman((input.getMouseX()+view.getX())/blockWidth, (input.getMouseY()+view.getY())/blockHeight);
		        listElements.addElement(new AI(((input.getMouseX()+view.getX())/blockWidth)*blockWidth+16, ((input.getMouseY()+view.getY())/blockHeight)*blockHeight+16));
		    }
		}
	}
	
	private void deleteDestroyableWall(int x, int y)
	{
		Element e;
		for(int i = 0; i < listElements.size(); i++)
		{
			e = listElements.get(i);
			
			if(e.getType() == Type.DESTROYABLE_WALL)
			{
				if((int)(e.getX()/32.0) == x && (int)(e.getY()/32.0) == y)
					e.destroy();
			}
		}
	}
	
	private void deleteBomberman(int x, int y)
	{
	    Element e;
	    for(int i = 0; i < AIs.size(); i++)
	    {
           e = AIs.get(i);
           if((int)(e.getX()/32.0) == x && (int)(e.getY()/32.0) == y)
           {
               e.destroy();
               AIs.remove(i);
           }
	    }
	    
       if(player != null && (int)(player.getX()/32.0) == x && (int)(player.getY()/32.0) == y)
           player.destroy();
	}
	
	public void render()
	{
		if(!editorMode)
		{
			graphics.drawImage(mapImage, -view.getX(), -view.getY());
		}
		
		else
		{
			for(int i = 0; i < nbBlockX; i++)
			{
				for(int j = 0; j < nbBlockY; j++)
				{
				    switch(mapTab[i][j])
				    {
				        case WALL:
				            graphics.drawImage(wallLine[0], i*blockWidth-view.getX(), j*blockHeight-view.getY());
				            graphics.drawImage(wallLine[1], i*blockWidth-view.getX(), j*blockHeight-view.getY());
				            graphics.drawImage(wallLine[2], i*blockWidth-view.getX(), j*blockHeight-view.getY());
				            graphics.drawImage(wallLine[3], i*blockWidth-view.getX(), j*blockHeight-view.getY());
				            break;
				            
				        default:
				            graphics.drawImage(ground, i*blockWidth-view.getX(), j*blockHeight-view.getY());
				            break;
				    }
				}
			}
		}
	}
}
