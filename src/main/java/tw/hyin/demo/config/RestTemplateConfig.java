/**
 *
 */
package tw.hyin.demo.config;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tw.hyin.java.utils.JsonUtil;
import tw.hyin.java.utils.http.RestTemplateUtil;

/**
 * @author YingHan 2022-03-04
 */
@Configuration
public class RestTemplateConfig extends MappingJackson2HttpMessageConverter {

    @Value("${openai.chatgpt.chat.key}")
    private String openaiApiKey;

    @Value("${line.bot.channel-token}")
    private String channelAccessToken;

    public RestTemplateConfig() {
        //RestClientException: Could not extract response
        //no suitable HttpMessageConverter found for response type... content type [text/plain;charset=UTF-8]
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);// ms
        factory.setConnectTimeout(15000);// ms
        return factory;
    }

    @SneakyThrows
    @Bean
    @Qualifier("openaiRestTemplate")
    public RestTemplate openaiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(RestTemplateUtil.getIgnoreSSLFactory());//PKIX path building failed
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @SneakyThrows
    @Bean
    @Qualifier("lineRestTemplate")
    public RestTemplate lineRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(RestTemplateUtil.getIgnoreSSLFactory());//PKIX path building failed
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + channelAccessToken);
            return execution.execute(request, body);
        });
        //add StringHttpMessageConverter to rest template's message converter with charset UTF-8
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

}
