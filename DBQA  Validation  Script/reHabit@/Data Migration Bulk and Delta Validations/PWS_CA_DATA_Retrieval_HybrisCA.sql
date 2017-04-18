USE Hybris_04052017
GO 
SELECT DISTINCT
       A.*
     , R.P_NAME AS STATE_NAME
     , NULL AS ProvinceIsoCode2
     , NULL AS ProvinceIsoCode4 INTO DM_QA.dbo.PWS_CA
	  -- DROP TABLE DM_QA.dbo.PWS_CA
FROM ( SELECT item_t0.p_rfaccountid
            , item_t0.p_rfaccountnumber
            , item_t0.p_pwscustomername
            , item_t0.p_dateofbirth
            , item_t0.p_consultantstate AS State
            , item_t0.p_consultanttown AS town
            , item_t0.p_consultantphone
            , item_t0.p_quote
            , item_t0.p_bestmomentbiz
            , item_t0.p_aboutmebiz
            , item_t0.p_aboutmecom
            , item_t0.p_caption1
            , item_t0.p_caption2
            , item_t0.p_caption3
            , item_t0.p_showsince
            , item_t0.p_sponsorcontactemail
            , item_t0.p_fburl
            , item_t0.p_pinteresturl
            , item_t0.p_twitterurl
            , item_t0.p_instagramurl
            , item_t0.p_customurlprefix
            , item_t0.p_enrolledaspulse AS PWS_ACTIVE
            , item_t0.p_pwsaccessenddate AS PWS_EXPIRATION_DATE
            , item_t4.IsoCode
            , item_t0.modifiedTS AS C_MODIFIED_TIME
			
       FROM users item_t0(NOLOCK)
          , pgrels item_t1(NOLOCK)
          , usergroups item_t2(NOLOCK)
          , enumerationvalues item_t3(NOLOCK)
          , countries item_t4(NOLOCK)
       WHERE ( item_t0.PK = item_t1.SourcePK
               AND item_t1.TargetPK = item_t2.PK
               AND item_t2.UniqueID != 'consultantcustomergroup'
               AND item_t0.p_accountstatus = item_t3.PK
               AND item_t0.p_country = item_t4.PK
               AND item_t3.Code = 'ACTIVE'
               AND item_t4.IsoCode = 'CA' ) ) AS A
     LEFT JOIN regionslp AS R ON R.ITEMPK = A.State;

