
DROP TABLE IF EXISTS regionais;
DROP TABLE IF EXISTS album_capas;
DROP TABLE IF EXISTS album;
DROP TABLE IF EXISTS artistas;


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
            references artistas (id);

CREATE TABLE IF NOT EXISTS album_capas (
      album_id BIGINT NOT NULL,
      nome VARCHAR(255),
      FOREIGN KEY (album_id) REFERENCES album(id)
);

CREATE TABLE IF NOT EXISTS regionais (
       id_interno BIGINT AUTO_INCREMENT PRIMARY KEY,
       id_origem INTEGER NOT NULL,
       nome VARCHAR(200) NOT NULL,
       ativo BOOLEAN NOT NULL DEFAULT TRUE,
       criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       INDEX idx_regional_ativo (id_origem, ativo)
);