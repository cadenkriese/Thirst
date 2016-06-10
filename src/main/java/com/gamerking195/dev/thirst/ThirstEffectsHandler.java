package com.gamerking195.dev.thirst;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ThirstEffectsHandler
{
	public ThirstEffectsHandler() {}
	public static ThirstEffectsHandler instance = new ThirstEffectsHandler();
	public static ThirstEffectsHandler getThirstEffects() {	return instance;	}

	private HashMap<Player, Integer> playerEffects = new HashMap<Player, Integer>();

	public void setEffect(Player p , int tier)
	{
		if (tier > 5) tier = 5;
		if (tier < 0) tier = 1;

		if (tier == 0)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				p.removePotionEffect(effect.getType());
			}
			return;
		}
		else if (tier == 1)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));
		}
		else if (tier == 2)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 0));
		}
		else if (tier == 3)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 1));
		}
		else if (tier == 4)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 3));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 2));
		}
		else if (tier == 5)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 99999, 2));
		}

		playerEffects.put(p, tier);
	}

	public void removePlayer(Player p)
	{
		playerEffects.remove(p); 
	}

	public void startEffects()
	{
		//Tier 1
		new BukkitRunnable()
		{
			public void run()
			{
				for (Player p : playerEffects.keySet())
				{
					if (playerEffects.get(p) == 1)
					{
						if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
						if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
						if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

						p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5, 1));
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 300L);

		//Tier 2
		new BukkitRunnable()
		{
			public void run()
			{
				for (Player p : playerEffects.keySet())
				{
					if (playerEffects.get(p) == 2)
					{
						if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
						if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
						if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

						p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 1));
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 400L);

		//Tier 3
		new BukkitRunnable()
		{
			public void run()
			{
				for (Player p : playerEffects.keySet())
				{
					if (playerEffects.get(p) == 3)
					{
						if (p.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().IgnoreCreative) return;
						if (p.isOp() && Main.getInstance().getYAMLConfig().IgnoreOP) return;
						if (p.hasPermission("thirst.ignore") || p.hasPermission("thirst.*")) return;

						p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 1));
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 600L);
	}
}
