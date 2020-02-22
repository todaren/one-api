package core.httpclient;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

public class HttpClientFactory {

    /**
     * Method creates instance of Unirest Http Client.
     * We assume that Content-Type in all requests will be "Application/JSON"
     * @return new Http Client
     */
    public static UnirestInstance createHttpClient() {
        UnirestInstance httpClient = Unirest.spawnInstance();
        httpClient.config()
                .verifySsl(false)
                .followRedirects(true)
                .addDefaultHeader("Content-Type", "Application/JSON");
        return httpClient;
    }
}
