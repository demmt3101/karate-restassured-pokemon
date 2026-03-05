package pokemon;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestData {
    public static Stream<Arguments> pokemonCases() {
        return Stream.of(
                Arguments.of(1, "bulbasaur"),
                Arguments.of(4, "charmander"),
                Arguments.of(7, "squirtle")
        );
    }
}