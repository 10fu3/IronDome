package ru.plsecuritylab.irondome.DataManager;

import ru.plsecuritylab.irondome.IronDome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msfblue1 on 2017/07/04.
 */
public class Config {
    boolean bootflag = false;
    boolean selfdefense = false;

    boolean usercheckmode = false;
    boolean plugincheckmode = true;


    Integer configver = 0;
    Integer altcount = 0;
    Double loginrep = 3.0;
    Integer bancount = 5;
    List<String> allowcounty = new ArrayList<>();
    List<String> disallowcounty = new ArrayList<>();
    List<String> ops = new ArrayList<>();
    List<String> plugins = new ArrayList<>();
    List<String> kickpermission = new ArrayList<>();

    public Integer getAltcount() {
        return altcount;
    }

    public void setAltcount(Integer altcount) {
        this.altcount = altcount;
    }

    public Integer getConfigver(){
        return configver;
    }

    public void setSelfdefense(boolean flag){
        this.selfdefense = flag;
    }

    public Double getLoginrep() {
        return loginrep;
    }

    public Integer getBancount() {
        return bancount;
    }

    public List<String> getAllowcounty() {
        return allowcounty;
    }

    public List<String> getDisallowcounty() {
        return disallowcounty;
    }

    public List<String> getOps() {
        return ops;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public List<String> getKickpermission() {
        return kickpermission;
    }

    public boolean isBootflag(){
        return this.bootflag;
    }

    public boolean isSelfdefense(){
        return this.selfdefense;
    }

    public boolean isUsercheckmode(){
        return this.usercheckmode;
    }

    public boolean isPlugincheckmode() {
        return plugincheckmode;
    }
    public void setUsercheckmode(boolean usercheckmode) {
        this.usercheckmode = usercheckmode;
    }

    public void setPlugincheckmode(boolean plugincheckmode) {
        this.plugincheckmode = plugincheckmode;
    }

    public Config(IronDome me){
        me.reloadConfig();
        if(me != null){
            if(me.getConfig().contains("SelfDefence")){
                if(me.getConfig().getString("SelfDefence").equalsIgnoreCase("true")){
                    this.selfdefense = true;
                }else{
                    this.selfdefense = false;
                }
            }
            if(me.getConfig().contains("UserCheckMode")){
                if(me.getConfig().getString("UserCheckMode").equalsIgnoreCase("true")){
                    this.usercheckmode = true;
                }else{
                    this.usercheckmode = false;
                }
            }
            if(me.getConfig().contains("Rep")){
                this.loginrep = Double.parseDouble(me.getConfig().getString("Rep"));
            }
            if(me.getConfig().contains("BanCount")){
                this.bancount = Integer.valueOf(me.getConfig().getString("BanCount"));
            }
            if(me.getConfig().contains("whitelist-country")){
                this.allowcounty.addAll(me.getConfig().getStringList("whitelist-country"));
            }
            if(me.getConfig().contains("blacklist-country")){
                this.disallowcounty.addAll(me.getConfig().getStringList("blacklist-country"));
            }
            if(me.getConfig().contains("ops")){
                for (int i = 0; i < me.getConfig().getStringList("ops").size(); i++) {
                    this.ops.add(me.getConfig().getStringList("ops").get(i).toLowerCase());
                }
            }
            if(me.getConfig().contains("plugins")){
                this.plugins.addAll(me.getConfig().getStringList("plugins"));
            }
            if(me.getConfig().contains("kick-permission")){
                this.kickpermission = me.getConfig().getStringList("kick-permission");

            }
            if(me.getConfig().contains("AltCount")){
                this.altcount = Integer.valueOf(me.getConfig().getString("AltCount"));
            }
            if(me.getConfig().contains("flag")){
                if(me.getConfig().getString("flag").equalsIgnoreCase("true")){
                    this.bootflag = true;
                }else{
                    this.bootflag = false;
                }
            }
            if(me.getConfig().contains("configver")){
                this.configver = Integer.valueOf(me.getConfig().getString("configver"));
            }

        }
    }
}
