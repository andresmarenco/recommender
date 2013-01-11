ALTER TABLE `recommender`.`folktaletype` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Code` ;
ALTER TABLE `recommender`.`language` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Name` ;
ALTER TABLE `recommender`.`region` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Name` ;
ALTER TABLE `recommender`.`scriptsource` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Name` ;
ALTER TABLE `recommender`.`storyteller` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Name` ;
ALTER TABLE `recommender`.`subgenre` ADD COLUMN `IFW` DOUBLE NULL  AFTER `Name` ;

DROP PROCEDURE `updateIFW`;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `updateIFW`()
BEGIN
	DECLARE stories_domain, dimension INT;

	SELECT count(1) FROM story INTO stories_domain;

	-- Keyword Table
	SELECT count(1) FROM keyword INTO dimension;

	UPDATE
		keyword,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from storykeywords as sk where sk.keywordId = k.id), 1))*log(10, dimension)) as result from keyword as k) as T
	SET
		keyword.ifw = T.result
	WHERE
		T.id = keyword.id;
	

	-- Subgenre Table
	SELECT count(1) FROM subgenre INTO dimension;

	UPDATE
		subgenre,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from story as sk where sk.subgenreId = k.id), 1))*log(10, dimension)) as result from subgenre as k) as T
	SET
		subgenre.ifw = T.result
	WHERE
		T.id = subgenre.id;


	-- Language Table
	SELECT count(1) FROM language INTO dimension;

	UPDATE
		language,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from story as sk where sk.languageId = k.id), 1))*log(10, dimension)) as result from language as k) as T
	SET
		language.ifw = T.result
	WHERE
		T.id = language.id;


	-- FolkTaleType Table
	SELECT count(1) FROM folktaletype INTO dimension;

	UPDATE
		folktaletype,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from story as sk where sk.folkTaleTypeId = k.id), 1))*log(10, dimension)) as result from folktaletype as k) as T
	SET
		folktaletype.ifw = T.result
	WHERE
		T.id = folktaletype.id;


	-- StoryTeller Table
	SELECT count(1) FROM storyteller INTO dimension;

	UPDATE
		storyteller,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from story as sk where sk.storyTellerId = k.id), 1))*log(10, dimension)) as result from storyteller as k) as T
	SET
		storyteller.ifw = T.result
	WHERE
		T.id = storyteller.id;


	-- ScriptSource Table
	SELECT count(1) FROM scriptsource INTO dimension;

	UPDATE
		scriptsource,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from story as sk where sk.scriptSourceId = k.id), 1))*log(10, dimension)) as result from scriptsource as k) as T
	SET
		scriptsource.ifw = T.result
	WHERE
		T.id = scriptsource.id;


	-- Region Table
	SELECT count(1) FROM region INTO dimension;

	UPDATE
		region,
		(SELECT k.id, (log(10, stories_domain/greatest((select count(1) from story as sk where sk.regionId = k.id), 1))*log(10, dimension)) as result from region as k) as T
	SET
		region.ifw = T.result
	WHERE
		T.id = region.id;
END
