package avalone.negend;

import java.util.Random;

import avalone.api.lwjgl.Point;

public class UndergroundChunk extends Chunk
{
	private Chunk cUp;
	private Random rand;
	
	public UndergroundChunk(Point pos,Chunk cUp,boolean setLight,Renderer rend) 
	{
		super(pos,cUp,setLight,rend);
		luckFactor = 200/(1-pos.y);
		if(luckFactor == 0)
		{
			luckFactor = 1;
		}
		this.cUp = cUp;
		genFlag = false;
		rand = new Random();
		setSurfaceModifier();
	}
	
	public void setSurfaceModifier()
	{
		
	}

	public Block chooseBlock(int posX,int posY)
	{
		surfaceModifier = cUp.surfaceModifRegister[posX]+Const.tailleChunkY;
		int a = rand.nextInt(2);
		if(posY == 7+surfaceModifier || posY == 7+surfaceModifier-a)
		{
			return Block.sand;
		}
		else if(posY > 7+surfaceModifier)
		{
			return Block.water;
		}
		return Block.stone;
	}
	
	/*public int sunLight(int x,int y)
	{
		int light = super.sunLight(x,y);
		if(y == Const.tailleChunkY-1)
		{
			if(cUp.cases[x][0].getSolidity().equals("nonsolid"))
			{
				light = Const.max(cUp.cases[x][0].light - 1,0);
			}
			else
			{
				light = Const.max(cUp.sunLight(x,0) - 1,0);
			}
		}
		if(light != 0)
		{
			System.out.println("underlight pos " + x + " and " + y + " ! value = " + light);
		}
		return light;
	}*/

	public void generate(Random rand, Ore[] ores) 
	{
		initCases(rand,ores);
	}
	
	/*public int undergroundLight(int x)
	{
		return cUp.cases[x][0].light;
	}*/
}
