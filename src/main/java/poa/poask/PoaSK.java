package poa.poask;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import poa.poask.expressions.ExprHostname;
import poa.poask.util.packetListener.Login;

import java.io.File;
import java.io.IOException;

public final class PoaSK extends JavaPlugin {

    private static PoaSK INSTANCE;

    @SuppressWarnings({"CallToPrintStackTrace", "DataFlowIssue"})
    @Override
    public void onEnable() {
        INSTANCE = this;
        SkriptAddon skriptAddon = Skript.registerAddon(this);

        Types.call();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ExprHostname(), this);
        pm.registerEvents(new Login(), this);

        try {
            File config = new File(getDataFolder(), "config.yml");

            if (config.length() == 0)
                config.delete();

        } catch (Exception ignored) {
        }
        saveDefaultConfig();

        try {
            skriptAddon.loadClasses("poa.poask", "expressions", "effects", "events", "effects.entity", "effects.packets");
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("poasktest").setExecutor(new Test());
    }

    public static PoaSK getInstance() {
        return INSTANCE;
    }

}
