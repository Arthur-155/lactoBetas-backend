package lactoBetas.controller;

import lactoBetas.betasDto.request.BetasPostRequest;
import lactoBetas.betasDto.request.BetasPutRequest;
import lactoBetas.betasDto.response.BetasGetResponse;
import lactoBetas.betasDto.response.BetasPostResponse;
import lactoBetas.domain.Betas;
import lactoBetas.mapper.BetasMapper;
import lactoBetas.service.BetasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("v1/betas")
public class BetasController {

    private final BetasService service;
    private final BetasMapper mapper;

    @GetMapping
    public ResponseEntity<List<BetasGetResponse>>findAll(@RequestParam(required = false)String name){
        var betas = service.findAll(name);
        var betasGetResponseList = mapper.toBetasGetResponseList(betas);
        return ResponseEntity.status(HttpStatus.OK).body(betasGetResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetasGetResponse> findById(@PathVariable Long id){
        var getBetasByIdResponse = service.findByIdOrThrowNotFound(id);
        var response = mapper.toBetasGetResponse(getBetasByIdResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BetasPostResponse> save(@RequestBody BetasPostRequest betasPostRequest){
        var request = mapper.toBetas(betasPostRequest);
        var savedBeta = service.save(request);
        var response = mapper.toBetasPostResponse(savedBeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rotateQueue")
    public ResponseEntity<Void> rotateQueue(){
        service.rotateQueue();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ordered")
    public ResponseEntity<List<BetasGetResponse>>findAllPositionOrdered(){
        var betas = service.findAllPositionOrdered();
        var response = mapper.toBetasGetResponseList(betas);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteByIdOrThrowNotFound(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody BetasPutRequest request){
        var betasToUpdate = mapper.toPutBetas(request);
        service.updateOrThrowNotFound(betasToUpdate);
        return ResponseEntity.noContent().build();
    }
}
