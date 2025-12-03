package net.swzo.tt20neoforged.util;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TPSCalculator {
    public Long lastTick;
    public Long currentTick;
    private double allMissedTicks = 0.0;
    private final List<Double> tpsHistory = new CopyOnWriteArrayList<>();
    public static final int MAX_TPS = 20;
    public static final int FULL_TICK = 50;
    private double smoothedMSPT = 50.0;

    public TPSCalculator() {

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Pre event) {

        if (this.currentTick != null) {
            this.lastTick = this.currentTick;
        }

        this.currentTick = System.currentTimeMillis();
        long currentMSPT = this.getMSPT();

        this.smoothedMSPT = (double)currentMSPT * 0.1 + this.smoothedMSPT * 0.9;

        this.addToHistory(this.getTPS());

        this.clearMissedTicks();
        this.missedTick();
    }

    private void addToHistory(double tps) {
        if (tpsHistory.size() >= 40) {
            tpsHistory.remove(0);
        }
        tpsHistory.add(tps);
    }

    public long getMSPT() {
        return this.lastTick != null && this.currentTick != null ? this.currentTick - this.lastTick : 0L;
    }

    public double getSmoothedMSPT() {
        return this.smoothedMSPT;
    }

    public double getAverageTPS() {
        return this.tpsHistory.stream().mapToDouble(Double::doubleValue).average().orElse(20.0);
    }

    public double getTPS() {
        if (this.smoothedMSPT <= 0.0) {
            return 20.0;
        } else {
            double tps = 1000.0 / this.smoothedMSPT;
            return Math.min(tps, 20.0);
        }
    }

    public void missedTick() {
        double currentSmoothedMSPT = this.getSmoothedMSPT();
        if (currentSmoothedMSPT > 0.0) {
            double missed = currentSmoothedMSPT / 50.0 - 1.0;
            this.allMissedTicks += Math.max(0.0, missed);
        }
    }

    public double getMostAccurateTPS() {
        return Math.min(this.getTPS(), this.getAverageTPS());
    }

    public double getAllMissedTicks() {
        return this.allMissedTicks;
    }

    public int applicableMissedTicks() {
        return (int) Math.floor(this.allMissedTicks);
    }

    public void clearMissedTicks() {
        this.allMissedTicks -= (double) this.applicableMissedTicks();
    }

    public void resetMissedTicks() {
        this.allMissedTicks = 0.0;
    }
}