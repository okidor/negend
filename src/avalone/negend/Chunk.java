package avalone.negend;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl.Point;

public abstract class Chunk 
{
	public Tile[][] cases;
	protected Renderer rend;
	public Point pos;
	protected int surfaceModifier;
	protected int[] surfaceModifRegister;
	protected boolean setLight;
	public ArrayList<Mob> mobList;
	public ArrayList<Player> playerList;
	protected ArrayList<WorldItem> itemList;
	protected Chunk cHorizontal;
	protected Chunk cVertical;
	protected int luckFactor;
	protected boolean genFlag;
	public Chunk[] chunkBuffer;
	public Map map;
	protected boolean lock;
	
	protected Chunk(Map map,Point pos,boolean setLight,Renderer rend)
	{
		this.pos = pos;
		cases = new Tile[Const.tailleChunkX][Const.tailleChunkY];
		initCases();
		this.rend = rend;
		this.setLight = setLight;
		surfaceModifRegister = new int[Const.tailleChunkX];
		mobList = new ArrayList<Mob>();
		playerList = new ArrayList<Player>();
		itemList = new ArrayList<WorldItem>();
		chunkBuffer = new Chunk[9];
		this.map = map;
		chunkBuffer[4] = this;
	}
	
	public abstract void generate(Random rand,Ore[] ores);
	
	public abstract Block chooseBlock(int posX,int posY);
	
	public abstract void extendGen(Random rand);
	
