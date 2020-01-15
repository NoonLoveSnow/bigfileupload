/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.6.39 : Database - big_file_upload
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`big_file_upload` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `big_file_upload`;

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(1000) DEFAULT NULL,
  `md5` varchar(1000) DEFAULT NULL,
  `location` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `md5` (`md5`(255))
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

/*Data for the table `file` */

insert  into `file`(`id`,`file_name`,`md5`,`location`,`create_time`) values 
(62,'SpringBoot资料.zip','ee567598284f8b128edd1d81a96a2877','C:\\files\\ee567598284f8b128edd1d81a96a2877\\SpringBoot资料.zip','2020-01-16 03:52:02');

/*Table structure for table `file_block` */

DROP TABLE IF EXISTS `file_block`;

CREATE TABLE `file_block` (
  `md5` varchar(1000) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `location` varchar(1000) DEFAULT NULL,
  `chunk` int(11) NOT NULL,
  `file_name` varchar(1000) DEFAULT NULL,
  `chunk_size` int(11) DEFAULT NULL,
  PRIMARY KEY (`md5`(128),`chunk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `file_block` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
