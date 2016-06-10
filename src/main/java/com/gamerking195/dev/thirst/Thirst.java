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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

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

		ThirstEffectsHandler.getThirstEffects().startEffects();

		thirstRemover();
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

		if (percent <= 5) emphasis = "&4!&c!&4!";

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
		p.setScoreboard(manager.getNewScoreboard());
		
		thirstConfig.set(p.getUniqueId().toString(), thirstCache.get(p));

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

		ThirstEffectsHandler.getThirstEffects().removePlayer(p);
		thirstCache.remove(p);
	}

	public void setThirst(Player p, int thirst)
	{
		if (thirst < 0) thirst = 0;
		if (thirst > 100) thirst = 100;

		thirstCache.put(p, thirst);

		if (thirst <= 0) ThirstEffectsHandler.getThirstEffects().setEffect(p, 5);
		else if (thirst <= 5) ThirstEffectsHandler.getThirstEffects().setEffect(p, 4);
		else if (thirst <= 10) ThirstEffectsHandler.getThirstEffects().setEffect(p, 3);
		else if (thirst <= 20) ThirstEffectsHandler.getThirstEffects().setEffect(p, 2);
		else if (thirst <= 30) ThirstEffectsHandler.getThirstEffects().setEffect(p, 1);
		else ThirstEffectsHandler.getThirstEffects().setEffect(p, 0);

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

	public void thirstRemover()
	{
		new BukkitRunnable()
		{
			public void run()
			{
				for (Player p : Bukkit.getOnlinePlayers())
				{
					if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
					if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
					if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;

					setThirst(p, getPlayerThirst(p)-Main.getInstance().getYAMLConfig().RemoveThirst);
				}
			}
		}.runTaskTimer(Main.getInstance(), Main.getInstance().getYAMLConfig().ThirstDelay*20, Main.getInstance().getYAMLConfig().ThirstDelay*20);
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
		else if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("action"))
		{
			ActionBarAPI.sendActionBar(p, getThirstString(p), Main.getInstance().getYAMLConfig().ThirstDelay*20+20);
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
