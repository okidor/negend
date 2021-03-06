package avalone.negend;

import java.util.HashMap;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.CustomIndex2DList;
import avalone.api.lwjgl.Point;

public abstract class Entity 
{
	public Point pos,oldPos;
	public int tailleX; public int tailleY;
	public int vitX;
	public int vitY;
	public int nbJump;
	public int layer;
	protected int turned;
	public int tailleCase;
	public Chunk currentChunk; public Chunk currentChunkHead;
	protected Chunk[] cAround;
	//public boolean updateChunk;
	protected HashMap<Entity,Integer> tagTable;
	//private boolean destroy;
	private CustomIndex2DList<Chunk> cl;
	
	protected Entity(int posX,int posY,Chunk spawn)
	{
		pos = new Point(posX,posY);
		oldPos = new Point(posX,posY);
		cAround = new Chunk[9];
		vitY = 0;
		vitX = 0;
		//updateChunk = false;
		tailleCase = Const.tailleCase;
		currentChunk = spawn;currentChunkHead = spawn;
		layer = 0;
		turned = 1;
		tagTable = new HashMap<Entity,Integer>();
		//destroy = false;
		cl = spawn.map.getCustomList();
	}
	
	public void getChunkAround()
	{
		cAround[0] = cl.get(currentChunk.pos.x - 1, currentChunk.pos.y - 1);
		cAround[1] = cl.get(currentChunk.pos.x, currentChunk.pos.y - 1);
		cAround[2] = cl.get(currentChunk.pos.x + 1, currentChunk.pos.y - 1);
		cAround[3] = cl.get(currentChunk.pos.x - 1, currentChunk.pos.y);
		cAround[4] = currentChunk;
		cAround[5] = cl.get(currentChunk.pos.x + 1, currentChunk.pos.y);
		cAround[6] = cl.get(currentChunk.pos.x - 1, currentChunk.pos.y + 1);
		cAround[7] = cl.get(currentChunk.pos.x, currentChunk.pos.y + 1);
		cAround[8] = cl.get(currentChunk.pos.x + 1, currentChunk.pos.y + 1);
	}
	
	public void setAroundFromChunk(Chunk chunk)
	{
		cAround[0] = chunk.chunkBuffer[0];
		cAround[1] = chunk.chunkBuffer[1];
		cAround[2] = chunk.chunkBuffer[2];
		cAround[3] = chunk.chunkBuffer[3];
		cAround[4] = chunk.chunkBuffer[4];
		cAround[5] = chunk.chunkBuffer[5];
		cAround[6] = chunk.chunkBuffer[6];
		cAround[7] = chunk.chunkBuffer[7];
		cAround[8] = chunk.chunkBuffer[8];
	}
	
	/*public void destroy()
	{
		destroy = true;
	}
	
	public boolean isDestroyed()
	{
		return destroy;
	}*/
	
	protected Tile baseCase(int posX,int posY)
	{
		if(pos.y < 0)//comble l'erreur d'approximation des nb negatifs
		{
			posY--;
		}
		if(pos.x < 0)
		{
			posX--;// a verif
		}
		int chunkNumber = -1;
		if(posX >= 0)
		{
			if(posX < Const.tailleChunkX)
			{
				chunkNumber = 1;
			}
			else
			{
				chunkNumber = 2;
				posX = 0;
			}
		}
		else
		{
			chunkNumber = 0;
			posX = Const.tailleChunkX - 1;
		}
		
		if(posY >= 0)
		{
			if(posY < Const.tailleChunkY)
			{
				chunkNumber = chunkNumber + 3;
			}
			else
			{
				posY = 0;
			}
		}
		else
		{
			chunkNumber = chunkNumber + 6;
			posY = Const.tailleChunkY - 1;
		}
		//Const.debug("(Entity:baseCase):" + "chunkNumber = " + chunkNumber + ", posX = " + posX + ", posY = " + posY);
		//Const.debug("(Entity:baseCase):" + "chunk = " + cAround[chunkNumber]);
		return cAround[chunkNumber].cases[posX][posY];
	}
	
	public Tile headLeft()
	{
		return baseCase(pos.x/tailleCase,pos.y/tailleCase + 1);
	}
	
	public Tile headRight()
	{
		return baseCase((pos.x+tailleX-1)/tailleCase,pos.y/tailleCase + 1);
	}
	
	public Tile overHeadLeft()
	{
		return baseCase(pos.x/tailleCase,pos.y/tailleCase + 2);
	}
	
	public Tile overHeadRight()
	{
		return baseCase((pos.x+tailleX-1)/tailleCase,pos.y/tailleCase + 2);
	}
	
	public Tile currentCaseLeft()
	{
		//int test = pos.y/tailleCase;
		//Const.debug("currentCase, caseY = " + test);
		return baseCase(pos.x/tailleCase,pos.y/tailleCase);
	}
	
	public Tile currentCaseRight()
	{
		return baseCase((pos.x+tailleX-1)/tailleCase,pos.y/tailleCase);
	}
	
	public Tile underFeetLeft()
	{
		return baseCase(pos.x/tailleCase,pos.y/tailleCase - 1);
	}
	
	public Tile underFeetRight()
	{
		return baseCase((pos.x+tailleX-1)/tailleCase,pos.y/tailleCase - 1);
	}
	
	public Tile leftOfFeet()
	{
		return baseCase(pos.x/tailleCase - 1,pos.y/tailleCase);
	}
	
	public Tile rightOfFeet()
	{
		return baseCase((pos.x+tailleX-1)/tailleCase + 1,pos.y/tailleCase);
	}
	
	public Tile leftOfHead()
	{
		return baseCase(pos.x/tailleCase - 1,pos.y/tailleCase + 1);
	}
	
	public Tile rightOfHead()
	{
		return baseCase((pos.x+tailleX-1)/tailleCase + 1,pos.y/tailleCase + 1);
	}
	
	public abstract void movements(AvaloneGLAPI glapi);
	
	public static double distance(Point p1,Point p2)
	{
		return Math.sqrt( (p1.x-p2.x) * (p1.x-p2.x) + (p1.y-p2.y) * (p1.y-p2.y));
	}
	
	public void changeChunk(Chunk newC,int n,AvaloneGLAPI glapi)
	{
		currentChunk = newC;
		//updateChunk = true;
		currentChunk.map.setChunkAround(this);
		if(n == 1)
		{
			pos.x = (Const.tailleChunkX-1)*tailleCase;
		}
		else if(n == 2)
		{
			pos.x = 0;
		}
		else if(n == 3)
		{
			//System.out.println("warning chunkY changed (-1)");
			pos.y = (Const.tailleChunkY-1)*tailleCase;
		}
		else if(n == 4)
		{
			//System.out.println("warning chunkY changed (+1)");
			pos.y = 0;
		}
		else
		{
			System.out.println("warning, illegal chunk change");
		}
		oldPos.setCoords(pos.x,pos.y);
	}
}
