package com.gamerking195.dev.thirst.configs;

import java.io.File;
import java.io.IOException;
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
	public static DataConfig  getConfig() {	return instance;	 }

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
				Logger log = Main.getInstance().getLogger();
				PluginDescriptionFile pdf = Main.getInstance().getDescription();

				log.log(Level.SEVERE, "=============================");
				log.log(Level.SEVERE, "Error while saving Thirst.yml for "+pdf.getName()+" V"+pdf.getVersion());
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "Printing StackTrace:");
				ex.printStackTrace();
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "Printing Message:");
				log.log(Level.SEVERE, ex.getMessage());
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "END OF ERROR");
				log.log(Level.SEVERE, "=============================");
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
			Logger log = Main.getInstance().getLogger();
			PluginDescriptionFile pdf = Main.getInstance().getDescription();

			log.log(Level.SEVERE, "=============================");
			log.log(Level.SEVERE, "Error while saving Thirst.yml for "+pdf.getName()+" V"+pdf.getVersion());
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "Printing StackTrace:");
			ex.printStackTrace();
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "Printing Message:");
			log.log(Level.SEVERE, ex.getMessage());
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "END OF ERROR");
			log.log(Level.SEVERE, "=============================");
		}
	}

	public int getThirstFromFIle(UUID pid)
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
}
