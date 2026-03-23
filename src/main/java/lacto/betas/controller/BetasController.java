package lacto.betas.controller;

import lacto.betas.domain.Betas;
import lacto.betas.service.BetasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("v1/betas")
public class BetasController {

    private final BetasService service;

    @GetMapping
    public List<Betas>findAll(@RequestParam(required = false)String name){
        return service.findAll(name);
    }

    @GetMapping("/{id}")
    public Optional<Betas> findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping
    public Betas save(@RequestBody Betas betas){
        return service.save(betas);
    }

    @PostMapping("/rotateQueue")
    public void rotateQueue(){
        service.rotateQueue();
    }

    @GetMapping("/ordered")
    public List<Betas>findAllPositionOrdered(){
        return service.findAllPositionOrdered();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @PutMapping
    public Betas update(@RequestBody Betas betas){
        return service.update(betas);
    }
}
