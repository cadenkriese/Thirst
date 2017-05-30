package com.gamerking195.dev.thirst.config;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cubespace.Yamler.Config.Path;
import org.apache.commons.lang.math.NumberUtils;

import com.gamerking195.dev.thirst.Main;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Comments;
import net.cubespace.Yamler.Config.YamlConfig;
import org.bukkit.Bukkit;

public class YAMLConfig extends YamlConfig {

    public YAMLConfig(Main plugin) {
        CONFIG_HEADER = new String[]
                                {
                                        "#################################",
                                        "                                #",
                                        "Thirst V" + Main.getInstance().getDescription().getVersion() + ", by " + Main.getInstance().getDescription().getAuthors() + "#",
                                        "                                #",
                                        "#################################",
                                        "",
                                        "Config Guide:",
                                        "",
                                        "STRING, Any text you want.",
                                        "INT, A number without a decimal.",
                                        "FLOAT, A number with a decimal.",
                                        "BOOLEAN, A string that either equals true or false"
                                };
        CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
    }

    //CONFIG

    @Comment("---------------General---------------")

    @Comments
            ({
                     "",
                     "REMOVE_THIRST",
                     "Desc: The amount of thirst that will be removed every THIRST_DELAY",
                     "Type: int (100 or lower.)",
                     "Default: 1"
            })
    @Path("Remove-Thirst")
    public int removeThirst = 1;

    @Comments
            ({
                     "",
                     "CRITICAL_THIRST_PERCENT",
                     "Desc: The percent at which a players thirst becomes critical and the THIRST_LOW_MESSAGE and !!! will be displayed.",
                     "Type: Integer",
                     "Default: 10"
            })
    @Path("Critical-Thirst-Percent")
    public int criticalThirstPercent = 10;

    @Comments
            ({
                     "",
                     "THIRST_DELAY",
                     "Desc: The delay in seconds before thirst is removed from every player.",
                     "Type: float (Time in seconds)",
                     "Default: 36 (will remove 100% over three days, just like in real life!)",
                     "Note: This does support values under one second without any changes in lag!"
            })
    @Path("Thirst-Delay")
    public float thirstDelay = 36;

    @Comments
            ({
                     "",
                     "THIRST_QUENCHING_ITEM",
                     "Desc: The item that will quench the thirst of a player.",
                     "Type: Formatted string array",
                     "Default:",
                     "- POTION=20",
                     "- GOLDEN_APPLE:1=100",
                     "Requirements: Should be in format: ITEM:METADATA=PERCENT(metadata optional)"
            })
    @Path("Thirst-Quenching-Items")
    public String[] thirstQuenchingItems = {"POTION=20", "GOLDEN_APPLE:1=100"};

    @Comments
            ({
                     "",
                     "IGNORE_CREATIVE",
                     "Desc: If true, anyone in creative will not be affected by Thirst.",
                     "Type: Boolean",
                     "Default: true"
            })
    @Path("Ignore-Creative")
    public boolean ignoreCreative = true;

    @Comments
            ({
                     "",
                     "IGNORE_OP",
                     "Desc: If true, anyone who is opped will not be affected by Thirst.",
                     "Type: Boolean",
                     "Default: false"
            })
    @Path("Ignore-OP")
    public boolean ignoreOP = false;

    @Comments
            ({
                     "",
                     "DRINK_BLOCK_WATER",
                     "Desc: If true, players will recover 2% of their thirst every second, if they are swimming in water.",
                     "Type: Boolean",
                     "Default: false"
            })
    @Path("Drink-Block-Water")
    public boolean drinkBlockWater = false;

    @Comments
            ({
                     "",
                     "BLOCK_DRINK_DELAY",
                     "Desc: Time in seconds between when players gain 1 Thirst from being under water.",
                     "Type: Float (Seconds)",
                     "Default: 1"
            })
    @Path("Block-Drink-Delay")
    public int blockDrinkDelay = 1;

    @Comments
            ({
                     "",
                     "ENABLE_UPDATER",
                     "Desc: If true, the auto-updater will ping the spigot site and alert you about new updates, and even automatically install them.",
                     "Note: If set to false you can still install updates via /thirst update.",
                     "Type: Boolean",
                     "Default: true"
            })
    @Path("Enable-Updater")
    public boolean enableUpdater = true;

    @Comments
            ({
                     "",
                     "REMOVE_WHEN_AFK",
                     "Desc: If false, thirst will not remove thirst whenever the player hasn't moved since their last removal of thirst.",
                     "Type: Boolean",
                     "Default: true"
            })
    @Path("Remove-When-AFK")
    public boolean removeAFK = true;

