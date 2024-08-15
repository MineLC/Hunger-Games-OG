package lc.mine.hg.utilities;

import java.util.*;

import lc.mine.hg.data.user.User;

public class Tagged
{
    private static HashMap<User, Long> taggedtime;
    private static HashMap<User, User> taggedplayer;
    
    static {
        Tagged.taggedtime = new HashMap<User, Long>();
        Tagged.taggedplayer = new HashMap<User, User>();
    }
    
    public static void addTagged(final User player, final User killer, final Long time) {
        Tagged.taggedplayer.put(player, killer);
        Tagged.taggedtime.put(player, time);
    }
    
    public static Long getTime(final User j) {
        if (Tagged.taggedtime.containsKey(j)) {
            return Tagged.taggedtime.get(j);
        }
        return 0L;
    }
    
    public static User getKiller(final User j) {
        User ret = null;
        if (Tagged.taggedplayer.containsKey(j)) {
            ret = Tagged.taggedplayer.get(j);
            Tagged.taggedplayer.remove(j);
            Tagged.taggedtime.remove(j);
        }
        return ret;
    }
    
    public static void removeTagged(final User jug) {
        if (Tagged.taggedplayer.containsKey(jug)) {
            Tagged.taggedplayer.remove(jug);
        }
        if (Tagged.taggedtime.containsKey(jug)) {
            Tagged.taggedtime.remove(jug);
        }
    }
}
