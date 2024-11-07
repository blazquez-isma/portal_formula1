-- USE portal_formula1;

-- Tabla Usuario
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `nombreUsuario` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `activo` boolean NOT NULL DEFAULT false,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombreUsuario` (`nombreUsuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Rol
CREATE TABLE `rol` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insertar Roles
INSERT INTO rol (nombre) VALUES ('ROLE_Administrador');
INSERT INTO rol (nombre) VALUES ('ROLE_Responsable');

-- Tabla Usuario_Roles
CREATE TABLE `usuario_roles` (
  `usuarioID` bigint NOT NULL,
  `rolID` bigint NOT NULL,
  PRIMARY KEY (`usuarioID`,`rolID`),
  KEY `rolID` (`rolID`),
  CONSTRAINT `usuario_roles_ibfk_1` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `usuario_roles_ibfk_2` FOREIGN KEY (`rolID`) REFERENCES `rol` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Equipo
CREATE TABLE `equipo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `twitter` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Miembro_Equipo
CREATE TABLE `miembro_equipo` (
  `usuarioID` bigint NOT NULL,
  `equipoID` bigint NOT NULL,
  PRIMARY KEY (`usuarioID`,`equipoID`),
  KEY `equipoID` (`equipoID`),
  CONSTRAINT `miembro_equipo_ibfk_1` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `miembro_equipo_ibfk_2` FOREIGN KEY (`equipoID`) REFERENCES `equipo` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Piloto
CREATE TABLE `piloto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `siglas` varchar(255) NOT NULL,
  `dorsal` int NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `pais` varchar(255) NOT NULL,
  `twitter` varchar(255) DEFAULT NULL,
  `equipoID` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `siglas` (`siglas`),
  UNIQUE KEY `dorsal` (`dorsal`),
  KEY `equipoID` (`equipoID`),
  CONSTRAINT `piloto_ibfk_1` FOREIGN KEY (`equipoID`) REFERENCES `equipo` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Coche
CREATE TABLE `coche` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `codigo` varchar(255) NOT NULL,
  `ERS_curvaLenta` float NOT NULL,
  `ERS_curvaMedia` float NOT NULL,
  `ERS_curvaRapida` float NOT NULL,
  `consumo` float NOT NULL,
  `equipoID` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `equipoID` (`equipoID`),
  CONSTRAINT `coche_ibfk_1` FOREIGN KEY (`equipoID`) REFERENCES `equipo` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Circuito
CREATE TABLE `circuito` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `ciudad` varchar(255) NOT NULL,
  `pais` varchar(255) NOT NULL,
  `trazado` varchar(255) DEFAULT NULL,
  `numeroVueltas` int NOT NULL,
  `longitud` float NOT NULL,
  `curvasLentas` int NOT NULL,
  `curvasMedias` int NOT NULL,
  `curvasRapidas` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Noticia
CREATE TABLE `noticia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permalink` varchar(255) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `texto` varchar(255) NOT NULL,
  `administradorID` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permalink` (`permalink`),
  KEY `administradorID` (`administradorID`),
  CONSTRAINT `noticia_ibfk_1` FOREIGN KEY (`administradorID`) REFERENCES `usuario` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Votacion
CREATE TABLE `votacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permalink` varchar(255) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `fechaLimite` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permalink` (`permalink`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Piloto_Votacion
CREATE TABLE `piloto_votacion` (
  `votacionID` bigint NOT NULL,
  `pilotoID` bigint NOT NULL,
  PRIMARY KEY (`votacionID`,`pilotoID`),
  KEY `pilotoID` (`pilotoID`),
  CONSTRAINT `piloto_votacion_ibfk_1` FOREIGN KEY (`votacionID`) REFERENCES `votacion` (`id`) ON DELETE CASCADE,
  CONSTRAINT `piloto_votacion_ibfk_2` FOREIGN KEY (`pilotoID`) REFERENCES `piloto` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Voto
CREATE TABLE `voto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombreVotante` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pilotoID` bigint NOT NULL,
  `votacionID` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `voto_ibfk_1` (`pilotoID`),
  KEY `voto_ibfk_2` (`votacionID`),
  CONSTRAINT `voto_ibfk_1` FOREIGN KEY (`pilotoID`) REFERENCES `piloto` (`id`) ON DELETE CASCADE,
  CONSTRAINT `voto_ibfk_2` FOREIGN KEY (`votacionID`) REFERENCES `votacion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Simulacion
CREATE TABLE `simulacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo` varchar(255) NOT NULL,
  `resultado` float NOT NULL,
  `cocheID` bigint DEFAULT NULL,
  `circuitoID` bigint DEFAULT NULL,
  `responsableID` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cocheID` (`cocheID`),
  KEY `circuitoID` (`circuitoID`),
  KEY `responsableID` (`responsableID`),
  CONSTRAINT `simulacion_ibfk_1` FOREIGN KEY (`cocheID`) REFERENCES `coche` (`id`) ON DELETE CASCADE,
  CONSTRAINT `simulacion_ibfk_2` FOREIGN KEY (`circuitoID`) REFERENCES `circuito` (`id`) ON DELETE CASCADE,
  CONSTRAINT `simulacion_ibfk_3` FOREIGN KEY (`responsableID`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


