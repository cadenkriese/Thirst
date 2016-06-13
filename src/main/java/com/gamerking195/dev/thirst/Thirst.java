package com.gamerking195.dev.thirst;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Thirst
{		
	HashMap<Player, Integer> thirstCache = new HashMap<Player, Integer>();

	public Thirst() {}
	public static Thirst instance = new Thirst();
	public static Thirst getThirst() {	return instance;	}

	private File thirstFile = new File(Main.getInstance().getDataFolder(), "thirst_data.yml");
	private FileConfiguration thirstConfig = YamlConfiguration.loadConfiguration(thirstFile); 

	private ScoreboardManager manager;


	public void init()
	{
		manager = Bukkit.getScoreboardManager();

		thirstConfig.options().copyDefaults(true);

		if(!thirstFile.exists())
		{
			try
			{
				thirstFile.createNewFile();
				thirstConfig.save(thirstFile);
			}
			catch (IOException e)
			{ 
				e.printStackTrace();
			}
		}

		for (Player p : Bukkit.getServer().getOnlinePlayers())
		{
			playerJoin(p);
		}
	}

	public String getThirstString(OfflinePlayer p)
	{		
		int percent = -1;

		if (thirstCache.containsKey(p))
		{
			percent = thirstCache.get(p);
		}
		else if (thirstConfig.contains(p.getUniqueId().toString()))
		{
			percent = thirstConfig.getInt(p.getUniqueId().toString());
		}
		else
		{
			return "NULL";
		}

		String emphasis = "";

		if (percent <= 5 && !Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("SCOREBOARD")) emphasis = " &4!&c!&4! ";

		String configMessage = Main.getInstance().getYAMLConfig().ThirstMessage.replace("%player%", p.getName()).replace("%percent%", getThirstPercent(p, true)).replace("%thirstbar%", "&1"+getThirstBar(p));

		return ChatColor.translateAlternateColorCodes('&', emphasis+configMessage+emphasis);
	}

	public String getThirstBar(OfflinePlayer p)
	{
		int percent = -1;

		if (thirstCache.containsKey(p))
		{
			percent = thirstCache.get(p);
		}
		else if (thirstConfig.contains(p.getUniqueId().toString()))
		{
			percent = thirstConfig.getInt(p.getUniqueId().toString());
		}
		else
		{
			return "NULL";
		}

		String thirstBar = "::::::::::";

		int bars = (int) Math.round(percent/10.0);

		return thirstBar.substring(0, bars)+ChatColor.RED+thirstBar.substring(bars, thirstBar.length())+ChatColor.RESET;
	}

	public String getThirstPercent(OfflinePlayer p, boolean colored)
	{
		int percent = -1;

		if (thirstCache.containsKey(p))
		{
			percent = thirstCache.get(p);
		}
		else if (thirstConfig.contains(p.getUniqueId().toString()))
		{
			percent = thirstConfig.getInt(p.getUniqueId().toString());
		}
		else
		{
			return "NULL";
		}

		if (colored)
		{
			String percentColor = "&a";

			if (percent <= 20) percentColor = "&4";
			else if (percent <= 40) percentColor = "&c";
			else if (percent <= 60) percentColor = "&6";
			else if (percent <= 80) percentColor = "&e";

			return percentColor+percent+"%";
		}
		else
		{
			return percent+"%";
		}
	}

	public void playerJoin(Player p)
	{
		UUID pid = p.getUniqueId();
		if (!p.hasPlayedBefore() || !thirstConfig.getKeys(false).contains(pid.toString()))
		{
			thirstConfig.set(pid.toString(), 100);
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
				return;
			}
			thirstCache.put(p, 100);
		}
		else
		{
			setThirst(p, thirstConfig.getInt(p.getUniqueId().toString()));
		}
		
		displayThirst(p);
	}

	public void playerLeave(Player p)
	{
		if (p.isDead()) thirstCache.put(p, 100);
		
		p.setScoreboard(manager.getNewScoreboard());
		
		thirstConfig.set(p.getUniqueId().toString(), thirstCache.get(p));

		saveThirstFile();

		thirstCache.remove(p);
	}
	
	public void saveThirstFile()
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

	public void setThirst(Player p, int thirst)
	{
		if (p.isDead()) thirst = 100;
		
		if (thirst < 0) thirst = 0;
		if (thirst > 100) thirst = 100;

		thirstCache.put(p, thirst);

		if (Main.getInstance().getYAMLConfig().Enabled)
		{
			for (String s : Main.getInstance().getYAMLConfig().Effects)
			{
				String[] parts = s.split("\\.");
				
				if (parts.length != 2)
				{
					Logger log = Main.getInstance().getLogger();
					PluginDescriptionFile pdf = Main.getInstance().getDescription();
					
					log.log(Level.SEVERE, "=============================");
					log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "Printing Error:");
					log.log(Level.SEVERE, "String '"+s+"' is in an invalid format!");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "END OF ERROR");
					log.log(Level.SEVERE, "=============================");
					return;
				}
				
				int percent = Integer.valueOf(parts[0]);
				
				String[] potionParts = parts[1].split("_");
				
				if (percent >= thirst)
				{
					if (potionParts.length != 3)
					{
						Logger log = Main.getInstance().getLogger();
						PluginDescriptionFile pdf = Main.getInstance().getDescription();
						
						log.log(Level.SEVERE, "=============================");
						log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "Printing Error:");
						log.log(Level.SEVERE, "String '"+s+"' is in an invalid format!");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "END OF ERROR");
						log.log(Level.SEVERE, "=============================");
						return;
					}
					
					if (potionParts[0].equalsIgnoreCase("DAMAGE"))
					{
						return;
					}
					
					PotionEffectType type = PotionEffectType.getByName(potionParts[0].toUpperCase());
					
					if (type == null)
					{
						Logger log = Main.getInstance().getLogger();
						PluginDescriptionFile pdf = Main.getInstance().getDescription();
						
						log.log(Level.SEVERE, "=============================");
						log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "Printing Error:");
						log.log(Level.SEVERE, "String '"+s+"' is in an invalid format!");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "END OF ERROR");
						log.log(Level.SEVERE, "=============================");
						return;
					}
					
					PotionEffect effect = new PotionEffect(type, Integer.valueOf(potionParts[1])*20, Integer.valueOf(potionParts[2])-1);
					
					p.addPotionEffect(effect);
				}
				else if (percent > thirst)
				{
					PotionEffectType type = PotionEffectType.getByName(potionParts[0].toUpperCase());
					p.removePotionEffect(type);
				}
			}
		}

		if (thirst <= 35) p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ThirstLowMessage.replace("%player%", p.getName()).replace("%percent%", getThirstPercent(p, true))));
		
		displayThirst(p);
	}

	public void refreshScoreboard (Player p)
	{
		Scoreboard board = manager.getNewScoreboard();
		Objective obj = board.registerNewObjective(p.getName().toUpperCase(), "dummy");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ScoreboardName.replace("%player%", p.getName())));
		obj.getScore(getThirstString(p)).setScore(-1);

		p.setScoreboard(board);
	}

	public int getPlayerThirst(Player p)
	{
		return thirstCache.get(p);
	}

	public void displayThirst(Player p)
	{
		if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
		if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
		if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;
		
		if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("scoreboard"))
		{
			refreshScoreboard(p);
		}
		else
		{
			return;
		}
	}

	public FileConfiguration getThirstConfig()
	{
		return thirstConfig;
	}
}
