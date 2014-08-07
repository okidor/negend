package avalone.negend;

import java.util.ArrayList;
import java.util.HashMap;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Point;

public class Renderer 
{
	private AvaloneGLAPI glapi;
	private Ore[] ores;
	private ArrayList<String[]> blocks;
	private int offsetX;
	private int offsetY;
	private ArrayList<ArrayList<String>> idAssoc;
	private HashMap<String,Integer> id;
	
	public Renderer(AvaloneGLAPI glapi,Ore[] ores,String filePath)
	{
		this.glapi = glapi;
		this.ores = ores;
		GameFile game = new GameFile(filePath);
		blocks = game.al;
		offsetX = 0;
		offsetY = 0;
		id = new HashMap<String,Integer>();
		//customTextures();
	}
	
	public AvaloneGLAPI getAPI()
	{
		return glapi;
	}
	
	/*private void customTextures()
	{
		for(int i = 0;i < blocks.size();i++)
		{
			String[] s = blocks.get(i);
		}
	}*/
	
	public void setOffset(int offsetX,int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	/*public String getTextureFromID(int id,int subID)
	{
		for(int i = 0;i < blocks.size();i++)
		{
			String[] s = blocks.get(i);
			if(s.length > 4 && s[4].equals("debug"))
			{
				System.out.println("debugging id : " + s[0]);
			}
			if(s[0].equals(Integer.toString(id)) && s[1].equals(Integer.toString(subID)))
			{
				return s[2];
			}
		}
		System.out.println("no texture found for id " + id + " and subID " + subID);
		return "default.png";
	}*/
	
	/*public String getSolidityFromID(int id,int subID)
	{
		if(id == 0)
		{
			return "nonsolid";
		}
		for(int i = 0;i < blocks.size();i++)
		{
			String[] s = blocks.get(i);
			if(s[0].equals(Integer.toString(id)) && s[1].equals(Integer.toString(subID)))
			{
				if(s.length > 4)
				{
					return s[4];
				}
				else
				{
					return "solid";
				}
			}
		}
		return "solid";
	}*/
	
	/*public int getLayerFromID(int id,int subID)
	{
		for(int i = 0;i < blocks.size();i++)
		{
			String[] s = blocks.get(i);
			if(s[0].equals(Integer.toString(id)) && s[1].equals(Integer.toString(subID)))
			{
				return Integer.decode(s[3]);
			}
		}
		return 0;
	}*/
	
	/*public void drawGroup(ArrayList<Point> p1s,ArrayList<Point> p2s,int blockID,int subID)
	{
		glapi.drawTexturedMultiRect(p1s,p2s,getTextureFromID(blockID,subID));
	}*/
	
	public void draw(Point p1,Point p2,Tile tile)
	{
		if(tile.subID >= 0)
		{
			draw(p1,p2,tile.getTexture());
		}
		else
		{
			int newSub = -tile.subID - 1 - tile.ore.id;
			int oldSub = tile.subID;
			tile.subID = newSub;
			draw(p1,p2,tile.getTexture());
			tile.subID = oldSub;
			addMineral(tile);
		}
		if(tile.debug)
		{
			glapi.drawAlphaRect(tile.coord, tile.coord.clone(Const.tailleCase),"RED",0.5f);
			glapi.clearFilter();
			tile.debug = false;
		}
		if(tile.debug2)
		{
			glapi.drawAlphaRect(tile.coord, tile.coord.clone(Const.tailleCase),"GREEN",0.5f);
			glapi.clearFilter();
			tile.debug2 = false;
		}
		if(tile.debug3)
		{
			glapi.drawAlphaRect(tile.coord, tile.coord.clone(Const.tailleCase),"BLUE",0.5f);
			glapi.clearFilter();
			tile.debug3 = false;
		}
	}
	
	/*public void drawItem(Point p1,Point p2,int id, int subID)
	{
		draw(p1,p2,Block.getBlock(id).getTexture(subID));
	}*/
	
	public void draw(Point p1,Point p2,String texture)
	{
		Point p1_2 = p1.clone(offsetX, offsetY);
		Point p2_2 = p2.clone(offsetX, offsetY);
		glapi.drawTexturedRect(p1_2,p2_2,texture);
	}
	
	public void drawText(Point p1,Point p2,String text,String c)
	{
		glapi.drawText(p1,p2,text,c);
	}
	
	public void drawText(Point p1,Point p2,int number,String c)
	{
		glapi.drawText(p1,p2,number,c);
	}
	
	public void drawTextGroup(ArrayList<Point> p1s,ArrayList<Point> p2s,String text,String c)
	{
		for(int i = 0;i < p1s.size();i++)
		{
			glapi.drawText(p1s.get(i),p2s.get(i),text,c);
		}
	}
	
	public void addMineral(Tile tile)
	{
		Point p1 = tile.coord.clone(0);
		drawMineral(p1,p1.clone(Const.tailleCase),tile.ore.color);
	}
	
	public void drawMineral(Point p1,Point p2,float[] color)
	{
		glapi.setFilter(color);
		draw(p1, p2,"minerai.png");
		glapi.clearFilter();
	}
	
	public void drawShadow(Point p,float shadow)
	{
		Point realP = p.clone(offsetX,offsetY);
		glapi.drawAlphaRect(realP, realP.clone(Const.tailleCase),"BLACK",shadow);
	}
	
	public void drawChunkLimit()
	{
		glapi.unbindTexture();
		glapi.drawAlphaRect(new Point(0,0),new Point(1,Const.tailleFenY),"red",0.5f);
		glapi.drawAlphaRect(new Point(Const.tailleFenX-1,0),new Point(Const.tailleFenX,Const.tailleFenY),"red",0.5f);
		glapi.drawAlphaRect(new Point(),new Point(Const.tailleFenX,1),"red",0.5f);
		glapi.drawAlphaRect(new Point(0,Const.tailleFenY-1),new Point(Const.tailleFenX,Const.tailleFenY),"red",0.5f);
		glapi.clearFilter();
	}
	
	public void addMarker()
	{
		
	}
	
	public void drawShadowGroup(ArrayList<Point> p1s,ArrayList<Point> p2s,int light)
	{
		for(int i = 0;i < p1s.size();i++)
		{
			p1s.set(i,p1s.get(i).clone(offsetX,offsetY));
			p2s.set(i,p2s.get(i).clone(offsetX,offsetY));
		}
		float shadow = 1-((float)light/(float)Const.maxLight);
		glapi.drawMultiAlphaRect(p1s, p2s, "BLACK", shadow);
	}
}
