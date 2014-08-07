package avalone.negend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Pack;
import avalone.api.lwjgl.Point;

public class SurfaceChunk extends Chunk
{
	public int biome;
	public int sideBiome;
	protected Random rand;
	
	public SurfaceChunk(Map map,Point pos,boolean setLight,Renderer rend,boolean init)
	{
		super(map,pos,setLight,rend);
		rand = new Random();
		luckFactor = 200;
	}
	
	protected void setSurfaceModifier()
	{
		if(pos.x > 0)
		{
			surfaceModifier = chunkBuffer[3].surfaceModifRegister[Const.tailleChunkX-1];
		}
		else if(pos.x < 0)
		{
			Const.debug("(SurfaceChunk:setSurfaceModifier): " + chunkBuffer[5]);
			surfaceModifier = chunkBuffer[5].surfaceModifRegister[0];
		}
	}
	
	public boolean positiveX()
	{
		return pos.x >= 0;
	}
	
	protected int chooseBiome()
	{
		if(sideBiome == Const.plainsBiome)
		{
			int a = rand.nextInt(14);//4
			//if(a == 0)
			if(a > 2)
			{
				return Const.hillsBiome;
			}
			else if(a == 1)
			{
				return Const.beachBiome;
			}
			else
			{
				return sideBiome;
			}
		}
		else if(sideBiome == Const.hillsBiome)
		{
			int a = rand.nextInt(4);
			if(a == 0)
			{
				return Const.plainsBiome;
			}
			else if(a == 1)
			{
				return sideBiome;
			}
			else
			{
				return Const.mountainsBiome;
			}
		}
		else if(sideBiome == Const.beachBiome)
		{
			int a = rand.nextInt(4);
			if(a == 0 || a == 1)
			{
				return sideBiome;
			}
			else if(a == 2)
			{
				return Const.plainsBiome;
			}
			else
			{
				return Const.oceanBiome;
			}
		}
		else if(sideBiome == Const.oceanBiome)
		{
			int a = rand.nextInt(5);
			if(a == 0)
			{
				return sideBiome;
			}
			else if(a == 3 || a == 4)
			{
				return Const.deepOceanBiome;
			}
			else
			{
				return Const.beachBiome;
			}
		}
		else if(sideBiome == Const.mountainsBiome)
		{
			int a = rand.nextInt(5);
			if(a == 0)
			{
				return sideBiome;
			}
			else if(a == 3 || a == 4)
			{
				return Const.skyMountainsBiome;
			}
			else
			{
				return Const.hillsBiome;
			}
		}
		else if(sideBiome == Const.deepOceanBiome)
		{
			int a = rand.nextInt(3);
			if(a == 0)
			{
				return sideBiome;
			}
			else
			{
				return Const.oceanBiome;
			}
		}
		else if(sideBiome == Const.skyMountainsBiome)
		{
			int a = rand.nextInt(3);
			if(a == 0)
			{
				return sideBiome;
			}
			else
			{
				return Const.mountainsBiome;
			}
		}
		else
		{
			Const.debug("(SurfaceChunk:chooseBiome): warning, sideBiome is not valid on chunk: " + pos.x + ", " + pos.y + " (has value:" + sideBiome + ")");
			return -1;
		}
	}
	
