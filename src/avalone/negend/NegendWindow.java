package avalone.negend;

import java.io.File;

//import org.gnome.gtk.*;
import org.gnome.gdk.Event;
import org.gnome.gtk.Button;
import org.gnome.gtk.Fixed;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.Image;
import org.gnome.gtk.Label;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;
import org.gnome.gtk.WindowPosition;

public class NegendWindow extends Window
{
	private Fixed fix;
	public int mode;
	public boolean newGame;
	FilePathEntry fpath;
	private Negend caller;
	private int nb;
	private Button partie;
	
	public NegendWindow(Negend caller,int nb)
	{
		this.caller = caller;
		this.nb = nb;
		newGame = true;
		setTitle("Negend");
		fix = new Fixed();
		
		initUI();
		
		add(fix);
        
        setDefaultSize(500, 400);
        setPosition(WindowPosition.CENTER);
        
        showAll();
	}
	
	public void initUI()
	{
		connect(new Window.DeleteEvent() 
	    {
			public boolean onDeleteEvent(Widget source, Event event) 
	        {
	            mode = Const.QUIT_MODE;
	            Gtk.mainQuit();
	            return false;
	        }
	    });
	
		initButtons();
		initLabels();
		initEntries();
		initImages();
	}
	
	public void initButtons()
	{
        Button edit = new Button("edit");
        partie = new Button("start game");
        /*Button btn4 = new Button("Button");
        btn4.setSizeRequest(80, 40);*/

        fix.put(edit, 290, 33);
        fix.put(partie, 191, 370);
        //fix.put(btn4, 100, 80);
       // System.out.println(close.getAllocation().toString());
        
        partie.connect(new Button.Clicked()
        {
            public void onClicked(Button source)
            {
            	mode = Const.PLAY_MODE;
            	fpath.addCreatedGame();
            	//fpath.updat
            	//destroy();
            	source.getParent().getParent().hide();
            	Gtk.mainQuit();
            	//caller.startGame(nb,fpath.getText(),newGame);
            	//showAll();
            }
        });
        
        edit.connect(new Button.Clicked()
        {
            public void onClicked(Button source)
            {
            	mode = Const.EDIT_MODE;
            	//hide();
            	source.getParent().getParent().hide();
            	//destroy();
            	Gtk.mainQuit();
            	//new CharacterEditor();
            	//initImages();
            	//source.getParent().getParent().showAll();
            }
        });
	}
	
	public void initLabels()
	{
		Label shadows = new Label("render shadows :");
		Label avatar = new Label("Avatar :");
		Label game = new Label("game name :");
		
		fix.put(shadows, 10, 205);
		fix.put(avatar, 50, 38);
		fix.put(game, 10, 245);
	}
	
	public void initEntries()
	{
		/*GameFile game = new GameFile(Const.configPath);
		String s[] = game.al.get(0);
		Entry entry = new Entry(s[2]);*/
		ConfigEntry entry = new ConfigEntry(0);
		entry.setLabelFromLinePart(2);
		entry.connect(new Entry.Changed()
		{
			public void onChanged(Entry entry) 
			{
				String message = entry.getText();
				if(message.equals("true") || message.equals("false"))
				{
					((ConfigEntry) entry).modifyLine(2,message);
					((ConfigEntry) entry).setError("");
					partie.setSensitive(true);
				}
				else
				{
					((ConfigEntry) entry).setError("wrong parameter");
					partie.setSensitive(false);
				}
			}
		});
		fpath = new FilePathEntry();
		//System.out.println("fpath = " + fpath.getText());
		fpath.connect(new Entry.Changed()
		{
			public void onChanged(Entry entry) 
			{
				String message = entry.getText();
				System.out.println("fpath = " + fpath.getText());
				if(message.length() > 0)
				{
					if(!((FilePathEntry) entry).gameExists(message))
					{
						((FilePathEntry) entry).setWarning("a new game will start");
						newGame = true;
					}
					else
					{
						((FilePathEntry) entry).setWarning("a game will continue");
						newGame = false;
					}
					partie.setSensitive(true);
				}
				else
				{
					((FilePathEntry) entry).setError("empty");
					partie.setSensitive(false);
				}
			}
		});
		
		//entry.setSizeRequest(10, 20);
		fix.put(entry, 180, 200);
		entry.putLabel(fix, 360, 205);
		
		fix.put(fpath, 180, 240);
		fpath.putLabel(fix, 355, 245);
	}
	
	public void update()
	{
		newGame = fpath.update(partie);
	}
	
	public String realPath(String path)
	{
		return System.getProperty("user.dir") + File.separator + "textures" + File.separator + path;
	}
	
	public void initImages()
	{
		Image img0 = new Image(realPath("player0.png"));
		Image img1 = new Image(realPath("player1.png"));
		Image img2 = new Image(realPath("player2.png"));
		fix.put(img0, 200, 30);
		fix.put(img1, 220, 30);
		fix.put(img2, 240, 30);
	}
}
