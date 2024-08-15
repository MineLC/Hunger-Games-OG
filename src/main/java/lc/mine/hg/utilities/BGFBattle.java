package lc.mine.hg.utilities;

import lc.mine.hg.main.BGMain;
import org.bukkit.block.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

public class BGFBattle implements Listener
{
    private static Block mainBlock;
    
    public static void createBattle() {
        Bukkit.getPluginManager().registerEvents((Listener)new BGFBattle(), (Plugin)BGMain.instance);
        BGFBattle.mainBlock = BGMain.getSpawn().add(0.0, 25.0, 0.0).getBlock();
        final Location loc = BGFBattle.mainBlock.getLocation();
        final Integer[] co = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 2, 2, 4, 4, 4, 1, -1, 1, 4, 4, 4, 2, 2, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, -1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, -2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 3, 3, 4, 4, 4, 1, -1, 1, 4, 4, 4, 3, 3, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2 };
        Integer[] array;
        for (int length = (array = co).length, j = 0; j < length; ++j) {
            final Integer i = array[j];
            Material m = Material.AIR;
            switch (i) {
                case 0: {
                    m = Material.AIR;
                    break;
                }
                case 1: {
                    m = Material.QUARTZ_BLOCK;
                    break;
                }
                case 2: {
                    m = Material.STAINED_GLASS;
                    break;
                }
                case 3: {
                    m = Material.SEA_LANTERN;
                    break;
                }
                case 4: {
                    m = Material.QUARTZ_BLOCK;
                }
            }
            if (i == -1) {
                loc.add(0.0, 0.0, 1.0);
                loc.subtract(10.0, 0.0, 0.0);
            }
            else if (i == -2) {
                loc.add(0.0, 1.0, 0.0);
                loc.subtract(10.0, 0.0, 9.0);
            }
            else {
                loc.getBlock().setType(m);
                if (loc.getBlock().getType() == Material.STAINED_GLASS) {
                    loc.getBlock().setData((byte)7);
                }
                loc.add(1.0, 0.0, 0.0);
            }
        }
    }
    
    public static void teleportGamers(final LinkedList<Player> linkedList) {
        for (final Player p : linkedList) {
            p.leaveVehicle();
            final Location loc = BGFBattle.mainBlock.getLocation().add(BGMain.random.nextInt(5) + 0.5, 1.0, BGMain.random.nextInt(5) + 0.5);
            p.teleport(loc);
        }
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        if (event.getCause().equals((Object)PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }
    
    public static Block getMainBlock() {
        return BGFBattle.mainBlock;
    }
}
