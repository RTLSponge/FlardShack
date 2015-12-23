package TopSuggestion;

import com.google.common.collect.Sets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.IgniteEntityEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Set;

@Plugin(id = "RefreshingFlame", name = "RefreshingFlame", version = "0.1-SNAPSHOT")
public class RefreshingFlame {

    //@Inject
    //Logger logger;

    //https://www.reddit.com/r/minecraftsuggestions/comments/3d5l3p/jumping_into_water_while_on_fire_plays_a/
    @Listener(order = Order.POST)
    public void onPlayerExtinguish(final IgniteEntityEvent event) {
        //logger.log(Level.WARNING, "Entity Ignited: "+event.getTargetEntity());
        int ticksToGo = event.getFireTicks();
        Entity entity = event.getTargetEntity();
        Sponge.getScheduler().createTaskBuilder()
                .intervalTicks(1)
                .execute(task -> trackFireEntities(task, entity))
                .submit(this);
    }

    final static Set<BlockType> WATER = Sets.newHashSet(BlockTypes.FLOWING_WATER, BlockTypes.WATER);

    public void trackFireEntities(final Task task, final Entity entity){
        boolean onFireStill = entity.get(Keys.IS_AFLAME).get();
        if(!onFireStill){
            //logger.log(Level.WARNING, "Extinguished");
            task.cancel();
            Location<World> location = entity.getLocation();
            //if(WATER.contains(location.getBlockType())){
            location.getExtent().playSound(SoundTypes.FIZZ, location.getPosition(), 2);
            location.getExtent().playSound(SoundTypes.FIZZ, location.getPosition(), 2);
            location.getExtent().playSound(SoundTypes.FIZZ, location.getPosition(), 2);
            location.getExtent().playSound(SoundTypes.FIZZ, location.getPosition(), 2);
            //}
        }
    }
}
