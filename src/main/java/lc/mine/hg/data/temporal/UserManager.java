package lc.mine.hg.data.temporal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserManager {
    private List<User> users;
    private static final String DATA_FILE = "data.json";
    private ObjectMapper objectMapper;

    // Constructor
    public UserManager() {
        objectMapper = new ObjectMapper();
        loadUsers();
    }

    // Load users from data.json
    private void loadUsers() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
            } else {
                users = new ArrayList<>();
                createDataFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }

    // Create data.json if it does not exist
    private void createDataFile() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                file.createNewFile();
                saveUsers(); // Solo guarda si el archivo es nuevo
                System.out.println("Created new data.json file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save users to data.json
    private void saveUsers() {
        try {
            objectMapper.writeValue(new File(DATA_FILE), users);
            System.out.println("Saved users to data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new user
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }
    // Update an existing user
    public void updateUser(User updatedUser) {
        if (updatedUser == null || updatedUser.getId() == null) {
            System.err.println("Updated user or user ID is null");
            return;
        }

        Optional<User> existingUserOpt = users.stream()
                .filter(user -> user.getId().equals(updatedUser.getId()))
                .findFirst();

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setKit(updatedUser.getKit());
            existingUser.setRank(updatedUser.getRank());
            existingUser.setLcoins(updatedUser.getLcoins());
            existingUser.setVipPoints(updatedUser.getVipPoints());
            existingUser.setWins(updatedUser.getWins());
            existingUser.setPlayedGames(updatedUser.getPlayedGames());
            existingUser.setDeaths(updatedUser.getDeaths());
            existingUser.setKills(updatedUser.getKills());
            existingUser.setFame(updatedUser.getFame());
            existingUser.setOwnKitList(updatedUser.getOwnKitList());
            saveUsers();
        } else {
            System.err.println("User with ID " + updatedUser.getId() + " not found");
        }
    }

    // Get all users
    public List<User> getUsers() {
        return users;
    }

    // Get user by ID
    public User getUserById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Save all users (public method for external use)
    public void saveAllUsers() {
        saveUsers();
    }

    // Register a new player with default stats
    public void registerNewPlayer(Player p) {
        List<String> ownKitList = new ArrayList<>();
        ownKitList.add("Default");

        UUID id = p.getUniqueId();
        String name = p.getName();
        User newUser = new User(id, name, "Default", "Nuevo", 0, 0, 0, 0, 0, 0, 0, ownKitList);
        addUser(newUser);
    }

    // Explicitly create data.json file if it does not exist
    public void createDataFileIfNotExists() {
        createDataFile();
    }
}