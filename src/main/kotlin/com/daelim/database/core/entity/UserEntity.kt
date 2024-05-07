package com.daelim.database.core.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")  // "user" 대신 "users" 사용을 권장
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var username: String,
    var password: String // 실제 애플리케이션에서는 비밀번호를 해시하여 저장
)
/*
CREATE TABLE `user` (
	`id` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_0900_ai_ci',
	`password` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_0900_ai_ci',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
 */