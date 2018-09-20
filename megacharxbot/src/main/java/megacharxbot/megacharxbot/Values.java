package megacharxbot.megacharxbot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Values {
	
	// Access Text
	
	public static String get(String str, int i) {
		try {
			Path file = Paths.get(str);
			BufferedReader reader = Files.newBufferedReader(file);
			while (i > 1) {
				reader.readLine();
				i--;
			}
			String r = reader.readLine();
			reader.close();
			return r;
		} catch (Exception e) {e.printStackTrace();return null;}
	}
	
	public static List<String> getAll(String str) {
		try {
			List<String> sl = new ArrayList<String>();
			Path file = Paths.get(str);
			BufferedReader reader = Files.newBufferedReader(file);
			for (int i = 0; i < getLen(str); i++) {
				sl.add(reader.readLine());
			}
			reader.close();
			return sl;
		} catch (Exception e) {e.printStackTrace();return null;}
	}
	
	public static void write(String str, int i, String out) {
		try {
			Path file = Paths.get(str);
			List<String> strl = new ArrayList<String>();
			int len = getLen(str);
			for (int y = 0; y < len; y++) {
				strl.add(get(str, y + 1));
			}
			BufferedWriter writer = Files.newBufferedWriter(file);
			for (int x = 0; x < len; x++) {
				if (x + 1 == i) {
					writer.write(out);
				}
				else {
					writer.write(strl.get(x));
				}
				if (x != len - 1) writer.newLine();
			}
			if (len == 0) writer.write(out);
			if (i == 0) {
				writer.newLine();
				writer.write(out);
			}
			writer.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static int getLen(String str) {
		try {
			Path file = Paths.get(str);
			BufferedReader reader = Files.newBufferedReader(file);
			int i = 0;
			while (reader.ready()) {
				reader.readLine();
				i++;
			}
			reader.close();
			return i;
		} catch (Exception e) {e.printStackTrace();return 0;}
	}
	
	// Other Functions
	
	public static boolean isAdmin(User user) {
		String ad = "admins";
		String str = user.getId();
		for (int i = 1; i < getLen(ad); i++) {
			if (str.equals(get(ad, i))) return true;
		}
		return false;
	}
	
	public static void makeFile(String str) {
		try {
			Path file = Paths.get(str);
			if (Files.notExists(file)) Files.createFile(file);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void makeDir(String str) {
		try {
			Path file = Paths.get(str);
			if (Files.notExists(file)) Files.createDirectory(file);
		} catch (Exception e) {e.printStackTrace();}
	}

}