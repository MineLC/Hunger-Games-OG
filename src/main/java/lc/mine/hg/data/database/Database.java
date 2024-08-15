package lc.mine.hg.data.database;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import lc.mine.hg.data.user.User;

public interface Database {
    
    void save(final Player player);
    User load(final Player player);
    void create(final Player player, final User user);
    User getCached(UUID uuid);
    Map<UUID, User> getUsers();
    void close();
}
