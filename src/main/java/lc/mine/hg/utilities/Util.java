package lc.mine.hg.utilities;

import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import java.util.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.io.*;

public class Util
{
    public static void sendTitle(final Player player, final int fadein, final int stay, final int fadeout, String title, String subtitle) {
        final PlayerConnection pConn = ((CraftPlayer)player).getHandle().playerConnection;
        final PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent)null, fadein, stay, fadeout);
        pConn.sendPacket((Packet)pTitleInfo);
        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            final IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            final PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket((Packet)pSubtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            final IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            final PacketPlayOutTitle pTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iComp);
            pConn.sendPacket((Packet)pTitle);
        }
    }
    
    public static void sendParticles(final World world, final String type, final float x, final float y, final float z, final float offsetX, final float offsetY, final float offsetZ, final float data, final int amount) {
        final EnumParticle particle = EnumParticle.valueOf(type);
        final PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particle, false, x, y, z, offsetX, offsetY, offsetZ, data, amount, new int[] { 1 });
        for (final Player player : world.getPlayers()) {
            final CraftPlayer start = (CraftPlayer)player;
            final EntityPlayer target = start.getHandle();
            final PlayerConnection connect = target.playerConnection;
            connect.sendPacket((Packet)particles);
        }
    }
    
    public static FireworkEffect getFireworkEffect(final Color one, final Color two, final Color three, final Color four, final Color five, final FireworkEffect.Type type) {
        return FireworkEffect.builder().flicker(false).withColor(new Color[] { one, two, three, four }).withFade(five).with(type).trail(true).build();
    }
    
    public static String colorize(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public static void copyFolder(final File src, final File dest) throws IOException {
        deleteDir(dest);
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            final String[] files = src.list();
            String[] array;
            for (int length2 = (array = files).length, i = 0; i < length2; ++i) {
                final String file = array[i];
                final File srcFile = new File(src, file);
                final File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        }
        else {
            final InputStream in = new FileInputStream(src);
            final OutputStream out = new FileOutputStream(dest);
            out.flush();
            final byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
    
    public static void deleteDir(final File dir) {
        if (dir.isDirectory()) {
            final String[] children = dir.list();
            for (int i = 0; i < children.length; ++i) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }
    
    public static void logToFile(final String message, final Plugin p, final String filename) {
        try {
            final File dataFolder = p.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            final File saveTo = new File(p.getDataFolder(), filename);
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }
            final FileWriter fw = new FileWriter(saveTo, true);
            final PrintWriter pw = new PrintWriter(fw);
            pw.println(message);
            pw.flush();
            pw.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String TimeToString(final long time) {
        final long hours = time / 3600L;
        final long minutes = (time - hours * 3600L) / 60L;
        final long seconds = time - hours * 3600L - minutes * 60L;
        return String.format("%02d:%02d", minutes, seconds).replace("-", "");
    }
}
