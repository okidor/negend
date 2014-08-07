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
	private boolean transiting;
	private boolean wasButtonDown;
	public int xp; public int level;
	
	public Player(int posX,int posY,Chunk spawn)
	{
		super(posX,posY,spawn);
		vitY = 0;//dx = 0;
		tailleX = Const.tailleCase;
		tailleY = Const.tailleCase*2;
		nbJump = Const.totalJump;
		zoom = 1;
		mouse = new Point();
		wasButtonDown = false;
		health = 100;maxHealth = 100;
		xp = 0;level = 0;
		spawn.playerList.add(this);
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
	            	jump();
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
	    			Const.debug("(Player:movements): chunk reference: " + currentChunk);
	    		}
	     		if (Keyboard.getEventKey() == Keyboard.KEY_6)
	    		{
	    			
	    		}
	        }
	    }
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			pos.x = pos.x - Const.depl;
			turned = 0;
			Physic.checkCollisionFromLeft(this, glapi);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			pos.x = pos.x + Const.depl;
			turned = 2;
			Physic.checkCollisionFromRight(this, glapi);
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
		Const.debug("(Player:changeChunk): oldChunkX = " + currentChunk.pos.x);
		Const.debug("(Player:changeChunk): oldChunkY = " + currentChunk.pos.y);
		currentChunk.playerList.remove(this);
		super.changeChunk(newC,n,glapi);
		currentChunk.playerList.add(this);
		Const.debug("(Player:changeChunk): newChunkX = " + currentChunk.pos.x);
		Const.debug("(Player:changeChunk): newChunkY = " + currentChunk.pos.y);
		Map.allowedToDraw = false;
		if(newC instanceof SurfaceChunk)
		{
			Const.debug("(Player:changeChunk): biome id = " +((SurfaceChunk) newC).biome);
		}
		int centerX = Const.tailleFenX/2;
		int centerY = Const.tailleFenY/2;
		if(n == 1)
		{
			//glapi.scroll((Const.tailleChunkX-1)*tailleCase+1,0);
			glapi.setView(centerX-Const.tailleCase,pos.y-centerY);
		}
		else if(n == 2)
		{
			//glapi.scroll(-(Const.tailleChunkX-1)*tailleCase-1,0);
			glapi.setView(-centerX,pos.y-centerY);
		}
		else if(n == 3)
		{
			glapi.setView(pos.x-centerX, centerY-Const.tailleCase);
		}
		else if(n == 4)
		{
			glapi.setView(pos.x-centerX, -centerY);
		}
	}
}
