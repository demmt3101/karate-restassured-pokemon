package pokemon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.provider.Arguments;
import pokemon.model.PokemonCase;
import pokemon.model.PokemonCases;

import java.io.InputStream;
import java.util.stream.Stream;

public class TestData {

    private static final String CASES_PATH = "/data/pokemon-cases.json";

    public static Stream<Arguments> pokemonCases() {
        try (InputStream is = TestData.class.getResourceAsStream(CASES_PATH)) {
            if (is == null) {
                throw new IllegalStateException("No se encontró el recurso: " + CASES_PATH +
                        " (verifica que exista en src/test/resources/data/pokemon-cases.json)");
            }

            ObjectMapper mapper = new ObjectMapper();
            PokemonCases data = mapper.readValue(is, PokemonCases.class);

            return data.getCases().stream()
                    .map(c -> Arguments.of(c.getId(), c.getName()));

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo " + CASES_PATH + ": " + e.getMessage(), e);
        }
    }
}