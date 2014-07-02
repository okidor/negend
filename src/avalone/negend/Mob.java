package avalone.negend;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

public class Mob extends AliveEntity
{
	private int chaseRadius;
	private Player target;
	private int targetOffsetX;
	public ArrayList<Player> playerList;
	
	public Mob(int posX,int posY,Chunk spawn)
	{
		super(posX,posY,spawn);
		chaseRadius = 30;
		playerList = new ArrayList<Player>();
		nbJump = Const.totalJump;
		tailleX = Const.tailleCase;
		tailleY = Const.tailleCase*2;
		maxHealth = 50;
		health = 50;
		initInventoryRandom();
	}
	
	public Mob(int posX,int posY,Chunk spawn,Player play)
	{
		this(posX,posY,spawn);
		playerList.add(play);
	}
	
	public Mob(int posX,int posY,Chunk spawn,ArrayList<Player> list)
	{
		this(posX,posY,spawn);
		playerList = list;
	}
	
	public void AddPlayer(Player play)
	{
		playerList.add(play);
	}
	
	public void initInventoryRandom()
	{
		int i = tierDropped(new Random());
		inv.addItem(new WeaponItem(0,0,i,0,"epee.png"), 1);
		inv.addItem(new ItemBlock(Block.dirt.blockID,0), 100);
	}
	
	public void onDeath()
	{
		currentChunk.mobList.remove(this);
		destroy();
	}
	
	public void setTarget()
	{
		for(int i = 0;i < playerList.size();i++)
		{
			if(verifPlayer(playerList.get(i)))
			{
				target = playerList.get(i);
				return;
			}
		}
		target = null;
	}
	
	private boolean verifPlayer(Player play)
	{
		if(currentChunk == play.currentChunk)
		{
			targetOffsetX = 0;
			return verifDistance(play,play.pos);
		}
		else if(currentChunk.pos.x == play.currentChunk.pos.x-1)
		{
			Point realPos = play.pos.clone(Const.tailleChunkX*tailleCase,0);
			targetOffsetX = 1200;
			return verifDistance(play,realPos);
		}
		else if(currentChunk.pos.x == play.currentChunk.pos.x+1)
		{
			Point realPos = play.pos.clone(-Const.tailleChunkX*tailleCase,0);
			targetOffsetX = -1200;
			return verifDistance(play,realPos);
		}
		return false;
	}
	
	private boolean verifDistance(Player play,Point playerPos)
	{
		if(distance(playerPos,pos) <= chaseRadius*tailleCase)
		{
			target = play;
			return true;
		}
		else
		{
			if(target == play)
			{
				target = null;
			}
			return false;
		}
	}
	
	public void draw(Renderer rend)
	{
		rend.draw(pos, pos.clone(tailleX,tailleY), "mob" + turned + ".png");
		if(damaged)
		{
			rend.getAPI().drawAlphaRect(pos, pos.clone(tailleX,tailleY),"red",0.5f);
		}
	}
	
	public void movements(AvaloneGLAPI glapi)
	{
		turned = 1;
		setTarget();
		if(target != null)
		{
			if(target.pos.x+targetOffsetX < pos.x)
			{
				if(leftOfFeet().getBlockSolidity().equals("solid"))
				{
					jump();
				}
				boolean outOfChunk = currentCaseLeft().coord.x >= pos.x;
				boolean wontCollide = currentCaseLeft().xG - pos.x < -2;
				sideActions(glapi,0,leftOfHead(),leftOfFeet(),currentCaseLeft(),HeadLeft(),cAround[3],cAround[1],-2,1,currentCaseLeft().xG,Const.tailleChunkX -1,/*-1,*/outOfChunk,pos.x,wontCollide);
			}
			else if(target.pos.x+targetOffsetX > pos.x)
			{
				if(rightOfFeet().getBlockSolidity().equals("solid"))
				{
					jump();
				}
				boolean outOfChunk = currentCaseRight().coord.x + Const.tailleCase <= pos.x+tailleX;
				boolean wontCollide = currentCaseRight().xD - (pos.x + tailleX) > 2;
				sideActions(glapi,2,rightOfHead(),rightOfFeet(),currentCaseRight(),HeadRight(),cAround[5],cAround[1],2,2,currentCaseRight().xD,0,/*1,*/outOfChunk,pos.x + tailleX,wontCollide);
			}
		}
	}
	
	public void sideActions(AvaloneGLAPI glapi,int turn,Tile sidePlayerHead,Tile sidePlayerFeet,Tile currentCaseSide,Tile playerHeadSide,Chunk sideChunk,Chunk cUp,int sideDepl,int chunkTransition,int blockX,int offsetX,boolean outOfChunk,int posX,boolean wontCollide)
	{
		turned = turn;		//selectionne la texture du perso
		if((!sidePlayerHead.getBlockSolidity().equals("solid")/* || sidePlayerHead.block.layer > layer*/ ) &&
		   (!sidePlayerFeet.getBlockSolidity().equals("solid")/* || sidePlayerFeet.block.layer > layer*/))
		{
			if(sidePlayerHead.getBlockID() == -1 && sidePlayerFeet.getBlockID() == -1)
			{
				if(outOfChunk)
				{
					if(!sideChunk.cases[offsetX][playerHeadSide.num.y].equals("solid") && 
					   !sideChunk.cases[offsetX][currentCaseSide.num.y].equals("solid"))
					{
						changeChunk(sideChunk,chunkTransition,glapi);
					}
					else
					{
						int diff = blockX - posX;
						pos.x = pos.x + diff;
					}
				}
				else
				{
					pos.x = pos.x + sideDepl;
				}
			}
			else if(sidePlayerHead.getBlockID() == -1 && playerHeadSide.getBlockID() == -1)
			{
				if(!cUp.cases[sidePlayerFeet.num.x][0].getBlockSolidity().equals("solid") && !sidePlayerFeet.getBlockSolidity().equals("solid"))
				{
					pos.x = pos.x + sideDepl;
				}
				else
				{
					int diff = blockX - posX;
					pos.x = pos.x + diff;
				}
			}
			else
			{
				pos.x = pos.x + sideDepl;
			}
		}
		else if(wontCollide)
		{
			pos.x = pos.x + sideDepl;
		}
		else
		{
			int diff = blockX - posX;
			pos.x = pos.x + diff;
		}
	}
	
	public int tierDropped(Random rand)
	{
		double d = 0;
		double random = rand.nextDouble();
		for(int i = 0;i <= 26165;i++)
		{
			d = d + (1/7700.0) * Math.pow(1.001,i/8.0);
			if(d > random)
			{
				return i;
			}
		}
		return 26165;
	}
	
	public int tierTestDropped(Random rand,double random)
	{
		double d = 0;
		for(int i = 0;i <= 26165;i++)
		{
			d = d + (1/7700.0) * Math.pow(1.001,i/8.0);
			if(d > random)
			{
				return i;
			}
		}
		return 26165;
	}
	
	public void changeChunk(Chunk newC,int n,AvaloneGLAPI glapi)
	{
		currentChunk.mobList.remove(this);
		super.changeChunk(newC,n,glapi);
		currentChunk.mobList.add(this);
	}
}
