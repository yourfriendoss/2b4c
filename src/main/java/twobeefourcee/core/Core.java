package twobeefourcee.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Wither;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import twobeefourcee.core.events.JoinMessages;
import twobeefourcee.core.events.PlaytimeBenefits;
import twobeefourcee.core.events.Scoreboard;

public class Core extends JavaPlugin {
	public static String success = ChatColor.translateAlternateColorCodes('&', "&8[&a&l2b4c&r&8]&7 ");
	public static String error = ChatColor.translateAlternateColorCodes('&', "&8[&c&l2b4c&r&8]&7 ");
	public static String info = ChatColor.translateAlternateColorCodes('&', "&8[&7&l2b4c&r&8]&7 ");

    private Server mc = Bukkit.getServer();
    private Logger log = this.getLogger();
    private PluginManager pm = mc.getPluginManager();

    public static File databaseFile = new File("plugins/2b4cCore/db.yml");
    public static YamlConfiguration database = YamlConfiguration.loadConfiguration(databaseFile);
    
    private static String[] annoucments = new String[] {
    		"Join the official 2b4c discord at: discord.gg/zXyQ7WsyJp",
    		"Crashing the server is &lstrictly forbidden.",
    		"Stuck? Type /kill to meet your tragic fate.",
    		"Track your total playtime with /pt",
    		"Reminder: Keep inventory is &lnot enabled.",
    		"Tip: Try not to die."
    };
    
    private static int annoucmentLocation = 0;
    
	@Override
	public void onEnable() {
		Reflections commandReflections = new Reflections("twobeefourcee.core.commands");
		Reflections listenerReflections = new Reflections("twobeefourcee.core.events");
		
	    for (Class <? extends CommandExecutor> command : commandReflections.getSubTypesOf(CommandExecutor.class)) {
		    try {
			    this.getCommand(command.getSimpleName().toLowerCase()).setExecutor(command.getDeclaredConstructor().newInstance());
			    log.info("Registered command " + command.getSimpleName());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			    	   | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			};
		}
		 
		for (Class <? extends Listener> listener : listenerReflections.getSubTypesOf(Listener.class)) {
		    try {
			    pm.registerEvents(listener.getDeclaredConstructor().newInstance(), this);
			    log.info("Registered event " + listener.getSimpleName());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
		    }
	    }
		
	    Bukkit.getOnlinePlayers().forEach(playerr -> {
	    	Scoreboard.joinEvent(new PlayerJoinEvent(playerr, null));
	    	JoinMessages.joinEvent(new PlayerJoinEvent(playerr, null));
	    	PlaytimeBenefits.joinEvent(new PlayerJoinEvent(playerr, null));
	    });
	    
	    
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	        public void run() { 
	        	annoucmentLocation++;
	        	
	        	if(annoucmentLocation == Core.annoucments.length)
	        		annoucmentLocation = 0;
	        	
	        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Core.success + Core.annoucments[annoucmentLocation]));
	        }
	    }, (20 * 60) * 2, (20 * 60) * 2);
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	        public void run() { 
	        	World w = mc.getWorlds().get(0);
	        	w.getEntitiesByClass(Wither.class).forEach(m -> {
	        		m.setHealth(0);
	        	});
	        }
	    }, 20 *300,20 *300);
		System.out.println("[2b4cCore] Enabled sucessfully!");
	}
	
	@Override
	public void onDisable() {
		System.out.println("[2b4cCore] Disabled.");
	}
}
