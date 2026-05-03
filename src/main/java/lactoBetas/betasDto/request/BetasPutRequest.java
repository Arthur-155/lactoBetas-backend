package lactoBetas.betasDto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BetasPutRequest {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
}
