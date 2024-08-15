package lc.mine.hg.data.database.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.Set;
import java.util.UUID;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import lc.mine.hg.data.database.Database;
import lc.mine.hg.data.user.User;

public final class MongoData implements Database {

    private final Map<UUID, User> cache = new HashMap<>();
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    private static final String
        KIT = "kit",
        WINS = "wins",
        PLAYED = "played",
        DEATHS = "deaths",
        KILLS = "kills",
        FAME = "fame",
        RANK = "rank";

    MongoData(MongoClient client, MongoCollection<Document> collection) {
        this.client = client;
        this.collection = collection;
    }
    
    @Override
    public User getCached(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public void save(final Player player) {
        final User data = cache.remove(player.getUniqueId());
        if (data == null) {
            return;
        }
        if (data instanceof User.New) {
            collection.insertOne(getNew(data));
            return;
        }
        final Bson query = createUpdateQuery(data);
        if (query != null) {
            CompletableFuture.runAsync(() -> collection.updateOne(Filters.eq("_id", player.getName()), query));
        }
    }
    private Document getNew(final User user) {
        final Document document = new Document();
        document.put("_id", user.getName());
        document.put(KIT, user.getKit());
        document.put(WINS, user.getWins());
        document.put(PLAYED, user.getPlayedGames());
        document.put(DEATHS, user.getDeaths());
        document.put(KILLS, user.getKills());
        document.put(FAME, user.getFame());
        return document;
    }


    private Bson createUpdateQuery(final User data) {
        final List<Bson> update = new ArrayList<>();

        setIf(update, KIT, data.getKit(), null);
        setIf(update, RANK, data.getRank(), null);
        setIf(update, WINS, data.getWins(), 0);
        setIf(update, PLAYED, data.getPlayedGames(), 0);
        setIf(update, DEATHS, data.getDeaths(), 0);
        setIf(update, KILLS, data.getKills(), 0);
        setIf(update, FAME, data.getFame(), 0);

        if (update.isEmpty()) {
            return null;
        } 
        return Updates.combine(update);
    }
    private void setIf(final List<Bson> updates, final String name, final Object value, final Object compare) {
        if (value != compare && !value.equals(compare)) {
            updates.add(Updates.set(name, value));
        }
    }

    @Override
    public User load(final Player player) {
        final Document document = collection.find(Filters.eq("_id", player.getName())).limit(1).first();
        if (document == null) {
            final User user = new User(player.getUniqueId(), player.getName(), "", null, 0, 0, 0, 0, 0);
            cache.put(player.getUniqueId(), user);
            return user;
        }
        final String kit = getOrDefault(document.getString(KIT), "");
        final String rank = getOrDefault(document.getString(RANK), null);
        final int wins = getOrDefault(document.getInteger(WINS), 0).intValue();
        final int played = getOrDefault(document.getInteger(PLAYED), 0).intValue();
        final int deaths = getOrDefault(document.getInteger(DEATHS), 0).intValue();
        final int kills = getOrDefault(document.getInteger(KILLS), 0).intValue();
        final int fame = getOrDefault(document.getInteger(FAME), 0).intValue();

        final User user = new User(
            player.getUniqueId(),
            player.getName(),
            kit,
            rank,
            wins,
            played,
            deaths,
            kills,
            fame
        );

        cache.put(player.getUniqueId(), user);
        return user;
    }
    private <T> T getOrDefault(final T value, final T returnDefault) {
        return (value == null) ? returnDefault : value;
    }
    
    @Override
    public void create(Player player, User data) {
        cache.put(player.getUniqueId(), data);
    }

    @Override
    public void close() {
        if (cache.isEmpty()) {
            client.close();
            return;
        }
        final Set<Entry<UUID, User>> entries = cache.entrySet();
        final List<Document> toInsert = new ArrayList<>();

        for (final Entry<UUID, User> entry : entries) {
            if (entry.getValue() instanceof User.New) {
                toInsert.add(getNew(entry.getValue()));
                return;
            }
            final Bson query = createUpdateQuery(entry.getValue());
            if (query != null) {
                collection.updateOne(Filters.eq("_id", entry.getValue().getName()), query);
            }
        }
        if (!toInsert.isEmpty()) {
            collection.insertMany(toInsert);
        }
        client.close();
    }

    @Override
    public Map<UUID, User> getUsers() {
        return cache;
    }
}