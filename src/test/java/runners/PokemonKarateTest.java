package runners;

import com.intuit.karate.junit5.Karate;

class PokemonKarateTest {

    @Karate.Test
    Karate pokemon() {
        return Karate.run("classpath:features/pokemon/pokemon.feature");
    }
}