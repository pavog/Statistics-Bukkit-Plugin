package com.wolvencraft.yasp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Util;

public class ItemListener implements Listener {
	
	public ItemListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.pickup")) return;
		DataCollector.get(player).itemPickUp(event.getItem().getItemStack());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.drop")) return;
		DataCollector.get(player).itemDrop(event.getItemDrop().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onFoodConsume(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Util.isExempt(player, "item.use")) return;
		DataCollector.get(player).itemUse(player.getItemInHand());
		DataCollector.get(player).misc().foodEaten();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemCraft(CraftItemEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(Util.isExempt(player, "item.craft")) return;
		DataCollector.get(player).itemCraft(event.getCurrentItem());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemSmelt(FurnaceExtractEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.smelt")) return;
		DataCollector.get(player).itemSmelt(new ItemStack(event.getItemType()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onToolBreak(PlayerItemBreakEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.break")) return;
		DataCollector.get(player).toolBreak(event.getBrokenItem());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemEnchant(EnchantItemEvent event) {
		Player player = event.getEnchanter();
		if(Util.isExempt(player, "item.enchant")) return;
		DataCollector.get(player).itemEnchant(new ItemStack(event.getItem().getType()));
	}
}