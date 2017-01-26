USE DM_QA
GO 

IF OBJECT_ID('dbqa.Map_tab') IS NULL 
CREATE TABLE  dbqa.Map_tab
(MapID INT IDENTITY(1,1) PRIMARY KEY,
SourceObject NVARCHAR(100),--RFOperations.dbo.Accounts
SourceColumn NVARCHAR(50),--FirstName
SourceDataTypes NVARCHAR(50),--Nvarchar(20)
SourceRef NVARCHAR(MAX)-- Direct or Transform
,TargetObject NVARCHAR(100),--Hybris.dbo.Users
TargetColumn NVARCHAR(50),--P_FirstName
TargetDataTypes NVARCHAR(50),-- Nvarchar(20)
[Key] NVARCHAR(50),--AccountNumber
TargetRef NVARCHAR(MAX),--ComposedTypes
Flag NVARCHAR(10), --C2C or Default or Manual 
[Owner] NVARCHAR(50),
[SQL Stmt] NVARCHAR(Max)--'Select * from TableA a join TabelB b on a.ID=b.ID Where a.Fields<>b.Fields'
)

--********************************************************************************************
--												OKTA
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* Products Table in Hybris.*/
(N'RFOperations.RFO_Accounts.AccountBase' ,N'AccountID',N'Bigint' ,N'' ,N'OKTA' ,N'p_AccountID' ,N'Bigint' ,N'AccountID' ,N'' , N'Key' ,N'OKTA',N'') 
,-- AccountID 
(N'RFOperations.Security.AccountSecurity' ,N'FirstName',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_firstName' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- FirstName 
(N'RFOperations.Security.AccountSecurity' ,N'LasttName',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_lasttName' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- LasttName 
(N'RFOperations.Security.AccountSecurity' ,N'Password',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_Password' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- Password 
(N'RFOperations.Security.AccountSecurity' ,N'UserName',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_UserName' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- UserName 
(N'RFOperations.RFO_Accounts.EmailAddresses' ,N'Email',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_Email' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
-- Email 