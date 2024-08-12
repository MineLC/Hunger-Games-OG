package lc.mine.hg.utilities;

import org.bukkit.entity.*;
import java.util.*;

public class BGTeam
{
    private static HashMap<Player, ArrayList<String>> teams;
    
    static {
        BGTeam.teams = new HashMap<Player, ArrayList<String>>();
    }
    
    public static void addMember(final Player player, final String memberName) {
        ArrayList<String> members = BGTeam.teams.get(player);
        if (members == null) {
            members = new ArrayList<String>();
        }
        members.add(memberName);
        BGTeam.teams.put(player, members);
    }
    
    public static void removeMember(final Player player, final String memberName) {
        final ArrayList<String> members = BGTeam.teams.get(player);
        if (members == null) {
            return;
        }
        members.remove(memberName);
        BGTeam.teams.put(player, members);
    }
    
    public static ArrayList<String> getTeamList(final Player player) {
        final ArrayList<String> members = BGTeam.teams.get(player);
        if (members == null) {
            return null;
        }
        if (members.size() == 0) {
            return null;
        }
        return members;
    }
    
    public static boolean isInTeam(final Player player, final String memberName) {
        final ArrayList<String> members = BGTeam.teams.get(player);
        return members != null && members.contains(memberName);
    }
}
