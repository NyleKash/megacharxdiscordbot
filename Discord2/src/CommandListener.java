import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class CommandListener extends ListenerAdapter
{

	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw(); 
        // getRawContent() is an atomic getter
        // getContent() is a lazyk getter which modifies the content for e.g. console view (strip discord formatting)
    	String pre = Values.get("prefix", 1);
		if (content.startsWith(pre)) {
				
			content = content.substring(pre.length());
			
			//ex .say channelID message
			
			if (content.startsWith("info ")) {
				content = content.substring(5);
				//content to start with the start of users name
				List<User> ul = event.getJDA().getUsersByName(content, true);
				User user = null;
				User iduser = event.getJDA().getUserById(content);
				if (!ul.isEmpty()) {
					user = ul.get(0);
				}
				else if (iduser != null) {
					user = iduser;
				}
				else {
					ul = event.getJDA().getUsers();
					for (int i = 0; i < ul.size(); i++) {
						User u = ul.get(i);
						if (u.getName().toLowerCase().startsWith(content.toLowerCase())) {
							user = u;
						}
						else if (u.getAsMention().equals(content.substring(0, 2).concat(content.substring(3)))) {
							user = u;
						}
					}
				}
				String out = "Invalid User";
				if (user != null) {
					user.openPrivateChannel().queue();
					out = user.getName() + ": " + user.getId();
					List<PrivateChannel> pcl = event.getJDA().getPrivateChannels();
					for (int i = 0; i < pcl.size(); i++) {
						if (pcl.get(i).getUser() == user) out = out + " DM: " + pcl.get(i).getId();
					}
					
					
				}
				MessageChannel channel = event.getChannel();
		        channel.sendMessage(out).queue();
			}
			
			else if (content.startsWith("info")) {
				User user = event.getAuthor();
				user.openPrivateChannel().queue();
				String out = user.getName() + ": " + user.getId();
				List<PrivateChannel> pcl = event.getJDA().getPrivateChannels();
				for (int i = 0; i < pcl.size(); i++) {
					if (pcl.get(i).getUser() == user) out = out + " DM: " + pcl.get(i).getId();
				}
				MessageChannel channel = event.getChannel();
		        channel.sendMessage(out).queue();
			}
			
			else if (content.startsWith("guild") ) {
				Guild guild = event.getGuild();
				List<Member> ml = guild.getMembers();
				String members = "";
				for (int i = 0; i < ml.size(); i++) {
					members = members + ml.get(i).getUser().getAsMention() + ", ";
				}
				List<TextChannel> tl = guild.getTextChannels();
				String texts = "";
				for (int i = 0; i < tl.size(); i++) {
					texts = texts + tl.get(i).getAsMention() + ", ";
				}
				List<VoiceChannel> vl = guild.getVoiceChannels();
				String voices = "";
				for (int i = 0; i < vl.size(); i++) {
					voices = voices + vl.get(i).getName() + ", ";
				}
				List<Role> rl = guild.getRoles();
				String roles = "";
				for (int i = 0; i < rl.size(); i++) {
					roles = roles + rl.get(i).getAsMention() + ", ";
				}
				MessageEmbed embed = new EmbedBuilder()
						.setAuthor(guild.getOwner().getEffectiveName(), guild.getOwner().getUser().getEffectiveAvatarUrl(), guild.getOwner().getUser().getEffectiveAvatarUrl())
						.setTitle(guild.getName())
						.setDescription(guild.getRegionRaw())
						.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
						.addField("Members", members, false)
						.addField("Text Channels", texts, false)
						.addField("Voice Channels", voices, false)
						.addField("Roles", roles, false)
						.setImage(guild.getIconUrl())
						.setFooter("Created On: ", guild.getIconUrl())
						.setTimestamp(guild.getCreationTime())
						.build();
				MessageChannel channel = event.getChannel();
				channel.sendMessage(embed).queue();
			}
			
			else if (content.startsWith("embed")) {
				String url = event.getJDA().getSelfUser().getAvatarUrl();
				LocalDateTime ldt = LocalDateTime.now(ZoneId.of("America/New_York")).plusHours(4);
				MessageEmbed embed = new EmbedBuilder()
						.addField("Embed", "This is an inline Embed", true)
						.addField("Embed2", "This is a second inline Embed", true)
						.addField("Embed3", "This is not an inline Embed", false)
						.addField("Embed4", "This is a second non-inline Embed", false)
						.addField("Embed5", "This is also an inline Embed", true)
						.addField("Embed6", "One more inline Embed!", true)
						.setAuthor("The Embedder", url, url)
						.setColor(java.awt.Color.red)
						.setDescription("This is a cool Embed")
						.setFooter("an embed", url)
						.setImage(url)
						.setThumbnail(url)
						.setTimestamp(ldt)
						.setTitle("EMBED!", url)
						.build();
				MessageChannel channel = event.getChannel();
		        channel.sendMessage(embed).queue();
			}
			
			else if (content.startsWith("pokedex")) {
				List<String> sl = Values.getAll("pokemon/" + event.getAuthor().getId());
				String out = event.getAuthor().getName() + "'s Pokedex:\n";
				if (sl.size() > 1) {
					for (int i = 1; i < sl.size(); i++) {
						out = out + sl.get(i) + "\n";
					}
				}
				else out = "You have no pokémon!";
				MessageChannel channel = event.getChannel();
		        channel.sendMessage(out).queue();
			}
			
			else if (content.startsWith("history")) { try {
				MessageChannel channel = event.getChannel();
				String out = "Working...";
				String m = event.getMessage().getId();
				channel.sendMessage(out).queue(new Consumer<Message>()
			    {
			        @Override
			        public void accept(Message t)
			        {
			            Values.write("msg", 1, t.getId());;
			        }
			    });
				String msgs = "";
				for (Message msg : channel.getIterableHistory()) {
					String name = msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator();
					OffsetDateTime odt = msg.getCreationTime();
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM/d/y h:m");
					String date = odt.format(format);
					String add = name + " [" + date + "]: " + msg.getContentDisplay() + "\n";
					msgs = add + msgs;
				}
				msgs = URLEncoder.encode(msgs, java.nio.charset.StandardCharsets.UTF_8.name());
				String stuff = "data=" + msgs + "&api_paste_private=true&api_paste_name=Message+Data&language=java";
				URL url = new URL("https://paste.lemonmc.com/api/json/create");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setRequestMethod("POST");
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		        wr.writeBytes(stuff);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		        String response;
		        String id = "";
		        String hash = "";
		        while ((response=reader.readLine())!=null) {
		        	if (response.contains("\"id\"")) {
		        		id = response.replaceAll("\\W+","").substring(2);
		        	}
		        	if (response.contains("\"hash\"")) {
		        		hash = response.replaceAll("\\W+","").substring(4);
		        	}
		        }
		        out = "https://paste.lemonmc.com/" + id + "/" + hash + "/raw";
		        wr.flush();
		        wr.close();
		        con.disconnect();
		        channel.editMessageById(Values.get("msg", 1), out).queue();
				} catch(Exception e) {
					MessageChannel channel = event.getChannel();
					String out = "There was an error retreiving history";
					channel.sendMessage(out).queue();;
				}
			}
			
			
			
			//else if (Values.isAdmin(event.getAuthor())) {
			else if (event.getAuthor().equals(event.getJDA().getUserById("120195047470661632"))) {
				
				if (content.startsWith("prefix ")) {
					String prefix = content.substring(7);
					Values.write("prefix", 1, prefix);
					String out = "New prefix is: " + prefix;
					MessageChannel channel = event.getChannel();
			        channel.sendMessage(out).queue();
				}	
				
				else if (content.startsWith("say")) {
					try {
						content = content.substring(4);
						String channelID = content.substring(0, 18);
						String out = content.substring(19);
						if (out.contains(":")) {
							for (int i = 0; i < out.length(); i++) {
								if (out.charAt(i) == ':') {
									for (int x = i + 1; i != x && x < out.length(); x++) {
										if (out.charAt(x) == ':') {
											if ((i == 0 || out.charAt(i - 1) != '<') && (x + 19 >= out.length() || out.charAt(x + 19) != '>')) {
												String replace = out.substring(i, x + 1);
												String emote = out.substring(i + 1, x);
												List<Emote> el = event.getJDA().getEmotesByName(emote, true);
												String men = "";
												if (!el.isEmpty()) {
													men = el.get(0).getAsMention();
													out = out.replaceAll(replace, men);
												}
											}
											i = x + 1;
										}
									}
								}
							}
						}
						try {
				    		MessageChannel channel = event.getJDA().getPrivateChannelById(channelID);
				            channel.sendMessage(out).queue();
						} catch(NullPointerException e) {
							try {
					    		MessageChannel channel = event.getJDA().getTextChannelById(channelID);
					            channel.sendMessage(out).queue();
							} catch(NullPointerException e2) {
								try {
									User user = event.getJDA().getUserById(channelID);
									String c = out;
									user.openPrivateChannel().queue( (channel) -> channel.sendMessage(c).queue() );
								} catch(NullPointerException e3) {
									MessageChannel channel = event.getChannel();
									out = "Invalid use of command";
						            channel.sendMessage(out).queue();
								}
							}
						}
					} catch(StringIndexOutOfBoundsException e) {
						MessageChannel channel = event.getChannel();
						String out = "Invalid use of command";
						channel.sendMessage(out).queue();
					}
				}
				
				else if (content.startsWith("transfer")) {
					//make && content length = someting, get guilds from that
					List<Emote> el = event.getJDA().getGuildById("419646867236716545").getEmotes();
					GuildController target = event.getJDA().getGuildById("457193989586681857").getController();
					for (int i = 0; i < el.size(); i++) {
						Emote e = el.get(i);
						try {
							URL url = new URL(e.getImageUrl());
							URLConnection urlc = url.openConnection();
							urlc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0a2) Gecko/20110613 Firefox/6.0a2");
							InputStream is = urlc.getInputStream();
							target.createEmote(e.getName(), Icon.from(is)).queue();
						} catch (MalformedURLException e1) {e1.printStackTrace();} catch (IOException e1) {e1.printStackTrace();}
					}
				}
				else {
					MessageChannel channel = event.getChannel();
					String out = "Invalid command";
					channel.sendMessage(out).queue();
				}
				
			}
			
			else {
				MessageChannel channel = event.getChannel();
				String out = "Invalid command";
				channel.sendMessage(out).queue();
			}
			    	
		}
        
    }
}