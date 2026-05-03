package lactoBetas.betasDto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetasPostRequest {
    private String name;
    private String description;
}
