package megacharxbot.megacharxbot;
import java.util.List;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.channel.priv.PrivateChannelDeleteEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DMListener extends ListenerAdapter 
{
    @Override	
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)
    {
		String ownerID = Values.get("constants", 4);
		String DMID = Values.get("constants", 6);
    	
    	if (event.getAuthor().equals(event.getJDA().getSelfUser()) || event.getAuthor().getId().equals(ownerID)) return;
        
    	Message message = event.getMessage();
        String content = message.getContentRaw();
        
        MessageChannel channel = event.getJDA().getPrivateChannelById(DMID);
        channel.sendMessage(event.getAuthor().getAsMention() + ": " + content).queue();
        
    }
    
    @Override
    public void onPrivateChannelDelete(PrivateChannelDeleteEvent event)
    {
    	List<User> users = event.getJDA().getUsers();
		for(int i = 0; users.size() > i; i++) {
			if (!users.get(i).equals(event.getJDA().getSelfUser()))
				users.get(i).openPrivateChannel().queue();
		}
    }
    
}