import java.util.List;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PkmnListener extends ListenerAdapter
{
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		if (event.getAuthor().isBot()) return;
		//random chance for pokemon
		int random = (int)(Math.random() * 101);
		if (random == 0) {
			String pkmn = Values.get("pkmn", (int)(Math.random() * Values.getLen("pkmn")));
			Values.write("pokemon/" + event.getAuthor().getId(), 0, pkmn);
			MessageChannel channel = event.getChannel();
	        channel.sendMessage(event.getAuthor().getAsMention() + ", you caught a " + pkmn + "!").queue();
		}
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		if (event.getUser().isBot()) return;
		List<User> users = event.getJDA().getUsers();
		Values.makeDir("pokemon");
		for(int i = 0; users.size() > i; i++) {
			User user = users.get(i);
			if (!user.isBot()) {
				user.openPrivateChannel().queue();
					Values.makeFile("pokemon/" + user.getId());
					Values.write("pokemon/" + user.getId(), 1, user.getName());
			}
		}
	}
	
	@Override
	public void onUserUpdateName(UserUpdateNameEvent event) {
		Values.write("pokemon/" + event.getUser().getId(), 1, event.getUser().getName());
	}
}
