package ru.plsecuritylab.irondome.RealtimeProtections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.PermData;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by msfblue1 on 2017/05/31.
 */
public class CheckPermissionPlayer extends BukkitRunnable implements Listener{

    IronDome plugin;
    boolean flag = false;

    public CheckPermissionPlayer(IronDome me){
        this.plugin = me;
        this.flag = true;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @Override
    public void run() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin,()->{
            if(!flag){
                return;
            }
            if(plugin.getConfig(plugin).getKickpermission().size() == 0){
                return;
            }
            for(Player player : Bukkit.getOnlinePlayers()){
                PermissionUser user = PermissionsEx.getUser(player);
                if(!plugin.getConfig(plugin).getKickpermission().isEmpty()){
                    for (int i = 0; i < plugin.getConfig(plugin).getKickpermission().size(); i++) {
                        if(user.has(plugin.getConfig(plugin).getKickpermission().get(i),player.getWorld().getName())){
                            plugin.getServer().getScheduler().runTask(plugin,()->{
                                player.kickPlayer(ChatColor.RED+"不正な権限を検知しました。");
                                for (int j = 0; j < PermData.DoPlayer("irondome.info.op").size(); j++) {
                                    Player admin = PermData.DoPlayer("irondome.info.op").get(j);
                                    if(admin != null){
                                        admin.sendMessage(ChatColor.RED+player.getName()+"の不正な権限の所持を検知しました。");
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void getShutdown(PluginDisableEvent e){
        if(e.getPlugin().getName().equalsIgnoreCase("PermissionsEx")){
            this.flag = false;
        }
    }

    @EventHandler
    public void getEnable(PluginEnableEvent e){
        if(e.getPlugin().getName().equalsIgnoreCase("PermissionsEx")){
            this.flag = true;
        }
    }
}
