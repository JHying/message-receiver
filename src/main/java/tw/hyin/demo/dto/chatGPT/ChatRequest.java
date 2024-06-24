/**
 *
 */
package tw.hyin.demo.dto.chatGPT;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YingHan 2022-03-08
 */
@Data
@AllArgsConstructor
public class ChatRequest implements Serializable {

    private String model; // for chat model
    private List<ChatMsg> messages; //for chat model

    public ChatRequest(String role) {
        this.model = "gpt-4-turbo";
        this.messages = new ArrayList<>();
        this.messages.add(new ChatMsg("system", role));
    }

    public ChatRequest(String role, String prompt) {
        this.model = "gpt-4-turbo";
        this.messages = new ArrayList<>();
        this.messages.add(new ChatMsg("system", role));
        this.messages.add(new ChatMsg("user", prompt));
    }

}
