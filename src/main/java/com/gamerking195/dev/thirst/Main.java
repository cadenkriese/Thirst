package com.gamerking195.dev.thirst;

import java.util.logging.Level;
import java.util.logging.Logger;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import com.gamerking195.dev.thirst.util.UtilSQL;
import com.gamerking195.dev.thirst.util.UtilUpdater;
import me.gamerzking.core.updater.Updater;
import net.cubespace.Yamler.Config.InvalidConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gamerking195.dev.thirst.command.ThirstCommand;
import com.gamerking195.dev.thirst.config.DataConfig;
import com.gamerking195.dev.thirst.config.YAMLConfig;
import com.gamerking195.dev.thirst.listener.PlayerDeathListener;
import com.gamerking195.dev.thirst.listener.PlayerGamemodeChangeListener;
import com.gamerking195.dev.thirst.listener.PlayerItemConsumeListener;
import com.gamerking195.dev.thirst.listener.PlayerJoinLeaveListener;
import com.gamerking195.dev.thirst.listener.PlayerMoveListener;
import com.gamerking195.dev.thirst.listener.PlayerRespawnListener;
import com.gamerking195.dev.thirst.listener.UpdateListener;
import com.gamerking195.dev.thirst.placeholder.ClipPlaceholders;

/*
 * Thirst plugin by GamerKing195
 *
 * Found on Spigot @ https://www.spigotmc.org/resources/thirst.24610/
 *
 * Found on GitHub @ https://github.com/GamerKing195/Thirst
 *
 * This plugin is using the GNU General Public License which basically means
 * sure take the code but you must make whatever you make with this open source as well & register under the same license.
 */

