package lactoBetas.commons;

import lactoBetas.domain.Betas;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BetasUtils {

    private static final LocalDateTime DATA_TESTE = LocalDateTime.of(2026, 5, 3, 0, 0);

    public List<Betas> getBetasList() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        var Arthur = Betas.builder().id(1L).name("Arthur").description("teste1").position(1).quantity(2).data(DATA_TESTE).build();
        var Gabriel = Betas.builder().id(1L).name("Gabriel").description("teste2").position(2).quantity(4).data(DATA_TESTE).build();
        var Gustavo = Betas.builder().id(1L).name("Gustavo").description("teste3").position(3).quantity(6).data(DATA_TESTE).build();
        return new ArrayList<>(List.of(Arthur, Gabriel, Gustavo));
    }

    public Betas newBetaToSave() {
        return Betas.builder().id(1L).name("Arthur Durate").description("Natura").position(4).quantity(9).data(DATA_TESTE).build();
    }
}
