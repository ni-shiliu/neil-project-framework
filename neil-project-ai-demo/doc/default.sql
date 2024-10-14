CREATE TABLE `neil`.`ai_application_config` (
  `id` BIGINT NOT NULL COMMENT '主键ID',
  `application_no` VARCHAR(32) NOT NULL COMMENT '应用NO',
  `type` VARCHAR(45) NOT NULL COMMENT '配置类型：PROMPT:提示词，PROLOGUE:开场白',
  `content` TEXT NOT NULL COMMENT '配置内容',
  `is_deleted` CHAR NOT NULL COMMENT '是否删除',
  `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
  `creator_id` BIGINT NOT NULL COMMENT '创建人',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  `modifier_id` BIGINT NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  INDEX `idx_application_no` (`application_no`),
  INDEX `idx_gmt_created` (`gmt_created`),
  INDEX `idx_gmt_modified` (`gmt_modified`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = 'AI应用配置';
