
SELECT COUNT(AccountID) FROM RFOperations.RFO_Accounts.ConsultantPWSInfo A,
Hybris.dbo.Users B,
Hybris.dbo.Countries C
Where a.Accountid=B.P_rfaccountid
AND B.P_country=C.PK
AND C.isocode='US'
		


 --To find the StartDate and End date of PWW Migrations Only.

--SELECT * FROM RFOperations.[dbo].[ErrorFile]
--WHERE FILENAME ='Customer_PWS_InitialMigration_3_Upload+1'



 SELECT COUNT(*) from HYBRIS.DBO.USERS A,
	Hybris.dbo.Countries B
	 WHERE A.p_country=B.PK and B.isocode='US'
	  AND A.P_profilePicture IS NOT NULL
	  and (A.modifiedTS BETWEEN '2015-06-26 10:10:15.630' AND '2015-06-26 10:10:28.890')
	 
	 



 --                                ProfilePictureURL

 --		Truncate table #PWSQALOG
 --		SELECT * FROM #PWSQALOG

CREATE TABLE #PWSQALOG
(
SRC_ID BIGINT,
DEST_ID BIGINT,
ErrorReason nvarchar(255)
);


 
		-- Generate Fav Product lookup table from RFL.
		SELECT S.SITEID,SKU
		INTO  ##FAVPROD
		FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
		JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
		JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
		JOIN RodanFieldsLive.dbo.Products p ON p.ProductID = ssv.Value
		WHERE   ss.SiteSettingID IN ( 9, 10 ) ORDER BY 1

		--Create AboutMe Lookup table. Since this table converts HTML to Varchar it is heavy in processing. So creating it upfront.
		SELECT
		A.ACCOUNTID, A.FIRSTNAME,A.LASTNAME,S.SITENAME,
		HTM.*,
		[dbo].[udfStripHTML](hc.Html) as AboutMe
		INTO AboutMeLookup
		FROM
		rodanfieldslive.dbo.HTMLSectionContent HTM,
		rodanfieldslive.dbo.HTMLContent HC,
		rodanfieldslive.dbo.SITES S,
		rodanfieldslive.dbo.ACCOUNTS A
		WHERE HTM.HTMLContentID=HC.HTMLContentID AND
		HTM.SITEID=S.SITEID AND 
		HC.HTMLCONTENTSTATUSID=5 AND
		S.DISTRIBUTORID=A.ACCOUNTID
		AND A.ACCOUNTID<>2 AND
		HC.HTML NOT LIKE '%PersonalPhoto%';
--			=====================================================
		--		Missing IN Hybris.
		
		 DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'

		INSERT INTO  #PWSQALog (SRC_ID,DEST_ID,ErrorReason)
	SELECT 
	A.AccountID,
	null,
	'Missing in Hybris' 
	FROM RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO A,
	RFOperations.RFO_Accounts.AccountRF B
Where A.AccountID=B.AccountId
AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 

	EXCEPT

	SELECT p_rfaccountid,
	null ,
	'Missing in Hybris' from HYBRIS.DBO.USERS WHERE modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS; 

			--SELECT * FROM #PWSQALOG
			--TRUNCATE TABLE #PWSQALOG

--			==============================================================
		
		
		DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
	DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'


	INSERT INTO PWSQALOG(SRC_ID,DEST_ID,ErrorReason)
	SELECT P_rfaccountid,U.pk,'Quote Does not Match'
	FROM
	HYBRIS.DBO.USERS U , 
	RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO PWS
	WHERE U.p_rfaccountid=PWS.ACCOUNTID AND 
		  U.p_customeremail IS NOT NULL AND
		 ( U.modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS) AND 
		  ISNULL(PWS.PWSQUOTE,'A') <> ISNULL(U.P_QUOTE,'A')


--	SELECT * FROM PWSQALOG WHERE ErrorReason ='Quote Does not Match'
			--SELECT * FROM #PWSQALOG
			--TRUNCATE TABLE #PWSQALOG

