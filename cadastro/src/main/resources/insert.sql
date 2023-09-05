INSERT INTO perfil VALUES (1,'ROLE_ADMIN');
INSERT INTO perfil VALUES (2, 'ROLE_ALUNO');
INSERT INTO perfil VALUES (3, 'ROLE_PROFESSOR');

create table perfil
(
    id bigint primary key not null,
    nome varchar2(20) unique not null
);
