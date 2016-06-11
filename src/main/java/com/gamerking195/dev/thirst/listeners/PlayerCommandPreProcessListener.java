package com.gamerking195.dev.thirst.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstEffectsHandler;

public class PlayerCommandPreProcessListener
implements Listener
{
	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().equalsIgnoreCase("/reload") || event.getMessage().equalsIgnoreCase("/rl") || event.getMessage().equalsIgnoreCase("/bukkit:reload") || event.getMessage().equalsIgnoreCase("/bukkit:rl"))
		{
			for (Player p : Bukkit.getServer().getOnlinePlayers())
			{
				p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
				
				Thirst.getThirst().getThirstConfig().set(p.getUniqueId().toString(), Thirst.getThirst().getPlayerThirst(p));

				Thirst.getThirst().saveThirstFile();

				ThirstEffectsHandler.getThirstEffects().removePlayer(p);
			}
		}
	}
}
