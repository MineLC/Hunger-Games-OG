package lc.mine.hg.data.economy;

import java.util.UUID;

import lc.mine.hg.data.user.User;
import lc.mine.hg.main.BGMain;

public class EconomyManager {


    // Add lcoins to a user
    public void addLcoins(UUID id, int amount) {
        User user = BGMain.database.getCached(id);
        if (user != null) {
            user.setLcoins(user.getLcoins() + amount);
        }
    }

    // Deduct lcoins from a user
    public void deductLcoins(UUID id, int amount) {
        User user = BGMain.database.getCached(id);
        if (user != null) {
            user.setLcoins(user.getLcoins() - amount);
        }
    }

    // Get lcoins of a user
    public double getLcoins(UUID id) {
        User user = BGMain.database.getCached(id);
        return user != null ? user.getLcoins() : 0;
    }
}
