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

public class StoreCommand extends Command {


    public StoreCommand(IBaritone baritone) {
        super(baritone, "store","put");
    }

    @Override
    public void execute(String label, IArgConsumer args) {
        int quantity = args.getAsOrDefault(Integer.class, 0);
        args.requireMin(1);
        List<BlockOptionalMeta> boms = new ArrayList<>();
        while (args.hasAny()) {
            boms.add(args.getDatatypeFor(ForBlockOptionalMeta.INSTANCE));
        }
        //WorldScanner.INSTANCE.repack(ctx);
        logDirect(String.format("Mining %s", boms.toString()));

    }

    @Override
    public Stream<String> tabComplete(String s, IArgConsumer args) {
        return args.tabCompleteDatatype(BlockById.INSTANCE);
    }

    @Override
    public String getShortDesc() {
        return "put items into chests";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "UwU",
                "d_b"
        );
    }
}