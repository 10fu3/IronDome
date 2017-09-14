package ru.plsecuritylab.irondome.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.plsecuritylab.irondome.DataManager.Config;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.PermData;

/**
 * Created by msfblue1 on 2017/04/29.
 */
public class ShieldMonitor {

    public static ShieldMonitor getInstance(){
        return new ShieldMonitor();
    }

    public void Monitor(IronDome plugin , CommandSender sender){

        Config config = plugin.getConfig(plugin);
        sender.sendMessage(PermData.AddPrefix(ChatColor.BLUE+"======モジュールの稼働状況======"));

        if(config.isSelfdefense()){
            sender.sendMessage(PermData.AddPrefix(ChatColor.AQUA+"SelfDefence"+ChatColor.GREEN+" 稼働"));
        }else{
            sender.sendMessage(PermData.AddPrefix(ChatColor.AQUA+"SelfDefence"+ChatColor.RED+" 未稼働・エラー"));
        }
        if(config.isUsercheckmode()){
            sender.sendMessage(PermData.AddPrefix(ChatColor.AQUA+"LoginChecker"+ChatColor.GREEN+" 稼働"));
        }else{
            sender.sendMessage(PermData.AddPrefix(ChatColor.AQUA+"LoginChecker"+ChatColor.RED +" 未稼働・エラー"));
        }
        if(config.isPlugincheckmode()){
            sender.sendMessage(PermData.AddPrefix(ChatColor.AQUA+"PluginChecker"+ChatColor.GREEN+" 稼働"));
        }else{
            sender.sendMessage(PermData.AddPrefix(ChatColor.AQUA+"PluginChecker"+ChatColor.RED +" 未稼働・エラー"));
        }

        sender.sendMessage(PermData.AddPrefix(ChatColor.BLUE+"======モジュールの稼働状況======"));
    }
}
