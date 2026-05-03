package lactoBetas.betasDto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetasPutRequest {
    private Long id;
    private String name;
    private String description;
}
