import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
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

public class CommandListener extends ListenerAdapter 
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getRawContent(); 
        // getRawContent() is an atomic getter
        // getContent() is a lazyk getter which modifies the content for e.g. console view (strip discord formatting)
        try {
        	String pre = Values.get("prefix.txt", 1);
			if (content.startsWith(pre)) {
					if (Values.isAdmin(event.getAuthor())) {
						
						content = content.substring(pre.length());
						
						String fin = "Executed!";
						
						if (content.startsWith("prefix ")) {
							String prefix = content.substring(7);
							Values.write("prefix.txt", 1, prefix);
							String out = "New prefix is: " + prefix;
							MessageChannel channel = event.getChannel();
					        channel.sendMessage(out).queue();
						}
						
						//ex .say channelID message
						else if (content.startsWith("say ")) {
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
							} catch(NullPointerException e) {}
							try {
					    		MessageChannel channel = event.getJDA().getTextChannelById(channelID);
					            channel.sendMessage(out).queue();
							} catch(NullPointerException e) {}
						}
						
						else if (content.startsWith("info ")) {
							content = content.substring(5);
							//content to start with the start of users name
							List<User> ul = event.getJDA().getUsersByName(content, true);
							User user = null;
							if (!ul.isEmpty()) {
								user = ul.get(0);
							}
							else {
								ul = event.getJDA().getUsers();
								for (int i = 0; i < ul.size(); i++) {
									User u = ul.get(i);
									if (u.getName().toLowerCase().startsWith(content.toLowerCase())) {
										user = u;
									}
									else if (u.getId().equals(content)) {
										user = u;
									}
								}
							}
							String out = "Invalid User";
							if (user != null) {
								user.openPrivateChannel().queue();
								out = user.getAsMention() + ": " + user.getId();
								List<PrivateChannel> pcl = event.getJDA().getPrivateChannels();
								for (int i = 0; i < pcl.size(); i++) {
									if (pcl.get(i).getUser() == user) out = out + " DM: " + pcl.get(i).getId();
								}
								
								
							}
							MessageChannel channel = event.getChannel();
					        channel.sendMessage(out).queue();
						}
						
						else if (content.startsWith("guild ")) {
							content = content.substring(6);
							List<Guild> gl = event.getJDA().getGuildsByName(content, true);
							Guild guild = null;
							if (!gl.isEmpty()) {
								guild = gl.get(0);
							}
							else {
								gl = event.getJDA().getGuilds();
								for (int i = 0; i < gl.size(); i++) {
									Guild g = gl.get(i);
									if (g.getName().toLowerCase().startsWith(content.toLowerCase())) {
										guild = g;
									}
									else if (g.getId().equals(content)) {
										guild = g;
									}
								}
							}
							MessageEmbed embed = null;
							if (guild != null) {
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
								for (int i = 0; i < rl.size() - 1; i++) {
									if (guild.equals(event.getGuild())) roles = roles + rl.get(i).getAsMention() + ", ";
									else roles = roles + rl.get(i).getName() + ", ";
								}
								embed = new EmbedBuilder()
										.setAuthor(guild.getOwner().getEffectiveName(), guild.getOwner().getUser().getEffectiveAvatarUrl(), guild.getOwner().getUser().getEffectiveAvatarUrl())
										.setTitle(guild.getName())
										.setDescription(guild.getRegionRaw() + " " + guild.getId())
										.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
										.addField("Members", members, false)
										.addField("Text Channels", texts, false)
										.addField("Voice Channels", voices, false)
										.addField("Roles", roles, false)
										.setImage(guild.getIconUrl())
										.setFooter("Created On: ", guild.getIconUrl())
										.setTimestamp(guild.getCreationTime())
										.build();
							}
							MessageChannel channel = event.getChannel();
							if (embed != null) channel.sendMessage(embed).queue();
							else channel.sendMessage("Invalid Guild").queue();
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
						
						
						else {
							fin = "That is an invalid command";
						}
						MessageChannel channel = event.getChannel();
				        channel.sendMessage(fin).queue();
						
					}
					else {
						String out = "You do not possess the power, mortal!";
						MessageChannel channel = event.getChannel();
					    channel.sendMessage(out).queue();
					}
			    	
			}
		} catch (Exception e) {
			e.printStackTrace();
			String out = "You have used that command incorectly";
			MessageChannel channel = event.getChannel();
        channel.sendMessage(out).queue();
        }
        
        
    }
}