package net.MGThorn.baritonelistmine.mixin;

import fi.dy.masa.malilib.gui.GuiBase;
import net.MGThorn.baritonelistmine.GUI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.WorldPresets;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow @Final private static Logger LOGGER;

    protected TitleScreenMixin(Text title) {
        super(title);
    }


    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100+ 205, y, 50, 20, Text.translatable("test") , button -> this.client.setScreen(new OptionsScreen(this, this.client.options))));


    }
    private void ButtonAction(){
        GuiBase.openGui(new GUI());
        LOGGER.info("New GUI added");


    }
    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton2(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100+ 260, y, 50, 20, Text.translatable("test") , button -> ButtonAction()));


    }







}
