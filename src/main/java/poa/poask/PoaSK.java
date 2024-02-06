package poa.poask;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import poa.poask.expressions.Hostname;
import poa.poask.util.packetListener.Login;

import java.io.File;
import java.io.IOException;

public final class PoaSK extends JavaPlugin {

    public static SkriptAddon skript;
    public static PoaSK INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE = this;
        skript = Skript.registerAddon(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Hostname(), this);
        pm.registerEvents(new Login(), this);


        try {
            File config = new File(getDataFolder(), "config.yml");

            if (config.length() == 0)
                config.delete();

        }
        catch (Exception ignored){}
        saveDefaultConfig();

        try {
            skript.loadClasses("poa.poask", "expressions", "effects", "events", "effects.entity", "effects.packets");
        } catch (IOException e) {
            e.printStackTrace();
        }


        getCommand("poasktest").setExecutor(new Test());

    }

}
