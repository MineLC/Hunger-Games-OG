package lc.mine.hg.utilities;

import lc.mine.hg.events.BGAbilitiesListener;
import lc.mine.hg.main.BGMain;
import java.io.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

public class BGCooldown
{
    public BGCooldown() {
        BGFiles.abconf = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "abilities.yml"));
    }
    
    public static void monkCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.12.Cooldown") * 1000);
    }
    
    public static void thiefCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.15.Cooldown") * 1000);
    }
    
    public static void ghostCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.16.Cooldown") * 1000);
    }
    
    public static void viperCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.19.Duration") * 1000);
    }
    
    public static void orcoCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.35.Duration") * 1000);
    }
    
    public static void trollCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.36.Duration") * 1000);
    }
    
    public static void thorCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.11.Cooldown") * 1000);
    }
    
    public static void flashCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
                player.sendMessage(ChatColor.GREEN + "Ahora puedes volver a teletransportarte!");
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.31.Cooldown") * 1000);
    }
    
    public static void timeCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGAbilitiesListener.cooldown.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.22.Cooldown") * 1000);
    }
    
    public static void freezeCooldown(final Player player) {
        final TimerTask action = new TimerTask() {
            @Override
            public void run() {
                BGChat.printPlayerChat(player, BGFiles.abconf.getString("AB.22.unfrozen"));
                BGMain.frezee.remove(player);
            }
        };
        final Timer timer = new Timer();
        timer.schedule(action, BGFiles.abconf.getInt("AB.22.Duration") * 1000);
    }
}
