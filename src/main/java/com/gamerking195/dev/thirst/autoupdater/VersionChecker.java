package com.gamerking195.dev.thirst.autoupdater;

import com.gamerking195.dev.pluginupdater.UpdateLocale;
import com.gamerking195.dev.pluginupdater.Updater;
import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstData;
import com.gamerking195.dev.thirst.config.DataConfig;
import com.gamerking195.dev.thirst.util.UtilActionBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class VersionChecker {
    private VersionChecker() {
    }

    private static VersionChecker instance = new VersionChecker();

    public static VersionChecker getInstance() {
        return instance;
    }

    private static boolean updateRequired = false;

    private boolean updating = false;

    private String latestVersionName = "";

    private final static String VERSION_URL = "https://api.spiget.org/v2/resources/24610/versions/latest";

    public boolean isLatestVersion() {
        Gson gson = new Gson();

        try {
            String latestVersion = readFrom(VERSION_URL);
            Type type = new TypeToken<JsonObject>(){}.getType();
            JsonObject object = gson.fromJson(latestVersion, type);

            latestVersionName = object.get("name").getAsString();
        }
        catch (Exception exception) {
            Main.getInstance().printError(exception, "Error occured whilst pinging spiget.");
        }

        return latestVersionName.equalsIgnoreCase(Main.getInstance().getDescription().getVersion());
    }

    public List<String> getTestedVersions() {
        Gson gson = new Gson();
        List<String> testedVersions = new ArrayList<>();

        try {
            String latestVersion = readFrom("https://api.spiget.org/v2/resources/24610/");

            Type type = new TypeToken<JsonObject>(){}.getType();
            JsonObject object = gson.fromJson(latestVersion, type);

            Type arrayType = new TypeToken<List<String>>(){}.getType();
            testedVersions = gson.fromJson(object.get("testedVersions"), arrayType);
        }
        catch (Exception exception) {
            Main.getInstance().printError(exception, "Error occured whilst pinging spiget.");
        }

        return testedVersions;
    }

    public String getLatestVersionName() {
        return latestVersionName;
    }

    public void update(Player initiater) {
        isLatestVersion();

        UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST V"+Main.getInstance().getDescription().getVersion()+" &b&l» &1&lV"+latestVersionName+" &8[RETREIVING UPDATER]"));

        updating = true;
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/39719/download");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "SpigetResourceUpdater");
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(new File(Main.getInstance().getDataFolder().getPath().substring(0, Main.getInstance().getDataFolder().getPath().lastIndexOf("/"))+"/PluginUpdater.jar"));
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 15);

                final String currentPercent = String.format("%.2f", (((double) downloadedFileSize) / ((double) completeFileSize)) * 100);

                String bar = "&a:::::::::::::::";

                bar = bar.substring(0, currentProgress+2)+"&c"+bar.substring(currentProgress+2);

                UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST V"+Main.getInstance().getDescription().getVersion()+" &b&l» &1&lV"+latestVersionName+" &8&l| "+bar+" &8&l| &2"+currentPercent+"% &8[DOWNLAODING UPDATER]"));

                bout.write(data, 0, x);
            }

            bout.close();
            in.close();

            UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST V"+Main.getInstance().getDescription().getVersion()+" &b&l» &1&lV"+latestVersionName+" &8[RUNNING UPDATER]"));

            Plugin target = Bukkit.getPluginManager().loadPlugin(new File(Main.getInstance().getDataFolder().getPath().substring(0, Main.getInstance().getDataFolder().getPath().lastIndexOf("/"))+"/PluginUpdater.jar"));
            target.onLoad();
            Bukkit.getPluginManager().enablePlugin(target);

            //Save player data

            for (Player p : Bukkit.getOnlinePlayers())
            {
                ThirstData thirstData = Thirst.getThirst().getThirstData(p);

                if (thirstData.getBar() != null)
                {
                    thirstData.getBar().removePlayer(p);
                }
            }

            Main.getInstance().getYAMLConfig().save();

            for (Player p : Bukkit.getServer().getOnlinePlayers())
            {
                p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());

                DataConfig.getConfig().writeThirstToFile(p.getUniqueId(), Thirst.getThirst().getPlayerThirst(p));
            }

            DataConfig.getConfig().saveFile();

            UpdateLocale locale = new UpdateLocale();
            locale.fileName = "Thirst-"+latestVersionName;

            new Updater(initiater, Main.getInstance(), 24610, locale, true).update();
        } catch (Exception ex) {
            Main.getInstance().printError(ex, "Error occured whilst downloading resource update.");
            UtilActionBar.getInstance().sendActionBar(initiater, ChatColor.translateAlternateColorCodes('&', "&f&lUPDATING &1&lTHIRST V"+Main.getInstance().getDescription().getVersion()+" &b&l» &1&lV"+latestVersionName+" &8[&c&lUPDATE FAILED &7&o(Check Console)&8]"));
        }
    }

    private String readFrom(String url) throws IOException
    {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }
}
