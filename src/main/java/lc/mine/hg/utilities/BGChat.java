package lc.mine.hg.utilities;

import lc.mine.hg.main.BGMain;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.configuration.*;

public class BGChat
{
    static Integer TIP_COUNT;
    static List<String> TIPS;
    
    static {
        BGChat.TIP_COUNT = 0;
        BGChat.TIPS = new ArrayList<String>();
    }
    
    public BGChat() {
        final List<String> tiplist = (List<String>)BGFiles.config.getStringList("TIPS");
        for (final String tip : tiplist) {
            BGChat.TIPS.add(tip);
        }
    }
    
    public static void printInfoChat(final String text) {
        broadcast(ChatColor.DARK_GREEN + text);
    }
    
    public static void printDeathChat(final String text) {
        broadcast(ChatColor.RED + text);
    }
    
    public static void printTimeChat(final String text) {
        broadcast(ChatColor.GREEN + text);
    }
    
    public static void printPlayerChat(final Player player, final String text) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + text));
    }
    
    private static void broadcast(final String msg) {
        for (final Player Online : Bukkit.getOnlinePlayers()) {
            Online.sendMessage(msg);
        }
    }
    
    public static void printHelpChat(final Player player) {
        printPlayerChat(player, BGMain.SERVER_TITLE);
        String are = "Hay";
        String players = "jugadores";
        if (BGMain.getGamers().size() == 1) {
            are = "Hay";
            players = "jugador";
        }
        final Integer timeleft = BGMain.MAX_GAME_RUNNING_TIME - BGMain.GAME_RUNNING_TIME;
        String is = "Faltan";
        String minute = "minutos";
        if (timeleft <= 1) {
            is = "Falta";
            minute = "minutos";
        }
        player.sendMessage(ChatColor.GRAY + " - " + are + " " + BGMain.getGamers().size() + " " + players + " conectados.");
        player.sendMessage(ChatColor.GRAY + " - " + is + " " + timeleft + " " + minute + " para terminar el juego.");
        if (BGMain.HELP_MESSAGE != null && BGMain.HELP_MESSAGE != "") {
            player.sendMessage(ChatColor.GRAY + " - " + BGMain.HELP_MESSAGE);
        }
    }
    
    public static void printKitChat(final Player player) {
        final Set<String> kits = (Set<String>)BGFiles.kitconf.getKeys(false);
        Integer invsize = 9;
        for (int i = 0; i <= 10; ++i) {
            if (i * 9 >= kits.size()) {
                invsize += i * 9;
                break;
            }
        }
        final Player pl = player;
        final IconMenu menu = new IconMenu("Selecciona un KIT", invsize, (IconMenu.OptionClickEventHandler)new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(final IconMenu.OptionClickEvent event) {
                BGKit.setKit(pl, ChatColor.stripColor(event.getName()));
                event.setWillClose(true);
                event.setWillDestroy(false);
            }
        }, (Plugin)BGMain.instance, true);
        Integer mypos = 0;
        Integer othpos = 1;
        for (String kitname : kits) {
            try {
                if (kitname.equalsIgnoreCase("default")) {
                    continue;
                }
                final char[] stringArray = kitname.toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                kitname = new String(stringArray);
                final ArrayList<String> container = new ArrayList<String>();
                final ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname.toLowerCase());
                final List<String> kititems = (List<String>)kit.getStringList("ITEMS");
                for (final String item : kititems) {
                    final String[] oneitem = item.split(",");
                    String itemstring = null;
                    Integer id = null;
                    Integer amount = null;
                    String enchantment = null;
                    String ench_numb = null;
                    if (oneitem[0].contains(":")) {
                        final String[] ITEM_ID = oneitem[0].split(":");
                        id = Integer.parseInt(ITEM_ID[0]);
                        amount = Integer.parseInt(oneitem[1]);
                    }
                    else {
                        id = Integer.parseInt(oneitem[0]);
                        amount = Integer.parseInt(oneitem[1]);
                    }
                    itemstring = " - " + amount + "x " + Material.getMaterial((int)id).toString().replace("_", " ").toLowerCase();
                    if (oneitem.length == 4) {
                        enchantment = Enchantment.getById(Integer.parseInt(oneitem[2])).getName().toLowerCase();
                        ench_numb = oneitem[3];
                        itemstring = String.valueOf(itemstring) + " with " + enchantment + " " + ench_numb;
                    }
                    container.add(ChatColor.GRAY + itemstring);
                }
                final List<String> pots = (List<String>)kit.getStringList("POTION");
                for (final String pot : pots) {
                    if ((pot != null & pot != "") && !pot.equals(0)) {
                        final String[] potion = pot.split(",");
                        if (Integer.parseInt(potion[0]) == 0) {
                            continue;
                        }
                        final PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
                        String name = pt.getName();
                        if (Integer.parseInt(potion[1]) == 0) {
                            name = String.valueOf(name) + " (Duracion: Infinita)";
                        }
                        else {
                            name = String.valueOf(name) + " (Duracion: " + potion[1] + " seg)";
                        }
                        container.add(ChatColor.AQUA + " * " + name);
                    }
                }
                final List<Integer> abils = (List<Integer>)kit.getIntegerList("ABILITY");
                for (final Integer abil : abils) {
                    final String desc = BGKit.getAbilityDesc((int)abil);
                    if (desc != null) {
                        container.add(ChatColor.LIGHT_PURPLE + " + " + desc);
                    }
                }
                if (!kit.getString("PERMS").equalsIgnoreCase("default")) {
                    container.add(" ");
                    String pref = "";
                    final String string;
                    switch (string = kit.getString("PERMS")) {
                        case "VIP": {
                            pref = "&b&lVIP";
                            break;
                        }
                        case "RUBY": {
                            pref = "&c&lRUBY";
                            break;
                        }
                        case "SVIP": {
                            pref = "&a&lSVIP";
                            break;
                        }
                        case "ELITE": {
                            pref = "&6&lELITE";
                            break;
                        }
                        default:
                            break;
                    }
                    container.add(ChatColor.translateAlternateColorCodes('&', "&eExclusivo para " + pref));
                }
                final Integer itemid = kit.getInt("ITEMMENU");
                final Material kitem = Material.getMaterial((int)itemid);
                final String perms = kit.getString("PERMS");
                boolean hasperms = false;
                if (perms.equalsIgnoreCase("VIP") && player.hasPermission("chg.vip")) {
                    hasperms = true;
                }
                else if (perms.equalsIgnoreCase("SVIP") && player.hasPermission("chg.svip")) {
                    hasperms = true;
                }
                else if (perms.equalsIgnoreCase("ELITE") && player.hasPermission("chg.elite")) {
                    hasperms = true;
                }
                else if (perms.equalsIgnoreCase("RUBY") && player.hasPermission("chg.ruby")) {
                    hasperms = true;
                }
                else if (perms.equalsIgnoreCase("default")) {
                    hasperms = true;
                }
                if (hasperms) {
                    String[] info = new String[container.size()];
                    info = container.toArray(info);
                    menu.setOption(mypos, new ItemStack(kitem, 1), ChatColor.GREEN + kitname, info);
                    ++mypos;
                }
                else {
                    String[] info = new String[container.size()];
                    info = container.toArray(info);
                    menu.setOption(invsize - othpos, new ItemStack(kitem, 1), ChatColor.RED + kitname, info);
                    ++othpos;
                }
                container.clear();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        menu.open(player);
    }
    
    public static void printKitInfo(final Player player, String kitname) {
        String kitinfoname = kitname;
        kitname = kitname.toLowerCase();
        final ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname);
        if (kit == null || !BGKit.isKit(kitname)) {
            printPlayerChat(player, "Ese kit no existe, para ver los kits usa: /kit");
            return;
        }
        final char[] stringArray = kitinfoname.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        kitinfoname = new String(stringArray);
        player.sendMessage(ChatColor.GREEN + kitinfoname + " Kit incluye:");
        final List<String> kititems = (List<String>)kit.getStringList("ITEMS");
        for (final String item : kititems) {
            final String[] oneitem = item.split(",");
            String itemstring = null;
            Integer id = null;
            Integer amount = null;
            String enchantment = null;
            String ench_numb = null;
            if (oneitem[0].contains(":")) {
                final String[] ITEM_ID = oneitem[0].split(":");
                id = Integer.parseInt(ITEM_ID[0]);
                amount = Integer.parseInt(oneitem[1]);
            }
            else {
                id = Integer.parseInt(oneitem[0]);
                amount = Integer.parseInt(oneitem[1]);
            }
            itemstring = " - " + amount + "x " + Material.getMaterial((int)id).toString().replace("_", " ").toLowerCase();
            if (oneitem.length == 4) {
                enchantment = Enchantment.getById(Integer.parseInt(oneitem[2])).getName().toLowerCase();
                ench_numb = oneitem[3];
                itemstring = String.valueOf(itemstring) + " with " + enchantment + " " + ench_numb;
            }
            player.sendMessage(ChatColor.GRAY + itemstring);
        }
        final List<String> pots = (List<String>)kit.getStringList("POTION");
        for (final String pot : pots) {
            if ((pot != null & pot != "") && !pot.equals(0)) {
                final String[] potion = pot.split(",");
                if (Integer.parseInt(potion[0]) == 0) {
                    continue;
                }
                final PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
                String name = pt.getName();
                if (Integer.parseInt(potion[1]) == 0) {
                    name = String.valueOf(name) + " (Duracion: Infinita)";
                }
                else {
                    name = String.valueOf(name) + " (Duracion: " + potion[1] + " seg)";
                }
                player.sendMessage(ChatColor.AQUA + " * " + name);
            }
        }
        final List<Integer> abils = (List<Integer>)kit.getIntegerList("ABILITY");
        for (final Integer abil : abils) {
            final String desc = BGKit.getAbilityDesc((int)abil);
            if (desc != null) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + " + " + desc);
            }
        }
    }
    
    public static void printTipChat() {
        if (BGChat.TIPS.size() - 1 < BGChat.TIP_COUNT) {
            BGChat.TIP_COUNT = 0;
        }
        final String tip = BGChat.TIPS.get(BGChat.TIP_COUNT);
        ++BGChat.TIP_COUNT;
        if (tip != "" || tip != null) {
            broadcast(ChatColor.GRAY + "[" + ChatColor.RED + "MineLC" + ChatColor.GRAY + "] " + ChatColor.GREEN + tip);
        }
    }
}
