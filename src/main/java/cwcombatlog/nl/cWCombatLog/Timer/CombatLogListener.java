package cwcombatlog.nl.cWCombatLog.Timer;

import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogListener implements Listener {

    private final CWCombatLog plugin;

    public CombatLogListener(CWCombatLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void  onQuit(PlayerQuitEvent event){
        handleLeve(event.getPlayer());
    }

    private void handleLeve(Player player){
        if(player.hasPermission("cwcombatlog.combatlog.bypass"))return;

        if(!plugin.hasActiveTimer(player)) return;

        plugin.stopTimer(player);

        plugin.markCombatLogKill(player.getUniqueId());

        try{
            player.setHealth(0.0);
        }catch (Exception ignored){}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void  onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(plugin.consumeCombatLogKill(player.getUniqueId())){
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.setHealth(0.0);
                player.sendMessage(ChatColor.RED +"You are dead by CombatLog");
            });
        }
    }

//    @EventHandler
//    public void deadStopTimer(PlayerDeathEvent event){
//        if(!plugin.hasActiveTimer(event.getPlayer())) return;
//        plugin.stopTimer(event.getPlayer());
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDead(PlayerDeathEvent event){
        Player p = event.getEntity();

        if(plugin.hasActiveTimer(p)){
            plugin.stopTimer(p);
        }
        plugin.clearCombatLogKill(p.getUniqueId());
    }
}
