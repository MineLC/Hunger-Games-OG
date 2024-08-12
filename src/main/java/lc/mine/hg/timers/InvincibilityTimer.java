package lc.mine.hg.timers;

import lc.mine.hg.main.BGMain;
import lc.mine.hg.utilities.BGChat;
import lc.mine.hg.utilities.enums.GameState;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class InvincibilityTimer
{
    private static Integer shed_id;
    
    static {
        InvincibilityTimer.shed_id = null;
    }
    
    public InvincibilityTimer() {
        InvincibilityTimer.shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) BGMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (BGMain.FINAL_COUNTDOWN > 0) {
                    if (BGMain.FINAL_COUNTDOWN >= 10 & BGMain.FINAL_COUNTDOWN % 10 == 0) {
                        BGChat.printTimeChat("La invencibilidad termina en " + BGMain.TIME(BGMain.FINAL_COUNTDOWN) + ".");
                    }
                    else if (BGMain.FINAL_COUNTDOWN < 10) {
                        BGChat.printTimeChat("La invencibilidad termina en " + BGMain.TIME(BGMain.FINAL_COUNTDOWN) + ".");
                        for (final Player pl : BGMain.getGamers()) {
                            pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 2.0f, 2.0f);
                        }
                    }
                    --BGMain.FINAL_COUNTDOWN;
                }
                else {
                    BGChat.printTimeChat("");
                    BGChat.printTimeChat("La invencibilidad ha terminado.");
                    BGMain.log.info("Game phase: 3 - Fighting");
                    for (final Player pl : BGMain.getGamers()) {
                        pl.playSound(pl.getLocation(), Sound.ANVIL_LAND, 1.0f, 2.0f);
                    }
                    BGChat.printTipChat();
                    BGMain.spawn.getWorld().setAutoSave(true);
                    BGMain.GAMESTATE = GameState.GAME;
                    new GameTimer();
                    InvincibilityTimer.cancel();
                }
            }
        }, 20L, 20L);
    }
    
    public static void cancel() {
        if (InvincibilityTimer.shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask((int)InvincibilityTimer.shed_id);
            InvincibilityTimer.shed_id = null;
        }
    }
}
