package avalone.negend;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.FPoint;
import avalone.api.lwjgl.Pack;
import avalone.api.lwjgl.Point;
import avalone.api.util.StringBuilder;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Player extends AliveEntity
{
	public Point mouse;
	//public int dx;
	public int zoom;
	private int depl;
	private boolean transiting;
	private boolean wasButtonDown;
	public int xp; public int level;
	
	public Player(int posX,int posY,Chunk spawn)
	{
		super(posX,posY,spawn);
		vit = 0;//dx = 0;
		tailleX = Const.tailleCase;
		tailleY = Const.tailleCase*2;
		nbJump = Const.totalJump;
		zoom = 1;
		mouse = new Point();
		depl = Const.depl;
		wasButtonDown = false;
		health = 100;maxHealth = 100;
		xp = 0;level = 0;
	}
	
	public void draw(Renderer rend)
	{
		rend.draw(pos, pos.clone(tailleX,tailleY), "player" + turned + ".png");
		inv.draw(rend,pos,turned);
		//rend.drawText(pos.clone(0,100), pos.clone(132,+132), "123456","white");
		float f = (float)health / 100.0f;
		rend.getAPI().drawTexturedCoordRect(pos.clone(500,280), pos.clone(600,300), new FPoint(0.0f,0.0f), new FPoint(f,1.0f),"health.png");
	}
	
	public void movements(AvaloneGLAPI glapi)
	{
		turned = 1;
		while (Keyboard.next()) 
		{
	        if (Keyboard.getEventKeyState()) 
		    {
	        	if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) 
	            {
	            	if(nbJump > 0 /*&& cAround[1].cases[play.pos.x/Const.tailleCase][]*/)
	            	{
	            		vit = vit + 10;
	            		nbJump--;
	            	}
	            }
	            if (Keyboard.getEventKey() == Keyboard.KEY_P)
	            {
	     			zoom = zoom + 1;
	     			glapi.zoom(zoom);
	            }
	     		if (Keyboard.getEventKey() == Keyboard.KEY_M)
	     		{
	     			zoom = 1;
	     			glapi.clearZoom();
	     		}
	     		if (Keyboard.getEventKey() == Keyboard.KEY_I)
	    		{
	    			inv.slotmax = 4 - inv.slotmax;
	    		}
	     		if (Keyboard.getEventKey() == Keyboard.KEY_4)
	    		{
	    			
	    		}
	     		if (Keyboard.getEventKey() == Keyboard.KEY_6)
	    		{
	    			
	    		}
	        }
	    }
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			boolean outOfChunk = currentCaseLeft().coord.x >= pos.x;
			boolean wontCollide = currentCaseLeft().xG - pos.x < -depl;
			sideActions(glapi,0,leftOfHead(),leftOfFeet(),currentCaseLeft(),HeadLeft(),cAround[3],cAround[1],-depl,1,currentCaseLeft().xG,Const.tailleChunkX -1,/*-1,*/outOfChunk,pos.x,wontCollide);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			boolean outOfChunk = currentCaseRight().coord.x + Const.tailleCase <= pos.x+tailleX;
			boolean wontCollide = currentCaseRight().xD - (pos.x + tailleX) > depl;
			sideActions(glapi,2,rightOfHead(),rightOfFeet(),currentCaseRight(),HeadRight(),cAround[5],cAround[1],depl,2,currentCaseRight().xD,0,/*1,*/outOfChunk,pos.x + tailleX,wontCollide);
		}
		if (Mouse.isButtonDown(0))
		{
			if(inv.slotmax == 3)
			{
				if(!wasButtonDown)
				{
					wasButtonDown = true;
					inv.setMouseItem(glapi.getMouse(),pos);
				}
			}
			else
			{
				ClickActions(glapi,false);
			}
		}
		else if(wasButtonDown && !Mouse.isButtonDown(0))
		{
			wasButtonDown = false;
		}
		else if(Mouse.isButtonDown(1))
		{
			if(inv.getSelectedID() != 0)
			{
				ClickActions(glapi,true);
			}
		}
		else
		{
			inv.selectedItem = glapi.changeValueWithWheel(inv.selectedItem,-1);
		}
	}
	
	public void sideActions(AvaloneGLAPI glapi,int turn,Tile sidePlayerHead,Tile sidePlayerFeet,Tile currentCaseSide,Tile playerHeadSide,Chunk sideChunk,Chunk cUp,int sideDepl,int chunkTransition,int blockX,int offsetX,/*int dxx,*/boolean outOfChunk,int posX,boolean wontCollide)
	{
		turned = turn;		//selectionne la texture du perso
		if((!sidePlayerHead.getBlockSolidity().equals("solid") /*|| sidePlayerHead.block.layer > layer*/ ) &&
		   (!sidePlayerFeet.getBlockSolidity().equals("solid") /*|| sidePlayerFeet.block.layer > layer*/))
		{
			if(sidePlayerHead.getBlockID() == -1 && sidePlayerFeet.getBlockID() == -1)
			{
				if(outOfChunk)
				{
					if(!sideChunk.cases[offsetX][playerHeadSide.num.y].equals("solid") && 
					   !sideChunk.cases[offsetX][currentCaseSide.num.y].equals("solid"))
					{
						transiting = true;
						//System.out.println("sideChunk:" + sideChunk.pos.x + "," + sideChunk.pos.y);
						changeChunk(sideChunk,chunkTransition,glapi);
						//System.exit(0);
					}
					else
					{
						int diff = blockX - posX;
						moveAndScroll(glapi,diff);
					}
				}
				else
				{
					//dx = dxx;
					moveAndScroll(glapi,sideDepl);
				}
			}
			else if(sidePlayerHead.getBlockID() == -1 && playerHeadSide.getBlockID() == -1)
			{
				if(!cUp.cases[sidePlayerFeet.num.x][0].getBlockSolidity().equals("solid") && !sidePlayerFeet.getBlockSolidity().equals("solid"))
				{
					//dx = dxx;
					moveAndScroll(glapi,sideDepl);
				}
				else
				{
					int diff = blockX - posX;
					moveAndScroll(glapi,diff);
				}
			}
			else
			{
				//dx = dxx;
				moveAndScroll(glapi,sideDepl);
			}
		}
		else if(wontCollide)
		{
			moveAndScroll(glapi,sideDepl);
		}
		else
		{
			int diff = blockX - posX;
			moveAndScroll(glapi,diff);
		}
	}
	
	public void moveAndScroll(AvaloneGLAPI glapi,int move)
	{
		pos.x = pos.x + move;
		glapi.scroll(move,0);
	}
	
	public void ClickActions(AvaloneGLAPI glapi,boolean add)
	{
		mouse = glapi.getMouse();
		if(inv.isWeaponSelected())
		{
			currentChunk.getDamagedMob(this);
		}
		else
		{
			boolean left = false;boolean right = false;boolean current = false;
			if(mouse.x < 0)
			{
				left = true;
			}
			else if(mouse.x/tailleCase >= Const.tailleChunkX)
			{
				right = true;
			}
			else
			{
				current = true;
			}
			yVerif(left,right,current,add);
		}
	}
	
	public void yVerif(boolean left,boolean right,boolean current,boolean add)
	{
		if(mouse.y < 0)
		{
			if(current)
			{
				cAround[7].choose(mouse.x/tailleCase,(mouse.y+608)/tailleCase,inv,add);
			}
			else if(left)
			{
				cAround[6].choose((mouse.x+1200)/tailleCase,(mouse.y+608)/tailleCase,inv,add);
			}
			else if(right)
			{
				cAround[8].choose((mouse.x-1200)/tailleCase,(mouse.y+608)/tailleCase,inv,add);
			}
			else
			{
				System.out.println("error when verifying height");
			}
		}
		else if(mouse.y/tailleCase >= Const.tailleChunkY)
		{
			if(current)
			{
				cAround[1].choose(mouse.x/tailleCase,(mouse.y-608)/tailleCase,inv,add);
			}
			else if(left)
			{
				cAround[0].choose((mouse.x+1200)/tailleCase,(mouse.y-608)/tailleCase,inv,add);
			}
			else if(right)
			{
				cAround[2].choose((mouse.x-1200)/tailleCase,(mouse.y-608)/tailleCase,inv,add);
			}
			else
			{
				System.out.println("error when verifying height");
			}
		}
		else
		{
			if(current)
			{
				currentChunk.choose(mouse.x/tailleCase,mouse.y/tailleCase,inv,add);
			}
			else if(left)
			{
				cAround[3].choose((mouse.x+1200)/tailleCase,mouse.y/tailleCase,inv,add);
			}
			else if(right)
			{
				cAround[5].choose((mouse.x-1200)/tailleCase,mouse.y/tailleCase,inv,add);
			}
			else
			{
				System.out.println("error when verifying height");
			}
		}
	}
	
	public void onDeath()
	{
		currentChunk.playerList.remove(this);
		//respawn();
	}
			
	public void changeChunk(Chunk newC,int n,AvaloneGLAPI glapi)
	{
		currentChunk.playerList.remove(this);
		super.changeChunk(newC,n,glapi);
		currentChunk.playerList.add(this);
		Map.allowedToDraw = false;
		if(n == 1)
		{
			glapi.scroll((Const.tailleChunkX-1)*tailleCase+1,0);
		}
		else if(n == 2)
		{
			glapi.scroll(-(Const.tailleChunkX-1)*tailleCase-1,0);
		}
		else if(n == 3)
		{
			//glapi.scroll(0,(Const.tailleChunkY-2)*tailleCase + 13);
			//glapi.scroll(0, Const.tailleFenY - tailleY);
			int centerX = Const.tailleFenX/2;
			int centerY = Const.tailleFenY/2;
			glapi.setView(pos.x-centerX, Const.tailleChunkY+centerY-54);//pourquoi 54 ??? :/
		}
		else if(n == 4)
		{
			//glapi.scroll(0,-(Const.tailleChunkY-2)*tailleCase - 13);
			//glapi.scroll(0, -Const.tailleFenY + tailleY);
			int centerX = Const.tailleFenX/2;
			int centerY = Const.tailleFenY/2;
			glapi.setView(pos.x-centerX, -centerY);
		}
	}
}
