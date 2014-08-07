package avalone.negend;

import java.util.Random;

import avalone.api.lwjgl.Point;

public class UndergroundChunk extends Chunk
{
	//private Chunk cUp;
	private Random rand;
	
	public UndergroundChunk(Map map,Point pos/*,Chunk cUp*/,boolean setLight,Renderer rend) 
	{
		super(map,pos/*,cUp*/,setLight,rend);
		luckFactor = 200/(1-pos.y);
		if(luckFactor == 0)
		{
			luckFactor = 1;
		}
		//this.cUp = cUp;
		genFlag = false;
		rand = new Random();
	}

	public Block chooseBlock(int posX,int posY)
	{
		surfaceModifier = chunkBuffer[1].surfaceModifRegister[posX]+Const.tailleChunkY;
		surfaceModifRegister[posX] = chunkBuffer[1].surfaceModifRegister[posX]+Const.tailleChunkY;
		int a = rand.nextInt(2);
		if(posY == surfaceModifier || posY == surfaceModifier-a)
		{
			return Block.sand;
		}
		else if(posY > surfaceModifier)
		{
			return Block.water;
		}
		return Block.stone;
	}

	public void generate(Random rand, Ore[] ores) 
	{
		chunkBuffer[1] = map.checkChunk(pos.x,pos.y+1);
		initGen(rand,ores);
	}

	public void extendGen(Random rand) 
	{
		//grottes
	}
}