	private void surfaceBiome()
	{
		int a;
		if(biome == Const.plainsBiome)
		{
			a = rand.nextInt(3);
			a--;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > 15)
			{
				surfaceModifier--;
			}
			if(surfaceModifier < 3)
			{
				surfaceModifier++;
			}
		}
		else if(biome == Const.hillsBiome)
		{
			a = rand.nextInt(5);
			a = a - 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > 20)
			{
				surfaceModifier--;
			}
			else if(surfaceModifier < 7)
			{
				if(a != 2)
				{
					surfaceModifier = surfaceModifier + 2;
				}
			}
		}
		else if(biome == Const.beachBiome)
		{
			a = rand.nextInt(7);
			a = a - 3; a = a / 3;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > 2)
			{
				surfaceModifier--;
			}
			if(surfaceModifier < 0)
			{
				surfaceModifier = surfaceModifier + 2;
			}
		}
		else if(biome == Const.oceanBiome)
		{
			a = rand.nextInt(4);
			a = a - 2; a = a / 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > -1)
			{
				surfaceModifier = surfaceModifier - 2;
			}
			if(surfaceModifier < -15)
			{
				surfaceModifier++;
			}
		}
		else if(biome == Const.mountainsBiome)
		{
			a = rand.nextInt(3);
			a--;a = a * 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier < 25)
			{
				surfaceModifier = surfaceModifier + 2;
			}
		}
		else if(biome == Const.deepOceanBiome)
		{
			a = rand.nextInt(4);
			a = a - 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > -15)
			{
				surfaceModifier = surfaceModifier - 2;
			}
		}
		else if(biome == Const.skyMountainsBiome)
		{
			a = rand.nextInt(4);
			a--;a = a * 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier < 40)
			{
				surfaceModifier = surfaceModifier + 2;
			}
		}
	}
	
	public void generate(Random rand,Ore[] ores)
	{
		if(pos.x > 0)
		{
			chunkBuffer[3] = map.checkChunk(pos.x-1,pos.y);
			sideBiome = ((SurfaceChunk)chunkBuffer[3]).biome;
		}
		else if(pos.x < 0)
		{
			chunkBuffer[5] = map.checkChunk(pos.x+1,pos.y);
			sideBiome = ((SurfaceChunk)chunkBuffer[5]).biome;
		}
		else
		{
			sideBiome = -1;
		}
		biome = chooseBiome();
		setSurfaceModifier();
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			surfaceBiome();
			if(positiveX())
			{
				surfaceModifRegister[i] = surfaceModifier;
			}
			else
			{
				surfaceModifRegister[Const.tailleChunkX-1 - i] = surfaceModifier;
			}
		}
		initGen(rand,ores);
		Const.debug("(SurfaceChunk:generate): end of function ");
	}
	
	public Block chooseBlock(int posX,int posY)
	{
		if(posY == surfaceModifRegister[posX])
		{
			if(surfaceModifRegister[posX] > 2)
			{
				return Block.grass;
			}
			else
			{
				return Block.sand;
			}
		}
		
		if(posY == surfaceModifRegister[posX]-1 || posY == surfaceModifRegister[posX]-2)
		{
			if(surfaceModifRegister[posX] > 2)
			{
				return Block.dirt;
			}
			else
			{
				if(posY == surfaceModifRegister[posX])
				{
					return Block.stone;
				}
				return Block.sand;
			}
		}
		
		if(posY <= surfaceModifRegister[posX]-3)
		{
			return Block.stone;
		}
		return Block.air;
	}
	
	public void extendGen(Random rand)
	{
		smooth();
		buildTrees(rand);
	}
	
	public void buildTrees(Random rand)
	{
		Const.debug("" + map.getCustomList().get(pos.x,pos.y+1));
		Const.debug("(SurfaceChunk:buildTrees): appel depuis chunk: " + pos.x + ", " + pos.y);
		Chunk cUp = map.checkChunk(pos.x, pos.y + 1,true);
		Const.debug("(SurfaceChunk:buildTrees): chunk reference: " + cUp);
		int taille;
		int nbArbres = rand.nextInt(10);
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i = 0;i < nbArbres;i++)
		{
			taille = rand.nextInt(4);
			taille = taille + 3;
			int place = rand.nextInt(72) + 1;
			for(int k = 0;k < al.size();k++)
			{
				if(al.get(k) == place+1 || al.get(k) == place -1 || al.get(k) == place)
				{
					place = rand.nextInt(72) + 1;
					k = 0;
				}
			}
			al.add(place);
			
			int y = -1;
			for(int j = 0;j < Const.tailleChunkY-1;j++)
			{
				if(cases[place][j].getBlockID() == Block.grass.blockID)
				{
					y = j;
					break;
				}
			}
			if(y != -1)
			{
				for(int j = 1;j <= taille;j++)
				{
					if(y + j < Const.tailleChunkY)
					{
						cases[place][y+j].setBlock(Block.wood);
						cases[place][y+j].light = Const.maxLight;
						cases[place][y+j].subID = 0;
						//cases[place][y+j].block.layer = 1;
						if(j > 2)
						{
							if(cases[place+1][y+j].getBlockID() == 0)
							{
								cases[place+1][y+j].setBlock(Block.leaves);
								cases[place+1][y+j].subID = j%2;
								//cases[place+1][y+j].block.layer = 1;
							}
							if(cases[place-1][y+j].getBlockID() == 0)
							{
								cases[place-1][y+j].setBlock(Block.leaves);
								cases[place-1][y+j].subID = 2 + j%2;
								//cases[place-1][y+j].block.layer = 1;
							}
						}
					}
					else
					{
						int debu = y+j;
						int debug = y+j-Const.tailleChunkY;
						Const.debug("(SurfaceChunk:buildTrees): height: " + debu + "replaced by " + debug);
						Const.debug("(SurfaceChunk:buildTrees): chunk pos: " + pos.x + ", " + pos.y + "replaced by " + cUp.pos.x + ", " + cUp.pos.y);
						cUp.cases[place][y+j-Const.tailleChunkY].setBlock(Block.wood);
						cUp.cases[place][y+j-Const.tailleChunkY].light = Const.maxLight;
						cUp.cases[place][y+j-Const.tailleChunkY].subID = 0;
						cUp.cases[place][y+j-Const.tailleChunkY].lock();
						if(j > 2)
						{
							if(cUp.cases[place+1][y+j-Const.tailleChunkY].getBlockID() == 0)
							{
								cUp.cases[place+1][y+j-Const.tailleChunkY].setBlock(Block.leaves);
								cUp.cases[place+1][y+j-Const.tailleChunkY].subID = j%2;
								cUp.cases[place+1][y+j-Const.tailleChunkY].lock();
							}
							if(cUp.cases[place-1][y+j-Const.tailleChunkY].getBlockID() == 0)
							{
								cUp.cases[place-1][y+j-Const.tailleChunkY].setBlock(Block.leaves);
								cUp.cases[place-1][y+j-Const.tailleChunkY].subID = 2 + j%2;
								cUp.cases[place-1][y+j-Const.tailleChunkY].lock();
							}
						}
					}
				}
				if(y+taille+1 < Const.tailleChunkY)
				{
					cases[place][y+taille+1].setBlock(Block.leaves);
					cases[place][y+taille+1].subID = 4;
					//cases[place][y+taille+1].block.layer = 1;
				}
				else
				{
					cUp.cases[place][y+taille+1-Const.tailleChunkY].setBlock(Block.leaves);
					cUp.cases[place][y+taille+1-Const.tailleChunkY].subID = 4;
					cUp.cases[place][y+taille+1-Const.tailleChunkY].lock();
				}
				if(y+taille < Const.tailleChunkY)
				{
					if(taille%2 == 1)
					{
						cases[place+1][y+taille].subID = 5;
						cases[place-1][y+taille].subID = 6;
					}
				}
				else
				{
					if(taille%2 == 1)
					{
						cUp.cases[place+1][y+taille-Const.tailleChunkY].subID = 5;
						cUp.cases[place+1][y+taille-Const.tailleChunkY].lock();
						cUp.cases[place-1][y+taille-Const.tailleChunkY].subID = 6;
						cUp.cases[place+1][y+taille-Const.tailleChunkY].lock();
					}
				}
			}
		}
		Const.debug("(SurfaceChunk:buildTrees): end of function ");
	}
}
