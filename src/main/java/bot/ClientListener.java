package bot;

import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import it.tdlight.jni.TdApi.Message;

public class ClientListener {

	private AuthorizationListener authorizationListener;
	private ChatsListener chatsListener;
	
	private TelegramClient client;
	
	public ClientListener(TelegramClient client) {
		this.client = client;
	}
	
	public void onUpdate(TdApi.Object object) {
		switch (object.getConstructor()) {
			case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
				if(authorizationListener == null)
					authorizationListener = new AuthorizationListener(client);
				
				authorizationListener.onAuthorizationState(((TdApi.UpdateAuthorizationState) object).authorizationState);
				break;
			case TdApi.UpdateNewMessage.CONSTRUCTOR:
				if(chatsListener == null)
					chatsListener = new ChatsListener(client);
				
				Message message = ((TdApi.UpdateNewMessage) object).message;
				chatsListener.onMessage(message);
				
				break;
		}
	}
	
	public void onException(Throwable e) {
		e.printStackTrace();
	}
	
}
