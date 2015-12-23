package TopSuggestion;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.AgeableData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.ai.GoalTypes;
import org.spongepowered.api.entity.ai.task.AITaskBuilder;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.builtin.creature.AttackLivingAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.AvoidEntityAITask;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(id = "BabyRevenge", name = "BabyRevenge", version = "0.1-SNAPSHOT")
public class BabyRevenge {

    @Inject
    Logger logger;

    @Listener(order = Order.LATE)
    public void onAttackBaby(DamageEntityEvent event) {
        logger.log(Level.WARNING, event.toString());
        Entity entity = event.getTargetEntity();
        if(!isBaby(entity)) {
            return;
        }
        logger.log(Level.WARNING, "Baby attacked");
        Living living = (Living) entity;
        Location<World> location = entity.getLocation();
        Collection<Entity> sameSpecies = location.getExtent().getEntities(e->e.getType().equals(entity.getType()));
        sameSpecies.stream().filter(e->location.getPosition().distance(location.getPosition()) < 10).forEach(
                e-> {
                    if(isBaby(e)){
                        logger.log(Level.WARNING, "found nearby baby "+e);
                        ((Agent) e).getGoal(GoalTypes.NORMAL).ifPresent(
                                agentGoal -> agentGoal.getTasksByType(AITaskTypes.AVOID_ENTITY).forEach(
                                        avoid->{
                                            ((AvoidEntityAITask) avoid).setTargetSelector(t->t.equals(event.getCause().first(Player.class)));
                                        }
                                )
                        );
                    } else {
                        logger.log(Level.WARNING, "found nearby adult "+e);
                        ((Agent) e).getGoal(GoalTypes.NORMAL).ifPresent(
                                agentGoal -> agentGoal.addTask(20,
                                        Sponge.getGame().getRegistry().createBuilder(AttackLivingAITask.Builder.class)
                                                .longMemory()
                                                .target(Player.class)
                                                .build((Creature) e))
                        );
                    }
                }
        );
    }

    static boolean isBaby(Entity e){
        Optional<AgeableData> data = e.get(AgeableData.class);
        if(!data.isPresent()) return false;
        return data.get().baby().get();
    }
}
