package lc.mine.hg.utilities;

import org.bukkit.*;
import org.bukkit.block.*;

public class BGSign
{
    public static void createSign(final Location loc, final String firstLine, final String secondLine, final String thirdLine, final String fourthLine) {
        try {
            String fline = firstLine;
            String sline = secondLine;
            String tline = thirdLine;
            String foline = fourthLine;
            if (fline != null && fline.length() > 15) {
                fline = fline.substring(0, 15);
            }
            if (sline != null && sline.length() > 15) {
                sline = sline.substring(0, 15);
            }
            if (tline != null && tline.length() > 15) {
                tline = tline.substring(0, 15);
            }
            if (foline != null && foline.length() > 15) {
                foline = foline.substring(0, 15);
            }
            final Block block = Bukkit.getWorlds().get(0).getBlockAt(loc);
            block.setType(Material.SIGN_POST);
            final Sign sign = (Sign)block.getState();
            if (fline != null) {
                sign.setLine(0, fline);
            }
            if (sline != null) {
                sign.setLine(1, sline);
            }
            if (tline != null) {
                sign.setLine(2, tline);
            }
            if (foline != null) {
                sign.setLine(3, foline);
            }
            sign.update(true);
        }
        catch (Exception ex) {}
    }
}
