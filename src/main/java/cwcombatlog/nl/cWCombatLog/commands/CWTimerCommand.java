package cwcombatlog.nl.cWCombatLog.commands;


import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CWTimerCommand implements CommandExecutor {

    private final CWCombatLog plugin;

    public CWTimerCommand(CWCombatLog plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /cwtimer
        if (args.length == 0) {
            sender.sendMessage("Use: /cwcombatlog reload");
            return true;
        }

        // /cwtimer reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("cwcombatlog.admin")) {
                sender.sendMessage("You do not have permission to do this");
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage("cwcombatlog config reload! (timer.seconds = " + plugin.getTimerSeconds() + ")");
            return true;
        }

        sender.sendMessage("Unknown command. Use: /cwcombatlog reload");
        return true;
    }
}
