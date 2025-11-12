package com.deadmanwatch.registry;

import com.deadmanwatch.DeadmanWatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

@SuppressWarnings({ "removal" })
public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DeadmanWatch.MODID);

    public static final RegistryObject<SoundEvent> CLOCK_ALERT =
            SOUND_EVENTS.register("clock_alert",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(DeadmanWatch.MODID, "clock_alert")
                    ));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
