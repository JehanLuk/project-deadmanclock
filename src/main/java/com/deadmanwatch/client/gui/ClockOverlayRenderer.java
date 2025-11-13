package com.deadmanwatch.client.gui;

import com.deadmanwatch.config.DeadmanWatchConfig;
import com.deadmanwatch.registry.ModSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings({ "removal", "null" })
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClockOverlayRenderer {

    private static final ResourceLocation INNER_TEX =
            new ResourceLocation("deadmanwatch", "textures/gui/clock_inner.png");
    private static final ResourceLocation OUTER_TEX =
            new ResourceLocation("deadmanwatch", "textures/gui/clock_outer.png");

    private static float smoothAngle = 0f;
    private static boolean alertPlayedThisNight = false;
    private static boolean wasNight = false;
    private static ResourceLocation lastDimension = null;
    private static int dimensionChangeCooldown = 0;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.options.hideGui) return;

        ResourceLocation currentDim = mc.level.dimension().location();

        if (lastDimension == null || !lastDimension.equals(currentDim)) {
            lastDimension = currentDim;
            dimensionChangeCooldown = 500;
            alertPlayedThisNight = false;
            wasNight = false;
        }

        if (dimensionChangeCooldown > 0) {
            dimensionChangeCooldown--;
        }

        PoseStack pose = event.getGuiGraphics().pose();

        long time = mc.level.getDayTime() % 24000L;
        float targetAngle = (time / 24000f) * 360f;
        smoothAngle += (targetAngle - smoothAngle) * 0.1f;

        int outerWidth = 39;
        int outerHeight = 21;
        int innerWidth = 31;
        int innerHeight = 31;

        float opacity = DeadmanWatchConfig.CLIENT.opacity.get().floatValue();
        int alertTime = DeadmanWatchConfig.CLIENT.nightAlertTime.get();
        boolean enableSound = DeadmanWatchConfig.CLIENT.enableSound.get();
        boolean showOutsideOverworld = DeadmanWatchConfig.CLIENT.showOutsideOverworld.get();
        DeadmanWatchConfig.Client.ClockAnchor anchor = DeadmanWatchConfig.CLIENT.anchor.get();
        int offsetX = DeadmanWatchConfig.CLIENT.offsetX.get();
        int offsetY = DeadmanWatchConfig.CLIENT.offsetY.get();

        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        float x = 0f, y = 0f;
        float baseRotation = 0f;

        switch (anchor) {
            case TOP -> {
                x = (screenW - outerWidth) / 2f + offsetX;
                y = 0f;
            }
            case LEFT -> {
                x = 0f;
                y = (screenH - outerHeight) / 2f + offsetY;
                baseRotation = 270f;
            }
            case RIGHT -> {
                x = screenW - outerWidth;
                y = (screenH - outerHeight) / 2f + offsetY;
                baseRotation = 90f;
            }
        }

        float centerX = x + outerWidth / 2f;
        float centerY = y + outerHeight / 2f;

        float innerOffsetX = 0f, innerOffsetY = 0f, outerOffsetX = 0f, outerOffsetY = 0f;

        switch (anchor) {
            case TOP -> innerOffsetY = -10f;
            case LEFT -> {
                innerOffsetX = -18f;
                outerOffsetX = -9f;
            }
            case RIGHT -> {
                innerOffsetX = 18f;
                outerOffsetX = 9f;
            }
        }

        boolean isOverworld = mc.level.dimension().equals(Level.OVERWORLD);

        if (!isOverworld && !showOutsideOverworld) {
            wasNight = false;
            alertPlayedThisNight = false;
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, opacity);

        pose.pushPose();
        {
            pose.translate(centerX + innerOffsetX, centerY + innerOffsetY, 0f);
            pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(baseRotation + smoothAngle));
            pose.translate(-innerWidth / 2f, -innerHeight / 2f, 0f);
            RenderSystem.setShaderTexture(0, INNER_TEX);
            event.getGuiGraphics().blit(INNER_TEX, 0, 0, 0, 0,
                    innerWidth, innerHeight, innerWidth, innerHeight);
        }
        pose.popPose();

        pose.pushPose();
        {
            pose.translate(centerX + outerOffsetX, centerY + outerOffsetY, 0f);
            pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(baseRotation));
            pose.translate(-outerWidth / 2f, -outerHeight / 2f, 0f);
            RenderSystem.setShaderTexture(0, OUTER_TEX);
            event.getGuiGraphics().blit(OUTER_TEX, 0, 0, 0, 0,
                    outerWidth, outerHeight, outerWidth, outerHeight);
        }
        pose.popPose();

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (!isOverworld) {
            wasNight = false;
            alertPlayedThisNight = false;
            return;
        }

        int nightStart = alertTime;
        int nightEnd = 23000;
        boolean isNight = time >= nightStart && time <= nightEnd;

        if (isNight && !wasNight) alertPlayedThisNight = false;

        if (dimensionChangeCooldown <= 0 && isNight && !alertPlayedThisNight) {
            if (enableSound && mc.getSoundManager() != null) {
                SoundEvent sound = ModSounds.CLOCK_ALERT.get();
                SimpleSoundInstance instance = new SimpleSoundInstance(
                        sound.getLocation(),
                        SoundSource.PLAYERS,
                        0.6f, 1.0f,
                        mc.player.getRandom(),
                        false, 0,
                        net.minecraft.client.resources.sounds.SoundInstance.Attenuation.NONE,
                        mc.player.getX(), mc.player.getY(), mc.player.getZ(), false
                );
                mc.getSoundManager().play(instance);
            }

            String message = DeadmanWatchConfig.CLIENT.nightMessage.get();
            mc.gui.setOverlayMessage(Component.literal(message), false);

            alertPlayedThisNight = true;
        }

        if (!isNight && wasNight) alertPlayedThisNight = false;
        wasNight = isNight;
    }
}
