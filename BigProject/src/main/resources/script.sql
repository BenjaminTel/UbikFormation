create table utilisateur (
	id BIGINT PRIMARY KEY NOT NULL,
	nom VARCHAR(255) NOT NULL,
	prenom VARCHAR(255) NOT NULL,
	login VARCHAR(255) NOT NULL,
	pwd VARCHAR(255) NOT NULL
);

create sequence utilisateur_id
increment 1
start 1;

create table auteur (
	id BIGINT PRIMARY KEY NOT NULL,
	nom VARCHAR(255) NOT NULL,
	prenom VARCHAR(255) NOT NULL
);

create sequence auteur_id
increment 1
start 1;

create table livre (
	id BIGINT PRIMARY KEY NOT NULL,
	auteurId BIGINT NOT NULL,
	titre VARCHAR(255) NOT NULL,
	datedeparution TIMESTAMP without time zone,
	CONSTRAINT livre_fk foreign key(auteurId) references auteur(id)
);
create sequence livre_id
increment 1
start 1;

CREATE TABLE tag (
    id BIGINT PRIMARY KEY NOT NULL,
    libelle VARCHAR(255)
);
create sequence tag_id
increment 1
start 1;

CREATE TABLE tag_livre (
    id_livre BIGINT NOT NULL,
    id_tag BIGINT NOT NULL,
    PRIMARY KEY (id_livre, id_tag),
    FOREIGN KEY (id_livre) REFERENCES livre(id),
    FOREIGN KEY (id_tag) REFERENCES tag(id)
);

select * from utilisateur;