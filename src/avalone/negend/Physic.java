package avalone.negend;

import avalone.api.lwjgl.AvaloneGLAPI;

public class Physic 
{
	public static void verifyGrav(Entity ent,AvaloneGLAPI glapi)
	{
		if(ent.vit <= 0)
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
		if(ent.vit > -Const.tailleCase + 1)
		{
			ent.vit = ent.vit + Const.gravity;
		}
		ent.pos.y = ent.pos.y + ent.vit;
		secureScroll(glapi,ent,0,ent.vit);
		if(ent.overHeadLeft().getBlockID() == -1 && ent.overHeadRight().getBlockID() == -1)
		{
			//if(cUp.cases[ent.pos.x/Const.tailleCase][0].blockID == 0 && cUp.cases[(ent.pos.x + ent.tailleX)/Const.tailleCase][0].blockID == 0)
			if(cUp.cases[ent.pos.x/Const.tailleCase][0].getBlockSolidity().equals("nonsolid") && cUp.cases[(ent.pos.x + ent.tailleX -1)/Const.tailleCase][0].getBlockSolidity().equals("nonsolid"))
			{
				ent.changeChunk(cUp, 4, glapi);
			}
			else
			{
				ent.vit = 0;
			}
		}
		else if(ent.overHeadLeft().getBlockSolidity().equals("solid") && ent.overHeadRight().getBlockSolidity().equals("solid"))
		{
			int oldPos = ent.pos.y;
			ent.vit = 0;
			ent.pos.y = ent.overHeadLeft().coord.y - Const.tailleCase * 2;
			secureScroll(glapi,ent,0,(ent.pos.y - oldPos));
		}
	}
	
	public static void checkCollisionFromGravity(Entity ent,AvaloneGLAPI glapi,Chunk cDown)
	{
		ent.oldPos.y = ent.pos.y;
		Tile currentL = ent.currentCaseLeft();
		Tile currentR = ent.currentCaseRight();
		//if(currentL.blockID != 0 || currentR.blockID != 0)
		if(currentL.getBlockSolidity().equals("solid") || currentR.getBlockSolidity().equals("solid"))
		{
			if(currentL.getBlockSolidity().equals("solid") && currentR.getBlockSolidity().equals("solid"))
			{
				if(ent.overHeadLeft().getBlockSolidity().equals("solid") && ent.overHeadRight().getBlockSolidity().equals("solid"))
				{
					System.out.println("enter stucked");
				}
				else
				{
					ent.pos.y = ent.HeadLeft().coord.y;
					ent.vit = 0;
					ent.nbJump = Const.totalJump;
				}
			}
			/*else if(currentL.layer <= ent.layer && currentR.layer <= ent.layer)
			{
				ent.pos.y = ent.HeadLeft().coord.y;
				ent.vit = 0;
				ent.nbJump = Const.totalJump;
			}*/
			else
			{
				fall(ent);
			}
		}
		else if((ent.underFeetLeft().getBlockSolidity().equals("solid") || ent.underFeetLeft().getBlockID() == -1) || 
				ent.underFeetRight().getBlockSolidity().equals("solid") || ent.underFeetRight().getBlockID() == -1)
		//else if(ent.underFeetLeft().blockID != 0 || ent.underFeetRight().blockID != 0)
		{
			/*System.out.println("layer: underenterleft = " + ent.underenterLeft().layer);
			System.out.println("layer: enter = " + ent.layer);*/
			boolean ul = /*ent.underFeetLeft().block.layer > ent.layer ||*/ ent.underFeetLeft().getBlockID() == 0;
			boolean ur = /*ent.underFeetRight().block.layer > ent.layer ||*/ ent.underFeetRight().getBlockID() == 0;
			if(ul && ur)
			{
				fall(ent);
			}
			//if(ent.underenterLeft().layer <= ent.layer || ent.underenterRight().layer <= ent.layer)
			else
			{
				if(ent.currentCaseLeft().yB == ent.pos.y)
				{
					ent.vit = 0;
					ent.nbJump = Const.totalJump;
				}
				else
				{
					if(ent.vit > (-1*Const.tailleCase) + 1)
					{
						ent.vit = ent.vit + Const.gravity;
					}
					if(ent.pos.y + ent.vit > ent.currentCaseLeft().yB)
					{
						ent.pos.y = ent.pos.y + ent.vit;
					}
					else
					{
						ent.pos.y = ent.currentCaseLeft().coord.y;
					}
				}
			}
			if(ent.underFeetLeft().getBlockID() == -1 && ent.underFeetRight().getBlockID() == -1)
			{
				if(ent.pos.x/Const.tailleCase < Const.tailleChunkX)
				{
					if(cDown.cases[ent.pos.x/Const.tailleCase][Const.tailleChunkY-1].getBlockID() == 0)
					{
						ent.changeChunk(cDown, 3, glapi);
					}
				}
			}
		}
		else
		{
			fall(ent);
		}
		secureScroll(glapi,ent,0,ent.pos.y-ent.oldPos.y);
		//System.out.println(ent.pos.y-ent.oldPos.y);
	}
	
	public static void checkCollisionFromMovements()
	{
		
	}
	
	private static void secureScroll(AvaloneGLAPI glapi, Entity ent,int dx,int dy)
	{
		if(ent instanceof Player)
		{
			glapi.scroll(dx,dy);
		}
	}
	
	private static void fall(Entity ent)
	{
		if(ent.vit > (-1*Const.tailleCase) + 1)
		{
			ent.vit = ent.vit + Const.gravity;
		}
		ent.pos.y = ent.pos.y + ent.vit;
	}
	
	/*public Point interpolationLin(Point p1,Point p2,float f)
	{
		int x = (int)(p1.x * (1 - f) + p2.x * f);
		int y = (int)(p1.y * (1 - f) + p2.y * f);
		return new Point(x,y);
	}*/
}
