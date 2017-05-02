package com.gamerking195.dev.thirst;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class ThirstData 
{
	private Player p;
	//the time in milliseconds when the players thirst will be removed (system.currentTimeMillis()+removalSpeed)
	private long thirstTime;
	//the interval at which a players thirst will be removed, fluctuates depends on config and players active multipliers.
	private long removalSpeed;
	//the amount of thirst the player has out of 100.
	private int thirstAmount;
	//the active bossbar for the player.
	private BossBar bar;
	//The last known location of the player.
	private Location location;
	
	/**
	 * @author GamerKing195
	 * @param p the player whos data this is.
	 * @param time the next time their thirst will be removed.
	 * @param speed the interval at which the players thirst will be removed.
	 * @param amount the amount of thirst a player has 0-100.
	 */
    ThirstData(Player p, long time, long speed, int amount)
	{
		this.p = p;
		thirstTime = time;
		removalSpeed = speed;
		thirstAmount = amount;
		this.location = p.getLocation();
	}

    /*
     * SETTERS
     */

    void setTime(long l)
	{
		thirstTime = l;
	}
	
    void setSpeed(long speed)
	{
		if (speed < 100)
			speed = 100;
		removalSpeed = speed;
	}

    void setThirstAmount(int thirstAmount)
    {
        if (thirstAmount > 100)
            thirstAmount = 100;
        if (thirstAmount < 0)
            thirstAmount = 0;


        this.thirstAmount = thirstAmount;
    }

    void setBar(BossBar bar)
    {
        this.bar = bar;
    }

    public void setLastLocation(Location location) {
        this.location = location;
    }

    /*
     * GETTERS
     */

    public long getSpeed()
    {
        return removalSpeed;
    }

    public Player getPlayer()
    {
        return p;
    }

    public long getTime()
    {
        return thirstTime;
    }

    int getThirstAmount()
	{
		return thirstAmount;
	}

	public BossBar getBar() 
	{
		return bar;
	}

    public Location getLastLocation() {
        return location;
    }
}
