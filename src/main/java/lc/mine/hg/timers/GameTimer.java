package lc.mine.hg.timers;

import lc.mine.hg.main.BGMain;
import lc.mine.hg.utilities.BGChat;
import lc.mine.hg.utilities.BGFBattle;
import lc.mine.hg.utilities.Util;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.potion.*;
import org.bukkit.plugin.*;

public class GameTimer
{
    private static Integer shed_id;
    
    static {
        GameTimer.shed_id = null;
    }
    
    public GameTimer() {
        GameTimer.shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) BGMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                ++BGMain.GAME_RUNNING_TIME;
                BGMain.checkwinner();
                if (BGMain.GAME_RUNNING_TIME % 5 != 0 & BGMain.GAME_RUNNING_TIME % 10 != 0) {
                    BGChat.printTipChat();
                }
                if (BGMain.GAME_RUNNING_TIME == BGMain.END_GAME_TIME - 1) {
                    for (final Player pl : BGMain.getGamers()) {
                        pl.playSound(pl.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, -1.0f);
                    }
                    BGChat.printInfoChat(ChatColor.RED + "[Batalla Final] " + ChatColor.GREEN + "Todos seran enviados a la cornucopia en 1 minuto!");
                }
                if (BGMain.GAME_RUNNING_TIME == BGMain.END_GAME_TIME && BGMain.END_GAME) {
                    BGMain.END_GAME = false;
                    BGFBattle.createBattle();
                    for (final Player p : BGMain.getGamers()) {
                        for (final PotionEffect pe : p.getActivePotionEffects()) {
                            p.removePotionEffect(pe.getType());
                        }
                        Util.sendTitle(p, 20, 60, 40, ChatColor.DARK_RED + "Batalla Final", ChatColor.WHITE + "El mejor equipado sobrevivira");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 9999, 0));
                    }
                    new EndGameTimer();
                }
                if (BGMain.GAME_RUNNING_TIME == BGMain.MAX_GAME_RUNNING_TIME - 1) {
                    BGChat.printInfoChat(ChatColor.RED + "[Batalla final] " + ChatColor.GREEN + "1 minuto restante.");
                }
                if (BGMain.GAME_RUNNING_TIME >= BGMain.MAX_GAME_RUNNING_TIME) {
                    for (final Player p : BGMain.getGamers()) {
                        for (final PotionEffect pe : p.getActivePotionEffects()) {
                            p.removePotionEffect(pe.getType());
                        }
                        Util.sendTitle(p, 20, 60, 40, ChatColor.DARK_RED + "Eliminando Atributos", ChatColor.WHITE + "El mejor equipado sobrevivira");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 9999, 1));
                    }
                }
            }
        }, 0L, 1200L);
    }
    
    public static void cancel() {
        if (GameTimer.shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask((int)GameTimer.shed_id);
            GameTimer.shed_id = null;
        }
    }
}
