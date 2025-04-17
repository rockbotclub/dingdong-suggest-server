CREATE TABLE `admins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `admin_wxid` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员微信ID',
  `admin_name` varchar(127) NOT NULL DEFAULT '' COMMENT '管理员姓名',
  `org_id` varchar(255) NOT NULL DEFAULT '' COMMENT '组织UUID',
  `admin_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '管理员手机号',
  `admin_passwd` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wxid` (`admin_wxid`) USING BTREE,
  KEY `idx_org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员信息表';

CREATE TABLE `organizations` (
  `id` varchar(255) NOT NULL DEFAULT '' COMMENT '组织UUID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `org_name` varchar(255) NOT NULL DEFAULT '' COMMENT '组织名称',
  `address` varchar(255) DEFAULT '' COMMENT '办公地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织信息';

CREATE TABLE `suggestions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '微建议标题',
  `problem_description` text NOT NULL COMMENT '问题描述',
  `problem_analysis` text COMMENT '问题分析（可以为空）',
  `suggestion` text NOT NULL COMMENT '具体建议',
  `expected_outcome` text COMMENT '预期效果（可以为空）',
  `image_urls` json DEFAULT NULL COMMENT '相关图片URL列表（存储为JSON数组）',
  `user_wxid` varchar(255) NOT NULL DEFAULT '' COMMENT '提交人微信ID',
  `status` tinyint(4) NOT NULL COMMENT '建议的状态枚举',
  `org_id` varchar(255) NOT NULL DEFAULT '' COMMENT '组织UUID',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`gmt_create`) USING BTREE,
  KEY `idx_submitter_wxid` (`user_wxid`) USING BTREE,
  KEY `idx_org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储用户提交的建议';

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `wxid` varchar(255) NOT NULL DEFAULT '' COMMENT '微信ID',
  `user_name` varchar(127) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `user_org` varchar(255) NOT NULL DEFAULT '' COMMENT '用户单位',
  `user_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `status` tinyint(255) NOT NULL COMMENT '用户状态枚举，0: 待认证; 1: 已认证',
  `org_id` varchar(255) NOT NULL DEFAULT '' COMMENT '组织UUID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wxid` (`wxid`) USING BTREE,
  KEY `idx_org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';