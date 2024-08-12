package lc.mine.hg.timers;

import lc.mine.hg.main.BGMain;
import lc.mine.hg.utilities.BGChat;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class PreGameTimer
{
    private static Integer shed_id;
    public static boolean started;
    
    static {
        PreGameTimer.shed_id = null;
        PreGameTimer.started = false;
    }
    
    public PreGameTimer() {
        PreGameTimer.started = true;
        PreGameTimer.shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) BGMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (BGMain.COUNTDOWN > 0) {
                    if (BGMain.COUNTDOWN >= 30 & BGMain.COUNTDOWN % 10 == 0) {
                        BGChat.printTimeChat("El juego comienza en " + BGMain.TIME(BGMain.COUNTDOWN) + ".");
                        for (final Player pl : BGMain.getGamers()) {
                            if (BGMain.getGamers().size() >= Bukkit.getServer().getMaxPlayers()) {
                                BGMain.COUNTDOWN = 26;
                                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
                            }
                        }
                    }
                    else if (BGMain.COUNTDOWN <= 25 && (BGMain.COUNTDOWN > 10 & BGMain.COUNTDOWN % 5 == 0)) {
                        BGChat.printTimeChat("El juego comienza en " + BGMain.TIME(BGMain.COUNTDOWN) + ".");
                    }
                    else if (BGMain.COUNTDOWN <= 10 && BGMain.COUNTDOWN > 3) {
                        BGChat.printTimeChat("El juego comienza en " + BGMain.TIME(BGMain.COUNTDOWN) + ".");
                    }
                    else if (BGMain.COUNTDOWN <= 3) {
                        BGChat.printTimeChat("El juego comienza en " + BGMain.TIME(BGMain.COUNTDOWN) + ".");
                        for (final Player pl : BGMain.getGamers()) {
                            pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1.0f, -1.0f);
                        }
                    }
                    --BGMain.COUNTDOWN;
                }
                else if (BGMain.getGamers().size() < BGMain.MINIMUM_PLAYERS) {
                    BGChat.printTimeChat("Esperando mas jugadores..");
                    BGMain.COUNTDOWN = BGMain.COUNTDOWN_SECONDS;
                }
                else {
                    BGMain.startgame();
                }
            }
        }, 0L, 20L);
    }
    
    public static void cancel() {
        if (PreGameTimer.shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask((int)PreGameTimer.shed_id);
            PreGameTimer.shed_id = null;
        }
    }
}
