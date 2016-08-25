-- MySQL dump 10.13  Distrib 5.7.13, for Linux (x86_64)
--
-- Host: localhost    Database: muster
-- ------------------------------------------------------
-- Server version	5.7.13-0ubuntu0.16.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `devices`
--

DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `devices` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MAC` varchar(17) NOT NULL,
  `studentID` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  UNIQUE KEY `studentDevice_ID_uindex` (`ID`),
  UNIQUE KEY `studentDevice_MAC_uindex` (`MAC`),
  KEY `studentDevice_students_ID_fk` (`studentID`),
  CONSTRAINT `studentDevice_students_ID_fk` FOREIGN KEY (`studentID`) REFERENCES `students` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devices`
--

LOCK TABLES `devices` WRITE;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
INSERT INTO `devices` VALUES (1,'1:2:3:4',1,'Test'),(2,'70:3E:AC:D8:7F:26',2,'Phone'),(3,'DC:37:14:34:C8:F9',3,'Phone'),(4,'D0:59:E4:95:87:64',4,'Phone'),(5,'8C:29:37:1C:FC:63',1,'Phone'),(6,'9C:FC:01:BF:54:68',5,'Phone'),(7,'F4:42:8F:62:C5:32',6,'Phone'),(8,'B8:5A:73:14:40:EC',7,'Phone'),(9,'10:41:7F:1E:CB:BA',8,'Phone');
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musters`
--

DROP TABLE IF EXISTS `musters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `musters` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `datestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deviceID` int(11) NOT NULL,
  `nodeID` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `musters_ID_uindex` (`ID`),
  KEY `musters_studentDevice_ID_fk` (`deviceID`),
  CONSTRAINT `musters_studentDevice_ID_fk` FOREIGN KEY (`deviceID`) REFERENCES `devices` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musters`
--

LOCK TABLES `musters` WRITE;
/*!40000 ALTER TABLE `musters` DISABLE KEYS */;
INSERT INTO `musters` VALUES (24,'2016-08-23 17:49:08',1,'Node1');
/*!40000 ALTER TABLE `musters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `students` (
  `fname` varchar(20) DEFAULT NULL,
  `lname` varchar(20) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `students_ID_uindex` (`ID`),
  UNIQUE KEY `students_email_uindex` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('Micah','Akin','mpakin1@nps.edu',1),('Mike','Harris','dmharris@nps.edu',2),('Andrew','Delding','arbelding@nps.edu',3),('Paul','Haagenson','pdhaagen@nps.edu',4),('Victor','Castro','vgcastro@nps.edu',5),('Raven','Holm','rrholm@nps.edu',6),('Steven','Thompson','skthomps@nps.edu',7),('Devin','Smiley','dsmiley@nps.edu',8);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-23 13:12:29
