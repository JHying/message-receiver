import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.junit.jupiter.api.Test;
import tw.hyin.java.utils.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author H-yin on 2021.
 */
public class UnitTest {

    @Test
    public void lineMsgJson() {
        try {
            String json = "{\\\"type\\\":\\\"message\\\",\\\"replyToken\\\":\\\"d84ba73sh8e4sdb1efewrffb9e2s\\\",\\\"source\\\":{\\\"type\\\":\\\"user\\\",\\\"userId\\\":\\\"setdsggwrwaqe\\\",\\\"senderId\\\":\\\"awewafewr\\\"},\\\"message\\\":{\\\"type\\\":\\\"text\\\",\\\"id\\\":\\\"3432534341\\\",\\\"text\\\":\\\"test\\\",\\\"emojis\\\":null,\\\"mention\\\":null},\\\"timestamp\\\":1698741096.131000000,\\\"mode\\\":\\\"active\\\",\\\"webhookEventId\\\":\\\"dsf435qre351\\\",\\\"deliveryContext\\\":{\\\"isRedelivery\\\":false}}";
            Event event = JsonUtil.jsonToPojo(json, Event.class);
            System.out.println(event);

            assertTrue(event instanceof MessageEvent);

            MessageEvent<?> messageEvent1 = (MessageEvent<?>) event;
            System.out.println(messageEvent1);

            assertTrue(messageEvent1.getMessage() instanceof TextMessageContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
