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
	
	public SurfaceChunk(Point pos,Chunk cSide,boolean setLight,Renderer rend)
	{
		super(pos,cSide,setLight,rend);
		luckFactor = 200;
		setSurfaceModifier();
	}
	
	public void setSurfaceModifier()
	{
		if(pos.x > 0)
		{
			surfaceModifier = cSide.rightModifier;
		}
		else if(pos.x < 0)
		{
			surfaceModifier = cSide.leftModifier;
		}
	}
	
	public boolean positiveX()
	{
		return pos.x >= 0;
	}
	
	/*public int undergroundLight(int x)
	{ 
		return 0;
	}*/
	
	public void generate(Random rand,Ore[] ores)
	{
		int a,b,c;
		if(positiveX())
		{
			for(int i = 0;i < Const.tailleChunkX;i++)
			{
				a = rand.nextInt(3);
				a--;
				surfaceModifier = surfaceModifier + a;
				surfaceModifRegister[i] = surfaceModifier;
				if(surfaceModifier > 30)
				{
					surfaceModifier--;
				}
				if(i == 0)
				{
					leftModifier = surfaceModifier;
				}
				genColumn(rand,ores,i);
			}	
			rightModifier = surfaceModifier;
		}
		else
		{
			for(int i = Const.tailleChunkX-1;i >= 0;i--)
			{
				a = rand.nextInt(3);
				a--;
				surfaceModifier = surfaceModifier + a;
				surfaceModifRegister[i] = surfaceModifier;
				if(surfaceModifier > 30)
				{
					surfaceModifier--;
				}
				if(i == cases.length-1)
				{
					rightModifier = surfaceModifier;
				}
				genColumn(rand,ores,i);
			}
			leftModifier = surfaceModifier;
		}
		/*if(pos.y == 0)
		{
			System.out.println("posX = " + pos.x + ", posY = " + pos.y + ", left: " + leftModifier + ", right: " + rightModifier);
			System.out.println("________________________________________________");
		}*/
		extendGen(rand);
		if(setLight)
		{
			light();
		}
	}
	
	private void genColumn(Random rand,Ore[] ores,int i)
	{
		for(int j = 0;j < cases[0].length;j++)
		{
			cases[i][j] = new Tile(i*Const.tailleCase,j*Const.tailleCase,chooseBlock(i,j));
			//cases[i][j].block.setLayer(rend);
			int b = rand.nextInt(ores.length);
			int c = rand.nextInt(luckFactor);
			if(c == 0 && cases[i][j].getBlockSolidity().equals("solid"))
			{
				cases[i][j].ore = ores[b];
				cases[i][j].subID = (cases[i][j].subID + 1 + b) * (-1);
			}
		}
	}
	
	public void extendGen(Random rand)
	{
		smooth();
		buildTrees(rand);
	}
	
	public void buildTrees(Random rand)
	{
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
				}
				if(y+taille+1 < Const.tailleChunkY)
				{
					cases[place][y+taille+1].setBlock(Block.leaves);
					cases[place][y+taille+1].subID = 4;
					//cases[place][y+taille+1].block.layer = 1;
				}
				if(y+taille < Const.tailleChunkY)
				{
					if(taille%2 == 1)
					{
						cases[place+1][y+taille].subID = 5;
						cases[place-1][y+taille].subID = 6;
					}
				}
			}
		}
	}
	
	public Block chooseBlock(int posX,int posY)
	{
		/*if(posY == 17 || posY == 18)
		{
			return 9;
		}*/
		if(posY == 7+surfaceModifier)
		{
			if(surfaceModifier > -5)
			{
				return Block.grass;
			}
			else
			{
				return Block.sand;
			}
		}
		
		if(posY == 6+surfaceModifier || posY == 5+surfaceModifier)
		{
			if(surfaceModifier > -5)
			{
				return Block.dirt;
			}
			else
			{
				if(posY == 5+surfaceModifier)
				{
					return Block.stone;
				}
				return Block.sand;
			}
		}
		
		if(posY <= 4+surfaceModifier)
		{
			return Block.stone;
		}
		return Block.air;
	}
	
	/*public void blockFlow(SurfaceChunk cDown,SurfaceChunk cUp,SurfaceChunk cLeft,SurfaceChunk cRight)
	{
		for(int i = 0;i < cases.length;i++)
		{
			for(int j = 0;j < cases[0].length;j++)
			{
				if(cases[i][j].block.solidity.equals("liquid"))
				{
					Block chosenDown = Block.undefined;
					Block chosenUp = Block.undefined;
					Block chosenLeft = Block.undefined;
					Block chosenRight = Block.undefined;
					Block chosenLeftLeft = Block.undefined;
					Block chosenRightRight = Block.undefined;
					if(j == 0)
					{
						chosenDown = cDown.cases[i][Const.tailleChunkY-1].block;
					}
					else if(j == Const.tailleChunkY-1)
					{
						chosenUp = cUp.cases[i][0].block;
					}
					else
					{
						chosenDown = cases[i][j-1].block;
						chosenUp = cases[i][j+1].block;
					}
					if(i == 0)
					{
						chosenLeft = cLeft.cases[Const.tailleChunkX-1][j].block;
						chosenLeftLeft = cLeft.cases[Const.tailleChunkX-2][j].block;
						chosenRight = cases[i+1][j].block;
						chosenRightRight = cases[i+2][j].block;
					}
					else if(i == 1)
					{
						chosenLeft = cases[i-1][j].block;
						chosenLeftLeft = cLeft.cases[Const.tailleChunkX-1][j].block;
						chosenRight = cases[i+1][j].block;
						chosenRightRight = cases[i+2][j].block;
					}
					else if(i == Const.tailleChunkX - 1)
					{
						chosenLeft = cases[i-1][j].block;
						chosenLeftLeft = cases[i-2][j].block;
						chosenRight = cRight.cases[0][j].block;
						chosenRightRight = cRight.cases[1][j].block;
					}
					else if(i == Const.tailleChunkX - 2)
					{
						chosenLeft = cases[i-1][j].block;
						chosenLeftLeft = cases[i-2][j].block;
						chosenRight = cases[i+1][j].block;
						chosenRightRight = cRight.cases[0][j].block;
					}
					else
					{
						chosenLeft = cases[i-1][j].block;
						chosenLeftLeft = cases[i-2][j].block;
						chosenRight = cases[i+1][j].block;
						chosenRightRight = cases[i+2][j].block;
					}
					cases[i][j].block.flow(rend,chosenDown,chosenLeft,chosenRight,chosenLeftLeft,chosenRightRight);
				}
			}
		}
	}*/
}
