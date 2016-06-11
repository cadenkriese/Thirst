package com.connorlinfoot.actionbarapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gamerking195.dev.thirst.Main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ActionBarAPI implements Listener {
	public static Plugin plugin;
	public static boolean works = true;
	public static String nmsver;
	private String pluginMessage = null;
	private String updateMessage = null;
	private boolean updateAvailable = false;
	private static boolean useOldMethods = false;

	public static void init() 
	{
		plugin = Main.getInstance();
		
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);

		if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_7_")) { // Not sure if 1_7 works for the protocol hack?
			useOldMethods = true;
		}

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (updateAvailable && event.getPlayer().isOp()) {
			event.getPlayer().sendMessage(updateMessage);
		}
		if (pluginMessage != null && event.getPlayer().isOp()) {
			event.getPlayer().sendMessage(pluginMessage);
		}
	}

	public static void sendActionBar(Player player, String message) {
		// Call the event, if cancelled don't send Action Bar
		ActionBarMessageEvent actionBarMessageEvent = new ActionBarMessageEvent(player, message);
		Bukkit.getPluginManager().callEvent(actionBarMessageEvent);
		if (actionBarMessageEvent.isCancelled())
			return;

		try {
			Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			Object p = c1.cast(player);
			Object ppoc;
			Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
			Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
			if (useOldMethods) {
				Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
				Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Method m3 = c2.getDeclaredMethod("a", String.class);
				Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
				ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
			} else {
				Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
				Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
				ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
			}
			Method m1 = c1.getDeclaredMethod("getHandle");
			Object h = m1.invoke(p);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
			m5.invoke(pc, ppoc);
		} catch (Exception ex) {
			ex.printStackTrace();
			works = false;
		}
	}

	public static void sendActionBar(final Player player, final String message, int duration) 
	{
		sendActionBar(player, message);

		// Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
		long sched = 0;
		while (duration > 60) 
		{
			duration -= 40;
			sched += 40;
			new BukkitRunnable() 
			{
				public void run() 
				{
					sendActionBar(player, message);
				}
			}.runTaskLater(plugin, sched);
		}
	}

	public static void sendActionBarToAllPlayers(String message) {
		sendActionBarToAllPlayers(message, -1);
	}

	public static void sendActionBarToAllPlayers(String message, int duration) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendActionBar(p, message, duration);
		}
	}
}