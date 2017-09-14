package ru.plsecuritylab.irondome.DataManager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.plsecuritylab.irondome.IronDome;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by msfblue1 on 2017/07/15.
 */
public class YamlManager {
    private File dir = new File(new StringBuilder().append("plugins/").append(IronDome.getPlugin().getDescription().getName()).append("/Database").toString());
    public YamlManager(){
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public OfflinePlayerData parseOfflinePlayerData(File file){
        YamlConfiguration yaml = new YamlConfiguration();
        OfflinePlayerData opd = new OfflinePlayerData();
        if(file.exists()){
            try {
                yaml.load(file);
                opd.setUUID(file.getName());
                opd.setIP(yaml.getString("IP"));
                opd.setName(yaml.getString("Name"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        return opd;
    }

    public boolean exists(String UUID){
        File file = new File(dir,UUID);
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    public void saveData(OfflinePlayerData data,User user){
        File file = new File(dir,user.getUuid());
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("IP",data.getIP());
        yaml.set("Name",data.getName());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(User player){
        File file = new File(dir,player.getUuid());
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OfflinePlayerData opd = parseOfflinePlayerData(file);
        YamlConfiguration yaml = new YamlConfiguration();
        if(player.isOnline()){
            yaml.set("IP",player.getAddress().getHostAddress());
        }else{
            yaml.set("IP",opd.getIP());
        }
        yaml.set("Name",player.getName());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<OfflinePlayerData> getDatas(){
        List<OfflinePlayerData> data = new ArrayList<>();
        for (int i = 0; i < dir.listFiles().length; i++) {
            File file = dir.listFiles()[i];
            data.add(parseOfflinePlayerData(file));
        }
        return data;
    }

    public List<OfflinePlayerData> getSameIPs(String IP){
        List<OfflinePlayerData> data = getDatas();
        List<OfflinePlayerData> returns = new ArrayList<>();
        if(!data.isEmpty()){
            for (int i = 0; i < data.size(); i++) {
                OfflinePlayerData opd = data.get(i);
                if(opd.getIP().equalsIgnoreCase(IP)){
                    returns.add(opd);
                }
            }
        }
        return returns;
    }

    public OfflinePlayerData getSameUUID(String UUID){
        File file = new File(dir,UUID);
        if(file.exists()){
            return parseOfflinePlayerData(file);
        }
        return new OfflinePlayerData();
    }

}
