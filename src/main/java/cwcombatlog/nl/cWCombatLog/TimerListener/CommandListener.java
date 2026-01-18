package cwcombatlog.nl.cWCombatLog.TimerListener;

import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private final CWCombatLog plugin;

    public CommandListener(CWCombatLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {


        if (event.getPlayer().hasPermission("cwcombatlog.bypass")){
            return;
        }


        if (!plugin.hasActiveTimer(event.getPlayer())){
            return;
        }


        String full = event.getMessage();
        String base = full.split(" ")[0].toLowerCase();


        List<String> allowed = plugin.getConfig().getStringList("command-block.allow");
        for (String a : allowed) {
            if (base.equalsIgnoreCase(a)) {
                return;
            }
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text("You may not use commands while in combat!").color(NamedTextColor.RED));
    }
}
