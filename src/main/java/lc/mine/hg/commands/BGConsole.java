package lc.mine.hg.commands;

import lc.mine.hg.main.BGMain;
import lc.mine.hg.timers.EndGameTimer;
import lc.mine.hg.utilities.BGChat;
import lc.mine.hg.utilities.BGFBattle;
import lc.mine.hg.utilities.enums.GameState;
import lc.mine.hg.utilities.enums.Translation;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class BGConsole implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("start")) {
            if (p.hasPermission("chg.forcestart")) {
                if (BGMain.GAMESTATE != GameState.PREGAME) {
                    msg(p, sender, ChatColor.RED + Translation.GAME_BEGUN.t());
                }
                else {
                    BGMain.startgame();
                }
            }
            else {
                msg(p, sender, ChatColor.RED + Translation.NO_PERMISSION.t());
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("settimeout")) {
            final int time = Integer.parseInt(args[0]);
            BGMain.COUNTDOWN = time * 20;
            msg(null, sender, ChatColor.YELLOW + "Tiempo actualizado a " + time + "segundos");
            return true;
        }
        if (!cmd.getName().equalsIgnoreCase("fbattle")) {
            return true;
        }
        if (!p.hasPermission("chg.forcefinalbattle")) {
            BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
            return true;
        }
        if (BGMain.GAMESTATE != GameState.GAME) {
            BGChat.printPlayerChat(p, "El juego no ha comenzado!");
            return true;
        }
        if (BGMain.END_GAME) {
            BGMain.END_GAME = false;
            BGFBattle.createBattle();
            new EndGameTimer();
            return true;
        }
        BGChat.printPlayerChat(p, ChatColor.RED + "No tienes permiso!");
        return true;
    }
    
    private static void msg(final Player p, final CommandSender s, final String msg) {
        if (p == null) {
            s.sendMessage(msg);
        }
        else {
            BGChat.printPlayerChat(p, msg);
        }
    }
}
