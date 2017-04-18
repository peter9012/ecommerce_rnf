


DECLARE @Calalog NVARCHAR(225)= 'rodanandfieldsCANContentCatalog' 
--(rodanandfieldsContentCatalog,rodanandfieldsCANContentCatalog,rodanandfieldsAUSContentCatalog)




SELECT  REPLACE(p_uid, '-ConsultantImageComponent', '') AS MediaEmail ,
        REPLACE(REPLACE(m.p_code, 'profile_pic_user_id_', ''), '_media', '') AS Media_AccountID ,
        m.pk AS MediaPK
INTO    #Medias 
--			SELECT DISTINCT ct.p_id
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
        LEFT JOIN Hybris.dbo.medias m ON m.pk = c.p_media
WHERE    cv.p_version = 'Online' AND
       ct.p_id = @Calalog AND
         p_name = 'Consultant Image Component About Me'
		-- AND REPLACE(p_uid, '-ConsultantImageComponent', '')  ='loridcal@icloud.com'


				


				
				
SELECT   c.p_emailaddress AS ContactEmail ,
        ISNULL(c.p_city, '') p_city ,
        ISNULL(c.p_state, '') p_state ,
        ISNULL(c.p_phonenum, '') p_phonenum ,
        p_showsince ,
        c.p_facebook ,
        c.p_instagram ,
        c.p_twitter ,
        c.p_pinterset 
INTO    #Contact
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
WHERE   cv.p_version = 'Online'
        AND ct.p_id = @Calalog
        AND p_name = 'Contact Details Component'




SELECT  REPLACE(p_uid, '-FavProductsComponent', '') FavProdEmail ,
        ISNULL(c.p_favproducts, '') p_favproducts
INTO    #FavProd
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
WHERE   cv.p_version = 'Online'
        AND ct.p_id = @Calalog
        AND p_name = 'Fav Products Component About Me'


		
SELECT  REPLACE(p_uid, '-QuestionsComponent1', '') Comp1Email ,
        p_answer AS [Why I joined Rodan and Fields]
INTO    #Component1
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
WHERE   cv.p_version = 'Online'
        AND ct.p_id = @Calalog
        AND p_name = 'Why I joined Rodan and Fields'


		
SELECT  REPLACE(p_uid, '-QuestionsComponent2', '') Comp2Email ,
        p_answer AS [What I love most about RF products]
INTO    #Component2
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
WHERE   cv.p_version = 'Online'
        AND ct.p_id = @Calalog
        AND p_name = 'What I love most about RF products'


		
SELECT  REPLACE(p_uid, '-QuestionsComponent3', '') Comp3Email ,
        p_answer AS [What I love most about my RF business]
INTO    #Component3
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
WHERE   cv.p_version = 'Online'
        AND ct.p_id = @Calalog
        AND p_name = 'What I love most about my RF business'



		
		
SELECT  REPLACE(p_uid, '-QuestionsComponent4', '') Comp4Email ,
        ISNULL(p_answer, '') AS [What I love most about Rodan and Fields]
INTO    #Component4
FROM    [Hybris].[dbo].[cmscomponent] c
        LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
        LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk = cv.p_catalog
WHERE   cv.p_version = 'Online'
        AND ct.p_id = @Calalog
        AND p_name = 'What I love most about Rodan and Fields'



DECLARE @Calalog NVARCHAR(225)= 'rodanandfieldsContentCatalog' 

SELECT  Main.p_customerid ,
        Main.p_uid ,
        Main.p_name ,
        Main.PageEmail ,
        Main.p_userprofile ,
        Main.AccountStatus ,
        Main.AccountType ,
        Medias.Media_AccountID ,
        Medias.MediaPK ,
        Contact.p_city ,
        Contact.p_state ,
        Contact.p_phonenum ,
        Contact.p_showsince ,
        Contact.p_facebook ,
        Contact.p_instagram ,
        Contact.p_twitter ,
        Contact.p_pinterset ,
        favprod.p_favproducts ,
        Component1.[Why I joined Rodan and Fields] ,
        Component2.[What I love most about RF products] ,
        Component3.[What I love most about my RF business] ,
        Component4.[What I love most about Rodan and Fields]
 INTO    dm_qa.dbo.Hybris_CA_PWS
FROM    ( SELECT    u.p_customerid ,
                    u.p_uid ,
					co.p_isocode[CountryID],
                    u.p_name ,
                    c.p_uid AS [PageEmail] ,
                    u.p_userprofile ,
                    v.Code [AccountStatus] ,
                    t.Code [AccountType],ct.*
          FROM      Hybris.dbo.users u
		  LEFT JOIN Hybris.dbo.countries co ON co.pk =u.p_country
                    JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
                                                           AND v.Code = 'Active'  
                    JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                                                           AND t.Code = 'CONSULTANT'
                    LEFT JOIN Hybris.dbo.cmspage c ON u.p_uid = REPLACE(c.p_uid,
                                                              '-about-me', '')
                    LEFT JOIN Hybris.dbo.enumerationvalues av ON av.pk = c.p_approvalstatus
                    LEFT JOIN Hybris.dbo.catalogversions cv ON cv.pk = c.p_catalogversion
					LEFT JOIN Hybris.dbo.catalogs ct ON ct.pk=cv.p_catalog
          WHERE     cv.p_version = 'Online'
                    AND c.p_defaultpage = 1
					AND ct.p_id=@Calalog
					
        ) Main
        LEFT JOIN #Medias Medias ON Medias.MediaEmail = Main.p_uid
        LEFT JOIN #Contact contact ON contact.ContactEmail = Main.p_uid
        LEFT JOIN #FavProd FavProd ON FavProd.FavProdEmail = Main.p_uid
        LEFT JOIN #Component1 Component1 ON Component1.Comp1Email = Main.p_uid
        LEFT JOIN #Component2 Component2 ON Component2.Comp2Email = Main.p_uid
        LEFT JOIN #Component3 Component3 ON Component3.Comp3Email = Main.p_uid
        LEFT JOIN #Component4 Component4 ON Component4.Comp4Email = Main.p_uid
		WHERE Main.[CountryID]='CA'


CREATE CLUSTERED INDEX cls_ix ON  dm_qa.dbo.Hybris_CA_PWS(p_customerid) 
		

		--  DROP TABLE dm_qa.dbo.Hybris_CA_PWS