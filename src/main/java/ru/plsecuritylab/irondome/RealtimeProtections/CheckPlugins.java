package ru.plsecuritylab.irondome.RealtimeProtections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.plsecuritylab.irondome.DataManager.PermData;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.Utils.PluginManager;

/**
 * Created by msfblue1 on 2017/08/28.
 */
public class CheckPlugins implements Listener{

    private IronDome plugin;

    public CheckPlugins(IronDome me){
        Bukkit.getServer().getPluginManager().registerEvents(this,me);
        this.plugin = me;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void getPluginDisableEvent(PluginDisableEvent e){
        String TargetName = new String(e.getPlugin().getName().toCharArray());
        Plugin target = PluginManager.getPlugin(TargetName);
        if(target == null) {
            target = PluginManager.loadPlugin(TargetName);
            if(target != null) {
                PermData.puts(PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), ChatColor.YELLOW + target.getName() + "をロード&有効化しました。");
            } else {
                this.plugin.getConfig(plugin).getPlugins().remove(TargetName);
            }
        }else{
            if(!target.isEnabled()){
                Bukkit.getPluginManager().enablePlugin(target);
                PermData.puts(PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), ChatColor.YELLOW + target.getName() + "を有効化しました。");
            }
        }
    }
}
