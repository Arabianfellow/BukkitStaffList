package me.Arabianfellow.StaffOnline;



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.event.VanishStatusChangeEvent;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;
import org.omg.CosNaming._BindingIteratorImplBase;

public class Main extends JavaPlugin implements Listener {
	
	List<String> staff = getConfig().getStringList("staff");
	public void onEnable() {
		try {
			Metrics metrics = new Metrics(this); metrics.start();
			} catch (IOException e) { // Failed to submit the stats :-(
			System.out.println("Error Submitting stats!");
			}
		getServer().getPluginManager().registerEvents(this, this);
		if(!new File(getDataFolder(), "config.yml").exists()){
			saveConfig();
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
	Player player = event.getPlayer();
	if(player.hasPermission("staff.toggle") || player.isOp()){
	staff.add(player.getName());
	getConfig().set("staff", staff);
    saveConfig();
	player.sendMessage(ChatColor.GREEN + "You are now added to the staff online list");
	Bukkit.broadcastMessage(ChatColor.RED + player.getName().toString()  + ChatColor.GOLD + " is now online");
	}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("staff.toggle") || player.isOp()){
		Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GOLD + " has left");
		staff.remove(player.getName());
		getConfig().set("staff", staff);
        saveConfig();
		} 
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onVanishUpdate(VanishStatusChangeEvent event) {
		   Player player = event.getPlayer();
	   if(event.isVanishing()) {
		   Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GOLD + " has left");
			staff.remove(player.getName());
			getConfig().set("staff", staff);
	        saveConfig();
	   } else if(!event.isVanishing()) {
		   staff.add(player.getName());
		   Bukkit.broadcastMessage(ChatColor.RED + player.getName().toString()  + ChatColor.GOLD + " is now online");
		   getConfig().set("staff", staff);
	        saveConfig();
	   }
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = (Player) sender;
		if(args.length == 0){
		if(cmd.getName().equalsIgnoreCase("staff") || cmd.getName().equalsIgnoreCase("s"));
		if(player.hasPermission("staff.list")){
		player.sendMessage(ChatColor.GOLD + "Staff Online:");
		player.sendMessage(ChatColor.RED + getConfig().getStringList("staff").toString());
		}else {
			player.sendMessage(ChatColor.RED + "No Permission!");
		}
    	} else if(args[0].equalsIgnoreCase("leave")){
		if(player.hasPermission("staff.toggle.leave")) {
		player.sendMessage(ChatColor.BLUE + "You are now appearing as offline!");
		Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GOLD + " has left");
		staff.remove(player.getName());
		getConfig().set("staff", staff);
        saveConfig();
		} else {
			player.sendMessage(ChatColor.RED + "No Permission!");
		}
	}else if(args[0].equalsIgnoreCase("join")) {
	  if (!getConfig().getStringList("staff").contains(player.getName())) {
		if(player.hasPermission("staff.toggle.join")){
		staff.add(player.getName());
        getConfig().set("staff", staff);
        saveConfig();
		player.sendMessage(ChatColor.BLUE + "You are now appearing as online!");
		Bukkit.broadcastMessage(ChatColor.RED + player.getName().toString()  + ChatColor.GOLD + " is now online");
		}else {
			player.sendMessage(ChatColor.RED + "No Permission!");
    	}
		} else {
			player.sendMessage(ChatColor.RED + "You are already online! Use /staff leave to appear offline");
		}
	}else if(args[0].equalsIgnoreCase("help")){
		if(player.hasPermission("staff.help")){
		player.sendMessage(ChatColor.GOLD + "Displaying Commands for: " + ChatColor.RED + "Staff List");
		player.sendMessage(ChatColor.GOLD + "/staff: " + ChatColor.WHITE + "Display online staff.");
		if(player.hasPermission("staff.help.staff"));
		player.sendMessage(ChatColor.GOLD + "/staff join: " + ChatColor.WHITE + "allows you to join the staff list");
		player.sendMessage(ChatColor.GOLD + "/staff leave: " +  ChatColor.WHITE +"allows you to leave the staff list");
		}else {
			player.sendMessage(ChatColor.RED + "No Permission!");
    	}
	}
		return false;
		
			
	}
}

