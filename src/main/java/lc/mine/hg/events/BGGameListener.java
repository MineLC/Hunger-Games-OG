package lc.mine.hg.events;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lc.mine.hg.data.temporal.User;
import lc.mine.hg.main.BGMain;
import lc.mine.hg.timers.PreGameTimer;
import lc.mine.hg.utilities.*;
import lc.mine.hg.utilities.enums.GameState;
import lc.mine.hg.utilities.enums.Translation;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import java.text.*;
import org.bukkit.event.server.*;
import org.apache.commons.lang.*;
import org.bukkit.command.*;
import org.bukkit.event.block.*;
import org.bukkit.scoreboard.*;
import org.bukkit.projectiles.*;
import org.bukkit.potion.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.util.Vector;

public class BGGameListener implements Listener
{
    private static BGMain bgMain;
    private static ItemStack stats_item;
    private static ItemStack kit_item;
    private static ItemStack book_item;
    private static ItemStack vippoints_item;
    private static IconMenu invStats_CHG;
    private static IconMenu invStats;
    private static IconMenu invVipPoints;
    private static IconMenu invStats_CHG_kills;
    private static IconMenu invStats_CHG_deaths;
    private static IconMenu invStats_CHG_part_ganadas;
    private static IconMenu invStats_CHG_part_jugadas;
    private static IconMenu invStats_CHG_lvl;
    
    static {
        BGGameListener.stats_item = new ItemUtils(Material.PAPER, (short) 0, 1, ChatColor.GREEN + "TOP Jugadores", ChatColor.GRAY + "Click derecho para ver el top de jugadores");
        BGGameListener.kit_item = new ItemUtils(Material.BOW, (short) 0, 1, ChatColor.GREEN + "Selector De Kit", ChatColor.GRAY + "Click derecho para abrir el menu de kits");
        BGGameListener.vippoints_item = new ItemUtils(Material.GOLD_INGOT, (short)0, 1, ChatColor.GREEN + "Men\u00fa de Vip Points", ChatColor.GRAY + "Click derecho para abrir el menu de vip points");
        BGGameListener.invStats_CHG = null;
        BGGameListener.invStats = null;
        BGGameListener.invVipPoints = null;
        BGGameListener.invStats_CHG_kills = null;
        BGGameListener.invStats_CHG_deaths = null;
        BGGameListener.invStats_CHG_part_ganadas = null;
        BGGameListener.invStats_CHG_part_jugadas = null;
        BGGameListener.invStats_CHG_lvl = null;
        BGGameListener.book_item = new ItemStack(Material.WRITTEN_BOOK);
        final List<String> pages = (List<String>) BGFiles.bookconf.getStringList("content");
        final List<String> content = new ArrayList<String>();
        final List<String> page = new ArrayList<String>();
        for (String line : pages) {
            line = line.replace("<server_title>", BGMain.SERVER_TITLE);
            line = line.replace("<space>", ChatColor.RESET + "\n");
            line = ChatColor.translateAlternateColorCodes('&', line);
            if (!line.contains("<newpage>")) {
                page.add(String.valueOf(line) + "\n");
            }
            else {
                String pagestr = "";
                for (final String l : page) {
                    pagestr = String.valueOf(pagestr) + l;
                }
                content.add(pagestr);
                page.clear();
            }
        }
        String pagestr2 = "";
        for (final String i : page) {
            pagestr2 = String.valueOf(pagestr2) + i;
        }
        content.add(pagestr2);
        page.clear();
        final BookMeta im = (BookMeta)BGGameListener.book_item.getItemMeta();
        im.setPages((List)content);
        im.setAuthor(BGFiles.bookconf.getString("author"));
        im.setTitle(BGFiles.bookconf.getString("title"));
        BGGameListener.book_item.setItemMeta((ItemMeta)im);
    }

    public BGGameListener(BGMain bgMain) {
        this.bgMain = bgMain;
    }

    private static IconMenu getInvStats_CHG_kills() {
        if (invStats_CHG_kills == null) {
            invStats_CHG_kills = new IconMenu("TOP Asesinatos - CHG", 45, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    if (e.getPosition() == 31) {
                        getInvStats_CHG().open(e.getPlayer());
                    }
                }
            }, (Plugin) BGMain.instance);

            final LinkedHashMap<String, Integer> top = bgMain.getTopManager().getTopKills();
            int slot = 0;
            for (final Map.Entry<String, Integer> es : top.entrySet()) {;

                    StringBuilder title = new StringBuilder();
                    StringBuilder description = new StringBuilder();

                    title.append(ChatColor.GOLD).append(ChatColor.BOLD).append("#").append(slot + 1).append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.RED).append(es.getKey());
                    description.append(ChatColor.GRAY).append(es.getValue()).append(" asesinatos");

