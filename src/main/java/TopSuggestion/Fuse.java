package TopSuggestion;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.TickBlockEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Plugin(id = "Fuse", name = "Fuse", version = "0.1-SNAPSHOT")
public class Fuse {

    //@Inject
    // logger;


    //https://www.reddit.com/r/minecraftsuggestions/comments/yfc8u/being_able_to_place_gunpowder_on_the_ground_light/
    @Listener(order = Order.LAST)
    public void onFireTick(final TickBlockEvent e) {
        fireTick(e.getTargetBlock());
    }

    @Listener(order = Order.LAST)
    public void onLightFuse(final InteractBlockEvent.Secondary lighterEvent){
        final BlockSnapshot blockSnapshot = lighterEvent.getTargetBlock();
        if(blockSnapshot.getState().getType().equals(BlockTypes.TRIPWIRE)){
            lighterEvent.getCause().first(Player.class).ifPresent(
                    player ->
                            player.getItemInHand()
                                    .filter(itemStack -> ItemTypes.FLINT_AND_STEEL.equals(itemStack.getItem()))
                                    .ifPresent(u->createTickingFire(blockSnapshot.getLocation().get())
                                    )
            );
        }
    }

    void createTickingFire(Location<World> x){
        x.setBlock(BlockTypes.FIRE.getDefaultState());
        //https://github.com/SpongePowered/SpongeCommon/issues/303
        //Tick next tick, fast spread.
        //x.addScheduledUpdate(1, 1);
        Sponge.getScheduler().createTaskBuilder().delayTicks(1).execute(task -> fireTick(x.createSnapshot())).submit(this);
    }

    private void fireTick(BlockSnapshot location) {
        if (location.getState().getType().equals(BlockTypes.FIRE)) {
            //logger.log(Level.WARNING, "Ticking fire");
            for (final Direction direction : Direction.values()) {
                //logger.log(Level.WARNING, "Direction: " + direction);
               // logger.log(Level.WARNING, "Passed tests " + direction.isOrdinal() + ' ' + direction.isUpright());
                if (direction.isCardinal() || direction.isUpright()) {
                    Location<World> x = location.getLocation().get().getRelative(direction);
                   // logger.log(Level.WARNING, "location "+x);
                    if (x.getBlock().getType().equals(BlockTypes.TRIPWIRE)) {
                        //logger.log(Level.WARNING, "Found Tripwire");
                        createTickingFire(x);
                    }
                }
            }
        }
    }

}
