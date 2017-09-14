package ru.plsecuritylab.irondome.DataManager;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.plsecuritylab.irondome.Network.HTTPConnect;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by msfblue1 on 2017/06/05.
 */
public class User extends HTTPConnect{

    private String name = "";
    private String uuid = "";
    private Boolean exist = false;
    private Boolean OP = false;
    private Boolean Online = false;
    private Boolean Banned = false;
    private Boolean Whitelisted = false;
    private Boolean PlayedBefore = false;
    private InetAddress Address = null;
    private Location BedSpawnLocation = null;
    private Boolean dead = false;
    private Boolean Allowflight = false;
    private Boolean fly = false;
    private Float Exp = 0F;
    private Long FirstPlayed = 0L;
    private Long LastPlayed = 0L;
    private Integer McbansCount = 0;
    private Double McbansReputation = 0.0;
    private String[] McbansHistory = new String[]{};
    private String JMPBanReason = "NONE";
    private Location Location = null;
    private GameMode gameMode = null;
    private Boolean Localhost = true;
    private String IPAddress = "";
    private String JoinCountry = "NONE";
    private String JoinCountryCODE = "NONE";
    private String JoinRegion = "NONE";
    private String JoinCity = "NONE";

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public Boolean exist() {
        return exist;
    }

    public Boolean isOp() {
        return OP;
    }

    public Boolean isOnline() {
        return Online;
    }

    public Boolean isBanned() {
        return Banned;
    }

    public Boolean isWhitelisted() {
        return Whitelisted;
    }

    public Boolean hasPlayedBefore() {
        return PlayedBefore;
    }

    public InetAddress getAddress() {
        return Address;
    }

    public String getIPAddress(){
        return IPAddress;
    }

    public Location getBedSpawnLocation() {
        return BedSpawnLocation;
    }

    public Boolean isDead() {
        return dead;
    }

    public Boolean isAllowflight() {
        return Allowflight;
    }

    public Boolean isFly() {
        return fly;
    }

    public Float getExp() {
        return Exp;
    }

    public Long getFirstPlayed() {
        return FirstPlayed;
    }

    public Long getLastPlayed() {
        return LastPlayed;
    }

    public Integer getMcbansCount() {
        return McbansCount;
    }

    public Double getMcbansReputation() {
        return McbansReputation;
    }

    public String[] getMcbansHistory() {
        return McbansHistory;
    }

    public String getJMPBanReason() {
        return JMPBanReason;
    }

    public org.bukkit.Location getLocation() {
        return Location;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getJoinCountry(){
        return JoinCountry;
    }

    public String getJoinCountryCODE() {
        return JoinCountryCODE;
    }

    public String getJoinRegion(){
        return JoinRegion;
    }

    public String getJoinCity() {
        return JoinCity;
    }

    public Boolean isLocalHost() {
        return Localhost;
    }

    public User(String TargetName , InetAddress inetAddress){
        JSONObject userdata = getJSONData("https://api-irondome.herokuapp.com/getdata?username="+TargetName);
        if(userdata.isEmpty()){
            this.exist = false;
            return;
        }else {
            this.exist = true;
        }
        if(userdata.containsKey("player")){
            OfflinePlayer player = PermData.getOfflinePlayer(TargetName);
            this.PlayedBefore = player.hasPlayedBefore();
            this.OP = player.isOp();
            this.Whitelisted = player.isWhitelisted();
            this.Banned = player.isBanned();
            this.FirstPlayed = player.getFirstPlayed();
            this.LastPlayed = player.getLastPlayed();
            this.Address = inetAddress;
            if(player.isOnline()){
                this.Online = player.isOnline();
                Player onlinePlayer = player.getPlayer();
                this.Address = player.getPlayer().getAddress().getAddress();
                this.dead = onlinePlayer.isDead();
                this.fly = onlinePlayer.isFlying();
                this.Allowflight = onlinePlayer.getAllowFlight();
                this.Location = onlinePlayer.getLocation();
                this.gameMode = onlinePlayer.getGameMode();
            }
            if(this.Address != null) {
                if (this.Address.getHostAddress().startsWith("192.168") || this.Address.getHostAddress().equalsIgnoreCase("127.0.0.1")) {
                    this.Localhost = true;
                } else {
                    this.Localhost = false;
                    this.IPAddress = this.Address.getHostAddress();
                    ru.plsecuritylab.irondome.Network.HTTPConnect connect = new ru.plsecuritylab.irondome.Network.HTTPConnect();
                    JSONObject address = connect.getJSONData("http://freegeoip.net/json/" + this.Address.getHostAddress());
                    if (address.containsKey("country_name")) {
                        this.JoinCountry = String.valueOf(address.get("country_name"));
                        this.JoinCountryCODE = String.valueOf(address.get("country_code"));
                        this.JoinRegion = String.valueOf(address.get("region_name"));
                        this.JoinCity = String.valueOf(address.get("city"));
                    }
                }
            }
            //this.exist = true;
            this.name = String.valueOf(userdata.get("player"));
            if(userdata.containsKey("uuid")){
                this.uuid = String.valueOf(userdata.get("uuid"));
            }else{
                this.uuid = "NONE";
            }
            if(userdata.containsKey("total")){
                this.McbansCount = Integer.parseInt(userdata.get("total").toString());
            }
            if(userdata.containsKey("reputation")){
                this.McbansReputation = Double.valueOf(userdata.get("reputation").toString());
            }
            if(userdata.containsKey("global")){
                JSONArray history = (JSONArray) userdata.get("global");
                ArrayList<String> returndata = new ArrayList<>();
                if(!history.isEmpty()){
                    for (int i = 0; i < history.size(); i++) {
                        returndata.add(String.valueOf(history.get(i)));
                    }
                }
                this.McbansHistory = returndata.toArray(new String[returndata.size()]);

            }
            if(userdata.containsKey("jmp")){
                this.JMPBanReason = String.valueOf(userdata.get("jmp"));
            }

        }

    }



}
