package com.gamerking195.dev.thirst.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import com.gamerking195.dev.thirst.Main;

public class DataConfig
{
	private DataConfig() {}
	private static DataConfig instance = new DataConfig();
	public static DataConfig getConfig() {	return instance;	 }

	private File thirstFile = new File(Main.getInstance().getDataFolder(), "thirst_data.yml");
	private FileConfiguration thirstConfig = YamlConfiguration.loadConfiguration(thirstFile); 

	public void init()
	{
		thirstConfig.options().copyDefaults(true);

		if(!thirstFile.exists())
		{
			try
			{
				thirstFile.createNewFile();
				thirstConfig.save(thirstFile);
			}
			catch (IOException ex)
			{ 			
				Main.getInstance().printError(ex, "Error occurred while creating thirst_data.yml");
			}
		}
	}

	public void saveFile()
	{
		try 
		{
			thirstConfig.save(thirstFile);
		} 
		catch (IOException ex) 
		{
			Main.getInstance().printError(ex, "Error occurred while creating thirst_data.yml");
		}
	}

	public int getThirstFromFile(UUID pid)
	{
		String uniqueId = pid.toString();

		if (thirstConfig.contains(uniqueId))
		{
			return thirstConfig.getInt(uniqueId);
		}

		return -1;
	}

	public void writeThirstToFile(UUID pid, int amount)
	{	
		thirstConfig.set(pid.toString(), amount);
	}

	public boolean fileContains(UUID pid)
	{
		return thirstConfig.contains(pid.toString());
	}

	public void setFile(HashMap<String, Integer> map) {
	    //clear the config
	    for (String string : thirstConfig.getKeys(false))
	        thirstConfig.set(string, null);

        //add new valued
		for (String key : map.keySet())
            thirstConfig.set(key, map.get(key));

		saveFile();
	}

	public HashMap<String, Integer> getFile() {
	    HashMap<String, Integer> map = new HashMap<>();

        for (String string : thirstConfig.getKeys(false))
            map.put(string, thirstConfig.getInt(string));

        return map;
    }
}
