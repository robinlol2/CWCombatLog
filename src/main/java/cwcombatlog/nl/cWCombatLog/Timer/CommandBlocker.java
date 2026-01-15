package cwcombatlog.nl.cWCombatLog.Timer;

import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandBlocker implements Listener {

    private final CWCombatLog plugin;

    public CommandBlocker(CWCombatLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {


        if (event.getPlayer().hasPermission("cwcombatlog.bypass")) return;


        if (!plugin.hasActiveTimer(event.getPlayer())) return;


        String full = event.getMessage();
        String base = full.split(" ")[0].toLowerCase();


        List<String> allowed = plugin.getConfig().getStringList("command-block.allow");
        for (String a : allowed) {
            if (base.equalsIgnoreCase(a)) {
                return;
            }
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED +"You may not use commands while in combat!");
    }
}
