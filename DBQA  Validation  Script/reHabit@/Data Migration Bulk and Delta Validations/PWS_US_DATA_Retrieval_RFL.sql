USE RodanFieldsLive_04052017
GO 


WITH MyStory
     AS (SELECT hsc.SiteID
              , hc.Name
              , CONVERT( NVARCHAR(MAX) , hc.Html) Html
         FROM dbo.HTMLSections hs
              JOIN dbo.HTMLSectionContent hsc ON hs.HTMLSectionID = hsc.HTMLSectionID
              JOIN dbo.HTMLContent hc ON hsc.HTMLContentID = hc.HTMLContentID
         WHERE hc.HtmlContentStatusID = 5
               AND hs.HTMLSectionID = 6395) ,
     MyPhoto
     AS (SELECT hsc.SiteID
              , CASE
                    WHEN hc.Html LIKE '%endimage%'
                         AND hc.Html LIKE '%imagepath%' THEN SUBSTRING(hc.Html , CHARINDEX('<!--imagepath-->' , hc.Html)+16 , CHARINDEX('"><!--endimage-->' , hc.Html)-CHARINDEX('<!--imagepath-->' , hc.Html)-16)
                    ELSE 'no_image'
                END PhotoURL
         FROM dbo.HTMLSections hs
              JOIN dbo.HTMLSectionContent hsc ON hs.HTMLSectionID = hsc.HTMLSectionID
              JOIN dbo.HTMLContent hc ON hsc.HTMLContentID = hc.HTMLContentID
         WHERE hc.HtmlContentStatusID = 5
               AND hs.HTMLSectionID = 6394) ,
     MyBestMoment
     AS (SELECT s.SiteID
              , CASE CONVERT( INT , ssv.Value)
                    WHEN 1 THEN 'Teaming up with the Doctors who created Proactiv® Solution in their newest business venture'
                    WHEN 2 THEN 'Seeing the vast potential in this business'
                    WHEN 3 THEN 'Being recognized at the Inaugural X-Factor Convention'
                    WHEN 4 THEN 'My first title advance'
                    WHEN 5 THEN 'Reaching Level V Status'
                    WHEN 6 THEN 'Opening my first check'
                    WHEN 7 THEN 'Enjoying the effects of residual income'
                    WHEN 8 THEN 'Seeing how my Rodan + Fields income helped eliminate my debt'
                    WHEN 9 THEN 'Earning $3,000 Leadership Express Bonus'
                    WHEN 10 THEN 'Earning $7,000 Leadership Express Bonus'
                    WHEN 11 THEN 'Earning $10,000 Leadership Express Bonus'
                    WHEN 12 THEN 'The day I was able to replace my corporate income'
                    ELSE NULL
                END MyBestMoment
         FROM dbo.Sites s
              JOIN dbo.Sites bs ON s.BaseSiteID = bs.SiteID
              JOIN dbo.SiteSettings ss ON bs.SiteID = ss.BaseSiteID
              JOIN dbo.SiteSettingValues ssv ON ss.SiteSettingID = ssv.SiteSettingID
                                                AND s.SiteID = ssv.SiteID
         WHERE s.IsBase = 0
               AND ss.SiteSettingID = 11) ,
     PWSSubscription
     AS (SELECT a.AccountID
              , CASE
                    WHEN ( r.roles >= 1 )
                         OR ( r.roles = 0
                              AND at.TemplateOrderID IS NOT NULL
                              AND a.EnrollmentDate > DATEADD(DAY , -31 , GETDATE()) ) THEN 1
                    ELSE 0
                END IsActive
         FROM dbo.Accounts a
              OUTER APPLY ( SELECT COUNT(*) roles
                            FROM dbo.AccountRole ar
                            WHERE ar.RoleID = 8
                                  AND ar.AccountID = a.AccountID ) r
              OUTER APPLY ( SELECT TOP 1 ao.TemplateOrderID
                            FROM dbo.AutoshipOrders ao
                                 JOIN dbo.AutoshipSchedules sched ON sched.AutoshipScheduleID = ao.AutoshipScheduleID
                                 JOIN dbo.Orders o ON o.OrderID = ao.TemplateOrderID
                            WHERE ao.AutoshipScheduleID = 3
                                  AND ao.AccountID = a.AccountID
                                  AND o.OrderStatusID = 7 ORDER BY ao.TemplateOrderID DESC ) at
         WHERE a.Active = 1
               AND a.AccountTypeID = 1) ,
     Sites
     AS (SELECT MIN(s.SiteID) SiteID
              , su.URL
              , s.DistributorID
         FROM accounts a
              JOIN dbo.Sites s ON s.DistributorID = a.AccountID
              JOIN dbo.SiteURLs su ON su.SiteID = s.SiteID
              JOIN dbo.AutoshipOrders aso ON aso.AccountID = a.AccountID
              JOIN dbo.Orders o ON o.OrderID = aso.TemplateOrderID
         WHERE o.OrderStatusID = 7--active pulse
               AND aso.AutoshipScheduleID = 3--pulse
               AND a.Active = 1 --account active
               AND a.AccountTypeID = 1 --consultant only
               AND su.URL LIKE '%.myrandf.com'
              GROUP BY su.URL
                             , s.DistributorID)
     SELECT a.AccountID
          , CAST(a.AccountNumber AS NVARCHAR(50)) AS AccountNumber
          , CAST(SUBSTRING(sul.URL , CHARINDEX('http://' , sul.URL)+7 , CHARINDEX('.myrandf' , sul.URL)-8) AS NVARCHAR(150)) SitePrefix
          , ISNULL(ps.IsActive , 0) IsActive
          , a.FirstName+' '+a.LastName ConsultantName
          , a.Birthday
          , ap.PhoneNumber
          , MAX(p1.SKU) FavProductSKU1
          , MAX(fp1ld.Name) FavProduct1
          , MAX(p2.SKU) FavProductSKU2
          , MAX(fp2ld.Name) FavProduct2
          , CAST(CASE
                     WHEN ISNULL(ms.Html , '') = '' THEN 'No matter your age, gender, skin tone or ethnicity, Rodan and Fields® products can help you to achieve healthier, younger-looking skin. As an R+F Independent Consultant, I love that I am able to help people live better in their skin and become more empowered in their lives. When my Customers ask me how they can achieve clearer, more radiant-looking skin, I tell them that the R+F secret to great-looking skin is Multi-Med Therapy®, using the right ingredients in the right formulations and applying them in the right order. From their cleansers to their sunscreens, all R+F products are specially formulated to complement each other. I have seen with my own eyes the powerful effects of these products. And I always look forward to seeing and celebrating their results.'
                     ELSE ms.Html
                 END AS NVARCHAR(MAX)) AS MyStory
          , CAST(CASE
                     WHEN ISNULL(mbm.MyBestMoment , '') = '' THEN 'Rodan + Fields® has brought confidence, freedom, connections and fun into my life. With online resources for skincare recommendations, product tips and business training, I am able to help my Customers experience healthier, younger-looking skin. As an R+F Independent Consultant, I am my own boss and free to create my own schedule. I have also made meaningful connections with so many amazing people. From other Consultants to Customers to the Home Office Team, the R+F Community is engaging, energetic, and makes being an R+F Consultant more fun than I could ever imagine.'
                     ELSE mbm.MyBestMoment
                 END AS NVARCHAR(250)) AS MyBestMoment
          , CAST(mp.PhotoURL AS NVARCHAR(500)) AS PhotoURL
          , CAST(MIN(ma.EmailAddress) AS NVARCHAR(250)) EmailAddress
		  INTO DM_QA.dbo.PWS_US 
		 -- DROP TABLE DM_QA.dbo.PWS_US
     FROM Sites sul
          JOIN dbo.Accounts a ON sul.DistributorID = a.AccountID
          LEFT JOIN MyStory ms ON sul.SiteID = ms.SiteID
          LEFT JOIN MyPhoto mp ON sul.SiteID = mp.SiteID
          LEFT JOIN MyBestMoment mbm ON sul.SiteID = mbm.SiteID
          LEFT JOIN PWSSubscription ps ON a.AccountID = ps.AccountID
          LEFT JOIN dbo.AccountPhones ap ON a.AccountID = ap.AccountID
          LEFT JOIN dbo.SiteSettings ss ON ss.SiteSettingID IN ( 9 , 10 )
          LEFT JOIN dbo.SiteSettingValues ssv ON ss.SiteSettingID = ssv.SiteSettingID
                                                 AND sul.SiteID = ssv.SiteID
          LEFT JOIN dbo.ProductDescriptions fp1pd ON ssv.Value = fp1pd.ProductID
                                                     AND ss.SiteSettingID = 9
          LEFT JOIN dbo.LocalDescriptions fp1ld ON fp1pd.LocalDescriptionID = fp1ld.LocalDescriptionID
                                                   AND fp1ld.LanguageID = 1
          LEFT JOIN dbo.Products p1 ON ssv.Value = p1.ProductID
                                       AND ss.SiteSettingID = 9
          LEFT JOIN dbo.ProductDescriptions fp2pd ON ssv.Value = fp2pd.ProductID
                                                     AND ss.SiteSettingID = 10
          LEFT JOIN dbo.LocalDescriptions fp2ld ON fp2pd.LocalDescriptionID = fp2ld.LocalDescriptionID
                                                   AND fp2ld.LanguageID = 1
          LEFT JOIN dbo.Products p2 ON ssv.Value = p2.ProductID
                                       AND ss.SiteSettingID = 10
          LEFT JOIN dbo.MailAccount ma ON a.AccountID = ma.AccountID
                                          AND ma.Active = 1
     WHERE sul.URL NOT LIKE '%.biz%'
           AND ISNULL(ps.IsActive , 0) = 1
     GROUP BY SUBSTRING(sul.URL , CHARINDEX('http://' , sul.URL)+7 , CHARINDEX('.myrandf' , sul.URL)-8)
            , a.FirstName+' '+a.LastName
            , a.AccountID
            , a.AccountNumber
            , ps.IsActive
            , a.Birthday
            , ap.PhoneNumber
            , ms.Html
            , mbm.MyBestMoment
            , mp.PhotoURL;
