package cwcombatlog.nl.cWCombatLog.CommandsListener;


import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor {

    private final CWCombatLog plugin;

    public CommandListener(CWCombatLog plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0){
            sender.sendMessage("Use: /cwcombatlog reload or /cwcombatlog help");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("cwcombatlog.admin")){
                sender.sendMessage("You do not have permission to do this");
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage("cwcombatlog config reload! (timer.seconds = " + plugin.getTimerSeconds() + ")");
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("cwcombatlog.admin")){
                sender.sendMessage("You do not have permission to do this");
                return true;
            }
            sender.sendMessage("Use: /cwcombatlog reload to reload the config");
            return true;
        }

        sender.sendMessage("Unknown command. Use: /cwcombatlog reload or /cwcombatlog help");
        return true;
    }
}
