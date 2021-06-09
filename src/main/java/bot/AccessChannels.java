package bot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AccessChannels {
	
	private static List<String> allowedChatIds = new ArrayList<>();
	
	public static void load(String path) throws IOException {
		File file = new File(path);
		Files.readAllLines(file.toPath()).forEach(allowedChatIds::add);
		
		allowedChatIds.forEach(s -> System.out.println("Loaded allowed chat id: "+s));
		
	}
	
	public static boolean allowed(Long chatId) {
		return allowedChatIds.contains(chatId.toString());
	}

}
