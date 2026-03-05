function fn() {
  var config = {
    baseUrl: 'https://pokeapi.co/api/v2'
  };

  // opcional: soportar ambientes
  var env = karate.env;
  karate.log('karate.env =', env);

  if (env == 'dev') {
    config.baseUrl = 'https://pokeapi.co/api/v2';
  }

  return config;
}