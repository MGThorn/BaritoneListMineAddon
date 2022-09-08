package net.MGThorn.baritonelistmine;

import baritone.Baritone;
import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.cache.IWaypoint;
import baritone.api.cache.IWaypointCollection;
import baritone.api.cache.IWorldData;
import baritone.api.cache.Waypoint;
import baritone.api.cache.IWaypoint.Tag;
import baritone.api.command.Command;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.command.ICommand;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.BlockById;
import baritone.api.command.datatypes.ForBlockOptionalMeta;
import baritone.api.command.datatypes.ForWaypoints;
import baritone.api.command.datatypes.RelativeBlockPos;
import baritone.api.command.exception.CommandInvalidStateException;
import baritone.api.command.exception.CommandInvalidTypeException;
import baritone.api.command.helpers.Paginator;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.utils.BetterBlockPos;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import baritone.api.utils.BlockOptionalMeta;
import baritone.cache.WorldScanner;
import net.minecraft.block.Block;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import static net.MGThorn.baritonelistmine.BaritoneListMine.LOGGER;
import static baritone.api.command.IBaritoneChatControl.FORCE_COMMAND_PREFIX;

public class ListMineCommand extends Command {


    public ListMineCommand(IBaritone baritone) {
        super(baritone, "listmine","lm");
    }

    @Override
    public void execute(String label, IArgConsumer args) {

        //logDirect(String.format(args.getString()));
        //output [CHAT] [Baritone] minecraft:acacia_door

    }

    @Override
    public Stream<String> tabComplete(String s, IArgConsumer args) {
        return args.tabCompleteDatatype(BlockById.INSTANCE);
    }

    @Override
    public String getShortDesc() {
        return "Mine a list of blocks";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "The mine command allows you to tell Baritone to search for and mine a list of blocks.",
                "",
                "The specified blocks can be ores (which are commonly cached), or any other block.",
                "",
                "Also note that baritone uses all cmd [...].", //TODO
                "",
                "Usage:",
                "> mine /data/list.txt"
        );
    }
}