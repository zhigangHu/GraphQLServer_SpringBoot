CREATE DATABASE /*!32312 IF NOT EXISTS*/`graphql` /*!40100 DEFAULT CHARACTER SET utf8 */;
 
USE `graphql`;
 
/*Table structure for table `author` */
 
DROP TABLE IF EXISTS `author`;
 
CREATE TABLE `author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `first_name` varchar(50) DEFAULT NULL COMMENT 'firstName',
  `last_name` varchar(50) DEFAULT NULL COMMENT 'lastName',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 
/*Table structure for table `book` */
 
DROP TABLE IF EXISTS `book`;
 
CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `author_id` bigint(20) NOT NULL,
  `isbn` varchar(255) DEFAULT NULL,
  `page_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 
/*Table structure for table `user` */
 
DROP TABLE IF EXISTS `user`;