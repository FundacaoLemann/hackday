# Como fazer

- Exportar o Excel para CSV. O próprio Excel faz isso.

- Criar a tabela em um MySQL:
```
create table
khan 
(
id int auto_increment primary key,
rede varchar(100),
escola varchar(100),
turma varchar(100),
login varchar(100),
aluno varchar(100),
com_dificuldade int,
precisa_praticar int,
praticado int,
nivel1 int,
nivel2 int,
dominado int,
pontos int,
exerciseminutes double,
videominutes double,
totalminutes double,
semana varchar(100)
)

CREATE TABLE ranking (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  escola varchar(100) DEFAULT NULL,
  a double DEFAULT NULL,
  b double DEFAULT NULL,
  PRIMARY KEY (id)
) 
```

- Para popular essa tabela a partir do CSV exporte, execute a classe `ExtratorKhan`, que está no projeto.
Aponte para o arquivo .csv

- Importe todos os INSERTs para essa base.

# Estatística

Para calcular a regressão linear de cada escola, usando R:

```
# apontar para algum arquivo da sua maquina
arquivo <- "lm.txt"

library(RMySQL)
con <- dbConnect(MySQL(), dbname="hackday_khan", user='root', password='')

sql <- "select distinct escola from khan"
rs <- dbGetQuery(con, sql)
for(i in 1:length(rs$escola)) {
	escola <- rs$escola[i]

	sql <- paste("select * from khan where escola = '", escola, "'", sep="")
	rs2 <- dbGetQuery(con, sql)

	x <- lm(formula = rs2$dominado ~ rs2$exerciseminutes)
	cat(paste(escola, ",", x$coefficients[1], ",", x$coefficients[2], "\n", sep=""), file=arquivo, append=TRUE)
}
```

Pronto. O CSV `lm.txt` foi criado. Agora, basta importar esse CSV dentro da tabela `ranking`.

Para calcular o ranking, basta executar a SQL: 

```
select distinct r.escola, k.rede, b 
from ranking r 
join khan k on k.escola = r.escola 

```
