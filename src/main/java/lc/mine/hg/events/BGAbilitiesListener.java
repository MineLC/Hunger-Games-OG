package lc.mine.hg.events;

import lc.mine.hg.main.BGMain;
import lc.mine.hg.main.Habilidades;
import lc.mine.hg.utilities.*;
import lc.mine.hg.utilities.enums.GameState;
import lc.mine.hg.utilities.enums.Translation;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import org.bukkit.projectiles.*;
import org.bukkit.util.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class BGAbilitiesListener implements Listener
{
    public static ArrayList<Player> cooldown;
    
    static {
        BGAbilitiesListener.cooldown = new ArrayList<Player>();
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        final Action a = event.getAction();
        if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && (BGKit.hasAbility(p, Habilidades.STRENGHTCOOKIE.getId()) & p.getItemInHand().getType() == Material.COOKIE)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, BGFiles.abconf.getInt("AB.5.Duration") * 20, 0));
            p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.COOKIE, 1) });
            p.playSound(p.getLocation(), Sound.BURP, 1.0f, 1.0f);
        }
        if ((a == Action.LEFT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR) && BGKit.hasAbility(p, Habilidades.RIGHHANDTFIRE.getId()) && p.getItemInHand() != null && p.getItemInHand().getType().equals((Object)Material.FIREBALL)) {
            final Vector lookat = p.getLocation().getDirection().multiply(10);
            final Fireball fire = (Fireball)p.getWorld().spawn(p.getLocation().add(lookat), (Class)Fireball.class);
            fire.setShooter((ProjectileSource)p);
            p.playSound(p.getLocation(), Sound.FIRE, 1.0f, 1.5f);
            p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.FIREBALL, 1) });
        }
        try {
            if (BGKit.hasAbility(p, Habilidades.AXETHUNDER.getId()) && a == Action.RIGHT_CLICK_BLOCK && p.getItemInHand().getType() == Material.DIAMOND_AXE) {
                if (!BGAbilitiesListener.cooldown.contains(p)) {
                    BGAbilitiesListener.cooldown.add(p);
                    BGCooldown.thorCooldown(p);
                    final Block block = event.getClickedBlock();
                    final Location loc = block.getLocation();
                    final World world = Bukkit.getServer().getWorlds().get(0);
                    if (event.getClickedBlock().getType() != Material.BEDROCK) {
                        event.getClickedBlock().setType(Material.NETHERRACK);
                    }
                    event.getClickedBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
                    world.strikeLightning(loc);
                }
                else {
                    BGChat.printPlayerChat(p, BGFiles.abconf.getString("AB.11.Expired"));
                }
            }
            if (BGKit.hasAbility(p, 16) && p.getItemInHand().getType() == Material.APPLE && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)) {
                if (!BGAbilitiesListener.cooldown.contains(p)) {
                    BGAbilitiesListener.cooldown.add(p);
                    BGCooldown.ghostCooldown(p);
                    p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.APPLE, 1) });
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, BGFiles.abconf.getInt("AB.16.Duration") * 20, 1));
                    p.playSound(p.getLocation(), Sound.PORTAL_TRIGGER, 1.0f, 1.0f);
                    BGChat.printPlayerChat(p, BGFiles.abconf.getString("AB.16.invisible"));
                }
                else {
                    BGChat.printPlayerChat(p, BGFiles.abconf.getString("AB.16.Expired"));
                }
            }
            if (BGKit.hasAbility(p, 21) && p.getItemInHand().getType() == Material.POTATO && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)) {
                p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.POTATO, 1) });
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, BGFiles.abconf.getInt("AB.21.Duration") * 20, 1));
                p.playSound(p.getLocation(), Sound.ENDERMAN_STARE, 1.0f, 1.0f);
            }
            if (BGKit.hasAbility(p, 22) && BGMain.GAMESTATE == GameState.GAME && p.getItemInHand().getType() == Material.WATCH && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)) {
                if (!BGAbilitiesListener.cooldown.contains(p)) {
                    BGAbilitiesListener.cooldown.add(p);
                    BGCooldown.timeCooldown(p);
                    p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.WATCH, 1) });
                    final int radius = BGFiles.abconf.getInt("AB.22.radius");
                    p.playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 1.0f);
                    final List<Entity> entities = (List<Entity>)p.getNearbyEntities((double)(radius + 30), (double)(radius + 30), (double)(radius + 30));
                    for (final Entity e : entities) {
                        if (e.getType().equals((Object)EntityType.PLAYER)) {
                            if (BGMain.isSpectator((Player)e)) {
                                continue;
                            }
                            final Player target = (Player)e;
                            if (BGTeam.isInTeam(p, target.getName())) {
                                continue;
                            }
                            if (p.getLocation().distance(target.getLocation()) >= radius) {
                                continue;
                            }
                            BGCooldown.freezeCooldown(target);
                            String text = BGFiles.abconf.getString("AB.22.target");
                            text = text.replace("<player>", p.getName());
                            BGChat.printPlayerChat(target, text);
                            BGCooldown.freezeCooldown(target);
                            BGMain.frezee.add(target);
                            p.playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, -1.0f);
                            p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, 2.0f);
                        }
                    }
                    BGChat.printPlayerChat(p, BGFiles.abconf.getString("AB.22.success"));
                }
                else {
                    BGChat.printPlayerChat(p, BGFiles.abconf.getString("AB.22.Expired"));
                }
            }
        }
        catch (NullPointerException ex) {}
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void Snowballs(final ProjectileHitEvent e) {
        final Projectile proj = e.getEntity();
        if (proj instanceof Snowball) {
            final Snowball snow = (Snowball)proj;
            final LivingEntity shooter = (LivingEntity)snow.getShooter();
            if (shooter instanceof Player) {
                final Player p = (Player)shooter;
                if (BGKit.hasAbility(p, 37)) {
                    final Location loc = snow.getLocation();
                    final String world = snow.getWorld().getName();
                    Bukkit.getServer().getWorld(world).getBlockAt(loc).setType(Material.WEB);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(final ProjectileHitEvent event) {
        final Projectile entity = event.getEntity();
        if (entity.getType() == EntityType.ARROW) {
            final Arrow arrow = (Arrow)entity;
            final LivingEntity shooter = (LivingEntity)arrow.getShooter();
            if (shooter.getType() == EntityType.PLAYER) {
                final Player player = (Player)shooter;
                if (BGMain.isSpectator(player)) {
                    return;
                }
                if (!BGKit.hasAbility(player, 1)) {
                    return;
                }
                Bukkit.getServer().getWorlds().get(0).createExplosion(arrow.getLocation(), 2.0f, false);
                arrow.remove();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(final EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) {
            return;
        }
        final Player p = e.getEntity().getKiller();
        if (BGKit.hasAbility(p, 7) && e.getEntityType() == EntityType.PIG) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.PORK, BGFiles.abconf.getInt("AB.7.Amount")));
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player p = event.getEntity();
        if (BGKit.hasAbility(p, 23)) {
            Bukkit.getServer().getWorlds().get(0).createExplosion(p.getLocation(), 2.0f, BGFiles.abconf.getBoolean("AB.23.Burn"));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (BGMain.isSpectator(p)) {
                return;
            }
            if (BGKit.hasAbility(p, 37)) {
                event.setCancelled(true);
            }
            else if (BGKit.hasAbility(p, 8)) {
                if (event.getDamage() > 4.0) {
                    event.setCancelled(true);
                    p.damage(4.0);
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    final List<Entity> nearbyEntities = (List<Entity>)event.getEntity().getNearbyEntities(5.0, 5.0, 5.0);
                    for (final Entity target : nearbyEntities) {
                        if (target instanceof Player) {
                            final Player t = (Player)target;
                            if (BGMain.isSpectator(t)) {
                                continue;
                            }
                            if (t.getName() == p.getName()) {
                                continue;
                            }
                            if (t.isSneaking()) {
                                t.damage(event.getDamage() / 2.0, event.getEntity());
                            }
                            else {
                                t.damage(event.getDamage(), event.getEntity());
                            }
                        }
                    }
                }
                else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    if (BGKit.hasAbility(p, 30) & !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 320, 1));
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.2f);
                    }
                    else if (BGKit.hasAbility(p, 6) & !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 260, 0));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 280, 1));
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.2f);
                    }
                }
                if (BGKit.hasAbility(p, 18)) {
                    if (event.getDamage() > 2.0) {
                        event.setDamage(event.getDamage() - 2.0);
                    }
                    else {
                        event.setDamage(event.getDamage() / 2.0);
                    }
                }
            }
        }
        else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            if (BGKit.hasAbility(p, 30) & !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 320, 1));
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.2f);
            }
            else if (BGKit.hasAbility(p, 6) & !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 260, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 280, 1));
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.2f);
            }
        }
        if (BGKit.hasAbility(p, 18)) {
            if (event.getDamage() > 2.0) {
                event.setDamage(event.getDamage() - 2.0);
            }
            else {
                event.setDamage(event.getDamage() / 2.0);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player p = event.getPlayer();
        final Block b = event.getBlock();
        if (BGKit.hasAbility(p, 2) && b.getType() == Material.LOG) {
            final World w = Bukkit.getServer().getWorlds().get(0);
            Double y = b.getLocation().getY() + 1.0;
            final Location l = new Location(w, b.getLocation().getX(), (double)y, b.getLocation().getZ());
            while (l.getBlock().getType() == Material.LOG) {
                l.getBlock().breakNaturally();
                ++y;
                l.setY((double)y);
            }
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();
        final Player p = event.getPlayer();
        if (BGKit.hasAbility(p, 10)) {
            if (block.getType() == Material.CROPS) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                block.setData(CropState.RIPE.getData());
            }
            if (block.getType() == Material.MELON_SEEDS) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                block.setData(CropState.RIPE.getData());
            }
            if (block.getType() == Material.PUMPKIN_SEEDS) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                block.setData(CropState.RIPE.getData());
            }
            if (block.getType() == Material.SAPLING) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                final TreeType t = this.getTree(block.getData());
                Bukkit.getServer().getWorlds().get(0).generateTree(block.getLocation(), t);
            }
        }
    }
    
    public TreeType getTree(final int data) {
        TreeType tretyp = TreeType.TREE;
        switch (data) {
            case 0: {
                tretyp = TreeType.TREE;
                break;
            }
            case 1: {
                tretyp = TreeType.REDWOOD;
                break;
            }
            case 2: {
                tretyp = TreeType.BIRCH;
                break;
            }
            case 3: {
                tretyp = TreeType.JUNGLE;
                break;
            }
            default: {
                tretyp = TreeType.TREE;
                break;
            }
        }
        return tretyp;
    }
    
    @EventHandler
    public void onFlash(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (BGKit.hasAbility(p, 31) && p.getInventory().getItemInHand().getType() == Material.REDSTONE_TORCH_ON && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            if (BGAbilitiesListener.cooldown.contains(p)) {
                p.sendMessage(ChatColor.RED + "Necesitas esperar para volver a usarlo!");
            }
            else {
                final Location loc = e.getPlayer().getTargetBlock((HashSet<Byte>) null, 500).getLocation();
                if (loc.getBlock().getType() == Material.AIR) {
                    p.sendMessage(ChatColor.RED + "Necesitas mirar un bloque para teletransportarte");
                    return;
                }
                if (loc.distance(e.getPlayer().getLocation()) > BGFiles.abconf.getInt("AB.31.Distance")) {
                    p.sendMessage(ChatColor.RED + "No puedes teletransportarte tan lejos");
                    return;
                }
                BGAbilitiesListener.cooldown.add(p);
                BGCooldown.flashCooldown(p);
                p.setFallDistance(0.0f);
                loc.add(0.0, 1.0, 0.0);
                p.teleport(loc);
                p.setFallDistance(0.0f);
                final int distance = (int)(p.getLocation().distance(loc) / 2.0);
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, distance, 0));
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -1.0f);
            }
        }
        else if (BGKit.hasAbility(p, 32) && e.getPlayer().getItemInHand().getType() == Material.FIREWORK) {
            e.setCancelled(true);
            if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) && e.getMaterial() == Material.FIREWORK && !p.isSneaking()) {
                final Block b = p.getLocation().getBlock();
                if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    p.setFallDistance(-5.0f);
                    final Vector vector = p.getEyeLocation().getDirection();
                    vector.multiply(0.6f);
                    vector.setY(1);
                    p.setVelocity(vector);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        final Entity defender = event.getEntity();
        if (BGMain.GAMESTATE == GameState.PREGAME && !(event.getEntity() instanceof Player)) {
            return;
        }
        if (BGMain.GAMESTATE != GameState.GAME && event.getEntity() instanceof Player) {
            return;
        }
        if (event.getEntity().isDead()) {
            return;
        }
        if (event.getDamager() instanceof Arrow) {
            final Arrow arrow = (Arrow)event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                final Player p = (Player)arrow.getShooter();
                final LivingEntity victom = (LivingEntity)event.getEntity();
                if (event.getEntity() instanceof LivingEntity && victom instanceof Player) {
                    final Player v = (Player)victom;
                    BGChat.printPlayerChat(p, "&aAhora &e" + v.getName() + " &atiene &4" + Math.round(v.getHealth() * 100.0) / 100L + "HP");
                    if (!BGKit.hasAbility(p, Habilidades.INSTANTKILLARROW.getId())) {
                        return;
                    }
                    if (p.getLocation().distance(event.getEntity().getLocation()) >= BGFiles.abconf.getInt("AB.9.Distance")) {
                        final ItemStack helmet = v.getInventory().getHelmet();
                        if (helmet == null) {
                            BGChat.printDeathChat(Translation.HEADSHOT_DEATH.t().replace("<victom>", v.getName()).replace("<player>", p.getName()));
                            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                            BGChat.printDeathChat(Translation.PLAYERS_REMAIN.t().replace("<amount>", new StringBuilder(String.valueOf(BGMain.getGamers().size() - 1)).toString()));
                            BGChat.printDeathChat("");
                            final Location light = v.getLocation();
                            Bukkit.getServer().getWorlds().get(0).strikeLightningEffect(light.add(0.0, 100.0, 0.0));
                            v.setHealth(0.0);
                            v.kickPlayer(ChatColor.RED + Translation.HEADSHOT_DEATH.t().replace("<victom>", v.getName()).replace("<player>", p.getName()));
                        }
                        else {
                            helmet.setDurability((short)(helmet.getDurability() + 20));
                            v.setHealth(2.0);
                            v.getInventory().setHelmet(helmet);
                        }
                        BGChat.printPlayerChat(p, Translation.HEADSHOT.t());
                    }
                }
            }
        }
        if (damager instanceof Player) {
            final Player dam = (Player)damager;
            if (BGKit.hasAbility(dam, 12)) {
                if (dam.getHealth() == dam.getMaxHealth()) {
                    return;
                }
                BGCooldown.monkCooldown(dam);
                double damage = (event.getDamage() - 4.0);
                damage /= 2.0;
                dam.setHealth(dam.getHealth() + 1.0 + damage);
            }
            if (BGKit.hasAbility(dam, 38) && (dam.getItemInHand().getType() == null || dam.getItemInHand().getType() == Material.AIR) && defender instanceof Player) {
                final Player def = (Player)defender;
                final int Healt = (int)def.getHealth();
                if (Healt > 3) {
                    def.setHealth((double)(Healt - 2));
                }
            }
            if (defender.getType() == EntityType.PLAYER) {
                final Player def = (Player)defender;
                if (BGKit.hasAbility(dam, 13) && dam.getItemInHand().getType() == Material.STICK && def.getItemInHand() != null) {
                    if (!BGAbilitiesListener.cooldown.contains(dam)) {
                        final int random = (int)(Math.random() * (BGFiles.abconf.getInt("AB.13.Chance") - 1) + 1.0);
                        if (random == 1) {
                            BGAbilitiesListener.cooldown.add(dam);
                            BGCooldown.thiefCooldown(dam);
                            dam.getInventory().clear(dam.getInventory().getHeldItemSlot());
                            dam.getInventory().addItem(new ItemStack[] { def.getItemInHand() });
                            def.getInventory().clear(def.getInventory().getHeldItemSlot());
                            BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.13.Success"));
                            BGChat.printPlayerChat(def, BGFiles.abconf.getString("AB.13.Success"));
                            dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                        }
                    }
                    else {
                        BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.15.Expired"));
                    }
                }
                if (BGKit.hasAbility(dam, 15) && dam.getItemInHand().getType() == Material.STICK && def.getItemInHand() != null) {
                    if (!BGAbilitiesListener.cooldown.contains(dam)) {
                        final int random = (int)(Math.random() * (BGFiles.abconf.getInt("AB.15.Chance") - 1) + 1.0);
                        if (random == 1) {
                            BGAbilitiesListener.cooldown.add(dam);
                            BGCooldown.thiefCooldown(dam);
                            dam.getInventory().clear(dam.getInventory().getHeldItemSlot());
                            dam.getInventory().addItem(new ItemStack[] { def.getItemInHand() });
                            def.getInventory().clear(def.getInventory().getHeldItemSlot());
                            BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.15.Success"));
                            BGChat.printPlayerChat(def, BGFiles.abconf.getString("AB.15.Success"));
                            dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                        }
                    }
                    else {
                        BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.15.Expired"));
                    }
                }
                if (BGKit.hasAbility(dam, 19)) {
                    final int random = (int)(Math.random() * (BGFiles.abconf.getInt("AB.19.Chance") - 1) + 1.0);
                    if (random == 1 && !BGAbilitiesListener.cooldown.contains(def)) {
                        def.addPotionEffect(new PotionEffect(PotionEffectType.POISON, BGFiles.abconf.getInt("AB.19.Duration") * 20, 1));
                        BGAbilitiesListener.cooldown.add(def);
                        BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.19.Damager"));
                        BGChat.printPlayerChat(def, BGFiles.abconf.getString("AB.19.Defender"));
                        BGCooldown.viperCooldown(def);
                        dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
                if (BGKit.hasAbility(dam, 35)) {
                    final int random = (int)(Math.random() * (BGFiles.abconf.getInt("AB.35.Chance") - 1) + 1.0);
                    if (random == 1 && !BGAbilitiesListener.cooldown.contains(def)) {
                        def.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, BGFiles.abconf.getInt("AB.35.Duration") * 20, 0));
                        BGAbilitiesListener.cooldown.add(def);
                        BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.35.Damager"));
                        BGChat.printPlayerChat(def, BGFiles.abconf.getString("AB.35.Defender"));
                        BGCooldown.orcoCooldown(def);
                        dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
                if (BGKit.hasAbility(dam, 36)) {
                    final int random = (int)(Math.random() * (BGFiles.abconf.getInt("AB.36.Chance") - 1) + 1.0);
                    if (random == 1 && !BGAbilitiesListener.cooldown.contains(def)) {
                        def.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BGFiles.abconf.getInt("AB.36.Duration") * 20, 0));
                        def.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, BGFiles.abconf.getInt("AB.36.Duration") * 20, 0));
                        def.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, BGFiles.abconf.getInt("AB.36.Duration") * 20, 0));
                        BGAbilitiesListener.cooldown.add(def);
                        BGChat.printPlayerChat(dam, BGFiles.abconf.getString("AB.36.Damager"));
                        BGChat.printPlayerChat(def, BGFiles.abconf.getString("AB.36.Defender"));
                        BGCooldown.trollCooldown(def);
                        dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
                if (BGKit.hasAbility(dam, 18) && dam.getItemInHand().getType() == Material.AIR) {
                    event.setDamage(event.getDamage() + 5.0);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(final EntityTargetEvent event) {
        final Entity entity = event.getTarget();
        if (entity != null && entity instanceof Player) {
            final Player player = (Player)entity;
            if (BGKit.hasAbility(player, 20) && event.getReason() == EntityTargetEvent.TargetReason.CLOSEST_PLAYER) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void sopasCurandero(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final int heal = 6; // 6 half hearts = 3 hearts
            final int feed = 6; // food level increase
            if (event.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP && BGKit.hasAbility(p, 34)) {
                final ItemStack bowl = new ItemStack(Material.BOWL, 1);
                final ItemMeta meta = bowl.getItemMeta();
                if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() - 1.0) {
                    if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() - heal + 1.0) {
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                        event.getPlayer().setHealth(event.getPlayer().getHealth() + heal);
                    } else if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() && event.getPlayer().getHealth() > event.getPlayer().getMaxHealth() - heal) {
                        event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                    }
                } else if (event.getPlayer().getHealth() == event.getPlayer().getMaxHealth() && event.getPlayer().getFoodLevel() < 20) {
                    if (event.getPlayer().getFoodLevel() < 20 - feed + 1) {
                        event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + feed);
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                    } else if (event.getPlayer().getFoodLevel() < 20 && event.getPlayer().getFoodLevel() > 20 - feed) {
                        event.getPlayer().setFoodLevel(20);
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                    }
                }
            }
            if (event.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP && BGKit.hasAbility(p, 33)) {
                final ItemStack bowl = new ItemStack(Material.BOWL, 1);
                final ItemMeta meta = bowl.getItemMeta();
                if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() - 4.0) {
                    event.getPlayer().getItemInHand().setType(Material.BOWL);
                    event.getPlayer().getItemInHand().setItemMeta(meta);
                    event.getPlayer().setItemInHand(bowl);
                    event.getPlayer().setHealth(event.getPlayer().getHealth() + 4.0);
                } else {
                    event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                    event.getPlayer().getItemInHand().setType(Material.BOWL);
                    event.getPlayer().getItemInHand().setItemMeta(meta);
                    event.getPlayer().setItemInHand(bowl);
                }
            }
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        if (BGMain.frezee.contains(e.getPlayer())) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 1, false));
            final Location from = e.getFrom();
            final Location to = e.getTo();
            double x = Math.floor(from.getX());
            double z = Math.floor(from.getZ());
            if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
                x += 0.5;
                z += 0.5;
                e.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));
            }
        }
    }
}
