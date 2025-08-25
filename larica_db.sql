-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: larica_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `donos_restaurante`
--

DROP TABLE IF EXISTS `donos_restaurante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donos_restaurante` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `senha` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `telefone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `data_cadastro` date NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donos_restaurante`
--

LOCK TABLES `donos_restaurante` WRITE;
/*!40000 ALTER TABLE `donos_restaurante` DISABLE KEYS */;
INSERT INTO `donos_restaurante` VALUES (1,'Barto','sabor.nordestino@email.com','senha123','(84) 98811-1122','2025-08-01'),(2,'Belanico','pizzaria.dovale@email.com','senha456','(84) 97722-2233','2025-08-01'),(3,'Timbu','lanche.rapido@email.com','senha789','(84) 96633-3344','2025-08-01'),(4,'Gola','churrasco.assu@email.com','senha101','(84) 95544-4455','2025-08-01'),(5,'Gundi','resto.fit@email.com','senha202','(84) 94455-5566','2025-08-01'),(6,'Carlos','carlos@email.com','123456','(84) 99999-8888','2025-08-05'),(7,'Nemantico','gigi@gmail.com','123','333333333333','2025-08-05'),(8,'Carlos Silva','carlosvilagram@email.com','senha123','11999999999','2025-08-15'),(9,'Carlos Silva','carlosvilagramdavila@email.com','senha123','11999999999','2025-08-15'),(10,'ToinBuxudo','Petencio@gmail.com','123','123','2025-08-15');
/*!40000 ALTER TABLE `donos_restaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itens_pedido`
--

DROP TABLE IF EXISTS `itens_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itens_pedido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantidade` int NOT NULL,
  `pedido_id` bigint DEFAULT NULL,
  `produto_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK42mycompce3b7yt3l6ukdwsxy` (`pedido_id`),
  KEY `FKxytdlekpdaobqphujy9bmuhl` (`produto_id`),
  CONSTRAINT `FK42mycompce3b7yt3l6ukdwsxy` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  CONSTRAINT `FKxytdlekpdaobqphujy9bmuhl` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itens_pedido`
--

LOCK TABLES `itens_pedido` WRITE;
/*!40000 ALTER TABLE `itens_pedido` DISABLE KEYS */;
INSERT INTO `itens_pedido` VALUES (1,3,1,4),(2,1,2,5),(3,2,3,4),(4,3,4,3),(5,1,5,1),(6,2,6,2),(7,2,7,2),(8,3,8,1),(9,1,9,1),(10,1,10,1),(11,1,11,1),(12,1,12,1),(13,1,13,1),(14,1,14,1),(15,1,14,6),(16,1,15,1),(17,1,15,6),(18,1,16,1),(19,1,16,6),(20,1,17,1),(21,1,17,6),(22,1,18,1),(23,1,19,1),(24,2,20,1),(25,1,20,2),(26,2,21,1),(27,1,21,2),(28,2,22,1),(29,1,22,2),(30,1,23,6),(31,1,24,6),(32,2,25,1),(33,1,25,2),(34,1,26,3),(35,1,27,7),(36,1,28,8),(37,1,29,4),(38,1,30,9),(39,1,31,2),(40,1,32,8),(41,1,33,9),(42,1,34,5),(43,1,35,2),(44,2,35,5),(45,1,36,1),(46,1,37,3),(47,3,38,3),(48,1,39,3),(49,1,40,9),(50,1,41,9),(51,2,42,9),(52,1,43,7),(53,1,44,6),(54,1,45,9),(55,1,46,3),(56,1,47,8),(57,1,48,2),(58,1,49,8),(59,1,50,8),(60,2,51,1),(61,1,52,8),(62,1,53,9),(63,1,54,7),(64,1,55,2),(65,1,56,1),(66,1,57,4),(67,1,58,10),(68,1,59,6),(69,1,60,6),(70,1,61,6),(71,2,62,1),(72,2,63,7),(73,1,64,2),(74,1,65,1),(75,1,66,6),(76,1,67,1),(77,1,68,1),(78,1,69,1),(79,1,70,1),(80,1,71,6);
/*!40000 ALTER TABLE `itens_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagamentos`
--

DROP TABLE IF EXISTS `pagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pedido_id` bigint NOT NULL,
  `preference_id` varchar(100) DEFAULT NULL,
  `init_point` varchar(500) DEFAULT NULL,
  `sandbox_init_point` varchar(500) DEFAULT NULL,
  `mp_payment_id` varchar(100) DEFAULT NULL,
  `status` varchar(40) DEFAULT NULL,
  `valor_total` decimal(15,2) DEFAULT NULL,
  `moeda` varchar(10) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `last_notification` longtext,
  PRIMARY KEY (`id`),
  KEY `idx_pag_pedido` (`pedido_id`),
  KEY `idx_pag_preference` (`preference_id`),
  KEY `idx_pag_status` (`status`),
  CONSTRAINT `fk_pag_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamentos`
--

LOCK TABLES `pagamentos` WRITE;
/*!40000 ALTER TABLE `pagamentos` DISABLE KEYS */;
INSERT INTO `pagamentos` VALUES (1,17,'149293884-67ee91d7-514f-48e7-a4ad-e524b157c7d0','https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=149293884-67ee91d7-514f-48e7-a4ad-e524b157c7d0','https://sandbox.mercadopago.com.br/checkout/v1/redirect?pref_id=149293884-67ee91d7-514f-48e7-a4ad-e524b157c7d0',NULL,'PENDENTE',54.80,'BRL','2025-08-12 08:48:20',NULL,NULL);
/*!40000 ALTER TABLE `pagamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` datetime(6) DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `usuario_id` bigint DEFAULT NULL,
  `restaurante_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5g0es69v35nmkmpi8uewbphs2` (`usuario_id`),
  KEY `fk_pedidos_restaurantes` (`restaurante_id`),
  CONSTRAINT `FK5g0es69v35nmkmpi8uewbphs2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_pedidos_restaurantes` FOREIGN KEY (`restaurante_id`) REFERENCES `restaurantes` (`id`),
  CONSTRAINT `FKf3mf88pcxawf3nt06400qmqn3` FOREIGN KEY (`restaurante_id`) REFERENCES `restaurantes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (1,'2025-07-31 22:00:24.726020','ENTREGUE',1,4),(2,'2025-07-31 22:00:57.218929','ENTREGUE',1,5),(3,'2025-08-01 08:14:30.261758','ENTREGUE',1,4),(4,'2025-08-01 08:14:59.817255','ENTREGUE',1,3),(5,'2025-08-01 08:15:40.360702','ENTREGUE',1,1),(6,'2025-08-01 08:42:16.902838','ENTREGUE',1,2),(7,'2025-08-01 08:42:16.931761','CANCELADO',1,2),(8,'2025-08-01 19:24:37.895129','ENTREGUE',1,1),(9,'2025-08-04 15:55:20.945034','ENTREGUE',1,1),(10,'2025-08-05 20:14:19.171372','ENTREGUE',1,1),(11,'2025-08-05 20:14:31.788878','ENTREGUE',1,1),(12,'2025-08-05 20:17:47.908876','ENTREGUE',1,1),(13,'2025-08-05 20:17:50.602586','ENTREGUE',1,1),(14,'2025-08-05 20:18:51.262562','ENTREGUE',1,1),(15,'2025-08-05 20:19:00.026876','ENTREGUE',1,1),(16,'2025-08-05 20:21:00.285618','ENTREGUE',1,1),(17,'2025-08-05 20:21:03.802197','AGUARDANDO',1,1),(18,'2025-08-05 20:21:11.206755','AGUARDANDO',1,1),(19,'2025-08-05 20:21:20.131828','AGUARDANDO',1,1),(20,'2025-08-05 20:31:13.068802','AGUARDANDO',1,1),(21,'2025-08-06 09:24:35.362832','AGUARDANDO',1,1),(22,'2025-08-06 09:28:09.575341','AGUARDANDO',1,1),(23,'2025-08-06 09:31:11.958166','AGUARDANDO',1,1),(24,'2025-08-06 09:31:12.358305','AGUARDANDO',1,1),(25,'2025-08-06 09:45:55.626675','AGUARDANDO',1,1),(26,'2025-08-06 09:59:45.390734','ENTREGUE',1,3),(27,'2025-08-06 10:00:07.543119','AGUARDANDO',1,1),(28,'2025-08-06 10:01:30.820034','ENTREGUE',1,2),(29,'2025-08-06 17:44:23.011509','ENTREGUE',1,4),(30,'2025-08-06 17:46:26.250232','ENTREGUE',1,5),(31,'2025-08-06 18:28:14.635902','EM_PREPARO',1,2),(32,'2025-08-06 19:24:14.091041','EM_PREPARO',1,2),(33,'2025-08-06 19:44:06.062485','EM_PREPARO',1,5),(34,'2025-08-06 19:46:00.358112','AGUARDANDO',1,5),(35,'2025-08-06 19:55:08.513903','AGUARDANDO',3,1),(36,'2025-08-06 20:08:51.943394','AGUARDANDO',1,1),(37,'2025-08-06 21:09:41.884774','ENTREGUE',1,3),(38,'2025-08-06 21:12:11.736108','AGUARDANDO',1,3),(39,'2025-08-06 21:14:28.365270','AGUARDANDO',1,3),(40,'2025-08-06 21:17:25.042635','AGUARDANDO',1,5),(41,'2025-08-06 21:17:36.566317','AGUARDANDO',1,5),(42,'2025-08-06 21:18:00.104445','AGUARDANDO',1,5),(43,'2025-08-07 10:25:54.826337','AGUARDANDO',22,1),(44,'2025-08-07 10:26:09.535047','AGUARDANDO',22,1),(45,'2025-08-07 10:35:16.400693','AGUARDANDO',22,5),(46,'2025-08-07 10:41:14.600311','AGUARDANDO',22,3),(47,'2025-08-07 10:41:37.864097','EM_PREPARO',22,2),(48,'2025-08-07 10:42:27.658630','EM_PREPARO',1,2),(49,'2025-08-07 10:48:39.309666','EM_PREPARO',22,2),(50,'2025-08-07 11:34:29.666476','EM_PREPARO',22,2),(51,'2025-08-07 11:38:32.300734','AGUARDANDO',22,1),(52,'2025-08-07 11:39:51.689383','EM_PREPARO',23,2),(53,'2025-08-07 11:46:27.969503','AGUARDANDO',22,5),(54,'2025-08-07 12:53:55.825505','AGUARDANDO',22,1),(55,'2025-08-13 18:56:55.466142','AGUARDANDO',22,2),(56,'2025-08-13 19:11:21.493915','AGUARDANDO',22,1),(57,'2025-08-13 20:10:01.688726','EM_PREPARO',22,4),(58,'2025-08-13 20:25:20.693489','AGUARDANDO',22,7),(59,'2025-08-14 18:11:29.253744','AGUARDANDO',22,1),(60,'2025-08-14 18:12:36.492955','AGUARDANDO',22,1),(61,'2025-08-14 18:12:52.044371','AGUARDANDO',22,1),(62,'2025-08-15 07:36:13.346076','EM_PREPARO',22,1),(63,'2025-08-20 18:39:09.150740','AGUARDANDO',22,1),(64,'2025-08-22 08:25:38.708952','AGUARDANDO',22,2),(65,'2025-08-22 08:32:32.082138','AGUARDANDO',22,1),(66,'2025-08-22 08:32:39.898168','AGUARDANDO',22,1),(67,'2025-08-22 08:54:30.644324','AGUARDANDO',22,1),(68,'2025-08-22 08:54:48.133932','AGUARDANDO',22,1),(69,'2025-08-22 08:56:43.789332','AGUARDANDO',22,1),(70,'2025-08-22 08:58:59.760099','AGUARDANDO',22,1),(71,'2025-08-22 08:59:54.944174','AGUARDANDO',22,1);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `preco` decimal(10,2) NOT NULL,
  `restaurante_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsl3jhd8nhd103c5nm6yocnnkx` (`restaurante_id`),
  CONSTRAINT `FKsl3jhd8nhd103c5nm6yocnnkx` FOREIGN KEY (`restaurante_id`) REFERENCES `restaurantes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'Hambúrguer artesanal com queijo e bacon','Larica Burger',25.90,1),(2,'Pizza grande de calabresa com borda de catupiry','Pizza Calabresa',39.90,2),(3,'Coxinha de frango com catupiry','Coxinha Top',7.50,3),(4,'Espetinho de carne com farofa','Espetinho Bovino',12.00,4),(5,'Salada com frango grelhado e molho especial','Salada Fit',18.75,5),(6,'Pão, carne, queijo e bacon','X-Bacon',28.90,1),(7,'com tapioca','ginga',28.90,1),(8,'Gia assadinha','Sanduiche de Gia',28.90,2),(9,'do Nemin','Mousse de Wey',30.00,5),(10,'Bem Fritinha','Carne de gia',30.00,7),(11,'Bem sequinho','Sanduiche de Camarao',28.00,1),(12,'Docinho','Picolé de Tapioca',23.00,5);
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurantes`
--

DROP TABLE IF EXISTS `restaurantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurantes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `endereco` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telefone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `dono_id` bigint DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_restaurante_dono` (`dono_id`),
  CONSTRAINT `fk_restaurante_dono` FOREIGN KEY (`dono_id`) REFERENCES `donos_restaurante` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurantes`
--

LOCK TABLES `restaurantes` WRITE;
/*!40000 ALTER TABLE `restaurantes` DISABLE KEYS */;
INSERT INTO `restaurantes` VALUES (1,'Rua das Flores, 123','Sabor Nordestino','(84) 98811-1122',1,-5.7945,-35.211),(2,'Av. Central, 456','Pizzaria do Vale','(84) 97722-2233',2,-5.838,-35.2075),(3,'Rua do Comércio, 789','Lanche Rápido','(84) 96633-3344',3,-5.8056,-35.2067),(4,'Travessa Natalina, 101','Churrasco Assú','(84) 95544-4455',4,-5.8735,-35.2095),(5,'Rua Lagoa Nova, 202','Restô Fit','(84) 94455-5566',5,-5.7793,-35.2502),(6,'Av. Brasil, 100','Churrasco de gato','(84) 98888-7777',6,-5.891,-35.1992),(7,'Lagoa do Piató','Peixe Frito','333333333333',7,-5.8093,-35.2175),(8,'Rua das Flores, 123 - Centro','Pizzaria do Bairro','1133333333',8,-5.79448,-35.211),(9,'Rua das Flores, 123 - Centro','Pizzaria do Bairro','1133333333',9,-5.8324,-35.2051),(10,'Rua Padre Lemos 101 - Praia do Meio Natal RN','Casa do Camarão','91987446061',10,-5.7759753,-35.1955246);
/*!40000 ALTER TABLE `restaurantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_cadastro` date DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `senha` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telefone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tipo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `endereco` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cidade` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cep` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'2025-07-25','cliente@larica.com','Cliente Exemplo','123456','11999999999','',NULL,NULL,NULL,NULL),(2,'2025-07-25','joao@email.com','João','123456',NULL,'',NULL,NULL,NULL,NULL),(3,'2025-07-25','alice.rocha@email.com','Alice Rocha','senha123','(11) 98877-1122','',NULL,NULL,NULL,NULL),(4,'2025-07-25','bruno.lima@email.com','Bruno Lima','senha456','(21) 97766-2233','',NULL,NULL,NULL,NULL),(5,'2025-07-25','camila.torres@email.com','Camila Torres','abc12345','(31) 96655-3344','',NULL,NULL,NULL,NULL),(6,'2025-07-25','diego.martins@email.com','Diego Martins','qwerty789','(41) 95544-4455','',NULL,NULL,NULL,NULL),(7,'2025-07-25','eduarda.alves@email.com','Eduarda Alves','senha2025','(51) 94433-5566','',NULL,NULL,NULL,NULL),(8,'2025-07-25','felipe.costa@email.com','Felipe Costa','12345678','(61) 93322-6677','',NULL,NULL,NULL,NULL),(9,'2025-07-25','gabriela.souza@email.com','Gabriela Souza','pass7890','(71) 92211-7788','',NULL,NULL,NULL,NULL),(10,'2025-07-25','henrique.dias@email.com','Henrique Dias','segura321','(81) 91100-8899','',NULL,NULL,NULL,NULL),(11,'2025-07-25','isabela.moura@email.com','Isabela Moura','laricaApp','(91) 90099-9900','',NULL,NULL,NULL,NULL),(12,'2025-07-25','joao.pedro@email.com','João Pedro','joao1234','(11) 98866-1010','',NULL,NULL,NULL,NULL),(13,'2025-07-25','karla.mendes@email.com','Karla Mendes','karla567','(21) 98755-1111','',NULL,NULL,NULL,NULL),(14,'2025-07-25','leonardo.pinto@email.com','Leonardo Pinto','leo321','(31) 97644-1222','',NULL,NULL,NULL,NULL),(15,'2025-07-25','marina.freitas@email.com','Marina Freitas','marina123','(41) 96533-1333','',NULL,NULL,NULL,NULL),(16,'2025-07-25','nicolas.farias@email.com','Nicolas Farias','nickpass','(51) 95422-1444','',NULL,NULL,NULL,NULL),(17,'2025-07-25','olivia.andrade@email.com','Olívia Andrade','olivia456','(61) 94311-1555','',NULL,NULL,NULL,NULL),(18,NULL,'jiunemojitsu@gmail.com','Nemin','eujamereergui',NULL,'',NULL,NULL,NULL,NULL),(21,NULL,'teste2@email.com','Teste Via Auth','123456',NULL,NULL,NULL,NULL,NULL,NULL),(22,NULL,'engnfaraujo@gmail.com','Nemin','010905',NULL,NULL,NULL,NULL,NULL,NULL),(23,NULL,'sabor.nordestino@email.com','Tupanzinho','123123',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-22 18:03:29
