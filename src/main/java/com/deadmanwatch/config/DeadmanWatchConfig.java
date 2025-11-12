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

            // Opacidade
            opacity = builder
                .comment("Opacidade do relógio (0.0 = invisível, 1.0 = visível)")
                .defineInRange("opacity", 1.0, 0.0, 1.0);

            // Âncora
            anchor = builder
                .comment("Posição fixa do relógio: TOP, LEFT, RIGHT")
                .defineEnum("anchor", ClockAnchor.TOP);

            // Deslocamento X
            offsetX = builder
                .comment("Deslocamento horizontal (em pixels)")
                .defineInRange("offsetX", 0, -200, 200);

            // Deslocamento Y
            offsetY = builder
                .comment("Deslocamento vertical (em pixels)")
                .defineInRange("offsetY", 0, -100, 100);

            // Som noturno
            enableSound = builder
                .comment("Ativar ou não o som de alerta noturno")
                .define("enableSound", true);

            // Texto noturno
            nightMessage = builder
                .comment("Texto exibido durante a noite")
                .define("nightMessage", "The night comes, the monsters begin to rise...");

            // Escala do texto noturno
            nightTextScale = builder
                .comment("Escala do texto exibido durante a noite (1.0 = normal, 2.0 = dobro, etc)")
                .defineInRange("nightTextScale", 1.0, 0.5, 3.0);

            // Horário do alerta noturno
            nightAlertTime = builder
                .comment("Horário do alerta noturno (em ticks, 0 = amanhecer, 13000 = pôr do sol, 24000 = reinício do dia)")
                .defineInRange("nightAlertTime", 13000, 13000, 24000);

            builder.pop();
        }
    }
}
