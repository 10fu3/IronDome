package ru.plsecuritylab.irondome.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.plsecuritylab.irondome.DataManager.PermData;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.Config;

/**
 * Created by msfblue1 on 2017/07/04.
 */
public class CommandReStart implements CommandExecutor{
    private IronDome plugin;

    public CommandReStart(IronDome me){
        this.plugin = me;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("idreload".equalsIgnoreCase(command.getName())){
            if (sender instanceof Player) {
                if (sender.hasPermission("irondome.util.reload")) {
                    plugin.setConfig(plugin);
                    sender.sendMessage(PermData.AddPrefix(ChatColor.GREEN + "Reload completed!"));
                    return true;
                } else {
                    sender.sendMessage(PermData.AddPrefix(ChatColor.RED + "You don't have permission."));
                    return true;
                }
            } else if (sender instanceof ConsoleCommandSender) {
                plugin.reload();
                sender.sendMessage(PermData.AddPrefix(ChatColor.GREEN + "Reload completed!"));
                return true;
            }

            return false;
        }else{
            return true;
        }
    }
}
