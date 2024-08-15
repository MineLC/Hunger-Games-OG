package lc.mine.hg.utilities;

import lc.mine.hg.data.user.User;
import lc.mine.hg.main.BGMain;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.enchantments.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.configuration.*;
import java.util.*;
import org.bukkit.*;

public class BGKit
{
    public static ArrayList<String> kits;
    private static HashMap<Integer, String> ABILITY_DESC;
    private static ItemStack compass;
    private static BGMain plugin;

    public BGKit(BGMain plugin) {
        BGKit.plugin = plugin;
        kits = new ArrayList<String>();
        ABILITY_DESC = new HashMap<Integer, String>();
        compass = new ItemUtils(Material.COMPASS, (short)0, 1, ChatColor.AQUA + "Rastreador" + ChatColor.BOLD, ChatColor.GRAY + "Click para rastrar al jugador mas cercano");
        final Set<String> kitList = (Set<String>)BGFiles.kitconf.getKeys(false);
        for (final String kit : kitList) {
            if (kit.equalsIgnoreCase("default")) {
                continue;
            }
            BGKit.kits.add(kit.toLowerCase());
        }
        BGKit.ABILITY_DESC.put(1, BGFiles.abconf.getString("AB.1.Desc"));
        BGKit.ABILITY_DESC.put(2, BGFiles.abconf.getString("AB.2.Desc"));
        BGKit.ABILITY_DESC.put(3, BGFiles.abconf.getString("AB.3.Desc"));
        BGKit.ABILITY_DESC.put(4, BGFiles.abconf.getString("AB.4.Desc"));
        BGKit.ABILITY_DESC.put(5, BGFiles.abconf.getString("AB.5.Desc"));
        BGKit.ABILITY_DESC.put(6, BGFiles.abconf.getString("AB.6.Desc"));
        BGKit.ABILITY_DESC.put(7, BGFiles.abconf.getString("AB.7.Desc"));
        BGKit.ABILITY_DESC.put(8, BGFiles.abconf.getString("AB.8.Desc"));
        BGKit.ABILITY_DESC.put(9, BGFiles.abconf.getString("AB.9.Desc"));
        BGKit.ABILITY_DESC.put(10, BGFiles.abconf.getString("AB.10.Desc"));
        BGKit.ABILITY_DESC.put(11, BGFiles.abconf.getString("AB.11.Desc"));
        BGKit.ABILITY_DESC.put(12, BGFiles.abconf.getString("AB.12.Desc"));
        BGKit.ABILITY_DESC.put(13, BGFiles.abconf.getString("AB.13.Desc"));
        BGKit.ABILITY_DESC.put(14, BGFiles.abconf.getString("AB.14.Desc"));
        BGKit.ABILITY_DESC.put(15, BGFiles.abconf.getString("AB.15.Desc"));
        BGKit.ABILITY_DESC.put(16, BGFiles.abconf.getString("AB.16.Desc"));
        BGKit.ABILITY_DESC.put(18, BGFiles.abconf.getString("AB.18.Desc"));
        BGKit.ABILITY_DESC.put(19, BGFiles.abconf.getString("AB.19.Desc"));
        BGKit.ABILITY_DESC.put(20, BGFiles.abconf.getString("AB.20.Desc"));
        BGKit.ABILITY_DESC.put(21, BGFiles.abconf.getString("AB.21.Desc"));
        BGKit.ABILITY_DESC.put(22, BGFiles.abconf.getString("AB.22.Desc"));
        BGKit.ABILITY_DESC.put(23, BGFiles.abconf.getString("AB.23.Desc"));
        BGKit.ABILITY_DESC.put(30, BGFiles.abconf.getString("AB.30.Desc"));
        BGKit.ABILITY_DESC.put(31, BGFiles.abconf.getString("AB.31.Desc"));
        BGKit.ABILITY_DESC.put(32, BGFiles.abconf.getString("AB.32.Desc"));
        BGKit.ABILITY_DESC.put(34, BGFiles.abconf.getString("AB.34.Desc"));
        BGKit.ABILITY_DESC.put(35, BGFiles.abconf.getString("AB.35.Desc"));
        BGKit.ABILITY_DESC.put(36, BGFiles.abconf.getString("AB.36.Desc"));
        BGKit.ABILITY_DESC.put(37, BGFiles.abconf.getString("AB.37.Desc"));
        BGKit.ABILITY_DESC.put(38, BGFiles.abconf.getString("AB.38.Desc"));
    }

