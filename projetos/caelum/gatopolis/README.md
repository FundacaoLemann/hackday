# 0. Config da máquina

. Instale um mysql
. Crie um banco chamado hackday

# 1. Setup do banco mysql

```
create table ChallengeOutput (id int primary key auto_increment, createdAt datetime, student varchar(14), asserts int, asserts_percentage int);
create index challenge_output_student on ChallengeOutput(student);
create table Student (id int primary key auto_increment, objectId varchar(14), gender varchar(20), school varchar(14),studentgroup varchar(14), birthdate long);
create table Diagnosis (id int primary key auto_increment, level varchar(100), student varchar(20), start_time long);
```

# 2. Descompacte os dados compartilhados com você

```
cd projetos/caelum/gatopolis
unzip ../../../dados/gat*.zip
mv gat* dados
```

# 3. Rode o programa para importar os dados

. Rodar o programa br.com.caelum.lemann.ImportaDados
. Importar os resultados para o mysql (exemplo com root sem senha):

```
mysql -u root hackday < dados/students.txt
mysql -u root hackday < dados/diagnosis.txt
mysql -u root hackday < dados/challenges.txt
```

. Gere tabelas de estatísticas extras

```
create table DayAccess as (select distinct date(createdAt) as dia, student from ChallengeOutput);

create table DiasPorSemana as (select count(dia) / ((datediff(max(dia), min(dia))+1) / 7.0) as dias_por_semana, student from DayAccess group by student);

update Student set gender = 'MALE' where gender = 'Menino';
update Student set gender = 'FEMALE' where gender = 'Menina';

drop table TempoParaAlfabetizar; create table TempoParaAlfabetizar as (select
objectId as student, 
((segundo - primeiro) / 60 / 60 / 24) as tempo
from
(
select s.objectId,
(select min(start_time) from Diagnosis d where level = 'PRE_SYLLABIC' and d.student = s.objectId) as primeiro,
(select min(start_time) from Diagnosis d where level = 'ALPHABETIC' and d.student = s.objectId) as segundo
from Student s
having
primeiro is not null and
segundo is not null
) x having tempo < 365 and tempo > 0);
```

# 4. Rode o algoritmo em R

Configure o usuário e senha, claro. Cada uma das queries a seguir levanta um conjunto de informações passível de análise.

```
library(RMySQL)
con <- dbConnect(MySQL(), dbname = "hackday", user="root", password='')

# dias por semana onde os alunos estudam
sql = 'create table DiasPorSemana as (select count(dia) / ((datediff(max(dia), min(dia))+1) / 7.0) as dias_por_semana, student from DayAccess group by student);'
rs = dbGetQuery(con, sql)
hist(rs$dias_por_semana)

# dia por semana POR genero
sql = "select student, dias_por_semana, if(gender='MALE', '0', '1') gender from DiasPorSemana d join Student s on s.objectId = d.student;"
plot(rs$dias_por_semana, rs$gender)

# dia por semana POR idade em meses
sql = 'select student, dias_por_semana, round(datediff(now(), from_unixtime(birthDate))/30) as idade from DiasPorSemana d join Student s on s.objectId = d.student having idade is not null;'
rs = dbGetQuery(con, sql)
plot(rs$dias_por_semana, rs$idade)

# RESTRICAO: limitando somente a quem terminou alfabetização
# tempo de alfabetização por frequencia semanal
sql = 'select t.student, dias_por_semana, round(datediff(now(), from_unixtime(birthDate))/30) as idade, if(gender="MALE", "0", "1") gender, tempo from DiasPorSemana d join Student s on s.objectId = d.student join TempoParaAlfabetizar as t on t.student=s.objectId;'
rs = dbGetQuery(con, sql)
plot(rs$dias_por_semana, rs$tempo)

# tempo de alfabetização por idade
plot(rs$idade, rs$tempo)


# asserts suspeitos...
sql = 'select hour(createdAt) as hora, avg(asserts_percentage) as media from ChallengeOutput where asserts>0 and asserts_percentage<=100 group by hour(createdAt)'
rs = dbGetQuery(con, sql)
barplot(rs)


# horas do dia onde exercicios sao feitos
sql = 'select hour(createdAt) as hora from ChallengeOutput'
rs = dbGetQuery(con, sql)
hist(rs$hora)
```