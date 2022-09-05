package net.MGThorn.baritonelistmine;

import baritone.api.BaritoneAPI;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import baritone.api.command.registry.Registry;

public class BaritoneListMine implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.



		LOGGER.info("Hello Fabric world!");



		//adding the command using BaritoneAPI
		StoragepointsCommand sp = new StoragepointsCommand(BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().getBaritone());
		BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().getRegistry().register(sp);
	}
}
