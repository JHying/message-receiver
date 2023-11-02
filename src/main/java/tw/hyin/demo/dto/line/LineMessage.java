package tw.hyin.demo.dto.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JHying(Rita) on 2023.
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineMessage {

    private String lineId;
    private String replyToken;
    private String message;
    private String userDisplayName;

}
