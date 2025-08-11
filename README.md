📝 Desafio Técnico: Performance e Análise de Dados via API
-
## Objetivo
API que recebe um arquivo JSON com 100.000 usuários e oferece endpoints performáticos e bem estruturados para análise dos dados.


Endpoints 
---


``POST /users``


* Recebe e armazena os usuários na memória.
* Pode simular um banco de dados em memória.


 ``GET /superusers``

* Filtro: score >= 900 e active = true
* Retorna os dados e o tempo de processamento da requisição.


``GET /top-countries``

* Agrupa os superusuários por país.
* Retorna os 5 países com maior número de superusuários.


``GET /team-insights``

* Agrupa por team.name.
* Retorna: total de membros, líderes, projetos concluídos e % de membros ativos.


``GET /active-users-per-day``

* Conta quantos logins aconteceram por data.
