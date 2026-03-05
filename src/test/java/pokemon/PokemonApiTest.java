package pokemon;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PokemonApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://pokeapi.co";
        RestAssured.basePath = "/api/v2";
    }

    @ParameterizedTest(name = "GET /pokemon/{0} -> name={1}")
    @MethodSource("pokemon.TestData#pokemonCases")
    void shouldGetPokemonById(int id, String expectedName) {
        given()
                .when()
                .get("/pokemon/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(expectedName))
                .body("abilities", not(empty()));
    }

    @ParameterizedTest(name = "GET /pokemon/{1} -> id={0}")
    @MethodSource("pokemon.TestData#pokemonCases")
    void shouldGetPokemonByName(int expectedId, String name) {
        given()
                .when()
                .get("/pokemon/{name}", name)
                .then()
                .statusCode(200)
                .body("id", equalTo(expectedId))
                .body("name", equalTo(name));
    }

    @Test
    void shouldReturn404ForUnknownPokemon() {
        given()
                .when()
                .get("/pokemon/{name}", "no-existe-12345")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldSupportPagination() {
        given()
                .queryParam("limit", 20)
                .queryParam("offset", 0)
                .when()
                .get("/pokemon")
                .then()
                .statusCode(200)
                .body("results", hasSize(20))
                .body("results[0].name", not(isEmptyOrNullString()))
                .body("results[0].url", startsWith("https://pokeapi.co/api/v2/pokemon/"));
    }
}