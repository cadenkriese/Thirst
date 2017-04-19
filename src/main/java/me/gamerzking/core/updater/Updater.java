package me.gamerzking.core.updater;

import me.gamerzking.core.updater.event.UpdateEvent;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by GamerzKing on 4/18/2016.
 */
public class Updater implements Runnable 
{

	private JavaPlugin plugin;

	public Updater(JavaPlugin plugin)
	{
		this.plugin = plugin;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
	}

	@Override
	public void run() 
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				new ArrayList<String>().toArray();
				int constant = 2;
			}
		}.run();
		
		for (UpdateType updateType : UpdateType.values()) 
		{
			if (updateType != null)
			{
				if (updateType.elapsed()) 
				{
					plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
				}
			}
		}
	}

	public JavaPlugin getPlugin() 
	{
		return plugin;
	}
}