    @Comment("---------------Display---------------")
    @Comments
            ({
                     "",
                     "DISPLAY_TYPE",
                     "Desc: Changes the way players see their thirst.",
                     "Type: Enum",
                     "Possible types: SCOREBOARD, ACTION, COMMAND, BOSSBAR",
                     "Default: ACTION",
                     "Note: The command /thirst view, and /thirst view %player% will always be enabled, use command to disable scoreboard, and action."
            })
    @Path("Display.Display-Type")
    public String displayType = "ACTION";

    @Comments
            ({
                     "",
                     "BAR_COLOR",
                     "Desc: The colors for the bossbar, if that display type is chosen.",
                     "Type: Enum",
                     "Possible types: BLUE, GREEN, PINK, PURPLE, RED, YELLOW, WHITE",
                     "Default: BLUE",
            })
    @Path("Display.Bar-Color")
    public String barColor = "BLUE";

    @Comments
            ({
                     "",
                     "BAR_STYLE",
                     "Desc: The style for the bossbar, if that display type is chosen.",
                     "Type: Enum",
                     "Possible types: SOLID, SEGMENTED_6, SEGMENTED_10, SEGMENTED_12, SEGMENTED_20",
                     "Default: SOLID",
            })
    @Path("Display.Bar-Style")
    public String barStyle = "SOLID";

    @Comments
            ({
                     "",
                     "USE_BAR_PROGRESS",
                     "Desc: Should the bossbar progress mimic that of the players thirst.",
                     "Type: Boolean",
                     "Default: FALSE"
            })
    @Path("Display.Use-Bar-Progress")
    public boolean useBarProgress = false;

    @Comments
            ({
                     "",
                     "ALWAYS_SHOWING",
                     "Desc: If set to false thirst will only display when a players thirst changes.",
                     "Type: Boolean",
                     "Default: false",
                     "Notes: For scoreboards & bossbar it will make the scoreboard pop up for 3 seconds then go away."
            })
    @Path("Display.Always-Showing")
    public boolean alwaysShowActionBar = true;

    @Comment("---------------Multipliers---------------")
    @Comments
            ({
                     "",
                     "BIOMES",
                     "Desc: List all biomes you want to modify players thirst percent. Ex: desert.5 will make players thirst go down 5 seconds quicker",
                     "Type: Formatted string array",
                     "Default:",
                     "DESERT.5",
                     "HELL.10"
            })
    @Path("Multipliers.Biomes")
    public String[] biomes = new String[]{"DESERT.5", "HELL.10"};
    @Comments
            ({
                     "",
                     "ARMOR",
                     "Desc: List all armor types you want to modify players thirst percent. Ex: leather.5 will make players thirst go down 5 seconds quicker in full leather armor",
                     "Type: Formatted string array",
                     "Default:",
                     "LEATHER.5",
                     "IRON_CHESTPLATE.10"
            })
    @Path("Multipliers.Armor")
    public String[] armor = new String[]{"LEATHER.5", "IRON_CHESTPLATE.10"};
    @Comments
            ({
                     "",
                     "SPRINT",
                     "Desc: How much quicker you want thirst to be removed when sprinting, put -1 to make no thirst removed.",
                     "Type: Integer",
                     "Default: -1"
            })
    @Path("Multipliers.Sprint")
    public int sprint = -1;
    @Comments
            ({
                     "",
                     "DAY",
                     "Desc: How much quicker you want thirst to be removed when it is day, put 0 to make no thirst removed, put negative to make thirst take longer to be removed.",
                     "Type: Integer",
                     "Default: 0"
            })
    @Path("Multipliers.Day")
    public int dayMultiplier = 0;
    @Comments
            ({
                     "",
                     "NIGHT",
                     "Desc: How much quicker you want thirst to be removed when it is night, put 0 to make no thirst removed, put negative to make thirst take longer to be removed.",
                     "Type: Integer",
                     "Default: 0"
            })
    @Path("Multipliers.Night")
    public int nightMultiplier = 0;

    @Comment("---------------Potions---------------")
    @Comments
            ({
                     "",
                     "ENABLED",
                     "Desc: If false, all potion effects will not be given.",
                     "Type: Boolean",
                     "Default: true"
            })
    @Path("Effects.Enabled")
    public boolean effectsEnabled = true;

