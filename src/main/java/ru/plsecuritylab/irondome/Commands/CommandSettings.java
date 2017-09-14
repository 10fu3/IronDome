package ru.plsecuritylab.irondome.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.Utils.ShieldMonitor;

/**
 * Created by msfblue1 on 2017/04/29.
 */
public class CommandSettings{

    private IronDome plugin;
    public CommandSettings (IronDome me){
        plugin = me;
    }

    public static CommandSettings getInstance (IronDome me){
        return new CommandSettings(me);
    }

    public boolean onCommand(CommandSender sender,String[] args){

        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "Disallow access from command block.");
            return true;
        }
        if (sender instanceof Player){
            if(!sender.hasPermission("irondome.util.shield")){
                sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "You don't have permission.");
                return true;
            }
        }

        if (args.length == 1 || args.length == 2) {
            ShieldMonitor.getInstance().Monitor(plugin, sender);
            return true;
        }

        if (args.length == 3) {
            switch (args[1]) {
                case "selfdefence":
                    if (args.length == 3) {
                        sender.sendMessage(ChatColor.RED + "config.ymlを書き換えた後、/idreloadを実行するかプラグインを再起動してください。");
                    } else {
                        ShieldMonitor.getInstance().Monitor(plugin, sender);
                    }
                    break;
                case "loginchecker":
                    if (args[2].equalsIgnoreCase("true")) {
                        this.plugin.getConfig(plugin).setUsercheckmode(true);
                        sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.GREEN + "LoginChecker が有効になりました");
                        break;
                    } else if (args[2].equalsIgnoreCase("false")) {
                        this.plugin.getConfig(plugin).setUsercheckmode(false);
                        sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "LoginChecker が無効になりました");
                        break;
                    } else {
                        sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "引数が正しくありません。");
                    }
                    break;
                case "pluginchecker":
                    if (args[2].equalsIgnoreCase("true")) {
                        this.plugin.getConfig(plugin).setPlugincheckmode(true);
                        sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.GREEN + "LivePluginChecker が有効になりました");
                        break;
                    } else if (args[2].equalsIgnoreCase("false")) {
                        this.plugin.getConfig(plugin).setPlugincheckmode(false);
                        sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "LivePluginChecker が無効になりました");
                        break;
                    } else {
                        sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "引数が正しくありません。");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.AQUA + "[IronDome] " + ChatColor.RED + "引数が正しくありません。");
                    break;
            }
        }

        return false;
    }
}
