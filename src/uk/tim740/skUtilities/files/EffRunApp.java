package uk.tim740.skUtilities.files;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import uk.tim740.skUtilities.Utils;
import uk.tim740.skUtilities.files.event.EvtRunApp;
import uk.tim740.skUtilities.skUtilities;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by tim740
 */
public class EffRunApp extends Effect {
    private Expression<String> path;

    @Override
    protected void execute(Event e) {
        File pth = new File(Utils.getDefaultPath(path.getSingle(e)));
        if (Desktop.isDesktopSupported()) {
            try {
                EvtRunApp era = new EvtRunApp(pth);
                Bukkit.getServer().getPluginManager().callEvent(era);
                if (!era.isCancelled()) {
                    if (!pth.exists()) {
                        throw new IOException();
                    } else {
                        Desktop.getDesktop().open(pth);
                    }
                }
            } catch (IOException x) {
                skUtilities.prSysE("'" + pth + "' isn't a valid path!", getClass().getSimpleName(), x);
            }
        } else {
            String p = System.getProperty("os.name").toLowerCase();
            Boolean gc = false;
            if (p.contains("solaris")) gc = true;
            if (p.contains("sunos")) gc = true;
            if (p.contains("linux")) gc = true;
            if (p.contains("unix")) gc = true;
            if (gc.equals(true)) {
                skUtilities.prSysI("Looks like your using a linux based system and don't have");
                skUtilities.prSysI("libgnome installed execute the command below in the terminal ");
                skUtilities.prSysI("'sudo apt-get install libgnome2-0'");
                skUtilities.prSysI("and then restart the system!");
            } else {
                skUtilities.prSysE("Sorry this OS ('" + p + "') isn't supported!", getClass().getSimpleName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
        path = (Expression<String>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return getClass().getName();
    }
}