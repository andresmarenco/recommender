ALTER TABLE `recommender`.`ir_user` ENGINE = InnoDB ;


CREATE  TABLE `recommender`.`ir_event_type` (
  `Id` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`Id`) ) ENGINE=InnoDB;


  
INSERT INTO `recommender`.`ir_event_type` (`Name`) VALUES ('login');
INSERT INTO `recommender`.`ir_event_type` (`Name`) VALUES ('logout');
INSERT INTO `recommender`.`ir_event_type` (`Name`) VALUES ('signup');
INSERT INTO `recommender`.`ir_event_type` (`Name`) VALUES ('view_story');

  
CREATE  TABLE `recommender`.`ir_event_log` (
  `Id` INT NOT NULL AUTO_INCREMENT ,
  `TriggeredDate` TIMESTAMP NOT NULL DEFAULT now() ,
  `UserId` INT NULL ,
  `EventTypeId` INT NOT NULL ,
  PRIMARY KEY (`Id`) ,
  INDEX `ir_event_log_user_idx` (`UserId` ASC) ,
  INDEX `ir_event_log_event_type_idx` USING BTREE (`EventTypeId` ASC) ,
  CONSTRAINT `ir_event_log_user_fkey`
    FOREIGN KEY (`UserId` )
    REFERENCES `recommender`.`ir_user` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `ir_event_log_event_type_fkey`
    FOREIGN KEY (`EventTypeId` )
    REFERENCES `recommender`.`ir_event_type` (`Id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE) ENGINE=InnoDB;


    
    
CREATE  TABLE `recommender`.`ir_story_view_type` (
  `Id` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`Id`) ) ENGINE=InnoDB;

  
  
INSERT INTO `recommender`.`ir_story_view_type` (`Name`) VALUES ('recommendation');
INSERT INTO `recommender`.`ir_story_view_type` (`Name`) VALUES ('browser');
INSERT INTO `recommender`.`ir_story_view_type` (`Name`) VALUES ('search');




CREATE  TABLE `recommender`.`ir_story_view_log` (
  `Id` INT NOT NULL ,
  `StoryId` INT NOT NULL ,
  `ViewTypeId` INT ,
  PRIMARY KEY (`Id`) ,
  INDEX `ir_story_view_log_event_idx` USING HASH (`Id` ASC) ,
  INDEX `ir_story_view_log_story_idx` USING BTREE (`StoryId` ASC) ,
  CONSTRAINT `ir_story_view_log_event_fkey`
    FOREIGN KEY (`Id` )
    REFERENCES `recommender`.`ir_event_log` (`Id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ir_story_view_log_story_fkey`
    FOREIGN KEY (`StoryId` )
    REFERENCES `recommender`.`story` (`Id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ir_story_view_log_view_type_fkey`
    FOREIGN KEY (`ViewTypeId` )
    REFERENCES `recommender`.`ir_story_view_type` (`Id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE) ENGINE=InnoDB;



