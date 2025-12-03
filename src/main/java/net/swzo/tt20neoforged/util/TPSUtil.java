package net.swzo.tt20neoforged.util;

import net.swzo.tt20neoforged.Tt20NeoForged;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TPSUtil {

    private static final DecimalFormat df = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.ROOT));
    private static final DecimalFormat dfMissedTicks = new DecimalFormat("###", new DecimalFormatSymbols(Locale.ROOT));

    public static String colorizeTPS(double tps, boolean format) {
        if (tps > 15.0) {
            String val = format ? formatTPS(tps) : String.valueOf(tps);
            return "§a" + val;
        } else if (tps > 10.0) {
            String val = format ? formatTPS(tps) : String.valueOf(tps);
            return "§e" + val;
        } else {
            String val = format ? formatTPS(tps) : String.valueOf(tps);
            return "§c" + val;
        }
    }

    public static String formatTPS(double tps) {
        return df.format(tps);
    }

    public static String formatMissedTicks(double missedTicks) {
        return dfMissedTicks.format(missedTicks);
    }

    public static float tt20(float ticks, boolean limitZero) {
        float newTicks = (float)rawTT20((double)ticks);
        if (limitZero) {
            return newTicks > 0.0F ? newTicks : 1.0F;
        } else {
            return newTicks;
        }
    }

    public static int tt20(int ticks, boolean limitZero) {
        int newTicks = (int)Math.ceil(rawTT20((double)ticks));
        if (limitZero) {
            return newTicks > 0 ? newTicks : 1;
        } else {
            return newTicks;
        }
    }

    public static double tt20(double ticks, boolean limitZero) {
        double newTicks = rawTT20(ticks);
        if (limitZero) {
            return newTicks > 0.0 ? newTicks : 1.0;
        } else {
            return newTicks;
        }
    }

    public static double rawTT20(double ticks) {
        if (ticks == 0.0) return 0.0;

        if (Tt20NeoForged.TPS_CALCULATOR == null) return ticks;

        double tps = Tt20NeoForged.TPS_CALCULATOR.getMostAccurateTPS();

        if (tps <= 0.0) return ticks; 

        double multiplier = 20.0 / tps;
        return ticks * multiplier;
    }
}