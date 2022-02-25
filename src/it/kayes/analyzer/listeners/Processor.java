package it.kayes.analyzer.listeners;

import it.kayes.analyzer.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class Processor {

    public static final HashMap<String, Integer> minutesOnline = new HashMap<>(); // Player:Amount Minutes
    public static final HashMap<String, Integer> onlinePlayers = new HashMap<>(); // Date:Amount Players (+Staff)
    public static final HashMap<String, Integer> onlineStaffPlayers = new HashMap<>(); // Date: Amount Staff
    public static final ArrayList<String> notOnlineStaff = new ArrayList<>(); // Date
    public static ArrayList<String> playersServer = new ArrayList<>(); // Player String
    public static int newPlayers = 0;

    public Processor() {
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
                        Main.getSaver().save(getDay(c2));
                    } catch (IOException e) {e.printStackTrace();}
                }

                boolean staff = false;
                int amountStaff = 0;
                String date = getDate(c);

                for (Player p : getOnlinePlayers()) {
                    String name = p.getName();

                    if (!playersServer.contains(name)) {
                        playersServer.add(name);
                        newPlayers++;
                    }

                    if (minutesOnline.containsKey(name))
                        minutesOnline.replace(name, minutesOnline.get(name)+1);
                    else minutesOnline.put(name, 1);

                    if (p.hasPermission("analyzer.staff")) {
                        if (!staff)
                            staff = true;
                        amountStaff++;
                    }
                }

                if (!staff && !notOnlineStaff.contains(date))
                    notOnlineStaff.add(date);

                onlinePlayers.put(date, getOnlinePlayers().size());
                onlineStaffPlayers.put(date, amountStaff);

            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 20L, 20L);
    }


    public String getDate(Calendar c) {
        return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
    }

    public String getDay(Calendar c) {
        return c.get(Calendar.DAY_OF_MONTH)+"."+(c.get(Calendar.MONTH)+1)+"."+c.get(Calendar.YEAR);
    }


}
