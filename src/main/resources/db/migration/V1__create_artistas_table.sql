CREATE TABLE IF NOT EXISTS artistas (
        id INT NOT NULL AUTO_INCREMENT,
        nome varchar(200) NOT NULL,
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS album (
                       id bigint not null auto_increment,
                       nome varchar(255) not null,
                       artista_id bigint,
                       primary key (id)
) engine=InnoDB;

alter table artistas
    modify column id bigint not null auto_increment;

alter table album
    add constraint FKr8bcyrnf3a81bfdw19heor0bu
        foreign key (artista_id)
            references artistas (id)