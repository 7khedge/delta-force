CREATE TABLE `jobEventQueue` (
  `id`             INT(11) NOT NULL,
  `message`        VARCHAR(1024)
                   COLLATE utf8_bin DEFAULT NULL,
  `status`         VARCHAR(45)
                   COLLATE utf8_bin DEFAULT NULL,
  `createDateTime` DATETIME         DEFAULT NULL,
  `updateDateTime` DATETIME         DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin