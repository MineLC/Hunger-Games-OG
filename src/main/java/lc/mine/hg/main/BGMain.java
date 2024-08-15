package lc.mine.hg.main;

import lc.mine.core.CorePlugin;
import lc.mine.hg.commands.BGConsole;
import lc.mine.hg.commands.BGPlayer;
import lc.mine.hg.data.database.Database;
import lc.mine.hg.data.database.mongodb.StartMongodb;
import lc.mine.hg.data.economy.EconomyManager;
import lc.mine.hg.data.economy.FameManager;
import lc.mine.hg.data.top.TopManager;
import lc.mine.hg.data.top.TopStorage;
import lc.mine.hg.data.user.User;
import lc.mine.hg.events.BGAbilitiesListener;
import lc.mine.hg.events.BGGameListener;
import lc.mine.hg.timers.GameTimer;
import lc.mine.hg.timers.InvincibilityTimer;
import lc.mine.hg.timers.PreGameTimer;
import lc.mine.hg.utilities.BGChat;
import lc.mine.hg.utilities.BGFiles;
import lc.mine.hg.utilities.BGKillsPAPI;
import lc.mine.hg.utilities.BGKit;
import lc.mine.hg.utilities.enums.GameState;
import lc.mine.hg.utilities.enums.Translation;
import org.bukkit.plugin.java.*;
import java.util.logging.*;
import org.bukkit.scoreboard.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import com.google.common.collect.*;
import java.io.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

public class BGMain extends JavaPlugin
{
    private static EconomyManager economyManager = new EconomyManager();
    private FameManager fameManager = new FameManager();

    public static GameState GAMESTATE;
    public static String HELP_MESSAGE;
    public static String SERVER_FULL_MSG;
    public static String GAME_IN_PROGRESS_MSG;
    public static String MOTD_PROGRESS_MSG;
    public static String MOTD_COUNTDOWN_MSG;
    public static String NO_KIT_MSG;
    public static String SERVER_TITLE;
    public static Integer COUNTDOWN_SECONDS;
    public static Integer FINAL_COUNTDOWN_SECONDS;
    public static Integer END_GAME_TIME;
    public static Integer MAX_GAME_RUNNING_TIME;
    public static Integer MINIMUM_PLAYERS;
    public static Integer GAME_ENDING_TIME;
    public static Boolean DEFAULT_KIT;
    public static Boolean END_GAME;
    public static Location spawn;
    public static ArrayList<Player> spectators;
    public static Integer COUNTDOWN;
    public static Integer FINAL_COUNTDOWN;
    public static Integer GAME_RUNNING_TIME;
    public static Integer WORLDRADIUS;
    public static BGMain instance;
    public static HashMap<String, Integer> kills;
    public static Logger log;
    public static HashMap<String, Team> TEAMS;
    public static String mapa;
    public static LinkedList<Player> gamers;
    public static String ganador;
    public final static SplittableRandom random = new SplittableRandom();
    public static HashMap<Player, Integer> Fame;
    public static World mainWorld;
    public static ArrayList<Player> frezee;
    public static CorePlugin core;
    public static boolean GEN_MAPS;

    public static Database database;
    
    static {
        resetGame();
    }

    public static void resetGame() {
        BGMain.GAMESTATE = GameState.PREGAME;
        BGMain.HELP_MESSAGE = null;
        BGMain.SERVER_FULL_MSG = "";
        BGMain.GAME_IN_PROGRESS_MSG = "";
        BGMain.MOTD_PROGRESS_MSG = "";
        BGMain.MOTD_COUNTDOWN_MSG = "";
        BGMain.NO_KIT_MSG = "";
        BGMain.SERVER_TITLE = null;
        BGMain.COUNTDOWN_SECONDS = 120;
        BGMain.FINAL_COUNTDOWN_SECONDS = 60;
        BGMain.END_GAME_TIME = 1;
        BGMain.MAX_GAME_RUNNING_TIME = 60;
        BGMain.MINIMUM_PLAYERS = 4;
        BGMain.GAME_ENDING_TIME = 50;
        BGMain.DEFAULT_KIT = false;
        BGMain.END_GAME = true;
        BGMain.spectators = new ArrayList<Player>();
        BGMain.COUNTDOWN = 0;
        BGMain.FINAL_COUNTDOWN = 0;
        BGMain.GAME_RUNNING_TIME = 0;
        BGMain.WORLDRADIUS = 250;
        BGMain.kills = new HashMap<String, Integer>();
        BGMain.log = Bukkit.getLogger();
        BGMain.TEAMS = new HashMap<String, Team>();
        BGMain.mapa = "default";
        BGMain.gamers = new LinkedList<Player>();
        BGMain.ganador = "Nadie";
        BGMain.Fame = new HashMap<Player, Integer>();
    }
    
