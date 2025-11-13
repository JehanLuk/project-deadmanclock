package com.deadmanwatch;

import com.deadmanwatch.client.gui.ClockOverlayRenderer;
import com.deadmanwatch.client.gui.ConfigScreen;
import com.deadmanwatch.config.DeadmanWatchConfig;
import com.deadmanwatch.registry.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings({"removal"})
@Mod(DeadmanWatch.MODID)
public class DeadmanWatch {
    public static final String MODID = "deadmanwatch";

    public DeadmanWatch() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DeadmanWatchConfig.CLIENT_SPEC);

        ModSounds.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(ClockOverlayRenderer.class);

        ConfigScreen.register();
    }
}
