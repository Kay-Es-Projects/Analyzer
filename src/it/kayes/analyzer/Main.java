package it.kayes.analyzer;

import it.kayes.analyzer.listeners.Processor2;
import it.kayes.analyzer.listeners.Saver2;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Saver2 saver;
    private static Processor2 processor;

    public void onEnable() {
        instance = this;

        processor = new Processor2();
        processor.process();

        saver = new Saver2();
    }

    public static Main getInstance() {
        return instance;
    }

    public static Saver2 getSaver() {
        return saver;
    }
}
