package ru.plsecuritylab.irondome.RealtimeProtections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.PermData;

import java.util.List;

/**
 * Created by msfblue1 on 2017/06/11.
 */
public class CheckCensor extends BukkitRunnable{
    IronDome plugin;

    public CheckCensor(IronDome me){
        this.plugin = me;
    }

    @Override
    public void run(){
        List<RegisteredListener> listeners = HandlerList.getRegisteredListeners(plugin);
        if(listeners.size() != plugin.Listeners.size()){
            for (int i = 0; i < plugin.Listeners.size(); i++) {
                if(!listeners.contains(plugin.Listeners.get(i))){
                    Bukkit.getPluginManager().registerEvents(plugin.Listeners.get(i).getListener(),plugin);
                    PermData.puts( PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), PermData.AddPrefix(ChatColor.YELLOW+"IronDomeのイベントリスナーを有効化しました。"));
                    PermData.puts( PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), PermData.AddPrefix(ChatColor.YELLOW+"第三者の悪意あるプラグイン・コマンドによる攻撃の可能性があります。"));
                    PermData.puts( PermData.ConvertSender(PermData.DoPlayer("irondome.info.plugins")), PermData.AddPrefix(ChatColor.YELLOW+"警戒・確認してください。"));
                }
            }
        }
    }
}
