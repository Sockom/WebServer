
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

public class WebSocketClient implements WebSocket.Listener {
    private WebSocket server = null;
    private Gson gson;
    private ByteBuffer fix;
    private DBAccess dbAccess;

    // Send down-link message to device
    // Must be in Json format according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
    public void sendDownLink(String jsonTelegram) {
        server.sendText(jsonTelegram,true);
    }

    // E.g. url: "wss://iotnet.teracom.dk/app?token=??????????????????????????????????????????????="
    // Substitute ????????????????? with the token you have been given
    public WebSocketClient(String url) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this);
        dbAccess = new DBAccess();
        gson = new Gson();
        fix= ByteBuffer.allocate(5);
        server = ws.join();
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(59000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                server.sendPing(fix);
            }
        }).start();

        System.out.println("past");

    }

    //onOpen()
    public void onOpen(WebSocket webSocket) {
        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");
    }

    //onError()
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    }
    //onClose()
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);
        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }
    //onPing()
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Ping: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    }
    //onPong()
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Pong: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    }
    //onText()
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String realData= data.toString().split("data\":\"")[1];
        CharSequence seq= realData;
        System.out.println(realData);
        String idHex=seq.subSequence(0,4).toString();
        String co2Hex= seq.subSequence(4,8).toString();
        String humidityHex= seq.subSequence(8,12).toString();
        String temperatureHex= seq.subSequence(12,16).toString();
        int id= Integer.parseInt(idHex.trim(),16);
        int co2= Integer.parseInt(co2Hex.trim(),16);
        int humidity= Integer.parseInt(humidityHex.trim(),16);
        int temperature= Integer.parseInt(temperatureHex.trim(),16);
        dbAccess.insertSensorDataTodbo(id,co2,humidity,temperature);
        //System.out.println(indented);
        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }

    private double hexToDouble(String hex){
        long longedHex =parseUnsignedHex(hex);
        double doubledHex=Double.longBitsToDouble(longedHex);
        return doubledHex;

    }

    private static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }
}
