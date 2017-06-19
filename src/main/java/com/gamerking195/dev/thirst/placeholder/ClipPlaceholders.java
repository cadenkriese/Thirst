package com.gamerking195.dev.thirst.placeholder;

import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class ClipPlaceholders
extends EZPlaceholderHook
{
	public ClipPlaceholders(Plugin plugin)
	{
		super(plugin, "gkthirst");
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier)
	{
		switch (identifier) {
			case "thirstmessage":
				return ThirstManager.getThirst().getThirstString(player);
			case "thirstbar":
				return ThirstManager.getThirst().getThirstBar(player);
			case "thirstpercent":
				return ThirstManager.getThirst().getThirstPercent(player);
			case "thirstremovalspeed":
				return String.format(String.valueOf(ThirstManager.getThirst().getThirstData(player).getSpeed() / 1000), "%.3f");
			case "thirstremovalamount":
				return String.valueOf(Thirst.getInstance().getYAMLConfig().removeThirst);
		}
		return null;
	}
}
