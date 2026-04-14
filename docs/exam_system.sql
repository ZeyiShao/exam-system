/*
 Navicat Premium Dump SQL

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80045 (8.0.45)
 Source Host           : localhost:3306
 Source Schema         : exam_system

 Target Server Type    : MySQL
 Target Server Version : 80045 (8.0.45)
 File Encoding         : 65001

 Date: 14/04/2026 10:13:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class_course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `class_course_teacher`;
CREATE TABLE `class_course_teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_id` int NOT NULL COMMENT '班级ID',
  `course_id` int NOT NULL COMMENT '课程ID',
  `teacher_id` bigint NOT NULL COMMENT '教师ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班级-课程-教师关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class_course_teacher
-- ----------------------------
INSERT INTO `class_course_teacher` VALUES (1, 2, 2, 4);

-- ----------------------------
-- Table structure for class_info
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班级名称',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年级',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级说明',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班级表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class_info
-- ----------------------------
INSERT INTO `class_info` VALUES (2, '软件工程221班', '2022级', '软件工程', '软件工程专业本科班级', '2026-04-03 16:17:50');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `course_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程编号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程说明',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (2, 'Java基础（修改）', 'JAVA001', 'Java程序设计基础课程-更新', 'ENABLED', '2026-04-03 15:43:29');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '考试ID',
  `exam_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考试名称',
  `paper_id` int NOT NULL COMMENT '试卷ID',
  `class_id` int NOT NULL COMMENT '班级ID',
  `course_id` int NOT NULL COMMENT '课程ID',
  `teacher_id` bigint NOT NULL COMMENT '教师ID',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `duration` int NOT NULL COMMENT '考试时长（分钟）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'NOT_STARTED' COMMENT '状态：NOT_STARTED/ONGOING/FINISHED/CANCELLED',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (2, 'Java在线测试', 2, 2, 2, 4, '2026-04-05 00:00:00', '2026-04-05 23:59:59', 90, 'ONGOING', '2026-04-05 10:13:53');

-- ----------------------------
-- Table structure for exam_answer
-- ----------------------------
DROP TABLE IF EXISTS `exam_answer`;
CREATE TABLE `exam_answer`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '答题记录ID',
  `record_id` int NOT NULL COMMENT '考试记录ID',
  `question_id` int NOT NULL COMMENT '题目ID',
  `user_answer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生答案',
  `correct_answer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正确答案',
  `is_correct` tinyint(1) NULL DEFAULT 0 COMMENT '是否答对',
  `score` int NULL DEFAULT 0 COMMENT '本题得分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '答题记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_answer
-- ----------------------------
INSERT INTO `exam_answer` VALUES (1, 1, 1, 'B', 'B', 1, 10);
INSERT INTO `exam_answer` VALUES (2, 1, 2, 'C', 'C', 1, 10);
INSERT INTO `exam_answer` VALUES (3, 1, 3, 'A,B,D', 'A,B,D', 1, 20);

-- ----------------------------
-- Table structure for exam_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '考试记录ID',
  `exam_id` int NOT NULL COMMENT '考试ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `score` int NULL DEFAULT 0 COMMENT '考试得分',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始答题时间',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '交卷时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'DOING' COMMENT '状态：DOING/FINISHED/ABSENT',
  `is_pass` tinyint(1) NULL DEFAULT 0 COMMENT '是否及格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_record
-- ----------------------------
INSERT INTO `exam_record` VALUES (1, 2, 2, 40, '2026-04-05 10:21:38', '2026-04-05 10:22:03', 'FINISHED', 1);

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '试卷ID',
  `paper_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '试卷名称',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `total_score` int NOT NULL COMMENT '总分',
  `duration` int NOT NULL COMMENT '考试时长（分钟）',
  `pass_score` int NOT NULL COMMENT '及格分',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人（教师ID）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'PENDING' COMMENT '审核状态：PENDING/APPROVED/REJECTED',
  `audit_user` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驳回原因',
  `paper_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'MANUAL' COMMENT '组卷方式：MANUAL/RANDOM',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (2, '软件工程测试卷A（修改后）', NULL, 40, 90, 24, 1, 'PENDING', NULL, NULL, NULL, 'MANUAL', '2026-04-01 13:43:04');
INSERT INTO `paper` VALUES (3, '随机测试卷A', NULL, 40, 60, 24, 1, 'PENDING', NULL, NULL, NULL, 'MANUAL', '2026-04-03 09:18:03');

-- ----------------------------
-- Table structure for paper_question
-- ----------------------------
DROP TABLE IF EXISTS `paper_question`;
CREATE TABLE `paper_question`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `paper_id` int NOT NULL COMMENT '试卷ID',
  `question_id` int NOT NULL COMMENT '题目ID',
  `score` int NOT NULL COMMENT '该题分值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_question
-- ----------------------------
INSERT INTO `paper_question` VALUES (7, 2, 1, 10);
INSERT INTO `paper_question` VALUES (8, 2, 2, 10);
INSERT INTO `paper_question` VALUES (9, 2, 3, 20);
INSERT INTO `paper_question` VALUES (10, 3, 2, 10);
INSERT INTO `paper_question` VALUES (11, 3, 6, 10);
INSERT INTO `paper_question` VALUES (12, 3, 3, 10);
INSERT INTO `paper_question` VALUES (13, 3, 8, 10);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `question_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题目内容',
  `option_a` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项A',
  `option_b` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项B',
  `option_c` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项C',
  `option_d` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项D',
  `correct_answer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正确答案（A/B/C/D 或 true/false）',
  `question_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题型（single/multiple/judge）',
  `difficulty` int NULL DEFAULT 1 COMMENT '难度（1简单 2中等 3困难）',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `course` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程分类',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人（教师ID）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'PENDING' COMMENT '审核状态：PENDING/APPROVED/REJECTED',
  `audit_user` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驳回原因',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 'Java中哪个是基本数据类型？', 'String', 'int', 'List', 'Object', 'B', 'single', 1, NULL, 'Java基础', 1, 'PENDING', NULL, NULL, NULL, '2026-03-25 08:46:10');
INSERT INTO `question` VALUES (2, '在SQL中，用于查询数据的关键字是？', 'INSERT', 'UPDATE', 'SELECT', 'DELETE', 'C', 'single', 1, NULL, '数据库', 1, 'PENDING', NULL, NULL, NULL, '2026-03-25 09:18:24');
INSERT INTO `question` VALUES (3, '以下哪些属于Java的基本数据类型？', 'int', 'double', 'String', 'boolean', 'A,B,D', 'multiple', 2, NULL, 'Java基础', 1, 'PENDING', NULL, NULL, NULL, '2026-03-26 13:36:55');
INSERT INTO `question` VALUES (4, 'Java是一种面向对象的编程语言。', NULL, NULL, NULL, NULL, 'true', 'judge', 1, NULL, 'Java基础', 1, 'PENDING', NULL, NULL, NULL, '2026-04-01 08:34:53');
INSERT INTO `question` VALUES (5, '下列哪种数据结构属于线性结构？', '二叉树', '图', '数组', '堆', 'C', 'single', 2, NULL, '数据结构', 1, 'PENDING', NULL, NULL, NULL, '2026-04-01 08:38:06');
INSERT INTO `question` VALUES (6, '在Java中，下列哪个关键字用于继承类？', 'implements', 'extends', 'import', 'package', 'B', 'single', 1, NULL, 'Java基础', 1, 'PENDING', NULL, NULL, NULL, '2026-04-01 08:38:25');
INSERT INTO `question` VALUES (7, '以下哪些属于关系型数据库？', 'MySQL', 'Oracle', 'MongoDB', 'SQL Server', 'A,B,D', 'multiple', 2, NULL, '数据库', 1, 'PENDING', NULL, NULL, NULL, '2026-04-01 08:41:54');
INSERT INTO `question` VALUES (8, '数组是一种非线性数据结构。', NULL, NULL, NULL, NULL, 'false', 'judge', 1, NULL, '数据结构', 1, 'PENDING', NULL, NULL, NULL, '2026-04-01 08:42:22');

-- ----------------------------
-- Table structure for student_class
-- ----------------------------
DROP TABLE IF EXISTS `student_class`;
CREATE TABLE `student_class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `class_id` int NOT NULL COMMENT '班级ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生-班级关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_class
-- ----------------------------
INSERT INTO `student_class` VALUES (2, 2, 2);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '123456', '管理员', 'ADMIN', '2026-03-24 09:02:21');
INSERT INTO `sys_user` VALUES (2, 'student1', '654321', '学生一号', 'STUDENT', '2026-03-24 09:02:21');
INSERT INTO `sys_user` VALUES (4, 'teacher1', '123456', '教师一号', 'TEACHER', '2026-04-04 08:52:38');

-- ----------------------------
-- Table structure for teacher_course
-- ----------------------------
DROP TABLE IF EXISTS `teacher_course`;
CREATE TABLE `teacher_course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `teacher_id` bigint NOT NULL COMMENT '教师ID',
  `course_id` int NOT NULL COMMENT '课程ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师-课程关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher_course
-- ----------------------------
INSERT INTO `teacher_course` VALUES (2, 4, 2);

SET FOREIGN_KEY_CHECKS = 1;