--				======================================================


 DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'

		MERGE #PWSQALOG AS T
		USING (
		SELECT P_rfaccountid,U.pk
		FROM
			HYBRIS.DBO.USERS U , RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO PWS , HYBRIS.DBO.MEDIAS M
		WHERE U.p_rfaccountid=PWS.ACCOUNTID AND 
		  U.p_customeremail IS NOT NULL AND 
		  --U.modifiedTS>=  @MigrationStartTS  AND
		  M.PK=U.P_PROFILEPICTURE AND
		  ISNULL(PWS.ProfilepictureURL,'A') <> ISNULL(M.realfilename,'A')) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'Profile Picture Image Does not match')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'Profile Picture Image Does not match';
	 
		
			select a.*,b.PK,b.realfilename,b.HybrisCount from 
			(Select Accountid,profilepictureURL,len(profilepictureURL) as TotalCount
			 from RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO
			where accountid in (select src_id from #PWSQALOG) ) a ,

			( SELECT U.PK, U.p_rfaccountid,M.realfilename,len(M.realfilename)as HybrisCount
			 from HYBRIS.DBO.USERS U , HYBRIS.DBO.MEDIAS M, #pwsqalog T
			 Where M.pk=U.p_profilepicture
			 AND T.SRC_ID=U.P_rfaccountID) b
			 where a.accountid=b.P_rfaccountid
 

 --SELECT ProfilepictureURL from RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO 
 --WHERE AccountId=733424
 --SELECT A.*,M.REALFILENAME FROM Hybris.dbo.Users A,
 --HYBRIS.DBO.MEDIAS M
 --Where  M.pk=A.p_profilepicture
 --AND  A.PK=8799667224580


 -----------------
 
 ------------------------------------------------------------------------------------------------------------------------------------------------------
-----				Business Moment Values

--------		 TRUNCATE TABLE #PWSQALOG
--		SELECT * FROM #PWSQALOG


		DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'
		
		MERGE #PWSQALOG AS T
		USING (SELECT P_rfaccountid,U.pk
		FROM
		HYBRIS.DBO.USERS U , (
		
		--declare @migrationstartts datetime ='2015-06-26 10:10:15.630'
		--declare @migrationendts datetime ='2015-06-26 10:10:28.890'
		SELECT S.DISTRIBUTORID, BMO.businessmomentvalue
				  		  FROM
						  rodanfieldslive.dbo.SITES S,
						  rodanfieldslive.dbo.SITESETTINGS SS,
						  rodanfieldslive.dbo.SITESETTINGVALUES SSV,
		                  rodanfieldslive.ACCOUNTS.BUSINESSMOMENTOPTIONS BMO,
						  RFOperations.rfo_Accounts.AccountRF B
		                  WHERE
							SSV.SiteSettingID=BMO.BUSINESSMOMENTID AND
							S.SITEID=SSV.SITEID AND
							SSV.SITESETTINGID=SS.SITESETTINGID AND
							S.DISTRIBUTORid=B.AccountID
							AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 
							AND SS.SITESETTINGID=11  ) PWS
							

		WHERE U.p_rfaccountid=PWS.DISTRIBUTORID  AND
			  U.p_customeremail IS NOT NULL AND
			  u.modifiedts>=  @migrationstartts 
			   and
			  ISNULL(PWS.businessmomentvalue,'A') <> ISNULL(U.p_bestmomentbiz,'A')
			  
			  ) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'BestMomentBiz Does not match')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'BestMomentBiz Does not match';

			
SELECT S.*,D.* FROM 
			
			(SELECT S.DISTRIBUTORID , BMO.businessmomentvalue
				  		  FROM
						  rodanfieldslive.dbo.SITES S,
						  rodanfieldslive.dbo.SITESETTINGS SS,
						  rodanfieldslive.dbo.SITESETTINGVALUES SSV,
		                  rodanfieldslive.ACCOUNTS.BUSINESSMOMENTOPTIONS BMO,
						  RFOperations.rfo_Accounts.AccountRF B
		                  WHERE
							SSV.SiteSettingID=BMO.BUSINESSMOMENTID AND
							S.SITEID=SSV.SITEID AND
							SSV.SITESETTINGID=SS.SITESETTINGID AND
							S.DISTRIBUTORid=B.AccountID
							AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 
							AND SS.SITESETTINGID=11 AND S.DISTRIBUTORID IN (SELECT SRC_ID FROM #PWSQALOG)
							) S,

					(SELECT PK,P_rfaccountID,P_bestmomentbiz from hybris.dbo.Users
					where P_rfaccountID in (SELECT SRC_ID FROM #PWSQALOG)
					)D
					WHERE S.DISTRIBUTORID=D.P_rfaccountID
					ORDER BY DISTRIBUTORID


			---	2 Records not been matchings.
			

				TRUNCATE TABLE #PWSQALOG
				SELECT * FROM #PWSQALOG


				



           DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
           DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'
           
           MERGE #PWSQALOG AS T
           USING (SELECT P_rfaccountid,U.pk
           FROM
           HYBRIS.DBO.USERS U , (
           
           SELECT S.DISTRIBUTORID, BMO.businessmomentvalue
                                  FROM
                                  rodanfieldslive.dbo.SITES S,
                                  rodanfieldslive.dbo.SITESETTINGS SS,
                                  rodanfieldslive.dbo.SITESETTINGVALUES SSV,
                             rodanfieldslive.ACCOUNTS.BUSINESSMOMENTOPTIONS BMO,
                                  RFOperations.rfo_Accounts.AccountRF B
                             WHERE
                                     SSV.VALUE=BMO.BUSINESSMOMENTID AND
                                     S.SITEID=SSV.SITEID AND
                                     SSV.SITESETTINGID=SS.SITESETTINGID AND
                                     S.DISTRIBUTORid=B.AccountID
                                     AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 
                                     AND SS.SITESETTINGID=11  ) PWS
                                     

           WHERE U.p_rfaccountid=PWS.DISTRIBUTORID  AND
                  U.p_customeremail IS NOT NULL AND
                  u.modifiedts>=  @migrationstartts 
                   and
                  ISNULL(PWS.businessmomentvalue,'A') <> ISNULL(U.p_bestmomentbiz,'A')
                  
                  ) AS S
           ON (S.p_rfaccountid=T.SRC_ID) 
           WHEN NOT MATCHED 
                THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'BestMomentBiz Does not match')
           WHEN MATCHED 
                THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'BestMomentBiz Does not match';

                
SELECT top 25 S.*,D.* FROM 
                
                (SELECT S.DISTRIBUTORID , BMO.businessmomentvalue
                                  FROM
                                  rodanfieldslive.dbo.SITES S,
                                  rodanfieldslive.dbo.SITESETTINGS SS,
                                  rodanfieldslive.dbo.SITESETTINGVALUES SSV,
                             rodanfieldslive.ACCOUNTS.BUSINESSMOMENTOPTIONS BMO,
                                  RFOperations.rfo_Accounts.AccountRF B
                             WHERE
                                     SSV.VALUE=BMO.BUSINESSMOMENTID AND
                                     S.SITEID=SSV.SITEID AND
                                     SSV.SITESETTINGID=SS.SITESETTINGID AND
                                     S.DISTRIBUTORid=B.AccountID
                                     AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 
                                     AND SS.SITESETTINGID=11 AND S.DISTRIBUTORID IN (SELECT SRC_ID FROM #PWSQALOG)
                                     ) S,

                           (SELECT PK,P_rfaccountID,P_bestmomentbiz from hybris.dbo.Users
                           where P_rfaccountID in (SELECT SRC_ID FROM #PWSQALOG)
                           )D
                           WHERE S.DISTRIBUTORID=D.P_rfaccountID
                           ORDER BY DISTRIBUTORID

						  






			--========================================================================================================

			DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
			DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'
			
	MERGE #PWSQALOG AS T
	USING (SELECT P_rfaccountid,U.pk
	FROM
	HYBRIS.DBO.USERS U , 
	(
	SELECT  a.SITEID, 
								  SUBSTRING(d.FAVPROD,1, LEN(d.FAVPROD) - 1) FavProd FROM ( SELECT DISTINCT SITEID FROM ##FAVPROD ) a
								  CROSS APPLY
								  (
									SELECT SKU + ', ' 
									FROM ##FAVPROD AS B	
									WHERE A.SITEID = B.SITEID 
									FOR XML PATH('') ) D (FAVPROD)) FP
	WHERE U.p_rfaccountid=FP.SITEID AND 
	U.p_customeremail IS NOT NULL AND
	
	 U.modifiedTS>=@MigrationStartTS AND
	ISNULL(FP.FavProd,'A') <> ISNULL(U.p_consultantfavproducts,'A')) AS S
	ON (S.p_rfaccountid=T.SRC_ID) 
	WHEN NOT MATCHED 
		THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'Favorite Products Do not match')
	WHEN MATCHED 
		THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'Favorite Products Do not match';
		

		SELECT S.*,D.* FROM (SELECT S.DistributorID,SKU
		--INTO  ##FAVPROD
		FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
		JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
		JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
		JOIN RodanFieldsLive.dbo.Products p ON p.ProductID = ssv.Value
		WHERE   ss.SiteSettingID IN ( 9, 10 )
		AND S.DISTRIBUTORID IN (SELECT SRC_ID FROM #PWSQALOG)
		 )S,
		(SELECT PK,P_rfaccountID,p_consultantfavproducts from hybris.dbo.Users
					where P_rfaccountID in (SELECT SRC_ID FROM #PWSQALOG) )D
					WHERE S.DISTRIBUTORID=D.P_rfaccountID
					ORDER BY DISTRIBUTORID

		--truncate table #pwsqalog
		--select * from #pwsqalog
--============================================================================================================
		--Verify AboutMeBiz and AboutMeCom Values



			DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
			DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'

		MERGE #PWSQALOG AS T
		USING (SELECT P_rfaccountid,U.pk
		FROM
		HYBRIS.DBO.USERS U , AboutMeLookup PWS
		WHERE U.p_rfaccountid=PWS.AccountID AND 
			  U.p_customeremail IS NOT NULL AND
			   U.modifiedTS>@MigrationStartTS AND
			  (ISNULL(PWS.AboutMe,'') <> ISNULL(U.p_aboutmecom,'') OR ISNULL(PWS.AboutMe,'') <> ISNULL(U.p_aboutmeBiz,''))) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'AboutMeCOM or AboutMeBIZ Values do not match-NEW')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'AboutMeCOM or AboutMeBIZ Values do not match';
			
			
			
			
			Select A.*,B.* from 
			(Select AccountID,Aboutme,len(Aboutme)as TotalRFL From aboutmelookup Where Accountid in ( select src_id from #pwsqalog))A,
			(select P_rfaccountid,P_aboutmecom,len(P_aboutmecom) as TotalHybrisCom,P_aboutmeBiz,len(P_aboutmebiz)as TotalHybrisBiz from Hybris.dbo.Users where PK in (select dest_id from #pwsqalog))B
			Where A.AccountID=B.P_rfaccountID
			
			--AND TotalHybrisCom<>TotalHybrisBiz
			--And TotalRFL>TotalHybrisCom
			--And TotalRFL<TotalHybrisCom


			TRUNCATE TABLE #PWSQALOG
			SELECT * FROM  #PWSQALOG
			---------------------------------------


			

			DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
			DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'

		MERGE #PWSQALOG AS T
		USING (SELECT P_rfaccountid,U.pk
		FROM
		HYBRIS.DBO.USERS U , AboutMeLookup PWS
		WHERE U.p_rfaccountid=PWS.AccountID AND 
			  U.p_customeremail IS NOT NULL AND
			   U.modifiedTS>@MigrationStartTS AND
			  (ISNULL(PWS.AboutMe,'A') <> ISNULL(U.p_aboutmecom,'A') OR ISNULL(PWS.AboutMe,'A') <> ISNULL(U.p_aboutmeBiz,'A'))) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'AboutMeCOM or AboutMeBIZ Values do not match-NEW')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'AboutMeCOM or AboutMeBIZ Values do not match';
			
			
			
			
			Select A.*,B.* from 
			(Select AccountID,Aboutme,LEN(LTRIM(RTRIM(ABOUTME)))AS TOTAL From aboutmelookup Where Accountid in ( select src_id from #pwsqalog))A,
			(select P_rfaccountid,P_aboutmecom,LEN(LTRIM(RTRIM(P_aboutmecom))) AS TOTALHyBRIS,P_aboutmeBiz from Hybris.dbo.Users where PK in (select dest_id from #pwsqalog))B
			Where A.AccountID=B.P_rfaccountID
			AND B.P_ABOUTMECOM IS NOT NULL


			TRUNCATE TABLE #PWSQALOG

			

--		========================================================




Select count(*)  FROM     RFOperations.[RFO_Accounts].[ConsultantPWSInfo] A,
	 RFOperations.rfo_Accounts.AccountRF B
Where A.Accountid=B.AccountId
AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01')

			--Total=31019  SoftTerminated before 2014-05-01=26635

 --To find the StartDate and End date of PWW Migrations Only.


--SELECT * FROM RFOperations.[dbo].[ErrorFile]
--WHERE FILENAME ='Customer_PWS_InitialMigration_3_Upload+1'

 SELECT COUNT(*) from HYBRIS.DBO.USERS A,
	Hybris.dbo.Countries B
	 WHERE A.p_country=B.PK and B.isocode='US'
	  and (A.modifiedTS BETWEEN '2015-06-26 10:10:15.630' AND '2015-06-26 10:10:28.890')
	 
	  ---Total counts=26627 which is 8 count less than Source




---=================================================================================


--		SELECT * FROM PWSQALOG
--		TRUNCATE TABLE PWSQALOG

--CREATE TABLE PWSQALOG
--(
--SRC_ID BIGINT,
--DEST_ID BIGINT,
--ErrorReason nvarchar(255)
--);

--To find the StartDate and End date of PWW Migrations Only.
--SELECT * FROM RFOperations.[dbo].[ErrorFile]
--WHERE FILENAME ='Customer_PWS_InitialMigration_3_Upload+1'



--		MigrationEndTS='2015-06-26 10:10:28.890'

--MigrationStartTS ='2015-06-26 10:10:15.630'

	 
	 

	 SELECT *FROM Hybris.dbo.Users
	 Where P_rfaccountID in (Select SRC_ID from PWSQALOG)-----ModifiedTS has been changed.
	
--========================================================

	--	 SELECT SRC_ID AS AccountID,ErrorReason from PWSQALOG   ---IF Excluded SoftTerminationDaete>=2014-05-01

--AccountID	ErrorReason
--733424	Missing in Hybris
--1816275	Missing in Hybris
--26	Missing in Hybris
--1496	Missing in Hybris
--2	Missing in Hybris
--252	Missing in Hybris
--747718	Missing in Hybris
--2020392	Missing in Hybris



USE [DataMigration]
GO

DECLARE	@return_value int

EXEC	@return_value = [Migration].[Migration_VerifyPWSMigration]
		@MigrationStartTS = N'2015-06-26 10:10:15.630',
		@MigrationEndTS   = N'2015-06-26 10:10:28.890'

SELECT	'Return Value' = @return_value

GO

--				SELECT * FROM PWSQALOG WHERE ERRORREASON='MISSING IN HYBRIS'


---==================================================================================================
	 

-----CREATE PROCEDURE [Migration].[Migration_Hybris_Customer_PWS_InitialMigration_1_Generation]
--    @CountryCode NVARCHAR(2) = 'US'
--AS

--	IF OBJECT_ID('datamigration..CustomerPWS') IS NOT NULL
--        DROP TABLE datamigration..CustomerPWS

	

--	;WITH    CTE
--			  AS ( SELECT   U.PK,
--							CPI.AccountId ,
--							CPI.PWSName ,
--							CPI.PWSQuote ,
--							CPI.ProfilePictureURL,
--			--,su.SiteID
--		--, pic.Picture AS PWS_Pic
--		--, pic.ContentStatus AS PWS_Status
							
--							bestmoment.BusinessMomentValue ,

--							MAX(CASE WHEN favSKUs.Title = 'FavoriteProduct1'
--									 THEN favSKUs.ProductID
--								END) AS consultantFavProducts1 ,
--							MAX(CASE WHEN favSKUs.Title = 'FavoriteProduct2'
--									 THEN favSKUs.ProductID
--								END) AS consultantFavProducts2
--		--INTO  #T2
	
--				   FROM     RFOperations.[RFO_Accounts].[ConsultantPWSInfo] CPI
--							JOIN Hybris..users U ON U.p_rfaccountid = CPI.AccountId
--							JOIN RFO_Accounts.AccountBase AB ON AB.AccountID =  CPI.AccountId  AND AB.CountryID = 236
--							--JOIN RFO_Reference.Countries C ON C.CountryID = AB.CountryID AND C.Alpha2Code = 'US'
--							JOIN RodanFieldsLive.dbo.Sites s ON s.DistributorID = CPI.AccountId --AND CPI.AccountId = 514602
--							JOIN RodanFieldsLive.dbo.SiteURLs su ON su.SiteID = s.SiteID  AND su.SiteURLID NOT IN (6739,15506)
--							LEFT JOIN ( SELECT  s.SiteID ,
--												ss.Title ,
--												bmo.BusinessMomentValue
--										FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
--												JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
--												JOIN [RodanFieldsLive].[accounts].[BusinessMomentOptions] bmo ON bmo.BusinessMomentId = ss.SiteSettingID
--												JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
--										WHERE   ss.SiteSettingID = 11
--									  ) bestmoment ON bestmoment.SiteID = s.SiteID
--							LEFT JOIN ( SELECT  s.SiteID ,
--												ss.Title ,
--												p.SKU ,
--												p.ProductID
--										FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
--												JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
--												JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
--												JOIN RodanFieldsLive.dbo.Products p ON p.ProductID = ssv.Value
--										WHERE   ss.SiteSettingID IN ( 9, 10 )
--									  ) favSKUs ON favSKUs.SiteID = s.SiteID
--				   GROUP BY CPI.AccountId ,
--							bestmoment.BusinessMomentValue ,
--							CPI.PWSName ,
--							CPI.PWSQuote ,
--							CPI.ProfilePictureURL,
--							U.PK--,SU.SiteURLID
--	--,su.SiteID
--	--ORDER BY  CPI.AccountId
--				 )
--		SELECT  C.PK AS 'PK[unique=true]',
--				--C.AccountId AS 'rfAccountID' ,
--				C.PWSName AS 'PWSCustomerName' ,
--				C.PWSQuote AS 'quote' ,
--				C.ProfilePictureURL AS 'profilepicture' ,
--				Story AS 'AboutMeBiz' ,
--				Story AS 'AboutMecom' ,
--				BusinessMomentValue AS 'bestmomentbiz',
--				--consultantFavProducts1,
--				--consultantFavProducts2
--				CASE WHEN consultantFavProducts1 IS NOT NULL
--						  AND consultantFavProducts2 IS NOT NULL
--					 THEN CONCAT(P1.PK, ',',
--								 P2.PK)
--					 ELSE CONCAT(P1.PK, P2.PK)
--				END AS 'consultantFavProducts' 
--				INTO datamigration..CustomerPWS
--		FROM    CTE C
--				LEFT JOIN ( SELECT  s.DistributorID
--			--, hs.SectionName
--									,
--									hc.Name AS Title
--			--, hscs.SortIndex
--									,
--									[dbo].[udfStripHTML](hc.Html) AS Story ,
--									cs.NAME AS ContentStatus
--							FROM    [RodanFieldsLive].dbo.Sites s
--									JOIN [RodanFieldsLive].dbo.HTMLSectionContent hsc ON s.SiteID = hsc.SiteID
--									JOIN [RodanFieldsLive].dbo.HTMLContent hc ON hc.HTMLContentID = hsc.HTMLContentID
--									JOIN [RodanFieldsLive].dbo.HTMLSections hs ON hs.HTMLSectionID = hsc.HTMLSectionID
--									LEFT JOIN [RodanFieldsLive].dbo.HTMLSectionChoices hscs ON hscs.HTMLSectionID = hsc.HTMLSectionID
--									LEFT JOIN [RodanFieldsLive].dbo.HtmlContentStatus cs ON cs.HtmlContentStatusID = hc.HtmlContentStatusID
--							WHERE   hs.SectionName = 'Bio'
--									AND hc.HtmlContentStatusID = 5 --Production
--						  ) story ON story.DistributorID = C.AccountId
--				LEFT JOIN Hybris..Products P1 ON P1.p_rflegacyproductid = consultantFavProducts1 AND p1.p_catalogversion = 8796093153881
--				--LEFT JOIN hybris..catalogversions cv ON cv.PK = p1.p_catalogversion AND cv.p_version = 'online'
--				LEFT JOIN Hybris..Products P2 ON P2.p_rflegacyproductid = consultantFavProducts2 AND p2.p_catalogversion = 8796093153881
--				--LEFT JOIN hybris..catalogversions cv2 ON cv.PK = p2.p_catalogversion AND cv2.p_version = 'online'
--		WHERE   C.AccountId <> 2  

--    RETURN 0
	

	----========================================================================================

---Truncate table PWSQALOG

	
	--ALTER PROCEDURE [Migration].[Migration_VerifyPWSMigration] @MigrationStartTS Datetime,@MigrationEndTS Datetime
AS


SET NOCOUNT ON 

BEGIN
IF OBJECT_ID('dbo.PWSQALOG', 'U') IS NOT NULL
  DROP TABLE dbo.PWSQALOG;

IF OBJECT_ID('dbo.AboutMeLookup', 'U') IS NOT NULL
  DROP TABLE dbo.AboutMeLookup;


IF OBJECT_ID('tempdb.dbo.##FAVPROD') IS NOT NULL
  DROP TABLE ##FAVPROD;

  IF @MigrationStartTS IS NULL
	 RAISERROR (15600,-1,-1, 'Please Provide a DateTime value. Procedure will validate records updated after this input value.');
  
CREATE TABLE PWSQALOG
(
SRC_ID BIGINT,
DEST_ID BIGINT,
ErrorReason nvarchar(255)
);

 
		-- Generate Fav Product lookup table from RFL.
		SELECT S.SITEID,SKU
		INTO  ##FAVPROD
		FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
		JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
		JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
		JOIN RodanFieldsLive.dbo.Products p ON p.ProductID = ssv.Value
		WHERE   ss.SiteSettingID IN ( 9, 10 ) ORDER BY 1

		--Create AboutMe Lookup table. Since this table converts HTML to Varchar it is heavy in processing. So creating it upfront.
		SELECT
		A.ACCOUNTID, A.FIRSTNAME,A.LASTNAME,S.SITENAME,
		HTM.*,
		[dbo].[udfStripHTML](hc.Html) as AboutMe
		INTO AboutMeLookup
		FROM
		rodanfieldslive.dbo.HTMLSectionContent HTM,
		rodanfieldslive.dbo.HTMLContent HC,
		rodanfieldslive.dbo.SITES S,
		rodanfieldslive.dbo.ACCOUNTS A
		WHERE HTM.HTMLContentID=HC.HTMLContentID AND
		HTM.SITEID=S.SITEID AND 
		HC.HTMLCONTENTSTATUSID=5 AND
		S.DISTRIBUTORID=A.ACCOUNTID
		AND A.ACCOUNTID<>2 AND
		HC.HTML NOT LIKE '%PersonalPhoto%';


--Find Missing PWS Information in Hybris Table. Since PWS is an attribute of a consultant, this result will indicate missing consultants in Hybris.
	
	
	
	DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
	DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'
	
	INSERT INTO  PWSQALog (SRC_ID,DEST_ID,ErrorReason)
	SELECT 
	A.AccountID,
	null,
	'Missing in Hybris' 
	FROM RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO A,
	RFOperations.RFO_Accounts.AccountRF B
Where A.AccountID=B.AccountId
AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 

	EXCEPT

	SELECT p_rfaccountid,
	null ,
	'Missing in Hybris' from HYBRIS.DBO.USERS WHERE modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS; 


	--		SELECT * FROM PWSQALOG


	
	
	----------------------------------------------------------------------
	
	
	DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
	DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'


	INSERT INTO PWSQALOG(SRC_ID,DEST_ID,ErrorReason)
	SELECT P_rfaccountid,U.pk,'Quote Does not Match'
	FROM
	HYBRIS.DBO.USERS U , 
	RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO PWS
	WHERE U.p_rfaccountid=PWS.ACCOUNTID AND 
		  U.p_customeremail IS NOT NULL AND
		 ( U.modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS) AND 
		  ISNULL(PWS.PWSQUOTE,'A') <> ISNULL(U.P_QUOTE,'A')


--	SELECT * FROM PWSQALOG WHERE ErrorReason ='Quote Does not Match'


		--Verify Profile Picture Image
		
		DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'

		
		MERGE PWSQALOG AS T
		USING (SELECT P_rfaccountid,U.pk
		FROM
			HYBRIS.DBO.USERS U , RFOPERATIONS.RFO_ACCOUNTS.CONSULTANTPWSINFO PWS , HYBRIS.DBO.MEDIAS M
		WHERE U.p_rfaccountid=PWS.ACCOUNTID AND 
		  U.p_customeremail IS NOT NULL AND 
		  ( U.modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS) AND
		  M.PK=U.P_PROFILEPICTURE AND
		  ISNULL(PWS.ProfilepictureURL,'A') <> ISNULL(M.realfilename,'A')) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'Profile Picture Image Does not match')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'Profile Picture Image Does not match';
			

		-----------------------------------------------------------------------------
		--Verify Best Business Moment Value

		DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'


		MERGE PWSQALOG AS T
		USING (SELECT P_rfaccountid,U.pk
		FROM
		HYBRIS.DBO.USERS U , (SELECT S.DISTRIBUTORID, BMO.businessmomentvalue
				  		  FROM
						  rodanfieldslive.dbo.SITES S,
						  rodanfieldslive.dbo.SITESETTINGS SS,
						  rodanfieldslive.dbo.SITESETTINGVALUES SSV,
		                  rodanfieldslive.ACCOUNTS.BUSINESSMOMENTOPTIONS BMO
		                  WHERE
							SSV.VALUE=BMO.BUSINESSMOMENTID AND
							S.SITEID=SSV.SITEID AND
							SSV.SITESETTINGID=SS.SITESETTINGID AND
							SS.SITESETTINGID=11) PWS
		WHERE U.p_rfaccountid=PWS.DISTRIBUTORID AND 
			  U.p_customeremail IS NOT NULL AND
			  ( U.modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS)  AND
			  ISNULL(PWS.businessmomentvalue,'A') <> ISNULL(U.p_bestmomentbiz,'A')) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'BestMomentBiz Does not match')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'BestMomentBiz Does not match';


-------------------------------------------------------------------------------------

	--	SELECT COUNT(*) FROM PWSQALOG WHERE ErrorReason<>'Missing in Hybris'	2854
		
			
  --  --Verify Favorite products


		DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'

	MERGE PWSQALOG AS T
	USING (SELECT P_rfaccountid,U.pk
	FROM
	HYBRIS.DBO.USERS U , (SELECT  a.SITEID, 
								  SUBSTRING(d.FAVPROD,1, LEN(d.FAVPROD) - 1) FavProd FROM ( SELECT DISTINCT SITEID FROM ##FAVPROD ) a
								  CROSS APPLY
								  (
									SELECT SKU + ', ' 
									FROM ##FAVPROD AS B	
									WHERE A.SITEID = B.SITEID 
									FOR XML PATH('') ) D (FAVPROD)) FP
	WHERE U.p_rfaccountid=FP.SITEID AND 
	U.p_customeremail IS NOT NULL AND
	( U.modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS)  AND
	ISNULL(FP.FavProd,'A') <> ISNULL(U.p_consultantfavproducts,'A')) AS S
	ON (S.p_rfaccountid=T.SRC_ID) 
	WHEN NOT MATCHED 
		THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'Favorite Products Do not match')
	WHEN MATCHED 
		THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'Favorite Products Do not match';



		
		--Verify AboutMeBiz and AboutMeCom Values

		
CREATE TABLE #PWSQALOG
(
SRC_ID BIGINT,
DEST_ID BIGINT,
ErrorReason nvarchar(255)


		DECLARE @MigrationStartTS DATETIME ='2015-06-26 10:10:15.630'
		DECLARE @MigrationEndTS DATETIME ='2015-06-26 10:10:28.890'
		MERGE PWSQALOG AS T
		USING (SELECT P_rfaccountid,U.pk
		FROM
		HYBRIS.DBO.USERS U , AboutMeLookup PWS
		WHERE U.p_rfaccountid=PWS.AccountID AND 
			  U.p_customeremail IS NOT NULL AND
			  ( U.modifiedTS BETWEEN @MigrationStartTS AND @MigrationEndTS)  AND
			  (ISNULL(PWS.AboutMe,'A') <> ISNULL(U.p_aboutmecom,'A') OR ISNULL(PWS.AboutMe,'A') <> ISNULL(U.p_aboutmeBiz,'A'))) AS S
		ON (S.p_rfaccountid=T.SRC_ID) 
		WHEN NOT MATCHED 
			THEN INSERT(SRC_ID,DEST_ID,ErrorReason) VALUES(S.p_rfaccountid,S.PK,'AboutMeCOM or AboutMeBIZ Values do not match')
		WHEN MATCHED 
			THEN UPDATE SET T.ErrorReason = T.ErrorReason+','+'AboutMeCOM or AboutMeBIZ Values do not match';


--						SELECT * FROM #PWSQALOG
--						TRUNCATE TABLE #PWSQALOG
			
END




GO


----------------=======================================

USE [DataMigration]
GO

/****** Object:  StoredProcedure [Migration].[Migration_Hybris_Customer_PWS_InitialMigration_1_Generation]    Script Date: 6/30/2015 9:55:32 AM ******/
SET ANSI_NULLS OFF
GO

SET QUOTED_IDENTIFIER OFF
GO







CREATE PROCEDURE [Migration].[Migration_Hybris_Customer_PWS_InitialMigration_1_Generation]
    @CountryCode NVARCHAR(2) = 'US'
AS

	IF OBJECT_ID('datamigration..CustomerPWS') IS NOT NULL
        DROP TABLE datamigration..CustomerPWS

	

	;WITH    CTE
			  AS ( SELECT   U.PK,
							CPI.AccountId ,
							CPI.PWSName ,
							CPI.PWSQuote ,
							CPI.ProfilePictureURL,
			--,su.SiteID
		--, pic.Picture AS PWS_Pic
		--, pic.ContentStatus AS PWS_Status
							
							bestmoment.BusinessMomentValue ,

							MAX(CASE WHEN favSKUs.Title = 'FavoriteProduct1'
									 THEN favSKUs.ProductID
								END) AS consultantFavProducts1 ,
							MAX(CASE WHEN favSKUs.Title = 'FavoriteProduct2'
									 THEN favSKUs.ProductID
								END) AS consultantFavProducts2
		--INTO  #T2
	
				   FROM     RFOperations.[RFO_Accounts].[ConsultantPWSInfo] CPI
							JOIN Hybris..users U ON U.p_rfaccountid = CPI.AccountId
							JOIN RFO_Accounts.AccountBase AB ON AB.AccountID =  CPI.AccountId  AND AB.CountryID = 236
							--JOIN RFO_Reference.Countries C ON C.CountryID = AB.CountryID AND C.Alpha2Code = 'US'
							JOIN RodanFieldsLive.dbo.Sites s ON s.DistributorID = CPI.AccountId --AND CPI.AccountId = 514602
							JOIN RodanFieldsLive.dbo.SiteURLs su ON su.SiteID = s.SiteID  AND su.SiteURLID NOT IN (6739,15506)
							LEFT JOIN ( SELECT  s.SiteID ,
												ss.Title ,
												bmo.BusinessMomentValue
										FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
												JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
												JOIN [RodanFieldsLive].[accounts].[BusinessMomentOptions] bmo ON bmo.BusinessMomentId = ss.SiteSettingID
												JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
										WHERE   ss.SiteSettingID = 11
									  ) bestmoment ON bestmoment.SiteID = s.SiteID
							LEFT JOIN ( SELECT  s.SiteID ,
												ss.Title ,
												p.SKU ,
												p.ProductID
										FROM    [RodanFieldsLive].[dbo].[SiteSettingValues] ssv
												JOIN [RodanFieldsLive].[dbo].[SiteSettings] ss ON ssv.SiteSettingID = ss.SiteSettingID
												JOIN RodanFieldsLive.dbo.Sites s ON s.SiteID = ssv.SiteID
												JOIN RodanFieldsLive.dbo.Products p ON p.ProductID = ssv.Value
										WHERE   ss.SiteSettingID IN ( 9, 10 )
									  ) favSKUs ON favSKUs.SiteID = s.SiteID
				   GROUP BY CPI.AccountId ,
							bestmoment.BusinessMomentValue ,
							CPI.PWSName ,
							CPI.PWSQuote ,
							CPI.ProfilePictureURL,
							U.PK--,SU.SiteURLID
	--,su.SiteID
	--ORDER BY  CPI.AccountId
				 )
		SELECT  C.PK AS 'PK[unique=true]',
				--C.AccountId AS 'rfAccountID' ,
				--C.PWSName AS 'name' ,
				C.PWSQuote AS 'quote' ,
				C.ProfilePictureURL AS 'profilepicture' ,
				Story AS 'AboutMeBiz' ,
				Story AS 'AboutMecom' ,
				BusinessMomentValue AS 'bestmomentbiz',
				--consultantFavProducts1,
				--consultantFavProducts2
				CASE WHEN consultantFavProducts1 IS NOT NULL
						  AND consultantFavProducts2 IS NOT NULL
					 THEN CONCAT(P1.PK, ',',
								 P2.PK)
					 ELSE CONCAT(P1.PK, P2.PK)
				END AS 'consultantFavProducts' 
				INTO datamigration..CustomerPWS
		FROM    CTE C
				LEFT JOIN ( SELECT  s.DistributorID
			--, hs.SectionName
									,
									hc.Name AS Title
			--, hscs.SortIndex
									,
									[dbo].[udfStripHTML](hc.Html) AS Story ,
									cs.Name AS ContentStatus
							FROM    [RodanFieldsLive].dbo.Sites s
									JOIN [RodanFieldsLive].dbo.HTMLSectionContent hsc ON s.SiteID = hsc.SiteID
									JOIN [RodanFieldsLive].dbo.HTMLContent hc ON hc.HTMLContentID = hsc.HTMLContentID
									JOIN [RodanFieldsLive].dbo.HTMLSections hs ON hs.HTMLSectionID = hsc.HTMLSectionID
									LEFT JOIN [RodanFieldsLive].dbo.HTMLSectionChoices hscs ON hscs.HTMLSectionID = hsc.HTMLSectionID
									LEFT JOIN [RodanFieldsLive].dbo.HtmlContentStatus cs ON cs.HtmlContentStatusID = hc.HtmlContentStatusID
							WHERE   hs.SectionName = 'Bio'
									AND hc.HtmlContentStatusID = 5 --Production
						  ) story ON story.DistributorID = C.AccountId
				LEFT JOIN Hybris..Products P1 ON P1.p_rflegacyproductid = consultantFavProducts1 AND p1.p_catalogversion = 8796093153881
				--LEFT JOIN hybris..catalogversions cv ON cv.PK = p1.p_catalogversion AND cv.p_version = 'online'
				LEFT JOIN Hybris..Products P2 ON P2.p_rflegacyproductid = consultantFavProducts2 AND p2.p_catalogversion = 8796093153881
				--LEFT JOIN hybris..catalogversions cv2 ON cv.PK = p2.p_catalogversion AND cv2.p_version = 'online'
		WHERE   C.AccountId <> 2  

    RETURN 0
	
	Create table #rfo_table(Accountid int,pk int)
	create table #hybris_table(rf_account int ,Pk int)
	Create table #result(Accountid int,rfoValue int,pk int, hybrisvalue int)

	Insert into #rfo_table select Accountid from RFOperations.[RFO_Accounts].[ConsultantPWSInfo]

GO



 SELECT pk
 ,P_rfaccountID
 ,modifiedTS
 ,name,p_quote
 ,P_profilepicture
 ,P_customeremail
 ,P_country
 ,P_mainphone
 ,P_consultantsince
 from Hybris.dbo.Users
 Where p_rfaccountid=2

 SELECT * FROM Hybris.[dbo].[photogallery]
 Where PK=8796230090782

 SELECT AccountID,PWSName,PWSQuote,ProfilePictureURL 
 FROM RFOperations.[RFO_Accounts].[ConsultantPWSInfo]
 WHERE AccountID=2

 --------------------------------------------------------------------------
