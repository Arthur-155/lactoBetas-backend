package lactoBetas.service;


import lactoBetas.commons.BetasUtils;
import lactoBetas.domain.Betas;
import lactoBetas.repository.BetasRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BetasServiceTest {
    @InjectMocks
    private BetasService service;
    @Mock
    BetasRepository repository;
    private List<Betas>betas = new ArrayList<>();
    @InjectMocks
    private BetasUtils betasUtils;

    @BeforeEach
    void init(){
        betas = betasUtils.getBetasList();
    }

    @DisplayName("findAll returns a list with all betas")
    @Test
    @Order(1)
    void FindAll_ReturnsAllBetas_whenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(betas);
        var expectedValue = service.findAll(null);
        Assertions.assertThat(expectedValue).isNotNull().hasSameElementsAs(betas);
    }

    @DisplayName("findAll returns a list of the found object when name is found")
    @Test
    @Order(2)
    void FindAll_ReturnsFoundObject_whenNameIsFound() {
        var foundObject = betas.getFirst();
        var expectedBetasFound = Collections.singletonList(foundObject);

        BDDMockito.when(repository.findByName(foundObject.getName())).thenReturn(expectedBetasFound);

        var expectedValue = service.findAll(foundObject.getName());
        Assertions.assertThat(expectedValue).containsAll(expectedBetasFound);
    }

    @DisplayName("findAll returns a empty list when name is null")
    @Test
    @Order(3)
    void FindAll_ReturnsEmptyList_whenNameIsNull() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());
        var expectedValue = service.findAll(name);
        Assertions.assertThat(expectedValue).isNotNull().isEmpty();
    }

    @DisplayName("find by id returns a object when the id is found")
    @Test
    @Order(4)
    void findById_ReturnsAnObject_whenIdIsFound() {
        var objectReceived = betas.getFirst();

        BDDMockito.when(repository.findById(objectReceived.getId())).thenReturn(Optional.of(objectReceived));

        var expectedValue = service.findByIdOrThrowNotFound(objectReceived.getId());

        Assertions.assertThat(expectedValue).isEqualTo(objectReceived);
    }

    @DisplayName("find by id returns a empty list when id is not found")
    @Test
    @Order(5)
    void findById_ReturnsAnEmptyList_whenIdIsNotFound() {
        var objectReceived = betas.getFirst();

        BDDMockito.when(repository.findById(objectReceived.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(objectReceived.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @DisplayName("save creates an object")
    @Test
    @Order(6)
    void save_createsAnObject_whenSuccessful() {
        var betaToSave = betasUtils.newBetaToSave();

        BDDMockito.when(repository.save(betaToSave)).thenReturn(betaToSave);

        var betaSaved = service.save(betaToSave);

        Assertions.assertThat(betaSaved).isEqualTo(betaSaved).hasNoNullFieldsOrProperties();
    }

    @DisplayName("delete deletes a object")
    @Test
    @Order(7)
    void delete_deleteAnObject_whenSuccessful() {
        var betaToDelete = betas.getFirst();

        BDDMockito.when(repository.findById(betaToDelete.getId())).thenReturn(Optional.of(betaToDelete));

        service.deleteByIdOrThrowNotFound(betaToDelete.getId());
        var list = repository.findAll();

        Assertions.assertThat(list).doesNotContain(betaToDelete);
    }

    @DisplayName("delete returns an exception when id is not found")
    @Test
    @Order(8)
    void delete_ReturnsAnException_whenIdIsNotFound() {
        var betaToDelete = betas.getFirst();

        BDDMockito.when(repository.findById(betaToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(betaToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @DisplayName("update updates an beta")
    @Test
    @Order(9)
    void update_UpdatesAnBeta_WhenSuccessful() {
        var betaToUpdate = betas.getFirst();
        betaToUpdate.setName("Gabs");

        BDDMockito.when(repository.findById(betaToUpdate.getId())).thenReturn(Optional.of(betaToUpdate));
        BDDMockito.when(repository.save(ArgumentMatchers.any(Betas.class))).thenReturn(betaToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.updateOrThrowNotFound(betaToUpdate));
    }

    @DisplayName("update throws an exception when id is not found")
    @Test
    @Order(10)
    void update_ThrowsAnException_WhenIdIsNotFound() {
        var betaToUpdate = betas.getFirst();
        BDDMockito.when(repository.findById(betaToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.updateOrThrowNotFound(betaToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @DisplayName("rotateQueue rotates the queue")
    @Test
    @Order(11)
    void RotateQueue_RotatesTheQueue_WhenSuccessful() {
        BDDMockito.when(repository.findAllByOrderByPositionAsc()).thenReturn(betas);

        var firstBefore = betas.getFirst();
        var expectedPosition = betas.size();
        var expectedQuantity = firstBefore.getQuantity() + 1;

        service.rotateQueue();

        Assertions.assertThat(firstBefore.getPosition()).isEqualTo(expectedPosition);
        Assertions.assertThat(firstBefore.getQuantity()).isEqualTo(expectedQuantity);
        Assertions.assertThat(firstBefore.getData()).isNotNull();
    }
}