    private static String oldWorld = "world";
    public static boolean loading = false;

    public static void loadMap() {
        BGMain.log.info("Deleting old world.");
        loading = true;
        BGMain.GEN_MAPS = BGFiles.worldconf.getBoolean("GEN_MAPS");
        final List<String> mapnames = (List<String>)BGFiles.worldconf.getStringList("WORLDS");
        final String map = mapnames.get(random.nextInt(mapnames.size()));
        final String[] splitmap = map.split(",");
        
        oldWorld = splitmap[0];
        BGMain.mainWorld = Bukkit.getServer().createWorld(new WorldCreator(map).generateStructures(false));
        BGMain.spawn = mainWorld.getSpawnLocation();

        Bukkit.getServer().unloadWorld(oldWorld, false);

        if (splitmap.length == 2) {
            BGMain.WORLDRADIUS = Integer.parseInt(splitmap[1]);
        }
        else {
            BGMain.WORLDRADIUS = 300;
        }
        loading = false;
        System.gc();
    }

    public static ArrayList<Player> getFrezee() {
        return BGMain.frezee;
    }
    
    public void onLoad() {
        BGMain.loading = true;
        BGMain.instance = this;

        try {
            new BGFiles();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void registerEvents() {
        final BGGameListener gl = new BGGameListener(this);
        final Enchants ench = new Enchants();
        final BGAbilitiesListener al = new BGAbilitiesListener();
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents((Listener)gl, (Plugin)this);
        pm.registerEvents((Listener)ench, (Plugin)this);
        pm.registerEvents((Listener)al, (Plugin)this);
    }
    
    public void registerCommands() {
        final ConsoleCommandSender console = Bukkit.getConsoleSender();
        if (this.getCommand("help") != null) {
            this.getCommand("help").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand help returns null");
        }
        if (this.getCommand("kit") != null) {
            this.getCommand("kit").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand kit returns null");
        }
        if (this.getCommand("rank") != null) {
            this.getCommand("rank").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand rank returns null");
        }
        if (this.getCommand("kitinfo") != null) {
            this.getCommand("kitinfo").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand kitinfo returns null");
        }
        if (this.getCommand("start") != null) {
            this.getCommand("start").setExecutor((CommandExecutor)new BGConsole());
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand start returns null");
        }
        if (this.getCommand("spawn") != null) {
            this.getCommand("spawn").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand spawn returns null");
        }
        if (this.getCommand("desbug") != null) {
            this.getCommand("desbug").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand desbug returns null");
        }
        if (this.getCommand("hack") != null) {
            this.getCommand("hack").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage("");
        }
        if (this.getCommand("fbattle") != null) {
            this.getCommand("fbattle").setExecutor((CommandExecutor)new BGConsole());
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand fbattle returns null");
        }
        if (this.getCommand("team") != null) {
            this.getCommand("team").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand team returns null");
        }
        if (this.getCommand("gamemaker") != null) {
            this.getCommand("gamemaker").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand gamemaker returns null");
        }
        if (this.getCommand("vanish") != null) {
            this.getCommand("vanish").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand vanish returns null");
        }
        if (this.getCommand("teleport") != null) {
            this.getCommand("teleport").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        if (this.getCommand("lobby") != null) {
            this.getCommand("lobby").setExecutor((CommandExecutor)new BGPlayer(this));
        }
        else {
            console.sendMessage(ChatColor.RED + "getCommand teleport returns null");
        }
    }
    
    public void onEnable() {
        BGMain.instance = this;
        final Plugin core = getServer().getPluginManager().getPlugin("LCCore");
        if (core == null) {
            getLogger().warning("LCCore don't found");
            getLogger().warning("CHG will not start");
            return;
        }
        BGMain.core = (CorePlugin)core;
        database = new StartMongodb().start(getLogger(), getConfig());
        if (database == null ) {
            getLogger().warning("MongoDB don't found");
            getLogger().warning("CHG will not start");
            return;
        }

        new TopManager().start();
        loadMap();

        BGMain.mainWorld.setDifficulty(Difficulty.PEACEFUL);
        (BGMain.log = Bukkit.getLogger()).info("Loading configuration options.");
        BGMain.SERVER_TITLE = this.getConfig().getString("MESSAGE.SERVER_TITLE");
        BGMain.HELP_MESSAGE = this.getConfig().getString("MESSAGE.HELP_MESSAGE");
        BGMain.DEFAULT_KIT = this.getConfig().getBoolean("DEFAULT_KIT");
        BGMain.NO_KIT_MSG = this.getConfig().getString("MESSAGE.NO_KIT_PERMISSION");
        BGMain.GAME_IN_PROGRESS_MSG = this.getConfig().getString("MESSAGE.GAME_PROGRESS");
        BGMain.SERVER_FULL_MSG = this.getConfig().getString("MESSAGE.SERVER_FULL");
        BGMain.MOTD_PROGRESS_MSG = this.getConfig().getString("MESSAGE.MOTD_PROGRESS");
        BGMain.MOTD_COUNTDOWN_MSG = this.getConfig().getString("MESSAGE.MOTD_COUNTDOWN");
        BGMain.MINIMUM_PLAYERS = this.getConfig().getInt("MINIMUM_PLAYERS_START");
        BGMain.MAX_GAME_RUNNING_TIME = this.getConfig().getInt("TIME.MAX_GAME-MIN");
        BGMain.FINAL_COUNTDOWN_SECONDS = this.getConfig().getInt("TIME.FINAL_COUNTDOWN-SEC");
        BGMain.END_GAME_TIME = this.getConfig().getInt("TIME.INCREASE_DIFFICULTY-MIN");
        copy(BGMain.instance.getResource("en.yml"), new File(BGMain.instance.getDataFolder(), "lang.yml"));
        Translation.e = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(BGMain.instance.getDataFolder(), "lang.yml"));
        if (BGMain.WORLDRADIUS < 60) {
            BGMain.log.warning("Worldborder radius has to be 60 or higher!");
            BGMain.WORLDRADIUS = 100;
        }
        new BGKillsPAPI(this).register();
        this.registerEvents();
        this.registerCommands();
        new BGKit(this);
        new BGChat();
        final World world = BGMain.mainWorld;
        BGMain.spawn = world.getSpawnLocation();
        world.setAutoSave(true);
        final WorldBorder wb = world.getWorldBorder();
        wb.setCenter(BGMain.spawn);
        wb.setWarningDistance(15);
        wb.setSize(250.0);
        world.setTime(6000L);
        world.setGameRuleValue("spectatorsGenerateChunks", "false");
        world.setGameRuleValue("KeepInventory", "false");
        BGMain.COUNTDOWN = BGMain.COUNTDOWN_SECONDS;
        BGMain.FINAL_COUNTDOWN = BGMain.FINAL_COUNTDOWN_SECONDS;
        BGMain.GAME_RUNNING_TIME = 0;
        BGMain.GAMESTATE = GameState.PREGAME;
        BGMain.log.info("Fase De Juego: 1 - Esperando");
        final String command = "minecraft:gamerule sendCommandFeedback false";
        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
        BGMain.frezee = new ArrayList<Player>();
    }
    
    public void onDisable() {       
        if (database != null) {
            database.close();
        }
        if (TopStorage.get() != null) {
            TopStorage.get().saveAll();
        }
        Bukkit.getServer().getScheduler().cancelAllTasks();
    }
    
    public static void startgame() {
        BGMain.Fame.clear();
        BGMain.log.info("Fase de juego: 2 - Comenzando");
        PreGameTimer.cancel();
        new InvincibilityTimer();
        BGMain.GAMESTATE = GameState.INVINCIBILITY;
        final World world = BGMain.mainWorld;
        world.setAutoSave(false);
        world.setDifficulty(Difficulty.NORMAL);
        world.setTime(0L);
        final List<Location> randomlocs = Lists.newArrayList();
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, 1.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, 2.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, 3.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, 4.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, -1.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, -2.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, -3.0));
        randomlocs.add(getSpawn().clone().add(0.0, 0.0, -4.0));
        randomlocs.add(getSpawn().clone().add(1.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(2.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(3.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(4.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(-1.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(-2.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(-3.0, 0.0, 0.0));
        randomlocs.add(getSpawn().clone().add(-4.0, 0.0, 0.0));
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (isSpectator(p)) {
                continue;
            }
            if (p.isInsideVehicle()) {
                p.getVehicle().eject();
            }
            p.teleport((Location)randomlocs.get(BGMain.random.nextInt(16)));
            p.setFlying(false);
            p.setFireTicks(0);
            p.setAllowFlight(false);
            BGKit.giveKit(p);
        }
        BGChat.printTimeChat(Translation.GAMES_HAVE_BEGUN.t());
        BGChat.printTimeChat(Translation.INVINCIBLE_FOR.t().replace("<time>", TIME(BGMain.FINAL_COUNTDOWN_SECONDS)));
        world.getWorldBorder().setSize(550.0);
        world.getWorldBorder().setSize(100.0, 600L);
    }

    public static void sendMessage(final User jug, final int x) {
        Player player = Bukkit.getPlayer(jug.getId());
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.3f);
        player.sendMessage(ChatColor.GOLD + "+" + x + " LCoins");
    }
    
    public static void sendVipMessage(final User jug, final int x) {
        Player player = Bukkit.getPlayer(jug.getId());
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.3f);
        player.sendMessage(ChatColor.GOLD + "+" + x + " Vip Points");
    }
    
    public static void copy(final InputStream in, final File file) {
        try {
            final OutputStream out = new FileOutputStream(file);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Location getSpawn() {
        return BGMain.spawn;
    }
    
    public static LinkedList<Player> getGamers() {
        return BGMain.gamers;
    }
    
    public static void checkwinner() {
        if (getGamers().size() <= 1) {
            if (getGamers().size() == 0) {
                GameTimer.cancel();
                Bukkit.getServer().shutdown();
            }
            else {
                GameTimer.cancel();
                final User jug = database.getCached(getGamers().get(0).getUniqueId());
                BGMain.ganador = Bukkit.getPlayer(jug.getId()).getName();
                jug.setWins(jug.getWins() + 1);
                jug.setPlayedGames(jug.getPlayedGames() + 1);
                jug.setVipPoints(jug.getVipPoints() + 1);
                sendVipMessage(jug, 10);
                final String title = "title " + ganador + " title [{\"text\":\"Ganaste!\",\"color\":\"gold\"}]";
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), title);
                final Location loc = Bukkit.getPlayer(jug.getId()).getEyeLocation();
                spawnRandomFirework(loc.clone().add(1.0, 3.0, 1.0));
                spawnRandomFirework(loc.clone().add(-1.0, 3.0, -1.0));
                spawnRandomFirework(loc.clone().add(1.0, 3.0, -1.0));
                spawnRandomFirework(loc.clone().add(-1.0, 3.0, 1.0));
                Bukkit.getPlayer(jug.getId()).playSound(loc, Sound.LEVEL_UP, 1.0f, 2.5f);
                economyManager.addLcoins(jug.getId(), 10);
                sendMessage(jug, 10);
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)BGMain.instance, (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        BGMain.loading = true;
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            p.kickPlayer(ChatColor.GOLD + BGMain.ganador + " es el ganador del juego!");
                        }
                        BGMain.resetGame();
                        BGMain.loadMap();
                    }
                }, 200L);
                for (final Player Online : Bukkit.getOnlinePlayers()) {
                    Online.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(ChatColor.STRIKETHROUGH).append("----------------------------------").toString());
                    Online.sendMessage(new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("                      HG").toString());
                    Online.sendMessage("");
                    Online.sendMessage(ChatColor.YELLOW + "                   Ganador: " + ChatColor.GRAY + ganador);
                    Online.sendMessage("");
                    SayKillWinners(Online);
                    Online.sendMessage("");
                    Online.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(ChatColor.STRIKETHROUGH).append("----------------------------------").toString());
                }
            }
        }
    }
    
    public static void SayKillWinners(final Player p) {
        final Map<String, Integer> asesinatos = sortByValue(BGMain.kills);
        int st = 0;
        ChatColor color = ChatColor.YELLOW;
        for (final Map.Entry<String, Integer> pk : asesinatos.entrySet()) {
            if (++st == 2) {
                color = ChatColor.GOLD;
            }
            else if (st == 3) {
                color = ChatColor.RED;
            }
            p.sendMessage(color + "             Asesino #" + st + ": " + ChatColor.GRAY + pk.getKey() + " - " + pk.getValue());
            if (st >= 3) {
                break;
            }
        }
    }

    public static Map<String, Integer> sortByValue(final Map<String, Integer> unsortMap) {
        // Create a new list to hold the values of the map
        final List<Integer> list = new LinkedList<Integer>();

        // Add all the values of the map to the list
        list.addAll(unsortMap.values());

        // Sort the list in descending order using a custom comparator
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(final Integer o1, final Integer o2) {
                // Compare the values of the two integers
                return o2.compareTo(o1);
            }
        });

        // Create a new map to hold the sorted entries
        final Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

        // Iterate over the sorted list and add the corresponding entries to the new map
        for (int i = 0; i < list.size(); i++) {
            final Integer value = list.get(i);
            for (final Map.Entry<String, Integer> entry : unsortMap.entrySet()) {
                if (entry.getValue().equals(value)) {
                    sortedMap.put(entry.getKey(), entry.getValue());
                    break;
                }
            }
        }

        // Return the new map with the sorted entries
        return sortedMap;
    }
    
    public static void spawnRandomFirework(final Location loc) {
        final Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        final FireworkMeta fwm = fw.getFireworkMeta();
        final int rt = BGMain.random.nextInt(2) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.STAR;
        if (rt == 1) {
            type = FireworkEffect.Type.STAR;
        }
        if (rt == 2) {
            type = FireworkEffect.Type.STAR;
        }
        final Color c1 = Color.RED;
        final Color c2 = Color.YELLOW;
        final Color c3 = Color.ORANGE;
        final FireworkEffect effect = FireworkEffect.builder().flicker(BGMain.random.nextBoolean()).withColor(c1).withColor(c2).withFade(c3).with(type).trail(BGMain.random.nextBoolean()).build();
        fwm.addEffect(effect);
        final int rp = BGMain.random.nextInt(3) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }
    
    public static void deleteDir(final File dir) {
        if (dir.isDirectory()) {
            final String[] children = dir.list();
            for (int i = 0; i < children.length; ++i) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }
    
    public static String TIME(final Integer i) {
        if (i >= 60) {
            final Integer time = i / 60;
            String add = "";
            if (time > 1) {
                add = "s";
            }
            return time + ChatColor.GREEN.toString() + " minuto" + add;
        }
        final Integer time = i;
        String add = "";
        if (time > 1) {
            add = "s";
        }
        return time + ChatColor.GREEN.toString() + " segundo" + add;
    }
    
    public static Boolean isSpectator(final Player p) {
        return BGMain.spectators.contains(p);
    }
    
    public static ArrayList<Player> getSpectators() {
        return BGMain.spectators;
    }
    
    public static void addSpectator(final Player p) {
        BGMain.spectators.add(p);
        Bukkit.getScheduler().runTaskLater((Plugin)BGMain.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                p.setGameMode(GameMode.SPECTATOR);
                p.setAllowFlight(true);
                p.setFlying(true);
            }
        }, 2L);
    }
    
    public static void remSpectator(final Player p) {
        BGMain.spectators.remove(p);
        p.getInventory().clear();
    }
    
    public static String getRankHGPrefix(final String rank) {
        String c = "";
        switch (rank) {
            case "Mitico": {
                c = "&d";
                break;
            }
            case "Ilustre": {
                c = "&1";
                break;
            }
            case "Conquistador": {
                c = "&3";
                break;
            }
            case "Rey": {
                c = "&c";
                break;
            }
            case "Feroz": {
                c = "&e";
                break;
            }
            case "Heroe": {
                c = "&b";
                break;
            }
            case "Moral": {
                c = "&f";
                break;
            }
            case "Nuevo": {
                c = "&7";
                break;
            }
            case "Emperador": {
                c = "&4";
                break;
            }
            case "Renombrado": {
                c = "&9";
                break;
            }
            case "Poderoso": {
                c = "&6";
                break;
            }
            case "Eminente": {
                c = "&2";
                break;
            }
            case "Aprendiz": {
                c = "&a";
                break;
            }
            case "Legendario": {
                c = "&5";
                break;
            }
            default:
                break;
        }
        return ChatColor.translateAlternateColorCodes('&', String.valueOf(c) + rank);
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public FameManager getFameManager() {
        return fameManager;
    }
}
