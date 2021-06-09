package bot;

import java.util.Scanner;

import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import it.tdlight.jni.TdApi.AuthorizationState;

public class AuthorizationListener {

	private TelegramClient client;
	
	public AuthorizationListener(TelegramClient client) {
		this.client = client;
	}
	
	public void onAuthorizationState(AuthorizationState authorizationState) {
		switch (authorizationState.getConstructor()) {
			case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
				TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
				parameters.databaseDirectory = "tdlib";
				parameters.useMessageDatabase = true;
				parameters.useSecretChats = true;
				parameters.apiId = 94575;
				parameters.apiHash = "a3406de8d171bb422bb6ddf3bbd800e2";
				parameters.systemLanguageCode = "ru";
				parameters.deviceModel = "Desktop";
				parameters.applicationVersion = "1.0";
				parameters.enableStorageOptimizer = true;
	
				client.send(new TdApi.SetTdlibParameters(parameters), this::onResult);
				System.out.println("[Auth] send params");
				
				break;
			case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
				client.send(new TdApi.CheckDatabaseEncryptionKey(), this::onResult);
				System.out.println("[Auth] send database encryption key");
				break;
			case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR: {
				System.out.println("[Auth] telegram request phone number: ");
				
				String phoneNumber = readFromConsole();
				
				client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), this::onResult);
				break;
			}
			case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR: {
				String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) authorizationState).link;
				System.out.println("[Auth] Please confirm this login link on another device: " + link);
				break;
			}
			case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR: {
				System.out.println("[Auth] telegram request authentication code:");
				
				String code = readFromConsole();
				client.send(new TdApi.CheckAuthenticationCode(code), this::onResult);
				break;
			}
			case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR: {
		
				System.out.println("[Auth] telegram request registration data");
				break;
			}
			case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
		
				System.out.println("[Auth] telegram request password: ");
				String password = readFromConsole();
				client.send(new TdApi.CheckAuthenticationPassword(password), this::onResult);
				
				break;
			}
			case TdApi.AuthorizationStateReady.CONSTRUCTOR:

				System.out.println("[Auth] ready");
				
				
				break;
			case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR:
				
				System.out.println("[Auth] logging out");
				break;
			case TdApi.AuthorizationStateClosing.CONSTRUCTOR:
				
				System.out.println("[Auth] closing");
				break;
			case TdApi.AuthorizationStateClosed.CONSTRUCTOR:
				System.out.println("[Auth] closed");
				break;
			default:
				System.err.println("Unsupported authorization state:\n" + authorizationState);
				break;
		}
	}
	
	private String readFromConsole() {
		Scanner scn = new Scanner(System.in);
		try {
			return scn.nextLine();
		}finally {
			scn.close();
		}
	}
	
	private void onResult(TdApi.Object object) {
		switch (object.getConstructor()) {
			case TdApi.Error.CONSTRUCTOR:
				System.err.println("Receive an error:\n" + object);
				onAuthorizationState(null);
			break;
			case TdApi.Ok.CONSTRUCTOR:
				
			break;
			default:
				System.err.println("Receive wrong response from TDLib:\n" + object);
		}
	}

}
