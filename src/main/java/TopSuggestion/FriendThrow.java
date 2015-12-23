package TopSuggestion;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.catalog.CatalogEntityData;
import org.spongepowered.api.data.manipulator.mutable.entity.VelocityData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

@Plugin(id="friendthrow", name="friendthrow", version = "0.1-SNAPSHOT")
public class FriendThrow {

    @Listener
    public void onEntityDamageEvent(InteractEntityEvent.Primary event, @First Player player) {
        if (player.getItemInHand().isPresent()) return;
        Entity entity = event.getTargetEntity();
        Optional<Entity> vehicle = entity.get(Keys.VEHICLE);
        if (vehicle.isPresent()){
            if(vehicle.get().equals(player)){
                entity.remove(Keys.VEHICLE);
                Vector3d rot = player.getRotation();
                Vector3d dir = Quaterniond.fromAxesAnglesDeg(rot.getX(), -rot.getY(), rot.getZ()).getDirection();
                Optional<VelocityData> velocity = entity.get(CatalogEntityData.VELOCITY_DATA);
                if(velocity.isPresent()){
                    VelocityData data = velocity.get();
                    Value<Vector3d> value = data.velocity();
                    Value<Vector3d> newValue = value.set(dir.normalize().mul(5));
                    data.set(newValue);
                    entity.offer(data);
                    event.setCancelled(true);
                }
            }
        }
        else {
            entity.offer(Keys.VEHICLE, player);
            event.setCancelled(true);
        }
    }
}
