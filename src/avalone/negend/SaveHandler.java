package avalone.negend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import avalone.api.lwjgl.CustomIndex2DList;
import avalone.api.lwjgl.Point;

public class SaveHandler 
{
	private CustomIndex2DList<Chunk> cl;
	private ArrayList<Point> loaded;
	private String gameName;
	private int[] sizes;
	private BufferedWriter out;
	private BufferedReader in;
	private Map map;
	private int mode;
	private Player play;
	
	public SaveHandler(Map map,String gameName)
	{
		this.map = map;
		this.gameName = gameName;
		mode = 0;
		loaded = new ArrayList<Point>();
	}
	
	public void save(CustomIndex2DList<Chunk> cl)
	{
		this.cl = cl;
		sizes = cl.size();
		File f = createFile(gameName);
		System.out.println("created file : " + f.getAbsolutePath());
		try 
		{
			out = new BufferedWriter(new FileWriter(f));
			out.write("//ores saving will go here\n");	
			saveUpLeft();
			saveUpRight();
			saveDownLeft();
			saveDownRight();
			out.write("#player:\n");
			savePlayer();
			out.write("\n");
			out.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public CustomIndex2DList<Chunk> load(Renderer rend)
	{
		cl = map.getCustomList();
		try 
		{
			File f = new File(gameName);
			System.out.println("reading file : " + f.getAbsolutePath());
			in = new BufferedReader(new FileReader(gameName));
			loadParts(rend);
			/*loadUpLeft(rend);
			loadUpRight(rend);
			loadDownLeft(rend);
			loadDownRight(rend);*/
			//Chunk c = loadChunk();
			//cl.add(c.pos.x, c.pos.y, c);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return cl;
	}
	
	public String check(String readLine)
	{
		if(readLine.startsWith(" "))
		{
			return check(readLine.substring(1));
		}
		return readLine;
	}
	
	public void savePlayer() throws IOException
	{
		Player play = map.play;
		out.write(play.pos.x + " " + play.pos.y + " " + play.currentChunk.pos.x + " " + play.currentChunk.pos.y + "\n");
		Inventory inv = play.inv;
		out.write("#items:\n");
		for(int i = 0;i < inv.getSize();i++)
		{
			out.write(inv.itemForSave(i).id + " " + inv.itemForSave(i).subID + " " + inv.itemForSave(i).texture + " " + inv.itemForSave(i).level + " " + inv.itemForSave(i).tier + " " + inv.itemForSave(i).type + " ");
			out.write(inv.numberForSave(i) + "\n");
		}
	}
	
	public Player getLoadedPlayer()
	{
		return play;
	}
	
	private void loadParts(Renderer rend) throws IOException
	{
		String[] readLine = new String[2];int i = 0;String[][] s = new String[2][0]; 
		while((readLine[i] = in.readLine()) != null)
		{
			readLine[i] = check(readLine[i]);
			if(readLine[i].length() > 0)
			{
				if(!readLine[i].startsWith("//") && !readLine[i].startsWith("#"))
				{
					s[i] = readLine[i].split("\\s+");
					if(mode == 1)
					{
						loadPlayerItem(s[i],i);
					}
					else if(mode == 2)
					{
						if(i == 1)
						{
							Chunk c = loadChunk(s,rend);
							loadChunkBlocks(c,s[1],rend);
						}
						i = 1 - i;
					}
					else if(mode == 3)
					{
						Const.debug("(SaveHandler:loadParts): " + cl.get(Integer.decode(s[i][2]),Integer.decode(s[i][3])));
						play = new Player(Integer.decode(s[i][0]),Integer.decode(s[i][1]),cl.get(Integer.decode(s[i][2]),Integer.decode(s[i][3])));
					}
				}
				else if(readLine[i].startsWith("#"))
				{
					s[i] = readLine[i].split("\\s+");
					if(s[i][0].equals("#items:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading items");
						i = 0;
						mode = 1;
					}
					else if(s[i][0].equals("#chunks:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading chunks");
						i = 0;
						mode = 2;
					}
					else if(s[i][0].equals("#player:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading player");
						i = 0;
						mode = 3;
					}
				}
			}
		}
	}
	
	private void loadPlayerItem(String[] s,int i)
	{
		Inventory inv = play.inv;
		Item item;
		if(Integer.decode(s[5]) == 1)
		{
			item = new ItemBlock(Integer.decode(s[0]),Integer.decode(s[1]));
		}
		else if(Integer.decode(s[5]) == 2)
		{
			float[] f = new float[3];
			f[0] = 0;
			f[1] = 0;
			f[2] = 0;
			item = new MineralItem(Integer.decode(s[1]),f);
		}
		else if(Integer.decode(s[5]) == 3)
		{
			item = new WeaponItem(Integer.decode(s[0]),Integer.decode(s[1]),Integer.decode(s[4]),Integer.decode(s[3]),s[2]);
		}
		else
		{
			item = new Item(Integer.decode(s[0]),Integer.decode(s[1]),EnumItem.general,Integer.decode(s[4]),Integer.decode(s[3]),s[2]);
		}
		inv.addItem(item,Integer.decode(s[6]));
	}
	
	private Chunk loadChunk(String[][] s,Renderer rend)
	{
		Point p1 = new Point(Integer.decode(s[0][0]),Integer.decode(s[0][1]));
		loaded.add(p1);
		Chunk c;
		Const.debug("(SaveHandler:loadChunk): p1:" + p1.x + ", " + p1.y);
		if(p1.x == 0 && p1.y == 0)
		{
			Const.debug("(SaveHandler:loadChunk): spawn will be loaded");
			c = new SpawnChunk(map,p1,true,rend,false);
		}
		else if(p1.y < 0)
		{				
			Const.debug("(SaveHandler:loadChunk): underground will be loaded");
			c = new UndergroundChunk(map,p1,true,rend);
		}
		else if(p1.y > 0)
		{
			Const.debug("(SaveHandler:loadChunk): sky will be loaded");
			c = new SkyChunk(map,p1,true,rend);
		}
		else
		{
			Const.debug("(SaveHandler:loadChunk): surface will be loaded");
			c = new SurfaceChunk(map,p1,true,rend,false);
			((SurfaceChunk) c).biome = Integer.decode(s[0][2]);
			//((SurfaceChunk) c).sideBiome = Integer.decode(s[0][3]);
			if(p1.x == 0)
			{
				System.err.println("error while loading chunk, wrong selection");
				System.exit(1);
			}
		}
		cl.add(c.pos.x, c.pos.y, c);
		return c;
	}
	
	private void loadChunkBlocks(Chunk c,String[] parameters,Renderer rend)
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				int id = Integer.decode(parameters[i*Const.tailleChunkY*3 + j*3]);
				c.cases[i][j] = new Tile(i*Const.tailleCase,j*Const.tailleCase,Block.getBlock(id));
				c.cases[i][j].subID = Integer.decode(parameters[i*Const.tailleChunkY*3 + j*3+1]);
				c.cases[i][j].ore = new Ore(Integer.decode(parameters[i*Const.tailleChunkY*3 + j*3+2]),rend.getAPI());
				//s = s + c.cases[i][j].blockID + " " + c.cases[i][j].subID + " " + c.cases[i][j].ore.id + " ";
			}
		}
		/*System.out.println(c.cases[0][0].getBlockID());
		System.out.println(c.cases[0][0].subID);
		System.out.println(c.cases[0][0].ore.id);
		
		System.out.println(c.cases[0][1].getBlockID());
		System.out.println(c.cases[0][1].subID);
		System.out.println(c.cases[0][1].ore.id);*/
	}
	
