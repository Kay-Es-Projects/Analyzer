package it.kayes.analyzer.listeners;

import it.kayes.analyzer.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class Processor2 {

    public static final ArrayList<String> minutesOnline = new ArrayList<>(); // Players
    public static ArrayList<String> playersServer = new ArrayList<>(); // New Player
    public static final ArrayList<String> newPlayersServer = new ArrayList<>(); // New Player
    public static int onlinePlayers = 0; // ++
    public static int onlineStaffPlayers = 0; // ++
    public static boolean onlineStaff = false; // !?
    public static int newPlayers = 0; //++

    public Processor2() {
        process();
    }

    public void process() {
        new BukkitRunnable() {
            byte seconds = 0;
            @Override
            public void run() {
                if (seconds<60) {
                    seconds++;
                    return;
                } else
                    seconds = 0;

                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Rome")));
                if (c.get(Calendar.HOUR_OF_DAY)==0 && c.get(Calendar.MINUTE)==0) {
                    Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Rome")));
                    c2.add(Calendar.DATE, -1);

                    try {
                        Main.getSaver().save(c2);
                    } catch (IOException | ParseException e) {e.printStackTrace();}
                } else {
                    try {
                        Main.getSaver().save(c);
                    } catch (IOException | ParseException e) {e.printStackTrace();}
                }

                String date = getDate(c);

                for (Player p : getOnlinePlayers()) {
                    String name = p.getName();

                    if (!playersServer.contains(name)) {
                        playersServer.add(name);
                        newPlayersServer.add(name);
                        newPlayers++;
                    }
                    minutesOnline.add(name);

                    if (p.hasPermission("analyzer.staff")) {
                        if (!onlineStaff)
                            onlineStaff = true;
                        onlineStaffPlayers++;
                    }
                    onlinePlayers++;
                }

            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 20L, 20L);
    }


    public static String getDate(Calendar c) {
        return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
    }

    public static String getDay(Calendar c) {
        return c.get(Calendar.DAY_OF_MONTH)+"."+(c.get(Calendar.MONTH)+1)+"."+c.get(Calendar.YEAR);
    }



}
