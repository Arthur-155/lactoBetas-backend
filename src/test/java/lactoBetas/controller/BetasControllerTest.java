package lactoBetas.controller;

import lactoBetas.commons.BetasUtils;
import lactoBetas.commons.FileUtils;
import lactoBetas.domain.Betas;
import lactoBetas.repository.BetasRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "lactoBetas")
class BetasControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private List<Betas> betasList;
    @MockitoBean
    private BetasRepository repository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private BetasUtils betaUtils;
    private final String URL = "/v1/betas";

    @BeforeEach
    void init() {
        {
            betasList = betaUtils.getBetasList();
        }
    }

    @DisplayName("GET v1/betas returns a list with all betas")
    @Test
    @Order(1)
    void ListAll_ReturnsAllBetas_whenNameIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(betasList);
        var response = fileUtils.readResourceFile("betas/get-betas-null-name-201.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/betas?name=Arthur returns a list of the found object when name is found")
    @Test
    @Order(2)
    void ListAll_ReturnsFoundObject_whenNameIsFound() throws Exception {
        var name = "Arthur";
        BDDMockito.when(repository.findByName(name)).thenReturn(betasList);
        var response = fileUtils.readResourceFile("betas/get-betas-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/betas?nome=not-found returns a empty list when name is not found")
    @Test
    @Order(3)
    void ListAll_ReturnsEmptyList_whenNameIsNull() throws Exception {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());
        var response = fileUtils.readResourceFile("betas/get-betas-x-nome-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/betas/{id} returns a object when the id is found")
    @Test
    @Order(4)
    void findById_ReturnsAnObject_whenIdIsFound() throws Exception {
        var id = betasList.getFirst().getId();
        var foundBeta = betasList.stream().filter(b -> b.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundBeta);
        var response = fileUtils.readResourceFile("betas/get-betas-byId-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @DisplayName("GET v1/betas/{id} returns a empty list when id is not found")
    @Test
    @Order(5)
    void findById_ReturnsAnEmptyList_whenIdIsNotFound() throws Exception {
        var id = 9999L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("POST v1/betas creates an object")
    @Test
    @Order(6)
    void save_createsAnObject_whenSuccessful() throws Exception {
        var betaToSave = betaUtils.newBetaToSave();
        var request = fileUtils.readResourceFile("betas/post-betas-request-200.json");
        var response = fileUtils.readResourceFile("betas/post-betas-response-201.json");

        BDDMockito.when(repository.findMaxPosition()).thenReturn(null);
        BDDMockito.when(repository.save(ArgumentMatchers.argThat(b -> b.getName()
                .equals(betaToSave.getName())))).thenReturn(betaToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("DELETE v1/betas/{id} deletes an object")
    @Test
    @Order(7)
    void delete_deleteAnObject_whenSuccessful() throws Exception {
        var id = betasList.getFirst().getId();
        var betaToDelete = betasList.stream().filter(b -> b.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(betaToDelete);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("DELETE v1/betas/{id} returns an NotFound when id is not found")
    @Test
    @Order(8)
    void delete_ReturnsNotFound_whenIdIsNotFound() throws Exception {
        var id = 999L;
        var betaToDelete = betasList.stream().filter(b -> b.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(betaToDelete);
        var response = fileUtils.readResourceFile("betas/delete-betas-byId-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("UPDATE v1/betas updates an betas")
    @Test
    @Order(9)
    void update_UpdatesAnBeta_WhenSuccessful() throws Exception {
        var betaToUpdate = betasList.getFirst();
        var betaToDelete = betasList.stream().filter(b -> b.getId().equals(betaToUpdate.getId())).findFirst();
        BDDMockito.when(repository.findById(betaToUpdate.getId())).thenReturn(betaToDelete);
        var request = fileUtils.readResourceFile("betas/put-betas-request-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("UPDATE v1/betas throws an NotFound when id is not found")
    @Test
    @Order(10)
    void update_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        var betaToUpdate = betasList.getFirst();
        var betaIdFound = betasList.stream().filter(b -> b.getId().equals(betaToUpdate.getId())).findFirst();
        BDDMockito.when(repository.findById(betaToUpdate.getId())).thenReturn(betaIdFound);
        var request = fileUtils.readResourceFile("betas/put-betas-request-404.json");
        var response = fileUtils.readResourceFile("betas/put-response-betas-byId-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

}