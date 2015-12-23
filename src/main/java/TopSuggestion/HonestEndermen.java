package TopSuggestion;
//https://www.reddit.com/r/minecraftsuggestions/comments/2cvzny/endermen_should_drop_the_block_they_are_holding/

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.entity.HarvestEntityEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "HonestEndermen", name = "HonestEndermen", version = "0.1-SNAPSHOT")
public class HonestEndermen {
    public void onEndyDeath(HarvestEntityEvent event){
        Entity entity = event.getTargetEntity();
//        entity.get()
    }
}