	public void light()
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(cases[i][j].getBlockSolidity().equals("solid"))
				{
					cases[i][j].light = sunLight(i,j);
				}
			}
		}
	}
	
	public int sunLight(int x,int y)
	{
		if(y < Const.tailleChunkY-1)
		{
			if(cases[x][y+1].getBlockSolidity().equals("nonsolid"))
			{
				return Const.max(cases[x][y+1].light - 1,0);
			}
			else
			{
				return Const.max(sunLight(x,y+1) - 1,0);
			}
		}
		return cases[x][y].light;
	}
	
	public void updateLight()
	{
		//System.out.println("cAround[4] = " + cAround[4].pos.x + " " + cAround[4].pos.y);
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(cases[i][j].getBlockSolidity().equals("solid"))
				{
					sideLight(i,j);
				}
			}
		}
	}
	
	public void sideLight(int x,int y)
	{
		int leftLight = getLeftLight(x,y,chunkBuffer[3]);
		int rightLight = getRightLight(x,y,chunkBuffer[5]);
		int downLight = getDownLight(x,y,chunkBuffer[7]);
		int upLight = getUpLight(x,y,chunkBuffer[1]);
		cases[x][y].light = Const.max(leftLight,rightLight,downLight,upLight) - 1;
		cases[x][y].light = Const.max(cases[x][y].light,0);
	}
	
	private int getLeftLight(int x,int y,Chunk cLeft)
	{
		if(x > 0)
		{
			return cases[x-1][y].light;
		}
		else
		{
			return cLeft.cases[Const.tailleChunkX-1][y].light;
		}
	}
	
	private int getRightLight(int x,int y,Chunk cRight)
	{
		if(x < Const.tailleChunkX-1)
		{
			return cases[x+1][y].light;
		}
		else
		{
			return cRight.cases[0][y].light;
		}
	}
	
	private int getDownLight(int x,int y,Chunk cDown)
	{
		if(y > 0)
		{
			return cases[x][y-1].light;
		}
		else
		{
			return cDown.cases[x][Const.tailleChunkY-1].light;
		}
	}
	
	private int getUpLight(int x,int y,Chunk cUp)
	{
		if(y < Const.tailleChunkY-1)
		{
			return cases[x][y+1].light;
		}
		else
		{
			return cUp.cases[x][0].light;
		}
	}
	
	public void smooth()
	{
		Const.debug("(Chunk:smooth): placer cette fonction dans surfaceChunk");
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(i > 0 && i < Const.tailleChunkX - 1)
				{
					if(j > 0 && j < Const.tailleChunkY - 1)
					{
						if(cases[i][j].getBlockID() == Block.grass.blockID)
						{
							if(cases[i-1][j].getBlockID() == Block.air.blockID && cases[i+1][j].getBlockID() == Block.air.blockID) 
							{
								cases[i][j].destroyBlock();
								cases[i][j].subID = 0;
								cases[i][j-1].setBlock(Block.grass);
								cases[i][j-1].subID = 0;
							}
						}
						else if(cases[i][j].getBlockID() == Block.air.blockID)
						{
							if(cases[i-1][j].getBlockID() == Block.grass.blockID && cases[i+1][j].getBlockID() == Block.grass.blockID) 
							{
								if(cases[i][j-1].getBlockID() == Block.grass.blockID)
								{
									cases[i][j].setBlock(Block.grass);
									cases[i][j-1].setBlock(Block.dirt);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void grassGrow()
	{
		for(int i = 0;i < cases.length;i++)
		{
			for(int j = 0;j < cases[0].length;j++)
			{
				if(j+1 < Const.tailleChunkY)
				{
					if(cases[i][j].getBlockID() == Block.dirt.blockID && cases[i][j+1].getBlockID() == Block.air.blockID)
					{
						if(cases[i][j].light > 7)
						{
							cases[i][j].setBlock(Block.grass);
						}
					}
				}
				if(cases[i][j].getBlockID() == Block.grass.blockID)
				{
					if(j < Const.tailleChunkY-1 && cases[i][j+1].getBlockID() != Block.air.blockID)
					{
						cases[i][j].setBlock(Block.dirt);
						cases[i][j].subID = Const.noMetaData;
					}
					else if(i > 0 && cases[i-1][j].getBlockID() == Block.air.blockID)
					{
						if(i < Const.tailleChunkX-1 && cases[i+1][j].getBlockID() == Block.air.blockID)
						{
							cases[i][j].subID = Const.upSubID;
						}
						else
						{
							cases[i][j].subID = Const.leftSubID;
						}
					}
					else if(i < Const.tailleChunkX-1 && cases[i+1][j].getBlockID() == Block.air.blockID)
					{
						cases[i][j].subID = Const.rightSubID;
					}
					else
					{
						cases[i][j].subID = Const.defaultSubID;
					}
				}
			}
		}
	}
	
	public void draw()
	{
		if(setLight)
		{
			updateLight();
		}
		grassGrow();
		// blockFlow(cAround[7],cAround[1],cAro							//cases[i][j].debugShadow(rend, offsetX, offsetY);und[3],cAround[5]);
		for(int i = 0;i < cases.length;i++)
		{
			for(int j = 0;j < cases[0].length;j++)
			{
				if(cases[i][j].getBlockID() != 0)
				{
					Point p1 = cases[i][j].coord.clone(0);
					Point p2 = p1.clone(Const.tailleCase);
				
					rend.draw(p1,p2,cases[i][j]);
					if(cases[i][j].light > 0 && cases[i][j].subID < 0)
					{
						rend.addMineral(cases[i][j]);
					}
				}
				else
				{
					if(cases[i][j].debug)
					{
						rend.getAPI().unbindTexture();
						rend.getAPI().drawAlphaRect(cases[i][j].coord, cases[i][j].coord.clone(Const.tailleCase),"RED",0.5f);
						rend.getAPI().clearFilter();
						cases[i][j].debug = false;
					}
					if(cases[i][j].debug2)
					{
						rend.getAPI().unbindTexture();
						rend.getAPI().drawAlphaRect(cases[i][j].coord, cases[i][j].coord.clone(Const.tailleCase),"GREEN",0.5f);
						rend.getAPI().clearFilter();
						cases[i][j].debug2 = false;
					}
					if(cases[i][j].debug3)
					{
						rend.getAPI().unbindTexture();
						rend.getAPI().drawAlphaRect(cases[i][j].coord, cases[i][j].coord.clone(Const.tailleCase),"BLUE",0.5f);
						rend.getAPI().clearFilter();
						cases[i][j].debug3 = false;
					}
				}
			}
		}
		drawShadows();
		AI();
		items();
		drawLimit();
	}
	
	protected void items()
	{
		for(int i = 0;i < itemList.size();i++)
		{
			WorldItem item = itemList.get(i);
			ArrayList<AliveEntity> tmp = new ArrayList<AliveEntity>();
			for(int j = 0;j < mobList.size();j++)
			{
				if(mobList.get(j).health > 0)
				{
					tmp.add(mobList.get(j));
				}
			}
			for(int j = 0;j < playerList.size();j++)
			{
				if(playerList.get(j).health > 0)
				{
					tmp.add(playerList.get(j));
				}
			}
			item.setTargetToNearest(tmp);
			Physic.verifyGrav(item,rend.getAPI());
			item.movements(rend.getAPI());
			item.draw(rend);
		}
	}
	
	public void addWorldItem(WorldItem item)
	{
		itemList.add(item);
	}
		
	public void AI()
	{
		for(int i = 0;i < mobList.size();i++)
		{
			Mob mob = mobList.get(i);
			Physic.verifyGrav(mob,rend.getAPI());
			mob.live(rend.getAPI());
		}
		for(int i = 0;i < mobList.size();i++)
		{
			mobList.get(i).draw(rend);
		}
	}
	
	public void getDamagedMob(Player play)
	{
		for(int i = 0;i < mobList.size();i++)
		{
			Mob mob = mobList.get(i);
			if(Mob.distance(play.pos, mob.pos) < 18)
			{
				if(play.turned == 0 || play.turned == 1)
				{
					if(mob.pos.x <= play.pos.x)
					{
						mob.takeDamage(play,play.inv.getSelectedWeaponDamageAmount(),-15);
					}
				}
				else if(play.turned == 2)
				{
					if(mob.pos.x >= play.pos.x)
					{
						mob.takeDamage(play,play.inv.getSelectedWeaponDamageAmount(),15);
					}
				}
			}
		}
	}
	
	public void drawShadows()
	{
		//ArrayList<Point>[] als = new ArrayList[Const.maxLight];
		rend.getAPI().unbindTexture();
		if(setLight)
		{
			for(int k = 0;k < Const.maxLight;k++)
			{
				ArrayList<Point> p1s = new ArrayList<Point>();
				ArrayList<Point> p2s = new ArrayList<Point>();
				for(int i = 0;i < cases.length;i++)
				{
					for(int j = 0;j < cases[0].length;j++)
					{
						if(cases[i][j].light == k)
						{
							Point p1 = cases[i][j].coord.clone(0);
							p1s.add(p1);
							p2s.add(p1.clone(Const.tailleCase));
						}
					}
				}
				rend.drawShadowGroup(p1s, p2s, k);
			}
		}
		rend.getAPI().clearFilter();
	}
	
	public void drawLimit()
	{
		rend.drawChunkLimit();
	}
	
	public void initCases()
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				cases[i][j] = new Tile(i*Const.tailleCase,j*Const.tailleCase);
			}
		}
	}
	
	public void initGen(Random rand, Ore[] ores)
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				cases[i][j].setBlock(chooseBlock(i,j));
				int b = rand.nextInt(ores.length);
				int c = rand.nextInt(luckFactor);
				if(c == 0 && cases[i][j].getBlockSolidity().equals("solid"))
				{
					cases[i][j].ore = ores[b];
					cases[i][j].subID = (cases[i][j].subID + 1 + b) * (-1);
				}
			}
		}
		extendGen(rand);
		if(setLight)
		{
			light();
		}
	}
	
	public int getFirstAirBlockHeight(int posX)
	{
		int gridX = posX/Const.tailleCase;
		for(int i = 0;i < Const.tailleChunkY-2;i++)
		{
			if(cases[gridX][i].getBlockID() == Block.grass.blockID && cases[gridX][i+1].getBlockID() == Block.air.blockID && cases[gridX][i+2].getBlockID() == Block.air.blockID)
			{
				return cases[gridX][i+1].coord.y;
			}
		}
		return -1;
	}
	
	public void choose(int x,int y,Inventory inv,boolean add)
	{
		if(add)
		{
			addBlock(x,y,inv);
		}
		else
		{
			destroyBlock(x,y,inv);
		}
		//cases[x][y].block.setLayer(rend);
	}
	
	public void destroyBlock(int x,int y,Inventory inv)
	{
		if(cases[x][y].getBlockID() != 0)
		{
			boolean mineral = false;
			if(cases[x][y].subID < 0)
			{
				mineral = true;
				cases[x][y].subID = (cases[x][y].subID + 1 + cases[x][y].ore.id) * (-1);
				inv.addMineral(cases[x][y].ore.id,cases[x][y].ore.color);
			}
			Const.debug("(Chunk:destroyBlock): id of worlditem spawned = " + cases[x][y].getBlockID());
			if(mineral)
			{
				addWorldItem(new WorldItem(cases[x][y].coord.x,cases[x][y].coord.y,this,new MineralItem(cases[x][y].subID,cases[x][y].ore.color),1));
			}
			addWorldItem(new WorldItem(cases[x][y].coord.x,cases[x][y].coord.y,this,new ItemBlock(cases[x][y].getBlockID(),cases[x][y].subID),1));
			cases[x][y].destroyBlock();
		}
	}
	
	public void addBlock(int x,int y,Inventory inv)
	{
		if(cases[x][y].getBlockID() == 0)
		{
			for(int i = 0; i < playerList.size();i++)
			{
				Player tmpPlay = playerList.get(i);
				if(tmpPlay.currentCaseLeft().num.x == x && tmpPlay.currentCaseLeft().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (currentCaseLeft)");
					return;
				}
				else if(tmpPlay.currentCaseRight().num.x == x && tmpPlay.currentCaseRight().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (currentCaseRight)");
					return;
				}
				else if(tmpPlay.headLeft().num.x == x && tmpPlay.headLeft().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (headLeft)");
					return;
				}
				else if(tmpPlay.headRight().num.x == x && tmpPlay.headRight().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (headRight)");
					return;
				}
			}
			Item tmp = inv.useItem();
			cases[x][y].setBlock(tmp);
			cases[x][y].light = 6;
		}
	}
}
