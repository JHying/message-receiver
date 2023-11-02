package tw.hyin.demo.mq.line;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tw.hyin.demo.dto.line.LineMessage;
import tw.hyin.demo.service.LineService;
import org.springframework.stereotype.Component;

/**
 * @author JHying(Rita) on 2023.
 */
@Component
@RequiredArgsConstructor
public class LineEventHandler {

    private final Logger logger = LogManager.getLogger();

    private final LineService lineService;
    private final LineMessagingClient lineMessagingClient;

    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        LineMessage lineMessage = this.retrieveTextMessage(messageEvent);
        String msg = lineMessage.getMessage();
        String lineId = lineMessage.getLineId();
        logger.info("【" + lineId + "】" + msg);
        Message message = lineService.handleChatMessage(msg);
        lineMessagingClient.replyMessage(new ReplyMessage(messageEvent.getReplyToken(), message));
    }

    public void handleStickerEvent(MessageEvent<StickerMessageContent> messageEvent) {
        StickerMessageContent msg = messageEvent.getMessage();
        String lineId = messageEvent.getSource().getUserId();
        logger.info("【" + lineId + "】Sticker (packageId=" + msg.getPackageId() + ", stickerId=" + msg.getStickerId() + ")");
        lineMessagingClient.replyMessage(new ReplyMessage(messageEvent.getReplyToken(), lineService.customReply("This is message from handleStickerEvent.")));
    }

    public void handlePostbackEvent(PostbackEvent postbackEvent) {
        //flex message, quick reply
        PostbackContent content = postbackEvent.getPostbackContent();
        String data = content.getData();
        String lineId = postbackEvent.getSource().getUserId();
        logger.info("【" + lineId + "】" + data);
        lineMessagingClient.replyMessage(new ReplyMessage(postbackEvent.getReplyToken(), lineService.customReply("This is message from handlePostbackEvent.")));
    }

    @SneakyThrows
    private LineMessage retrieveTextMessage(MessageEvent<TextMessageContent> messageEvent) {
        String lineId = messageEvent.getSource().getUserId();
        return LineMessage.builder()
                .lineId(lineId)
                .replyToken(messageEvent.getReplyToken())
                .message(messageEvent.getMessage().getText())
                .userDisplayName(lineMessagingClient.getProfile(lineId).get().getDisplayName())
                .build();
    }

}
