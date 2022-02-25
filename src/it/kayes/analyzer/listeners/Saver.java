package it.kayes.analyzer.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Saver {

    private static final File filePlayers = new File("plugins/Analyzer" + File.separator + "players.yml");
    private static final FileConfiguration cfgPlayers = YamlConfiguration.loadConfiguration(filePlayers);

    public Saver() {
        new File("plugins/Analyzer").mkdir();
        new File("plugins/Analyzer/logs").mkdir();
        if (!filePlayers.exists())
            try {
                filePlayers.createNewFile();
            } catch (IOException ignored) {}
        else
            Processor.playersServer = (ArrayList<String>) cfgPlayers.getStringList("players");
    }

    public void save(String date) throws IOException {
        File log = new File("plugins/Analyzer/logs" + File.separator + date+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(log);
        if (!log.exists())
            log.createNewFile();

        HashMap<String, Integer> onlinePlayers = new HashMap<>();
        HashMap<String, Integer> onlineStaff = new HashMap<>();
        ArrayList<String> nostaff = new ArrayList<>();
        Processor.newPlayers += cfg.getInt("new_players");

        if (cfg.getString("onlineMinutes")!=null)
        for (String s : cfg.getConfigurationSection("onlineMinutes").getKeys(false))
            Processor.minutesOnline.replace(s, cfg.getInt("onlineMinutes." + s) + Processor.minutesOnline.get(s));

        if (cfg.getString("date_players")!=null)
            for (String s : cfg.getConfigurationSection("date_players").getKeys(false))
                onlinePlayers.put(s, cfg.getInt("date_players."+s));
        onlinePlayers.putAll(Processor.onlinePlayers);

        if (cfg.getString("date_staff")!=null)
            for (String s : cfg.getConfigurationSection("date_staff").getKeys(false))
                onlineStaff.put(s, cfg.getInt("date_staff."+s));
        onlineStaff.putAll(Processor.onlineStaffPlayers);

        if (cfg.getString("date_no_staff")!=null)
            nostaff.addAll(cfg.getStringList("date_no_staff"));
        nostaff.addAll(Processor.notOnlineStaff);


        cfg.set("new_players", Processor.newPlayers);
        for (String s : Processor.minutesOnline.keySet())
            cfg.set("onlineMinutes."+s, Processor.minutesOnline.get(s));
        for (String s : onlinePlayers.keySet())
            cfg.set("date_players."+s, onlinePlayers.get(s));
        for (String s : onlineStaff.keySet())
            cfg.set("date_staff."+s, onlineStaff.get(s));
        cfg.set("date_no_staff", nostaff);
        cfg.save(log);

        cfgPlayers.set("players", Processor.playersServer);
        cfgPlayers.save(filePlayers);

        Processor.minutesOnline.clear();
        Processor.notOnlineStaff.clear();
        Processor.onlineStaffPlayers.clear();
        Processor.onlinePlayers.clear();
        Processor.newPlayers = 0;
    }


}
