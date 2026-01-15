package cwcombatlog.nl.cWCombatLog;

import cwcombatlog.nl.cWCombatLog.Timer.CombatLogListener;
import cwcombatlog.nl.cWCombatLog.Timer.TimerTrigger;
import cwcombatlog.nl.cWCombatLog.commands.CWTimerCommand;
import cwcombatlog.nl.cWCombatLog.papi.CWTimerExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class CWCombatLog extends JavaPlugin {

    private final Map<UUID, BukkitTask> activeTimers = new HashMap<>();
    private final Set<UUID> pendingCombatLogKills = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TimerTrigger(this), this);
        if (getCommand("cwcombatlog") != null) {getCommand("cwcombatlog").setExecutor(new CWTimerCommand(this));}
        getServer().getPluginManager().registerEvents(new cwcombatlog.nl.cWCombatLog.Timer.CommandBlocker(this), this);
        getServer().getPluginManager().registerEvents(new CombatLogListener(this), this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new CWTimerExpansion(this).register();
            getLogger().info("PlaceholderAPI is verbonden(%cwcombatlog_in_combat%)");
        }else{
            getLogger().info("PlaceholderAPI is niet verbonden)");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        activeTimers.values().forEach(BukkitTask::cancel);
        activeTimers.clear();
        pendingCombatLogKills.clear();
    }

    public int getTimerSeconds() {
        return getConfig().getInt("timer.seconds", 10);
    }

    public boolean isIncludeVictim() {
        return getConfig().getBoolean("timer.include-victim", false);
    }

    public boolean hasActiveTimer(Player player) {
        return activeTimers.containsKey(player.getUniqueId());
    }

    public void stopTimer(Player player) {
        BukkitTask t = activeTimers.remove(player.getUniqueId());
        if (t != null) t.cancel();
    }


    public void markCombatLogKill(UUID uuid) {
        pendingCombatLogKills.add(uuid);
    }

    public void clearCombatLogKill(UUID uuid){
        pendingCombatLogKills.remove(uuid);
    }

    public boolean consumeCombatLogKill(UUID uuid) {
        return pendingCombatLogKills.remove(uuid);
    }

    public void startTimer(Player player, int seconds){
        UUID uuid = player.getUniqueId();

        boolean wasAlreadyInCombat = activeTimers.containsKey(uuid);

        BukkitTask old = activeTimers.remove(uuid);
        if (old != null) old.cancel();

        if(!wasAlreadyInCombat){
            player.sendMessage(ChatColor.RED +"You are in combat");
        }

        final int[] timeLeft = { seconds };

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(this, () -> {

            if (timeLeft[0] <= 0) {
                player.sendMessage(ChatColor.GREEN +"The combat is over!");

                BukkitTask t = activeTimers.remove(uuid);
                if (t != null) t.cancel();

                return;
            }

            //player.sendMessage("Timer: " + timeLeft[0] + " seconde over...");
            timeLeft[0]--;

        }, 0L, 20L);

        activeTimers.put(uuid, task);
    }
}