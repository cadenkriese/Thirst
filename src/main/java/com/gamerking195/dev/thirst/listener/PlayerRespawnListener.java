package com.gamerking195.dev.thirst.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import com.gamerking195.dev.thirst.Thirst;

public class PlayerRespawnListener 
implements Listener
{
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		final Player p = event.getPlayer();

		if (!Thirst.getThirst().validatePlayer(p)) return;
		
		Thirst.getThirst().setThirst(p, 100);
	}
}
