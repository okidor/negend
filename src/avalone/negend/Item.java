package avalone.negend;

import avalone.api.lwjgl.Point;

public class Item 
{
	public int id;
	public int subID;
	private EnumItem en;
	public int tier;
	public int level;
	public int damage;
	public String texture;
	protected int type;
	
	public Item(int id,int subID,EnumItem en,int tier,int level,String texture)
	{
		this.id = id;
		this.subID = subID;
		this.en = en;
		this.tier = tier;
		this.level = level;
		damage = level*tier;
		this.texture = texture;
		setType();
	}
	
	public void setType()
	{
		type = 0;
	}
	
	public boolean isPlacable()
	{
		return en.placable;
	}
	
	public boolean isMineral()
	{
		return en.isMineral;
	}
	
	public boolean isWeapon()
	{
		return en.isWeapon;
	}
	
	public boolean isStackable()
	{
		return en.stackable;
	}
	
	public void draw(Renderer rend,Point p)
	{
		p.moveCoords(7, 7);
		rend.draw(p,p.clone(16),texture);
	}
	
	public void drawHand(Renderer rend,Point posPlayer,int turned)
	{
		if(turned == 0)
		{
			Point p = posPlayer.clone(-7,5);
			rend.draw(p,p.clone(16),texture);
		}
		else if(turned == 1)
		{
			Point p = posPlayer.clone(-15,5);
			rend.draw(p,p.clone(16),texture);
		}
		else if(turned == 2)
		{
			Point p = posPlayer.clone(7,5);
			Point p2 = p.clone(16);
			p.moveCoords(16, 0);
			p2.moveCoords(-16,0);
			rend.draw(p,p2,texture);
		}
	}
}
