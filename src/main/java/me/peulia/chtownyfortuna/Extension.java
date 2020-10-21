package me.peulia.chtownyfortuna;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import com.palmergames.bukkit.towny.Towny;
import org.bukkit.plugin.Plugin;

@MSExtension("CHTownyForTuna")
public class Extension extends AbstractExtension {
    public static int VERSION_MAJOR = 1;

    public static int VERSION_MINOR = 1;

    public static int VERSION_EXTRA = 0;

    @Override
    public Version getVersion() { return (Version)new SimpleVersion(VERSION_MAJOR, VERSION_MINOR, VERSION_EXTRA); }

    @Override
    public void onStartup() {
        System.out.println("CHTowny starting...");
        CommandHelperPlugin chp = CommandHelperPlugin.self;
        try {
            Static.checkPlugin("Towny", Target.UNKNOWN);
            Plugin pl = chp.getServer().getPluginManager().getPlugin("Towny");
            if (!(pl instanceof Towny)) {

                throw new CREInvalidPluginException("", Target.UNKNOWN);
            }
            System.out.println("Towny " + getVersion() + " loaded.");
        } catch (ConfigRuntimeException cre) {
            Static.getLogger().warning("Towny not found, cannot function!");
        }
    }
}
