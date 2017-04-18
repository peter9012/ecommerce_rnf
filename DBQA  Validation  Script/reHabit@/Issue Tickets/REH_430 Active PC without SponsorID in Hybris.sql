
SELECT  rf.AccountID [RFAccount] ,
u.p_defaultb2bunit,
        rf.SponsorId AS [RFSponsorID] ,
        CONCAT(ac.FirstName, SPACE(1), ac.LastName) AS RFName ,
        u.p_customerid [HybrispCustomerID] ,
        u.p_newsponsorid ,
		su.p_customerid[LoadedSponserID],
        u.p_name ,
        v.Code [HybrisAccountStatus] ,
        t.Code [HybrisAccountTypes] ,
        u.p_uid ,
        u.p_softterminateddate ,
        u.p_hardterminateddate,u.p_defaultb2bunit
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        JOIN RFOperations.RFO_Accounts.accountcontacts ac ON ac.AccountId = rf.AccountID
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(rf.AccountID AS NVARCHAR(225))
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
        JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
		JOIN Hybris.dbo.users su ON su.p_customerid=cast(rf.SponsorId AS NVARCHAR(225))
WHERE   u.p_newsponsorid IS NULL
        AND AccountTypeID IN ( 2 )-- PC Accounts
        AND v.Code = 'Active' --AND u.p_customerid='6903243'

	


SELECT  rf.AccountID [RFAccount] ,
u.p_defaultb2bunit,
        rf.SponsorId AS [RFSponsorID] ,
        CONCAT(ac.FirstName, SPACE(1), ac.LastName) AS RFName ,
        u.p_customerid [HybrispCustomerID] ,
        u.p_newsponsorid ,
		su.p_customerid[LoadedSponserID],
        u.p_name ,
        v.Code [HybrisAccountStatus] ,
        t.Code [HybrisAccountTypes] ,
        u.p_uid ,
        u.p_softterminateddate ,
        u.p_hardterminateddate,u.p_defaultb2bunit
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        JOIN RFOperations.RFO_Accounts.accountcontacts ac ON ac.AccountId = rf.AccountID
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(rf.AccountID AS NVARCHAR(225))
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
        JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
		JOIN Hybris.dbo.users su ON su.p_customerid=cast(rf.SponsorId AS NVARCHAR(225))
WHERE   su.p_defaultb2bunit IS NULL
        AND AccountTypeID IN ( 2 )-- PC Accounts
        AND v.Code = 'Active' --AND u.p_customerid='6903243'



        SELECT  *
        FROM    ( SELECT    ab.AccountID ,
                            ab.AccountNumber ,
                            rf.SponsorId ,
                            ac.FirstName ,
                            ac.LastName ,
                            ea.EmailAddress ,
                            t.Name [RFAccountType] ,
                            rf.HardTerminationDate ,
                            rf.SoftTerminationDate ,
                            rf.Active,
							s.Active SActive,s.SitePrefix,
							acs.Username[BaseAccountUserNamee]
                  FROM      RFOperations.RFO_Accounts.AccountBase ab
                            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                                                              AND ab.AccountTypeID = 1
                                                              AND rf.Active = 1
                            JOIN RFOperations.RFO_Accounts.accountcontacts ac ON ac.AccountId = ab.AccountID
                            JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                            JOIN RFOperations.RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId
                                                              AND ea.EmailAddressTypeID = 1
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
							JOIN RFOperations.Security.AccountSecurity acs ON acs.AccountID = ab.AccountID
							JOIN RFOperations.Hybris.sites s ON s.AccountID = ab.AccountID
                  WHERE     EXISTS ( SELECT 1
                                     FROM   hybris.dbo.users u
                                     WHERE  u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225)) )
                ) Base
                JOIN ( SELECT   ab.AccountID AS SponsorID,
                                acs.Username SponsorUserName,
                                acs.Password SponsorPassword
                       FROM     RFOperations.RFO_Accounts.AccountBase ab
                                JOIN RFOperations.Security.AccountSecurity acs ON acs.AccountID = ab.AccountID
                     ) Sp ON Base.SponsorId = sp.SponsorID
                JOIN ( SELECT   u.p_customerid ,
                                u.p_uid ,
                                u.p_name ,
                                u.p_defaultb2bunit ,
                                u.p_newsponsorid ,
                                ug.pk [B2BSponsorPK] ,
                                ug.p_uid [B2B_uid] ,
                                v.Code [HybrisCustomerTypes]
                       FROM     hybris.dbo.users u
                                LEFT JOIN Hybris.dbo.usergroups ug ON u.p_defaultb2bunit = ug.pk
                                JOIN hybris.dbo.enumerationvalues v ON v.pk = u.p_type
                       WHERE    v.Code IN ( 'CONSULTANT', 'PREFERRED',
                                            'RETAIL' )
                     ) Hybris ON Hybris.p_customerid = CAST(Base.AccountID AS NVARCHAR(225))
					 WHERE Base.AccountID=4647220


					 SELECT * FROM Hybris.dbo.units