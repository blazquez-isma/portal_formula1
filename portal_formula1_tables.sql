CREATE TABLE `circuito` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `ciudad` varchar(100) NOT NULL,
  `pais` varchar(100) NOT NULL,
  `trazado` varchar(255) DEFAULT NULL,
  `numeroVueltas` int NOT NULL,
  `longitud` float NOT NULL,
  `curvasLentas` int NOT NULL,
  `curvasMedias` int NOT NULL,
  `curvasRapidas` int NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `coche` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `ERS_curvaLenta` float NOT NULL,
  `ERS_curvaMedia` float NOT NULL,
  `ERS_curvaRapida` float NOT NULL,
  `consumo` float NOT NULL,
  `equipoID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `equipoID` (`equipoID`),
  CONSTRAINT `coche_ibfk_1` FOREIGN KEY (`equipoID`) REFERENCES `equipo` (`ID`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `equipo` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `twitter` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `miembro_equipo` (
  `usuarioID` int NOT NULL,
  `equipoID` int NOT NULL,
  PRIMARY KEY (`usuarioID`,`equipoID`),
  KEY `equipoID` (`equipoID`),
  CONSTRAINT `miembro_equipo_ibfk_1` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `miembro_equipo_ibfk_2` FOREIGN KEY (`equipoID`) REFERENCES `equipo` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `noticia` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `permalink` varchar(100) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `texto` text NOT NULL,
  `administradorID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `permalink` (`permalink`),
  KEY `administradorID` (`administradorID`),
  CONSTRAINT `noticia_ibfk_1` FOREIGN KEY (`administradorID`) REFERENCES `usuario` (`ID`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `piloto` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `siglas` char(3) NOT NULL,
  `dorsal` int NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `pais` varchar(100) NOT NULL,
  `twitter` varchar(50) DEFAULT NULL,
  `equipoID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `siglas` (`siglas`),
  UNIQUE KEY `dorsal` (`dorsal`),
  KEY `equipoID` (`equipoID`),
  CONSTRAINT `piloto_ibfk_1` FOREIGN KEY (`equipoID`) REFERENCES `equipo` (`ID`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `piloto_votacion` (
  `votacionID` int NOT NULL,
  `pilotoID` int NOT NULL,
  PRIMARY KEY (`votacionID`,`pilotoID`),
  KEY `pilotoID` (`pilotoID`),
  CONSTRAINT `piloto_votacion_ibfk_1` FOREIGN KEY (`votacionID`) REFERENCES `votacion` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `piloto_votacion_ibfk_2` FOREIGN KEY (`pilotoID`) REFERENCES `piloto` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `rol` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `simulacion` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `tipo` enum('Consumo de Combustible','Ganancia de ERS') NOT NULL,
  `resultado` float NOT NULL,
  `cocheID` int DEFAULT NULL,
  `circuitoID` int DEFAULT NULL,
  `responsableID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `cocheID` (`cocheID`),
  KEY `circuitoID` (`circuitoID`),
  KEY `responsableID` (`responsableID`),
  CONSTRAINT `simulacion_ibfk_1` FOREIGN KEY (`cocheID`) REFERENCES `coche` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `simulacion_ibfk_2` FOREIGN KEY (`circuitoID`) REFERENCES `circuito` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `simulacion_ibfk_3` FOREIGN KEY (`responsableID`) REFERENCES `usuario` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `usuario` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `nombreUsuario` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `rolID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `nombreUsuario` (`nombreUsuario`),
  UNIQUE KEY `email` (`email`),
  KEY `rolID` (`rolID`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`rolID`) REFERENCES `rol` (`ID`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `votacion` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `permalink` varchar(100) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `fechaLimite` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `permalink` (`permalink`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `voto` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nombreVotante` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `pilotoID` int NOT NULL,
  `votacionID` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email` (`email`),
  KEY `voto_ibfk_1` (`pilotoID`),
  KEY `voto_ibfk_2` (`votacionID`),
  CONSTRAINT `voto_ibfk_1` FOREIGN KEY (`pilotoID`) REFERENCES `piloto` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `voto_ibfk_2` FOREIGN KEY (`votacionID`) REFERENCES `votacion` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