                    invStats_CHG_kills.setOption(slot++, new ItemUtils(es.getKey(), 1, title.toString(), description.toString()));
            }
            invStats_CHG_kills.setOption(31, new ItemStack(Material.MAP), new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD).append("Regresar").toString(), new String[0]);
        }
        return invStats_CHG_kills;
    }

    private static IconMenu getInvStats_CHG_partidas_ganadas() {
        if (invStats_CHG_part_ganadas == null) {
            invStats_CHG_part_ganadas = new IconMenu("TOP Partidas Ganadas - CHG", 45, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    if (e.getPosition() == 31) {
                        getInvStats_CHG().open(e.getPlayer());
                    }
                }
            }, (Plugin) BGMain.instance);

            final LinkedHashMap<String, Integer> top = bgMain.getTopManager().getTopWins();
            int slot = 0;
            for (final Map.Entry<String, Integer> es : top.entrySet()) {
                    StringBuilder title = new StringBuilder();
                    StringBuilder description = new StringBuilder();

                    title.append(ChatColor.GOLD).append(ChatColor.BOLD).append("#").append(slot + 1).append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.RED).append(es.getKey());
                    description.append(ChatColor.GRAY).append(es.getValue()).append(" partidas ganadas");

                    invStats_CHG_part_ganadas.setOption(slot++, new ItemUtils(es.getKey(), 1, title.toString(), description.toString()));
            }
            invStats_CHG_part_ganadas.setOption(31, new ItemStack(Material.MAP), new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD).append("Regresar").toString(), new String[0]);
        }
        return invStats_CHG_part_ganadas;
    }

    private static IconMenu getInvStats_CHG_partidas_jugadas() {
        if (invStats_CHG_part_jugadas == null) {
            invStats_CHG_part_jugadas = new IconMenu("TOP Partidas Jugadas - CHG", 45, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    if (e.getPosition() == 31) {
                        getInvStats_CHG().open(e.getPlayer());
                    }
                }
            }, (Plugin) BGMain.instance);

            final LinkedHashMap<String, Integer> top = bgMain.getTopManager().getTopPlayedGames();
            int slot = 0;
            for (final Map.Entry<String, Integer> es : top.entrySet()) {
                    StringBuilder title = new StringBuilder();
                    StringBuilder description = new StringBuilder();

                    title.append(ChatColor.GOLD).append(ChatColor.BOLD).append("#").append(slot + 1).append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.RED).append(es.getKey());
                    description.append(ChatColor.GRAY).append(es.getValue()).append(" partidas jugadas");

                    invStats_CHG_part_jugadas.setOption(slot++, new ItemUtils(es.getKey(), 1, title.toString(), description.toString()));
            }
            invStats_CHG_part_jugadas.setOption(31, new ItemStack(Material.MAP), new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD).append("Regresar").toString(), new String[0]);
        }
        return invStats_CHG_part_jugadas;
    }

    private static IconMenu getInvStats_CHG_lvl() {
        if (invStats_CHG_lvl == null) {
            invStats_CHG_lvl = new IconMenu("TOP Nivel - CHG", 45, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    if (e.getPosition() == 31) {
                        getInvStats_CHG().open(e.getPlayer());
                    }
                }
            }, (Plugin) BGMain.instance);

            final LinkedHashMap<String, Integer> top = bgMain.getTopManager().getTopLevels();
            int slot = 0;
            for (final Map.Entry<String, Integer> es : top.entrySet()) {
                    StringBuilder title = new StringBuilder();
                    title.append(ChatColor.GOLD).append(ChatColor.BOLD).append("#").append(slot + 1).append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.RED).append(es.getKey());

                    String description = ChatColor.GRAY + "Nivel: " + es.getValue();

                    invStats_CHG_lvl.setOption(slot++, new ItemUtils(es.getKey(), 1, title.toString(), description));
            }
            invStats_CHG_lvl.setOption(31, new ItemStack(Material.MAP), new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD).append("Regresar").toString(), new String[0]);
        }
        return invStats_CHG_lvl;
    }

    private static IconMenu getInvStats_CHG_deaths() {
        if (invStats_CHG_deaths == null) {
            invStats_CHG_deaths = new IconMenu("TOP Muertes - CHG", 45, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    if (e.getPosition() == 31) {
                        getInvStats_CHG().open(e.getPlayer());
                    }
                }
            }, (Plugin) BGMain.instance);

            final LinkedHashMap<String, Integer> top = bgMain.getTopManager().getTopDeaths();
            int slot = 0;
            for (final Map.Entry<String, Integer> es : top.entrySet()) {
                    StringBuilder title = new StringBuilder();
                    title.append(ChatColor.GOLD).append(ChatColor.BOLD).append("#").append(slot + 1).append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.RED).append(es.getKey());

                    String description = ChatColor.GRAY + "" +es.getValue() + " muertes";

                    invStats_CHG_deaths.setOption(slot++, new ItemUtils(es.getKey(), 1, title.toString(), description));
            }
            invStats_CHG_deaths.setOption(31, new ItemStack(Material.MAP), new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD).append("Regresar").toString(), new String[0]);
        }
        return invStats_CHG_deaths;
    }
    
    private static IconMenu getInvStats_CHG() {
        if (BGGameListener.invStats_CHG == null) {
            (BGGameListener.invStats_CHG = new IconMenu("TOP Jugadores - CHG", 45, (IconMenu.OptionClickEventHandler)new IconMenu.OptionClickEventHandler() {
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    switch (e.getPosition()) {
                        case 19: {
                            getInvStats_CHG_kills().open(e.getPlayer());
                            break;
                        }
                        case 21: {
                            getInvStats_CHG_partidas_ganadas().open(e.getPlayer());
                            break;
                        }
                        case 23: {
                            getInvStats_CHG_partidas_jugadas().open(e.getPlayer());
                            break;
                        }
                        case 25: {
                            getInvStats_CHG_deaths().open(e.getPlayer());
                            break;
                        }
                    }
                }
            }, (Plugin)BGMain.instance)).setOption(19, new ItemStack(Material.SIGN), new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("Asesinatos   ").toString(), ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas asesinatos");
            BGGameListener.invStats_CHG.setOption(21, new ItemStack(Material.SIGN), new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("Partidas Ganadas").toString(), ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas partidas ganadas");
            BGGameListener.invStats_CHG.setOption(23, new ItemStack(Material.SIGN), new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("Partidas Jugadas").toString(), ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas partidas jugadas");
            BGGameListener.invStats_CHG.setOption(25, new ItemStack(Material.SIGN), new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("Muertes  ").toString(), ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas muertes");
        }
        return BGGameListener.invStats_CHG;
    }
    
    private static IconMenu getInv_VipPoints(final Player p) {
        User jug = bgMain.getUserManager().getUserById(p.getUniqueId());
        if (BGGameListener.invVipPoints == null) {
            (BGGameListener.invVipPoints = new IconMenu("Men\u00fa de Vip Points", 27, (IconMenu.OptionClickEventHandler)new IconMenu.OptionClickEventHandler() {
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    switch (e.getPosition()) {
                        case 9: {
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &b&lVIP");
                            BGChat.printPlayerChat(e.getPlayer(), "&bhttps://tienda.mine.lc");
                            break;
                        }
                        case 11: {
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &a&lSVIP");
                            BGChat.printPlayerChat(e.getPlayer(), "&ahttps://tienda.mine.lc");
                            break;
                        }
                        case 15: {
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &6&lELITE");
                            BGChat.printPlayerChat(e.getPlayer(), "&6https://tienda.mine.lc");
                            break;
                        }
                        case 17: {
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &c&lRUBY");
                            BGChat.printPlayerChat(e.getPlayer(), "&chttps://tienda.mine.lc");
                            break;
                        }
                    }
                }
            }, (Plugin)BGMain.instance)).setOption(9, new ItemStack(Material.IRON_INGOT), ChatColor.AQUA + "Rango " + ChatColor.BOLD + "VIP", ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "75" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "�Usa /lobby para reclamarlos!");
            BGGameListener.invVipPoints.setOption(11, new ItemStack(Material.GOLD_INGOT), ChatColor.GREEN + "Rango " + ChatColor.BOLD + "SVIP", ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "150" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "�Usa /lobby para reclamarlos!");
            final ItemStack skull = new ItemUtils(p.getName(), 1, "", "");
            BGGameListener.invVipPoints.setOption(13, skull, ChatColor.GOLD + "Vip Points", new StringBuilder().append(ChatColor.YELLOW).append(jug.getVipPoints()).toString());
            BGGameListener.invVipPoints.setOption(15, new ItemStack(Material.DIAMOND), ChatColor.GOLD + "Rango " + ChatColor.BOLD + "ELITE", ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "225" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "�Usa /lobby para reclamarlos!");
            BGGameListener.invVipPoints.setOption(17, new ItemStack(Material.EMERALD), ChatColor.RED + "Rango " + ChatColor.BOLD + "RUBY", ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "300" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "�Usa /lobby para reclamarlos!");
        }
        return BGGameListener.invVipPoints;
    }
    
    private static IconMenu getInv_Stats(final Player p) {
        User jug = bgMain.getUserManager().getUserById(p.getUniqueId());
        if (BGGameListener.invStats == null) {
            (BGGameListener.invStats = new IconMenu("Tus Estad\u00edsticas", 45, (IconMenu.OptionClickEventHandler)new IconMenu.OptionClickEventHandler() {
                public void onOptionClick(final IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                }
            }, (Plugin)BGMain.instance)).setOption(10, new ItemStack(Material.DIAMOND_SWORD), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("Asesinatos").toString(), new StringBuilder().append(ChatColor.GRAY).append(jug.getKills()).toString());
            BGGameListener.invStats.setOption(12, new ItemStack(Material.SKULL_ITEM), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("Muertes").toString(), new StringBuilder().append(ChatColor.GRAY).append(jug.getDeaths()).toString());
            BGGameListener.invStats.setOption(14, new ItemStack(Material.DIAMOND_AXE), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("KDR").toString(), new StringBuilder().append(ChatColor.GRAY).append((jug.getDeaths() == 0 ? 0 : jug.getKills() / jug.getDeaths())).toString());
            BGGameListener.invStats.setOption(16, new ItemStack(Material.GOLDEN_APPLE, 1, (short)1), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("Victorias").toString(), new StringBuilder().append(ChatColor.GRAY).append(jug.getWins()).toString());
            BGGameListener.invStats.setOption(28, new ItemStack(Material.PAPER), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("Jugadas").toString(), new StringBuilder().append(ChatColor.GRAY).append(jug.getPlayedGames()).toString());
            BGGameListener.invStats.setOption(30, new ItemStack(Material.GOLD_INGOT), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("LCoins").toString(), new StringBuilder().append(ChatColor.GRAY).append(jug.getLcoins()).toString());
            BGGameListener.invStats.setOption(32, new ItemStack(Material.DIAMOND), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("Vip Points").toString(), new StringBuilder().append(ChatColor.GRAY).append(jug.getVipPoints()).toString());
            BGGameListener.invStats.setOption(34, new ItemStack(Material.NETHER_STAR), new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("Nivel").toString(), ChatColor.GRAY + jug.getRank());
        }
        return BGGameListener.invStats;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            return;
        }
        if (BGMain.isSpectator(e.getPlayer())) {
            e.setFormat(ChatColor.translateAlternateColorCodes('&', "&7&lEspectador " + e.getFormat()));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        final Action a = event.getAction();
        if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && BGMain.GAMESTATE == GameState.PREGAME) {
            if (p.getItemInHand().getType() == Material.BOW) {
                BGChat.printKitChat(p);
            }
            else if (p.getItemInHand().getType() == Material.PAPER) {
                getInvStats_CHG().open(p);
            }
            else if (p.getItemInHand().getType() == Material.WRITTEN_BOOK) {
                final ByteBuf buf = Unpooled.buffer(256);
                buf.setByte(0, 0);
                buf.writerIndex(1);
                final PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
            }
            else if (p.getItemInHand().getType() == Material.GOLD_INGOT) {
                getInv_VipPoints(p).open(p);
            }
            else if (p.getItemInHand().getType() == Material.SKULL_ITEM) {
                getInv_Stats(p).open(p);
            }
        }
        if (BGMain.isSpectator(p)) {
            event.setCancelled(true);
            return;
        }
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            event.setCancelled(true);
            return;
        }
        if (p.getItemInHand().getType() == Material.COMPASS) {
            Player cplayer = null;
            double cdistance = 1000.0;
            for (final Player gamers : BGMain.getGamers()) {
                if (p == gamers) {
                    continue;
                }
                if (BGTeam.isInTeam(p, gamers.getName())) {
                    continue;
                }
                final double distance = gamers.getLocation().distance(p.getLocation());
                if (distance >= cdistance) {
                    continue;
                }
                cplayer = gamers;
                cdistance = distance;
            }
            if (cplayer != null) {
                final DecimalFormat df = new DecimalFormat("##.#");
                p.sendMessage(ChatColor.GOLD + "La br\u00fajula apunta al jugador " + ChatColor.YELLOW + cplayer.getName() + ChatColor.GREEN + " (" + df.format(cdistance) + " bloques)!");
                p.setCompassTarget(cplayer.getLocation());
            }
            else {
                p.sendMessage(ChatColor.GRAY + "La br\u00fajula apunta al spawn!");
                p.setCompassTarget(BGMain.getSpawn());
            }
        }
    }
    
    @EventHandler
    public void onPing(final ServerListPingEvent e) {
        if (BGMain.GAMESTATE != GameState.PREGAME) {
            e.setMotd(ChatColor.GOLD + "Estado: " + ChatColor.AQUA + "Progreso" + "==" + ChatColor.GOLD + "Mapa: " + ChatColor.AQUA + WordUtils.capitalize(BGMain.mapa));
        }
        else {
            e.setMotd(ChatColor.GOLD + "Estado: " + ChatColor.AQUA + "Esperando" + "==" + ChatColor.GOLD + "Mapa: " + ChatColor.AQUA + WordUtils.capitalize(BGMain.mapa));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootArrow(final EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && BGMain.isSpectator((Player)event.getEntity())) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof Player && BGMain.GAMESTATE == GameState.PREGAME) {
            event.getBow().setDurability((short)0);
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (BGMain.isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (BGMain.GAMESTATE != GameState.GAME) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        if (BGMain.isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(final PlayerKickEvent event) {
        event.setLeaveMessage((String)null);
    }
    
    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent e) {
        final User jug = bgMain.getUserManager().getUserById(e.getUniqueId());
        final Player p = Bukkit.getPlayer(e.getUniqueId());
        try {
            bgMain.getUserManager().updateUser(jug);
        }
        catch (Exception a) {
            a.printStackTrace();
            p.kickPlayer("&cError al cargar tus datos Ingresa Nuevamente!");
        }
    }
    
    @EventHandler
    public void onLogin(final PlayerLoginEvent e) {

        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (BGMain.GAMESTATE == GameState.PREGAME) {
                if (e.getPlayer().hasPermission("chg.bypassfull")) {
                    e.allow();
                }
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "El juego esta lleno!");
            }
            else {
                e.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.RED + "El juego esta en progreso!");
            }
        }
    }
    
    @EventHandler
    public void onPickUp(final PlayerPickupItemEvent e) {
        final Material mat = e.getItem().getItemStack().getType();
        if (mat == Material.DIAMOND_BLOCK || mat == Material.GOLD_ORE || mat == Material.IRON_ORE) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player p = event.getPlayer();
        //agrega una validacion de que el usuario este en el UserManager registrado y sino registralo como usuario nuevo
        if (bgMain.getUserManager().getUserById(p.getUniqueId()) == null) {
            bgMain.getUserManager().registerNewPlayer(p);
        }
        final User jug = bgMain.getUserManager().getUserById(p.getUniqueId());
        event.setJoinMessage((String)null);
        p.getInventory().clear();
        p.updateInventory();
        if (jug.getRank() == null) {
            jug.setRank("Nuevo");
            bgMain.getUserManager().saveAllUsers();
        }
        else if (jug.getRank().equals("")) {
            bgMain.getUserManager().updateUser(jug);
        }
        p.setGameMode(GameMode.SURVIVAL);
        p.setMaxHealth(24.0);
        p.setHealth(24.0);
        p.setAllowFlight(true);
        BGMain.kills.put(p.getName(), 0);
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            BGMain.gamers.add(p);
            p.getInventory().addItem(new ItemStack[] { BGGameListener.kit_item });
            p.getInventory().addItem(new ItemStack[] { BGGameListener.book_item });
            p.getInventory().setItem(8, BGGameListener.stats_item);
            p.getInventory().setItem(7, BGGameListener.vippoints_item);
            final ItemStack stats_item = new ItemUtils(event.getPlayer().getName(), 1, ChatColor.GREEN + "Tus Estad\u00edsticas", ChatColor.GRAY + "Click para ver tus estadisticas");
            p.getInventory().setItem(4, stats_item);
            if (!PreGameTimer.started && Bukkit.getOnlinePlayers().size() > BGMain.MINIMUM_PLAYERS - 1) {
                new PreGameTimer();
            }
        }
        else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f" + event.getPlayer().getName() + " &aesta espectando"));
            BGMain.addSpectator(p);
        }
        final String command = "minecraft:gamerule sendCommandFeedback false";
        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
        Util.sendTitle(p, 0, 60, 0, "&6&lCHG", "&awww.mine.lc");
    }

    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void FoodCe(final FoodLevelChangeEvent e) {
        if (BGMain.GAMESTATE != GameState.GAME) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (BGMain.GAMESTATE == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player p = event.getPlayer();
        event.setQuitMessage((String)null);
        final User jug = bgMain.getUserManager().getUserById(p.getUniqueId());
        Player pjug = Bukkit.getPlayer(jug.getId());
        if (BGMain.gamers.contains(p)) {
            BGMain.gamers.remove(p);
        }
        if (BGMain.isSpectator(p)) {
            BGMain.remSpectator(p);
        }
        else if (BGMain.GAMESTATE == GameState.GAME) {
            final long ttime = Tagged.getTime(jug);
            final User killer = Tagged.getKiller(jug);
            Player pkiller = Bukkit.getPlayer(killer.getId());
            if (System.currentTimeMillis() - ttime < 10000L && killer != jug) {
                if (killer != null) {
                    killer.setKills(killer.getKills() + 1);
                    BGMain.kills.put(pkiller.getName(), BGMain.kills.get(pkiller.getName()) + 1);
                    jug.setDeaths(jug.getDeaths() + 1);
                    this.sendGameMessage(ChatColor.GRAY + pjug.getName() + ChatColor.YELLOW + " se desconecto pero fue asesinado por " + ChatColor.GRAY + pkiller.getName() + ChatColor.YELLOW + "!");
                    bgMain.getUserManager().saveAllUsers();
                    final Location loc = p.getLocation();
                    ItemStack[] armorContents;
                    for (int length = (armorContents = p.getInventory().getArmorContents()).length, i = 0; i < length; ++i) {
                        final ItemStack is = armorContents[i];
                        try {
                            if (is != null) {
                                loc.getWorld().dropItem(loc, is);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    ItemStack[] contents;
                    for (int length2 = (contents = p.getInventory().getContents()).length, j = 0; j < length2; ++j) {
                        final ItemStack is = contents[j];
                        try {
                            if (is != null) {
                                loc.getWorld().dropItem(loc, is);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            else {
                this.sendGameMessage(ChatColor.GRAY + pjug.getName() + ChatColor.YELLOW + " ha muerto.");
            }
            BGMain.checkwinner();
        }
        Tagged.removeTagged(jug);
        if (BGMain.TEAMS.containsKey("kills" + p.getName())) {
            BGMain.TEAMS.remove("kills" + p.getName()).unregister();
        }
        if (BGMain.TEAMS.containsKey("deaths" + p.getName())) {
            BGMain.TEAMS.remove("deaths" + p.getName()).unregister();
        }
        if (BGMain.TEAMS.containsKey("kdr" + p.getName())) {
            BGMain.TEAMS.remove("kdr" + p.getName()).unregister();
        }
        if (BGMain.TEAMS.containsKey("lvl" + p.getName())) {
            BGMain.TEAMS.remove("lvl" + p.getName()).unregister();
        }
        p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        p.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
        //bgMain.getUserManager().removeUser(jug);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity entityDamager = event.getDamager();
        final Entity entityDamaged = event.getEntity();
        if (entityDamager instanceof Arrow) {
            if (entityDamaged instanceof Player && ((Arrow)entityDamager).getShooter() instanceof Player) {
                final Arrow arrow = (Arrow)entityDamager;
                final Vector velocity = arrow.getVelocity();
                final Player shooter = (Player)arrow.getShooter();
                final Player damaged = (Player)entityDamaged;
                if (BGMain.isSpectator(damaged)) {
                    final double x = damaged.getLocation().getBlockX() + 2;
                    final double y = damaged.getLocation().getBlockY() + 10;
                    final double z = damaged.getLocation().getBlockZ() + 2;
                    final Location loc = new Location(damaged.getWorld(), x, y, z);
                    damaged.teleport(loc);
                    BGChat.printPlayerChat(damaged, ChatColor.RED + Translation.SPECTATOR_IN_THE_WAY.t());
                    final Arrow newArrow = (Arrow)shooter.launchProjectile((Class)Arrow.class);
                    newArrow.setShooter((ProjectileSource)shooter);
                    newArrow.setVelocity(velocity);
                    newArrow.setBounce(false);
                    event.setCancelled(true);
                    arrow.remove();
                }
            }
        }
        else if (entityDamager instanceof Player) {
            final Player player = (Player)event.getDamager();
            if (BGMain.isSpectator((Player)entityDamager)) {
                event.setCancelled(true);
                return;
            }
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                final Collection<PotionEffect> pe = (Collection<PotionEffect>)player.getActivePotionEffects();
                for (final PotionEffect effect : pe) {
                    if (effect.getType().equals((Object)PotionEffectType.INCREASE_DAMAGE)) {
                        if (effect.getAmplifier() == 0) {
                            event.setDamage(event.getDamage() - 9.0);
                        }
                        else {
                            event.setDamage(event.getDamage() - 11.5);
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player && BGMain.isSpectator((Player)event.getRightClicked()) && !BGMain.isSpectator(event.getPlayer())) {
            event.getRightClicked().teleport(BGMain.getSpawn());
            BGChat.printPlayerChat((Player)event.getRightClicked(), ChatColor.RED + Translation.SPECTATOR_IN_THE_WAY.t());
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player p = (Player)event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (BGMain.isSpectator(p) || BGMain.GAMESTATE == GameState.INVINCIBILITY || BGMain.GAMESTATE == GameState.PREGAME) {
                    event.setCancelled(true);
                    p.setFallDistance(0.0f);
                    p.teleport(p.getWorld().getSpawnLocation());
                    p.playSound(p.getLocation(), Sound.HURT_FLESH, 1.0f, 1.3f);
                }
            }
            else if (BGMain.isSpectator(p)) {
                event.setCancelled(true);
                return;
            }
        }
        if (BGMain.GAMESTATE != GameState.GAME && event.getEntity() instanceof Player) {
            event.setCancelled(true);
            return;
        }
        if (BGMain.GAMESTATE == GameState.PREGAME && !(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        e.setRespawnLocation(p.getLocation());
        BGMain.addSpectator(p);
        p.sendMessage(ChatColor.AQUA + "Ahora eres espectador. Para salir usa el comando /lobby!");
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(final PlayerDeathEvent e) {
        e.setDeathMessage((String)null);
        final Player ptarget = e.getEntity();
        final User target = bgMain.getUserManager().getUserById(ptarget.getUniqueId());
        final Entity ent = (Entity)e.getEntity();
        EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.CUSTOM;
        if (ent.getLastDamageCause() != null) {
            damageCause = ent.getLastDamageCause().getCause();
        }
        if (BGMain.gamers.contains(ptarget)) {
            BGMain.gamers.remove(ptarget);
        }
        BGMain.spectators.add(ptarget);
        BGChat.printPlayerChat(ptarget, ChatColor.YELLOW + Translation.NOW_SPECTATOR.t());
        final EntityDamageEvent.DamageCause dCause = damageCause;
        target.setDeaths(target.getDeaths() + 1);
        if (System.currentTimeMillis() - Tagged.getTime(target) < 10000L) {
            final User killer = Tagged.getKiller(target);
            final Player pkiller = Bukkit.getPlayer(killer.getId());
            if (killer != null) {
                bgMain.getFameManager().addFame(killer.getId());
                killer.setKills(killer.getKills() + 1);
                bgMain.getUserManager().saveAllUsers();
                final String rank = killer.getRank();
                final int fame = killer.getFame();
                if (fame >= 25 && fame < 100) {
                    if (!rank.equalsIgnoreCase("Aprendiz")) {
                        killer.setRank("Aprendiz");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Aprendiz!!"));
                    }
                } else if (fame >= 100 && fame < 200) {
                    if (!rank.equalsIgnoreCase("Heroe")) {
                        killer.setRank("Heroe");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Heroe!!"));
                    }
                } else if (fame >= 200 && fame < 300) {
                    if (!rank.equalsIgnoreCase("Feroz")) {
                        killer.setRank("Feroz");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Feroz!!"));
                    }
                } else if (fame >= 300 && fame < 500) {
                    if (!rank.equalsIgnoreCase("Poderoso")) {
                        killer.setRank("Poderoso");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Poderoso!!"));
                    }
                } else if (fame >= 500 && fame < 1000) {
                    if (!rank.equalsIgnoreCase("Mortal")) {
                        killer.setRank("Mortal");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Mortal!!"));
                    }
                } else if (fame >= 1000 && fame < 2000) {
                    if (!rank.equalsIgnoreCase("Terrorifico")) {
                        killer.setRank("Terrorifico");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Terrorifico!!"));
                    }
                } else if (fame >= 2000 && fame < 4000) {
                    if (!rank.equalsIgnoreCase("Conquistador")) {
                        killer.setRank("Conquistador");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Conquistador!!"));
                    }
                } else if (fame >= 4000 && fame < 6000) {
                    if (!rank.equalsIgnoreCase("Renombrado")) {
                        killer.setRank("Renombrado");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Renombrado!!"));
                    }
                } else if (fame >= 6000 && fame < 8000) {
                    if (!rank.equalsIgnoreCase("Ilustre")) {
                        killer.setRank("Ilustre");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Ilustre!!"));
                    }
                } else if (fame >= 8000 && fame < 10000) {
                    if (!rank.equalsIgnoreCase("Eminente")) {
                        killer.setRank("Eminente");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Eminente!!"));
                    }
                } else if (fame >= 10000 && fame < 20000) {
                    if (!rank.equalsIgnoreCase("Rey")) {
                        killer.setRank("Rey");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Rey!!"));
                    }
                } else if (fame >= 20000 && fame < 100000) {
                    if (!rank.equalsIgnoreCase("Emperador")) {
                        killer.setRank("Emperador");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Emperador!!"));
                    }
                } else if (fame >= 100000 && fame < 200000) {
                    if (!rank.equalsIgnoreCase("Legendario")) {
                        killer.setRank("Legendario");
                        pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Legendario!!"));
                    }
                } else if (fame >= 200000 && !rank.equalsIgnoreCase("Mítico")) {
                    killer.setRank("Mítico");
                    pkiller.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Mítico!!"));
                }
                BGMain.kills.put(pkiller.getName(), BGMain.kills.get(pkiller.getName()) + 1);
                if (ptarget != null) {
                    if (dCause == EntityDamageEvent.DamageCause.PROJECTILE) {
                        ptarget.playSound(ptarget.getLocation(), Sound.ENDERMAN_SCREAM, 1.0f, 2.0f);
                    }
                    else {
                        ptarget.playSound(ptarget.getLocation(), Sound.ENDERMAN_DEATH, 1.0f, 2.0f);
                    }
                }
                this.sendGameMessage(this.getDeathMessage(dCause, true, target, killer));
                bgMain.getUserManager().saveAllUsers();
            }
        }
        else {
            this.sendGameMessage(this.getDeathMessage(dCause, false, target, target));
        }
        target.setPlayedGames(target.getPlayedGames() + 1);
        bgMain.getUserManager().saveAllUsers();
        Bukkit.getScheduler().runTaskLater((Plugin)BGMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (ptarget.isDead()) {
                    ptarget.sendMessage("");
                }
            }
        }, 8L);
        final Location loc = ptarget.getLocation();
        String fl = new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append(BGFiles.dsign.getString("FIRST_LINE")).toString();
        String sl = ChatColor.DARK_RED + BGFiles.dsign.getString("SECOND_LINE");
        fl = fl.replace("[name]", ptarget.getName());
        sl = sl.replace("[name]", ptarget.getName());
        BGSign.createSign(loc, fl, sl, "", "");
        if (ptarget.getKiller() != null && ptarget.getKiller() instanceof Player) {
            final Player killer2 = ptarget.getKiller();
            if (BGKit.hasAbility(killer2, 14)) {
                if (killer2.getFoodLevel() <= 14) {
                    killer2.setFoodLevel(killer2.getFoodLevel() + 6);
                }
                else {
                    killer2.setFoodLevel(20);
                }
            }
        }
        Bukkit.getServer().getWorlds().get(0).strikeLightningEffect(ptarget.getLocation().clone().add(0.0, 50.0, 0.0));
        BGMain.checkwinner();
    }
    
    private void sendGameMessage(final String message) {
        for (final Player Online : Bukkit.getOnlinePlayers()) {
            Online.sendMessage(message);
        }
    }
    
    private String getDeathMessage(final EntityDamageEvent.DamageCause dCause, final boolean withHelp, final User target, final User killer) {
        String first = "";
        Player pkiller = Bukkit.getPlayer(killer.getId());
        Player ptarget = Bukkit.getPlayer(target.getId());
        String second = ChatColor.RED + " por " + ChatColor.RED + pkiller.getName();
        try {
            if (dCause.equals((Object)EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || dCause.equals((Object)EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " exploto";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.DROWNING)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " se ahogo";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.FIRE) || dCause.equals((Object)EntityDamageEvent.DamageCause.FIRE_TICK)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio quemado";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                if (pkiller.getItemInHand().getType() == null) {
                    first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " fue asesinado por " + ChatColor.GREEN + pkiller.getName();
                    second = "";
                }
                else {
                    final String item = pkiller.getItemInHand().getItemMeta().getDisplayName();
                    if (item == null) {
                        first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " fue asesinado por " + ChatColor.GREEN + pkiller.getName();
                        second = "";
                    }
                    else {
                        first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " fue asesinado por " + ChatColor.GREEN + pkiller.getName() + ChatColor.RED + " usando " + ChatColor.DARK_RED + pkiller.getItemInHand().getItemMeta().getDisplayName();
                        second = "";
                    }
                }
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " fue aplastado";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.WITHER)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio por magia oscura";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.POISON)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio envenenado";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.LAVA)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio quemado";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.PROJECTILE)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " fue disparado por " + ChatColor.GREEN + pkiller.getName();
                second = "";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.SUFFOCATION)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio sofocado";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.FALL)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " se cayo de muy alto";
            }
            else if (dCause.equals((Object)EntityDamageEvent.DamageCause.VOID)) {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " cayo al vacio";
            }
            else {
                first = ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio";
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ChatColor.DARK_RED + ptarget.getName() + ChatColor.RED + " murio.";
        }
        if (withHelp) {
            return String.valueOf(first) + second + ChatColor.RED + "!";
        }
        return String.valueOf(first) + ChatColor.RED + ".";
    }
    
    @EventHandler
    public void onEntityDamageEntity(final EntityDamageByEntityEvent e) {
        final Entity ent = e.getEntity();
        if (ent instanceof Player) {
            final Player ptarget = (Player)ent;
            final User target = bgMain.getUserManager().getUserById(ptarget.getUniqueId());
            final Entity damager = e.getDamager();
            if (e.getCause().equals((Object)EntityDamageEvent.DamageCause.PROJECTILE)) {
                if (damager instanceof Snowball) {
                    final Snowball snowball = (Snowball)damager;
                    if (snowball.getShooter() instanceof Player) {
                        final Player pkiller = (Player)snowball.getShooter();
                        final User killer = bgMain.getUserManager().getUserById(pkiller.getUniqueId());
                        Tagged.addTagged(target, killer, System.currentTimeMillis());
                    }
                }
                else if (damager instanceof Egg) {
                    final Egg egg = (Egg)damager;
                    if (egg.getShooter() instanceof Player) {
                        final Player pkiller = (Player)egg.getShooter();
                        final User killer = bgMain.getUserManager().getUserById(pkiller.getUniqueId());
                        Tagged.addTagged(target, killer, System.currentTimeMillis());
                    }
                }
                else if (damager instanceof Arrow) {
                    final Arrow arrow = (Arrow)damager;
                    if (arrow.getShooter() instanceof Player) {
                        final Player pkiller = (Player)arrow.getShooter();
                        final User killer = bgMain.getUserManager().getUserById(pkiller.getUniqueId());
                        Tagged.addTagged(target, killer, System.currentTimeMillis());
                    }
                }
                else if (damager instanceof EnderPearl) {
                    final EnderPearl ePearl = (EnderPearl)damager;
                    if (ePearl.getShooter() instanceof Player) {
                        final Player pkiller = (Player)ePearl.getShooter();
                        final User killer = bgMain.getUserManager().getUserById(pkiller.getUniqueId());
                        Tagged.addTagged(target, killer, System.currentTimeMillis());
                    }
                }
                else if (damager instanceof ThrownPotion) {
                    final ThrownPotion potion = (ThrownPotion)damager;
                    if (potion.getShooter() instanceof Player) {
                        final Player pkiller = (Player)potion.getShooter();
                        final User killer = bgMain.getUserManager().getUserById(pkiller.getUniqueId());
                        Tagged.addTagged(target, killer, System.currentTimeMillis());
                    }
                }
            }
            else if (damager instanceof Player) {
                final User killer = bgMain.getUserManager().getUserById(damager.getUniqueId());
                Tagged.addTagged(target, killer, System.currentTimeMillis());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/me ") || event.getMessage().toLowerCase().startsWith("/kil") || event.getMessage().toLowerCase().contains(":me ")) {
            event.setCancelled(true);
            return;
        }
        if (event.getMessage().toLowerCase().startsWith("/say ")) {
            if (event.getPlayer().hasPermission("bg.admin.*")) {
                final String say = event.getMessage().substring(5);
                BGChat.printInfoChat(say);
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEntityCollision(final VehicleEntityCollisionEvent event) {
        if (event.getEntity() instanceof Player && BGMain.isSpectator((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDestroy(final VehicleDestroyEvent event) {
        final Entity entity = event.getAttacker();
        if (entity instanceof Player && BGMain.isSpectator((Player)entity)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEnter(final VehicleEnterEvent event) {
        final Entity entity = event.getEntered();
        if (entity instanceof Player && BGMain.isSpectator((Player)entity)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDamage(final VehicleDamageEvent event) {
        final Entity entity = event.getAttacker();
        if (entity instanceof Player && BGMain.isSpectator((Player)entity)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEntityShear(final PlayerShearEntityEvent event) {
        if (BGMain.isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player && BGMain.isSpectator((Player)event.getEntity().getShooter())) {
            event.getEntity().remove();
        }
    }
}
