package avalone.negend;

import avalone.api.lwjgl.Point;

public class Block 
{
	public int blockID;
	public int layer;
	private String[] textures;
	public String solidity;
	
	public static final Block undefined = new Block(-1,new String[]{"default"},"nonsolid");
	public static final Block air = new Block(0,new String[]{"air"},"nonsolid");
	public static final Block grass = new Block(1,new String[]{"grass", "grass_up", "grass_left", "grass_right"},"solid");
	public static final Block dirt = new Block(2,new String[]{"dirt"},"solid");
	public static final Block stone = new Block(3,new String[]{"stone"},"solid");
	public static final Block wood = new Block(4,new String[]{"wood"},"nonsolid");
	public static final Block leaves = new Block(5,new String[]{"leaves0","leaves1","leaves2","leaves3","leaves4","leaves5","leaves6"},"nonsolid");
	public static final Block water = new Block(6,new String[]{"water0"},"liquid");
	public static final Block redstone = new Block(7,new String[]{"redstone"},"solid");
	public static final Block sand = new Block(8,new String[]{"sand"},"solid");
	public static final Block plank = new Block(9,new String[]{"plank"},"solid");
	public static final Block snow = new Block(10,new String[]{"snow", "snow_up", "snow_left", "snow_right"},"solid");
			
	private Block(int id,String[] textures,String solidity)
	{
		blockID = id;
		this.textures = textures;
		this.solidity = solidity;
		layer = 0;
	}
	
	public static Block getBlock(int id)
	{
		switch(id)
		{
			case 0 : return air;
			case 1 : return grass;
			case 2 : return dirt;
			case 3 : return stone;
			case 4 : return wood;
			case 5 : return leaves;
			case 6 : return water;
			case 7 : return redstone;
			case 8 : return sand;
			case 9 : return plank;
			case 10 : return snow;
			default : return undefined;
		}
	}
	
	public String getTexture(int subID)
	{
		if(subID < 0 || subID >= textures.length)
		{
			System.out.println("warning, subID out of bounds (" + subID + ") for block " + textures[0]);
			return "default.png";
		}
		return textures[subID]  + ".png";
	}
	
	/*public void debugShadow(Renderer rend,int offsetX,int offsetY)
	{
		Point p = coord.clone(offsetX,offsetY);
		rend.drawText(p, p.clone(taille/2), light, "red");
	}*/
}
