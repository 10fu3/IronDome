package ru.plsecuritylab.irondome.DataManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.plsecuritylab.irondome.IronDome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msfblue1 on 2017/05/30.
 */
public class PermData {

    public static String AddPrefix(String mes){
        return (ChatColor.AQUA + "[IronDome] ") + ChatColor.RESET + mes;
    }

    public static void SendHelp(CommandSender sender){
        if(HasPermission(sender,"irondome.util.access")){
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"======ヘルプ======"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id search <UserName> "+ChatColor.GREEN+"指定したプレーヤの簡易情報を表示します"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id search <UserName> -a "+ChatColor.GREEN+"指定したプレーヤのすべての情報を表示します"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id shield "+ChatColor.GREEN+"各シールドの状況を表示します"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id shield <ShieldName> <true|false> "+ChatColor.GREEN+"指定したシールドを有効・無効化します"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id history "+ChatColor.GREEN+"ログインしたプレーヤーの一覧を表示します。"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/idreload "+ChatColor.GREEN+"config.ymlを再読み込みします"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id ban USERNAME "+ChatColor.GREEN+"指定したプレーヤーをBANします"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id mode "+ChatColor.GREEN+"ログイン時のInfoを表示・非表示にします"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id move USERNAME "+ChatColor.GREEN+"プレーヤーの動きをストップします"));
            sender.sendMessage(AddPrefix(ChatColor.AQUA+"/id spam USERNAME "+ChatColor.GREEN+"プレーヤーのチャットをストップします"));
        }
    }

    public static boolean HasPermission(CommandSender sender, String perm){
        if(sender instanceof Player){
            return sender.hasPermission(perm);
        }else if(sender instanceof ConsoleCommandSender){
            return true;
        }
        return false;
    }

    public static List<Player> GetOnlineOperators(){
        List<Player> returnData = new ArrayList<>();
        for (int i = 0; i < Bukkit.getOfflinePlayers().length; i++) {
            OfflinePlayer player = Bukkit.getOfflinePlayers()[i];
            if(player.isOp() && player.isOnline()){
                returnData.add(player.getPlayer());
            }
        }
        return returnData;
    }

    public static List<CommandSender> ConvertSender(List<Player> players) {
        if (players != null) {
            List<CommandSender> returnPlayer = new ArrayList<>();
            returnPlayer.add(Bukkit.getConsoleSender());
            returnPlayer.addAll(players);
            return returnPlayer;
        } else {
            return null;
        }
    }

    public static void puts(List<CommandSender> senders, String mes){
        senders.forEach(sender -> sender.sendMessage(AddPrefix(mes)));
    }

    public static void puts(String Sendmessage) {
        Bukkit.getConsoleSender().sendMessage(AddPrefix(ChatColor.RESET + Sendmessage));
    }

    public static List<Player> DoPlayer(String per) {
        //Infoを表示する対象のリスト
        List<Player> Users = new ArrayList<>();
        //権限持ちにメッセージを通知するために通知対象リストに追加
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            if (player.hasPermission(per)) {
                Users.add(player);
            }
        }
        return Users;
    }

    public static OfflinePlayer getOfflinePlayer(String name){
        OfflinePlayer offlinePlayer = null;
        boolean local = false;
        if(Bukkit.getOfflinePlayers().length != 0){
            for (int i = 0; i < Bukkit.getOfflinePlayers().length; i++) {
                if(name.equalsIgnoreCase(Bukkit.getOfflinePlayers()[i].getName())){
                    local = true;
                    offlinePlayer = Bukkit.getOfflinePlayers()[i];
                }
            }
            if(!local){
                offlinePlayer = Bukkit.getOfflinePlayer(name);
            }
        }
        return offlinePlayer;
    }

    public static boolean canJoin(User playerData){

        Config config = IronDome.getConfig(IronDome.getPlugin());
        Boolean flag = true;
        if (config.getBancount() > 0) {
            if (playerData.getMcbansCount() >= config.getBancount()) {
                //Bukkit.getConsoleSender().sendMessage(playerData.getName()+" はグローバルBANの回数が多すぎです。");
                flag = false;
            }
        }
        if (playerData.getMcbansReputation() <= config.getLoginrep()) {
            //Bukkit.getConsoleSender().sendMessage(playerData.getName()+" は評判値が低すぎます。");
            flag = false;
        }
        if (playerData.getJMPBanReason().equalsIgnoreCase("Compromised Account")) {
            //Bukkit.getConsoleSender().sendMessage(playerData.getName()+" は盗まれたアカウントです。");
            flag = false;
        }
        if (playerData.getJMPBanReason().equalsIgnoreCase("盗難アカウントへの不正アクセス行為")){
            //Bukkit.getConsoleSender().sendMessage(playerData.getName()+" は盗まれたアカウントにアクセスしています。");
            flag = false;
        }
        if (!config.getDisallowcounty().isEmpty()){
            if(playerData.isOnline()){
                if(!playerData.isLocalHost()){
                    for (int i = 0; i < config.getDisallowcounty().size(); i++) {
                        if(playerData.getJoinCountryCODE().equalsIgnoreCase(config.getDisallowcounty().get(i))){
                            flag = false;
                        }
                    }
                }
            }
        }
        if (!config.getAllowcounty().isEmpty()){
            if(playerData.isOnline()){
                if(!playerData.isLocalHost()){
                    Boolean flag2 = false;
                    for (int i = 0; i < config.getAllowcounty().size(); i++) {
                        if(playerData.getJoinCountryCODE().equalsIgnoreCase(config.getAllowcounty().get(i))){
                            flag2 = true;
                        }
                    }
                    if(!flag2){
                        flag = false;
                    }
                }
            }
        }

        if(flag){
            return true;
        }else{
            return false;
        }
    }
}
