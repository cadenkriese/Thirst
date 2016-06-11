package me.gamerzking.core.updater;

import com.gamerking195.dev.thirst.Main;

import me.gamerzking.core.utils.UtilTime;

/**
 * Created by GamerzKing on 4/18/2016.
 */
public enum UpdateType 
{
	MINUTE(60000),
	HALF_MINUTE(30000),
	FIVE_SECOND(5000),
	THREE_SECOND(3000),
	SECOND(1000),
	HALF_SECOND(500),
	QUARTER_SECOND(250),
	TIER1(15000),
	TIER2(20000),
	TIER3(30000),
	CONFIG((long) Main.getInstance().getYAMLConfig().ThirstDelay*1000),
	TICK(50);

	private long milliseconds;
	private long currentTime;

	UpdateType(long milliseconds) 
	{
		this.milliseconds = milliseconds;
		this.currentTime = System.currentTimeMillis();
	}

	public boolean elapsed() 
	{
		if (UtilTime.elapsed(currentTime, milliseconds))
		{
			currentTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public long getMilliseconds() {
		return milliseconds;
	}

	public long getCurrentTime() {
		return currentTime;
	}
}