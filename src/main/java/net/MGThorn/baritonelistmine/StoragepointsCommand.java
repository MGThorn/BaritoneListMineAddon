//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import static net.MGThorn.baritonelistmine.BaritoneListMine.LOGGER;
import static baritone.api.command.IBaritoneChatControl.FORCE_COMMAND_PREFIX;

public class StoragepointsCommand extends Command {
    private Map<IWorldData, List<IWaypoint>> deletedStoragepoints  = new HashMap();



    public StoragepointsCommand(IBaritone baritone) {
        super(baritone, "storagepoints", "storagepoint", "sp");
    }

    @Override
    public void execute(String label, IArgConsumer args){
        Action action = args.hasAny() ? Action.getByName(args.getString()) : Action.LIST;
        if (action == null) {
            try {
                throw new CommandInvalidTypeException(args.consumed(), "an action");
            } catch (CommandInvalidTypeException e) {
                e.printStackTrace();
            }
        }
        BiFunction<IWaypoint, Action, Text> toComponent = (waypoint, _action) -> {
            MutableText component = Text.literal("");
            MutableText tagComponent = Text.literal(waypoint.getTag().name() + " ");
            tagComponent.setStyle(tagComponent.getStyle().withColor(Formatting.GRAY));
            String name = waypoint.getName();
            MutableText nameComponent = Text.literal(!name.isEmpty() ? name : "<empty>");
            nameComponent.setStyle(nameComponent.getStyle().withColor(!name.isEmpty() ? Formatting.GRAY : Formatting.DARK_GRAY));
            MutableText timestamp = Text.literal(" @ " + new Date(waypoint.getCreationTimestamp()));
            timestamp.setStyle(timestamp.getStyle().withColor(Formatting.DARK_GRAY));
            component.append(tagComponent);
            component.append(nameComponent);
            component.append(timestamp);
            component.setStyle(component.getStyle()
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Text.literal("Click to select")
                    ))
                    .withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            String.format(
                                    "%s%s %s %s @ %d",
                                    FORCE_COMMAND_PREFIX,
                                    //TODO need to find fix here
                                    label,
                                    _action.names[0],
                                    waypoint.getTag().getName(),
                                    waypoint.getCreationTimestamp()
                            ))
                    ));
            return component;
        };
        Function<IWaypoint, Text> transform = waypoint ->
                toComponent.apply(waypoint, action == Action.LIST ? Action.INFO : action);

        if (action == Action.LIST) {
            Tag tag = args.hasAny() ? Tag.getByName(args.peekString()) : null;
            if (tag != null) {
                args.get();
            }
            IWaypoint[] waypoints = tag != null
                    ? ForWaypoints.getWaypointsByTag(this.baritone, tag)
                    : ForWaypoints.getWaypoints(this.baritone);
            if (waypoints.length > 0) {
                args.requireMax(1);
                //TODO what is the Paginator even? --> this seems to go away when line 127 is fixed
                Paginator.paginate(
                        args,
                        waypoints,
                        () -> logDirect(
                                tag != null
                                        ? String.format("All waypoints by tag %s:", tag.name())
                                        : "All waypoints:"
                        ),
                        transform,
                        String.format(
                                "%s%s %s%s",
                                FORCE_COMMAND_PREFIX,
                                label,
                                //TODO need to find fix here : hints same fix as line 91
                                action.names[0],
                                tag != null ? " " + tag.getName() : ""
                        )
                );
            } else {
                args.requireMax(0);
                try {
                    throw new CommandInvalidStateException(
                            tag != null
                                    ? "No waypoints found by that tag"
                                    : "No waypoints found"
                    );
                } catch (CommandInvalidStateException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (action == Action.SAVE) {
            Tag tag = args.hasAny() ? Tag.getByName(args.peekString()) : null;
            if (tag == null) {
                tag = Tag.USER;
            } else {
                args.get();
            }
            String name = (args.hasExactlyOne() || args.hasExactly(4)) ? args.getString() : "";
            BetterBlockPos pos = args.hasAny()
                    ? args.getDatatypePost(RelativeBlockPos.INSTANCE, ctx.playerFeet())
                    : ctx.playerFeet();
            args.requireMax(0);
            IWaypoint waypoint = new Waypoint(name, tag, pos);
            ForWaypoints.waypoints(this.baritone).addWaypoint(waypoint);
            MutableText component = Text.literal("Waypoint added: ");
            component.setStyle(component.getStyle().withColor(Formatting.GRAY));
            component.append(toComponent.apply(waypoint, Action.INFO));
            logDirect(component);
        }
        else if (action == Action.CLEAR) {
            args.requireMax(1);
            Tag tag = Tag.getByName(args.getString());
            IWaypoint[] waypoints = ForWaypoints.getWaypointsByTag(this.baritone, tag);
            for (IWaypoint waypoint : waypoints) {
                ForWaypoints.waypoints(this.baritone).removeWaypoint(waypoint);
            }
            deletedStoragepoints.computeIfAbsent(baritone.getWorldProvider().getCurrentWorld(), k -> new ArrayList<>()).addAll(Arrays.<IWaypoint>asList(waypoints));
            MutableText textComponent = Text.literal(String.format("Cleared %d waypoints, click to restore them", waypoints.length));
            textComponent.setStyle(textComponent.getStyle().withClickEvent(new ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    String.format(
                            "%s%s restore @ %s",
                            FORCE_COMMAND_PREFIX,
                            label,
                            Stream.of(waypoints).map(wp -> Long.toString(wp.getCreationTimestamp())).collect(Collectors.joining(" "))
                    )
            )));
            logDirect(textComponent);
        }
        else if (action == Action.RESTORE) {
            List<IWaypoint> waypoints = new ArrayList<>();
            List<IWaypoint> deletedWaypoints = this.deletedStoragepoints.getOrDefault(baritone.getWorldProvider().getCurrentWorld(), Collections.emptyList());
            if (args.peekString().equals("@")) {
                args.get();
                // no args.requireMin(1) because if the user clears an empty tag there is nothing to restore
                while (args.hasAny()) {
                    long timestamp = args.getAs(Long.class);
                    for (IWaypoint waypoint : deletedWaypoints) {
                        if (waypoint.getCreationTimestamp() == timestamp) {
                            waypoints.add(waypoint);
                            break;
                        }
                    }
                }
            } else {
                args.requireExactly(1);
                int size = deletedWaypoints.size();
                int amount = Math.min(size, args.getAs(Integer.class));
                waypoints = new ArrayList<>(deletedWaypoints.subList(size - amount, size));
            }
            waypoints.forEach(ForWaypoints.waypoints(this.baritone)::addWaypoint);
            deletedWaypoints.removeIf(waypoints::contains);
            logDirect(String.format("Restored %d waypoints", waypoints.size()));
        } else {
            IWaypoint[] waypoints = args.getDatatypeFor(ForWaypoints.INSTANCE);
            IWaypoint waypoint = null;
            if (args.hasAny() && args.peekString().equals("@")) {
                args.requireExactly(2);
                args.get();
                long timestamp = args.getAs(Long.class);
                for (IWaypoint iWaypoint : waypoints) {
                    if (iWaypoint.getCreationTimestamp() == timestamp) {
                        waypoint = iWaypoint;
                        break;
                    }
                }
                if (waypoint == null) {
                    try {
                        throw new CommandInvalidStateException("Timestamp was specified but no waypoint was found");
                    } catch (CommandInvalidStateException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                switch (waypoints.length) {
                    case 0:
                        try {
                            throw new CommandInvalidStateException("No waypoints found");
                        } catch (CommandInvalidStateException e) {
                            e.printStackTrace();
                        }
                    case 1:
                        waypoint = waypoints[0];
                        break;
                    default:
                        break;
                }
            }
            if (waypoint == null) {
                args.requireMax(1);
                Paginator.paginate(
                        args,
                        waypoints,
                        () -> logDirect("Multiple waypoints were found:"),
                        transform,
                        String.format(
                                "%s%s %s %s",
                                FORCE_COMMAND_PREFIX,
                                label,
                                action.names[0],
                                args.consumedString()
                        )
                );
            } else {
                if (action == Action.INFO) {
                    logDirect(transform.apply(waypoint));
                    logDirect(String.format("Position: %s", waypoint.getLocation()));
                    MutableText deleteComponent = Text.literal("Click to delete this waypoint");
                    deleteComponent.setStyle(deleteComponent.getStyle().withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            String.format(
                                    "%s%s delete %s @ %d",
                                    FORCE_COMMAND_PREFIX,
                                    label,
                                    waypoint.getTag().getName(),
                                    waypoint.getCreationTimestamp()
                            )
                    )));
                    MutableText goalComponent = Text.literal("Click to set goal to this waypoint");
                    goalComponent.setStyle(goalComponent.getStyle().withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            String.format(
                                    "%s%s goal %s @ %d",
                                    FORCE_COMMAND_PREFIX,
                                    label,
                                    waypoint.getTag().getName(),
                                    waypoint.getCreationTimestamp()
                            )
                    )));
                    MutableText recreateComponent = Text.literal("Click to show a command to recreate this waypoint");
                    recreateComponent.setStyle(recreateComponent.getStyle().withClickEvent(new ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            String.format(
                                    "%s%s save %s %s %s %s %s",

                                    BaritoneAPI.getSettings().prefix.value,// This uses the normal prefix because it is run by the user.

                                    label,
                                    waypoint.getTag().getName(),
                                    waypoint.getName(),
                                    waypoint.getLocation().x,
                                    waypoint.getLocation().y,
                                    waypoint.getLocation().z
                            )
                    )));
                    MutableText backComponent = Text.literal("Click to return to the waypoints list");
                    backComponent.setStyle(backComponent.getStyle().withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            String.format(
                                    "%s%s list",
                                    FORCE_COMMAND_PREFIX,
                                    label
                            )
                    )));
                    logDirect(deleteComponent);
                    logDirect(goalComponent);
                    logDirect(recreateComponent);
                    logDirect(backComponent);
                } else if (action == Action.DELETE) {
                    ForWaypoints.waypoints(this.baritone).removeWaypoint(waypoint);
                    deletedStoragepoints.computeIfAbsent(baritone.getWorldProvider().getCurrentWorld(), k -> new ArrayList<>()).add(waypoint);
                    MutableText textComponent = Text.literal("That waypoint has successfully been deleted, click to restore it");
                    textComponent.setStyle(textComponent.getStyle().withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            String.format(
                                    "%s%s restore @ %s",
                                    FORCE_COMMAND_PREFIX,
                                    label,
                                    waypoint.getCreationTimestamp()
                            )
                    )));
                    logDirect(textComponent);
                } else if (action == Action.GOAL) {
                    Goal goal = new GoalBlock(waypoint.getLocation());
                    baritone.getCustomGoalProcess().setGoal(goal);
                    logDirect(String.format("Goal: %s", goal));
                } else if (action == Action.GOTO) {
                    Goal goal = new GoalBlock(waypoint.getLocation());
                    baritone.getCustomGoalProcess().setGoalAndPath(goal);
                    logDirect(String.format("Going to: %s", goal));
                }
            }
        }
        


        LOGGER.info("cmd sp used");


    }

    @Override
    public Stream<String> tabComplete(String s, IArgConsumer args) {
        if (args.hasAny()) {
            if (args.hasExactlyOne()) {
                return new TabCompleteHelper()
                        .append(Action.getAllNames())
                        .sortAlphabetically()
                        .filterPrefix(args.getString())
                        .stream();
            } else {
                Action action = Action.getByName(args.getString());
                if (args.hasExactlyOne()) {
                    if (action == Action.LIST || action == Action.SAVE || action == Action.CLEAR) {
                        return new TabCompleteHelper()
                                .append(IWaypoint.Tag.getAllNames())
                                .sortAlphabetically()
                                .filterPrefix(args.getString())
                                .stream();
                    } else if (action == Action.RESTORE) {
                        return Stream.empty();
                    } else {
                        return args.tabCompleteDatatype(ForWaypoints.INSTANCE);
                    }
                } else if (args.has(3) && action == Action.SAVE) {
                    args.get();
                    args.get();
                    return args.tabCompleteDatatype(RelativeBlockPos.INSTANCE);
                }
            }
        }
        return Stream.empty();


    }

    @Override
    public String getShortDesc() {
        return "Manage storagepoints";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "The storagepoint command allows you to manage Baritone's storagepoints.",
                "",
                "storagepoints can be used to mark positions for later. storagepoints are each given a tag and an optional name.",
                "",
                "Note that the info, delete, and goal commands let you specify a storagepoint by tag. If there is more than one storagepoint with a certain tag, then they will let you select which storagepoint you mean.",
                "",
                "Missing arguments for the save command use the USER tag, creating an unnamed storagepoint and your current position as defaults.",
                "",
                "Usage:",
                "> sp [l/list] - List all waypoints.",
                "> sp <l/list> <tag> - List all waypoints by tag.",
                "> sp <s/save> - Save an unnamed USER waypoint at your current position",
                "> sp <s/save> [tag] [name] [pos] - Save a waypoint with the specified tag, name and position.",
                "> sp <i/info/show> <tag/name> - Show info on a waypoint by tag or name.",
                "> sp <d/delete> <tag/name> - Delete a waypoint by tag or name.",
                "> sp <restore> <n> - Restore the last n deleted waypoints.",
                "> sp <c/clear> <tag> - Delete all waypoints with the specified tag.",
                "> sp <g/goal> <tag/name> - Set a goal to a waypoint by tag or name.",
                "> sp <goto> <tag/name> - Set a goal to a waypoint by tag or name and start pathing."
        );
    }
    private enum Action {
        LIST("list", "get", "l"),
        CLEAR("clear", "c"),
        SAVE("save", "s"),
        INFO("info", "show", "i"),
        DELETE("delete", "d"),
        RESTORE("restore"),
        GOAL("goal", "g"),
        GOTO("goto");
        private final String[] names;

        Action(String... names) {
            this.names = names;
        }
        public static Action getByName(String name) {
            for (Action action : Action.values()) {
                for (String alias : action.names) {
                    if (alias.equalsIgnoreCase(name)) {
                        return action;
                    }
                }
            }
            return null;
        }

        public static String[] getAllNames() {
            Set<String> names = new HashSet<>();
            for (Action action : Action.values()) {
                names.addAll(Arrays.asList(action.names));
            }
            return names.toArray(new String[0]);
        }
    }


}