package lactoBetas.betasDto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BetasPutResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime data;
    private Integer position;
    private Integer quantity;
}
