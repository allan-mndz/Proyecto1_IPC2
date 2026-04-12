-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema proyecto1
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `proyecto1` ;

-- -----------------------------------------------------
-- Schema proyecto1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `proyecto1` ;
USE `proyecto1` ;

-- -----------------------------------------------------
-- Table `proyecto1`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Usuarios` (
  `nombre` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `tipo` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`nombre`),
  UNIQUE INDEX `password_UNIQUE` (`password` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Clientes` (
  `dpi` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `nacionalidad` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`dpi`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE,
  UNIQUE INDEX `dpi_UNIQUE` (`dpi` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Destinos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Destinos` (
  `nombre` VARCHAR(45) NOT NULL,
  `pais` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(450) NOT NULL,
  `clima` VARCHAR(100) NOT NULL,
  `imagen_url` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`nombre`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Proveedores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Proveedores` (
  `nombre` VARCHAR(100) NOT NULL,
  `tipo` TINYINT(50) UNSIGNED NOT NULL,
  `pais` VARCHAR(45) NOT NULL,
  `contacto` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nombre`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Paquetes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Paquetes` (
  `nombre` VARCHAR(100) NOT NULL,
  `destino_nombre` VARCHAR(100) NOT NULL,
  `duracion` INT UNSIGNED NOT NULL,
  `descripcion` VARCHAR(255) NOT NULL,
  `precio` DECIMAL(10,2) UNSIGNED NOT NULL,
  `capacidad` INT UNSIGNED NOT NULL,
  `estado` TINYINT(50) UNSIGNED NOT NULL,
  PRIMARY KEY (`nombre`),
  INDEX `fk_paquete_destino_idx` (`destino_nombre` ASC) VISIBLE,
  CONSTRAINT `fk_paquete_destino`
    FOREIGN KEY (`destino_nombre`)
    REFERENCES `proyecto1`.`Destinos` (`nombre`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Servicio_Paquete`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Servicio_Paquete` (
  `id_servicio` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `paquete_nombre` VARCHAR(100) NOT NULL,
  `proveedor_nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255) NOT NULL,
  `costo` DECIMAL(10,2) UNSIGNED NOT NULL,
  PRIMARY KEY (`id_servicio`),
  UNIQUE INDEX `id_servicio_UNIQUE` (`id_servicio` ASC) VISIBLE,
  INDEX `fk_servicio_paquete_idx` (`paquete_nombre` ASC) VISIBLE,
  INDEX `fk_servicio_proveedor_idx` (`proveedor_nombre` ASC) VISIBLE,
  CONSTRAINT `fk_servicio_paquete`
    FOREIGN KEY (`paquete_nombre`)
    REFERENCES `proyecto1`.`Paquetes` (`nombre`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_servicio_proveedor`
    FOREIGN KEY (`proveedor_nombre`)
    REFERENCES `proyecto1`.`Proveedores` (`nombre`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Reservaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Reservaciones` (
  `numero_reservacion` VARCHAR(45) NOT NULL,
  `fecha_creacion` DATE NOT NULL,
  `fecha_viaje` DATE NOT NULL,
  `paquete_nombre` VARCHAR(100) NOT NULL,
  `cantidad_pasajeros` INT UNSIGNED NOT NULL,
  `agente_nombre` VARCHAR(45) NOT NULL,
  `costo_total` DECIMAL(10,2) UNSIGNED NOT NULL,
  `estado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`numero_reservacion`),
  UNIQUE INDEX `numero_reservacion_UNIQUE` (`numero_reservacion` ASC) VISIBLE,
  INDEX `fk_reservacion_paquete_idx` (`paquete_nombre` ASC) VISIBLE,
  INDEX `fk_reservacion_agente_idx` (`agente_nombre` ASC) VISIBLE,
  CONSTRAINT `fk_reservacion_paquete`
    FOREIGN KEY (`paquete_nombre`)
    REFERENCES `proyecto1`.`Paquetes` (`nombre`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_reservacion_agente`
    FOREIGN KEY (`agente_nombre`)
    REFERENCES `proyecto1`.`Usuarios` (`nombre`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Reservacion_Pasajero`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Reservacion_Pasajero` (
  `numero_reservacion` VARCHAR(45) NOT NULL,
  `dpi_cliente` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`numero_reservacion`, `dpi_cliente`),
  INDEX `fk_respas_cliente_idx` (`dpi_cliente` ASC) VISIBLE,
  CONSTRAINT `fk_respas_reservacion`
    FOREIGN KEY (`numero_reservacion`)
    REFERENCES `proyecto1`.`Reservaciones` (`numero_reservacion`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_respas_cliente`
    FOREIGN KEY (`dpi_cliente`)
    REFERENCES `proyecto1`.`Clientes` (`dpi`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1`.`Pagos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto1`.`Pagos` (
  `id_pago` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `numero_reservacion` VARCHAR(45) NOT NULL,
  `monto` DECIMAL(10,2) UNSIGNED NOT NULL,
  `metodo` TINYINT(50) UNSIGNED NOT NULL,
  `fecha` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_pago`),
  UNIQUE INDEX `id_pago_UNIQUE` (`id_pago` ASC) VISIBLE,
  INDEX `fk_pago_reservacion_idx` (`numero_reservacion` ASC) VISIBLE,
  CONSTRAINT `fk_pago_reservacion`
    FOREIGN KEY (`numero_reservacion`)
    REFERENCES `proyecto1`.`Reservaciones` (`numero_reservacion`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
