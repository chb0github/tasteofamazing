drop SCHEMA IF EXISTS SDR;
create SCHEMA IF NOT EXISTS SDR;
use SDR;

CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(60) NOT NULL,
  enabled BOOLEAN NOT NULL
);
CREATE TABLE Roles (
  name VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE authorities (
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  role VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users (username),
  FOREIGN KEY (role) REFERENCES Roles (name),
  UNIQUE (username, role)
);

CREATE TABLE groups (
  id bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE group_authorities (
  group_id  BIGINT      NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE group_members (
  id       BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  group_id BIGINT      NOT NULL,
   FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE persistent_logins (
  username  VARCHAR(64) NOT NULL,
  series    VARCHAR(64) PRIMARY KEY,
  token     VARCHAR(64) NOT NULL,
  last_used TIMESTAMP   NOT NULL
);

CREATE TABLE acl_sid (
  id        BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  principal BOOLEAN      NOT NULL,
  sid       VARCHAR(100) NOT NULL,
  UNIQUE (sid, principal)
);

CREATE TABLE acl_class (
  id    BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  class VARCHAR(100) NOT NULL,
  UNIQUE (class)
);

CREATE TABLE acl_object_identity (
  id                 BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  object_id_class    BIGINT             NOT NULL,
  object_id_identity BIGINT             NOT NULL,
  parent_object      BIGINT,
  owner_sid          BIGINT             NOT NULL,
  entries_inheriting BOOLEAN            NOT NULL,
  UNIQUE (object_id_class, object_id_identity),
  FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
  FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
  FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
);

CREATE TABLE acl_entry (
  id                  BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,

  acl_object_identity BIGINT             NOT NULL,
  ace_order           INT                NOT NULL,
  sid                 BIGINT             NOT NULL,
  mask                INTEGER            NOT NULL,
  granting            BOOLEAN            NOT NULL,
  audit_success       BOOLEAN            NOT NULL,
  audit_failure       BOOLEAN            NOT NULL,
  CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity, ace_order),
  CONSTRAINT foreign_fk_4 FOREIGN KEY (acl_object_identity)
  REFERENCES acl_object_identity (id),
  CONSTRAINT foreign_fk_5 FOREIGN KEY (sid) REFERENCES acl_sid (id)
);
