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
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
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

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class Thirst
{		
	HashMap<Player, Integer> thirstCache = new HashMap<Player, Integer>();

	//How fast players thirst gets removed in Milliseconds.
	public HashMap<String, ThirstData> thirstRemovalSpeed = new HashMap<String, ThirstData>();

	private Thirst() {}
	private static Thirst instance = new Thirst();
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

	public void removeThirst(Player p)
	{
		setThirst(p, getPlayerThirst(p)-Main.getInstance().getYAMLConfig().RemoveThirst);

		long speed = calculateSpeed(p);
		ThirstData data = getThirstData(p);
		data.setSpeed(speed);
		data.setTime((long) (System.currentTimeMillis()+speed));
		setThirstData(p, data);

		displayThirst(p);
	}

	public long calculateSpeed(Player p)
	{
		long speed = (long) (Main.getInstance().getYAMLConfig().ThirstDelay*1000);

		//SPRINT
		if(p.isSprinting())
		{
			if (Main.getInstance().getYAMLConfig().Sprint != -1 && Main.getInstance().getYAMLConfig().Sprint != -0)
			{
				speed -= Main.getInstance().getYAMLConfig().Sprint*1000;
			}
		}

		//BIOME
		Biome b = p.getWorld().getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
		for(String s : Main.getInstance().getYAMLConfig().Biomes)
		{
			String[] split = s.split("\\.");

			if (!Main.getInstance().isInt(split[1]))
			{
				Logger log = Main.getInstance().getLogger();
				PluginDescriptionFile pdf = Main.getInstance().getDescription();

				log.log(Level.SEVERE, "=============================");
				log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "Printing Error:");
				log.log(Level.SEVERE, "String '"+split[1]+"' is not a valid number!");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "END OF ERROR");
				log.log(Level.SEVERE, "=============================");
			}

			String biome = split[0];
			int timeRemoved = Integer.valueOf(split[1])*1000;

			if (Biome.valueOf(biome) == null)
			{
				Logger log = Main.getInstance().getLogger();
				PluginDescriptionFile pdf = Main.getInstance().getDescription();

				log.log(Level.SEVERE, "=============================");
				log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "Printing Error:");
				log.log(Level.SEVERE, "String '"+split[0]+"' is not a valid biome!");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "");
				log.log(Level.SEVERE, "END OF ERROR");
				log.log(Level.SEVERE, "=============================");
			}

			if (b.toString().equalsIgnoreCase(biome))
			{
				speed -= timeRemoved;
			}
		}

		//ARMOR
		for (String s : Main.getInstance().getYAMLConfig().Armor)
		{
			String[] split = s.split("\\.");

			if (split.length == 2)
			{
				if (!Main.getInstance().isInt(split[1]))
				{
					Logger log = Main.getInstance().getLogger();
					PluginDescriptionFile pdf = Main.getInstance().getDescription();

					log.log(Level.SEVERE, "=============================");
					log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "Printing Error:");
					log.log(Level.SEVERE, "String '"+split[1]+"' is not a valid number!");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "END OF ERROR");
					log.log(Level.SEVERE, "=============================");
				}
				
				String armorType = split[0];
				int timeRemoved = Integer.valueOf(split[1])*1000;

				if (p.getInventory().getBoots() != null && p.getInventory().getLeggings() != null && p.getInventory().getChestplate() != null && p.getInventory().getHelmet() != null)
				{
					if (armorType.equalsIgnoreCase("LEATHER"))
					{
						if (p.getInventory().getHelmet().getType() == Material.LEATHER_HELMET && p.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE && p.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS && p.getInventory().getBoots().getType() == Material.LEATHER_BOOTS)
						{
							speed -= timeRemoved;
						}
					}
					else if (armorType.equalsIgnoreCase("GOLD"))
					{
						if (p.getInventory().getHelmet().getType() == Material.GOLD_HELMET && p.getInventory().getChestplate().getType() == Material.GOLD_CHESTPLATE && p.getInventory().getLeggings().getType() == Material.GOLD_LEGGINGS && p.getInventory().getBoots().getType() == Material.GOLD_BOOTS)
						{
							speed -= timeRemoved;
						}
					}
					else if (armorType.equalsIgnoreCase("CHAINMAIL"))
					{
						if (p.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET && p.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE && p.getInventory().getLeggings().getType() == Material.CHAINMAIL_LEGGINGS && p.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS)
						{
							speed -= timeRemoved;
						}
					}
					else if (armorType.equalsIgnoreCase("IRON"))
					{
						if (p.getInventory().getHelmet().getType() == Material.IRON_HELMET && p.getInventory().getChestplate().getType() == Material.IRON_CHESTPLATE && p.getInventory().getLeggings().getType() == Material.IRON_LEGGINGS && p.getInventory().getBoots().getType() == Material.IRON_BOOTS)
						{
							speed -= timeRemoved;
						}
					}
					else if (armorType.equalsIgnoreCase("DIAMOND"))
					{
						if (p.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET && p.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE && p.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS && p.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS)
						{
							speed -= timeRemoved;
						}
					}
			}
				Material mat = Material.valueOf(armorType);
				if (mat != null)
				{
					if ((p.getInventory().getBoots() != null && p.getInventory().getBoots().getType() == mat) || (p.getInventory().getLeggings() != null && p.getInventory().getLeggings().getType() == mat)  || (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() == mat) || (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() == mat))
					{
						speed -= timeRemoved;
					}
				}
				else
				{
					Logger log = Main.getInstance().getLogger();
					PluginDescriptionFile pdf = Main.getInstance().getDescription();

					log.log(Level.SEVERE, "=============================");
					log.log(Level.SEVERE, "Error while reading the config for "+pdf.getName()+" V"+pdf.getVersion());
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "Printing Error:");
					log.log(Level.SEVERE, "String '"+armorType+"' is not a valid item!");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "");
					log.log(Level.SEVERE, "END OF ERROR");
					log.log(Level.SEVERE, "=============================");
				}
			}
			else
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
			}
		}

		//DAY
		if (p.getWorld().getTime() > 0 && p.getWorld().getTime() < 12300)
		{
			if (Main.getInstance().getYAMLConfig().DayMultiplier > 0)
			{
				speed -= Main.getInstance().getYAMLConfig().DayMultiplier*1000;
			}
			else
			{
				speed += Main.getInstance().getYAMLConfig().DayMultiplier*1000;
			}
		}
		//NIGHT
		else
		{
			if (Main.getInstance().getYAMLConfig().NightMultiplier > 0)
			{
				speed -= Main.getInstance().getYAMLConfig().NightMultiplier*1000;
			}
			else
			{
				speed += Main.getInstance().getYAMLConfig().NightMultiplier*1000;
			}
		}
		
		if (speed < 100)
		{
			speed = 100;
		}

		return speed;
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

		String configMessage = Main.getInstance().getYAMLConfig().ThirstMessage.replace("%player%", p.getName()).replace("%percent%", getThirstPercent(p, true)).replace("%thirstbar%", "&1"+getThirstBar(p).replace("%removespeed%", String.valueOf(getThirstData(p).getSpeed()/1000)));

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

		long removalSpeed = calculateSpeed(p);
		long removeTime = (long) (System.currentTimeMillis()+removalSpeed);
		ThirstData playerData = new ThirstData(p, removeTime, removalSpeed);
		setThirstData(p, playerData);

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
		
		thirstRemovalSpeed.remove(p.getName());

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
		int oldThirst = getPlayerThirst(p);

		if (p.isDead()) thirst = 100;

		if (thirst < 0) thirst = 0;
		if (thirst > 100) thirst = 100;

		thirstCache.put(p, thirst);

		if (Main.getInstance().getYAMLConfig().Enabled)
		{
			for (String s : Main.getInstance().getYAMLConfig().Effects)
			{
				String[] parts = s.split("\\.");

				if (parts.length != 4)
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

				if (percent >= thirst)
				{
					if (parts[1].equalsIgnoreCase("DAMAGE"))
					{
						return;
					}

					PotionEffectType type = PotionEffectType.getByName(parts[1].toUpperCase());

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

					PotionEffect effect = new PotionEffect(type, Integer.valueOf(parts[2])*20, Integer.valueOf(parts[3])-1);

					p.addPotionEffect(effect);
				}
				else if (percent > thirst)
				{
					PotionEffectType type = PotionEffectType.getByName(parts[1].toUpperCase());
					p.removePotionEffect(type);
				}
			}
		}

		if (thirst <= 35 && oldThirst != 0 & oldThirst != -1) p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ThirstLowMessage.replace("%player%", p.getName()).replace("%percent%", getThirstPercent(p, true))));

		displayThirst(p);
	}

	public void refreshScoreboard (Player p)
	{
		if (getThirstString(p).length() > 40)
		{
			Logger log = Main.getInstance().getLogger();
			PluginDescriptionFile pdf = Main.getInstance().getDescription();

			log.log(Level.SEVERE, "=============================");
			log.log(Level.SEVERE, "Error while displaying scoreboard for "+p.getName()+" in "+pdf.getName()+" V"+pdf.getVersion());
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "Printing Message:");
			log.log(Level.SEVERE, "The string "+getThirstString(p)+" is longer than 40 characters.");
			log.log(Level.SEVERE, "You must have a thirst message under 40 characters to use the SCOREBOARD displaytype.");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "NOTE: This message will be displayed every time Thirst trys to update someones thirst (A lot!)");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "");
			log.log(Level.SEVERE, "END OF ERROR");
			log.log(Level.SEVERE, "=============================");

			return;
		}

		Scoreboard board = manager.getNewScoreboard();
		Objective obj = board.registerNewObjective(p.getName().toUpperCase(), "dummy");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ScoreboardName.replace("%player%", p.getName())));
		obj.getScore(getThirstString(p)).setScore(-1);

		p.setScoreboard(board);
	}

	public int getPlayerThirst(Player p)
	{
		if (!thirstCache.containsKey(p))
		{
			if (thirstConfig.contains(p.getUniqueId().toString()))
			{
				return thirstConfig.getInt(p.getUniqueId().toString());
			}
		}
		else
		{
			return thirstCache.get(p);
		}
		return -1;
	}

	public ThirstData getThirstData(OfflinePlayer oP)
	{
		return thirstRemovalSpeed.get(oP.getUniqueId().toString());
	}

	public void setThirstData(Player p, ThirstData data)
	{	
		thirstRemovalSpeed.put(p.getUniqueId().toString(), data);
	}

	public void displayThirst(Player p)
	{
		if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
		if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
		if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

		if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("ACTION")) ActionBarAPI.sendActionBar(p, Thirst.getThirst().getThirstString(p));
		else if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("SCOREBOARD")) refreshScoreboard(p);
	}

	public FileConfiguration getThirstConfig()
	{
		return thirstConfig;
	}
}
