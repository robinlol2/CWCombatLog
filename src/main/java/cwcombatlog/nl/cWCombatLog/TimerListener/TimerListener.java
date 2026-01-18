package cwcombatlog.nl.cWCombatLog.TimerListener;


import cwcombatlog.nl.cWCombatLog.CWCombatLog;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class TimerListener implements Listener{

    private final CWCombatLog plugin;

    public TimerListener(CWCombatLog plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void HitTimerStartEvent(EntityDamageByEntityEvent event){
        Player attacker = null;
        Player victim = null;

        if(event.getDamager() instanceof Player p){
            attacker = p;
        }
        else if(event.getDamager() instanceof Projectile proj){
            ProjectileSource shooter = proj.getShooter();
            if(shooter instanceof Player p){
                attacker = p;
            }
        }

        if(attacker == null){
            return;
        }
        if(event.getEntity() instanceof Player p){
            victim = p;
        }
        else if(event.getEntity() instanceof Projectile proj){
            ProjectileSource shooter = proj.getShooter();
            if(shooter instanceof Player p){
                victim = p;
            }
        }
        if(victim == null){
            return;
        }

        int seconds = plugin.getTimerSeconds();
        plugin.startTimer(attacker, seconds);

        if (plugin.isIncludeVictim()){
            plugin.startTimer(victim, seconds);
        }
    }
}
