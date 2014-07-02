package avalone.negend;

public enum EnumItem 
{
	vide(false,false,false,false),
	standardBlock(true,false,false,true),
	mineralBlock(true,true,false,true),
	weapon(false,false,true,false);
	
	public boolean placable;
	public boolean isMineral;
	public boolean isWeapon;
	public boolean stackable;
	
	private EnumItem(boolean placable,boolean isMineral,boolean isWeapon,boolean stackable)
	{
		this.placable = placable;
		this.isMineral = isMineral;
		this.isWeapon = isWeapon;
		this.stackable = stackable;
	}
	
	//for (EnumItem it : EnumItem.values()) {
}
