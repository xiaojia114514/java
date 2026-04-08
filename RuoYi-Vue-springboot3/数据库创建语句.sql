-- 1. 课程表 (course)
CREATE TABLE `course` (
  `course_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `course_code` varchar(50) NOT NULL COMMENT '课程代码',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `course_english_name` varchar(200) DEFAULT NULL COMMENT '英文名称',
  `course_nature` varchar(50) DEFAULT NULL COMMENT '课程性质',
  `total_hours` int(4) DEFAULT 0 COMMENT '学时',
  `credit` decimal(3,1) DEFAULT 0 COMMENT '学分',
  `semester` varchar(20) DEFAULT NULL COMMENT '开课学期',
  `class_name` varchar(100) DEFAULT NULL COMMENT '授课班级',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- 2. 教学大纲表 (syllabus)
CREATE TABLE `syllabus` (
  `syllabus_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '大纲ID',
  `course_id` bigint(20) DEFAULT NULL COMMENT '课程ID（唯一约束，允许为空）',
  `syllabus_name` varchar(200) NOT NULL COMMENT '文件名称',
  `syllabus_file` varchar(500) NOT NULL COMMENT '文件路径',
  `syllabus_size` bigint(20) DEFAULT 0 COMMENT '文件大小',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`syllabus_id`),
  UNIQUE KEY `uk_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教学大纲表';

-- 3. 试卷实质审查表 (paper_review)
CREATE TABLE `paper_review` (
  `review_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '审查ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID（唯一约束）',
  `review_name` varchar(200) NOT NULL COMMENT '文件名称',
  `review_file` varchar(500) NOT NULL COMMENT '文件路径',
  `review_size` bigint(20) DEFAULT 0 COMMENT '文件大小',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `uk_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷实质审查表';

-- 4. 考试成绩单 (exam_score)
CREATE TABLE `exam_score` (
  `score_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '成绩单ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `score_name` varchar(200) NOT NULL COMMENT '文件名称',
  `score_file` varchar(500) NOT NULL COMMENT '文件路径',
  `score_size` bigint(20) DEFAULT 0 COMMENT '文件大小',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`score_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试成绩单';

-- 5. 成绩细目表 (score_detail)
CREATE TABLE `score_detail` (
  `score_detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '细目表ID',
  `score_id` bigint(20) NOT NULL COMMENT '成绩单ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `detail_name` varchar(200) NOT NULL COMMENT '文件名称',
  `detail_file` varchar(500) NOT NULL COMMENT '文件路径',
  `detail_size` bigint(20) DEFAULT 0 COMMENT '文件大小',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`score_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩细目表';

-- 6. 课程教学质量分析报告表 (quality_report)
CREATE TABLE `quality_report` (
  `report_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `report_name` varchar(200) NOT NULL COMMENT '报告名称',
  `report_content` text COMMENT '报告内容',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程教学质量分析报告表';
