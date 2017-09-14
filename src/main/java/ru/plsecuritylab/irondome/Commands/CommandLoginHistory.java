package ru.plsecuritylab.irondome.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.plsecuritylab.irondome.DataManager.PermData;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by msfblue1 on 2017/06/03.
 */
public class CommandLoginHistory {
    public static void onCommands(CommandSender sender){
        if(sender instanceof BlockCommandSender){
            sender.sendMessage(ChatColor.RED+"Disallow access from command block.");
            return;
        }
        if(sender instanceof Player){
            Player target = (Player)sender;
            if(target.hasPermission("irondome.info.history")){
                sender.sendMessage(PermData.AddPrefix(ChatColor.RED+"You don't have permissions."));
                return;
            }
        }
        List<OfflinePlayer> offlinePlayers = Arrays.asList(Bukkit.getOfflinePlayers());
        List<OfflinePlayer> afterPlayers = new ArrayList<>();
        List<Long> dates = new ArrayList<>();

        for (int i = 0; i < offlinePlayers.size(); i++) {
            dates.add(offlinePlayers.get(i).getLastPlayed());
        }

        Long[] dateArray = dates.toArray(new Long[dates.size()]);
        Arrays.sort(dateArray,Collections.reverseOrder());

        for (int i = 0; i < dateArray.length; i++) {
            Long date = dateArray[i];
            for (int j = 0; j < offlinePlayers.size(); j++) {
                OfflinePlayer player = offlinePlayers.get(j);
                if(date.equals(player.getLastPlayed())){
                    afterPlayers.add(player);
                }
            }
        }

        List<String> messages = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.BLUE);
        builder.append("======Join history======");
        messages.add(builder.toString());

        builder = new StringBuilder();

        for (int i = 0; i < afterPlayers.size(); i++) {
            OfflinePlayer player = afterPlayers.get(i);
            String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(player.getLastPlayed());
            builder.append(time);
            builder.append(" ");
            builder.append(ChatColor.GREEN);
            builder.append(player.getName());
            messages.add(builder.toString());
            builder = new StringBuilder();
        }

        for (int i = 0; i < messages.size(); i++) {
            sender.sendMessage(messages.get(i));
        }
    }
}
