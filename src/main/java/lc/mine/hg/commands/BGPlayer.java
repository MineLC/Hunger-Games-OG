package lc.mine.hg.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import lc.mine.hg.data.user.User;
import lc.mine.hg.main.BGMain;
import lc.mine.hg.utilities.BGChat;
import lc.mine.hg.utilities.BGKit;
import lc.mine.hg.utilities.BGTeam;
import lc.mine.hg.utilities.enums.GameState;
import lc.mine.hg.utilities.enums.Translation;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.plugin.*;
import org.bukkit.*;

public class BGPlayer implements CommandExecutor
{
    final BGMain plugin;
    public BGPlayer(final BGMain plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player p = (Player)sender;
        final User jug = BGMain.database.getCached(p.getUniqueId());
        if (cmd.getName().equalsIgnoreCase("help")) {
            BGChat.printHelpChat(p);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("rank")) {
            final String rango = jug.getRank();
            if (!BGMain.Fame.containsKey(p)) {
                BGMain.Fame.put(p, 0);
            }
            final int fame = jug.getFame();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6============================"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Rango: &a" + rango));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Fama: &a" + fame));
            int faltante = 0;
            if (rango.equalsIgnoreCase("Nuevo")) {
                faltante = 25 - fame;
            } else if (rango.equalsIgnoreCase("Aprendiz")) {
                faltante = 100 - fame;
            } else if (rango.equalsIgnoreCase("Heroe")) {
                faltante = 200 - fame;
            } else if (rango.equalsIgnoreCase("Feroz")) {
                faltante = 300 - fame;
            } else if (rango.equalsIgnoreCase("Poderoso")) {
                faltante = 500 - fame;
            } else if (rango.equalsIgnoreCase("Mortal")) {
                faltante = 1000 - fame;
            } else if (rango.equalsIgnoreCase("Terrorifico")) {
                faltante = 2000 - fame;
            } else if (rango.equalsIgnoreCase("Conquistador")) {
                faltante = 4000 - fame;
            } else if (rango.equalsIgnoreCase("Renombrado")) {
                faltante = 6000 - fame;
            } else if (rango.equalsIgnoreCase("Ilustre")) {
                faltante = 8000 - fame;
            } else if (rango.equalsIgnoreCase("Eminente")) {
                faltante = 10000 - fame;
            } else if (rango.equalsIgnoreCase("Rey héroe")) {
                faltante = 20000 - fame;
            } else if (rango.equalsIgnoreCase("Emperador")) {
                faltante = 100000 - fame;
            } else if (rango.equalsIgnoreCase("Legendario")) {
                faltante = 200000 - fame;
            }
            if (!rango.equalsIgnoreCase("Mitico")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6RankUP: &a" + faltante + " Fame"));
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6RankUP: &aRango Máximo."));
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6============================"));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("gamemaker")) {
            if (!p.hasPermission("chg.gamemaker")) {
                BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
                return true;
            }
            if (BGMain.isSpectator(p)) {
                BGMain.remSpectator(p);
                BGChat.printPlayerChat(p, ChatColor.RED + "Ya no eres Espectador!");
                p.setGameMode(GameMode.CREATIVE);
                return true;
            }
            BGMain.addSpectator(p);
            p.setGameMode(GameMode.SPECTATOR);
            BGChat.printPlayerChat(p, ChatColor.RED + "Ahora eres Espectador!");
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("vanish")) {
            if (p.hasPermission("chg.vanish")) {
                if (!BGMain.isSpectator(p)) {
                    BGMain.spectators.add(p);
                }
                if (BGMain.gamers.contains(p)) {
                    BGMain.gamers.remove(p);
                }
                if (p.getGameMode() == GameMode.SURVIVAL) {
                    p.setGameMode(GameMode.CREATIVE);
                    for (final Player Online : Bukkit.getOnlinePlayers()) {
                        Online.hidePlayer(p);
                    }
                    BGChat.printPlayerChat(p, ChatColor.RED + "Ahora eres invisible!");
                }
                else {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    for (final Player Online : Bukkit.getOnlinePlayers()) {
                        Online.showPlayer(p);
                    }
                    BGChat.printPlayerChat(p, ChatColor.GREEN + "Ahora eres visible!");
                }
                return true;
            }
            BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("kitinfo")) {
            if (args.length != 1) {
                return false;
            }
            BGChat.printKitInfo(p, args[0]);
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("kit")) {
            if (BGMain.GAMESTATE != GameState.PREGAME) {
                BGChat.printPlayerChat(p, ChatColor.RED + Translation.GAME_BEGUN.t());
                return true;
            }
            if (args.length != 1) {
                BGChat.printKitChat(p);
                return true;
            }
            BGKit.setKit(p, args[0]);
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (BGMain.GAMESTATE != GameState.PREGAME && !BGMain.isSpectator(p)) {
                BGChat.printPlayerChat(p, Translation.GAME_BEGUN.t());
                return true;
            }
            p.teleport(BGMain.getSpawn());
            BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TELEPORTED_SPAWN.t());
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("desbug")) {
            if (BGMain.GAMESTATE != GameState.INVINCIBILITY) {
                BGChat.printPlayerChat(p, Translation.GAME_BEGUN.t());
                return true;
            }
            p.teleport((Entity)p);
            BGChat.printPlayerChat(p, ChatColor.GOLD + "Desbugeado!");
            return true;
        }
        else {
            if (cmd.getName().equalsIgnoreCase("settimeout") && sender.hasPermission("minelc.settimeout")) {
                final int time = Integer.parseInt(args[0]);
                BGMain.COUNTDOWN = time * 20;
                BGChat.printPlayerChat(p, ChatColor.YELLOW + "Tiempo actualizado a " + time + "segundos");
                return true;
            }
            if (cmd.getName().equalsIgnoreCase("team")) {
                if (args.length > 2) {
                    return false;
                }
                if (args.length == 0) {
                    BGChat.printPlayerChat(p, ChatColor.YELLOW + Translation.TEAM_FUNC_CMDS.t());
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (args.length < 2) {
                        return false;
                    }
                    if (Bukkit.getServer().getPlayer(args[1]) == null) {
                        BGChat.printPlayerChat(p, ChatColor.RED + Translation.PLAYER_NOT_ONLINE.t());
                        return true;
                    }
                    final Player player = Bukkit.getServer().getPlayer(args[1]);
                    if (BGTeam.isInTeam(p, player.getName())) {
                        BGChat.printPlayerChat(p, ChatColor.RED + Translation.TEAM_FUNC_PLAYER_ALREADY_TEAM.t());
                        return true;
                    }
                    BGTeam.addMember(p, player.getName());
                    BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TEAM_FUNC_ADDED_PLAYER.t());
                    return true;
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length < 2) {
                        return false;
                    }
                    if (!BGTeam.isInTeam(p, args[1])) {
                        BGChat.printPlayerChat(p, ChatColor.RED + Translation.TEAM_FUNC_PLAYER_ALREADY_TEAM.t());
                        return true;
                    }
                    BGTeam.removeMember(p, args[1]);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TEAM_FUNC_REMOVED_PLAYER.t());
                    return true;
                }
                else if (args[0].equalsIgnoreCase("list")) {
                    if (args.length < 1 || args.length > 1) {
                        return false;
                    }
                    String text = ChatColor.YELLOW + Translation.TEAM_FUNC_YOUR_TEAM.t();
                    if (BGTeam.getTeamList(p).size() == 0) {
                        text = String.valueOf(text) + " Nobody";
                        BGChat.printPlayerChat(p, text);
                        return true;
                    }
                    for (final String t : BGTeam.getTeamList(p)) {
                        text = String.valueOf(text) + " " + t;
                    }
                    BGChat.printPlayerChat(p, text);
                    return true;
                }
            }
            if (cmd.getName().equalsIgnoreCase("hack")) {
                if (BGMain.isSpectator(p)) {
                    try {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setAllowFlight(true);
                        p.setFlying(true);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 1));
                        p.sendMessage(ChatColor.GREEN + "Ahora eres visible durante un momento..");
                        if (p.hasPermission("chg.hack")) {
                            Bukkit.getScheduler().runTaskLater((Plugin)BGMain.instance, (Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    p.setGameMode(GameMode.SPECTATOR);
                                    p.setAllowFlight(true);
                                    p.setFlying(true);
                                    p.setFlySpeed(0.2f);
                                    p.sendMessage(ChatColor.YELLOW + "Ahora vuelves a ser invisible.");
                                }
                            }, 4L);
                        }
                        else {
                            Bukkit.getScheduler().runTaskLater((Plugin)BGMain.instance, (Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    p.setGameMode(GameMode.SPECTATOR);
                                    p.setAllowFlight(true);
                                    p.setFlying(true);
                                    p.setFlySpeed(0.2f);
                                    p.sendMessage(ChatColor.YELLOW + "Ahora vuelves a ser invisible.");
                                }
                            }, 2L);
                        }
                    }
                    catch (Exception ex) {
                        p.setGameMode(GameMode.SPECTATOR);
                        ex.printStackTrace();
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "Comando solo para espectadores");
                }
            }
            if (cmd.getName().equalsIgnoreCase("teleport")) {
                if (!BGMain.isSpectator(p)) {
                    BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
                    return true;
                }
                if (args.length > 2) {
                    return false;
                }
                if (args.length == 0) {
                    BGChat.printPlayerChat(p, ChatColor.YELLOW + Translation.TELEPORT_FUNC_CMDS.t());
                    return true;
                }
                if (args.length == 1) {
                    if (Bukkit.getServer().getPlayer(args[0]) == null) {
                        BGChat.printPlayerChat(p, ChatColor.RED + Translation.PLAYER_NOT_ONLINE.t());
                        return true;
                    }
                    final Player target = Bukkit.getServer().getPlayer(args[0]);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TELEPORT_FUNC_TELEPORTED_PLAYER.t().replace("<player>", target.getName()));
                    p.teleport((Entity)target);
                    return true;
                }
                else if (args.length == 2) {
                    int x = 0;
                    int z = 0;
                    try {
                        x = Integer.parseInt(args[0]);
                        z = Integer.parseInt(args[1]);
                    }
                    catch (NumberFormatException e) {
                        BGChat.printPlayerChat(p, ChatColor.RED + Translation.TELEPORT_FUNC_COORDS_NOT_VALID.t());
                        return true;
                    }
                    final Location loc = new Location(BGMain.mainWorld, (double)x, BGMain.mainWorld.getHighestBlockYAt(x, z) + 1.5, (double)z);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TELEPORT_FUNC_TELEPORTED_COORDS.t().replace("<x>", new StringBuilder(String.valueOf(x)).toString()).replace("<z>", new StringBuilder(String.valueOf(z)).toString()));
                    p.teleport(loc);
                    return true;
                }
            }
            if (cmd.getName().equalsIgnoreCase("lobby")) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("lobby");
                p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
            return true;
        }
    }
}