	private void saveUpLeft() throws IOException
	{
		out.write("#chunks:\n");
		out.write("#up left\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(-i,j);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveUpRight() throws IOException
	{
		out.write("#up right\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(i,j);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveDownLeft() throws IOException
	{
		out.write("#down left\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(-i,-j);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveDownRight() throws IOException
	{
		out.write("#down right\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(i,-j);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveChunk(Chunk c) throws IOException
	{
		out.write(c.pos.x + " " + c.pos.y + " ");
		if(c instanceof SurfaceChunk && !(c instanceof SpawnChunk))
		{
			out.write(((SurfaceChunk) c).biome + " " + ((SurfaceChunk) c).sideBiome);
		}
		out.write("\n");
		String s = "";
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				s = s + c.cases[i][j].getBlockID() + " " + c.cases[i][j].subID + " " + c.cases[i][j].ore.id + " ";
			}
		}
		out.write(s + "\n");
	}
	
	public static File createFile(String path)
    {
    	File f = new File(path);
        boolean exists = f.exists();
        if(exists)
        {
            f.delete();
        }
        try
        {
        	f.createNewFile();
        }
        catch(IOException ie)
        {
        	System.out.println("couldn't create file");
        }
        return f;
    }
	
	public File checkExists(String path,String filetype)
    {
    	File f = new File(path);
    	if(!f.exists())
    	{
    		System.out.println(filetype + " " + path + " does not exist.");
    		return new File("error");
    	}
    	return f;
    }
}