    public static void giveKit(final Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet((ItemStack)null);
        p.getInventory().setChestplate((ItemStack)null);
        p.getInventory().setLeggings((ItemStack)null);
        p.getInventory().setBoots((ItemStack)null);
        p.setExp(0.0f);
        p.setLevel(0);
        p.setFoodLevel(20);
        p.setFlying(false);
        p.setAllowFlight(false);

        final User jug = BGMain.database.getCached(p.getUniqueId());
        final String perms = BGFiles.kitconf.getConfigurationSection(jug.getKit().toLowerCase()).getString("PERMS");
        boolean hasperms = false;
        if (perms.equalsIgnoreCase("VIP") && p.hasPermission("chg.vip")) {
            hasperms = true;
        }
        else if (perms.equalsIgnoreCase("SVIP") && p.hasPermission("chg.svip")) {
            hasperms = true;
        }
        else if (perms.equalsIgnoreCase("ELITE") && p.hasPermission("chg.elite")) {
            hasperms = true;
        }
        else if (perms.equalsIgnoreCase("RUBY") && p.hasPermission("chg.ruby")) {
            hasperms = true;
        }
        else if (perms.equalsIgnoreCase("default")) {
            hasperms = true;
        }
        if (!hasperms) {
            jug.setKit("default");
        }
        Bukkit.getConsoleSender().sendMessage("Jugador " + p.getName() + " recibio el kit " + jug.getKit().toLowerCase());
        if (!BGKit.kits.contains(jug.getKit().toLowerCase())) {
            p.getInventory().addItem(new ItemStack[] { BGKit.compass });
            if (BGMain.DEFAULT_KIT) {
                try {
                    final ConfigurationSection def = BGFiles.kitconf.getConfigurationSection("default");
                    final List<String> kititems = (List<String>)def.getStringList("ITEMS");
                    for (final String item : kititems) {
                        final String[] oneitem = item.split(",");
                        ItemStack i = null;
                        Integer id = null;
                        Integer amount = null;
                        Short durability = null;
                        if (oneitem[0].contains(":")) {
                            final String[] ITEM_ID = oneitem[0].split(":");
                            id = Integer.parseInt(ITEM_ID[0]);
                            amount = Integer.parseInt(oneitem[1]);
                            durability = Short.parseShort(ITEM_ID[1]);
                            i = new ItemStack((int)id, (int)amount, (short)durability);
                        }
                        else {
                            id = Integer.parseInt(oneitem[0]);
                            amount = Integer.parseInt(oneitem[1]);
                            i = new ItemStack((int)id, (int)amount);
                        }
                        if (oneitem.length == 4) {
                            i.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem[2])), Integer.parseInt(oneitem[3]));
                        }
                        if (id < 298 || 317 < id) {
                            p.getInventory().addItem(new ItemStack[] { i });
                        }
                        else if (id == 298 || id == 302 || id == 306 || id == 310 || id == 314) {
                            i.setAmount(1);
                            p.getInventory().setHelmet(i);
                        }
                        else if (id == 299 || id == 303 || id == 307 || id == 311 || id == 315) {
                            i.setAmount(1);
                            p.getInventory().setChestplate(i);
                        }
                        else if (id == 300 || id == 304 || id == 308 || id == 312 || id == 316) {
                            i.setAmount(1);
                            p.getInventory().setLeggings(i);
                        }
                        else {
                            if (id != 301 && id != 305 && id != 309 && id != 313 && id != 317) {
                                continue;
                            }
                            i.setAmount(1);
                            p.getInventory().setBoots(i);
                        }
                    }
                    final List<String> pots = (List<String>)def.getStringList("POTION");
                    for (final String pot : pots) {
                        if ((pot != null & pot != "") && !pot.equals(0)) {
                            final String[] potion = pot.split(",");
                            if (Integer.parseInt(potion[0]) == 0) {
                                continue;
                            }
                            if (Integer.parseInt(potion[1]) == 0) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.getById(Integer.parseInt(potion[0])), BGMain.MAX_GAME_RUNNING_TIME * 1200, Integer.parseInt(potion[2])));
                            }
                            else {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.getById(Integer.parseInt(potion[0])), Integer.parseInt(potion[1]) * 20, Integer.parseInt(potion[2])));
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        final String kitname = jug.getKit().toLowerCase();

        try {
            final ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname.toLowerCase());
            final List<String> kititems2 = (List<String>)kit.getStringList("ITEMS");
            for (final String item2 : kititems2) {
                final String[] oneitem2 = item2.split(",");
                ItemStack j = null;
                Integer id2 = null;
                Integer amount2 = null;
                Short durability2 = null;
                int blue = 0;
                int green = 0;
                int red = 0;
                if (item2.toLowerCase().contains(">")) {
                    final String[] color = item2.split(">");
                    if (color[1].toLowerCase().contains("blue")) {
                        blue = 255;
                        green = 0;
                        red = 0;
                    }
                    else if (color[1].toLowerCase().contains("green")) {
                        blue = 0;
                        green = 255;
                        red = 0;
                    }
                    else if (color[1].toLowerCase().contains("red")) {
                        blue = 0;
                        green = 0;
                        red = 255;
                    }
                    else if (color[1].toLowerCase().contains("black")) {
                        blue = 0;
                        green = 0;
                        red = 0;
                    }
                    else if (color[1].toLowerCase().contains("white")) {
                        blue = 255;
                        green = 255;
                        red = 255;
                    }
                }
                if (oneitem2[0].contains(":")) {
                    final String[] ITEM_ID2 = oneitem2[0].split(":");
                    id2 = Integer.parseInt(ITEM_ID2[0]);
                    amount2 = Integer.parseInt(oneitem2[1]);
                    durability2 = Short.parseShort(ITEM_ID2[1]);
                    j = new ItemStack((int)id2, (int)amount2, (short)durability2);
                }
                else {
                    id2 = Integer.parseInt(oneitem2[0]);
                    amount2 = Integer.parseInt(oneitem2[1]);
                    j = new ItemStack((int)id2, (int)amount2);
                }
                if (oneitem2.length == 4) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                }
                else if (oneitem2.length == 6) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[4])), Integer.parseInt(oneitem2[5]));
                }
                else if (oneitem2.length == 8) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[4])), Integer.parseInt(oneitem2[5]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[6])), Integer.parseInt(oneitem2[7]));
                }
                else if (oneitem2.length == 10) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[4])), Integer.parseInt(oneitem2[5]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[6])), Integer.parseInt(oneitem2[7]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[8])), Integer.parseInt(oneitem2[9]));
                }
                else if (oneitem2.length == 12) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[4])), Integer.parseInt(oneitem2[5]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[6])), Integer.parseInt(oneitem2[7]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[8])), Integer.parseInt(oneitem2[9]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[10])), Integer.parseInt(oneitem2[11]));
                }
                else if (oneitem2.length == 14) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[4])), Integer.parseInt(oneitem2[5]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[6])), Integer.parseInt(oneitem2[7]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[8])), Integer.parseInt(oneitem2[9]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[10])), Integer.parseInt(oneitem2[11]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[12])), Integer.parseInt(oneitem2[13]));
                }
                else if (oneitem2.length == 16) {
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[2])), Integer.parseInt(oneitem2[3]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[4])), Integer.parseInt(oneitem2[5]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[6])), Integer.parseInt(oneitem2[7]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[8])), Integer.parseInt(oneitem2[9]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[10])), Integer.parseInt(oneitem2[11]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[12])), Integer.parseInt(oneitem2[13]));
                    j.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem2[14])), Integer.parseInt(oneitem2[15]));
                }
                if (id2 < 298 || 317 < id2) {
                    p.getInventory().addItem(new ItemStack[] { j });
                }
                else if (id2 == 298 || id2 == 302 || id2 == 306 || id2 == 310 || id2 == 314) {
                    if (id2 == 298 && kitname.equalsIgnoreCase("spiderman")) {
                        final LeatherArmorMeta h = (LeatherArmorMeta)j.getItemMeta();
                        h.setColor(Color.RED);
                        j.setItemMeta((ItemMeta)h);
                    }
                    j.setAmount(1);
                    p.getInventory().setHelmet(j);
                }
                else if (id2 == 299 || id2 == 303 || id2 == 307 || id2 == 311 || id2 == 315) {
                    if (id2 == 299 && kitname.equalsIgnoreCase("spiderman")) {
                        final LeatherArmorMeta c = (LeatherArmorMeta)j.getItemMeta();
                        c.setColor(Color.BLUE);
                        j.setItemMeta((ItemMeta)c);
                    }
                    j.setAmount(1);
                    p.getInventory().setChestplate(j);
                }
                else if (id2 == 300 || id2 == 304 || id2 == 308 || id2 == 312 || id2 == 316) {
                    if (id2 == 300 && kitname.equalsIgnoreCase("spiderman")) {
                        final LeatherArmorMeta l = (LeatherArmorMeta)j.getItemMeta();
                        l.setColor(Color.RED);
                        j.setItemMeta((ItemMeta)l);
                    }
                    j.setAmount(1);
                    p.getInventory().setLeggings(j);
                }
                else {
                    if (id2 != 301 && id2 != 305 && id2 != 309 && id2 != 313 && id2 != 317) {
                        continue;
                    }
                    if (id2 == 301 && kitname.equalsIgnoreCase("spiderman")) {
                        final LeatherArmorMeta b = (LeatherArmorMeta)j.getItemMeta();
                        b.setColor(Color.BLUE);
                        j.setItemMeta((ItemMeta)b);
                    }
                    j.setAmount(1);
                    p.getInventory().setBoots(j);
                }
            }
            final List<String> pots2 = (List<String>)kit.getStringList("POTION");
            for (final String pot2 : pots2) {
                if ((pot2 != null & pot2 != "") && !pot2.equals(0)) {
                    final String[] potion2 = pot2.split(",");
                    if (Integer.parseInt(potion2[0]) == 0) {
                        continue;
                    }
                    if (Integer.parseInt(potion2[1]) == 0) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getById(Integer.parseInt(potion2[0])), BGMain.MAX_GAME_RUNNING_TIME * 1200, Integer.parseInt(potion2[2])));
                    }
                    else {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getById(Integer.parseInt(potion2[0])), Integer.parseInt(potion2[1]) * 20, Integer.parseInt(potion2[2])));
                    }
                }
            }
            p.getInventory().addItem(new ItemStack[] { BGKit.compass });
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }
    
    public static void setKit(final Player player, String kitname) {
        kitname = kitname.toLowerCase();
        kitname = kitname.replace(".", "");
        final ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname);
        if (kit == null && !BGKit.kits.contains(kitname)) {
            BGChat.printPlayerChat(player, ChatColor.RED + "El kit no existe!");
            return;
        }
        final User jug = BGMain.database.getCached(player.getUniqueId());
        if (jug.getKit().equalsIgnoreCase(kitname)) {
            player.sendMessage(ChatColor.RED + "Ya tienes seleccionado este kit!");
            return;
        }
        final String perms = BGFiles.kitconf.getConfigurationSection(kitname).getString("PERMS");
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
            jug.setKit(kitname);
            final char[] stringArray = kitname.toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            kitname = new String(stringArray);
            BGChat.printPlayerChat(player, ChatColor.GREEN + "Seleccionaste " + ChatColor.DARK_GREEN + ChatColor.ITALIC + kitname + ChatColor.RESET + ChatColor.GREEN + " como tu kit.");
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.5f);
            return;
        }
        BGChat.printPlayerChat(player, ChatColor.RED + BGMain.NO_KIT_MSG);
    }
    
    public static Boolean hasAbility(final Player player, final Integer ability) {
        if (BGMain.isSpectator(player)) {
            return false;
        }
        final User jug = BGMain.database.getCached(player.getUniqueId());

        if (BGKit.kits.contains(jug.getKit().toLowerCase())) {
            final String kitname = jug.getKit().toLowerCase();
            final ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname);
            final List<Integer> s = (List<Integer>)kit.getIntegerList("ABILITY");
            for (final Integer i : s) {
                if (i.equals(ability)) {
                    return true;
                }
            }
            return false;
        }
        if (BGMain.DEFAULT_KIT) {
            final ConfigurationSection def = BGFiles.kitconf.getConfigurationSection("default");
            final List<Integer> s2 = (List<Integer>)def.getIntegerList("ABILITY");
            for (final Integer j : s2) {
                if (j.equals(ability)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    public static boolean isKit(final String kitName) {
        return BGKit.kits.contains(kitName);
    }
    
    public static String getAbilityDesc(final Integer ability) {
        if (ability == 0) {
            return null;
        }
        if (BGKit.ABILITY_DESC.containsKey(ability)) {
            return BGKit.ABILITY_DESC.get(ability);
        }
        return null;
    }
    
    public static void setAbilityDesc(final Integer ability, final String description) throws Error {
        if (BGKit.ABILITY_DESC.containsKey(ability)) {
            throw new Error("No hay descripcion.");
        }
        BGKit.ABILITY_DESC.put(ability, description);
    }
}
