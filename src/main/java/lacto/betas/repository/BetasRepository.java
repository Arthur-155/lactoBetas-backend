package lacto.betas.repository;

import lacto.betas.domain.Betas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetasRepository extends JpaRepository<Betas,Long> {

    List<Betas>findByName(String name);
    List<Betas>findAllByOrderByPositionAsc();
    @Query("SELECT MAX(b.position) FROM Betas b")
    Integer findMaxPosition();
}
