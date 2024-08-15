package lc.mine.hg.data.user;

import java.util.UUID;

import lc.mine.core.database.PlayerData;
import lc.mine.hg.main.BGMain;

public class User {   
    private UUID id;
    private String name;
    private String kit;
    private String rank;
    private int wins;
    private int playedGames;
    private int deaths;
    private int kills;
    private int fame;

    // Default constructor required for Jackson
    public User() {}

    // Parameterized constructor
    public User(UUID id, String name, String kit, String rank, int wins, int playedGames, int deaths, int kills, int fame) {
        this.id = id;
        this.name = name;
        this.kit = kit;
        this.rank = rank;
        this.wins = wins;
        this.playedGames = playedGames;
        this.deaths = deaths;
        this.kills = kills;
        this.fame = fame;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public double getLcoins() {
        final PlayerData data = BGMain.core.getData().getCached(id);
        return (data == null) ? 0 : data.getLcoins();
    }

    public void setLcoins(double lcoins) {
        final PlayerData data = BGMain.core.getData().getCached(id);
        if (data == null) {
            return;
        }
        data.setLcoins(lcoins);
    }

    public double getVipPoints() {
        final PlayerData data = BGMain.core.getData().getCached(id);
        return (data == null) ? 0 : data.getVipPoins();
    }

    public void setVipPoints(double vipPoints) {
        final PlayerData data = BGMain.core.getData().getCached(id);
        if (data == null) {
            return;
        }
        data.setVipPoins(vipPoints);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getFame() {
        return fame;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    // Additional methods
    public int getLevel() {
        int lvl = kills / 100;
        lvl += wins / 2;
        return lvl;
    }

    public static final class New extends User {
        
    }
}