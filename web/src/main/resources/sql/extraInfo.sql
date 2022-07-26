-- Agregamos a la tabla reviews la FK para borrar las reviews cuando se borren las peliculas
ALTER TABLE reviews ADD CONSTRAINT FK_REVIEW_FILM_ID FOREIGN KEY (film) REFERENCES films(id) ON DELETE CASCADE;

-- Agregamos a la tabla scores la FK para borrar las scores cuando se borre un usuario
ALTER TABLE scores ADD CONSTRAINT FK_SCORE_USER_ID FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE;

-- Agregamos los roles por defecto
insert into roles (nombre) values ("USER");
insert into roles (nombre) values ("ADMIN");

-- Agregamos un usuario administrador por defecto
insert into users (active,birth_date, creation_date, email, last_login, name, password, surname, username, id_rol) values (1,now(), now(),"admin@admin.com", null, "admin", "$2a$10$Xe7NvUtqM1gF3FKzVf68P.GclLXMawb8Ni7bFtHfjOFqwFgpM4i7S","admin", "admin", (select id from roles where nombre="ADMIN"));

-- Borrado en cascada de la base de datos
drop table scores;
drop table reviews;
drop table rel_person_film;
drop table persons;
drop table films;
drop table users;
drop table roles;