package goldenshadow.gearviewspoofer.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ItemData {


    public static ItemData INSTANCE;
    private JsonObject data = null;

    public static void init() {
        INSTANCE = new ItemData();
    }

    public ItemData() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.wynncraft.com/v3/item/database?fullResult"))
                .build();
        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).completeOnTimeout(null, 10, TimeUnit.SECONDS);

        //possible race condition but pretty unlikely that the player sends data before the api has responded
        future.thenAccept(response -> {
            httpClient.close();
            if (response == null) return; // API is down or something :(
            data = JsonParser.parseString(response.body()).getAsJsonObject();
        });
    }

    public JsonObject getItem(String name) {
        if (data == null) return null;
        return data.getAsJsonObject(name);
    }

    public int getStatValue(JsonObject itemObject, String statKey, Quality quality) {
        if (quality == Quality.DEFAULT) throw new IllegalArgumentException("Quality has to either be PERFECT or DEFECTIVE!");

        JsonObject identifications = itemObject.getAsJsonObject("identifications");
        JsonElement stat = identifications.get(statKey);
        if (stat.isJsonPrimitive()) return stat.getAsInt();
        JsonObject minMaxObject = stat.getAsJsonObject();
        return quality == Quality.PERFECT ? minMaxObject.get("max").getAsInt() : minMaxObject.get("min").getAsInt();
    }

    public boolean isPositiveValue(JsonObject itemObject, String statKey, Quality quality) {
        if (quality == Quality.DEFAULT) throw new IllegalArgumentException("Quality has to either be PERFECT or DEFECTIVE!");

        JsonObject identifications = itemObject.getAsJsonObject("identifications");
        JsonElement stat = identifications.get(statKey);
        if (stat.isJsonPrimitive()) return stat.getAsInt() > 0;
        JsonObject minMaxObject = stat.getAsJsonObject();
        return quality == Quality.PERFECT ? (minMaxObject.get("max").getAsInt() > 0) : (minMaxObject.get("min").getAsInt() > 0);
    }
}