    @Comments
            ({
                     "",
                     "EFFECTS",
                     "Desc: The effects that will be applied on low thirst.",
                     "Type: Formatted string array",
                     "Default:",
                     "- 10.SLOW_DIGGING.30.1",
                     "- 0.DAMAGE.2.3",
                     "Requirements: Should be in format 'PERCENT.POTIONEFFECT_DURATION-IN-SECONDS_AMPLIFIER'",
                     "Note: To damage a player use the effect damage the duration will be the time between each damage",
                     "and the amplifier is how much damage done (out of 20)."
            })
    @Path("Effects.Potions")
    public String[] potions = {"10.CONFUSION.30.1", "0.DAMAGE.2.3"};


    @Comment("---------------Messages---------------")

    @Comments
            ({
                     "",
                     "THIRST_MESSAGE",
                     "Desc: Changes the message displayed in the display_type.",
                     "Type: String",
                     "Variables: %thirstbar%, %percent%, %player%, %removespeed%",
                     "Default: &bTHIRST &f- &8[%thirstbar%&8] %percent%"
            })
    @Path("Messages.ThirstMessage")
    public String thirstMessage = "&b&lTHIRST &f- &8[%thirstbar%&8] %percent%";

    @Comments
            ({
                     "",
                     "THIRST_LOW_MESSAGE",
                     "Desc: The message that will be displayed to a player when their thirst is below the critical thirst amount.",
                     "Type: String",
                     "Variables: %percent%, %player%",
                     "Default: &8[&bThirst&8] &aWatch out &e%player%, &ayour thirst is at &e%percent%!"
            })
    @Path("Messages.ThirstLowMessage")
    public String thirstLowMessage = "&8[&1Thirst&8] &bWatch out &f%player%, &byour thirst is at &f%percent%!";

    @Comments
            ({
                     "",
                     "THIRST_DEATH_MESSAGE",
                     "Desc: The message that will be sent when a player dies of thirst.",
                     "Type: String",
                     "Variables: %player%",
                     "Default: &f%player% didn't drink his water bottle."
            })
    @Path("Messages.Thirst-Death-Message")
    public String thirstDeathMessage = "&f%player% didn't drink his water bottle.";

    @Comments
            ({
                     "",
                     "THIRST_VIEW_PLAYER_MESSAGE",
                     "Desc: The message that will be sent when someone does /thirst view %player%",
                     "Type: String",
                     "Variables: %player%, %thirstbar%, %percent%, %thirstmessage%, %removespeed%",
                     "Default: &f%player%'s &bthirst: %thirstmessage%"
            })
    @Path("Messages.Thirst-View-Player")
    public String thirstViewPlayerMessage = "&8[&1Thirst&8] &f%player%'s &bthirst: %thirstmessage%";

    @Comments
            ({
                     "",
                     "THIRST_VIEW_MESSAGE",
                     "Desc: The message sent when a player does /thirst view",
                     "Type: String",
                     "Variables: %player%",
                     "Default: &8[&1Thirst&8] &bYour thist: ",
                     "Note: This message will be displayed before the %thirstmessage%",
                     "Note: There will not be a space between messages unless you add one."
            })
    @Path("Messages.Thirst-View-Message")
    public String thirstViewMessage = "&8[&1Thirst&8] &bYour thirst: ";

    @Comments
            ({
                     "",
                     "INVALID_COMMAND_MESSAGE",
                     "Desc: The message that wil be sent when someone does /thirst view %player% with an invalid playername.",
                     "Type: String",
                     "Default: &8[&1Thirst&8] &bInvalid command syntax!"
            })
    @Path("Messages.Invalid-Command-Message")
    public String invalidCommandMessage = "&8[&1Thirst&8] &bInvalid command syntax!";

    @Comments
            ({
                     "",
                     "NO_PERMISSION_MESSAGE",
                     "Desc: The message that will be sent when a player does not have permission to do something.",
                     "Type: String",
                     "Default: &8[&1Thirst&8] &bYou do not have permission to do that!"
            })
    @Path("Messages.No-Permission-Message")
    public String noPermissionMessage = "&8[&1Thirst&8] &bYou do not have permission to do that!";

    @Comments
            ({
                     "",
                     "SCOREBOARD_NAME",
                     "Desc: Only applies if display_type is set to SCOREBOARD.",
                     "Type: String",
                     "Variables: %player%",
                     "Default: &f&lTHIRST"
            })
    @Path("Messages.Scoreboard-Title")
    public String scoreboardName = "&f&lTHIRST";

    @Comment("---------------Disabled Areas---------------")

    @Comments
            ({
                     "",
                     "DISABLED_WORLDS",
                     "Desc: List all of the worlds that will be unnaffected by thirst.",
                     "Type: String Array",
                     "Default: []"
            })
    @Path("DisabledAreas.Disabled-Worlds")
    public String[] disabledWorlds = new String[0];

