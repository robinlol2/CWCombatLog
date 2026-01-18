package cwcombatlog.nl.cWCombatLog.PapiListener;

import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperAPIListener extends PlaceholderExpansion {

    private final CWCombatLog plugin;

    public PaperAPIListener(CWCombatLog plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier(){
        return "cwcombatlog";
    }

    @Override
    public @NotNull String getAuthor(){
        return "ColorWave Studio's";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params){
        if(player == null){
            return "";
        }

        if(params.equalsIgnoreCase("in_combat")){
            return plugin.hasActiveTimer(player) ? "true" : "false";
        }

        return null;
    }
}
