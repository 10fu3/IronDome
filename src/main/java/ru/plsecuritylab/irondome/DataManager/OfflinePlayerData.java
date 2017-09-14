package ru.plsecuritylab.irondome.DataManager;

/**
 * Created by msfblue1 on 2017/07/15.
 */
public class OfflinePlayerData {

    public Boolean isEmpty(){
        if(getUUID().equalsIgnoreCase("")){
            return true;
        }else{
            return false;
        }
    }

    public String getBanDate(){
        return this.BanDate;
    }

    public void setBanDate(String date){
        if(date == null){
            this.BanDate= "";
        }else{
            this.BanDate= date;
        }
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void reset(){
        this.IP = "";
    }

    private String BanDate = "";
    private String UUID = "";
    private String IP = "";
    private String Name = "";

}