    @Comments
            ({
                     "",
                     "DISABLED_REGIONS",
                     "Desc: List all of the worldgaurd regions that will be unnaffected by thirst..",
                     "Type: String Array",
                     "Default: []"
            })
    @Path("DisabledAreas.Disabled-Regions")
    public String[] disabledRegions = new String[0];

    @Comment("---------------SQL Database---------------")

    @Comments
            ({
                     "",
                     "ENABLE_SQL",
                     "Desc: Should thirst data be stored in a MySQL database so you can sync thirst between servers.",
                     "Type: boolean",
                     "Default: false"
            })
    @Path("SQL.Enable-Sql")
    public boolean enableSQL = false;

    @Comments
            ({
                     "",
                     "HOST_NAME",
                     "Desc: Hostname / IP to the MySQL db.",
                     "Type: sting"
            })
    @Path("SQL.Hostname")
    public String hostName = "0.0.0.0";

    @Comments
            ({
                     "",
                     "USERNAME",
                     "Desc: Username Thirst will use to connect to the database.",
                     "Type: boolean"
            })
    @Path("SQL.Username")
    public String username = "root";

    @Comments
            ({
                     "",
                     "PASSWORD",
                     "Desc: Username Thirst will use to connect to the database.",
                     "Type: boolean"
            })
    @Path("SQL.Password")
    public String password = "1234";

    @Comments
            ({
                     "",
                     "DATABASE",
                     "Desc: The database that Thirst will store its table in.",
                     "Type: boolean"
            })
    @Path("SQL.Database")
    public String database = "db";

    @Comments
            ({
                     "",
                     "TABLENAME",
                     "Desc: The name of the table Thirst will store data in.",
                     "Type: boolean"
            })
    @Path("SQL.Tablename")
    public String tablename = "thirst";

    //CLASSES

    public class ThirstItem extends YamlConfig {
        private String item = "POTION";
        private int quenchPercent = 20;
        private int metaData = 0;

        public ThirstItem(String s) {
            if (!s.contains("=")) {
                Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");

                item = "AIR";
                quenchPercent = 0;
                return;
            }

            Pattern pat = Pattern.compile("(.*?):(\\d*)=(\\d*)");
            Matcher match = pat.matcher(s);

            if (match.find()) {
                if (NumberUtils.isNumber(match.group(2)) && NumberUtils.isNumber(match.group(3))) {
                    item = match.group(1);
                    metaData = Integer.valueOf(match.group(2));
                    quenchPercent = Integer.valueOf(match.group(3));
                }
                else {
                    Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");

                    item = "AIR";
                    quenchPercent = 0;
                }
            }
            else {
                Pattern pat2 = Pattern.compile("(.*?)=(\\d*)");
                Matcher match2 = pat2.matcher(s);

                if (match2.find()) {
                    if (NumberUtils.isNumber(match2.group(2))) {
                        item = match2.group(1);
                        quenchPercent = Integer.valueOf(match2.group(2));
                    } else {
                        Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");

                        item = "AIR";
                        quenchPercent = 0;
                    }
                }
                else {
                    Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");

                    item = "AIR";
                    quenchPercent = 0;
                }
            }
        }

        public String toString() {
            return "" + item.toUpperCase() + "-" + quenchPercent;
        }

        public String getItem() {
            return item;
        }

        public int getQuenchPercent() {
            return quenchPercent;
        }

        public int getMetaData() {
            return metaData;
        }
    }

    //METHODS

    public int getDamageInterval() {
        for (String s : potions) {
            String[] parts = s.split("\\.");
            if (parts.length != 4) {
                Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");

                return -1;
            }

            if (parts[1].equalsIgnoreCase("DAMAGE")) {
                return Integer.valueOf(parts[2]);
            }
        }
        return -1;
    }

    public int getDamageAmount() {
        for (String s : potions) {
            String[] parts = s.split("\\.");
            if (parts.length != 4) {
                Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");

                return -1;
            }

            if (parts[1].equalsIgnoreCase("DAMAGE")) {
                return Integer.valueOf(parts[3]);
            }
        }
        return -1;
    }

    public int getDamagePercent() {
        for (String s : potions) {
            String[] parts = s.split("\\.");
            if (parts.length != 4) {
                Main.getInstance().printPluginError("Error while reading the config.", "String '" + s + "' is in an invalid format!");
                return -1;
            }

            int percent = Integer.valueOf(parts[0]);

            if (parts[1].startsWith("DAMAGE")) {
                return percent;
            }
        }
        return -1;
    }
}
