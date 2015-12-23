package TopSuggestion;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Sets;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.Set;

@Plugin(id = "KnockOnWood", name = "KnockOnWood", version = "0.1-SNAPSHOT")
public class KnockOnWood {

    static boolean isWoodDoor(BlockState door){
        Set<BlockType> set = Sets.newHashSet(
                BlockTypes.ACACIA_DOOR,
                BlockTypes.BIRCH_DOOR,
                BlockTypes.DARK_OAK_DOOR,
                BlockTypes.JUNGLE_DOOR,
                BlockTypes.SPRUCE_DOOR,
                BlockTypes.WOODEN_DOOR,
                BlockTypes.TRAPDOOR
        );
        return set.contains(door.getType());
    }

    static boolean isMetalDoor(BlockState door){
        Set<BlockType> set = Sets.newHashSet(
                BlockTypes.IRON_TRAPDOOR,
                BlockTypes.IRON_DOOR,
                BlockTypes.IRON_BARS,
                BlockTypes.IRON_BLOCK
        );
        return set.contains(door.getType());
    }

    //https://www.reddit.com/r/minecraftsuggestions/comments/3h82ow/left_clicking_a_door_with_your_fists_produces_a/
    @Listener(order = Order.LAST)
    public void onKnock(final InteractBlockEvent.Primary knock){
        final Optional<Player> optPlayer = knock.getCause().first(Player.class);
        if (!optPlayer.isPresent()) return;
        final Player player = optPlayer.get();
        Location<World> location = knock.getTargetBlock().getLocation().get();
        BlockState bs = knock.getTargetBlock().getState();
        Vector3d position = location.getPosition();
        if(isMetalDoor(bs)) {
            location.getExtent().playSound(SoundTypes.BLAZE_HIT, position, 1, 1);
        } else if(isWoodDoor(bs)){
            location.getExtent().playSound(SoundTypes.ZOMBIE_WOOD, position, 1, 1);
        }
    }

}
