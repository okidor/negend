package avalone.negend;

import avalone.api.lwjgl.AvaloneGLAPI;

public class Physic 
{
	public static void verifyGrav(Entity ent,AvaloneGLAPI glapi)
	{
		applyGrav(ent);
		secureScroll(glapi,ent,0,ent.vitY);
		if(ent.vitY <= 0)
		{
			//System.out.println("descente");
			checkCollisionFromGravity(ent, glapi,ent.cAround[7]);
		}
		else
		{
			//System.out.println("montee");
			checkCollisionFromJump(ent, glapi,ent.cAround[1]);
		}
	}
	
	public static void checkCollisionFromJump(Entity ent,AvaloneGLAPI glapi,Chunk cUp)
	{
		if(ent.overHeadLeft().getBlockSolidity().equals("solid") || ent.overHeadRight().getBlockSolidity().equals("solid"))
		{
			int oldPos = ent.pos.y;
			ent.vitY = 0;
			ent.pos.y = ent.overHeadLeft().yB - ent.tailleY;
			secureScroll(glapi,ent,0,(ent.pos.y - oldPos));
		}
		else
		{
			if(ent.pos.y + ent.tailleY >= Const.tailleChunkY * Const.tailleCase)
			{
				ent.changeChunk(cUp, 4, glapi);
			}
		}
	}
	
	public static void checkCollisionFromGravity(Entity ent,AvaloneGLAPI glapi,Chunk cDown)
	{
		if(ent.currentCaseLeft().getBlockSolidity().equals("solid") || ent.currentCaseRight().getBlockSolidity().equals("solid"))
		{
			int oldPos = ent.pos.y;
			ent.vitY = 0;
			ent.pos.y = ent.currentCaseLeft().yH;
			secureScroll(glapi,ent,0,(ent.pos.y - oldPos));
			ent.nbJump = Const.totalJump;
		}
		else
		{
			if(ent.pos.y < 0)
			{
				ent.changeChunk(cDown, 3, glapi);
			}
		}
	}
	
	public static void checkCollisionFromLeft(Entity ent,AvaloneGLAPI glapi)
	{
		if(ent.currentCaseLeft().getBlockSolidity().equals("solid") || ent.headLeft().getBlockSolidity().equals("solid"))
		{
			int tmpPosX = ent.pos.x;
			ent.pos.x = ent.currentCaseLeft().xD;
			int diff = -Const.depl - (tmpPosX - ent.pos.x);
			secureScroll(glapi,ent,diff,0);
		}
		else
		{
			if(ent.pos.x < 0)
			{
				ent.changeChunk(ent.cAround[3],1,glapi);
			}
			else
			{
				secureScroll(glapi,ent,-Const.depl,0);
			}
		}
	}
	
	public static void checkCollisionFromRight(Entity ent,AvaloneGLAPI glapi)
	{
		if(ent.currentCaseRight().getBlockSolidity().equals("solid") || ent.headRight().getBlockSolidity().equals("solid"))
		{
			int tmpPosX = ent.pos.x;
			ent.pos.x = ent.currentCaseRight().xG - ent.tailleX;
			int diff = Const.depl - (tmpPosX - ent.pos.x);
			secureScroll(glapi,ent,diff,0);
		}
		else
		{
			if(ent.pos.x + ent.tailleX > Const.tailleChunkX * Const.tailleCase)
			{
				ent.changeChunk(ent.cAround[5],2,glapi);
			}
			else
			{
				secureScroll(glapi,ent,Const.depl,0);
			}
		}
	}
	
	public static void unstuck(WorldItem item)
	{
		int i = 0;
		if(!item.currentCaseLeft().getBlockSolidity().equals("solid"))
		{
			if(item.currentCaseLeft().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
		}
		else if(!item.currentCaseRight().getBlockSolidity().equals("solid"))
		{
			if(item.currentCaseRight().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
		}
		else if(!item.underFeetLeft().getBlockSolidity().equals("solid"))
		{
			if(item.underFeetLeft().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
			if(item.underFeetLeft().coord.y + Const.tailleCase/2 < item.pos.y)
			{
				item.pos.y = item.pos.y - 2;
			}
		}
		else if(!item.underFeetRight().getBlockSolidity().equals("solid"))
		{
			if(item.underFeetRight().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
			if(item.underFeetRight().coord.y + Const.tailleCase/2 < item.pos.y)
			{
				item.pos.y = item.pos.y - 2;
			}
		}
		else if(!item.leftOfFeet().getBlockSolidity().equals("solid"))
		{
			if(item.leftOfFeet().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
		}
		else if(!item.rightOfFeet().getBlockSolidity().equals("solid"))
		{
			if(item.rightOfFeet().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
		}
		else if(!item.headLeft().getBlockSolidity().equals("solid"))
		{
			if(item.headLeft().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
			if(item.headLeft().coord.y + Const.tailleCase/2 > item.pos.y)
			{
				item.pos.y = item.pos.y + 2;
			}
		}
		else if(!item.headRight().getBlockSolidity().equals("solid"))
		{
			if(item.headRight().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
			if(item.headRight().coord.y + Const.tailleCase/2 > item.pos.y)
			{
				item.pos.y = item.pos.y + 2;
			}
		}
	}
	
	public static void airFriction()
	{
		
	}
	
	private static void secureScroll(AvaloneGLAPI glapi, Entity ent,int dx,int dy)
	{
		if(ent instanceof Player)
		{
			glapi.scroll(dx,dy);
		}
	}
	
	private static void applyGrav(Entity ent)
	{
		if(ent.vitY > -Const.tailleCase + 1)
		{
			ent.vitY = ent.vitY + Const.gravity;
		}
		ent.pos.y = ent.pos.y + ent.vitY;
	}
}
