package avalone.negend;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl.AvaloneGLAPI;

public class Ore
{
	public ArrayList<String> voyelles = new ArrayList<String>();
	public ArrayList<String> consonnes = new ArrayList<String>();
	public ArrayList<String> banned = new ArrayList<String>();
	
	public String name;
	public int id;
	public int miningLevel;
	public int hardness;
	public int globalRarity;//influe les deux valeurs suivantes
	public int maxVeinSize;
	public int VeinRariry;
	public float[] color;
	public Random rand;
	private AvaloneGLAPI glapi;
	
	public Ore(int id,AvaloneGLAPI glapi)
	{
		this.id = id;
		this.glapi = glapi;
		initLists();
		rand = new Random();
		createName();
		createColor();
		createStats();
	}
	
	public Ore(int id)
	{
		this.id = id;
	}
	
	public void initLists()
	{
		voyelles.add("a");voyelles.add("e");voyelles.add("i");voyelles.add("o");voyelles.add("u");voyelles.add("y");
		
		consonnes.add("b");consonnes.add("c");consonnes.add("d");consonnes.add("f");consonnes.add("g");consonnes.add("h");
		consonnes.add("j");consonnes.add("k");consonnes.add("l");consonnes.add("m");consonnes.add("n");consonnes.add("p");
		consonnes.add("q");consonnes.add("r");consonnes.add("s");consonnes.add("t");consonnes.add("v");consonnes.add("w");
		consonnes.add("x");consonnes.add("z");
		
		banned.add("qc");banned.add("xw");banned.add("pw");banned.add("xx");banned.add("vw");banned.add("fv");
		banned.add("vh");banned.add("fh");banned.add("bg");banned.add("qg");
	}
	
	public void createName()
	{
		createPrefixe();
		//System.out.println("prefixe is: "+ name);
		createAtom();
		//System.out.println("prefixe + atom are: "+ name);
		createSuffixe();
		//System.out.println("final word is: "+ name);
		checkIfBanned();
	}
	
	public void createColor()
	{
		color = glapi.getFilter(rand.nextInt(256),rand.nextInt(256),rand.nextInt(2));
	}
	
	public void createStats()
	{
		
	}
	
	public void createPrefixe()
	{
		int first = rand.nextInt(2);
		if (first == 0)
		{
			name = chooseVoyelle() + chooseConsonne() + chooseVoyelle();
		}
		else
		{
			name = chooseConsonne() + chooseVoyelle() + chooseConsonne();
		}
    	
	}
	
	public void createAtom()
	{
		int nbSyllabes = rand.nextInt(4);
		boolean dou = dedouble();
		if(dou) //ca ne peut pas etre des voyelles
		{
			name = name + name.charAt(name.length()-1);
			//System.out.println("consonne dedoublee, le prefixe est maintenant: "+ s);
			createSyllabes(nbSyllabes);
		}
		else
		{
			char c = name.charAt(name.length()-1);
			if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y')
			{
				name = name + chooseConsonne();
				//System.out.println("consonne ajoutee, le prefixe est maintenant: "+ s);
				createSyllabes(nbSyllabes);
			}
			else
			{
				createSyllabes(nbSyllabes);
			}
		}
		//System.out.println("string retourne: " + s);
	}
	
	public void createSyllabes(int nbSyllabes)
	{
		for(int i = 0;i < nbSyllabes;i++)
		{
			createSyllabe();
		}
	}
	
	public String chooseVoyelle()
	{
		int i = rand.nextInt(voyelles.size());
		return voyelles.get(i);
	}
	
	public String chooseConsonne()
	{
		int i = rand.nextInt(consonnes.size());
		return consonnes.get(i);
	}
	
	public void createSyllabe()//commence par une voyelle et finit par une consonne
	{
		name = name + chooseVoyelle();
		int i = rand.nextInt(5);
		if(i == 0)
		{
			name = name + chooseVoyelle();
		}
		name = name + chooseConsonne();
		int j = rand.nextInt(3);
		if(j == 0)
		{
			name = name + chooseConsonne();
		}
	}
	
	public boolean dedouble()
	{
		char c = name.charAt(name.length()-1);
		if(c == 'h' || c == 'j' || c == 'q' || c == 'v' || c == 'w' || c == 'x' || c == 'b' || c == 'd' || c == 'g')
		{
			return false;
		}
		else if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y')
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void createSuffixe()
	{
		int last = rand.nextInt(3);
		if(last == 0)
		{
			name = name + "ium";
		}
		else if(last == 1)
		{
			name = name + "ite";
		}
		else
		{
			name = name + "oïne";
		}
	}
	
	public void checkIfBanned()
	{
		for(int i = 0;i < banned.size();i++)
		{
			if(name.contains(banned.get(i)))
			{
				//System.out.println("found: " + banned.get(i));
				System.out.println("detected banned letters");
				replaceLetters(banned.get(i));
			}
		}
	}
	
	public void replaceLetters(String s)
	{
		int i = findPos(s);
		System.out.println("pos detected to be " + i);
		if(i >= 0)
		{
			String newS = "";
			for(int j = 0; j < i;j++)
			{
				newS = newS + name.charAt(j);
			}
			for(int j = i; j < i + s.length();j++)
			{
				int z = rand.nextInt(2);
				if(z == 0)
				{
					newS = newS + chooseConsonne();
				}
				else
				{
					newS = newS + chooseVoyelle();
				}
			}
			for(int j = i + s.length(); j < name.length();j++)
			{
				newS = newS + name.charAt(j);
			}
			System.out.println("new string = " + newS);
			name = newS;
		}
	}
	
	public int findPos(String s)
	{
		for(int i = 0;i < name.length();i++)
		{
			int stringOffset = 0;
			while(name.charAt(i+stringOffset) == s.charAt(stringOffset))
			{
				if(stringOffset == s.length() - 1)
				{
					return i;
				}
				else
				{
					stringOffset++;
				}
			}
		}
		return -1;
	}
}
