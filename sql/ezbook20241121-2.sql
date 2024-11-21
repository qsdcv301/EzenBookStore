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
  `publish_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isbn` varchar(20) NOT NULL,
  `category_id` bigint NOT NULL,
  `subcategory_id` bigint NOT NULL,
  `ifkr` tinyint NOT NULL DEFAULT '0',
  `stock` int NOT NULL DEFAULT '0',
  `price` bigint NOT NULL,
  `count` bigint NOT NULL DEFAULT '0',
  `discount` tinyint NOT NULL DEFAULT '0',
  `bookdescription_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'책 제목 1','작가 1','출판사 1','2024-11-10 08:01:18','9780000000001',1,1,1,100,15000,0,10,1),(2,'책 제목 2','작가 2','출판사 2','2024-11-10 08:01:18','9780000000002',1,1,1,100,15000,0,10,2),(3,'책 제목 3','작가 3','출판사 3','2024-11-10 08:01:18','9780000000003',1,1,1,100,15000,0,10,3),(4,'책 제목 4','작가 4','출판사 4','2024-11-10 08:01:18','9780000000004',1,1,1,100,15000,0,10,4),(5,'책 제목 5','작가 5','출판사 5','2024-11-10 08:01:18','9780000000005',1,1,1,100,15000,0,10,5);
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
  `contents` varchar(5000) NOT NULL,
  `description` varchar(5000) NOT NULL,
  `writer` varchar(5000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookdescription`
--

