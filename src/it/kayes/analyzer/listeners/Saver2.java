package it.kayes.analyzer.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Saver2 {

    private static final File filePlayers = new File("plugins/Analyzer" + File.separator + "players.yml");
    private static final FileConfiguration cfgPlayers = YamlConfiguration.loadConfiguration(filePlayers);

    public Saver2() {
        new File("plugins/Analyzer").mkdir();
        new File("plugins/Analyzer/logs").mkdir();
        if (!filePlayers.exists())
            try {
                filePlayers.createNewFile();
            } catch (IOException ignored) {}
        else
            Processor2.playersServer = (ArrayList<String>) cfgPlayers.getStringList("players");
    }

    public void save(Calendar c) throws IOException, ParseException {
        File log = new File("plugins/Analyzer/logs" + File.separator + Processor2.getDay(c) + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(log);
        File logjs = new File(cfgPlayers.getString("pathSave") + File.separator + Processor2.getDay(c) + ".json");
        FileWriter cfgjs = new FileWriter(logjs);
        if (!log.exists())
            log.createNewFile();
        if (!logjs.exists())
            logjs.createNewFile();

        if (Processor2.minutesOnline.size() > 0)
            for (String s : Processor2.minutesOnline)
                cfg.set("onlineMinutes." + s, cfg.getInt("onlineMinutes." + s) + 1);

        cfg.set("date_players." + Processor2.getDate(c), Processor2.onlinePlayers);
        cfg.set("date_staff." + Processor2.getDate(c), Processor2.onlineStaffPlayers);

        if (!Processor2.onlineStaff) {
            ArrayList<String> list = (ArrayList<String>) cfg.getStringList("date_no_staff");
            list.add(Processor2.getDate(c));
            cfg.set("date_no_staff", list);
        }

        if (Processor2.newPlayers>0)
            cfg.set("newPlayers", cfg.getInt("newPlayers") + Processor2.newPlayers);

        HashMap<String, Integer> minutesOnline = new HashMap<>();
        HashMap<String, Integer> onlinePlayers = new HashMap<>();
        HashMap<String, Integer> onlineStaff = new HashMap<>();
        ArrayList<String> nostaff = new ArrayList<>();

        if (cfg.getString("onlineMinutes") != null) {
            for (String s : cfg.getConfigurationSection("onlineMinutes").getKeys(false))
                minutesOnline.put(s, cfg.getInt("onlineMinutes." + s));
        }
        else if (cfg.getString("date_players") != null) {
            for (String s : cfg.getConfigurationSection("date_players").getKeys(false))
                onlinePlayers.put(s, cfg.getInt("date_players." + s));
        }
        else if (cfg.getString("date_staff") != null) {
            for (String s : cfg.getConfigurationSection("date_staff").getKeys(false))
                onlineStaff.put(s, cfg.getInt("date_staff." + s));
        }
        else if (cfg.getString("date_no_staff")!=null) {
            nostaff.addAll(cfg.getStringList("date_no_staff"));
        }

        JSONObject dataJS = new JSONObject();
        JSONObject onlineMinutesListJS = new JSONObject();
        for (String s : minutesOnline.keySet())
            onlineMinutesListJS.put(s, minutesOnline.get(s));
        dataJS.put("onlineMinutes", onlineMinutesListJS);
        JSONObject datePlayersListJS = new JSONObject();
        for (String s : onlinePlayers.keySet())
            datePlayersListJS.put(s, onlinePlayers.get(s));
        dataJS.put("datePlayers", datePlayersListJS);
        JSONObject dateStaffListJS = new JSONObject();
        for (String s : onlineStaff.keySet())
            dateStaffListJS.put(s, onlineStaff.get(s));
        dataJS.put("dateStaff", dateStaffListJS);
        JSONArray dateNoStaffJS = new JSONArray();
        dateNoStaffJS.addAll(nostaff);
        dataJS.put("noStaff", dateNoStaffJS);
        dataJS.put("newPlayers", cfg.getInt("newPlayers"));

        cfgjs.write(dataJS.toJSONString());
        cfgjs.flush();

        cfg.save(log);

        cfgPlayers.set("players", Processor2.playersServer);
        cfgPlayers.save(filePlayers);

        Processor2.minutesOnline.clear();
        Processor2.onlineStaff = false;
        Processor2.onlineStaffPlayers = 0;
        Processor2.onlinePlayers = 0;
        Processor2.newPlayers = 0;
    }


}
