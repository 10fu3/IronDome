package ru.plsecuritylab.irondome;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import ru.plsecuritylab.irondome.Commands.*;
import ru.plsecuritylab.irondome.DataManager.PermData;
import ru.plsecuritylab.irondome.Utils.PlayerDisplay;

import java.util.ArrayList;
import java.util.List;

import static ru.plsecuritylab.irondome.DataManager.PermData.AddPrefix;

/**
 * Created by msfblue1 on 2017/04/09.
 */
public class MainCommand implements CommandExecutor {
    private IronDome plugin;

    public MainCommand(IronDome me) {
        this.plugin = me;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(AddPrefix("コマンドブロックからのアクセスは禁止です。"));
            return false;
        }

        if ("id".equalsIgnoreCase(command.getName())) {
            if(args.length == 0){
                PermData.SendHelp(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("search")) {
                if(args.length == 1){
                    PermData.SendHelp(sender);
                    return true;
                }
                if (!PermData.HasPermission(sender,"irondome.info.search")) {
                    sender.sendMessage(AddPrefix(ChatColor.RED + "権限がありません。"));
                    return true;
                }else{
                    List<CommandSender> senders = new ArrayList<>();
                    senders.add(sender);
                    if(args.length == 2){
                        new Thread(new PlayerDisplay(plugin, senders, args[1],false)).start();
                    }else if(args.length == 3 && args[2].equalsIgnoreCase("-a")){
                        new Thread(new PlayerDisplay(plugin, senders, args[1],true)).start();
                    }else{
                        sender.sendMessage(AddPrefix(ChatColor.RED + "引数が不正です。"));
                    }
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("shield")) {
                CommandSettings.getInstance(plugin).onCommand(sender,args);
                return true;
            } else if (args[0].equalsIgnoreCase("history")){
                CommandLoginHistory.onCommands(sender);
            } else if(args.length == 2 && args[0].equalsIgnoreCase("mode") && args[1].equalsIgnoreCase("-list")){
                sender.sendMessage(AddPrefix(ChatColor.YELLOW+"次のプレーヤーが情報表示されません。"));
                for(CommandSender p:plugin.noneedinfo){
                    sender.sendMessage(p.getName());
                }
                return true;
            }else if(args.length == 1 && args[0].equalsIgnoreCase("mode")) {
                if (plugin.noneedinfo.contains(sender)) {
                    plugin.noneedinfo.remove(sender);
                    sender.sendMessage(AddPrefix(ChatColor.YELLOW + "ログイン時のInfoは表示されます。"));
                } else {
                    plugin.noneedinfo.add(sender);
                    sender.sendMessage(AddPrefix(ChatColor.YELLOW + "ログイン時のInfoは非表示になりました。"));
                }
            }else{
                PermData.SendHelp(sender);
            }
        }
        return false;
    }
}
