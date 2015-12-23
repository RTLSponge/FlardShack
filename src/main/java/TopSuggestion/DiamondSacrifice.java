package TopSuggestion;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

@Plugin(id = "DiamondSacrifice", name = "DiamondSacrifice", version = "0.1-SNAPSHOT")
public class DiamondSacrifice {

    @Inject
    Logger logger;

    @Listener
    public void onDiamondDeath(DamageEntityEvent death){
        Entity entity = death.getTargetEntity();
        Optional<BlockSnapshot> blockSnapshot = death.getCause().first(BlockSnapshot.class);
        if(!blockSnapshot.isPresent()) return;
    }

    //@Listener
    //public void onEntityIngnite(IgniteEntityEvent igniteEntityEvent){
    //    logger.warn(igniteEntityEvent.toString());
    //}
}
