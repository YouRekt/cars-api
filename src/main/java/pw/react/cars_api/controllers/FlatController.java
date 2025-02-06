package pw.react.cars_api.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/flatly")
public class FlatController {
    private final WebClient webClient;

    public FlatController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(System.getenv("API_FLATLY")).build();
    }

    @RequestMapping(value = "/{*endpoint}", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE})
    public Mono<ResponseEntity<Object>> proxyRequest(
            @PathVariable String endpoint,
            @RequestParam MultiValueMap<String, String> params,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestHeader Map<String, String> headers,
            HttpMethod method) {

        return webClient.method(method)
                .uri(uriBuilder -> uriBuilder.path("/" + endpoint).queryParams(params).build())
                .headers(httpHeaders -> httpHeaders.setAll(headers))
                .bodyValue(body != null ? body : Map.of())
                .retrieve()
                .toEntity(Object.class);
    }
}
