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
	
	public SaveHandler(String gameName)
	{
		this.gameName = gameName;
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
			out.write("\n");
			saveUpLeft();
			saveUpRight();
			saveDownLeft();
			saveDownRight();
			out.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public CustomIndex2DList<Chunk> load(Renderer rend)
	{
		cl = new CustomIndex2DList<Chunk>();
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
	
	private void loadUpLeft(Renderer rend)
	{
		
	}
	
	private void loadUpRight(Renderer rend)
	{
		
	}
	
	private void loadDownLeft(Renderer rend)
	{
		
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
					if(i == 1)
					{
						Chunk c = null;
						if(s[0].length == 2)
						{
							Point p = new Point(Integer.decode(s[0][0]),Integer.decode(s[0][1]));
							c = new SpawnChunk(p,true,rend);
							cl.add(c.pos.x, c.pos.y, c);
							loaded.add(p);
							System.out.println("spawnpoint loaded : " + c.pos.x + " " + c.pos.y);
						}
						else if(s[0].length == 4)
						{
							Point p1 = new Point(Integer.decode(s[0][0]),Integer.decode(s[0][1]));
							Point p2 = new Point(Integer.decode(s[0][2]),Integer.decode(s[0][3]));
							c = loadChunk(p1,p2,rend);
							System.out.println("chunk loaded : " + c.pos.x + " " + c.pos.y);
						}
						else
						{
							System.out.println("storage fail");
						}
						loadChunkBlocks(c,s[1],rend);
					}
					i = (i + 1) % 2;
				}
			}
		}
	}
	
	private Chunk loadChunk(Point p1,Point p2,Renderer rend)
	{
		loaded.add(p1);
		Chunk c;
		if(p1.y < 0)
		{				
			c = new UndergroundChunk(p1,cl.get(p2.x,p2.y),true,rend);
		}
		else if(p1.y > 0)
		{
			c = new SkyChunk(p1,cl.get(p2.x,p2.y),true,rend);
		}
		else
		{
			if(p1.x > 0)
			{
				c = new SurfaceChunk(p1,cl.get(p2.x,p2.y),true,rend);
			}
			else if(p1.x < 0)
			{
				c = new SurfaceChunk(p1,cl.get(p2.x,p2.y),true,rend);
			}
			else
			{
				c = new SurfaceChunk(new Point(0,0),null,true,rend);
				System.err.println("erreur de selection de chunk");
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
				int id = Integer.decode(parameters[i*Const.tailleChunkY + j*3]);
				c.cases[i][j] = new Tile(i*Const.tailleCase,j*Const.tailleCase,Block.getBlock(id));
				c.cases[i][j].subID = Integer.decode(parameters[i*Const.tailleChunkY + j*3+1]);
				c.cases[i][j].ore = new Ore(Integer.decode(parameters[i*Const.tailleChunkY + j*3+2]),rend.getAPI());
				//s = s + c.cases[i][j].blockID + " " + c.cases[i][j].subID + " " + c.cases[i][j].ore.id + " ";
			}
		}
		System.out.println(c.cases[0][0].getBlockID());
		System.out.println(c.cases[0][0].subID);
		System.out.println(c.cases[0][0].ore.id);
		
		System.out.println(c.cases[0][1].getBlockID());
		System.out.println(c.cases[0][1].subID);
		System.out.println(c.cases[0][1].ore.id);
	}
	
	private void saveUpLeft() throws IOException
	{
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
		if(c.pos.x == 0 && c.pos.y == 0)
		{
			out.write(c.pos.x + " " + c.pos.y + "\n");
		}
		else
		{
			out.write(c.pos.x + " " + c.pos.y + " " + c.cSide.pos.x + " " + c.cSide.pos.y + "\n");
		}
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
