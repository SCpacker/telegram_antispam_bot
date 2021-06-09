package bot;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import it.tdlight.jni.TdApi.ChatMemberStatusBanned;
import it.tdlight.jni.TdApi.ChatMemberStatusLeft;
import it.tdlight.jni.TdApi.Message;
import it.tdlight.jni.TdApi.MessageContent;
import it.tdlight.jni.TdApi.MessageDocument;
import it.tdlight.jni.TdApi.MessageText;

public class ChatsListener {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private TelegramClient client;
	
	public ChatsListener(TelegramClient client) {
		this.client = client;
	}
	
	public void onMessage(Message message) {
		MessageContent content = message.content;
		
		log("Get new message: "+message.chatId+" "+message.id+" "+message.content);
		
		if(!AccessChannels.allowed(message.chatId))
			return;
		
		if(content instanceof MessageText) {
			MessageText messageText = (MessageText) content;
			if(textContainsArabic(messageText.text.text)) {
				deleteMessage(message.chatId,message.id);
				
				client.send(new TdApi.SetChatMemberStatus(message.chatId, message.sender, new ChatMemberStatusLeft()), obj -> {
					log("result of delete member "+obj);
				});
			}

		
			
		} else if(content instanceof MessageDocument){
			MessageDocument document = (MessageDocument)content;
			if(textContainsArabic(document.caption.text)) {
				deleteMessage(message.chatId,message.id);
				
				client.send(new TdApi.SetChatMemberStatus(message.chatId, message.sender, new ChatMemberStatusLeft()), obj -> {
					log("result of delete member "+obj);
				});
			}
			
			if(textContainsArabic(document.document.fileName)) {
				deleteMessage(message.chatId,message.id);
				
				client.send(new TdApi.SetChatMemberStatus(message.chatId, message.sender, new ChatMemberStatusLeft()), obj -> {
					log("result of delete member "+obj);
				});
			}
			
			
		}
	}
	
	private void log(String log) {
		String date = DATE_FORMAT.format(new Date());
		System.out.println("["+date+"] "+log);
	}
	
	private void deleteMessage(long chatId, long messageId) {
		client.send(new TdApi.DeleteMessages(chatId, new long[] {messageId}, true), (obj) ->{
			log("result of delete: "+obj);
		});
	}

	private boolean textContainsArabic(String text) {
	    for (char charac : text.toCharArray()) {
	        if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
	            return true;
	        }
	    }
	    return false;
	}
	
}
