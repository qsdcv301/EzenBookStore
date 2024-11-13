-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: ezbook
-- ------------------------------------------------------
-- Server version	8.4.2

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
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `publish_date` timestamp NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `category_id` bigint NOT NULL,
  `subcategory_id` bigint NOT NULL,
  `ifkr` tinyint NOT NULL DEFAULT '1',
  `stock` int NOT NULL DEFAULT '0',
  `price` int NOT NULL,
  `count` bigint NOT NULL DEFAULT '0',
  `discount` int NOT NULL DEFAULT '0',
  `bookdescription_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'책 제목 1','작가 1','출판사 1','2024-11-10 08:01:18','9780000000001',1,1,1,100,15000,0,10,1),(2,'책 제목 2','작가 2','출판사 2','2024-11-10 08:01:18','9780000000002',1,1,1,100,15000,0,10,2),(3,'책 제목 3','작가 3','출판사 3','2024-11-10 08:01:18','9780000000003',1,1,1,100,15000,0,10,3),(4,'책 제목 4','작가 4','출판사 4','2024-11-10 08:01:18','9780000000004',1,1,1,100,15000,0,10,4),(5,'책 제목 5','작가 5','출판사 5','2024-11-10 08:01:18','9780000000005',1,1,1,100,15000,0,10,5),(6,'책 제목 6','작가 6','출판사 6','2024-11-10 08:01:18','9780000000006',1,1,1,100,15000,0,10,6),(7,'책 제목 7','작가 7','출판사 7','2024-11-10 08:01:18','9780000000007',1,1,1,100,15000,0,10,7),(8,'책 제목 8','작가 8','출판사 8','2024-11-10 08:01:18','9780000000008',1,1,1,100,15000,0,10,8),(9,'책 제목 9','작가 9','출판사 9','2024-11-10 08:01:18','9780000000009',1,1,1,100,15000,0,10,9),(10,'책 제목 10','작가 10','출판사 10','2024-11-10 08:01:18','9780000000010',1,1,1,100,15000,0,10,10),(11,'책 제목 11','작가 11','출판사 11','2024-11-10 08:01:18','9780000000011',1,1,1,100,15000,0,10,11),(12,'책 제목 12','작가 12','출판사 12','2024-11-10 08:01:18','9780000000012',1,1,1,100,15000,0,10,12),(13,'책 제목 13','작가 13','출판사 13','2024-11-10 08:01:18','9780000000013',1,1,1,100,15000,0,10,13),(14,'책 제목 14','작가 14','출판사 14','2024-11-10 08:01:18','9780000000014',1,1,1,100,15000,0,10,14),(15,'책 제목 15','작가 15','출판사 15','2024-11-10 08:01:18','9780000000015',1,1,1,100,15000,0,10,15),(16,'책 제목 16','작가 16','출판사 16','2024-11-10 08:01:18','9780000000016',1,1,1,100,15000,0,10,16),(17,'책 제목 17','작가 17','출판사 17','2024-11-10 08:01:18','9780000000017',1,1,1,100,15000,0,10,17),(18,'책 제목 18','작가 18','출판사 18','2024-11-10 08:01:18','9780000000018',1,1,1,100,15000,0,10,18),(19,'책 제목 19','작가 19','출판사 19','2024-11-10 08:01:18','9780000000019',1,1,1,100,15000,0,10,19),(20,'책 제목 20','작가 20','출판사 20','2024-11-10 08:01:18','9780000000020',1,1,1,100,15000,0,10,20);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookdescription`
--

DROP TABLE IF EXISTS `bookdescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookdescription` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contents` text NOT NULL,
  `description` text NOT NULL,
  `writer` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookdescription`
--

