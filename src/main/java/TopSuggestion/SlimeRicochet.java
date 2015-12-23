package TopSuggestion;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.projectile.Arrow;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

@Plugin(id = "SlimeRicochet", name = "SlimeRicochet", version = "0.1-SNAPSHOT")
public class SlimeRicochet {
    @Inject
    //Logger logger;

    //https://www.reddit.com/r/minecraftsuggestions/comments/3vso63/slimes_have_25_chance_to_ricochet_an_arrow/
    @Listener(order = Order.LAST)
    public void onSlimeCollide(CollideEntityEvent event){
        Optional<Arrow> arrow = event.getCause().first(Arrow.class);
        if(!arrow.isPresent()) return;
        //logger.warning("Arrow hit");
        if(0 < event.getEntities().stream().filter(entity -> entity.getType().equals(EntityTypes.SLIME)).count()){
            //logger.warning("Slime Bounce");
            arrow.get().setRotation(arrow.get().getRotation().negate());
            Optional<Vector3d> velocity = arrow.get().get(Keys.VELOCITY);
            if(velocity.isPresent()){
                final Vector3d newVelocity = velocity.get().negate();//.mul(2);
                arrow.get().offer(Keys.VELOCITY, newVelocity);
                event.setCancelled(true);
            }
        }
    }
}
