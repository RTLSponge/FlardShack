package TopSuggestion;

import com.google.common.collect.Sets;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

@Plugin(id = "SnowReduce", name = "SnowReduce", version = "0.1-SNAPSHOT")
public class SnowReduce {

    static boolean isSnowBlock(BlockType type){
        return Sets.newHashSet(
                BlockTypes.SNOW,
                BlockTypes.SNOW_LAYER
        ).contains(type);
    }

    static int getMaxLayer(){
        return BlockTypes.SNOW_LAYER.getDefaultState().getValue(Keys.LAYER).get().getMaxValue();
    }
    static BlockState getLayerMax() {
        return BlockTypes.SNOW_LAYER.getDefaultState().with(Keys.LAYER, getMaxLayer()).get();
    }
    static int getMinLayer() {
        return BlockTypes.SNOW_LAYER.getDefaultState().getValue(Keys.LAYER).get().getMinValue();
    }
    static BlockState getLayerMin(){
        return BlockTypes.SNOW_LAYER.getDefaultState().with(Keys.LAYER, getMinLayer()).get();
    }

    static BlockState getAIR(){
        return BlockTypes.AIR.getDefaultState();
    }

    static BlockState reduceLayer(final BlockState blockState){
        final BlockType type = blockState.getType();
        final Optional<Integer> currentLayer = blockState.get(Keys.LAYER);
        if(type.equals(BlockTypes.SNOW)) {
            return getLayerMax();
        }else if(blockState.equals(getLayerMin())) {
            return getAIR();
        }else if(currentLayer.isPresent()){
            return blockState.with(Keys.LAYER, currentLayer.get() - 1).orElse(blockState);
        } else{
            return blockState;
        }
    }

    static final boolean isShovel(ItemStack itemStack){
        return Sets.newHashSet(
                ItemTypes.DIAMOND_SHOVEL,
                ItemTypes.GOLDEN_SHOVEL,
                ItemTypes.IRON_SHOVEL,
                ItemTypes.STONE_SHOVEL,
                ItemTypes.WOODEN_SHOVEL
        ).contains(itemStack.getItem());
    }

    //https://www.reddit.com/r/minecraftsuggestions/comments/3g46wv/right_click_with_shovel_on_snow_block_will_take_1/
    @Listener(order = Order.LAST)
    public void onShovelRightClick(InteractBlockEvent.Secondary boop){
        Optional<Player> player = boop.getCause().first(Player.class);
        if(!player.isPresent()) return;
        BlockState bs = boop.getTargetBlock().getState();
        player.get().getItemInHand()
                .filter(SnowReduce::isShovel)
                .ifPresent(
                        itemStack -> {
                            if(isSnowBlock(bs.getType())){
                                boop.getTargetBlock()
                                        .getLocation().get()
                                        .setBlock(reduceLayer(bs));
                            }
                        }
                );
    }

}
