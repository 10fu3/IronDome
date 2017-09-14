package ru.plsecuritylab.irondome;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.plsecuritylab.irondome.Commands.CommandReStart;
import ru.plsecuritylab.irondome.RealtimeProtections.CheckPlayerLogin;
import ru.plsecuritylab.irondome.RealtimeProtections.*;
import ru.plsecuritylab.irondome.DataManager.Config;
import ru.plsecuritylab.irondome.DataManager.PermData;

import java.util.ArrayList;
import java.util.List;


public final class IronDome extends JavaPlugin {

    private static IronDome plugin;

    private static Config config;

    public List<RegisteredListener> Listeners = new ArrayList<>();

    //public Map<String,List> Alts = new HashMap<>();
    public List<CommandSender> noneedinfo = new ArrayList<>();

    public List<Player> stopmove = new ArrayList<>();

    public static Config getConfig(IronDome me){
        return config;
    }

    public void setConfig(IronDome me){
        this.config = new Config(me);
    }

    public static IronDome getPlugin(){
        return plugin;
    }

    public void reload(){
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        this.reloadConfig();
        load();
    }

    public void load(){
        //設定ファイルがなかった場合に生成する
        saveDefaultConfig();
        plugin = this;
        config = new Config(this);
        if(config.getConfigver().compareTo(4) == 0){
            if(config.isBootflag()){
                this.getServer().getPluginCommand("id").setExecutor(new MainCommand(this));
                new CheckPlayerLogin(this);
                new OldCheckPlugins(this);
                new CheckOperetors(this).runTaskTimer(this, 0, 1);
                new CheckCensor(this).runTaskTimer(this,0,1);
                //new CheckBlock(this).runTaskTimer(this,0,20);
                //new CheckPlayerMove(this);
                //new CheckChat(this);

                if(Bukkit.getPluginManager().getPlugin("PermissionsEx") != null){
                    new CheckPermissionPlayer(this).runTaskTimer(this,0,10);
                }

                //イベントをフックするクラスに自分自身を渡してインスタンスを生成
                PermData.puts(ChatColor.GREEN + "保護されています。");
                Listeners = HandlerList.getRegisteredListeners(this);
            }else{
                PermData.puts(ChatColor.YELLOW + "設定ファイルを生成しました");
                PermData.puts(ChatColor.YELLOW + "保護を有効にするには再度プラグインをロードしなおしてください。");
                PermData.puts(ChatColor.RED + "保護されていません！");
            }
        }else{
            PermData.puts(ChatColor.YELLOW + "設定ファイルのバージョンが合いません！");
            PermData.puts(ChatColor.YELLOW + "保護を有効にするには一度plugins/IronDome/config.ymlを削除し、再度プラグインをロードしなおしてください。");
            PermData.puts(ChatColor.RED + "保護されていません！");
        }
    }

    @Override
    public void onEnable() {
        this.reloadConfig();
        this.getServer().getPluginCommand("idreload").setExecutor(new CommandReStart(this));
        load();
    }

    @Override
    public void onDisable() {
        if (config.isSelfdefense()) {
            PermData.puts(ChatColor.RED + "IronDomeの自己防御機能が作動しました。サーバーを終了します。");
            this.getServer().shutdown();
        }
        PermData.puts(ChatColor.RED + "保護が無効になりました。");
    }


}
