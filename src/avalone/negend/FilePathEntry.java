package avalone.negend;

import org.gnome.gdk.RGBA;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Fixed;
import org.gnome.gtk.Label;
import org.gnome.gtk.StateFlags;

public class FilePathEntry extends Entry
{
	private GameFile game;
	private Label label;
	
	public FilePathEntry()
	{
		super();
		game = new GameFile(Const.savePath);
		label = new Label();
		setWarning("a new game will start");
		setText(defaultName());
	}
	
	public boolean gameExists(String name)
	{
		for(int i = 0;i < game.al.size();i++)
		{
			String[] s = game.al.get(i);
			String[] splitName = name.split("\\s+");
			if(s.length == splitName.length)
			{
				boolean found = true;
				for(int j = 0;j < s.length;j++)
				{
					if(!s[j].equals(splitName[j]))
					{
						found = false;
					}
				}
				if(found)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public String defaultName()
	{
		boolean searching = true;
		boolean keep = true;
		int nb = 0;
		while(searching)
		{
			keep = true;
			//System.out.println("nb vaut " + nb);
			for(int i = 0; i < game.al.size();i++)
			{
				String[] s = game.al.get(i);
				if(s.length == 3)
				{
					if(s[0].equals("new") && s[1].equals("game") && s[2].equals(String.valueOf(nb)))
					{
						nb++;
						//System.out.println("trouve on cherche un autre");
						keep = false;
						break;
					}
				}
			}
			if(keep)
			{
				searching = false;
			}
		}
		return "new game " + nb;
	}
	
	public void setError(String message)
	{
		label.overrideColor(StateFlags.NORMAL, RGBA.RED);
		label.setLabel(message);
	}
	
	public void setWarning(String message)
	{
		label.overrideColor(StateFlags.NORMAL, RGBA.BLUE);
		label.setLabel(message);
	}
	
	public void putLabel(Fixed fix,int x,int y)
	{
		int posX = x;
		int posY = y;
		fix.put(label,posX,posY);
	}
}
