package com.gamerking195.dev.thirst.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstEffectsHandler;

import me.gamerzking.core.updater.UpdateType;
import me.gamerzking.core.updater.event.UpdateEvent;

public class UpdateListener
implements Listener
{
	@EventHandler
	public void onUpdate(UpdateEvent event)
	{
		if (event.getType() == UpdateType.SECOND)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;
				
				if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("ACTION")) ActionBarAPI.sendActionBar(p, Thirst.getThirst().getThirstString(p));
				else if (Main.getInstance().getYAMLConfig().DisplayType.equalsIgnoreCase("SCOREBOARD")) Thirst.getThirst().displayThirst(p);
			}
		}
		if (event.getType() == UpdateType.CONFIG)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) continue;
				if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) continue;
				if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) continue;

				Thirst.getThirst().setThirst(p, Thirst.getThirst().getPlayerThirst(p)-Main.getInstance().getYAMLConfig().RemoveThirst);
			}
		}
		else if (event.getType() == UpdateType.TIER1)
		{
			HashMap<Player, Integer> playerEffects = ThirstEffectsHandler.getThirstEffects().getPlayerEffects();
			for (Player p : playerEffects.keySet())
			{
				if (playerEffects.get(p) == 1)
				{
					if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
					if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
					if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
				}
			}
		}
		else if (event.getType() == UpdateType.TIER2)
		{
			HashMap<Player, Integer> playerEffects = ThirstEffectsHandler.getThirstEffects().getPlayerEffects();
			for (Player p : playerEffects.keySet())
			{
				if (playerEffects.get(p) == 1)
				{
					if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
					if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
					if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
				}
			}
		}
		else if (event.getType() == UpdateType.TIER3)
		{
			HashMap<Player, Integer> playerEffects = ThirstEffectsHandler.getThirstEffects().getPlayerEffects();
			for (Player p : playerEffects.keySet())
			{
				if (playerEffects.get(p) == 1)
				{
					if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
					if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
					if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 1));
				}
			}
		}
	}
}
