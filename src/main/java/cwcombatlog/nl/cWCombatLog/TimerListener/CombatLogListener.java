package cwcombatlog.nl.cWCombatLog.TimerListener;

import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public void onQuit(PlayerQuitEvent event){
        handleLeve(event.getPlayer());
    }

    private void handleLeve(Player player){
        if(player.hasPermission("cwcombatlog.combatlog.bypass")){
            return;
        }

        if(!plugin.hasActiveTimer(player)){
            return;
        }

        plugin.stopTimer(player);

        plugin.markCombatLogKill(player.getUniqueId());

        try{
            player.setHealth(0.0);
        }catch (Exception ex){
            plugin.getLogger().warning("Failed to kill player on combat-log: " + ex.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(plugin.consumeCombatLogKill(player.getUniqueId())){
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.setHealth(0.0);
                player.sendMessage(Component.text("You are dead by CombatLog").color(NamedTextColor.RED));
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDead(PlayerDeathEvent event){
        Player p = event.getEntity();

        if(plugin.hasActiveTimer(p)){
            plugin.stopTimer(p);
        }
        plugin.clearCombatLogKill(p.getUniqueId());
    }
}
