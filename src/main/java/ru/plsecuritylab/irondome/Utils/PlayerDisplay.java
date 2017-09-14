package ru.plsecuritylab.irondome.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import ru.plsecuritylab.irondome.DataManager.OfflinePlayerData;
import ru.plsecuritylab.irondome.DataManager.User;
import ru.plsecuritylab.irondome.DataManager.YamlManager;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.PermData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msfblue1 on 2017/06/18.
 */
public class PlayerDisplay extends ru.plsecuritylab.irondome.Network.HTTPConnect implements Runnable {

    List<String> category = new ArrayList<>();
    List<String> value = new ArrayList<>();

    //自分自身のインスタンス
    private IronDome plugin;
    //検索するプレーヤーの名前（String)
    public String Target = "";
    //情報の送信先に関する情報 ここではコマンドを送信するのは一人であるためリストである必要はないが、ログイン時の一斉送信時にはリストを扱うため、ここではリストで統一する。
    public List<CommandSender> Sendtargets;

    public Boolean all = false;


    public PlayerDisplay(IronDome me, List<CommandSender> sendtargets, String target, Boolean all) {
        this.plugin = me;
        this.Sendtargets = sendtargets;
        this.Target = target;
        this.all = all;
    }

    public void put(String category, String value) {
        this.category.add(category);
        this.value.add(value);
    }

    //検索用
    @Override
    public void run() {
        User playerdata = new User(Target,null);
        if(!playerdata.exist()) {
            PermData.puts(Sendtargets,ChatColor.YELLOW+"アカウントが見つからないか、APIサーバーがダウンしています。");
            PermData.puts(Sendtargets,ChatColor.YELLOW+"JMS info: "+"https://minecraft.jp/players/"+playerdata.getName());
        }else{
            YamlManager manager = new YamlManager();
            List<OfflinePlayerData> opds = manager.getSameIPs(manager.getSameUUID(playerdata.getUuid()).getIP());
            if(all){
                put("UUID",playerdata.getUuid());
            }
            //GBANされているか
            if(playerdata.getMcbansCount() != 0){
                put("GBAN Count",ChatColor.RED+String.valueOf(playerdata.getMcbansCount()));
                if(all){
                    put("Reputation",String.valueOf(playerdata.getMcbansReputation()));
                    if(!(playerdata.getMcbansHistory().length == 0)){
                        put("","===== MCBans Data =====");
                        for (int i = 0; i < playerdata.getMcbansHistory().length; i++) {
                            put(String.valueOf(i+1),playerdata.getMcbansHistory()[i]);
                        }
                    }
                }
            }
            //JMPBANされているか
            if (playerdata.getJMPBanReason() != null) {
                if (playerdata.getJMPBanReason().equalsIgnoreCase("Compromised Account")) {
                    put("盗難アカウントの可能性",ChatColor.RED+"高");
                }
            }
            put("JMS info","https://minecraft.jp/players/"+playerdata.getName());
            //過去プレイしたことがあるか
            if(playerdata.hasPlayedBefore()){
                String time = new SimpleDateFormat("yyyy/MM/dd").format(playerdata.getFirstPlayed());
                if(all){
                    put("初回ログイン",time);
                }
                time = new SimpleDateFormat("yyyy/MM/dd").format(playerdata.getLastPlayed());
                put("最終ログイン",time);
            }else{
                category.add("初回ログイン");
                value.add("情報なし");
            }
            if(!playerdata.isLocalHost()){
                put("IP",playerdata.getIPAddress());
                if(all){
                    put("HostName",playerdata.getAddress().getHostName());
                }
                put("Country",playerdata.getJoinCountry());
                if(all){
                    put("AddressRegion",playerdata.getJoinRegion());
                }
            }else if(playerdata.isOnline()){
                put("IP","LocalHost");
            }
            //オンラインか
            if(playerdata.isOnline()){

                if(all){
                    StringBuilder builder = new StringBuilder();
                    builder.append(playerdata.getLocation().getWorld().getName());
                    builder.append(" , ");
                    builder.append(playerdata.getLocation().getBlockX());
                    builder.append(" , ");
                    builder.append(playerdata.getLocation().getBlockY());
                    builder.append(" , ");
                    builder.append(playerdata.getLocation().getBlockZ());

                    put("GameMode",playerdata.getGameMode().toString());

                    put("現在地",builder.toString());

                }

            }

            if(playerdata.isOp()){
                put("OP",String.valueOf(playerdata.isOp()));
            }
            if(playerdata.isBanned()){
                put("Ban",ChatColor.RED+String.valueOf(playerdata.isBanned()));
            }
            if(playerdata.isWhitelisted()){
                put("White list",String.valueOf(playerdata.isWhitelisted()));
            }
            if(opds.size() > 1){
                StringBuilder builder = new StringBuilder().append(ChatColor.YELLOW);
                for (int i = 0; i < opds.size(); i++) {
                    if(!opds.get(i).getName().equalsIgnoreCase(playerdata.getName())){
                        builder.append(opds.get(i).getName());
                        builder.append(" ");
                    }
                }
                put(ChatColor.RED+"Alts",builder.toString());
            }
            if(!"".equalsIgnoreCase(manager.getSameUUID(playerdata.getUuid()).getIP())){
                put("最後に確認したアドレス",manager.getSameUUID(playerdata.getUuid()).getIP());
            }
            if(!PermData.canJoin(playerdata)){
                put("ログイン時のパス","false");
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                //渡す先がなかったら無視
                if (Sendtargets != null && !Sendtargets.isEmpty()) {
                    for (int i = 0; i < Sendtargets.size(); i++) {
                        CommandSender sender = Sendtargets.get(i);
                        if(sender != null){
                            if (sender instanceof ConsoleCommandSender) {
                                sender.sendMessage("");
                                sender.sendMessage(ChatColor.BLUE + "===== " + ChatColor.AQUA + playerdata.getName() + ChatColor.BLUE + " に関する情報=====");
                                if(!all){
                                    sender.sendMessage(ChatColor.AQUA +"'-a'をコマンドに追加すると全情報が表示されます。");
                                }
                            } else {
                                sender.sendMessage("");
                                sender.sendMessage(ChatColor.BLUE + "===== " + ChatColor.AQUA + playerdata.getName() + ChatColor.BLUE + " に関する情報=====");
                                if(!all){
                                    sender.sendMessage(ChatColor.AQUA +"'-a'をコマンドに追加すると全情報が表示されます。");
                                }
                            }
                            for (int j = 0; j < category.size(); j++) {
                                if (sender instanceof ConsoleCommandSender) {
                                    sender.sendMessage(ChatColor.BLUE + category.get(j) + ChatColor.AQUA + " => " + ChatColor.GREEN + value.get(j));
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + category.get(j) + ChatColor.AQUA + " => " + ChatColor.GREEN + value.get(j));
                                }
                            }
                        }
                    }
                }

            });
        }
    }
}
