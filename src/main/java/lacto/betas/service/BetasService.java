package lacto.betas.service;

import lacto.betas.domain.Betas;
import lacto.betas.repository.BetasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BetasService {

    private final BetasRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Betas>findAll(String name){
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Optional<Betas> findById(Long id){
        return repository.findById(id);
    }

    public List<Betas> findAllPositionOrdered(){
        return repository.findAllByOrderByPositionAsc();
    }

    public Betas save(Betas betas){
        Integer maxPosition = repository.findMaxPosition();
        betas.setPosition(maxPosition == null ? 1 : maxPosition + 1);

        return repository.save(betas);
    }

    public void rotateQueue(){
        List<Betas> list = repository.findAllByOrderByPositionAsc();

        if (list.isEmpty()){return;}

        Betas first = list.getFirst();//posicao 1, o topo

        for(int i = 1; i < list.size(); i++){
            list.get(i).setPosition(i);//trocando index, por posicao
        }

        first.setPosition(list.size());//topo vai pro final

        repository.saveAll(list);
//        Betas newTop = repository.findAllByOrderByPositionAsc().getFirst();
//        webHook(newTop);
    }

//    public void webHook(Betas betas){
//        if(betas.getUrl() != null && !betas.getUrl().isBlank()){
//            restTemplate.postForObject(betas.getUrl(),betas,String.class);
//        }
//    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public Betas update(Betas betas){
        Betas updated = repository.findById(betas.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        updated.setName(betas.getName());
        updated.setDescription(betas.getDescription());
        updated.setData(betas.getData());

        return repository.save(updated);
    }

}



