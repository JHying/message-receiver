/**
 *
 */
package tw.hyin.demo.dto.chatGPT;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YingHan 2022-03-08
 */
@Data
@AllArgsConstructor
public class ChatRsp implements Serializable {

    private String id;
    private String object;
    private Integer created;
    private List<Choice> choices;
    private ChatUsage usage;

    @Data
    @AllArgsConstructor
    public static class Choice {
        private ChatMsg message; // for chat model
        private Integer index;
        private String finish_reason;
    }

    @Data
    @AllArgsConstructor
    public static class ChatUsage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
    }

}
