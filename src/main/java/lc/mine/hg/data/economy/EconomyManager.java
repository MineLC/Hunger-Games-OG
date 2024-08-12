package lc.mine.hg.data.economy;

import lc.mine.hg.data.temporal.User;
import lc.mine.hg.data.temporal.UserManager;
import java.util.UUID;

public class EconomyManager {
    private UserManager userManager;

    // Constructor
    public EconomyManager(UserManager userManager) {
        this.userManager = userManager;
    }

    // Add lcoins to a user
    public void addLcoins(UUID id, int amount) {
        User user = userManager.getUserById(id);
        if (user != null) {
            user.setLcoins(user.getLcoins() + amount);
            userManager.saveAllUsers();
        }
    }

    // Deduct lcoins from a user
    public void deductLcoins(UUID id, int amount) {
        User user = userManager.getUserById(id);
        if (user != null) {
            user.setLcoins(user.getLcoins() - amount);
            userManager.saveAllUsers();
        }
    }

    // Get lcoins of a user
    public int getLcoins(UUID id) {
        User user = userManager.getUserById(id);
        return user != null ? user.getLcoins() : 0;
    }
}
