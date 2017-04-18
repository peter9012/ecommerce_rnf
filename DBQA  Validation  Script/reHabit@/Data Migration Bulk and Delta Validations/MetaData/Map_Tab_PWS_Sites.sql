

 
USE DM_QA
GO 


--********************************************************************************************
--												PWS SITES __ CANADA 
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* Products Table in Hybris.*/
(N'Hybris_CA.dbo.CAN_POS_Data' ,N'p_rfaccountid',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_customerid' ,N'nvarchar' ,N'AccountID' ,N'' , N'Key' ,N'PWS_CA',N'') 
,-- AccountID 
(N'Hybris_CA.dbo.CAN_POS_Data' ,N'p_rfaccountnumber',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_accountnumber' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- AccountNumber 
(N'Hybris_CA.dbo.CAN_POS_Data' ,N'PWSName',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_name' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- AccountNumber 
(N'Hybris_CA.dbo.CAN_POS_Data' ,N'BirthDate',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_dateofbirth' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- DateofBirth
(N'Hybris_CA.dbo.CAN_POS_Data' ,N'PhoneNumber',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_phone' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- PhoneNumber 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Quote',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_quote' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Quote 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'bestmomentbiz',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_bestmomentbiz' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- bestmomentbiz 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'aboutmebiz',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_aboutmebiz' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- aboutmebiz 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'aboutmecom',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_aboutmecom' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- aboutmecom 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'caption1',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_caption1' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- caption1 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'caption2',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_caption2' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- caption2 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'caption3',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_caption3' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- caption3 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'ShowSince',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_showsince' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- ShowSince 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'SponsorEmail',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_sponsorcontactemail' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- SponsorEmail 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Facebook',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_fburl' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Facebook 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Pinterest',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_pinteresturl' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Pinterest 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Twitter',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_twitterurl' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Twitter 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Instagram',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_instagramurl' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Instagram 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'CustomerURLPrefix',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_customurlprefix' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- CustomerURLPrefix 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'PWSActive',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_active' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- PWSActive 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'PWSExpirationDate',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_expirationdate' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- PWSExpirationDate 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Country',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_country' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Country 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'ModifiedDate',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'ModifiedTS' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- ModifiedDate 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Town',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_town' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Town 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Region',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_region' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_CA',N'') 
,-- Region 


--********************************************************************************************
--								PWS SITES __ US 
--********************************************************************************************
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'AccountID',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_customerid' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- AccountID 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'AccountNumber',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_accountNumber' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- AccountNumber 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'SitePrefix',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_siteurl' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- SitePrefix 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'IsActive',N'bit' ,N'' ,N'Hybris.dbo.users' ,N'p_active' ,N'bit' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- IsActive 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'ConsultantName',N'Nvarchar' ,N'' ,N'Hybris.dbo.users' ,N'p_name' ,N'nvarchar' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- ConsultantName 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'Birthday',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_dateofBirth' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- Birthday 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'PhoneNumber',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_phone' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- PhoneNumber 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'FavProduct1',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_FavProduct1' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- FavProduct1 
  (N'Hybris_CA.dbo.CAN_POS_Data' ,N'FavProductSKU1',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_FavProductSKU1' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- FavProductSKU1 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N'FavProduct2',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_FavProduct2' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- FavProduct2 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N'FavProductSKU2',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_FavProductSKU2' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- FavProductSKU2 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N'MyStory',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'P_MyStory' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- MyStory 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N'MyBestMoment',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'P_MyStory' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- MyBestMoment 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N'PhotoURL',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_PhotoURL' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
,-- PhotoURL 
 (N'Hybris_CA.dbo.CAN_POS_Data' ,N' PulseEmail	',N'datetime' ,N'' ,N'Hybris.dbo.users' ,N'p_ PulseEmail' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'PWS_US',N'') 
--  PulseEmail	








