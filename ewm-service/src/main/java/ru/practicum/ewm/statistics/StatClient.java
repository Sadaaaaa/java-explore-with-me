package ru.practicum.ewm.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.util.EndpointHit;

@Service
public class StatClient extends BaseClient {

    private static final String LOCALHOST = "http://localhost:9090";

    @Autowired
    public StatClient(@Value(LOCALHOST) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> sendStatistic(EndpointHit endpointHit) {
        String path = "/hit";
        return post(path, endpointHit);
    }
}
