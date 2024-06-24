/**
 *
 */
package tw.hyin.demo.mq.line;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tw.hyin.demo.config.RabbitMqConfig;
import tw.hyin.demo.mq.IMqConsumer;
import tw.hyin.java.utils.Log;

/**
 * @author YingHan
 * @Description
 * @since 2022-03-24
 */
@Component
@RequiredArgsConstructor
public class LineEventReceiver implements IMqConsumer<Event> {

    private final LineEventHandler lineEventHandler;

    @Override
    @SneakyThrows
    @RabbitListener(queues = {RabbitMqConfig.LINE_QUEUE}, concurrency = "5")
    public void receiveMessage(Event receiveObj, Message message, Channel channel) {
        Log.info("Received message >> ID: {}", message.getMessageProperties().getMessageId());
        try {
            this.eventHandler(receiveObj);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            Log.error("【Error Occurred】" + e.getMessage());
        }
    }

    @SneakyThrows
    private void eventHandler(Event event) {
        if (event instanceof MessageEvent) {
            //messageEvent
            MessageEvent<?> messageEvent = (MessageEvent<?>) event;
            if (messageEvent.getMessage() instanceof TextMessageContent) {
                //text
                lineEventHandler.handleTextEvent((MessageEvent<TextMessageContent>) event);
            } else if (messageEvent.getMessage() instanceof StickerMessageContent) {
                //sticker
                lineEventHandler.handleStickerEvent((MessageEvent<StickerMessageContent>) event);
            }
            return;
        } else if (event instanceof PostbackEvent) {
            //postbackEvent
            PostbackEvent postbackEvent = (PostbackEvent) event;
            lineEventHandler.handlePostbackEvent(postbackEvent);
            return;
        }
        Log.error("【Unknown Message Type】" + event.toString());
    }

}
