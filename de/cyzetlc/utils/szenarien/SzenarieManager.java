package de.cyzetlc.utils.szenarien;

public class SzenarieManager 
{
	private static Szenarie currentSzenarie = Szenarie.Keine;
	
	public static Szenarie getCurrentSzenarie() 
	{
		return currentSzenarie;
	}
	
	public static void setCurrentSzenarie(Szenarie currentSzenarie) 
	{
		SzenarieManager.currentSzenarie = currentSzenarie;
	}
}
