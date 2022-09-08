package net.MGThorn.baritonelistmine;

import java.io.File;


import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.command.exception.CommandNotFoundException;
import baritone.api.event.events.*;
import baritone.api.event.listener.IGameEventListener;
import baritone.command.ExampleBaritoneControl;
import baritone.event.GameEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

import static baritone.api.command.IBaritoneChatControl.FORCE_COMMAND_PREFIX;
import static net.MGThorn.baritonelistmine.BaritoneListMine.LOGGER;


public class GUI extends GuiBase
{
    public GUI()
    {

        this.title = StringUtils.translate("BLM selection Browser");
    }


    @Override
    public void initGui()
    {
        super.initGui();

        int x = 12;
        int y = this.height - 40;
        int buttonWidth;
        String label;
        ButtonGeneric button;




        y = this.height - 26;
        x += this.createButton(x, y, -1, ButtonListener.Type.LOAD_LIST) + 4;
        x += this.createButton(x, y, -1, ButtonListener.Type.PICK_LIST) + 4;


    }

    private int createButton(int x, int y, int width, ButtonListener.Type type)
    {
        ButtonListener listener = new ButtonListener(type, this);
        String label = StringUtils.translate(type.getTranslationKey());

        if (width == -1)
        {
            width = this.getStringWidth(label) + 10;
        }

        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, label);

        if (type == ButtonListener.Type.PICK_LIST)
        {
            button.setHoverStrings(StringUtils.translate("click to set your list file to mine from"));
        }

        this.addButton(button, listener);

        return width;
    }

    private static class ButtonListener implements IButtonActionListener
    {
        private final Type type;


        public ButtonListener(Type type, GUI gui)
        {
            this.type = type;

        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            if(type == ButtonListener.Type.PICK_LIST){
                BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
                //LOGGER.info(Registry.BLOCK.stream().findFirst().toString())
                //output : Optional[Block{minecraft:air}]

            }else if(type == ButtonListener.Type.LOAD_LIST){
                //BaritoneAPI.getProvider().getPrimaryBaritone().getInputOverrideHandler().onSendChatMessage(new ChatEvent("pause"));

            }




        }

        public enum Type
        {
            LOAD_LIST  ("show the list"),
            PICK_LIST   ("pick list");

            private final String translationKey;

            private Type(String translationKey)
            {
                this.translationKey = translationKey;
            }

            public String getTranslationKey()
            {
                return this.translationKey;
            }
        }
    }


}

