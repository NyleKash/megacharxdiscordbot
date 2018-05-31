import java.util.List;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.GuildAction;

public class Main {
	
	public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException, Exception {

		JDA api = new JDABuilder(AccountType.BOT)
				.setToken(Values.get("constants.txt", 2))
				.setGame(Game.playing("Charizardite X"))
				.buildBlocking();
		api.addEventListener(new CommandListener());
		api.addEventListener(new DMListener());
		// generic listener is pretty useless api.addEventListener(new GenericListener());
		
		// open dm with everyone
		List<User> users = api.getUsers();
		for(int i = 0; users.size() > i; i++) {
			if (!users.get(i).equals(api.getSelfUser()))
				users.get(i).openPrivateChannel().queue();
		}
		
		while(true) {
			Thread.sleep(60000);
			api.getPresence().setGame(Game.playing("Charizardite Y???"));
			Thread.sleep(60000);
			api.getPresence().setGame(Game.playing("Charizardite X"));
		}
				
	}
	

}
