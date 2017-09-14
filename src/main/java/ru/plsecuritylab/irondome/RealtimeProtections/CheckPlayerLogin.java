package ru.plsecuritylab.irondome.RealtimeProtections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.plsecuritylab.irondome.DataManager.*;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.Utils.PlayerDisplay;

import java.util.Date;
import java.util.List;

import static ru.plsecuritylab.irondome.DataManager.PermData.canJoin;

/**
 * Created by msfblue1 on 2017/06/21.
 */
public class CheckPlayerLogin implements Listener{
    private IronDome plugin;

    public CheckPlayerLogin(IronDome me){
        this.plugin = me;
        Bukkit.getPluginManager().registerEvents(this,plugin);
        PermData.puts(ChatColor.GREEN+"CensorLoginPlayerが有効になりました。");
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void getLoginEvent(PlayerJoinEvent e){
        YamlManager manager = new YamlManager();
        if(!e.getPlayer().hasPermission("irondome.bypath.alts")){
            Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
                User playerData = new User(e.getPlayer().getName(),e.getPlayer().getAddress().getAddress());
                manager.saveData(playerData);
                List<OfflinePlayerData> opds = manager.getSameIPs(manager.getSameUUID(playerData.getUuid()).getIP());
                Integer setcount = plugin.getConfig(plugin).getAltcount();
                Integer accounts = opds.size();
                if(!(setcount < 0)) {
                    if (accounts - 1 > setcount) {
                        Bukkit.getScheduler().runTask(plugin,()->{
                            if(e.getPlayer() != null){
                                e.getPlayer().kickPlayer(ChatColor.RED+"利用可能なサブアカウントの数を超えています。管理者までお問い合わせください。");
                                List<CommandSender> senders = PermData.ConvertSender(PermData.DoPlayer("irondome.info.join"));
                                if(!senders.isEmpty()){
                                    for (int i = 0; i < senders.size(); i++) {
                                        senders.get(i).sendMessage(PermData.AddPrefix(ChatColor.AQUA+e.getPlayer().getName()+ChatColor.YELLOW+"はキックされました。理由:サブアカウント数上限"));
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void getLoginEvent(AsyncPlayerPreLoginEvent e){
        if (plugin.getConfig(plugin).isUsercheckmode()) {
            User playerData = new User(e.getName(),e.getAddress());
            List<CommandSender> senders = PermData.ConvertSender(PermData.DoPlayer("irondome.info.join"));
            if(!plugin.noneedinfo.isEmpty()){
                for (int i = 0; i < plugin.noneedinfo.size(); i++) {
                    if(senders.contains(plugin.noneedinfo.get(i))){
                        senders.remove(plugin.noneedinfo.get(i));
                    }
                }
            }
            if (!canJoin(playerData)) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "ログインに必要な条件を満たしていません。");
                PermData.puts(senders,ChatColor.YELLOW+playerData.getName()+" はログイン条件を満たさなかったためログインに失敗しました。");
            } else {
                new Thread(new PlayerDisplay(plugin, senders,e.getName(),false)).start();
            }
        }
    }

}
