package avalone.negend;

import java.util.ArrayList;

import avalone.api.lwjgl.Point;

public class Inventory 
{
	private Item objectType[];
	private int objectNumber[];
	public int selectedItem;
	public int slotmax;
	private Item onMouseItem;
	private int onMouseNumber;
	private AliveEntity owner;
	private int size;
	
	private static final Item noItem = new ItemBlock(0,0);
	
	public Inventory(AliveEntity owner)
	{
		objectType = new Item[30];
		objectNumber = new int[30];
		selectedItem = 0;
		slotmax = 1;
		size = 30;
		this.owner = owner;
		init();
	}
	
	public void init()
	{
		GameFile game = new GameFile("mod/startInventory.txt");
		ArrayList<String[]> slotList = new ArrayList<String[]>();
		slotList = game.al;
		for(int i = 0;i < size;i++)
		{
			/*String[] s = slotList.get(i);
			if(Integer.decode(s[3]) <= Const.unplacableOffset)
			{
				objectType[Integer.decode(s[1])] = new Item(Integer.decode(s[3]),Integer.decode(s[4]));
				objectNumber[Integer.decode(s[1])] = Integer.decode(s[5]);
			}*/
			objectType[i] = noItem;
			objectNumber[i] = 0;
		}
		onMouseItem = noItem;
		onMouseNumber = 0;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getSelectedID()
	{
		return objectType[selectedItem].id;
	}
	
	public boolean isWeaponSelected()
	{
		return objectType[selectedItem].isWeapon();
	}
	
	public int getSelectedWeaponDamageAmount()
	{
		if(isWeaponSelected())
		{
			return objectType[selectedItem].damage;
		}
		return 0;
	}
	
	public boolean isEmptySlot(int ind)
	{
		if(ind >= 0 && ind < 30)
		{
			if(objectType[ind] == noItem)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
	public WorldItem dropItem(int ind)
	{
		if(objectNumber[ind] >= 1)
		{
			WorldItem item = new WorldItem(owner.pos.x,owner.pos.y,owner.currentChunk,objectType[ind],1);
			objectNumber[ind] --;
			if(objectNumber[ind] == 0)
			{
				objectType[ind] = noItem;
			}
			return item;
		}
		return null;
	}
	
	public WorldItem dropItemStack(int ind)
	{
		if(objectNumber[ind] >= 1)
		{
			WorldItem item = new WorldItem(owner.pos.x,owner.pos.y,owner.currentChunk,objectType[ind],objectNumber[ind]);
			objectNumber[ind] = 0;
			objectType[ind] = noItem;
			return item;
		}
		return null;
	}
	
	public Item useItem()
	{
		if(objectType[selectedItem].isPlacable())
		{
			if(objectNumber[selectedItem] >= 1)
			{
				objectNumber[selectedItem] --;
				if(objectNumber[selectedItem] == 0)
				{
					Item tmp = objectType[selectedItem];
					objectType[selectedItem] = noItem;
					return tmp;
				}
			}
			return objectType[selectedItem];
		}
		return noItem;
	}
	
	public void addItem(Item item,int amount)
	{
		System.out.println("added " + amount + " of " + item + " to " + owner);
		if(item.isStackable())
		{
			for(int i = 0;i < 30;i++)
			{
				if(objectType[i].id == item.id && objectType[i].subID == item.subID && objectNumber[i] < 999)
				{
					objectNumber[i] = objectNumber[i] + amount;
					return;
				}
			}
		}
		for(int i = 0;i < size;i++)
		{
			if(objectType[i].id == 0)
			{
				objectType[i] = item;
				objectNumber[i] = amount;
				return;
			}
		}
		System.out.println("warning, inventory is full");
	}
	
	public void addMineral(int subID,float[] color)
	{
		MineralItem invItem = new MineralItem(subID,color); 
		for(int i = 0;i < size;i++)
		{
			if(objectType[i].id == invItem.id && objectType[i].subID == invItem.subID && objectNumber[i] < 999)
			{
				objectNumber[i]++;
				return;
			}
		}
		for(int i = 0;i < size;i++)
		{
			if(objectType[i].id == 0)
			{
				objectType[i] = invItem;
				objectNumber[i] = 1;
				return;
			}
		}
	}
	
	public void setMouseItem(Point p,Point pos)
	{
		int invSlot = getSlotFromPoint(p,pos);
		if(invSlot != -1)
		{
			Item tmpItem = onMouseItem;
			int tmpNumber = onMouseNumber;
			onMouseItem = objectType[invSlot];
			onMouseNumber = objectNumber[invSlot];
			objectType[invSlot] = tmpItem;
			objectNumber[invSlot] = tmpNumber;
		}
	}
	
	public int getSlotFromPoint(Point p,Point pos)
	{
		for(int j = 0;j < slotmax;j++)
		{
			Point p1 = new Point(pos.x - 141,270 + pos.y -(j * 35));
			for(int i = 0;i < 10;i++)
			{
				p1.moveCoords(35,0);
				Point p2 = p1.clone(30);
				if(p.x >= p1.x && p.x <= p2.x && p.y >= p1.y && p.y <= p2.y)
				{
					return j*10+i;
				}
			}
		}
		return -1;
	}
	
	public void draw(Renderer rend,Point pos,int turned)
	{
		if(selectedItem < 0)
		{
			selectedItem = 0;
		}
		else if(selectedItem > 9)
		{
			selectedItem = 9;
		}
		for(int j = 0;j < slotmax;j++)
		{
			Point p1 = new Point(pos.x - 141,270 + pos.y -(j * 35));
			for(int i = 0;i < 10;i++)
			{
				p1.moveCoords(35,0);
				if(j*10+i == selectedItem)
				{
					rend.draw(p1, p1.clone(30), "red.png");
					if(objectType[j*10+i].id != 0)
					{
						objectType[j*10+i].drawHand(rend,pos,turned);
					}
				}
				else
				{
					rend.draw(p1, p1.clone(30), "case.png");
				}
				if(objectType[j*10+i].id != 0)
				{
					objectType[j*10+i].draw(rend,p1.clone(0));
					drawNumber(rend,objectNumber[j*10+i],p1);
				}
			}
		}
		if(onMouseItem.id != 0)
		{
			Point p = rend.getAPI().getMouse();
			onMouseItem.draw(rend,p.clone(-15));
			drawNumber(rend,onMouseNumber,p.clone(-15));
		}
	}
	
	public void drawNumber(Renderer rend,int number,Point p1)
	{
		String s = "" + number;
		while(s.length() < 3)
		{
			s = " " + s;
		}
		rend.drawText(p1, p1.clone(16), s, "red");
	}
	
	public Item itemForSave(int ind)
	{
		return objectType[ind];
	}
	
	public int numberForSave(int ind)
	{
		return objectNumber[ind];
	}
}
