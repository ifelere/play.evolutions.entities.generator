/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  ifelere
 * Created: Aug 26, 2021
 */

-- User roles


-- !Ups
CREATE TABLE user_groups(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    congregation_id bigint(20) NOT NULL,
    name varchar(255),
    PRIMARY KEY (id)
);

CREATE INDEX IDX_user_groups_congregation_id ON user_groups(congregation_id);

CREATE TABLE user_group_members(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_group_id bigint(20) NOT NULL,
    user_id bigint(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IDX_user_group_members ON user_group_members(user_group_id, user_id);


CREATE TABLE user_group_permissions(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    subject varchar(200) NOT NULL,
    permissions varchar(255) DEFAULT '',
    user_group_id bigint(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IDX_user_group_permissions ON user_group_permissions (user_group_id);

-- !Downs
DROP TABLE user_groups;
DROP TABLE user_group_permissions;
DROP TABLE user_group_members;