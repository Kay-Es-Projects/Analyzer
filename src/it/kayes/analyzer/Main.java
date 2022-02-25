package it.kayes.analyzer;

import it.kayes.analyzer.listeners.Processor;
import it.kayes.analyzer.listeners.Saver;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Saver saver;
    private static Processor processor;

    public void onEnable() {
        instance = this;

        processor = new Processor();
        processor.process();

        saver = new Saver();
    }

    public void onDisable() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Rome")));

        try {
            Main.getSaver().save(processor.getDay(c));
        } catch (IOException e) {e.printStackTrace();}
    }

    public static Main getInstance() {
        return instance;
    }

    public static Saver getSaver() {
        return saver;
    }
}
