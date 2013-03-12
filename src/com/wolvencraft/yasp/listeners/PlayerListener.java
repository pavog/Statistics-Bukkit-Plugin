package com.wolvencraft.yasp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class PlayerListener implements Listener {
	
	public PlayerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		DataCollector.global().playerLogin(Bukkit.getOnlinePlayers().length);
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).player().login(player.getLocation());
		Message.send(player, Settings.getWelcomeMessage(player));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).player().logout(player.getLocation());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "move")) return;
		Location playerLocation = player.getLocation();
		double distance = playerLocation.distance(event.getTo());
		if(player.isInsideVehicle()) {
			Vehicle vehicle = (Vehicle) player.getVehicle();
			if(vehicle.getType().equals(EntityType.MINECART)) {
				DataCollector.get(player).player().distance().addDistanceMinecart(distance);
			} else if(vehicle.getType().equals(EntityType.BOAT)) {
				DataCollector.get(player).player().distance().addDistanceBoat(distance);
			} else if(vehicle.getType().equals(EntityType.PIG)) {
				DataCollector.get(player).player().distance().addDistancePig(distance);
			}
		} else if (playerLocation.getBlock().getType().equals(Material.WATER) || playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER)) {
			DataCollector.get(player).player().distance().addDistanceSwimmed(distance);
		} else {
			DataCollector.get(player).player().distance().addDistanceFoot(distance);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.fish")) return;
		DataCollector.get(player).player().misc().fishCaught();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.kick")) return;
		DataCollector.get(player).player().misc().kicked();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEggThrow(PlayerEggThrowEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.eggThrow")) return;
		DataCollector.get(player).player().misc().eggThrown();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onArrowShoot(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Util.isExempt(player, "misc.arrowShoot")) return;
		DataCollector.get(player).player().misc().arrowShot();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Util.isExempt(player, "misc.takeDamage")) return;
		DataCollector.get(player).player().misc().damageTaken(event.getDamage());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBedEnter(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.bedEnter")) return;
		DataCollector.get(player).player().misc().bedEntered();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPortalEnter(PlayerPortalEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.portalEnter")) return;
		DataCollector.get(player).player().misc().portalEntered();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChatMessage(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.chat")) return;
		DataCollector.get(player).player().misc().chatMessageSent(event.getMessage().split(" ").length);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChatCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player, "misc.command")) return;
		DataCollector.get(player).player().misc().commandSent();
	}
}
