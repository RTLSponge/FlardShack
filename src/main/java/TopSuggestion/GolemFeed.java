package TopSuggestion;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Sets;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Plugin(id = "GolemFeed", name = "GolemFeed", version = "0.1-SNAPSHOT")
public class GolemFeed {

    static boolean isGolem(Entity entity){
        return Sets.newHashSet(EntityTypes.IRON_GOLEM, EntityTypes.SNOWMAN).contains(entity.getType());
    }

    static Optional<ItemType> healItemFor(EntityType type){
        Map<EntityType, ItemType> map = new HashMap<>();
        map.put(EntityTypes.IRON_GOLEM, ItemTypes.IRON_INGOT);
        map.put(EntityTypes.SNOWMAN, ItemTypes.SNOWBALL);
        return Optional.ofNullable(map.get(type));
    }

    static ParticleEffect createEffect(ParticleType type){
        ///particle heart ~ ~ ~ 1 0 1 1 5 force
        return ParticleEffect.builder()
                .type(type)
                .count(5)
                .offset(Vector3d.createRandomDirection(new Random()))
                .motion(Vector3d.FORWARD)
                .build();
    }

    //https://www.reddit.com/r/minecraftsuggestions/comments/2ej19m/heal_iron_golems_with_iron_ingots/
    @Listener(order = Order.LAST)
    public void onGolemRightClick(InteractEntityEvent.Secondary boop){
        Entity entity = boop.getTargetEntity();
        Optional<Player> optPlayer = boop.getCause().first(Player.class);
        if(!optPlayer.isPresent()) return;
        Player player = optPlayer.get();
        if(isGolem(entity)){
            Optional<ItemType> heal = healItemFor(entity.getType());
            if(player.getItemInHand().isPresent()){
                boolean shouldHeal = player.getItemInHand().get().getItem().equals(heal.get());
                if(shouldHeal) {

                    final HealthData data = entity.getOrCreate(HealthData.class).get();
                    MutableBoundedValue<Double> health = data.health();
                    Value<Double> nextValue = health.set(health.get() + (health.getMaxValue() / 10));
                    final HealthData nextData = data.set(nextValue);
                    final DataTransactionResult result = entity.offer(nextData);
                    ParticleEffect effect;
                    Location<World> loc = entity.getLocation();
                    //if not at max health, and was successful.
                    if((!health.get().equals(health.getMaxValue())) && DataTransactionResult.Type.SUCCESS.equals(result.getType())){
                        //But what about creative? Should their be a method to reduce an itemstack as if you had used it?
                        ItemStack item = player.getItemInHand().get();
                        item.setQuantity(item.getQuantity() - 1);
                        player.setItemInHand(item);
                        effect = createEffect(ParticleTypes.HEART);
                    } else if (health.get().equals(health.getMaxValue())) {
                        effect = createEffect(ParticleTypes.VILLAGER_HAPPY);
                    } else{
                        effect = createEffect(ParticleTypes.BARRIER);
                    }
                    boop.setCancelled(true);
                    loc.getExtent().spawnParticles(effect, loc.getPosition());
                }
            }
        }
    }

}
