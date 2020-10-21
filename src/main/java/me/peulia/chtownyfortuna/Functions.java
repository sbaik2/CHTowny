package me.peulia.chtownyfortuna;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREInvalidWorldException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;
import com.laytonsmith.core.exceptions.CRE.CRENotFoundException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Iterator;
import java.util.List;

public class Functions {
    public static String docs() {
        return "Returns Towny Name in string";
    }

    @api
    public static class getResidents extends AbstractFunction {
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRENotFoundException.class, CREIllegalArgumentException.class};
        }

        public boolean isRestricted() {
            return false;
        }

        public Boolean runAsync() {
            return false;
        }

        public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
            Static.checkPlugin("Towny", t);
            if (!(args[0] instanceof CString)) {
                throw new CREIllegalArgumentException("Give me string bitch.", t);
            } else if (!TownyAPI.getInstance().getDataSource().hasTown(args[0].val())) {
                return CNull.NULL;
            } else {
                CArray arr = new CArray(t);

                try {
                    List<Resident> residents = TownyAPI.getInstance().getDataSource().getTown(args[0].val()).getResidents();
                    if (residents != null && residents.size() > 0) {
                        Iterator var6 = residents.iterator();

                        while(var6.hasNext()) {
                            Resident resident = (Resident)var6.next();
                            arr.push(new CString(resident.getName(), t), t);
                        }
                    }
                } catch (NotRegisteredException var8) {
                }

                return arr;
            }
        }

        public Version since() {
            return new SimpleVersion(1, 0, 0);
        }

        public String getName() {
            return "towny_get_residents";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "array {townname} Returns the name of residents of the town. If town doesn't exist, an empty array is returned.";
        }
    }

    @api
    public static class getPlayerTownName extends AbstractFunction {
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRENotFoundException.class, CREIllegalArgumentException.class, NotRegisteredException.class};
        }

        public boolean isRestricted() {
            return false;
        }

        public Boolean runAsync() {
            return false;
        }

        public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
            Static.checkPlugin("Towny", t);
            if (!(args[0] instanceof CString)) {
                throw new CREIllegalArgumentException("Give me string bitch.", t);
            } else if (!TownyAPI.getInstance().getDataSource().hasResident(args[0].val())) {
                return CNull.NULL;
            } else {
                String name = null;

                try {
                    name = TownyAPI.getInstance().getDataSource().getResident(args[0].val()).getTown().getName();
                } catch (NotRegisteredException var6) {
                }

                return (Mixed)(name == null ? CNull.NULL : new CString(name, t));
            }
        }

        public Version since() {
            return new SimpleVersion(1, 0, 0);
        }

        public String getName() {
                return "towny_get_player_town_name";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "string {playername} Returns the name of the town player is part of. If the player isn't part of any towns, null will be passed down.";
        }
    }


    @api
    public static class getTownAtLocation extends AbstractFunction {
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRENotFoundException.class, CREIllegalArgumentException.class, CREInvalidWorldException.class};
        }

        public boolean isRestricted() {
            return false;
        }

        public Boolean runAsync() {
            return false;
        }

        public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
            Static.checkPlugin("Towny", t);

            MCLocation mcloc = ObjectGenerator.GetGenerator().location(args[0], null, t);
            if(mcloc == null){
                return CNull.NULL;
            }
            Location loc = new Location(Bukkit.getWorld(mcloc.getWorld().getName()), mcloc.getBlockX(), mcloc.getBlockY(), mcloc.getBlockZ());

            try {
                Town town = WorldCoord.parseWorldCoord(loc).getTownBlock().getTown();
                return new CString(town.getName(), t);
            } catch (NotRegisteredException var6) {
                return CNull.NULL;
            }

        }

        public Version since() {
            return new SimpleVersion(1, 1, 0);
        }

        public String getName() {
            return "towny_get_town_at_loc";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "string {townname} Returns the name of the town in a specific location. If the location isn't part of a town, null will be passed down.";
        }
    }
}