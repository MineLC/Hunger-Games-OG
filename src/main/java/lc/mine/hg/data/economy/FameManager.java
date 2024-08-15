package lc.mine.hg.data.economy;

import java.util.UUID;

import lc.mine.hg.data.database.Database;
import lc.mine.hg.data.user.User;
import lc.mine.hg.main.BGMain;

public class FameManager {

    // Add fame to a user following the specified logic
    public void addFame(UUID id) {
        User user = BGMain.database.getCached(id);
        if (user != null) {
            int fame = user.getFame();
            if (fame < 4) {
                fame++;
            } else if (fame < 8) {
                fame += 2;
            } else if (fame < 40) {
                fame += 4;
            } else {
                fame += 40;
            }
            user.setFame(fame);
        }
    }

    // Deduct fame from a user
    public void deductFame(UUID id, int amount) {
        User user = BGMain.database.getCached(id);
        if (user != null) {
            user.setFame(user.getFame() - amount);
        }
    }

    // Get fame of a user
    public int getFame(UUID id) {
        User user = BGMain.database.getCached(id);
        return user != null ? user.getFame() : 0;
    }
}