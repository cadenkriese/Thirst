package com.gamerking195.dev.thirst.listener;

import com.gamerking195.dev.thirst.ThirstManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener 
implements Listener
{
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();

		if (!ThirstManager.getThirst().validatePlayer(player)) return;
		
		ThirstManager.getThirst().setThirst(player, 100);
	}
}
