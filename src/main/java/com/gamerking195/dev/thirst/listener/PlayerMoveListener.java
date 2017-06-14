package com.gamerking195.dev.thirst.listener;

import java.util.HashMap;
import java.util.UUID;

import com.gamerking195.dev.thirst.Thirst;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gamerking195.dev.thirst.ThirstManager;

public class PlayerMoveListener
		implements Listener
{
	public static void init()
	{
		runnable.runTaskTimer(Thirst.getInstance(), (long) Thirst.getInstance().getYAMLConfig().blockDrinkDelay*20, (long) Thirst.getInstance().getYAMLConfig().blockDrinkDelay*20);
	}

	public static void reload() {
	    Bukkit.getScheduler().cancelTask(runnable.getTaskId());

	    runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (String uuid : drinkingMap.keySet())
                {
                    UUID pid = UUID.fromString(uuid);
                    Player player = Bukkit.getPlayer(pid);

                    if (player != null) {
                        ThirstManager.getThirst().setThirst(player, ThirstManager.getThirst().getPlayerThirst(player)+1);
                    }
                }
            }
        };

	    init();
    }

	private static HashMap<String, Player> drinkingMap = new HashMap<>();

	private static BukkitRunnable runnable = new BukkitRunnable()
	{
		@Override
		public void run()
		{
			for (String uuid : drinkingMap.keySet())
			{
				UUID pid = UUID.fromString(uuid);
				Player player = Bukkit.getPlayer(pid);

				if (player != null) {
					ThirstManager.getThirst().setThirst(player, ThirstManager.getThirst().getPlayerThirst(player)+1);
				}
			}
		}
	};

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		if (player.getEyeLocation().getBlock().getType() == Material.WATER || player.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER)
		{
			if (Thirst.getInstance().getYAMLConfig().drinkBlockWater)
			{
				if (!drinkingMap.containsKey(player.getUniqueId().toString()))
				{
					drinkingMap.put(player.getUniqueId().toString(), player);
				}
			}
		}
		else
		{
			if (drinkingMap.containsKey(player.getUniqueId().toString()))
			{
				drinkingMap.remove(player.getUniqueId().toString());
			}
		}
	}
}
