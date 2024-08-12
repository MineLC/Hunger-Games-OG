package lc.mine.hg.utilities;

import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

public class IconMenu implements Listener
{
    private String name;
    private int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private Inventory inv;
    private String[] optionNames;
    private ItemStack[] optionIcons;
    private boolean destroy;
    
    public IconMenu(final String name, final int size, final OptionClickEventHandler handler, final Plugin plugin) {
        this.inv = null;
        this.destroy = false;
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
        this.inv = Bukkit.createInventory((InventoryHolder)null, size, name);
    }
    
    public IconMenu(final String name, final int size, final OptionClickEventHandler handler, final Plugin plugin, final boolean destroy) {
        this.inv = null;
        this.destroy = false;
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        this.destroy = destroy;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
        this.inv = Bukkit.createInventory((InventoryHolder)null, size, name);
    }
    
    public IconMenu setOption(final int position, final ItemStack icon, final String name, final String... info) {
        this.optionNames[position] = name;
        this.optionIcons[position] = this.setItemNameAndLore(icon, name, info);
        this.inv.setItem(position, icon);
        return this;
    }
    
    public IconMenu setOption(final int position, final ItemStack icon) {
        this.optionNames[position] = icon.getItemMeta().getDisplayName();
        this.optionIcons[position] = icon;
        this.inv.setItem(position, icon);
        return this;
    }
    
    public void open(final Player player) {
        player.openInventory(this.inv);
    }
    
    public void destroy() {
        HandlerList.unregisterAll((Listener)this);
        this.handler = null;
        this.plugin = null;
        this.optionNames = null;
        this.optionIcons = null;
        this.inv = null;
    }
    
    public void clear() {
        this.optionNames = new String[this.size];
        this.optionIcons = new ItemStack[this.size];
        this.inv.clear();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().equals(this.inv)) {
            event.setCancelled(true);
            final int slot = event.getRawSlot();
            if (slot >= 0 && slot < this.size && this.optionNames[slot] != null) {
                final OptionClickEvent e = new OptionClickEvent((Player)event.getWhoClicked(), slot, this.optionNames[slot], this);
                this.handler.onOptionClick(e);
                if (e.willClose()) {
                    final Player p = (Player)event.getWhoClicked();
                    p.closeInventory();
                }
                if (e.willDestroy()) {
                    this.destroy();
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    void onInventoryClick(final InventoryCloseEvent event) {
        if (event.getInventory().equals(this.inv) && this.destroy) {
            this.destroy();
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    void onInventoryClick(final PlayerQuitEvent event) {
        event.getPlayer().closeInventory();
    }
    
    public boolean isDestroy() {
        return this.destroy;
    }
    
    public void setDestroy(final boolean destroy) {
        this.destroy = destroy;
    }
    
    private ItemStack setItemNameAndLore(final ItemStack item, final String name, final String[] lore) {
        final ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore((List)Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
    
    public class OptionClickEvent
    {
        private Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean destroy;
        private IconMenu iconmenu;
        
        public OptionClickEvent(final Player player, final int position, final String name, final IconMenu iconmenu) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
            this.iconmenu = iconmenu;
        }
        
        public Player getPlayer() {
            return this.player;
        }
        
        public int getPosition() {
            return this.position;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean willClose() {
            return this.close;
        }
        
        public boolean willDestroy() {
            return this.destroy;
        }
        
        public void setWillClose(final boolean close) {
            this.close = close;
        }
        
        public void setWillDestroy(final boolean destroy) {
            this.destroy = destroy;
        }
        
        public IconMenu getIconmenu() {
            return this.iconmenu;
        }
        
        public void setIconmenu(final IconMenu iconmenu) {
            this.iconmenu = iconmenu;
        }
    }
    
    public interface OptionClickEventHandler
    {
        void onOptionClick(final OptionClickEvent p0);
    }
}
