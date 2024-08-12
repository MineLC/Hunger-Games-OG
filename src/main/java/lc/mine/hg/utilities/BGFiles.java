package lc.mine.hg.utilities;

import lc.mine.hg.main.BGMain;
import java.io.*;
import org.bukkit.configuration.file.*;

public class BGFiles
{
    public static FileConfiguration abconf;
    public static FileConfiguration bookconf;
    public static FileConfiguration config;
    public static FileConfiguration dsign;
    public static FileConfiguration kitconf;
    public static FileConfiguration worldconf;
    
    public BGFiles() {
        try {
            this.loadFiles();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadFiles() throws Exception {
        final File configFile = new File(BGMain.instance.getDataFolder(), "config.yml");
        final File kitFile = new File(BGMain.instance.getDataFolder(), "kit.yml");
        final File deathSignFile = new File(BGMain.instance.getDataFolder(), "deathsign.yml");
        final File abilitiesFile = new File(BGMain.instance.getDataFolder(), "abilities.yml");
        final File bookFile = new File(BGMain.instance.getDataFolder(), "book.yml");
        final File worldFile = new File(BGMain.instance.getDataFolder(), "world.yml");
        Integer creation = 0;
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            BGMain.copy(BGMain.instance.getResource("config.yml"), configFile);
            ++creation;
        }
        if (!kitFile.exists()) {
            kitFile.getParentFile().mkdirs();
            BGMain.copy(BGMain.instance.getResource("kit.yml"), kitFile);
            ++creation;
        }
        if (!deathSignFile.exists()) {
            deathSignFile.getParentFile().mkdirs();
            BGMain.copy(BGMain.instance.getResource("deathsign.yml"), deathSignFile);
            ++creation;
        }
        if (!abilitiesFile.exists()) {
            abilitiesFile.getParentFile().mkdirs();
            BGMain.copy(BGMain.instance.getResource("abilities.yml"), abilitiesFile);
            ++creation;
        }
        if (!bookFile.exists()) {
            bookFile.getParentFile().mkdirs();
            BGMain.copy(BGMain.instance.getResource("book.yml"), bookFile);
            ++creation;
        }
        if (!worldFile.exists()) {
            worldFile.getParentFile().mkdirs();
            BGMain.copy(BGMain.instance.getResource("world.yml"), worldFile);
            ++creation;
        }
        BGFiles.abconf = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "abilities.yml"));
        BGFiles.bookconf = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "book.yml"));
        BGFiles.config = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "config.yml"));
        BGFiles.dsign = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "deathsign.yml"));
        BGFiles.kitconf = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "kit.yml"));
        BGFiles.worldconf = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "world.yml"));
    }
}
