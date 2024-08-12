package lc.mine.hg.data.economy;

import lc.mine.hg.data.temporal.User;
import lc.mine.hg.data.temporal.UserManager;

import java.util.UUID;

public class FameManager {
    private UserManager userManager;

    // Constructor
    public FameManager(UserManager userManager) {
        this.userManager = userManager;
    }

    // Add fame to a user following the specified logic
    public void addFame(UUID id) {
        User user = userManager.getUserById(id);
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
            userManager.saveAllUsers();
        }
    }

    // Deduct fame from a user
    public void deductFame(UUID id, int amount) {
        User user = userManager.getUserById(id);
        if (user != null) {
            user.setFame(user.getFame() - amount);
            userManager.saveAllUsers();
        }
    }

    // Get fame of a user
    public int getFame(UUID id) {
        User user = userManager.getUserById(id);
        return user != null ? user.getFame() : 0;
    }
}