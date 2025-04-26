CREATE TABLE `jwt_tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `token` varchar(512) NOT NULL DEFAULT '' COMMENT 'JWT Token',
  `wxid` varchar(64) NOT NULL DEFAULT '' COMMENT '微信ID',
  `gmt_expired` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`) USING BTREE,
  KEY `idx_wx_id` (`wxid`) USING BTREE,
  KEY `idx_expired` (`gmt_expired`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='JWT Token存储表'