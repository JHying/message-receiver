package tw.hyin.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tw.hyin.demo.dto.chatGPT.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tw.hyin.java.utils.JsonUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JHying(Rita) on 2023.
 * @description
 */
@Service
@RequiredArgsConstructor
public class MsgService {

    private final RestTemplate openaiRestTemplate;

    @Value("${openai.chatgpt.chat.url}")
    private String apiUrl;

    @Value("${openai.chatgpt.image.url}")
    private String imageUrl;

    @SneakyThrows
    public String callChatGPT(List<String> prompts, String role) {
        // create a request
        ChatRequest request = new ChatRequest(role);
        for (String prompt : prompts) {
            request.getMessages().add(new ChatMsg("user", prompt));
        }
        // call the API
        String repStr = openaiRestTemplate.postForObject(apiUrl, request, String.class);
        ChatRsp response = JsonUtil.jsonToPojo(repStr, ChatRsp.class);
        if (response == null || response.getChoices().isEmpty()) {
            return "No response.";
        } else {
            List<String> choices = response.getChoices().stream()
                    .map(ChatRsp.Choice::getMessage)
                    .filter(message -> message.getRole().equals("assistant"))
                    .map(c -> c.getContent().replaceAll("\n\n", "\r\n")).collect(Collectors.toList());
            return String.join("\r\n", choices);
        }
    }

    @SneakyThrows
    public String chatGPTImage(ChatPrompt prompt) {
        // create a request
        ImageRequest request = new ImageRequest(prompt.getText());
        // call the API
        String repStr = openaiRestTemplate.postForObject(imageUrl, request, String.class);
        ImageRsp response = JsonUtil.jsonToPojo(repStr, ImageRsp.class);
        if (response == null || response.getData().isEmpty()) {
            return "No response.";
        } else {
            List<String> choices = response.getData().stream()
                    .map(ImageRsp.ChatData::getUrl).collect(Collectors.toList());
            return String.join("\r\n", choices);
        }
    }

}
