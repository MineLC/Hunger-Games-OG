package lc.mine.hg.utilities;

import lc.mine.hg.data.user.User;
import lc.mine.hg.main.BGMain;
import me.clip.placeholderapi.expansion.*;
import org.bukkit.entity.*;

public class BGKillsPAPI extends PlaceholderExpansion
{
    private BGMain plugin;
    
    public BGKillsPAPI(final BGMain plugin) {
        this.plugin = plugin;
    }
    
    public boolean persist() {
        return true;
    }
    
    public boolean canRegister() {
        return true;
    }
    
    public String getAuthor() {
        return "GatitoGuapote";
    }
    
    public String getIdentifier() {
        return "minelcchg";
    }
    
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }
    
    public String onPlaceholderRequest(final Player player, final String identifier) {

        if (player == null) {
            return "";
        }
        User jug = BGMain.database.getCached(player.getUniqueId());
        if (identifier.equals("wins")) {
            return String.valueOf(jug.getWins());
        }
        if (identifier.equals("playeds")) {
            return String.valueOf(jug.getPlayedGames());
        }
        if (identifier.equals("kdr")) {
            //value of kdr is double
            //kdr = kills/deaths
            //ajusta si la division es cero que el valor default sea 0.0
            if (jug.getKills() == 0 && jug.getDeaths() == 0) {
                return String.valueOf(0.0);
            }
            if (jug.getDeaths() == 0) {
                return String.valueOf(jug.getKills());
            }
            double kdr = jug.getKills() / jug.getDeaths();
            return String.valueOf(kdr);


        }
        if (identifier.equals("enpartida")) {
            return String.valueOf(BGMain.getGamers().size());
        }
        if (identifier.equals("espectando")) {
            return String.valueOf(BGMain.getSpectators().size());
        }
        if (identifier.equals("lcoins")) {
            return String.valueOf(jug.getLcoins());
        }
        if (identifier.equals("vippoints")) {
            return String.valueOf(jug.getVipPoints());
        }
        if (identifier.equals("rank")) {
            String jrank = jug.getRank();
            String rank = null;
            switch (jrank) {
                case "Feroz":
                    rank = "Heroe Feroz";
                    break;
                case "Poderoso":
                    rank = "Heroe Poderoso";
                    break;
                case "Mortal":
                    rank = "Heroe Mortal";
                    break;
                case "Terrorifico":
                    rank = "Heroe Terrorifico";
                    break;
                case "Conquistador":
                    rank = "Heroe Conquistador";
                    break;
                case "Renombrado":
                    rank = "Heroe Renombrado";
                    break;
                case "Ilustre":
                    rank = "Heroe Ilustre";
                    break;
                case "Eminente":
                    rank = "Heroe Eminente";
                    break;
                case "Rey":
                    rank = "Rey Heroe";
                    break;
                case "Emperador":
                    rank = "Heroe Emperador";
                    break;
                case "Legendario":
                    rank = "Heroe Legendario";
                    break;
                case "Mitico":
                    rank = "Heroe Mitico";
                    break;
                default:
                    rank = jug.getRank();
                    break;

            }
            return rank;
        }
        if (identifier.equals("alphalevel")) {
            final int kills = jug.getFame();
            String a = BGMain.getRankHGPrefix("Nuevo");
            if (kills >= 300 && kills < 500) {
                a = BGMain.getRankHGPrefix("Aprendiz");
            }
            else if (kills >= 500 && kills < 1000) {
                a = BGMain.getRankHGPrefix("Heroe");
            }
            else if (kills >= 1000 && kills < 2000) {
                a = BGMain.getRankHGPrefix("Feroz");
            }
            else if (kills >= 2000 && kills < 3000) {
                a = BGMain.getRankHGPrefix("Poderoso");
            }
            else if (kills >= 3000 && kills < 4000) {
                a = BGMain.getRankHGPrefix("Mortal");
            }
            else if (kills >= 4000 && kills < 5000) {
                a = BGMain.getRankHGPrefix("Terrorifico");
            }
            else if (kills >= 5000 && kills < 6000) {
                a = BGMain.getRankHGPrefix("Conquistador");
            }
            else if (kills >= 6000 && kills < 7000) {
                a = BGMain.getRankHGPrefix("Renombrado");
            }
            else if (kills >= 7000 && kills < 8000) {
                a = BGMain.getRankHGPrefix("Ilustre");
            }
            else if (kills >= 8000 && kills < 9000) {
                a = BGMain.getRankHGPrefix("Eminente");
            }
            else if (kills >= 9000 && kills < 10000) {
                a = BGMain.getRankHGPrefix("Rey");
            }
            else if (kills >= 10000 && kills < 15000) {
                a = BGMain.getRankHGPrefix("Emperador");
            }
            else if (kills >= 15000 && kills < 20000) {
                a = BGMain.getRankHGPrefix("Legendario");
            }
            else if (kills >= 20000) {
                a = BGMain.getRankHGPrefix("Mitico");
            }
            return a;
        }
        if (identifier.equals("numerlevel")) {
            String level = "";
            switch (jug.getRank()) {
                case "Heroe": {
                    level = "12 ";
                    break;
                }
                case "Feroz": {
                    level = "11 ";
                    break;
                }
                case "Poderoso": {
                    level = "10 ";
                    break;
                }
                case "Moral": {
                    level = "9 ";
                    break;
                }
                case "Terrorifico": {
                    level = "8 ";
                    break;
                }
                case "Conquistador": {
                    level = "7 ";
                    break;
                }
                case "Renombrado": {
                    level = "6 ";
                    break;
                }
                case "Ilustre": {
                    level = "5 ";
                    break;
                }
                case "Eminente": {
                    level = "4 ";
                    break;
                }
                case "Rey": {
                    level = "3 ";
                    break;
                }
                case "Emperador": {
                    level = "2 ";
                    break;
                }
                case "Legendario": {
                    level = "1 ";
                    break;
                }
                default:
                    break;
            }
            return level;
        }
        if (identifier.equals("kit")) {
            //first letter in capital
            return String.valueOf(jug.getKit().substring(0, 1).toUpperCase() + jug.getKit().substring(1));
        }
        if (identifier.equals("kills")) {
            return String.valueOf(jug.getKills());
        }
        if (identifier.equals("deaths")) {
            return String.valueOf(jug.getDeaths());
        }

        return identifier;
    }
}
