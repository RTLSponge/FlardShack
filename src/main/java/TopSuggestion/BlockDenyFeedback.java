package TopSuggestion;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "BlockDenyFeedback", name = "BlockDenyFeedback", version = "0.1-SNAPSHOT")
public class BlockDenyFeedback {

    static ParticleEffect DENIED = ParticleEffect.builder().type(ParticleTypes.BARRIER).count(1).build();

    @Listener(order = Order.LAST)
    @IsCancelled
    public void onCancelledPlayerCausedBlockEvent(InteractBlockEvent blockEvent, @First Player player){
        Vector3d x = blockEvent.getInteractionPoint()
                        .orElse(
                                blockEvent.getTargetBlock().getLocation()
                                        .orElse(
                                                player.getLocation()
                                        ).getPosition()
                        );
        player.spawnParticles(DENIED, x);
    }

    @Listener
    public void onInteractGrass(InteractBlockEvent.Primary event){
        event.setCancelled(event.getTargetBlock().getState().getType().equals(BlockTypes.GRASS));
    }

}
