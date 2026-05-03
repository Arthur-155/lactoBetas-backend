package lactoBetas.service;

import lactoBetas.betasExceptions.NotFoundException;
import lactoBetas.domain.Betas;
import lactoBetas.repository.BetasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BetasService {

    private final BetasRepository repository;

    public List<Betas>findAll(String name){
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Betas findByIdOrThrowNotFound(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id não encontrado!"));
    }

    public List<Betas> findAllPositionOrdered(){
        return repository.findAllByOrderByPositionAsc();
    }

    public Betas save(Betas betas){
        Integer maxPosition = repository.findMaxPosition();
        betas.setPosition(maxPosition == null ? 1 : maxPosition + 1);

        return repository.save(betas);
    }

    public void deleteByIdOrThrowNotFound(Long id){
        var betaToDelete = findByIdOrThrowNotFound(id);
        repository.delete(betaToDelete);
    }

    public Betas updateOrThrowNotFound(Betas betas){
        Betas betaToUpdate = findByIdOrThrowNotFound(betas.getId());

        Betas updated = Betas.builder()
                .id(betaToUpdate.getId())
                .name(betas.getName())
                .description(betas.getDescription())
                .data(betaToUpdate.getData())
                .position(betaToUpdate.getPosition())
                .build();
        return repository.save(updated);
    }

    public void rotateQueue(){
        List<Betas> list = repository.findAllByOrderByPositionAsc();

        if (list.isEmpty()){return;}

        Betas first = list.getFirst();

        for(int i = 1; i < list.size(); i++){
            list.get(i).setPosition(i);
        }

        first.setPosition(list.size());
        first.setQuantity(first.getQuantity() +1);
        first.setData(LocalDateTime.now());

        repository.saveAll(list);
    }

}



