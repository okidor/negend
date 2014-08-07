package avalone.negend;

import avalone.api.lwjgl.Point;

public class WeaponItem extends Item
{
	public WeaponItem(int id,int subID,int tier,int level,String texture)
	{
		super(id+ Const.unplacableOffset + 1,subID,EnumItem.weapon,tier,level,texture);
	}
	
	public void setType()
	{
		type = 3;
	}
}
