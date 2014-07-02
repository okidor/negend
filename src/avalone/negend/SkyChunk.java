package avalone.negend;

import java.util.Random;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Point;

public class SkyChunk extends Chunk
{
	public SkyChunk(Point pos,Chunk cDown,boolean setLight,Renderer rend)
	{
		super(pos,cDown,setLight,rend);
		luckFactor = 1;
		setSurfaceModifier();
	}
	
	public Block chooseBlock(int posX,int posY)
	{
		return Block.air;
	}
	
	public void generateIsland()
	{
		
	}

	public void setSurfaceModifier() 
	{
		
	}

	public void generate(Random rand, Ore[] ores) 
	{
		initCases(rand,ores);
		generateIsland();
	}
}
