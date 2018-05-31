import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.User;

public class Values {
	
	// Access Text (should I be closing?)
	
	public static String get(String str, int i) throws Exception{
		Path file = Paths.get(str);
		BufferedReader reader = Files.newBufferedReader(file);
		while (i > 1) {
		reader.readLine();
		i--;
		}
		return reader.readLine();
	}
	
	public static void write(String str, int i, String out) throws Exception{
		Path file = Paths.get(str);
		List<String> strl = new ArrayList<String>();
		int len = getLen(str);
		for (int y = 0; y < len; y++) {
			strl.add(get(str, y + 1));
			System.out.println(get(str, y));
		}
		System.out.println();
		BufferedWriter writer = Files.newBufferedWriter(file);
		for (int x = 0; x < len; x++) {
			if (x + 1 == i) {
				System.out.println(out);
				writer.write(out);
			}
			else {
				System.out.println(strl.get(x));
				writer.write(strl.get(x));
			}
			if (x != len - 1) writer.newLine();
		}
		writer.close();
	}
	
	public static int getLen(String str) throws Exception{
		Path file = Paths.get(str);
		BufferedReader reader = Files.newBufferedReader(file);
		int i = 0;
		while (reader.ready()) {
			reader.readLine();
			i++;
		}
		return i;
	}
	
	// Other Functions
	
	public static boolean isAdmin(User user) throws Exception {
		String ad = "admins.txt";
		String str = user.getId();
		for (int i = 1; i < getLen(ad); i++) {
			if (str.equals(get(ad, i))) return true;
		}
		return false;
	}
	
	public static void makeDir(String str) throws Exception {
		Path file = Paths.get(str);
		if (Files.notExists(file)) Files.createDirectory(file);
	}
}