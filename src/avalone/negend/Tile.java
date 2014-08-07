package avalone.negend;

import avalone.api.lwjgl.Point;

public class Tile 
{
	public Point coord;
	private Block[] layer;
	public int taille;
	public int xG,xD,yB,yH;
	public Point num;
	
	public Ore ore;
	public int subID;
	public int light;
	public boolean debug;
	public boolean debug2;
	public boolean debug3;
	private boolean locked;
	
	//public static final Tile undefined_tile = new Tile(-1,-1,Block.undefined);
	
	public Tile(int x,int y)
	{
		this(x,y,Block.air);
	}
	
	public Tile(int x,int y,Block block)
	{
		coord = new Point(x,y);
		taille = Const.tailleCase;
		initBorders();
		num = new Point(coord.x/taille, coord.y/taille);
		layer = new Block[3];
		layer[0] = block;
		layer[1] = Block.undefined;
		layer[2] = Block.undefined;
		ore = new Ore(-1);
		subID = 0;
		initLight();
		debug = false;
		locked = false;
	}
	
	public void initLight()
	{
		if(getBlockSolidity().equals("solid"))
		{
			light = 0;
			return;
		}
		light = Const.maxLight;
	}
	
	public void initBorders()
	{
		if(coord.y == 0)
		{
			yB = Const.tailleCase*Const.tailleChunkY;
			yH = coord.y + taille;
		}
		else if(coord.y == Const.tailleCase * (Const.tailleChunkY - 1))
		{
			yB = coord.y;
			yH = 0;
		}
		else
		{
			yB = coord.y;
			yH = coord.y + taille;
		}
		
		if(coord.x == 0)
		{
			xG = Const.tailleChunkX * Const.tailleCase;
			xD = coord.x + taille;
		}
		else if(coord.x == Const.tailleCase * (Const.tailleChunkX - 1))
		{
			xG = coord.x;
			xD = 0;
		}
		else
		{
			xG = coord.x;
			xD = coord.x + taille;
		}
		   //a revoir pour bords de chunk
	}
	
	public boolean isEmpty()
	{
		return true;
	}
	
	public void lock()
	{
		locked = true;
	}
	
	public String getTexture()
	{
		return layer[0].getTexture(subID);
	}
	
	public int getBlockID()
	{
		return layer[0].blockID;
	}
	
	public String getBlockSolidity()
	{
		return layer[0].solidity;
	}
	
	public void setBlock(Block block)
	{
		if(!locked)
		{
			layer[0] = block;
		}
		else
		{
			Const.debug("(Tile:setBlock): couldn't change block because tile is locked");
		}
	}
	
	public void setBlock(Item item)
	{
		if(!locked)
		{
			if(item.isPlacable())
			{
				layer[0] = Block.getBlock(item.id);
				subID = item.subID;
			}
			else
			{
				Const.debug("(Tile): tried to place non placable item");
			}
		}
		else
		{
			Const.debug("(Tile:setBlock): couldn't change block because tile is locked");
		}
	}
	
	public void destroyBlock()
	{
		layer[0] = Block.air;
		light = Const.maxLight;
	}
}
