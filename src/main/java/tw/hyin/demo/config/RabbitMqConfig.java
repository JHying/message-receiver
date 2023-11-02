/**
 *
 */
package tw.hyin.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author YingHan
 * @Description
 * @since 2022-03-23
 */
@Configuration
public class RabbitMqConfig {

    public static final String LINE_QUEUE = "lineMessage.queue";

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        //rabbitMQ will use getRequiredMessageConverter().toMessage by default if the object not instance of Message
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
