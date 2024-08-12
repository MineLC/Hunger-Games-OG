package lc.mine.hg.data.temporal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private UUID id;
    private String name;
    private String kit;
    private String rank;
    private int lcoins;
    private int vipPoints;
    private int wins;
    private int playedGames;
    private int deaths;
    private int kills;
    private int fame;
    private List<String> ownKitList;

    // Default constructor required for Jackson
    public User() {}

    // Parameterized constructor
    public User(UUID id, String name, String kit, String rank, int lcoins, int vipPoints, int wins, int playedGames, int deaths, int kills, int fame, List<String> ownKitList) {
        this.id = id;
        this.name = name;
        this.kit = kit;
        this.rank = rank;
        this.lcoins = lcoins;
        this.vipPoints = vipPoints;
        this.wins = wins;
        this.playedGames = playedGames;
        this.deaths = deaths;
        this.kills = kills;
        this.fame = fame;
        this.ownKitList = ownKitList;
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

    public int getLcoins() {
        return lcoins;
    }

    public void setLcoins(int lcoins) {
        this.lcoins = lcoins;
    }

    public int getVipPoints() {
        return vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
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

    public List<String> getOwnKitList() {
        return ownKitList;
    }

    public void setOwnKitList(List<String> ownKitList) {
        this.ownKitList = ownKitList;
    }

    // Additional methods
    @JsonIgnore
    public int getLevel() {
        int lvl = kills / 100;
        lvl += wins / 2;
        return lvl;
    }
}