package lc.mine.hg.timers;

import lc.mine.hg.main.BGMain;
import lc.mine.hg.utilities.BGChat;
import lc.mine.hg.utilities.BGFBattle;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class EndGameTimer
{
    private static Integer shed_id;
    
    static {
        EndGameTimer.shed_id = null;
    }
    
    public EndGameTimer() {
        EndGameTimer.shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) BGMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                final World w = Bukkit.getWorlds().get(0);
                w.setDifficulty(Difficulty.HARD);
                w.strikeLightning(BGMain.spawn.clone().add(0.0, 50.0, 0.0));
                BGChat.printInfoChat(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Batalla Final").toString());
                BGChat.printInfoChat("Teletransportando al spawn.");
                BGMain.log.info("Game phase: 4 - Final");
                BGFBattle.teleportGamers(BGMain.getGamers());
            }
        }, 0L, 1200L);
    }
    
    public static void cancel() {
        if (EndGameTimer.shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask((int)EndGameTimer.shed_id);
            EndGameTimer.shed_id = null;
        }
    }
}
