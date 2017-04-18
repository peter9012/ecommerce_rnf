USE [DM_QA]
GO

/****** Object:  StoredProcedure [dbo].[uspAccountBase_Hybris_RFO]    Script Date: 4/4/2017 5:42:25 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[uspAccountBase_Hybris_RFO]
    (
      @StartDate DATETIME2 ,
      @EndDate DATETIME2
    )
AS
    BEGIN
        SET NOCOUNT ON;
        SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

 -- Please Change as your Logic to Capture Delta
 --SET @StartDate=GETDATE()-2
 --SET @EndDate=GETDATE()

        SELECT  p_customerid [AccountID] ,
                p_accountnumber [AccountNumber] ,
                CASE s.Code
                  WHEN 'ACTIVE' THEN 1
                  WHEN 'HARDTERMINATED' THEN 10
                  WHEN 'INACTIVE_VOLUNTARY' THEN 8
                  WHEN 'INACTIVE_INVOLUNTARY' THEN 9
                  WHEN 'INACTIVE' THEN 2
                  ELSE NULL
                END [AccountStatus] ,
                CASE co.p_isocode
                  WHEN 'US' THEN 236
                  WHEN 'CA' THEN 40
                  WHEN 'AU' THEN 14
                  ELSE NULL
                END AS CountryID ,
                CASE cu.p_isocode
                  WHEN 'USD' THEN 4
                  WHEN 'CAD' THEN 38
                  WHEN 'AUD' THEN 13
                END currencyID ,
                CASE t.Code
                  WHEN 'CONSULTANT' THEN 1
                  WHEN 'PREFERRED' THEN 2
                  WHEN 'RETAIL' THEN 3
                END [AccountType] ,
                CASE la.p_isocode
                  WHEN 'en' THEN 4
                  ELSE NULL
                END [LanguageID] ,
                GETDATE() AS ServerModifieDate ,
                '<><><>Please provide Identical JOB Name ' [ChangedBYApplication] ,
                1 AS AccountContactType ,
                'DisplayTax Number' [Required] ,
                CASE WHEN CHARINDEX(' ', LTRIM(RTRIM(u.p_name))) > 1
                     THEN SUBSTRING(LTRIM(RTRIM(u.p_name)), 1,
                                    CHARINDEX(' ', LTRIM(RTRIM(u.p_name))))
                     ELSE LTRIM(RTRIM(u.p_name))
                END FistName ,
                CASE WHEN CHARINDEX(' ', LTRIM(RTRIM(u.p_name))) > 1
                     THEN SUBSTRING(LTRIM(RTRIM(u.p_name)),
                                    CHARINDEX(' ', LTRIM(RTRIM(u.p_name))),
                                    ( LEN(RTRIM(LTRIM(u.p_name)))
                                      - ( CHARINDEX(' ',
                                                    LTRIM(RTRIM(u.p_name)))
                                          - 1 ) ))
                     ELSE NULL
                END LastName ,
                u.p_name [LegalName] ,
                ISNULL(g.Code, 'Unknown') [gender] ,
                CASE t.Code
                  WHEN 'CONSULTANT' THEN con.SponsorID
                  WHEN 'PREFERRED' THEN pc.SponsorID
                  ELSE '2'
                END AS SponsorID ,
                u.createdTS [EnrollmentDate] ,
                u.p_softterminateddate [SoftTerminationDate] ,
                u.p_hardterminateddate [HardTerminationDate] ,
                u.p_active [Active] ,
                u.p_taxexempt [IsTaxExempt] ,
                u.p_businessentity [IsBusinessEntity] ,
                u.p_lastrenewaldate [LastRenewalDate] ,
                IIF(CONCAT(u.p_spousefirstname, SPACE(1), u.p_spouselastname) = '', NULL, CONCAT(u.p_spousefirstname,
                                                              SPACE(1),
                                                              u.p_spouselastname)) [CoApplicant] ,
                u.p_nextrenewaldate [NextRenewalDate] ,
                ad.p_streetname [AddressLine1] ,
                ad.p_streetnumber [AddressLine2] ,
                ad.p_postalcode [PostalCode] ,
                ad.p_town [Locale] ,
                rg.p_isocodeshort [Region] ,
                ad.p_phone1 [PhoneNumberRow]
        FROM    Hybris.dbo.users u
                LEFT JOIN Hybris.dbo.enumerationvalues s ON s.pk = u.p_accountstatus
                LEFT JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                LEFT JOIN Hybris.dbo.countries co ON co.pk = u.p_country
                LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = u.p_sessioncurrency
                LEFT JOIN Hybris.dbo.languages la ON la.pk = u.p_sessionlanguage
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = u.p_gender
                LEFT JOIN ( SELECT  pg.SourcePK ,
                                    su.p_customerid [SponsorID]
                            FROM    Hybris.dbo.pgrels pg
                                    JOIN Hybris.dbo.users su ON su.p_defaultb2bunit = pg.TargetPK
                                    JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                                              AND v.Code = 'Consultant'
                          ) Con ON con.SourcePK = u.p_defaultb2bunit
                LEFT JOIN ( SELECT  su.p_defaultb2bunit ,
                                    su.p_customerid [SponsorID]
                            FROM    Hybris.dbo.users su
                                    JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                                              AND v.Code = 'Consultant'
                          ) PC ON PC.p_defaultb2bunit = u.p_defaultb2bunit
                LEFT JOIN Hybris.dbo.addresses ad ON ad.pk = u.p_defaultshipmentaddress
                LEFT JOIN Hybris.dbo.regions rg ON rg.pk = ad.p_region
        WHERE   u.createdTS BETWEEN @StartDate AND @EndDate
    END 

GO


