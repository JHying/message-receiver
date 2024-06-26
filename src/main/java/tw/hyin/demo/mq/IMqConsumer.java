/**
 *
 */
package tw.hyin.demo.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author YingHan
 *
 * @Description
 */
public interface IMqConsumer<T> {

    public void receiveMessage(T receiveObj, Message message, Channel channel);

}
