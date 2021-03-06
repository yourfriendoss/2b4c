package twobeefourcee.core.events;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.md_5.bungee.api.ChatColor;
import twobeefourcee.core.Core;

public class Scoreboard implements Listener {
	
    @EventHandler
    static public void joinEvent(PlayerJoinEvent event) {
    	Player player = event.getPlayer();

    	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getPlugin(Core.class), new Runnable() {
    		public void run() {
    	    	BPlayerBoard board = Netherboard.instance().createBoard(player, Core.error.substring(0, Core.error.length() - 1));
    
    	    	double days = ((double)player.getStatistic(Statistic.PLAY_ONE_MINUTE)) / 20 / 86400;
    	    	    	    	
    	    	board.set(ChatColor.translateAlternateColorCodes('&', "&cPlaytime: &l" + String.format("%.2f", days) + "d"), 2);
    	    	board.set(ChatColor.translateAlternateColorCodes('&', "&cTotal Players: &l" + Bukkit.getOnlinePlayers().size()), 3);
    	    	
    	    	board.set("", 1);
    	    	
    	    	board.set(ChatColor.translateAlternateColorCodes('&', "&cdiscord.gg/&lzXyQ7WsyJp"), 0);
    	    	
    	    	Bukkit.getOnlinePlayers().forEach(playerr -> {
    	    		if(player != playerr) {
    	    			BPlayerBoard bboard = Netherboard.instance().getBoard(playerr);
    	    	    	
    	    			bboard.set(ChatColor.translateAlternateColorCodes('&', "&cTotal Players: &l" + Bukkit.getOnlinePlayers().size()), 3);

    	    		};
    	    	});
    		}
    	}, 20 * 2);

    }
    
    @EventHandler
    public void leaveEvent(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	
    	Bukkit.getOnlinePlayers().forEach(playerr -> {
    		if(player != playerr) {
    			BPlayerBoard board = Netherboard.instance().getBoard(playerr);
    	    	
    	    	board.set(ChatColor.translateAlternateColorCodes('&', "&cTotal Players: &l" + (Bukkit.getOnlinePlayers().size()-1)), 3);
    		};
    	});
    }
}