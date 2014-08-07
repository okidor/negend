package avalone.negend;

import java.util.ArrayList;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Point;

public class WorldItem extends Entity
{
	private Item item;
	private int amount;
	private AliveEntity target;
	private int radius;
	private boolean shouldNotSpawn;
	
	protected WorldItem(int posX, int posY, Chunk spawn,Item item,int amount) 
	{
		super(posX + (Const.tailleCase - Const.tailleItem)/2, posY/* + (Const.tailleCase - Const.tailleItem)/2*/, spawn);
		this.item = item;
		this.amount = amount;
		radius = 2;
		tailleX = Const.tailleItem;
		tailleY = Const.tailleItem;
		vitY = 8;
		shouldNotSpawn = false;
		setAroundFromChunk(spawn);
		changeItem();
	}

	public void movements(AvaloneGLAPI glapi) 
	{
		if(target == null)
		{
			//System.out.println("target is null");
			return;
		}
		if(shouldNotSpawn)
		{
			currentChunk.itemList.remove(this);
			return;
		}
		if(pos.x + tailleX >= target.pos.x && pos.x <= target.pos.x + target.tailleX)
		{
			if(pos.y + tailleY >= target.pos.y && pos.y <= target.pos.y + target.tailleY)
			{
				target.inv.addItem(item,amount);
				Const.debug("(WorldItem:movements): id = " + item.id + " et subID = " + item.subID);
				Const.debug("(WorldItem:movements):" + item.isStackable());
				//destroy();
				currentChunk.itemList.remove(this);
			}
		}
		else if(distance(pos,target.pos) < radius * Const.tailleCase)
		{
			goTowards(target);
		}
		else
		{
			Physic.unstuck(this);
		}
	}
	
	private void goTowards(Entity ent)
	{
		int distX = pos.x - ent.pos.x;
		if(distX < 0)
		{
			distX = -distX;
		}
		int spdX = radius*tailleCase - distX;
		if(spdX > 5)
		{
			spdX = 5;
		}
		
		int distY = pos.y - ent.pos.y;
		if(distY < 0)
		{
			distY = -distY;
		}
		int spdY = radius*tailleCase - distY;
		if(spdY > 5)
		{
			spdY = 5;
		}
	}
	
	public void changeItem()
	{
		switch(item.id)
		{
			case 1:
				item.id = 2;
				item.subID = 0;
				item.texture = Block.getBlock(2).getTexture(0);
				break;
			case 5:
				item.id = 0;
				item.subID = 0;
				item.texture = Block.getBlock(0).getTexture(0);
				shouldNotSpawn = true;
			case 10:
				item.id = 2;
				item.subID = 0;
				item.texture = Block.getBlock(2).getTexture(0);
				break;
			default:
				break;
		}
	}
	
	public void draw(Renderer rend)
	{
		rend.draw(pos,pos.clone(Const.tailleItem),item.texture);
	}
	
	public void setTarget(AliveEntity ent)
	{
		target = ent;
	}
	
	public void setTargetToNearest(ArrayList<AliveEntity> entities)
	{
		if(!entities.isEmpty())
		{
			double mindist = distance(pos,entities.get(0).pos);
			int indice = 0;
			for(int i = 1; i < entities.size();i++)
			{
				if(mindist > distance(pos,entities.get(i).pos))
				{
					mindist = distance(pos,entities.get(i).pos);
					indice = i;
				}
			}
			setTarget(entities.get(indice));
		}
	}

}
