package lactoBetas.betasDto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class BetasPostResponse {
    private Long id;
    private String description;
    private LocalDateTime data;
    private Integer position;
    private Integer quantity;
}
