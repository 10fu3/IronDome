package ru.plsecuritylab.irondome.RealtimeProtections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.PermData;
import ru.plsecuritylab.irondome.Utils.PluginManager;

/**
 * Created by msfblue1 on 2017/07/10.
 */
public class OldCheckPlugins implements Listener{
    private IronDome plugin;

    public OldCheckPlugins(IronDome me){
        this.plugin = me;

        Bukkit.getScheduler().runTaskTimer(plugin,()-> plugin.getServer().getScheduler().runTaskAsynchronously(plugin,()->{

            if(!this.plugin.getConfig(plugin).getPlugins().isEmpty()){
                for (int i = 0; i < this.plugin.getConfig(plugin).getPlugins().size(); i++) {
                    Plugin target = PluginManager.getPlugin(this.plugin.getConfig(plugin).getPlugins().get(i));
                    if(target == null) {
                        target = PluginManager.loadPlugin(this.plugin.getConfig(plugin).getPlugins().get(i));
                        if(target != null) {
                            PermData.puts(PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), ChatColor.YELLOW + target.getName() + "をロード&有効化しました。");
                        } else {
                            this.plugin.getConfig(plugin).getPlugins().remove(this.plugin.getConfig(plugin).getPlugins().get(i));
                        }
                    }else{
                        if(!target.isEnabled()){
                            Bukkit.getPluginManager().enablePlugin(target);
                            PermData.puts(PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), ChatColor.YELLOW + target.getName() + "を有効化しました。");
                        }
                    }
                }
            }
        }),40,10);
    }
}
