package com.gamerking195.dev.thirst;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gamerking195.dev.thirst.util.UtilActionBar;
import com.gamerking195.dev.thirst.util.UtilSQL;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.gamerking195.dev.thirst.config.DataConfig;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.cubespace.Yamler.Config.InvalidConfigurationException;

public class Thirst
{
    private Thirst() {}
    private static Thirst instance = new Thirst();
    public static Thirst getThirst() {	return instance;	 }

    //All data for players thirst String = Player UUID.
    private ConcurrentHashMap<String, ThirstData> playerThirstData = new ConcurrentHashMap<String, ThirstData>();

    private ScoreboardManager manager;

    void init()
    {
        manager = Bukkit.getScoreboardManager();

        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
            playerJoin(player);
        }
    }

    public void removeThirst(Player player)
    {
        setThirst(player, getPlayerThirst(player)-Main.getInstance().getYAMLConfig().removeThirst);

        long speed = calculateSpeed(player);
        ThirstData data = getThirstData(player);
        data.setSpeed(speed);
        data.setTime(System.currentTimeMillis()+speed);
        setThirstData(player, data);

        displayThirst(player);
    }

    private long calculateSpeed(Player player)
    {
        long speed = (long) (Main.getInstance().getYAMLConfig().thirstDelay *1000);

        //SPRINT
        if(player.isSprinting())
        {
            if (Main.getInstance().getYAMLConfig().sprint != -1 && Main.getInstance().getYAMLConfig().sprint != -0)
            {
                speed -= Main.getInstance().getYAMLConfig().sprint *1000;
            }
        }

        //BIOME
        Biome b = player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        for(String s : Main.getInstance().getYAMLConfig().biomes)
        {
            String[] split = s.split("\\.");

            if (!NumberUtils.isNumber(split[1]))
                Main.getInstance().printPluginError("Error while reading the config.", "String '"+split[1]+"' is not a valid number!");

            String biome = split[0];
            int timeRemoved = Integer.valueOf(split[1])*1000;

            if (Arrays.stream(Biome.values()).noneMatch((biome2) -> biome2.name().equals(biome.toUpperCase())))
                Main.getInstance().printPluginError("Error while reading the config.", "String '"+split[0]+"' is not a valid biome!");

            if (b.toString().equalsIgnoreCase(biome))
            {
                speed -= timeRemoved;
            }
        }

        //ARMOR
        for (String string : Main.getInstance().getYAMLConfig().armor)
        {
            String[] split = string.split("\\.");

            if (split.length == 2)
            {
                if (!NumberUtils.isNumber(split[1]))
                    Main.getInstance().printPluginError("Error while reading the config.", "String '"+split[1]+"' is not a valid number!");

                String armorType = split[0];
                int timeRemoved = Integer.valueOf(split[1])*1000;

                if (player.getInventory().getBoots() != null && player.getInventory().getLeggings() != null && player.getInventory().getChestplate() != null && player.getInventory().getHelmet() != null)
                {
                    if (armorType.equalsIgnoreCase("LEATHER"))
                    {
                        if (player.getInventory().getHelmet().getType() == Material.LEATHER_HELMET && player.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE && player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS && player.getInventory().getBoots().getType() == Material.LEATHER_BOOTS)
                        {
                            speed -= timeRemoved;
                        }
                    }
                    else if (armorType.equalsIgnoreCase("GOLD"))
                    {
                        if (player.getInventory().getHelmet().getType() == Material.GOLD_HELMET && player.getInventory().getChestplate().getType() == Material.GOLD_CHESTPLATE && player.getInventory().getLeggings().getType() == Material.GOLD_LEGGINGS && player.getInventory().getBoots().getType() == Material.GOLD_BOOTS)
                        {
                            speed -= timeRemoved;
                        }
                    }
                    else if (armorType.equalsIgnoreCase("CHAINMAIL"))
                    {
                        if (player.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET && player.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE && player.getInventory().getLeggings().getType() == Material.CHAINMAIL_LEGGINGS && player.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS)
                        {
                            speed -= timeRemoved;
                        }
                    }
                    else if (armorType.equalsIgnoreCase("IRON"))
                    {
                        if (player.getInventory().getHelmet().getType() == Material.IRON_HELMET && player.getInventory().getChestplate().getType() == Material.IRON_CHESTPLATE && player.getInventory().getLeggings().getType() == Material.IRON_LEGGINGS && player.getInventory().getBoots().getType() == Material.IRON_BOOTS)
                        {
                            speed -= timeRemoved;
                        }
                    }
                    else if (armorType.equalsIgnoreCase("DIAMOND"))
                    {
                        if (player.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET && player.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE && player.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS && player.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS)
                        {
                            speed -= timeRemoved;
                        }
                    }
                }
                else if (!armorType.equalsIgnoreCase("LEATHER") && !armorType.equalsIgnoreCase("GOLD") && !armorType.equalsIgnoreCase("CHAINMAIL") && !armorType.equalsIgnoreCase("IRON") && !armorType.equalsIgnoreCase("DIAMOND")) {
                    if (validateMaterial(armorType) != null) {
                        Material mat = Material.valueOf(armorType);
                        if ((player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == mat) || (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == mat) || (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == mat) || (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == mat)) {
                            speed -= timeRemoved;
                        }
                    }
                    else
                        Main.getInstance().printPluginError("Error while reading the config.", "String '"+string+"' is in an invalid format!");
                }
            }
        }

        //DAY
        if (player.getWorld().getTime() > 0 && player.getWorld().getTime() < 12300)
        {
            if (Main.getInstance().getYAMLConfig().dayMultiplier > 0)
            {
                speed -= Main.getInstance().getYAMLConfig().dayMultiplier *1000;
            }
            else
            {
                speed += Main.getInstance().getYAMLConfig().dayMultiplier *1000;
            }
        }
        //NIGHT
        else
        {
            if (Main.getInstance().getYAMLConfig().nightMultiplier > 0)
            {
                speed -= Main.getInstance().getYAMLConfig().nightMultiplier *1000;
            }
            else
            {
                speed += Main.getInstance().getYAMLConfig().nightMultiplier *1000;
            }
        }

        if (speed < 100)
        {
            speed = 100;
        }

        return speed;
    }

    public String getThirstString(OfflinePlayer offlinePlayer)
    {
        int percent = getPlayerThirst(offlinePlayer);

        String emphasis = "";

        if (percent <= Main.getInstance().getYAMLConfig().criticalThirstPercent && !Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD")) emphasis = " &4!&c!&4! ";

        String configMessage = Main.getInstance().getYAMLConfig().thirstMessage.replace("%player%", offlinePlayer.getName()).replace("%percent%", getThirstPercent(offlinePlayer, true)).replace("%thirstbar%", "&1"+getThirstBar(offlinePlayer)).replace("%removespeed%", String.valueOf((double) getThirstData(offlinePlayer).getSpeed()/1000));

        return ChatColor.translateAlternateColorCodes('&', emphasis+configMessage+emphasis);
    }

    public String getThirstBar(OfflinePlayer offlinePlayer)
    {
        int percent = getPlayerThirst(offlinePlayer);

        String thirstBar = "::::::::::";

        int bars = (int) Math.round(percent/10.0);

        return thirstBar.substring(0, bars)+ChatColor.RED+thirstBar.substring(bars, thirstBar.length())+ChatColor.RESET;
    }

    public String getThirstPercent(OfflinePlayer offlinePlayer, boolean colored)
    {
        int percent = getPlayerThirst(offlinePlayer);

        if (colored)
        {
            String percentColor = "&a";

            if (percent <= 20) percentColor = "&4";
            else if (percent <= 40) percentColor = "&c";
            else if (percent <= 60) percentColor = "&6";
            else if (percent <= 80) percentColor = "&e";

            return percentColor+percent+"%";
        }
        else
        {
            return percent+"%";
        }
    }

    public void playerJoin(Player player)
    {
        UUID pid = player.getUniqueId();

        long removalSpeed = calculateSpeed(player);
        long removeTime = System.currentTimeMillis()+removalSpeed;
        int startingThirst = 100;

        if (!player.hasPlayedBefore() || getPlayerThirst(player) < 0)
        {
            if (Main.getInstance().getYAMLConfig().enableSQL) {
                UtilSQL.getInstance().runStatement("INSERT INTO TABLENAME (uuid,thirst) VALUES ('"+player.getUniqueId().toString()+"','"+getPlayerThirst(player)+"') ON DUPLICATE KEY UPDATE thirst = "+getPlayerThirst(player));
            }
            else {
                DataConfig.getConfig().writeThirstToFile(pid, 100);
                DataConfig.getConfig().saveFile();
            }
        }
        else
        {
            startingThirst = getPlayerThirst(player);
        }

        ThirstData playerData = new ThirstData(player, removeTime, removalSpeed, startingThirst);
        setThirstData(player, playerData);

        displayThirst(player);
    }

    public void playerLeave(Player player)
    {
        if (player.isDead()) setThirst(player, 100);

        player.setScoreboard(manager.getNewScoreboard());

        if (Main.getInstance().getYAMLConfig().enableSQL) {
            UtilSQL.getInstance().runStatement("INSERT INTO TABLENAME (uuid,thirst) VALUES ('"+player.getUniqueId().toString()+"','"+getPlayerThirst(player)+"') ON DUPLICATE KEY UPDATE thirst = "+getPlayerThirst(player));
        }
        else {
            DataConfig.getConfig().writeThirstToFile(player.getUniqueId(), getPlayerThirst(player));
            DataConfig.getConfig().saveFile();
        }

        playerThirstData.remove(player.getName());

        playerThirstData.remove(player);
    }

    public void setThirst(Player player, int thirst)
    {
        int oldThirst = getPlayerThirst(player);

        if (player.isDead()) thirst = 100;

        if (thirst < 0) thirst = 0;
        if (thirst > 100) thirst = 100;

        playerThirstData.get(player.getUniqueId().toString()).setThirstAmount(thirst);

        if (Main.getInstance().getYAMLConfig().effectsEnabled)
        {
            for (String potion : Main.getInstance().getYAMLConfig().potions)
            {
                String[] parts = potion.split("\\.");

                if (parts.length != 4)
                {
                    Main.getInstance().printPluginError("Error occurred while reading the config", "String '"+potion+"' is in an invalid format!");
                    return;
                }

                int percent = Integer.valueOf(parts[0]);

                if (percent >= thirst)
                {
                    if (parts[1].equalsIgnoreCase("DAMAGE"))
                    {
                        return;
                    }

                    PotionEffectType type = PotionEffectType.getByName(parts[1].toUpperCase());

                    if (type == null)
                    {
                        Main.getInstance().printPluginError("Error occurred while reading the config", "String '"+potion+"' is in an invalid format!");
                        return;
                    }

                    PotionEffect effect = new PotionEffect(type, Integer.valueOf(parts[2])*20, Integer.valueOf(parts[3])-1);

                    player.addPotionEffect(effect);
                }
                else if (percent > thirst)
                {
                    PotionEffectType type = PotionEffectType.getByName(parts[1].toUpperCase());
                    player.removePotionEffect(type);
                }
            }
        }

        if (thirst <= Main.getInstance().getYAMLConfig().criticalThirstPercent && oldThirst != -1 && oldThirst > thirst) player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().thirstLowMessage.replace("%player%", player.getName()).replace("%percent%", getThirstPercent(player, true))));

        displayThirst(player);
    }

    private void refreshScoreboard(Player player)
    {
        if (getThirstString(player).length() > 40)
        {
            Main.getInstance().printPluginError("Error occurred while displaying scoreboard.", "The string "+getThirstString(player)+" is longer than 40 characters." +
                                                                                                      "\nYou must have a thirst message under 40 characters to use the SCOREBOARD displaytype." +
                                                                                                      "\n " +
                                                                                                      "\nNOTE: This message will be displayed every time Thirst tries to update someones thirst (A lot!)");
            return;
        }

        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective(player.getName().toUpperCase(), "dummy");

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().scoreboardName.replace("%player%", player.getName())));
        obj.getScore(getThirstString(player)).setScore(-1);

        player.setScoreboard(board);
    }

    public void blipScoreboard(final Player player)
    {
        refreshScoreboard(player);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
            }
        }.runTaskLater(Main.getInstance(), 100L);
    }

    public int getPlayerThirst(OfflinePlayer player) {
        int percent = -1;

        if (playerThirstData.containsKey(player.getUniqueId().toString()))
        {
            percent = playerThirstData.get(player.getUniqueId().toString()).getThirstAmount();
        }
        else if (Main.getInstance().getYAMLConfig().enableSQL)
        {
            ResultSet resultSet = UtilSQL.getInstance().runQuery("SELECT * FROM TABLENAME WHERE uuid = '"+player.getUniqueId().toString()+"'");
            if (resultSet != null) {
                try {
                    if (resultSet.isClosed()) {
                        Main.getInstance().getLogger().severe("RESULT SET IS CLOSED");
                        return -1;
                    }

                    if (resultSet.first()) {
                        percent = resultSet.getInt("thirst");

                        if (player.isOnline()) {
                            Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
                            long removalSpeed = calculateSpeed(onlinePlayer);
                            long removeTime = System.currentTimeMillis() + removalSpeed;
                            ThirstData playerData = new ThirstData(onlinePlayer, removeTime, removalSpeed, percent);
                            playerThirstData.put(onlinePlayer.getUniqueId().toString(), playerData);
                        }
                    }
                } catch (Exception exception) {
                    Main.getInstance().printError(exception, "Error occurred while retrieving thirst from database.");
                } finally {
                    try {
                        resultSet.close();
                    } catch (Exception exception) {
                        Main.getInstance().printError(exception, "Error occurred while closing result set.");
                    }
                }
            }
        }
        else if (DataConfig.getConfig().fileContains(player.getUniqueId()))
        {
            percent = DataConfig.getConfig().getThirstFromFile(player.getUniqueId());
        }

        return percent;
    }

    public ThirstData getThirstData(OfflinePlayer offlinePlayer)
    {
        return playerThirstData.get(offlinePlayer.getUniqueId().toString());
    }

    private void setThirstData(Player player, ThirstData data)
    {
        playerThirstData.put(player.getUniqueId().toString(), data);
    }

    public void displayThirst(Player player)
    {
        if (validatePlayer(player))
        {
            if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("ACTION")) UtilActionBar.getInstance().sendActionBar(player, getThirstString(player));
            else if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD") && !Main.getInstance().getYAMLConfig().alwaysShowActionBar) blipScoreboard(player);
            else if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD")) refreshScoreboard(player);
            else if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("BOSSBAR") && !Main.getInstance().getYAMLConfig().alwaysShowActionBar) blipBossbar(player);
            else if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("BOSSBAR")) sendBossBar(player);
        }
    }

    private void sendBossBar(Player player)
    {
        if (Bukkit.getBukkitVersion().contains("1.9") || Bukkit.getServer().getBukkitVersion().contains("1.10") || Bukkit.getBukkitVersion().contains("1.11"))
        {
            if (validateColor(Main.getInstance().getYAMLConfig().barColor) != null && validateStyle(Main.getInstance().getYAMLConfig().barStyle) != null) {
                BossBar bar;

                if (getThirstData(player).getBar() != null) {
                    bar = getThirstData(player).getBar();
                    bar.setTitle(ChatColor.translateAlternateColorCodes('&', getThirstString(player)));
                    bar.setColor(BarColor.valueOf(Main.getInstance().getYAMLConfig().barColor.toUpperCase()));
                    bar.setStyle(BarStyle.valueOf(Main.getInstance().getYAMLConfig().barStyle.toUpperCase()));
                } else {
                    bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', getThirstString(player)), BarColor.valueOf(Main.getInstance().getYAMLConfig().barColor.toUpperCase()), BarStyle.valueOf(Main.getInstance().getYAMLConfig().barStyle.toUpperCase()), new BarFlag[0]);
                    bar.addPlayer(player);
                    getThirstData(player).setBar(bar);
                }

                if (Main.getInstance().getYAMLConfig().useBarProgress)
                    bar.setProgress((double) getPlayerThirst(player) / 100);
            }
        }
        else
        {
            try
            {
                Main.getInstance().getLogger().log(Level.SEVERE, "[Thirst V"+Main.getInstance().getDescription().getVersion()+"] Your Spigot version is not compatible with the Bossbar display type, please use version 1.9 or higher.");
                Main.getInstance().getLogger().log(Level.SEVERE, "[Thirst V"+Main.getInstance().getDescription().getVersion()+"] Changing to display type ACTION...");

                UtilActionBar.getInstance().sendActionBar(player, Thirst.getThirst().getThirstString(player));

                Main.getInstance().getYAMLConfig().displayType = "ACTION";
                Main.getInstance().getYAMLConfig().save();
            }
            catch (InvalidConfigurationException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void blipBossbar(final Player player)
    {
        sendBossBar(player);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                ThirstData data = Thirst.getThirst().getThirstData(player);

                if (data.getBar() != null)
                {
                    data.getBar().removePlayer(player);
                }
            }
        }.runTaskLater(Main.getInstance(), 100L);
    }

    public ConcurrentHashMap<String, ThirstData> getThirstDataMap()
    {
        return playerThirstData;
    }

    public boolean validatePlayer(Player player)
    {
        if (player == null) return false;
        if (!player.isOnline()) return false;
        if (player.getGameMode() == GameMode.CREATIVE && Main.getInstance().getYAMLConfig().ignoreCreative) return false;
        if (player.isOp() && Main.getInstance().getYAMLConfig().ignoreOP)  return false;
        if (player.hasPermission("thirst.ignore") || player.hasPermission("thirst.*"))  return false;

        String world = player.getWorld().getName();
        List<String> worlds = Arrays.asList(Main.getInstance().getYAMLConfig().disabledWorlds);
        if ( worlds.size() > 0)
        {
            if (worlds.contains(world)) return false;
        }

        if (Main.getInstance().isWorldGuardEnabled())
        {
            for(ProtectedRegion region : WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()))
            {
                if (region == null) continue;
                ArrayList<String> regions  = (ArrayList<String>) Arrays.asList(Main.getInstance().getYAMLConfig().disabledRegions);

                if (regions.contains(region)) return false;
            }
        }
        return true;
    }

    private BarColor validateColor(String name)
    {
        try
        {
            return Enum.valueOf(BarColor.class, name.toUpperCase());
        }
        catch (IllegalArgumentException iae)
        {
            return null;
        }
    }

    private BarStyle validateStyle(String name)
    {
        try
        {
            return Enum.valueOf(BarStyle.class, name.toUpperCase());
        }
        catch (IllegalArgumentException iae)
        {
            return null;
        }
    }

    private Material validateMaterial(String name) {
        try
        {
            return Enum.valueOf(Material.class, name.toUpperCase());
        }
        catch (IllegalArgumentException iae)
        {
            return null;
        }
    }
}
