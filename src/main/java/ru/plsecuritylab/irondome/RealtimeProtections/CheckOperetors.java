package ru.plsecuritylab.irondome.RealtimeProtections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import ru.plsecuritylab.irondome.IronDome;
import ru.plsecuritylab.irondome.DataManager.PermData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msfblue1 on 2017/06/20.
 */
public class CheckOperetors extends BukkitRunnable{
    private IronDome plugin;
    public CheckOperetors(IronDome me){
        this.plugin = me;
    }


    @Override
    public void run() {
        plugin.getServer().getScheduler().runTaskTimer(plugin,()->{
            List<OfflinePlayer> ops = new ArrayList<>();
            ops.addAll(Bukkit.getOperators());
            if(!plugin.getConfig(plugin).getOps().isEmpty()){
                for (int i = 0; i < plugin.getConfig(plugin).getOps().size(); i++) {
                    OfflinePlayer player = PermData.getOfflinePlayer(plugin.getConfig(plugin).getOps().get(i));
                    if(!Bukkit.getOperators().contains(player)){
                        player.setOp(true);
                        PermData.ConvertSender(PermData.DoPlayer("irondome.plugins")).forEach((info) -> {
                            info.sendMessage(ChatColor.RED + "OP削除検知 " + ChatColor.AQUA + "結果: " + ChatColor.GREEN + player.getName() + " を追加しました");
                        });
                    }
                }
                for (int i = 0; i < ops.size(); i++) {
                    if(!plugin.getConfig(plugin).getOps().contains(ops.get(i).getName().toLowerCase())){
                        ops.get(i).setOp(false);
                        int finalI = i;
                        PermData.ConvertSender(PermData.DoPlayer("irondome.plugins")).forEach((info) -> info.sendMessage(ChatColor.RED + "OP追加検知 " + ChatColor.AQUA + "結果: " + ChatColor.GREEN + ops.get(finalI).getName() + " を削除しました"));
                    }
                }
            }
        },10,1500);
    }
}
