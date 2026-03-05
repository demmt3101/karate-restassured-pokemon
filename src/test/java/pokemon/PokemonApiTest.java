package pokemon;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API Testing")
@Feature("PokeAPI")
@Owner("QA")
public class PokemonApiTest {

    private static RequestSpecification baseSpec;

    @BeforeAll
    static void setup() {
        baseSpec = new RequestSpecBuilder()
                .setBaseUri("https://pokeapi.co")
                .setBasePath("/api/v2")
                .addFilter(new AllureRestAssured())   // adjunta request/response en Allure
                .build();
    }

    @Story("Consultar Pokémon por ID")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /pokemon/{id} - debe retornar el Pokémon correcto")
    @ParameterizedTest(name = "ID={0} -> name={1}")
    @MethodSource("pokemon.TestData#pokemonCases")
    void shouldGetPokemonById(int id, String expectedName) {
        given()
                .spec(baseSpec)
                .pathParam("id", id)
                .when()
                .get("/pokemon/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(expectedName))
                .body("height", greaterThan(0))
                .body("weight", greaterThan(0))
                .body("abilities", not(empty()));
    }

    @Story("Consultar Pokémon por nombre")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /pokemon/{name} - debe retornar el Pokémon correcto")
    @ParameterizedTest(name = "name={1} -> ID={0}")
    @MethodSource("pokemon.TestData#pokemonCases")
    void shouldGetPokemonByName(int expectedId, String name) {
        given()
                .spec(baseSpec)
                .pathParam("name", name)
                .when()
                .get("/pokemon/{name}")
                .then()
                .statusCode(200)
                .body("id", equalTo(expectedId))
                .body("name", equalTo(name));
    }

    @Story("Consistencia entre endpoints")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Cross-check: GET por ID -> nombre -> GET por nombre debe devolver el mismo ID")
    @ParameterizedTest(name = "Cross-check ID={0}")
    @MethodSource("pokemon.TestData#pokemonCases")
    void shouldBeConsistentAcrossIdAndName(int id, String expectedName) {
        String name =
                given()
                        .spec(baseSpec)
                        .pathParam("id", id)
                        .when()
                        .get("/pokemon/{id}")
                        .then()
                        .statusCode(200)
                        .body("name", equalTo(expectedName))
                        .extract()
                        .path("name");

        given()
                .spec(baseSpec)
                .pathParam("name", name)
                .when()
                .get("/pokemon/{name}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Story("Manejo de errores")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("GET /pokemon/{name} - debe retornar 404 si no existe")
    @Test
    void shouldReturn404ForUnknownPokemon() {
        given()
                .spec(baseSpec)
                .pathParam("name", "no-existe-12345")
                .when()
                .get("/pokemon/{name}")
                .then()
                .statusCode(404);
    }

    @Story("Paginación")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /pokemon?limit&offset - debe retornar lista paginada")
    @Test
    void shouldSupportPagination() {
        given()
                .spec(baseSpec)
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