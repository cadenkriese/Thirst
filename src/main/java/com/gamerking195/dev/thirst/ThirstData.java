package com.gamerking195.dev.thirst;

import org.bukkit.entity.Player;

public class ThirstData 
{
	private Player p;
	private long thirstTime;
	private long removalSpeed;
	
	public ThirstData(Player p, long time, long speed)
	{
		this.p = p;
		thirstTime = time;
		removalSpeed = speed;
	}
	
	public Player getPlayer()
	{
		return p;
	}
	
	public long getTime()
	{
		return thirstTime;
	}
	
	public void setTime(long l)
	{
		thirstTime = l;
	}
	
	public long getSpeed()
	{
		return removalSpeed;
	}
	
	public void setSpeed(long speed)
	{
		if (speed < 100)
			speed = 100;
		removalSpeed = speed;
	}
}