LOCK TABLES `bookdescription` WRITE;
/*!40000 ALTER TABLE `bookdescription` DISABLE KEYS */;
INSERT INTO `bookdescription` VALUES (1,'이 컬럼은 책 목차를 위한 공간입니다.','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(2,'책 설명 2','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(3,'책 설명 3','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(4,'책 설명 4','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(5,'책 설명 5','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(6,'책 설명 6','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(7,'책 설명 7','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(8,'책 설명 8','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(9,'책 설명 9','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(10,'책 설명 10','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(11,'책 설명 11','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(12,'책 설명 12','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(13,'책 설명 13','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(14,'책 설명 14','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(15,'책 설명 15','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(16,'책 설명 16','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(17,'책 설명 17','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(18,'책 설명 18','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(19,'책 설명 19','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(20,'책 설명 20','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.');
/*!40000 ALTER TABLE `bookdescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `book_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,5,1,2),(2,8,2,83),(3,8,3,44),(4,9,4,91),(5,1,5,34),(6,7,6,12),(7,7,7,15),(8,7,8,68),(9,6,9,59),(10,4,10,97),(11,9,11,15),(12,4,12,13),(13,7,13,4),(14,2,14,59),(15,5,15,71),(16,1,16,22),(17,9,17,77),(18,2,18,53),(19,2,19,10),(20,1,20,1);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'카테고리 1'),(2,'카테고리 2'),(3,'카테고리 3'),(4,'카테고리 4'),(5,'카테고리 5'),(6,'카테고리 6'),(7,'카테고리 7'),(8,'카테고리 8'),(9,'카테고리 9'),(10,'카테고리 10');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tracking_num` bigint NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` timestamp NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  `name` varchar(100) DEFAULT NULL,
  `tel` varchar(20) DEFAULT '010-1234-5678',
  `addr` varchar(255) DEFAULT NULL,
  `addrextra` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (1,3835,'2024-11-10 08:24:48','2024-11-10 08:24:48',2,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(2,10468,'2024-11-10 08:24:48',NULL,5,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(3,8840,'2024-11-10 08:24:48','2024-11-10 08:24:48',4,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(4,3486,'2024-11-10 08:24:48',NULL,2,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(5,8721,'2024-11-10 08:24:48','2024-11-10 08:24:48',5,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(6,8152,'2024-11-10 08:24:48',NULL,4,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(7,8691,'2024-11-10 08:24:48','2024-11-10 08:24:48',2,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(8,9764,'2024-11-10 08:24:48',NULL,2,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(9,4165,'2024-11-10 08:24:48','2024-11-10 08:24:48',0,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(10,6869,'2024-11-10 08:24:48',NULL,5,'수령인','010-1234-5678','수령주소','수령 상세 주소');
/*!40000 ALTER TABLE `delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `start_date` timestamp NOT NULL,
  `end_date` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'이벤트 제목 1','이벤트 내용 1','2024-11-10 08:33:34','2024-11-17 08:33:34'),(2,'이벤트 제목 2','이벤트 내용 2','2024-11-10 08:33:34','2024-11-17 08:33:34'),(3,'이벤트 제목 3','이벤트 내용 3','2024-11-10 08:33:34','2024-11-17 08:33:34'),(4,'이벤트 제목 4','이벤트 내용 4','2024-11-10 08:33:34','2024-11-17 08:33:34'),(5,'이벤트 제목 5','이벤트 내용 5','2024-11-10 08:33:34','2024-11-17 08:33:34'),(6,'이벤트 제목 6','이벤트 내용 6','2024-11-10 08:33:34','2024-11-17 08:33:34'),(7,'이벤트 제목 7','이벤트 내용 7','2024-11-10 08:33:34','2024-11-17 08:33:34'),(8,'이벤트 제목 8','이벤트 내용 8','2024-11-10 08:33:34','2024-11-17 08:33:34'),(9,'이벤트 제목 9','이벤트 내용 9','2024-11-10 08:33:34','2024-11-17 08:33:34'),(10,'이벤트 제목 10','이벤트 내용 10','2024-11-10 08:33:34','2024-11-17 08:33:34'),(11,'이벤트 제목 11','이벤트 내용 11','2024-11-10 08:33:34','2024-11-17 08:33:34'),(12,'이벤트 제목 12','이벤트 내용 12','2024-11-10 08:33:34','2024-11-17 08:33:34'),(13,'이벤트 제목 13','이벤트 내용 13','2024-11-10 08:33:34','2024-11-17 08:33:34'),(14,'이벤트 제목 14','이벤트 내용 14','2024-11-10 08:33:34','2024-11-17 08:33:34'),(15,'이벤트 제목 15','이벤트 내용 15','2024-11-10 08:33:34','2024-11-17 08:33:34'),(16,'이벤트 제목 16','이벤트 내용 16','2024-11-10 08:33:34','2024-11-17 08:33:34'),(17,'이벤트 제목 17','이벤트 내용 17','2024-11-10 08:33:34','2024-11-17 08:33:34'),(18,'이벤트 제목 18','이벤트 내용 18','2024-11-10 08:33:34','2024-11-17 08:33:34'),(19,'이벤트 제목 19','이벤트 내용 19','2024-11-10 08:33:34','2024-11-17 08:33:34'),(20,'이벤트 제목 20','이벤트 내용 20','2024-11-10 08:33:34','2024-11-17 08:33:34');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'공지사항 제목 1','공지사항 내용 1','2024-11-10 08:31:40'),(2,'공지사항 제목 2','공지사항 내용 2','2024-11-10 08:31:40'),(3,'공지사항 제목 3','공지사항 내용 3','2024-11-10 08:31:40'),(4,'공지사항 제목 4','공지사항 내용 4','2024-11-10 08:31:40'),(5,'공지사항 제목 5','공지사항 내용 5','2024-11-10 08:31:40'),(6,'공지사항 제목 6','공지사항 내용 6','2024-11-10 08:31:40'),(7,'공지사항 제목 7','공지사항 내용 7','2024-11-10 08:31:40'),(8,'공지사항 제목 8','공지사항 내용 8','2024-11-10 08:31:40'),(9,'공지사항 제목 9','공지사항 내용 9','2024-11-10 08:31:40'),(10,'공지사항 제목 10','공지사항 내용 10','2024-11-10 08:31:40'),(11,'공지사항 제목 11','공지사항 내용 11','2024-11-10 08:31:40'),(12,'공지사항 제목 12','공지사항 내용 12','2024-11-10 08:31:40'),(13,'공지사항 제목 13','공지사항 내용 13','2024-11-10 08:31:40'),(14,'공지사항 제목 14','공지사항 내용 14','2024-11-10 08:31:40'),(15,'공지사항 제목 15','공지사항 내용 15','2024-11-10 08:31:40'),(16,'공지사항 제목 16','공지사항 내용 16','2024-11-10 08:31:40'),(17,'공지사항 제목 17','공지사항 내용 17','2024-11-10 08:31:40'),(18,'공지사항 제목 18','공지사항 내용 18','2024-11-10 08:31:40'),(19,'공지사항 제목 19','공지사항 내용 19','2024-11-10 08:31:40'),(20,'공지사항 제목 20','공지사항 내용 20','2024-11-10 08:31:40');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderitem`
--

DROP TABLE IF EXISTS `orderitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderitem` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderitem`
--

LOCK TABLES `orderitem` WRITE;
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
INSERT INTO `orderitem` VALUES (1,19,4),(2,1,2),(3,1,2),(4,6,4),(5,15,4),(6,15,4),(7,4,5),(8,14,4),(9,10,1),(10,16,2),(11,12,1),(12,11,5),(13,9,2),(14,9,4),(15,7,5),(16,4,3),(17,11,5),(18,4,1),(19,1,3),(20,12,4),(21,3,5),(22,4,2),(23,4,5),(24,4,5),(25,13,1),(26,11,2),(27,16,2),(28,2,1),(29,14,4),(30,1,1);
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `orders_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_price` int NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `orderitem_id` bigint NOT NULL,
  `delivery_id` bigint DEFAULT NULL,
  `payment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,5,'2024-11-10 08:34:51',1325,4,0,19,19),(2,2,'2024-11-10 08:34:51',1132,5,0,17,6),(3,7,'2024-11-10 08:34:51',1290,5,0,20,2),(4,1,'2024-11-10 08:34:51',1172,3,0,10,1),(5,10,'2024-11-10 08:34:51',1484,5,0,16,18),(6,1,'2024-11-10 08:34:51',1129,0,0,2,14),(7,1,'2024-11-10 08:34:51',1283,4,0,6,5),(8,4,'2024-11-10 08:34:51',1192,4,0,7,19),(9,10,'2024-11-10 08:34:51',1250,3,0,12,5),(10,1,'2024-11-10 08:34:51',1312,0,0,9,7),(11,4,'2024-11-10 08:34:51',1257,3,0,7,14),(12,5,'2024-11-10 08:34:51',1172,2,0,11,10),(13,3,'2024-11-10 08:34:51',1353,5,0,15,8),(14,5,'2024-11-10 08:34:51',1158,2,0,12,18),(15,9,'2024-11-10 08:34:51',1109,3,0,11,2),(16,10,'2024-11-10 08:34:51',1009,2,0,17,17),(17,8,'2024-11-10 08:34:51',1300,4,0,14,19),(18,3,'2024-11-10 08:34:51',1300,2,0,13,9),(19,3,'2024-11-10 08:34:51',1412,3,0,3,9),(20,1,'2024-11-10 08:34:51',1399,4,0,17,16);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `payment_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` int NOT NULL,
  `method` tinyint NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '0',
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,'2024-11-10 08:40:03',1043,0,1,1),(2,'2024-11-10 08:40:03',1091,2,0,3),(3,'2024-11-10 08:40:03',1194,2,1,4),(4,'2024-11-10 08:40:03',1342,0,1,8),(5,'2024-11-10 08:40:03',1011,2,2,4),(6,'2024-11-10 08:40:03',1059,2,2,2),(7,'2024-11-10 08:40:03',1339,2,2,10),(8,'2024-11-10 08:40:03',1359,0,2,8),(9,'2024-11-10 08:40:03',1479,0,1,8),(10,'2024-11-10 08:40:03',1126,0,0,5),(11,'2024-11-10 08:40:03',1448,2,0,1),(12,'2024-11-10 08:40:03',1002,1,0,9),(13,'2024-11-10 08:40:03',1472,2,0,2),(14,'2024-11-10 08:40:03',1330,0,0,2),(15,'2024-11-10 08:40:03',1032,1,0,4),(16,'2024-11-10 08:40:03',1312,2,0,1),(17,'2024-11-10 08:40:03',1345,0,0,2),(18,'2024-11-10 08:40:03',1118,1,1,9),(19,'2024-11-10 08:40:03',1260,2,0,7),(20,'2024-11-10 08:40:03',1209,1,2,7);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qna`
--

DROP TABLE IF EXISTS `qna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qna` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `category` tinyint NOT NULL,
  `question` text NOT NULL,
  `title` varchar(255) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `answer` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna`
--

LOCK TABLES `qna` WRITE;
/*!40000 ALTER TABLE `qna` DISABLE KEYS */;
INSERT INTO `qna` VALUES (1,4,3,'문의 내용 1','문의 제목 1','2024-11-10 08:40:19','답변 내용 1'),(2,9,4,'문의 내용 2','문의 제목 2','2024-11-10 08:40:19','답변 내용 2'),(3,9,2,'문의 내용 3','문의 제목 3','2024-11-10 08:40:19','답변 내용 3'),(4,10,0,'문의 내용 4','문의 제목 4','2024-11-10 08:40:19','답변 내용 4'),(5,8,3,'문의 내용 5','문의 제목 5','2024-11-10 08:40:19','답변 내용 5'),(6,9,1,'문의 내용 6','문의 제목 6','2024-11-10 08:40:19','답변 내용 6'),(7,9,1,'문의 내용 7','문의 제목 7','2024-11-10 08:40:19','답변 내용 7'),(8,8,4,'문의 내용 8','문의 제목 8','2024-11-10 08:40:19','답변 내용 8'),(9,4,1,'문의 내용 9','문의 제목 9','2024-11-10 08:40:19','답변 내용 9'),(10,10,1,'문의 내용 10','문의 제목 10','2024-11-10 08:40:19','답변 내용 10'),(11,3,1,'문의 내용 11','문의 제목 11','2024-11-10 08:40:19','답변 내용 11'),(12,2,3,'문의 내용 12','문의 제목 12','2024-11-10 08:40:19','답변 내용 12'),(13,5,3,'문의 내용 13','문의 제목 13','2024-11-10 08:40:19','답변 내용 13'),(14,5,0,'문의 내용 14','문의 제목 14','2024-11-10 08:40:19','답변 내용 14'),(15,8,2,'문의 내용 15','문의 제목 15','2024-11-10 08:40:19','답변 내용 15'),(16,3,4,'문의 내용 16','문의 제목 16','2024-11-10 08:40:19','답변 내용 16'),(17,1,1,'문의 내용 17','문의 제목 17','2024-11-10 08:40:19','답변 내용 17'),(18,3,2,'문의 내용 18','문의 제목 18','2024-11-10 08:40:19','답변 내용 18'),(19,7,4,'문의 내용 19','문의 제목 19','2024-11-10 08:40:19','답변 내용 19'),(20,4,4,'문의 내용 20','문의 제목 20','2024-11-10 08:40:19','답변 내용 20');
/*!40000 ALTER TABLE `qna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `rating` tinyint NOT NULL,
  `comment` text NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,15,9,1,'리뷰 내용 1','2024-11-10 08:40:40'),(2,6,9,2,'리뷰 내용 2','2024-11-10 08:40:40'),(3,7,5,3,'리뷰 내용 3','2024-11-10 08:40:40'),(4,16,6,3,'리뷰 내용 4','2024-11-10 08:40:40'),(5,11,3,5,'리뷰 내용 5','2024-11-10 08:40:40'),(6,15,10,3,'리뷰 내용 6','2024-11-10 08:40:40'),(7,7,4,5,'리뷰 내용 7','2024-11-10 08:40:40'),(8,14,6,3,'리뷰 내용 8','2024-11-10 08:40:40'),(9,18,9,4,'리뷰 내용 9','2024-11-10 08:40:40'),(10,20,9,1,'리뷰 내용 10','2024-11-10 08:40:40'),(11,6,10,5,'리뷰 내용 11','2024-11-10 08:40:40'),(12,11,1,3,'리뷰 내용 12','2024-11-10 08:40:40'),(13,16,3,5,'리뷰 내용 13','2024-11-10 08:40:40'),(14,12,3,4,'리뷰 내용 14','2024-11-10 08:40:40'),(15,15,6,3,'리뷰 내용 15','2024-11-10 08:40:40'),(16,6,6,4,'리뷰 내용 16','2024-11-10 08:40:40'),(17,8,5,1,'리뷰 내용 17','2024-11-10 08:40:40'),(18,6,1,2,'리뷰 내용 18','2024-11-10 08:40:40'),(19,8,10,4,'리뷰 내용 19','2024-11-10 08:40:40'),(20,5,4,1,'리뷰 내용 20','2024-11-10 08:40:40');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategory`
--

DROP TABLE IF EXISTS `subcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategory` (
  `id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategory`
--

LOCK TABLES `subcategory` WRITE;
/*!40000 ALTER TABLE `subcategory` DISABLE KEYS */;
INSERT INTO `subcategory` VALUES (1,1,'서브카테고리 1 - 카테고리 1'),(2,1,'서브카테고리 2 - 카테고리 1'),(3,1,'서브카테고리 3 - 카테고리 1'),(4,2,'서브카테고리 1 - 카테고리 2'),(5,2,'서브카테고리 2 - 카테고리 2'),(6,2,'서브카테고리 3 - 카테고리 2'),(7,3,'서브카테고리 1 - 카테고리 3'),(8,3,'서브카테고리 2 - 카테고리 3'),(9,3,'서브카테고리 3 - 카테고리 3'),(10,4,'서브카테고리 1 - 카테고리 4'),(11,4,'서브카테고리 2 - 카테고리 4'),(12,4,'서브카테고리 3 - 카테고리 4'),(13,5,'서브카테고리 1 - 카테고리 5'),(14,5,'서브카테고리 2 - 카테고리 5'),(15,5,'서브카테고리 3 - 카테고리 5');
/*!40000 ALTER TABLE `subcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(225) NOT NULL,
  `name` varchar(64) NOT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `addr` varchar(225) DEFAULT NULL,
  `addrextra` varchar(225) DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `birthday` int DEFAULT NULL,
  `grade` tinyint DEFAULT '0',
  `point` int DEFAULT '0',
  `provider` varchar(60) NOT NULL DEFAULT 'ezen',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'user1@ezbook.com','password1','사용자1','010-1234-5671','서울시 강남구1','역삼동 1231','2024-11-10 08:40:57',19905704,1,3073,'ezen'),(2,'user2@ezbook.com','password2','사용자2','010-1234-5672','서울시 강남구2','역삼동 1232','2024-11-10 08:40:57',19903535,3,9464,'ezen'),(3,'user3@ezbook.com','password3','사용자3','010-1234-5673','서울시 강남구3','역삼동 1233','2024-11-10 08:40:57',19903560,4,4147,'ezen'),(4,'user4@ezbook.com','password4','사용자4','010-1234-5674','서울시 강남구4','역삼동 1234','2024-11-10 08:40:57',19904124,3,6297,'ezen'),(5,'user5@ezbook.com','password5','사용자5','010-1234-5675','서울시 강남구5','역삼동 1235','2024-11-10 08:40:57',19908567,1,1810,'ezen'),(6,'user6@ezbook.com','password6','사용자6','010-1234-5676','서울시 강남구6','역삼동 1236','2024-11-10 08:40:57',19908825,4,4786,'ezen'),(7,'user7@ezbook.com','password7','사용자7','010-1234-5677','서울시 강남구7','역삼동 1237','2024-11-10 08:40:57',19909456,1,4018,'ezen'),(8,'user8@ezbook.com','password8','사용자8','010-1234-5678','서울시 강남구8','역삼동 1238','2024-11-10 08:40:57',19902942,1,2235,'ezen'),(9,'user9@ezbook.com','password9','사용자9','010-1234-5679','서울시 강남구9','역삼동 1239','2024-11-10 08:40:57',19904824,3,371,'ezen'),(10,'user10@ezbook.com','password10','사용자10','010-1234-56710','서울시 강남구10','역삼동 12310','2024-11-10 08:40:57',19901232,2,9331,'ezen'),(11,'user11@ezbook.com','password11','사용자11','010-1234-56711','서울시 강남구11','역삼동 12311','2024-11-10 08:40:57',19903122,3,6494,'ezen'),(12,'user12@ezbook.com','password12','사용자12','010-1234-56712','서울시 강남구12','역삼동 12312','2024-11-10 08:40:57',19901240,3,7649,'ezen'),(13,'user13@ezbook.com','password13','사용자13','010-1234-56713','서울시 강남구13','역삼동 12313','2024-11-10 08:40:57',19909706,2,6580,'ezen'),(14,'user14@ezbook.com','password14','사용자14','010-1234-56714','서울시 강남구14','역삼동 12314','2024-11-10 08:40:57',19907764,4,9898,'ezen'),(15,'user15@ezbook.com','password15','사용자15','010-1234-56715','서울시 강남구15','역삼동 12315','2024-11-10 08:40:57',19903858,4,4197,'ezen'),(16,'user16@ezbook.com','password16','사용자16','010-1234-56716','서울시 강남구16','역삼동 12316','2024-11-10 08:40:57',19903806,2,8555,'ezen'),(17,'user17@ezbook.com','password17','사용자17','010-1234-56717','서울시 강남구17','역삼동 12317','2024-11-10 08:40:57',19905077,4,1141,'ezen'),(18,'user18@ezbook.com','password18','사용자18','010-1234-56718','서울시 강남구18','역삼동 12318','2024-11-10 08:40:57',19908164,3,284,'ezen'),(19,'user19@ezbook.com','password19','사용자19','010-1234-56719','서울시 강남구19','역삼동 12319','2024-11-10 08:40:57',19900837,1,1938,'ezen'),(20,'user20@ezbook.com','password20','사용자20','010-1234-56720','서울시 강남구20','역삼동 12320','2024-11-10 08:40:57',19901304,0,7406,'ezen');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-13 13:08:57
