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
	
	public static final Tile undefined_tile = new Tile(-1,-1,Block.undefined);
	
	public Tile(int x,int y,Block block)
	{
		this.coord = new Point(x,y);
		coord = new Point(x,y);
		taille = Const.tailleCase;
		xG = x; xD = x + taille; yB = y; yH = y + taille;
		num = new Point(coord.x/taille, coord.y/taille);
		layer = new Block[3];
		layer[0] = block;
		layer[1] = Block.undefined;
		layer[2] = Block.undefined;
		ore = new Ore(-1);
		subID = 0;
		initLight();
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
	
	public boolean isEmpty()
	{
		return true;
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
		layer[0] = block;
	}
	
	public void setBlock(Item item)
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
	
	public void destroyBlock()
	{
		layer[0] = Block.air;
		light = Const.maxLight;
	}
}
