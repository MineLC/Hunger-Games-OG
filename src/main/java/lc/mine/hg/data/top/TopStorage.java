package lc.mine.hg.data.top;

import java.util.Collection;

import lc.mine.hg.data.user.User;
import lc.mine.hg.main.BGMain;

public record TopStorage(
    Top kills,
    Top deaths,
    Top wins,
    Top played
) {
    private static TopStorage storage;

    public static TopStorage get() {
        return storage;
    }

    public void save(final User user) {
        final Top.Player player = new Top.Player(user.getName(), 0);
        player.value = user.getKills();
        kills().calculatePosition(player);

        player.value = user.getDeaths();
        deaths().calculatePosition(player);

        player.value = user.getWins();
        wins().calculatePosition(player);

        player.value = user.getPlayedGames();
        played().calculatePosition(player);
    }

    public void saveAll() {
        final Collection<User> users = BGMain.database.getUsers().values();
        for (final User user : users) {
            save(user);
        }
        new TopManager().save();
    }

    public static void set(TopStorage newStorage) {
        storage = newStorage;
    }
}