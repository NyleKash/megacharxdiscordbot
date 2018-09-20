package megacharxbot.megacharxbot;
import java.util.List;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Main {
	
	public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException, Exception {

		JarGUI GUI = new JarGUI();
		GUI.setup();
		
		JDA api = new JDABuilder(AccountType.BOT)
				.setToken(Values.get("constants", 2))
				.buildBlocking();
		api.addEventListener(new CommandListener());
		api.addEventListener(new DMListener());
//		api.addEventListener(new PkmnListener());
		
		// open dm with everyone      happens in pkmnlistener too
		// also makes the pokemonfile for everyone
		List<User> users = api.getUsers();
		Values.makeDir("pokemon");
		for(int i = 0; users.size() > i; i++) {
			User user = users.get(i);
			if (!user.isBot()) {
				user.openPrivateChannel().queue();
					Values.makeFile("pokemon/" + user.getId());
					Values.write("pokemon/" + user.getId(), 1, user.getName());
			}
		}
		while(true) {
			api.getPresence().setGame(Game.playing(Values.get("games", (int)(Math.random() * Values.getLen("games") + 1))));
			Thread.sleep(60000);
		}
				
	}
	

}
