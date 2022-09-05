package net.MGThorn.baritonelistmine.mixin;

import fi.dy.masa.malilib.gui.GuiBase;
import net.MGThorn.baritonelistmine.GUI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    private void ButtonAction() {
        GuiBase.openGui(new GUI());
    }

    @Inject(at = @At("RETURN"), method = "initWidgets")
    private void addCustomButton(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + -16, 204, 20, Text.translatable("BLM file explorer"), button ->  ButtonAction()));


    }




}
