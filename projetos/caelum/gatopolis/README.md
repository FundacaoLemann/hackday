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