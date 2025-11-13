package com.deadmanwatch.client.gui;

import com.deadmanwatch.config.DeadmanWatchConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

@SuppressWarnings("removal")
public class ConfigScreen {

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> createScreen(parent)
                )
        );
    }

    private static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Deadman's Watch Config"));

        ConfigCategory category = builder.getOrCreateCategory(Component.literal("Clock Settings"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        var opacityEntry = entryBuilder
                .startDoubleField(Component.literal("Clock's opacity"), DeadmanWatchConfig.CLIENT.opacity.get())
                .setDefaultValue(1.0)
                .setMin(0.0)
                .setMax(1.0)
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.opacity::set)
                .build();
        category.addEntry(opacityEntry);

        var nightMsgEntry = entryBuilder
                .startStrField(Component.literal("Night message"), DeadmanWatchConfig.CLIENT.nightMessage.get())
                .setDefaultValue("The night comes, the monsters begin to rise...")
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.nightMessage::set)
                .build();
        category.addEntry(nightMsgEntry);

        var alertEntry = entryBuilder
                .startIntField(Component.literal("Night alert time (ticks)"), DeadmanWatchConfig.CLIENT.nightAlertTime.get())
                .setDefaultValue(13000)
                .setMin(13000)
                .setMax(20000)
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.nightAlertTime::set)
                .build();
        category.addEntry(alertEntry);

        var textScaleEntry = entryBuilder
                .startDoubleField(Component.literal("Text size"), DeadmanWatchConfig.CLIENT.nightTextScale.get())
                .setDefaultValue(1.0)
                .setMin(0.5)
                .setMax(3.0)
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.nightTextScale::set)
                .build();
        category.addEntry(textScaleEntry);

        var soundEntry = entryBuilder
                .startBooleanToggle(Component.literal("Enable sound effect"), DeadmanWatchConfig.CLIENT.enableSound.get())
                .setDefaultValue(true)
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.enableSound::set)
                .build();
        category.addEntry(soundEntry);

        var showOutsideEntry = entryBuilder
                .startBooleanToggle(Component.literal("Show in all dimensions"), DeadmanWatchConfig.CLIENT.showOutsideOverworld.get())
                .setDefaultValue(false)
                .setTooltip(Component.literal("If enabled, the clock and alert will appear in all dimensions."))
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.showOutsideOverworld::set)
                .build();
        category.addEntry(showOutsideEntry);

        var anchorEntry = entryBuilder
                .startEnumSelector(
                        Component.literal("Anchor position"),
                        DeadmanWatchConfig.Client.ClockAnchor.class,
                        DeadmanWatchConfig.CLIENT.anchor.get()
                )
                .setDefaultValue(DeadmanWatchConfig.Client.ClockAnchor.TOP)
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.anchor::set)
                .build();
        category.addEntry(anchorEntry);

        var offsetXEntry = entryBuilder
                .startIntField(Component.literal("Horizontal position (X)"), DeadmanWatchConfig.CLIENT.offsetX.get())
                .setDefaultValue(0)
                .setMin(-194)
                .setMax(194)
                .setTooltip(Component.literal("Only works with TOP"))
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.offsetX::set)
                .build();
        category.addEntry(offsetXEntry);

        var offsetYEntry = entryBuilder
                .startIntField(Component.literal("Vertical position (Y)"), DeadmanWatchConfig.CLIENT.offsetY.get())
                .setDefaultValue(0)
                .setMin(-101)
                .setMax(101)
                .setTooltip(Component.literal("Only works with LEFT & RIGHT"))
                .setSaveConsumer(DeadmanWatchConfig.CLIENT.offsetY::set)
                .build();
        category.addEntry(offsetYEntry);

        builder.setSavingRunnable(DeadmanWatchConfig.CLIENT_SPEC::save);

        return builder.build();
    }
}
