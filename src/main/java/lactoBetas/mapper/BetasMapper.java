package lactoBetas.mapper;

import lactoBetas.betasDto.request.BetasPostRequest;
import lactoBetas.betasDto.request.BetasPutRequest;
import lactoBetas.betasDto.response.BetasGetResponse;
import lactoBetas.betasDto.response.BetasPostResponse;
import lactoBetas.domain.Betas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BetasMapper {
    @Mapping(target = "data", expression = "java(java.time.LocalDateTime.now())")
    Betas toBetas(BetasPostRequest betasPostRequest);

    BetasPostResponse toBetasPostResponse(Betas betas);

    Betas toPutBetas(BetasPutRequest betasPutRequest);

    BetasGetResponse toBetasGetResponse(Betas betas);

    List<BetasGetResponse>toBetasGetResponseList(List<Betas>betas);
}
