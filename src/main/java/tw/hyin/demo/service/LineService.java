package tw.hyin.demo.service;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tw.hyin.java.utils.JsonUtil;
import tw.hyin.java.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JHying(Rita) on 2023.
 * @description
 */
@Service
@Setter
@RequiredArgsConstructor
public class LineService {

    private final MsgService msgService;
    private final RestTemplate lineRestTemplate;
    private final static String LINE_PUSH_URL = "https://api.line.me/v2/bot/message/push";

    @SneakyThrows
    @Scheduled(cron = "0 0 12 ? * THU", zone = "Asia/Taipei")
    private void linePush() {
        PushMessage pushRequest = new PushMessage("YOUR_LINE_ID", new TextMessage("hellow world."));
        Log.info("【Push Completed】- " + lineRestTemplate.postForObject(LINE_PUSH_URL, pushRequest, String.class));
    }

    public TextMessage handleChatMessage(String msg) {
        if (msg.contains("Hi ChatGPT")) {
            List<String> prompts = new ArrayList<>();
            prompts.add("Please response: This is the message from ChatGPT.");
            return this.askGPT(prompts);
        } else {
            return this.defaultReply();
        }
    }

    @SneakyThrows
    public TextMessage defaultReply() {
        String defaultAns = """
                {
                    "type": "text",
                    "text": "$Thanks for your text message!",
                    "emojis": [
                      {
                        "index": 0,
                        "productId": "5ac1bfd5040ab15980c9b435",
                        "emojiId": "039"
                      }
                    ]
                }
                """;
        return JsonUtil.jsonToPojo(defaultAns, TextMessage.class);
    }

    public TextMessage customReply(String reply) {
        return new TextMessage(reply);
    }

    public TextMessage askGPT(List<String> messages) {
        String rspFromGpt = msgService.callChatGPT(messages, "You are a helpful assistant.");
        return new TextMessage(rspFromGpt);
    }

}
