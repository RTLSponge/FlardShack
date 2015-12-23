package TopSuggestion;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.MessageSinkEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.sink.MessageSink;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://www.reddit.com/r/minecraftsuggestions/comments/3do0n4/chat_warnings_like_you_can_only_sleep_at_night/
 */
@Plugin(id = "ChatRedirect", name = "ChatRedirect", version = "0.1-SNAPSHOT")
public class ChatRedirect {

    @Listener(order = Order.LATE)
    public void onChatEvent(MessageSinkEvent event){
        MessageSink oldSink = event.getSink();
        Iterable<CommandSource> recipients = event.getSink().getRecipients();
        List<Player> players = new ArrayList<Player>();
        Set<CommandSource> sources = new HashSet<CommandSource>();
        recipients.forEach(commandSource -> {
            if(commandSource instanceof Player){
                players.add((Player) commandSource);
            } else {
               sources.add(commandSource);
            }
        });
        event.setSink(new MessageSink() {
            @Override
            public Iterable<CommandSource> getRecipients() {
                return sources;
            }

            @Override
            public Text transformMessage(CommandSource target, Text text) {
                return oldSink.transformMessage(target, text);
            }
        });

        players.forEach(player -> {
            player.sendMessage(ChatTypes.ACTION_BAR, oldSink.transformMessage(player, event.getMessage()));
        });
    }
}
