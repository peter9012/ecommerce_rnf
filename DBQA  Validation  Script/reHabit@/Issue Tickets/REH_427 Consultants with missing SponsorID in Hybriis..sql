


	
	 	
SELECT  u.p_customerid AS AccountID ,
        su.PK ,
        su.p_customerid AS SponsorID ,
        u.p_uid ,
       -- su.p_uid ,
        rf.AccountID [RFO_AccountID] ,
        rf.SponsorId [RFSponsorID] ,
        rf.EnrollerId,rf.Active
FROM    Hybris.dbo.users u
        JOIN RFOperations_02022017.RFO_Accounts.AccountRF rf ON rf.AccountID = u.p_customerid
        JOIN Hybris.dbo.pgrels pg ON pg.SourcePK = u.p_defaultb2bunit
        JOIN Hybris.dbo.usergroups Sug ON Sug.PK = pg.TargetPK
        JOIN Hybris.dbo.users su ON su.p_defaultb2bunit = sug.PK
        JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                               AND v.Code = 'Consultant'
WHERE   rf.SponsorId <> su.p_customerid
AND rf.SoftTerminationDate IS NULL 




/* 

AccountID	PK	SponsorID	p_uid	RFO_AccountID	RFSponsorID	EnrollerId	Active
629194	8829675470852	512556	jodimcmahon@comcast.net	629194	521213	521213	1
617780	8818316967940	548782	suzieryansnell@gmail.com	617780	612373	612373	1
614718	8826456637444	510692	brendac@wcs.edu	614718	516833	516833	1
643741	8819583188996	512168	tammiadair@yahoo.com	643741	569650	569650	1
663347	8819583188996	512168	tal22059@yahoo.com	663347	518383	592732	1
578704	8818709299204	509635	accseymour@gmail.com	578704	571234	571234	1

*/


	 	
SELECT  u.p_customerid AS AccountID ,
        su.PK ,
        su.p_customerid AS SponsorID ,
        u.p_uid ,
      su.p_uid 
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.pgrels pg ON pg.SourcePK = u.p_defaultb2bunit AND u.p_customerid='629194'
        JOIN Hybris.dbo.usergroups Sug ON Sug.PK = pg.TargetPK
        JOIN Hybris.dbo.users su ON su.p_defaultb2bunit = sug.PK
        JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                               AND v.Code = 'Consultant'




											
											  
