package avalone.negend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameFile 
{
	ArrayList<String[]> al;
	private String path;
	
	public GameFile(String path)
	{
		al = new ArrayList<String[]>();
		this.path = path;
		read();
	}
	
	public void read()
	{
		int i = 0;
		File f = checkExists(path,"file");
        if(f.getName().equals("error"))
        {
        	System.out.println("error on reading file");
        }
        
        BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(f));
			String readLine;
			while((readLine = in.readLine()) != null)
			{
				readLine = check(readLine);
				if(readLine.length() > 0)
				{
					if(!readLine.startsWith("//"))
					{
						al.add(readLine.split("\\s+"));
					}
				}
			}
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void write()
	{
		File f = checkExists(path,"file");
        if(f.getName().equals("error"))
        {
        	System.out.println("error on reading file");
        }
        
        BufferedWriter out;
		try 
		{
			out = new BufferedWriter(new FileWriter(f));
			for(int i = 0;i < al.size();i++)
			{
				String s[] =  al.get(i);
				String news = "";
				for(int j = 0;j < s.length;j++)
				{
					news = news + s[j] +" ";
				}
				out.write(news + "\n");
			}
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addNewLine(String message)
	{
		String[] newMessage = message.split("\\s+");
		al.add(newMessage);
		write();
	}
	
	public String check(String readLine)
	{
		if(readLine.startsWith(" "))
		{
			return check(readLine.substring(1));
		}
		return readLine;
	}
	
	public static File createFile(String path)
    {
    	File f = new File(path);
        boolean exists = f.exists();
        if(exists)
        {
            f.delete();
        }
        try
        {
        	f.createNewFile();
        }
        catch(IOException ie)
        {
        	System.out.println("couldn't create file");
        }
        return f;
    }
	
	public File checkExists(String path,String filetype)
    {
    	File f = new File(path);
    	if(!f.exists())
    	{
    		System.out.println(filetype + " " + path + " does not exist.");
    		return new File("error");
    	}
    	return f;
    }
	
	public String randomNameNewFileGenerator() throws IOException
    {
    	Random rand = new Random();
    	int taille = rand.nextInt(10);
    	ArrayList<String> al = new ArrayList<String>();
    	al.add("a");al.add("b");al.add("c");al.add("d");al.add("e");al.add("f");al.add("g");al.add("h");al.add("i");
    	al.add("j");al.add("k");al.add("l");al.add("m");al.add("n");al.add("o");al.add("p");al.add("q");al.add("r");
    	al.add("s");al.add("t");al.add("u");al.add("v");al.add("w");al.add("x");al.add("y");al.add("z");
    	String name = ".";
    	for(int i = 0;i < taille;i++)
    	{
    		int a = rand.nextInt(26);
    		name = name + al.get(a);
    		name = name + ".av";
    	}
    	return name;
    }
}
