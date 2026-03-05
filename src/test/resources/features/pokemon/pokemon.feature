Feature: Pokemon API

  Background:
    * url baseUrl

  Scenario: Consultar pokemon 1 y validar nombre bulbasaur
    Given path 'pokemon', 1
    When method get
    Then status 200
    And match response.name == 'bulbasaur'

  Scenario: Consultar pokemon 4 y validar nombre charmander
    Given path 'pokemon', 4
    When method get
    Then status 200
    And match response.name == 'charmander'

  Scenario: Consultar pokemon 7 y validar nombre squirtle
    Given path 'pokemon', 7
    When method get
    Then status 200
    And match response.name == 'squirtle'