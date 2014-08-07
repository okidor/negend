package avalone.negend;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.CustomIndex2DList;
import avalone.api.lwjgl.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Map 
{
	private CustomIndex2DList<Chunk> cl;
	//private HashSet<Entity> globalEntityList;
	public Random rand;
	private Point origin;
	private Point end;
	public Player play;
	public Ore[] ores;
	private AvaloneGLAPI glapi;
	private Renderer rend;
	private GameFile game;
	private boolean setLight = true;
	private Point center;
	private String gameName;
	private boolean newGame;
	
	public static boolean allowedToDraw = true;

	public Map(int tailleX,int tailleY,AvaloneGLAPI glapi,Renderer rend,Ore[] ores,String gameName,boolean newGame)
	{
		this.gameName = gameName;
		this.newGame = newGame;
		Const.debug("(Map): reading config file");
		initConfig();
		center = new Point(Const.tailleChunkX*Const.tailleCase/2,Const.tailleChunkY*Const.tailleCase/2);
		cl = new CustomIndex2DList<Chunk>();
		//globalEntityList = new HashSet<Entity>();
		rand = new Random();
		origin = new Point(0,0);
		end = new Point(tailleX,tailleY);
		this.ores = ores;
		this.glapi = glapi;
		this.rend = rend;
		if(newGame)
		{
			Const.debug("(Map): generating world");
			newGame();
		}
		else
		{
			Const.debug("(Map): loading a world");
			SaveHandler save = new SaveHandler(this,"save" + File.separator + gameName);
			cl = save.load(rend);
			SpawnChunk spawnPoint = (SpawnChunk) cl.get(0,0);
			play = save.getLoadedPlayer();
			firstScroll(play);
			//play.currentChunk.playerList.add(play);
			spawnMob(spawnPoint);
		}
	}
	
	public void newGame()
	{
		SpawnChunk spawnPoint = new SpawnChunk(this,new Point(),setLight,rend,true);
		Const.debug("(Map): generating spawn chunk");
		generate(spawnPoint);
		Const.debug("(Map): generating player");
		spawnPlayer(spawnPoint);
		Const.debug("(Map): generating a mob");
		spawnMob(spawnPoint);
	}
	
	public void initConfig()
	{
		game = new GameFile(Const.configPath);
		String s[] = game.al.get(0);
		setLight = Boolean.valueOf(s[2]);
	}
	
	public void generate(Chunk c)
	{
		cl.add(c.pos.x, c.pos.y, c);
		c.generate(rand,ores);
	}
	
	public void actions()
	{
		setChunkAround(play);
		//checkEntities();
		Physic.verifyGrav(play,glapi);
		play.live(glapi);
		draw();
		//System.out.println("player drawn at chunk coords: x = " + play.currentChunk.pos.x + " and y = " + play.currentChunk.pos.y);
	}
	
	public void setChunkAround(Entity ent)
	{
		ent.cAround[0] = checkChunk(ent.currentChunk.pos.x-1, ent.currentChunk.pos.y+1);
		ent.cAround[1] = checkChunk(ent.currentChunk.pos.x, ent.currentChunk.pos.y+1);
		ent.cAround[2] = checkChunk(ent.currentChunk.pos.x+1, ent.currentChunk.pos.y+1);
		ent.cAround[3] = checkChunk(ent.currentChunk.pos.x-1, ent.currentChunk.pos.y);
		ent.cAround[4] = ent.currentChunk;
		ent.cAround[5] = checkChunk(ent.currentChunk.pos.x+1, ent.currentChunk.pos.y);
		ent.cAround[6] = checkChunk(ent.currentChunk.pos.x-1, ent.currentChunk.pos.y-1);
		ent.cAround[7] = checkChunk(ent.currentChunk.pos.x, ent.currentChunk.pos.y-1);
		ent.cAround[8] = checkChunk(ent.currentChunk.pos.x+1, ent.currentChunk.pos.y-1);
	}
	
	public void setChunkAround(Chunk center)
	{
		center.chunkBuffer[0] = checkChunk(center.pos.x-1, center.pos.y+1);
		center.chunkBuffer[1] = checkChunk(center.pos.x, center.pos.y+1);
		center.chunkBuffer[2] = checkChunk(center.pos.x+1, center.pos.y+1);
		center.chunkBuffer[3] = checkChunk(center.pos.x-1, center.pos.y);
		center.chunkBuffer[5] = checkChunk(center.pos.x+1, center.pos.y);
		center.chunkBuffer[6] = checkChunk(center.pos.x-1, center.pos.y-1);
		center.chunkBuffer[7] = checkChunk(center.pos.x, center.pos.y-1);
		center.chunkBuffer[8] = checkChunk(center.pos.x+1, center.pos.y-1);
	}
	
	/*private void checkEntities()
	{
		
		Iterator<Entity> it = globalEntityList.iterator();
		while (it.hasNext()) 
		{
			Entity tmp = it.next();
			if(tmp.updateChunk)
			{
				setChunkAround(tmp);
				tmp.updateChunk = false;
			}
			if(tmp.isDestroyed())
			{
				globalEntityList.remove(tmp);
				Const.debug("(Map:checkEntities): entity removed");
			}
		}
	}*/
	
	public Point genSpawnCoords(Chunk c)
	{
		Point p = new Point();
		p.y = -1;
		while(p.y == -1)
		{
			p.x = rand.nextInt(Const.tailleChunkX*Const.tailleCase);
			p.y = c.getFirstAirBlockHeight(p.x);
		}
		return p;
	}
	
	public void spawnMob(Chunk c)
	{
		Point p = genSpawnCoords(c);
		Mob mob1 = new Mob(p.x,p.y,c,play);
		setChunkAround(mob1);
		//globalEntityList.add(mob1);
		c.mobList.add(mob1);
	}
	
	public void spawnPlayer(Chunk spawnPoint)
	{
		Point p = genSpawnCoords(spawnPoint);
		play = new Player(p.x,p.y,spawnPoint);
		setChunkAround(play);
		while(play.currentCaseRight().getBlockSolidity().equals("solid"))
		{
			p.x--;
		}
		play.inv.addItem(new WeaponItem(0,0,10,10,"epee.png"), 1);
		firstScroll(play);
		
		//globalEntityList.add(play);
		//spawnPoint.playerList.add(play);
	}
	
	public void firstScroll(Player play)
	{
		int centerX = Const.tailleFenX/2;
		int centerY = Const.tailleFenY/2;
		Point pos = play.pos;
		glapi.scroll(pos.x-centerX,pos.y-centerY);
	}
	
	public Chunk checkChunk(int posX,int posY)
	{
		return checkChunk(posX,posY,false);
	}
	
	public Chunk checkChunk(int posX,int posY,boolean debug)
	{
		if(debug)
		{
			Const.debug("(Map:checkChunk): pos = " + posX + ", " + posY);
			Const.debug("(Map:checkChunk): " + cl.get(posX,posY));
		}
		Chunk cFound = cl.get(posX,posY);
		if(cFound == null)
		{
			Chunk c;
			if(posY < 0)
			{				
				c = new UndergroundChunk(this,new Point(posX,posY),setLight,rend);
				//System.out.println("new underGroundchunk: posX = " + posX + ", posY " + posY);
				//c.chunkBuffer[1] = checkChunk(posX,posY+1);
			}
			else if(posY > 0)
			{
				c = new SkyChunk(this,new Point(posX,posY),setLight,rend);
				//c.chunkBuffer[7] = checkChunk(posX,posY-1);
			}
			else
			{
				if(posX > 0)
				{
					c = new SurfaceChunk(this,new Point(posX,posY),setLight,rend,true);
					//c.chunkBuffer[3] = checkChunk(posX-1,posY);
				}
				else if(posX < 0)
				{
					c = new SurfaceChunk(this,new Point(posX,posY),setLight,rend,true);
					//c.chunkBuffer[5] = checkChunk(posX+1,posY);
				}
				else
				{
					c = new SurfaceChunk(this,new Point(0,0),setLight,rend,true);
					System.err.println("error while getting chunk,spawn point should exist");
					System.exit(1);
				}
			}
			generate(c);
			return c;
		}
		else
		{
			return cFound;
		}
	}
	
	public void draw()
	{
		int left = -1;
		int right = 1;
		int up = -1;
		int down = 1;
		if(play.pos.x > Const.tailleFenX/2 + Const.tailleCase*Const.maxLight - 1)
		{
			left = 0;
		}
		else if(play.pos.x < Const.tailleFenX/2 - Const.tailleCase*Const.maxLight - 1)
		{
			right = 0;
		}
		if(play.pos.y > Const.tailleFenY/2 + Const.tailleCase*Const.maxLight - 1)
		{
			down = 0;
		}
		else if(play.pos.y < Const.tailleFenY/2 - Const.tailleCase*Const.maxLight - 1)
		{
			up = 0;
		}
		origin.setCoords(play.pos.x-center.x, play.pos.y-center.y);
		end.setCoords(play.pos.x+center.x, play.pos.y+center.y);
		rend.draw(origin, end, "sky.png");
		if(allowedToDraw)
		{
			for(int i = left;i <= right;i++)
			{
				for(int j = up;j <= down;j++)
				{
					rend.setOffset(i*1200,(j*(-608)));
					setChunkAround(play.cAround[(i+1)+(j+1)*3]);
					play.cAround[(i+1)+(j+1)*3].draw();
				}
			}
			rend.setOffset(0, 0);
			play.draw(rend);
		}
		else
		{
			allowedToDraw = true;
		}
	}
	
	public void save()
	{
		SaveHandler save = new SaveHandler(this,"save" + File.separator + gameName);
		save.save(cl);
		/*if(newGame)
		{
			GameFile path = new GameFile(Const.savePath);
			path.addNewLine(gameName);
			newGame = false;
		}*/
	}
	
	public CustomIndex2DList<Chunk> getCustomList()
	{
		return cl;
	}
}
