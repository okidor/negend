package avalone.negend;

import java.util.Random;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Point;

public class SkyChunk extends Chunk
{
	public SkyChunk(Map map,Point pos,boolean setLight,Renderer rend)
	{
		super(map,pos,setLight,rend);
		luckFactor = 300;
		Const.debug("construction skychunk at pos " + pos.x + ", " + pos.y);
		//new Exception().printStackTrace();
	}
	
	public Block chooseBlock(int posX,int posY)
	{
		surfaceModifier = chunkBuffer[7].surfaceModifRegister[posX]-Const.tailleChunkY;
		surfaceModifRegister[posX] = chunkBuffer[7].surfaceModifRegister[posX]-Const.tailleChunkY;
		if(posY == surfaceModifier)
		{
			return Block.snow;
		}
		if(posY == surfaceModifier-1 || posY == surfaceModifier-2)
		{
			return Block.dirt;
		}
		if(posY <= surfaceModifier-3)
		{
			return Block.stone;
		}
		if(cases[posX][posY].getBlockID() != Block.air.blockID)
		{
			return Block.getBlock(cases[posX][posY].getBlockID());//ca marche mais faut trouver un code moins moche
		}
		return Block.air;
	}

	public void generate(Random rand, Ore[] ores) 
	{
		chunkBuffer[7] = map.checkChunk(pos.x,pos.y-1);
		if(!lock)
		{
			initGen(rand,ores);
			lock = true;
		}
		else
		{
			Const.debug("(SkyChunk:generate): tried to generate twice on chunk: " + pos.x + ", " + pos.y);
		}
	}

	public void extendGen(Random rand) 
	{
		//islands
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			if(chunkBuffer[7].cases[i][0].getBlockID() == Block.wood.blockID)
			{
				
			}
		}
	}
}
