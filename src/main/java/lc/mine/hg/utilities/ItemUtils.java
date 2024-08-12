package lc.mine.hg.utilities;

import java.util.*;
import com.google.common.collect.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;

public class ItemUtils extends ItemStack
{
    public ItemUtils(final ItemStack itemStack) {
        this.setType(itemStack.getType());
        this.setAmount(itemStack.getAmount());
        this.setDurability(itemStack.getDurability());
        this.setItemMeta(itemStack.getItemMeta());
    }
    
    public ItemUtils(final Material material, final Short damageid, final Integer amount, final String displayName, final List<String> lores) {
        this.setType(material);
        this.setAmount((int)amount);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Short damageid, final Integer amount, final String displayName, final String loresSplit) {
        this.setType(material);
        this.setAmount((int)amount);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        final List<String> lr = Lists.newArrayList();
        String[] split;
        for (int length = (split = loresSplit.split("==")).length, i = 0; i < length; ++i) {
            final String lrs = split[i];
            lr.add(lrs);
        }
        im.setLore((List)lr);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final String skull_item_Owner, final Integer amount, final String displayName, final String loresSplit) {
        this.setType(Material.SKULL_ITEM);
        this.setAmount((int)amount);
        this.setDurability((short)3);
        final SkullMeta im = (SkullMeta)this.getItemMeta();
        im.setDisplayName(displayName);
        final List<String> lr = Lists.newArrayList();
        String[] split;
        for (int length = (split = loresSplit.split("==")).length, i = 0; i < length; ++i) {
            final String lrs = split[i];
            lr.add(lrs);
        }
        im.setLore((List)lr);
        im.setOwner(skull_item_Owner);
        this.setItemMeta((ItemMeta)im);
    }
    
    public ItemUtils(final String skull_item_Owner, final Integer amount) {
        this.setType(Material.SKULL_ITEM);
        this.setAmount((int)amount);
        this.setDurability((short)3);
        final SkullMeta im = (SkullMeta)this.getItemMeta();
        im.setOwner(skull_item_Owner);
        this.setItemMeta((ItemMeta)im);
    }
    
    public ItemUtils(final Material material, final Short damageid) {
        this.setType(material);
        this.setDurability((short)damageid);
        this.setAmount(1);
    }
    
    public ItemUtils(final Material material, final Integer amount) {
        this.setType(material);
        this.setAmount((int)amount);
    }
    
    public ItemUtils(final Material material, final String displayName) {
        this.setType(material);
        this.setAmount(1);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final List<String> lores) {
        this.setType(material);
        this.setAmount(1);
        final ItemMeta im = this.getItemMeta();
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Short damageid, final Integer amount) {
        this.setType(material);
        this.setAmount((int)amount);
        this.setDurability((short)damageid);
    }
    
    public ItemUtils(final Material material, final Short damageid, final Integer amount, final String displayName) {
        this.setType(material);
        this.setAmount((int)amount);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Short damageid, final Integer amount, final List<String> lores) {
        this.setType(material);
        this.setAmount((int)amount);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Short damageid, final String displayName) {
        this.setType(material);
        this.setAmount(1);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Short damageid, final String displayName, final List<String> lores) {
        this.setType(material);
        this.setAmount(1);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Short damageid, final List<String> lores) {
        this.setType(material);
        this.setAmount(1);
        this.setDurability((short)damageid);
        final ItemMeta im = this.getItemMeta();
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Integer amount, final String displayName) {
        this.setType(material);
        this.setAmount((int)amount);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Integer amount, final String displayName, final List<String> lores) {
        this.setType(material);
        this.setAmount((int)amount);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final Integer amount, final List<String> lores) {
        this.setType(material);
        this.setAmount((int)amount);
        final ItemMeta im = this.getItemMeta();
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils(final Material material, final String displayName, final List<String> lores) {
        this.setType(material);
        this.setAmount(1);
        final ItemMeta im = this.getItemMeta();
        im.setDisplayName(displayName);
        im.setLore((List)lores);
        this.setItemMeta(im);
    }
    
    public ItemUtils getCustomItem(final ItemStack itemStack) {
        this.setType(itemStack.getType());
        this.setAmount(itemStack.getAmount());
        this.setDurability(itemStack.getDurability());
        this.setItemMeta(itemStack.getItemMeta());
        return this;
    }
    
    public boolean isSameLike(final ItemUtils customItem) {
        if (!customItem.getType().equals((Object)this.getType())) {
            return false;
        }
        if (this.getItemMeta().hasDisplayName()) {
            if (!customItem.getItemMeta().hasDisplayName()) {
                return false;
            }
            if (!this.getItemMeta().getDisplayName().equals(customItem.getItemMeta().getDisplayName())) {
                return false;
            }
        }
        return (!this.getItemMeta().hasLore() || customItem.getItemMeta().hasLore()) && this.getItemMeta().getEnchants().equals(customItem.getItemMeta().getEnchants()) && this.getDurability() == customItem.getDurability();
    }
    
    public boolean isSameItemStackLike(final ItemStack itemStack) {
        if (!itemStack.getType().equals((Object)this.getType())) {
            return false;
        }
        if (this.getItemMeta().hasDisplayName()) {
            if (!itemStack.getItemMeta().hasDisplayName()) {
                return false;
            }
            if (!this.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                return false;
            }
        }
        return (!this.getItemMeta().hasLore() || itemStack.getItemMeta().hasLore()) && this.getItemMeta().getEnchants().equals(itemStack.getItemMeta().getEnchants()) && this.getDurability() == itemStack.getDurability();
    }
    
    public void add(final Player player) {
        player.getInventory().addItem(new ItemStack[] { this });
    }
    
    public void add(final Inventory inventory) {
        inventory.addItem(new ItemStack[] { this });
    }
    
    public void set(final Player player, final Integer slot) {
        player.getInventory().setItem((int)slot, (ItemStack)this);
    }
    
    public void set(final Inventory inventory, final Integer slot) {
        inventory.setItem((int)slot, (ItemStack)this);
    }
    
    public void setHelmet(final Player p) {
        p.getInventory().setHelmet((ItemStack)this);
    }
    
    public void setChestplate(final Player p) {
        p.getInventory().setChestplate((ItemStack)this);
    }
    
    public void setLeggings(final Player p) {
        p.getInventory().setLeggings((ItemStack)this);
    }
    
    public void setBoots(final Player p) {
        p.getInventory().setBoots((ItemStack)this);
    }
    
    public void setInHand(final Player p) {
        p.getInventory().setItemInHand((ItemStack)this);
    }
    
    public void dropItem(final Location loc) {
        loc.getWorld().dropItem(loc, (ItemStack)this);
    }
    
    public void dropItemNaturally(final Location loc) {
        loc.getWorld().dropItemNaturally(loc, (ItemStack)this);
    }
}
