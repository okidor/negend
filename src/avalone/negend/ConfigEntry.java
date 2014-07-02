package avalone.negend;

import org.gnome.gtk.*;
import org.gnome.gdk.Event;
import org.gnome.gdk.RGBA;

public class ConfigEntry extends Entry
{
	private GameFile game;
	private String[] cutLine;
	private Label label;
	
	public ConfigEntry(int line)
	{
		game = new GameFile(Const.configPath);
		label = new Label();
		cutLine = game.al.get(line);
	}
	
	public void setLabelFromLinePart(int wordNumber)
	{
		setText(cutLine[wordNumber]);
	}
	
	public void modifyLine(int wordNumber,String message)
	{
		cutLine[wordNumber] = message;
		game.write();
	}
	
	public void setError(String message)
	{
		label.overrideColor(StateFlags.NORMAL, RGBA.RED);
		label.setLabel(message);
	}
	
	public void putLabel(Fixed fix,int x,int y)
	{
		int posX = x;
		int posY = y;
		fix.put(label,posX,posY);
	}
}
