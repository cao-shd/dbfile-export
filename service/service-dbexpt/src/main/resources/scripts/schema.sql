CREATE DATABASE IF NOT EXISTS `dbexpt` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

USE `dbexpt`;

DROP TABLE IF EXISTS `t_file_export_instance`;
CREATE TABLE `t_file_export_instance` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父实例ID',
  `task_status` int NOT NULL DEFAULT '0' COMMENT '实例状态',
  `process_node` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理节点',
  `file_cnt` int NOT NULL DEFAULT '1' COMMENT '文件总数',
  `done_file_cnt` int NOT NULL DEFAULT '0' COMMENT '完成文件总数',
  `file_type` int NOT NULL DEFAULT '1' COMMENT '文件类型(1-文本文件)',
  `file_type_id` bigint NOT NULL COMMENT '文件类型ID',
  `file_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输出路径',
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输出文件名(不带扩展名)',
  `file_prefix` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '输出文件后缀',
  `db_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库别名',
  `pre_logic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '前置生成逻辑',
  `main_logic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '主体生成逻辑',
  `post_logic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '后置生成逻辑',
  `callback_logic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '回调处理逻辑',
  `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态(0-可用/1-删除)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='导出文件实例';

DROP TABLE IF EXISTS `t_file_export_type_text`;
CREATE TABLE `t_file_export_type_text` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `charset` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'UTF-8' COMMENT '输出文件字符集',
  `line_separator` int NOT NULL DEFAULT '1' COMMENT '行分割符',
  `field_separator` int NOT NULL DEFAULT '1' COMMENT '字段分割符',
  `warp_with` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '包围字符串',
  `auto_header` int NOT NULL DEFAULT '0' COMMENT '是否自动生成头(0-不自动生成/1-自动生成)',
  `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态(0-可用/1-删除)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件导出文本类型';

DROP TABLE IF EXISTS `t_file_export_path`;
CREATE TABLE `t_file_export_path` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `path_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '路径名称',
  `location` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '路径地址',
  `version` int DEFAULT '0' COMMENT '版本号',
  `status` int DEFAULT '0' COMMENT '状态(0-可用/1-删除)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件导出路径';

DROP TABLE IF EXISTS `t_file_export_workspace`;
CREATE TABLE `t_file_export_workspace` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `instance_id` bigint NOT NULL COMMENT '实例ID',
  `instance_parent_id` bigint DEFAULT NULL COMMENT '父实例ID',
  `file_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '工作路径',
  `file_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文件名称',
  `version` int DEFAULT '0' COMMENT '版本号',
  `status` int DEFAULT '0' COMMENT '状态(0-可用/1-删除)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件导出工作空间';


-- TEST DATA
INSERT INTO t_file_export_path (id,path_name,location,version,status,create_time,update_time,update_by) VALUES
	 (1,'test_path1','/file_export/test1/',0,0,'2023-04-29 16:51:30','2023-04-29 16:51:30','test'),
	 (2,'test_path2','/file_export/test2/',0,0,'2023-04-29 16:51:30','2023-04-29 16:51:30','test'),
	 (3,'test_path3','/file_export/test3/',0,0,'2023-04-29 16:51:30','2023-04-29 16:51:30','test'),
	 (4,'test_path4','/file_export/test4/',0,0,'2023-04-29 16:51:30','2023-04-29 16:51:30','test'),
	 (5,'test_path5','/file_export/test5/',0,0,'2023-04-29 16:51:31','2023-04-29 16:51:31','test');

INSERT INTO t_file_export_instance (id,parent_id,task_status,process_node,file_cnt,done_file_cnt,file_type,file_type_id,file_path,file_name,file_prefix,db_alias,pre_logic,main_logic,post_logic,callback_logic,version,status,create_time,update_time,update_by) VALUES
	 (1,NULL,0,'127.0.0.1',1,0,1,1,'test_path1','test_1','.txt','his','H1,H2,H3','select 1 as H1, 2 as H2, 3 as H3',NULL,NULL,0,0,'2023-05-02 10:33:34','2023-05-03 20:05:23','test'),
	 (2,5,0,'127.0.0.1',1,1,1,2,'','test_2','.txt','his','H1,H2,H3','select 1 as H1, 2 as H2, 3 as H3',NULL,NULL,0,0,'2023-05-02 10:33:34','2023-05-03 19:46:01','test'),
	 (3,5,0,'127.0.0.1',1,1,1,3,'','test_3','.txt','his','H1,H2,H3','select 1 as H1, 2 as H2, 3 as H3',NULL,NULL,0,0,'2023-05-02 10:33:34','2023-05-03 19:46:01','test'),
	 (4,5,0,'127.0.0.1',1,1,1,4,'','test_4','.txt','his','H1,H2,H3','select 1 as H1, 2 as H2, 3 as H3',NULL,NULL,0,0,'2023-05-02 10:33:34','2023-05-03 19:46:01','test'),
	 (5,NULL,0,'127.0.0.1',3,3,2,5,'test_path5','test_5','.zip','his','','',NULL,NULL,0,0,'2023-05-02 10:33:35','2023-05-03 19:46:11','test');

INSERT INTO t_file_export_type_text (id,charset,line_separator,field_separator,warp_with,auto_header,version,status,create_time,update_time,update_by) VALUES
	 (1,'UTF-8',1,1,'',0,0,0,'2023-05-01 23:13:01','2023-05-01 23:13:01','test'),
	 (2,'UTF-8',1,2,'',0,0,0,'2023-05-01 23:13:01','2023-05-01 23:13:01','test'),
	 (3,'UTF-8',1,3,'',0,0,0,'2023-05-01 23:13:01','2023-05-01 23:13:01','test'),
	 (4,'UTF-8',1,4,'',0,0,0,'2023-05-01 23:13:01','2023-05-01 23:13:01','test'),
	 (5,'GBK',1,2,'',0,0,0,'2023-05-01 23:13:01','2023-05-01 23:13:01','test');


CREATE DATABASE IF NOT EXISTS `dbexpt_his` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

USE `dbexpt_his`;
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `age` int DEFAULT NULL COMMENT '年龄',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `version` int DEFAULT '0' COMMENT '版本号',
  `status` int DEFAULT '0' COMMENT '状态(0-可用/1-删除)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户';

-- LOAD DATA LOCAL INFILE '/Project/dbexpt/run/m_user.csv' INTO TABLE m_user FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS;
