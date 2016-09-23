package com.gamerking195.dev.thirst.placeholderapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.gamerking195.dev.thirst.Thirst;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Placeholders
extends EZPlaceholderHook
{
	public Placeholders(Plugin plugin) 
	{
		super(plugin, "gkthirst");
	}

	@Override
	public String onPlaceholderRequest(Player p, String identifier) 
	{
		if (identifier.equals("thirstmessage"))
		{
			return Thirst.getThirst().getThirstString(p);
		}
		
		if (identifier.equals("thirstbar"))
		{
			return Thirst.getThirst().getThirstBar(p);
		}
		
		if (identifier.equals("thirstpercent"))
		{
			return Thirst.getThirst().getThirstPercent(p, true);
		}
		
		if (identifier.equals("thirstremoval"))
		{
			return String.format(String.valueOf(Thirst.getThirst().getThirstData(p).getSpeed()/1000), "%.2f");
		}
		
		return null;
	}
}