public class Main
		extends JavaPlugin
{
	public Main() {}
	private static Main instance;
	public static Main getInstance() {	 return instance;	}

	private Logger log;
	private YAMLConfig yamlConf;

	private boolean worldGuardEnabled = false;

	@Override
	public void onEnable()
	{
		instance = this;
		PluginDescriptionFile pdf = getDescription();
		log = getLogger();

		new BukkitRunnable()
		{
			public void run()
			{
				loadFiles();
			}
		}.runTaskLater(this, 2L);

		log.log(Level.INFO, "V"+pdf.getVersion()+" enabled!");
		log.log(Level.INFO, pdf.getName()+" developed by "+pdf.getAuthors());
	}

	@Override
	public void onDisable()
	{
		if (yamlConf.enableSQL) {

			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD"))
					player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());

				UtilSQL.getInstance().runStatement("UPDATE TABLENAME SET thirst = "+Thirst.getThirst().getPlayerThirst(player)+" WHERE uuid = '"+player.getUniqueId().toString()+"'");
			}
		}
		else {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD"))
					p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());

				DataConfig.getConfig().writeThirstToFile(p.getUniqueId(), Thirst.getThirst().getPlayerThirst(p));
			}

			DataConfig.getConfig().saveFile();
		}
		try
		{
			yamlConf.reload();
			yamlConf.save();
		}
		catch(InvalidConfigurationException ex)
		{
			printError(ex, "Error while saving the config.yml file please check that you didn't use tabs and all formatting is correct.");
		}
	}

	private void loadFiles()
	{
		//CONFIGS

		try
		{
			yamlConf = new YAMLConfig(this);
			yamlConf.init();
		}
		catch(InvalidConfigurationException ex)
		{
			printError(ex, "Error while initializing the config.yml file please check that you didn't use tabs and all formatting is correct.");
		}

		if (yamlConf.enableSQL)
			UtilSQL.getInstance().init();
		else
			DataConfig.getConfig().init();

		//API

		if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			PlaceholderAPI.registerPlaceholder(this, "gkthirst_thirstmessage", event -> {
				if (event.isOnline()) {
					if (event.getPlayer() != null) {
						return Thirst.getThirst().getThirstString(event.getPlayer());
					}
				}
				return null;
			});
			PlaceholderAPI.registerPlaceholder(this, "gkthirst_thirstbar", event -> {
				if (event.isOnline()) {
					if (event.getPlayer() != null) {
						return Thirst.getThirst().getThirstBar(event.getPlayer());
					}
				}
				return null;
			});
			PlaceholderAPI.registerPlaceholder(this, "gkthirst_thirstpercent", event -> {
				if (event.isOnline()) {
					if (event.getPlayer() != null) {
						return Thirst.getThirst().getThirstPercent(event.getPlayer(), true);
					}
				}
				return null;
			});
			PlaceholderAPI.registerPlaceholder(this, "gkthirst_thirstremovalspeed", event -> {
				if (event.isOnline()) {
					if (event.getPlayer() != null) {
						return String.format(String.valueOf(Thirst.getThirst().getThirstData(event.getPlayer()).getSpeed() / 1000), "%.3f");
					}
				}
				return null;
			});
			PlaceholderAPI.registerPlaceholder(this, "gkthirst_thirstremovalamount", event -> {
				if (event.isOnline()) {
					if (event.getPlayer() != null) {
						return String.valueOf(getYAMLConfig().removeThirst);
					}
				}
				return null;
			});
		}

		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
		{
			if (new ClipPlaceholders(this).hook())
			{
				log.log(Level.INFO, "Found PlaceholderAPI, successfully hooked!");
			}
			else
			{
				printPluginError("Error occurred while adding placeholder support.", "The plugin failed to hook with Placeholder API, please report this bug.");
			}
		}

		new Updater(this);

		if (yamlConf.disabledRegions != null && yamlConf.disabledRegions.length > 0)
		{
			if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard"))
			{
				worldGuardEnabled = true;
			}
			else
			{
				printPluginError("Error occurred while parsing config.", "You are trying to use WorldGuard in the config but the plugin could not be found!");
			}
		}

		//CLASSES
		Thirst.getThirst().init();
		PlayerMoveListener.init();
		UtilUpdater.getInstance().init();

		//COMMANDS
		this.getCommand("thirst").setExecutor(new ThirstCommand());

		//EVENTS
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new PlayerJoinLeaveListener(), instance);
		pm.registerEvents(new PlayerItemConsumeListener(), instance);
		pm.registerEvents(new PlayerDeathListener(), instance);
		pm.registerEvents(new PlayerGamemodeChangeListener(), instance);
		pm.registerEvents(new UpdateListener(), instance);
		pm.registerEvents(new PlayerRespawnListener(), instance);
		pm.registerEvents(new PlayerMoveListener(), instance);

		//VALIDATION
		if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("BOSSBAR") && !Bukkit.getBukkitVersion().contains("1.9") && !Bukkit.getBukkitVersion().contains("1.10") && !Bukkit.getBukkitVersion().contains("1.11") && !Bukkit.getBukkitVersion().contains("1.12"))
		{
			try
			{
				Main.getInstance().getLogger().log(Level.SEVERE, "[Thirst V"+Main.getInstance().getDescription().getVersion()+"] Your Spigot version is not compatible with the Bossbar display type, please use version 1.9 or higher.");
				Main.getInstance().getLogger().log(Level.SEVERE, "[Thirst V"+Main.getInstance().getDescription().getVersion()+"] Changing to display type ACTION...");

				Main.getInstance().getYAMLConfig().displayType = "ACTION";
				Main.getInstance().getYAMLConfig().save();
			}
			catch (InvalidConfigurationException ex)
			{
				printError(ex, "Error while validating the config.yml file please check that you used all spaces and all formatting is correct.");
			}
		}
	}

	public YAMLConfig getYAMLConfig()
	{
		return yamlConf;
	}

	boolean isWorldGuardEnabled()
	{
		return worldGuardEnabled;
	}

	public void printError(Exception ex, String extraInfo) {
		log.severe("A severe error has occured with the Thirst plugin.");
		log.severe("If you cannot figure out this error on your own (e.g. a config error) please copy and paste everything from here to END ERROR and post it at https://github.com/GamerKing195/Thirst/issues.");
		log.severe("");
		log.severe("============== BEGIN ERROR ==============");
		log.severe("PLUGIN VERSION: Thirst V" + getDescription().getVersion());
		log.severe("");
		log.severe("PLUGIN MESSAGE: "+extraInfo);
		log.severe("");
		log.severe("MESSAGE: " + ex.getMessage());
		log.severe("");
		log.severe("STACKTRACE: ");
		ex.printStackTrace();
		log.severe("");
		log.severe("============== END ERROR ==============");
	}

	public void printPluginError(String header, String message) {
		log.severe("============== BEGIN ERROR ==============");
		log.severe(header);
		log.severe("");
		log.severe("PLUGIN VERSION: Thirst V" + getDescription().getVersion());
		log.severe("");
		log.severe("PLUGIN MESSAGE: "+message);
		log.severe("");
		log.severe("============== END ERROR ==============");
	}
}
