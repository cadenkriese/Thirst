package com.gamerking195.dev.thirst.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;

public class PlayerMoveListener
		implements Listener
{
	public static void init()
	{
		runnable.runTaskTimer(Main.getInstance(), 20L, 20L);
	}

	private static HashMap<String, Player> drinkingMap = new HashMap<String, Player>();

	private static BukkitRunnable runnable = new BukkitRunnable()
	{
		@Override
		public void run()
		{
			for (String uuid : drinkingMap.keySet())
			{
				UUID pid = UUID.fromString(uuid);
				Player p = Bukkit.getPlayer(pid);

				if (p != null)
				{
					Thirst.getThirst().setThirst(p, Thirst.getThirst().getPlayerThirst(p)+2);
				}
			}
		}
	};

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();

		if (p.getEyeLocation().getBlock().getType() == Material.WATER || p.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER)
		{
			if (Main.getInstance().getYAMLConfig().drinkBlockWater)
			{
				if (!drinkingMap.containsKey(p.getUniqueId().toString()))
				{
					drinkingMap.put(p.getUniqueId().toString(), p);
				}
			}
		}
		else
		{
			if (drinkingMap.containsKey(p.getUniqueId().toString()))
			{
				drinkingMap.remove(p.getUniqueId().toString());
			}
		}
	}
}
