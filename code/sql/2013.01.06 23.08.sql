ALTER TABLE `recommender`.`keyword` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Name` ;



-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `updateIFW`()
BEGIN
	DECLARE stories_domain, dimension INT;

	SELECT count(1) FROM story INTO stories_domain;
	SELECT count(1) FROM keyword INTO dimension;

	UPDATE
		keyword,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from storykeywords as sk where sk.keywordId = k.id), 1))*log(10, dimension)) as result from keyword as k) as T
	SET
		keyword.ifw = T.result
	WHERE
		T.id = keyword.id;
	
END