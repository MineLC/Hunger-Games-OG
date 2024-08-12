package lc.mine.hg.data.temporal;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class TopManager {
    private UserManager userManager;

    // Constructor
    public TopManager(UserManager userManager) {
        this.userManager = userManager;
    }

    // Get top users by wins
    public LinkedHashMap<String, Integer> getTopWins() {
        return getTopUsers(User::getWins);
    }

    // Get top users by kills
    public LinkedHashMap<String, Integer> getTopKills() {
        return getTopUsers(User::getKills);
    }

    // Get top users by deaths
    public LinkedHashMap<String, Integer> getTopDeaths() {
        return getTopUsers(User::getDeaths);
    }

    // Get top users by played games
    public LinkedHashMap<String, Integer> getTopPlayedGames() {
        return getTopUsers(User::getPlayedGames);
    }

    // Get top users by levels
    public LinkedHashMap<String, Integer> getTopLevels() {
        return getTopUsers(User::getLevel);
    }

    // Helper method to get top users based on a given attribute extractor
    private LinkedHashMap<String, Integer> getTopUsers(ToIntFunction<User> attributeExtractor) {
        return userManager.getUsers().stream()
                .sorted(Comparator.comparingInt(attributeExtractor).reversed())
                .collect(Collectors.toMap(
                        User::getName,
                        attributeExtractor::applyAsInt,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}