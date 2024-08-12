package lc.mine.hg.main;

import org.bukkit.event.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import java.util.*;

public class Enchants implements Listener
{
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onclick1(final InventoryClickEvent e) {
        if (e.getClickedInventory() != e.getWhoClicked().getInventory() && e.getCurrentItem() != null) {
            e.setCurrentItem(this.detectiontest(e.getCurrentItem()));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClick2(final PlayerPickupItemEvent e) {
        e.getItem().setItemStack(this.detectiontest(e.getItem().getItemStack()));
    }

    public ItemStack detectiontest(final ItemStack item) {
        if (item == null || item.getType() == Material.AIR || item.getEnchantments().isEmpty()) {
            return item;
        }
        final Map<Enchantment, Integer> Enchants = item.getEnchantments();

        if (Enchants.containsKey(Enchantment.DAMAGE_ALL)) {
            final Integer level = Enchants.get(Enchantment.DAMAGE_ALL);
            if (level >= 4) {
                item.removeEnchantment(Enchantment.DAMAGE_ALL);
                if (Enchantment.DAMAGE_ALL.canEnchantItem(item)) {
                    item.addEnchantment(Enchantment.DAMAGE_ALL, 3);
                }
            }
        }

        if (Enchants.containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            final Integer level = Enchants.get(Enchantment.PROTECTION_ENVIRONMENTAL);
            if (level >= 3) {
                item.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
                if (Enchantment.PROTECTION_ENVIRONMENTAL.canEnchantItem(item)) {
                    item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                }
            }
        }

        if (Enchants.containsKey(Enchantment.KNOCKBACK)) {
            final Integer level = Enchants.get(Enchantment.KNOCKBACK);
            if (level >= 3) {
                item.removeEnchantment(Enchantment.KNOCKBACK);
                if (Enchantment.KNOCKBACK.canEnchantItem(item)) {
                    item.addEnchantment(Enchantment.KNOCKBACK, 2);
                }
            }
        }

        if (Enchants.containsKey(Enchantment.ARROW_DAMAGE)) {
            final Integer level = Enchants.get(Enchantment.ARROW_DAMAGE);
            if (level >= 5) {
                item.removeEnchantment(Enchantment.ARROW_DAMAGE);
                if (Enchantment.ARROW_DAMAGE.canEnchantItem(item)) {
                    item.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
                }
            }
        }

        if (Enchants.containsKey(Enchantment.ARROW_KNOCKBACK)) {
            final Integer level = Enchants.get(Enchantment.ARROW_KNOCKBACK);
            if (level >= 3) {
                item.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                if (Enchantment.ARROW_KNOCKBACK.canEnchantItem(item)) {
                    item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                }
            }
        }

        return item;
    }
    
    public void detectPotion(final Player p) {
        final Collection<PotionEffect> pot = (Collection<PotionEffect>)p.getActivePotionEffects();
        if (pot.isEmpty()) {
            return;
        }
        for (final PotionEffect pe : pot) {
            if (pe.getAmplifier() > 3) {
                p.removePotionEffect(pe.getType());
            }
            else {
                if (pe.getType() != PotionEffectType.INCREASE_DAMAGE || pe.getAmplifier() <= 1) {
                    continue;
                }
                p.removePotionEffect(pe.getType());
            }
        }
    }
}
