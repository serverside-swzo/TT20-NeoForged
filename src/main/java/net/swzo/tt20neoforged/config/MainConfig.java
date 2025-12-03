package net.swzo.tt20neoforged.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MainConfig {
    public static final MainConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        final Pair<MainConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(MainConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    private final ModConfigSpec.BooleanValue enabled;
    private final ModConfigSpec.BooleanValue blockEntityAcceleration;
    private final ModConfigSpec.BooleanValue blockBreakingAcceleration;
    private final ModConfigSpec.BooleanValue potionEffectAcceleration;
    private final ModConfigSpec.BooleanValue fluidAcceleration;
    private final ModConfigSpec.BooleanValue pickupAcceleration;
    private final ModConfigSpec.BooleanValue eatingAcceleration;
    private final ModConfigSpec.BooleanValue portalAcceleration;
    private final ModConfigSpec.BooleanValue sleepingAcceleration;
    private final ModConfigSpec.BooleanValue serverWatchdog;
    private final ModConfigSpec.BooleanValue singleplayerWarning;
    private final ModConfigSpec.BooleanValue timeAcceleration;
    private final ModConfigSpec.BooleanValue bowAcceleration;
    private final ModConfigSpec.BooleanValue crossbowAcceleration;
    private final ModConfigSpec.BooleanValue randomTickSpeedAcceleration;
    private final ModConfigSpec.BooleanValue automaticUpdater;
    private final ModConfigSpec.IntValue tickRepeatCap;

    public MainConfig(ModConfigSpec.Builder builder) {
        builder.comment("Main Configuration for TT20").push("general");

        enabled = builder
                .comment("Enables or disables the mod. (Default: true)")
                .define("enabled", true);

        blockEntityAcceleration = builder
                .comment("Accelerates block entities. (Default: false)")
                .define("block-entity-acceleration", false);

        blockBreakingAcceleration = builder
                .comment("Accelerates block breaking. (Default: true)")
                .define("block-breaking-acceleration", true);

        potionEffectAcceleration = builder
                .comment("Accelerates potion effects. (Default: true)")
                .define("potion-effect-acceleration", true);

        fluidAcceleration = builder
                .comment("Accelerates fluid flow. (Default: true)")
                .define("fluid-acceleration", true);

        pickupAcceleration = builder
                .comment("Accelerates item pickup. (Default: true)")
                .define("pickup-acceleration", true);

        eatingAcceleration = builder
                .comment("Accelerates eating and drinking. (Default: true)")
                .define("eating-acceleration", true);

        portalAcceleration = builder
                .comment("Accelerates portal travel. (Default: true)")
                .define("portal-acceleration", true);

        sleepingAcceleration = builder
                .comment("Accelerates sleeping. (Default: true)")
                .define("sleeping-acceleration", true);

        serverWatchdog = builder
                .comment("Enables the server watchdog. (Default: true)")
                .define("server-watchdog", true);

        singleplayerWarning = builder
                .comment("Shows a warning in singleplayer. (Default: true)")
                .define("singleplayer-warning", true);

        timeAcceleration = builder
                .comment("Accelerates the passage of time. (Default: true)")
                .define("time-acceleration", true);

        bowAcceleration = builder
                .comment("Accelerates bow drawing. (Default: true)")
                .define("bow-acceleration", true);

        crossbowAcceleration = builder
                .comment("Accelerates crossbow loading. (Default: true)")
                .define("crossbow-acceleration", true);

        randomTickSpeedAcceleration = builder
                .comment("Accelerates random block ticks. (Default: true)")
                .define("random-tickspeed-acceleration", true);

        automaticUpdater = builder
                .comment("Enables the automatic updater. (Default: true)")
                .define("automatic-updater", true);

        tickRepeatCap = builder
                .comment("The maximum number of times a tick can be repeated. (Default: 10)")
                .defineInRange("tick-repeat-cap", 10, 1, 1000);

        builder.pop();
    }

    public void reload() {

    }

    public int getTickRepeatCap() {
        return this.tickRepeatCap.get();
    }

    public void setTickRepeatCap(int cap) {
        this.tickRepeatCap.set(cap);
    }

    public void enabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public boolean enabled() {
        return this.enabled.get();
    }

    public void serverWatchdog(boolean enabled) {
        this.serverWatchdog.set(enabled);
    }

    public boolean serverWatchdog() {
        return this.serverWatchdog.get();
    }

    public void blockEntityAcceleration(boolean enabled) {
        this.blockEntityAcceleration.set(enabled);
    }

    public boolean blockEntityAcceleration() {
        return this.blockEntityAcceleration.get();
    }

    public void blockBreakingAcceleration(boolean enabled) {
        this.blockBreakingAcceleration.set(enabled);
    }

    public boolean blockBreakingAcceleration() {
        return this.blockBreakingAcceleration.get();
    }

    public void potionEffectAcceleration(boolean enabled) {
        this.potionEffectAcceleration.set(enabled);
    }

    public boolean potionEffectAcceleration() {
        return this.potionEffectAcceleration.get();
    }

    public void fluidAcceleration(boolean enabled) {
        this.fluidAcceleration.set(enabled);
    }

    public boolean fluidAcceleration() {
        return this.fluidAcceleration.get();
    }

    public void pickupAcceleration(boolean enabled) {
        this.pickupAcceleration.set(enabled);
    }

    public boolean pickupAcceleration() {
        return this.pickupAcceleration.get();
    }

    public void eatingAcceleration(boolean enabled) {
        this.eatingAcceleration.set(enabled);
    }

    public boolean eatingAcceleration() {
        return this.eatingAcceleration.get();
    }

    public void portalAcceleration(boolean enabled) {
        this.portalAcceleration.set(enabled);
    }

    public boolean portalAcceleration() {
        return this.portalAcceleration.get();
    }

    public void sleepingAcceleration(boolean enabled) {
        this.sleepingAcceleration.set(enabled);
    }

    public boolean sleepingAcceleration() {
        return this.sleepingAcceleration.get();
    }

    public void automaticUpdater(boolean enabled) {
        this.automaticUpdater.set(enabled);
    }

    public boolean automaticUpdater() {
        return this.automaticUpdater.get();
    }

    public void singlePlayerWarning(boolean enabled) {
        this.singleplayerWarning.set(enabled);
    }

    public boolean singlePlayerWarning() {
        return this.singleplayerWarning.get();
    }

    public void timeAcceleration(boolean enabled) {
        this.timeAcceleration.set(enabled);
    }

    public boolean timeAcceleration() {
        return this.timeAcceleration.get();
    }

    public void bowAcceleration(boolean enabled) {
        this.bowAcceleration.set(enabled);
    }

    public boolean bowAcceleration() {
        return this.bowAcceleration.get();
    }

    public void crossbowAcceleration(boolean enabled) {
        this.crossbowAcceleration.set(enabled);
    }

    public boolean crossbowAcceleration() {
        return this.crossbowAcceleration.get();
    }

    public void randomTickSpeedAcceleration(boolean enabled) {
        this.randomTickSpeedAcceleration.set(enabled);
    }

    public boolean randomTickSpeedAcceleration() {
        return this.randomTickSpeedAcceleration.get();
    }

    public boolean getBoolean(String key) {

        return switch (key) {
            case "enabled" -> enabled();
            case "block-entity-acceleration" -> blockEntityAcceleration();
            case "block-breaking-acceleration" -> blockBreakingAcceleration();

            default -> false;
        };
    }
}