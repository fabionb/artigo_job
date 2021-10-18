# Obtenção de grande volume de dados em Jobs
Este repositório foi utilizado para a escrita do artigo sobre como trabalhar com uma grande quantidade de dados em jobs.

## Requisitos
Rodar um Oracle com o seguinte comando:
```
docker run -d --name oracle --network host -e ORACLE_ALLOW_REMOTE=true oracleinanutshell/oracle-xe-11g
```

## Execução
Para rodar o código, executar o seguinte comando:
```
./gradlew bootRun
```

## URL's
Para criar registros no banco, utilizar as seguintes URL's:
http://localhost:8081/create/1000

http://localhost:8081/createMillion

Na primeira URL é possível informar quantos registros serão criados.

Para executar o job das maneiras diferentes citadas no artigo, utilizar as seguintes URL's:

http://localhost:8081/limit/job/100

http://localhost:8081/paging/job/100

http://localhost:8081/scroll/job/100/100

http://localhost:8081/jpastream/job/100/100

Nas URL's com 1 parâmetro, o parâmetro informa quantos registros serão lidos por vez e a cada quantos registros será executado o flush / clear da session do Hibernate.

Nas URL's com 2 parâmetros, o parâmetro informa a cada quantos registros será executado o flush / clear da session do Hibernate. O segundo parâmetro informa o fetch size do driver.
