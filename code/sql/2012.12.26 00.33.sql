CREATE  TABLE `recommender`.`ir_story_user_score` (
  `storyId` INT NOT NULL ,
  `userId` INT NOT NULL ,
  `score` FLOAT NOT NULL ,
  PRIMARY KEY (`storyId`, `userId`) ,
  INDEX `ir_story_user_score_story_idx` (`storyId` ASC) ,
  INDEX `ir_story_user_score_user_idx` (`userId` ASC) ,
  CONSTRAINT `ir_story_user_score_story_fkey`
    FOREIGN KEY (`storyId` )
    REFERENCES `recommender`.`story` (`Id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ir_story_user_score_user_fkey`
    FOREIGN KEY (`userId` )
    REFERENCES `recommender`.`ir_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;