LOCK TABLES `bookdescription` WRITE;
/*!40000 ALTER TABLE `bookdescription` DISABLE KEYS */;
INSERT INTO `bookdescription` VALUES (1,'이 컬럼은 책 목차를 위한 공간입니다.','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(2,'책 설명 2','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(3,'책 설명 3','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(4,'책 설명 4','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.'),(5,'책 설명 5','이곳은 책의 대한 설명을 위한 공간입니다.','이곳은 작가에 대한 설명을 위한 공간입니다.');
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
INSERT INTO `cart` VALUES (1,1,1,2),(2,2,2,83),(3,3,3,44),(4,4,4,91),(5,1,5,34);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'카테고리 1'),(2,'카테고리 2'),(3,'카테고리 3'),(4,'카테고리 4'),(5,'카테고리 5');
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
  `tracking_num` varchar(255) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` timestamp NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL DEFAULT '수령인',
  `tel` varchar(20) NOT NULL DEFAULT '수령자전화번호',
  `addr` varchar(255) NOT NULL DEFAULT '수령주소',
  `addrextra` varchar(255) NOT NULL DEFAULT '수령상세주소',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (1,'3835','2024-11-10 08:24:48','2024-11-10 08:24:48',2,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(2,'10468','2024-11-10 08:24:48','2024-11-10 08:24:48',5,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(3,'8840','2024-11-10 08:24:48','2024-11-10 08:24:48',4,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(4,'3486','2024-11-10 08:24:48','2024-11-10 08:24:48',2,'수령인','010-1234-5678','수령주소','수령 상세 주소'),(5,'8721','2024-11-10 08:24:48','2024-11-10 08:24:48',5,'수령인','010-1234-5678','수령주소','수령 상세 주소');
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
  `content` varchar(5000) NOT NULL,
  `start_date` timestamp NOT NULL,
  `end_date` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'이벤트 제목 1','이벤트 내용 1','2024-11-10 08:33:34','2024-11-17 08:33:34'),(2,'이벤트 제목 2','이벤트 내용 2','2024-11-10 08:33:34','2024-11-17 08:33:34'),(3,'이벤트 제목 3','이벤트 내용 3','2024-11-10 08:33:34','2024-11-17 08:33:34'),(4,'이벤트 제목 4','이벤트 내용 4','2024-11-10 08:33:34','2024-11-17 08:33:34'),(5,'이벤트 제목 5','이벤트 내용 5','2024-11-10 08:33:34','2024-11-17 08:33:34');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exchangereturn`
--

DROP TABLE IF EXISTS `exchangereturn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exchangereturn` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `orderitem_id` varchar(45) NOT NULL,
  `category` tinyint NOT NULL,
  `question` varchar(5000) NOT NULL,
  `answer` tinyint NOT NULL DEFAULT '1',
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exchangereturn`
--

LOCK TABLES `exchangereturn` WRITE;
/*!40000 ALTER TABLE `exchangereturn` DISABLE KEYS */;
INSERT INTO `exchangereturn` VALUES (1,1,'1',1,'질문내용',1,'2024-11-21 02:30:13'),(2,2,'2',2,'질문내용2',1,'2024-11-21 02:30:13'),(3,3,'3',3,'질문내용3',1,'2024-11-21 02:30:13'),(4,4,'4',1,'질문내용4',1,'2024-11-21 02:30:13'),(5,5,'5',2,'질문내용5',1,'2024-11-21 02:30:13');
/*!40000 ALTER TABLE `exchangereturn` ENABLE KEYS */;
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
  `content` varchar(10000) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=575 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'asdasdada','afdsdasfafdsfdsa','2024-11-10 08:31:40'),(2,'공지사항 제목 2ㅇㅁㄴㅁㅇㄴ','공지사항 내용 2를 수정함','2024-11-10 08:31:40'),(3,'공지사항 제목 3','공지사항 내용 3','2024-11-10 08:31:40'),(4,'공지사항 제목 4','공지사항 내용 4','2024-11-10 08:31:40'),(5,'공지사항 제목 5','공지사항 내용 5','2024-11-10 08:31:40');
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
  `orders_id` bigint NOT NULL DEFAULT '0',
  `status` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderitem`
--

LOCK TABLES `orderitem` WRITE;
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
INSERT INTO `orderitem` VALUES (1,19,4,1,1),(2,1,2,1,1),(3,1,2,1,1),(4,6,4,1,1),(5,15,4,1,1);
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
  `total_price` bigint NOT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
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
INSERT INTO `orders` VALUES (1,1,'2024-11-10 08:34:51',1325,4,1,1),(2,2,'2024-11-10 08:34:51',1132,5,3,3),(3,3,'2024-11-10 08:34:51',1290,5,2,2),(4,4,'2024-11-10 08:34:51',1172,3,4,5),(5,5,'2024-11-10 08:34:51',1484,5,5,4);
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
  `amount` bigint NOT NULL,
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
INSERT INTO `payment` VALUES (1,'2024-11-10 08:40:03',1043,1,1,1),(2,'2024-11-10 08:40:03',1091,2,0,3),(3,'2024-11-10 08:40:03',1194,2,1,4),(4,'2024-11-10 08:40:03',1342,1,1,5),(5,'2024-11-10 08:40:03',1011,2,2,2);
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
  `title` varchar(255) NOT NULL,
  `question` varchar(5000) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `answer` varchar(5000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna`
--

LOCK TABLES `qna` WRITE;
/*!40000 ALTER TABLE `qna` DISABLE KEYS */;
INSERT INTO `qna` VALUES (1,1,3,'문의 제목 1','문의 내용 1','2024-11-10 08:40:19','답변 내용 1'),(2,2,4,'문의 제목 2','문의 내용 2','2024-11-10 08:40:19','답변 내용 2'),(3,3,2,'문의 제목 3','문의 내용 3','2024-11-10 08:40:19','답변 내용 3'),(4,4,0,'문의 제목 4','문의 내용 4','2024-11-10 08:40:19','답변 내용 4'),(5,5,3,'문의 제목 5','문의 내용 5','2024-11-10 08:40:19','답변 내용 5');
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
  `title` varchar(255) NOT NULL,
  `comment` varchar(5000) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,1,1,1,'타이틀','리뷰 내용 1','2024-11-10 08:40:40'),(2,2,2,2,'타이틀 ','리뷰내용2','2024-11-21 07:35:07'),(3,3,3,3,'타이틀 ','리뷰 내용 3','2024-11-21 07:35:35'),(4,4,4,4,'타이틀 ','리뷰 내용임니다.4','2024-11-21 07:35:35'),(5,5,5,5,'타이틀 ','리뷰내용5','2024-11-21 04:07:54');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategory`
--

DROP TABLE IF EXISTS `subcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategory`
--

LOCK TABLES `subcategory` WRITE;
/*!40000 ALTER TABLE `subcategory` DISABLE KEYS */;
INSERT INTO `subcategory` VALUES (1,1,'서브카테고리 1 - 카테고리 1'),(2,1,'서브카테고리 2 - 카테고리 1'),(3,1,'서브카테고리 3 - 카테고리 1'),(4,2,'서브카테고리 1 - 카테고리 2'),(5,2,'서브카테고리 2 - 카테고리 2');
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
  `name` varchar(64) NOT NULL DEFAULT '이름',
  `tel` varchar(20) NOT NULL DEFAULT '전화번호',
  `addr` varchar(225) NOT NULL DEFAULT '주소',
  `addrextra` varchar(225) NOT NULL DEFAULT '상세주소',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `grade` int NOT NULL DEFAULT '0',
  `point` bigint NOT NULL DEFAULT '0',
  `provider` varchar(60) NOT NULL DEFAULT 'ezen',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'user1@ezbook.com','password1','사용자1','010-1234-5671','서울시 강남구1','역삼동 1231','2024-11-10 08:40:57','2024-11-21 00:28:30',1,3073,'ezen'),(2,'user2@ezbook.com','password2','사용자2','010-1234-5672','서울시 강남구2','역삼동 1232','2024-11-10 08:40:57','2024-11-21 00:28:30',3,9464,'ezen'),(3,'user3@ezbook.com','password3','사용자3','010-1234-5673','서울시 강남구3','역삼동 1233','2024-11-10 08:40:57','2024-11-21 00:28:30',4,4147,'ezen'),(4,'user4@ezbook.com','password4','사용자4','010-1234-5674','서울시 강남구4','역삼동 1234','2024-11-10 08:40:57','2024-11-21 00:28:30',3,6297,'ezen'),(5,'user5@ezbook.com','password5','사용자5','010-1234-5675','서울시 강남구5','역삼동 1235','2024-11-10 08:40:57','2024-11-21 00:28:30',1,1810,'ezen');
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

-- Dump completed on 2024-11-21 17:59:25
