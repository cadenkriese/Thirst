package com.gamerking195.dev.thirst;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.gamerzking.core.updater.Updater;
import net.cubespace.Yamler.Config.InvalidConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gamerking195.dev.thirst.commands.ThirstCommand;
import com.gamerking195.dev.thirst.listeners.PlayerCommandPreProcessListener;
import com.gamerking195.dev.thirst.listeners.PlayerDeathListener;
import com.gamerking195.dev.thirst.listeners.PlayerGamemodeChangeListener;
import com.gamerking195.dev.thirst.listeners.PlayerItemConsumeListener;
import com.gamerking195.dev.thirst.listeners.PlayerJoinLeaveListener;
import com.gamerking195.dev.thirst.placeholderapi.Placeholders;

public class Main 
extends JavaPlugin
{
	public Main() {}
	private static Main instance;
	public static Main getInstance() {	 return instance;	}

	private Logger log;
	private PluginDescriptionFile pdf;
	private YAMLConfig config;

	@Override
	public void onEnable()
	{
		instance = this;
		pdf = getDescription();
		log = getLogger();

		new BukkitRunnable()
		{
			public void run()
			{
				loadFiles();
			}
		}.runTaskLater(this, 40L);

		log.log(Level.INFO, "V"+pdf.getVersion()+" enabled!");
		log.log(Level.INFO, pdf.getName()+" developed by "+pdf.getAuthors());
	}

	@Override
	public void onDisable()
	{	
		try 
		{
			config.reload();
			config.save();
		} 
		catch(InvalidConfigurationException ex) 
		{
			log.log(Level.SEVERE, "=============================");
			log.log(Level.SEVERE, "Error while saving the config for "+pdf.getName()+" V"+pdf.getVersion());
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

	private void loadFiles()
	{
		//API
		try
		{
			config = new YAMLConfig(this);
			config.init();
		}
		catch(InvalidConfigurationException ex)
		{
			log.log(Level.SEVERE, "=============================");
			log.log(Level.SEVERE, "Error while initializing the config for "+pdf.getName()+" V"+pdf.getVersion());
			log.log(Level.SEVERE, "WARNING, Thirst will not enable until the error is fixed!");
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
			this.setEnabled(false);
			return;
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
		{
			if (new Placeholders(this).hook())
			{
				log.log(Level.INFO, "Found PlaceholderAPI, sucsesfully hooked!");
			}
			else
			{
				log.log(Level.WARNING, "Found PlaceholderAPI, but the placeholders did not hook!");
			}
		}
		
		ActionBarAPI.init();
		
		new Updater(this);
		
		//CLASSES

		Thirst.getThirst().init();
		ThirstEffectsHandler.getThirstEffects().startEffects();

		//COMMANDS
		this.getCommand("thirst").setExecutor(new ThirstCommand());
		
		//EVENTS
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new PlayerJoinLeaveListener(), this);
		pm.registerEvents(new PlayerItemConsumeListener(), this);
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new PlayerGamemodeChangeListener(), this);
		pm.registerEvents(new PlayerCommandPreProcessListener(), this);
		
		//PLACEHOLDERS
	}

	public YAMLConfig getYAMLConfig() 
	{
		return config;
	}
	
	public void disable()
	{
		this.setEnabled(false);
	}
}
