package cwcombatlog.nl.cWCombatLog;

import cwcombatlog.nl.cWCombatLog.TimerListener.CombatLogListener;
import cwcombatlog.nl.cWCombatLog.TimerListener.TimerListener;
import cwcombatlog.nl.cWCombatLog.CommandsListener.CommandListener;
import cwcombatlog.nl.cWCombatLog.PapiListener.PaperAPIListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

public final class CWCombatLog extends JavaPlugin{

    private Map<UUID, BukkitTask> activeTimers = new HashMap<>();
    private final Set<UUID> pendingCombatLogKills = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TimerListener(this), this);
        if (getCommand("cwcombatlog") != null){
            getCommand("cwcombatlog").setExecutor(new CommandListener(this));
        }
        getServer().getPluginManager().registerEvents(new cwcombatlog.nl.cWCombatLog.TimerListener.CommandListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatLogListener(this), this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PaperAPIListener(this).register();
            getLogger().info("PlaceholderAPI is connected(%cwcombatlog_in_combat%)");
        }else{
            getLogger().info("PlaceholderAPI is not connected");
        }
    }

    @Override
    public void onDisable() {
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
        if (t != null) {
            t.cancel();
        }
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

        BukkitTask oldTimer = activeTimers.remove(uuid);
        if (oldTimer != null){
            oldTimer.cancel();
        }

        if(!wasAlreadyInCombat){
            player.sendMessage(Component.text("You are in combat").color(NamedTextColor.RED));
        }

        final int[] timeLeft = { seconds };

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(this, () -> {

            if (timeLeft[0] <= 0){
                player.sendMessage(Component.text("The combat is over!").color(NamedTextColor.GREEN));

                BukkitTask t = activeTimers.remove(uuid);
                if (t != null){
                    t.cancel();
                }

                return;
            }
            timeLeft[0]--;

        }, 0L, 20L);

        activeTimers.put(uuid, task);
    }
}