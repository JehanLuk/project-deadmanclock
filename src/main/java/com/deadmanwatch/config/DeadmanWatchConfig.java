package com.deadmanwatch.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DeadmanWatchConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        CLIENT = new Client(builder);
        CLIENT_SPEC = builder.build();
    }

    public static class Client {
        public final ForgeConfigSpec.DoubleValue opacity;
        public final ForgeConfigSpec.EnumValue<ClockAnchor> anchor;
        public final ForgeConfigSpec.BooleanValue enableSound;
        public final ForgeConfigSpec.BooleanValue showOutsideOverworld;
        public final ForgeConfigSpec.ConfigValue<String> nightMessage;
        public final ForgeConfigSpec.DoubleValue nightTextScale;
        public final ForgeConfigSpec.IntValue nightAlertTime;
        public final ForgeConfigSpec.IntValue offsetX;
        public final ForgeConfigSpec.IntValue offsetY;

        public enum ClockAnchor {
            TOP, LEFT, RIGHT
        }

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("Clock Settings");

            opacity = builder
                .comment("Clock opacity (0.0 = invisible, 1.0 = fully visible)")
                .defineInRange("opacity", 1.0, 0.0, 1.0);

            anchor = builder
                .comment("Fixed position of the clock: TOP, LEFT, or RIGHT")
                .defineEnum("anchor", ClockAnchor.TOP);

            offsetX = builder
                .comment("Horizontal offset (in pixels)")
                .defineInRange("offsetX", 0, -200, 200);

            offsetY = builder
                .comment("Vertical offset (in pixels)")
                .defineInRange("offsetY", 0, -100, 100);

            enableSound = builder
                .comment("Enable or disable the night alert sound")
                .define("enableSound", true);

            showOutsideOverworld = builder
                .comment("Display the clock and alerts in dimensions outside the Overworld (Nether, End, etc.)")
                .define("showOutsideOverworld", false);

            nightMessage = builder
                .comment("Text displayed at night")
                .define("nightMessage", "The night comes, the monsters begin to rise...");

            nightTextScale = builder
                .comment("Scale of the text displayed at night (1.0 = normal, 2.0 = double, etc.)")
                .defineInRange("nightTextScale", 1.0, 0.5, 3.0);

            nightAlertTime = builder
                .comment("Night alert time (in ticks, 0 = sunrise, 13000 = sunset, 24000 = new day)")
                .defineInRange("nightAlertTime", 13000, 13000, 24000);

            builder.pop();
        }
    }
}
