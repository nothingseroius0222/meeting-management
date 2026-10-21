-- Table structure for table `meeting_reservation`
DROP TABLE IF EXISTS `meeting_reservation`;
CREATE TABLE `meeting_reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `room_id` bigint(20) NOT NULL COMMENT '会议室ID',
  `user_id` bigint(20) NOT NULL COMMENT '预约用户ID',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会议主题',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '会议描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `attendee_count` int(11) DEFAULT '1' COMMENT '参会人数',
  `status` int(11) DEFAULT '0' COMMENT '状态：0-待审核，1-已通过，2-已拒绝，3-已取消',
  `audit_remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `meeting_reservation_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `meeting_room` (`id`),
  CONSTRAINT `meeting_reservation_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议预约表';

-- Data for table `meeting_reservation`
INSERT INTO `meeting_reservation` VALUES (1, 6, 2, 'user', 'user', '2025-09-05 16:00:00.0', '2025-09-05 16:00:00.0', 4, 1, '', '2025-09-06 15:57:17.0', '2025-09-06 15:57:17.0', 0);
INSERT INTO `meeting_reservation` VALUES (2, 2, 2, 'user', 'user', '2025-09-15 16:00:00.0', '2025-09-17 16:00:00.0', 3, 0, NULL, '2025-09-06 16:00:23.0', '2025-09-06 16:00:23.0', 0);

-- Table structure for table `meeting_room`
DROP TABLE IF EXISTS `meeting_room`;
CREATE TABLE `meeting_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
  `room_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会议室名称',
  `location` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会议室位置',
  `capacity` int(11) NOT NULL COMMENT '容量',
  `equipment` text COLLATE utf8mb4_unicode_ci COMMENT '设备描述',
  `status` int(11) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室表';

-- Data for table `meeting_room`
INSERT INTO `meeting_room` VALUES (1, '会议室A', '1楼101室', 10, '投影仪、白板、音响设备', 1, '2025-09-06 15:30:30.0', '2025-09-06 15:30:30.0', 0);
INSERT INTO `meeting_room` VALUES (2, '会议室B', '2楼201室', 20, '投影仪、白板、音响设备、视频会议设备', 1, '2025-09-06 15:30:30.0', '2025-09-06 15:30:30.0', 0);
INSERT INTO `meeting_room` VALUES (3, '会议室C', '3楼301室', 30, '投影仪、白板、音响设备、视频会议设备、同声传译设备', 1, '2025-09-06 15:30:30.0', '2025-09-06 15:30:30.0', 0);
INSERT INTO `meeting_room` VALUES (4, '小会议室', '1楼102室', 6, '投影仪、白板', 1, '2025-09-06 15:30:30.0', '2025-09-06 15:30:30.0', 0);
INSERT INTO `meeting_room` VALUES (5, '大会议室', '2楼202室', 50, '投影仪、白板、音响设备、视频会议设备、同声传译设备、LED显示屏', 1, '2025-09-06 15:30:30.0', '2025-09-06 15:30:30.0', 0);
INSERT INTO `meeting_room` VALUES (6, '测试1', '1', 10, '测试1', 1, '2025-09-06 15:52:59.0', '2025-09-06 15:52:59.0', 0);

-- Table structure for table `sys_user`
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `user_type` int(11) DEFAULT '0' COMMENT '用户类型：0-普通用户，1-管理员',
  `status` int(11) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- Data for table `sys_user`
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$VRcUnqzN7XuTS3SDtMxIZubfQw52oakGzVhYcInOkzJP.ItLQ7kFa', '系统管理员', NULL, NULL, 1, 1, '2025-09-06 15:30:30.0', '2025-09-06 15:30:30.0', 0);
INSERT INTO `sys_user` VALUES (2, 'user', '$2a$10$lVyAYdxDnkAjQeDUTyZJrOMRhhBedBXvn/5mJk2Xc2s6WaBhFMqYi', 'user', 'user@qq.com', '15915311111', 0, 1, '2025-09-06 15:56:42.0', '2025-09-06 15:56:42.0', 0);

-- Table structure for sys_notification
DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE `sys_notification`  (
                                     `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
                                     `user_id` bigint(0) NOT NULL COMMENT '接收人ID',
                                     `type` tinyint(0) NOT NULL COMMENT '通知类型: 1=系统通知, 1=预约通知, 0=会议室停用通知',
                                     `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
                                     `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知内容',
                                     `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转链接（如 /reservation/my）',
                                     `is_read` tinyint(0) NULL DEFAULT 0 COMMENT '是否已读: 0=未读, 1=已读',
                                     `target_id` bigint(0) NULL DEFAULT NULL COMMENT '关联业务ID（如预约ID、会议室ID）',
                                     `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                     `read_at` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
                                     `deleted` tinyint(0) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知表' ROW_FORMAT = Dynamic;

-- Records of sys_notification
INSERT INTO `sys_notification` VALUES (16, 4, 0, '【重要通知】会议室已停用', 'user02，您好！\n会议室《会议室B》已停用，您有 2 个预约在该会议室，已被自动取消。\n请重新预约其他会议室。', NULL, 0, 2, '2026-07-17 11:39:17', '2026-07-17 11:39:28', 0);

