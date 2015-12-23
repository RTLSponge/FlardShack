package TopSuggestion;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.CollideBlockEvent;
import org.spongepowered.api.plugin.Plugin;


public class GoldCollide {

    @Listener(order = Order.LAST)
    public void fallThruGold(final CollideBlockEvent collideBlockEvent) {
        if (collideBlockEvent.getTargetBlock().getType().equals(BlockTypes.GOLD_BLOCK)) {
            collideBlockEvent.setCancelled(true);
        }
    }
}