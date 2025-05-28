-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: kids_learning
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
-- Dumping data for table `children_name`
--

LOCK TABLES `children_name` WRITE;
/*!40000 ALTER TABLE `children_name` DISABLE KEYS */;
INSERT INTO `children_name` VALUES (1,'אורי','male'),(2,'נועם','male'),(3,'דן','male'),(4,'יואב','male'),(5,'רועי','male'),(6,'אדם','male'),(7,'איתן','male'),(8,'עידו','male'),(9,'טל','male'),(10,'נמרוד','male'),(11,'אופיר','male'),(12,'שחר','male'),(13,'מתן','male'),(14,'אבירם','male'),(15,'אביב','male'),(16,'עמרי','male'),(17,'עמרי','male'),(18,'אלעד','male'),(19,'דניאל','male'),(20,'שון','male'),(21,'ליאור','male'),(22,'תום','male'),(23,'עידו','male'),(24,'נדב','male'),(25,'ניב','male'),(26,'סער','male'),(27,'ברק','male'),(28,'יונתן','male'),(29,'שלו','male'),(30,'אור','male'),(31,'מאור','male'),(32,'עידן','male'),(33,'שגיא','male'),(34,'סער','male'),(35,'אליה','male'),(36,'גיא','male'),(37,'רוני','male'),(38,'עופר','male'),(39,'אילון','male'),(40,'איציק','male'),(41,'רם','male'),(42,'מיכאל','male'),(43,'אשר','male'),(44,'יהונתן','male'),(45,'עמית','male'),(46,'אדר','male'),(47,'אלמוג','male'),(48,'רפאל','male'),(49,'צחי','male'),(50,'יאיר','male'),(51,'עוז','male'),(52,'אוהד','male'),(53,'ירין','male'),(54,'אלון','male'),(55,'נרי','male'),(56,'בר','male'),(57,'עוזי','male'),(58,'יואל','male'),(59,'דביר','male'),(60,'מקס','male'),(61,'גדי','male'),(62,'שמואל','male'),(63,'יצחק','male'),(64,'עמנואל','male'),(65,'שמעון','male'),(66,'בנימין','male'),(67,'נחום','male'),(68,'משה','male'),(69,'ציון','male'),(70,'יוסף','male'),(71,'אוריאל','male'),(72,'נחשון','male'),(73,'נמרי','male'),(74,'נריה','male'),(75,'אליה','male'),(76,'דגן','male'),(77,'חגי','male'),(78,'דור','male'),(79,'ישי','male'),(80,'יובל','male'),(81,'צביקה','male'),(82,'חיים','male'),(83,'איל','male'),(84,'ליאון','male'),(85,'אייל','male'),(86,'ישי','male'),(87,'אמיר','male'),(88,'אוהב','male'),(89,'אדריאל','male'),(90,'שלומי','male'),(91,'שקד','male'),(92,'תמיר','male'),(93,'רני','male'),(94,'אופק','male'),(95,'שילה','male'),(96,'אורן','male'),(97,'אסף','male'),(98,'איילון','male'),(99,'רועיאל','male'),(100,'זיו','male'),(101,'נועה','female'),(102,'שירה','female'),(103,'רוני','female'),(104,'תמר','female'),(105,'איילת','female'),(106,'מאיה','female'),(107,'עדי','female'),(108,'שני','female'),(109,'דנה','female'),(110,'יעל','female'),(111,'ליה','female'),(112,'מיכל','female'),(113,'עפרי','female'),(114,'הילה','female'),(115,'אור','female'),(116,'דניאל','female'),(117,'אוריין','female'),(118,'רעות','female'),(119,'ניצן','female'),(120,'נוי','female'),(121,'לי','female'),(122,'טליה','female'),(123,'אביגיל','female'),(124,'הדס','female'),(125,'מרווה','female'),(126,'מור','female'),(127,'רוני','female'),(128,'ליאן','female'),(129,'שרון','female'),(130,'סיון','female'),(131,'ענבל','female'),(132,'שקד','female'),(133,'רותם','female'),(134,'אסתר','female'),(135,'שרה','female'),(136,'צופיה','female'),(137,'עדן','female'),(138,'שלי','female'),(139,'עדי','female'),(140,'אושרת','female'),(141,'נוגה','female'),(142,'יסמין','female'),(143,'כרמל','female'),(144,'מיקה','female'),(145,'גפן','female'),(146,'אופק','female'),(147,'אפרת','female'),(148,'רחל','female'),(149,'טלי','female'),(150,'בר','female'),(151,'לינוי','female'),(152,'שיר','female'),(153,'דפנה','female'),(154,'מרים','female'),(155,'אלה','female'),(156,'גילי','female'),(157,'אורטל','female'),(158,'אביטל','female'),(159,'יובל','female'),(160,'ספיר','female'),(161,'דנהלי','female'),(162,'טל','female'),(163,'אמילי','female'),(164,'נעמי','female'),(165,'זהר','female'),(166,'נוגית','female'),(167,'אילה','female'),(168,'עיינה','female'),(169,'מיכלי','female'),(170,'אדל','female'),(171,'רומי','female'),(172,'איימי','female'),(173,'אביה','female'),(174,'נופר','female'),(175,'מאור','female'),(176,'מלי','female'),(177,'שני','female'),(178,'עירית','female'),(179,'מעיין','female'),(180,'חגית','female'),(181,'אריאל','female'),(182,'אלהלי','female'),(183,'שושנה','female'),(184,'זהבית','female'),(185,'יערה','female'),(186,'עמית','female'),(187,'תהל','female'),(188,'מילנה','female'),(189,'כריסטינה','female'),(190,'אנאל','female'),(191,'הגר','female'),(192,'בת שבע','female'),(193,'ברכה','female'),(194,'ציפי','female'),(195,'לילך','female'),(196,'רינת','female'),(197,'אבישג','female'),(198,'חני','female'),(199,'אלישבע','female'),(200,'תמרה','female');
/*!40000 ALTER TABLE `children_name` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-28 12:51:48
