package net.swzo.tt20neoforged;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.swzo.tt20neoforged.command.MainCommand;
import net.swzo.tt20neoforged.config.BlockEntityMaskConfig;
import net.swzo.tt20neoforged.config.MainConfig;
import net.swzo.tt20neoforged.util.TPSCalculator;
import org.slf4j.Logger;

@Mod(Tt20NeoForged.MODID)
public class Tt20NeoForged {
    public static final String MODID = "tt20neoforged";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static TPSCalculator TPS_CALCULATOR;

    public Tt20NeoForged(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        TPS_CALCULATOR = new TPSCalculator();
        modContainer.registerConfig(ModConfig.Type.COMMON, MainConfig.SPEC, "tt20/config.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, BlockEntityMaskConfig.SPEC, "tt20/block_entity_mask.toml");
        modEventBus.addListener(this::onConfigLoad);
        modEventBus.addListener(this::onConfigReload);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MainCommand.register(event.getDispatcher());
    }

    private void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == BlockEntityMaskConfig.SPEC)
            BlockEntityMaskConfig.INSTANCE.reload();
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == BlockEntityMaskConfig.SPEC)
            BlockEntityMaskConfig.INSTANCE.reload();
    }
}