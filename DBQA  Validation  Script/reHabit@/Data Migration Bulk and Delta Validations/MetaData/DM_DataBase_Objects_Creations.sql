USE [master]
GO
/****** Object:  Database [DM_QA]    Script Date: 12/7/2016 2:59:16 PM ******/

IF  DB_ID('DM_QA') IS   NULL 
CREATE DATABASE DM_QA

-- Validating If Exists or Not 

ELSE 
PRINT 'DataBase not Created Yet'


/****** Object:  Schema dbqa   Script Date: 12/7/2016 3:05:11 PM ******/
USE DM_QA 
GO

CREATE SCHEMA dbqa

GO

-- Validating Existance of Schema 
USE DM_QA
GO 
IF  SCHEMA_ID('dbqa') IS NOT NULL 
PRINT 'Schema Created'
ELSE
PRINT 'Error with Creating Schema'



---Creating Message Stored Procedure.

USE DM_QA 
GO 

/*
	Author:			NAND KHADKA
	Date:			12/07/2016
	Description:	Wrapper proc that executes a RAISERROR WITH NOWAIT message.
					
	Sample Execute:
					EXEC Datamigration.uspPrintMessage N'test message'
*/	



CREATE PROCEDURE dbqa.uspPrintMessage
	@Message NVARCHAR(4000)
   ,@ElapsedTime VARCHAR(30) = NULL
AS 
	BEGIN
		IF @ElapsedTime IS NULL 
			RAISERROR(@Message, 10, 1) WITH NOWAIT 
		ELSE 
			RAISERROR(@Message, 10, 1, @ElapsedTime) WITH NOWAIT
	END

GO

--EXEC dbqa.[uspPrintMessage]  @Message = N'SP Created' -- nvarchar(4000)
--    ,@ElapsedTime = '10:00' -- varchar(30)


--***************************************
-- Creation of Map_Tab(Mapping Table)
--***************************************

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



SELECT * FROM dbqa.Map_tab
		  
-- Parent Table Validation:


-- SELECT  b.PK,FirstName,P_FirstName FROM #RFO a
--join #Hybris b on a.AccountID = b.PK Where FirstName<>P_FirstName

/*
--With CommonKey(AccountNumebr VS P_rfaccountID)
1. Source to Target Count Validation By Country >>> Provide a Log each Run to track in LogTable1
 RFO Key Bridge Table Validation:--
2. Missing in Source Counts and Sample and Missing in Target Counts and Sample .>> Provide counts a Log each Run to track in LogTable1 and Missing Sample in LogTable2
3.Duplicate Validation If any. Provide Counts a Log each Run to track in LogTable1 and Duplicate Sample in LogTable2
4. Provide Loaded Counts for Fields to Fields Validation.
-- 4. Fields to Fields Validation and  Default Issues Counts:will provide end of excution of valitaion scripts:
5. Provide All  Fields to Fields Issue details Logs Table3 (SourceKey,SourceFields,SourceValue,TargetKey,TargetFields,TargetValue) 
6. Default Validtion: All  Issues in Table3.


*/


--*******************************************************************
-- for Record Count Level Log By Objects,ValidationTypes and By Attributes
--*******************************************************************

USE DM_QA
GO 

IF OBJECT_ID(N'dbqa.SourceTargetLog') IS NULL
    CREATE TABLE dbqa.SourceTargetLog
        (
          LogID INT IDENTITY(1, 1) ,
          FlowTypes NVARCHAR(50) NOT NULL , -- Bulk
          ValidationTypes NVARCHAR(50) NOT NULL,--Counts
          [Owner] NVARCHAR(50) NOT NULL , --Accounts
          SourceCount INT ,--100
          TargetCount INT ,--200
          Comments NVARCHAR(100) , --100 more in Target
		  ExecutionStatus NVARCHAR(20),
          RunDate DATETIME NOT NULL DEFAULT GETDATE() ,
          ValidatedBy NVARCHAR(50) NOT NULL  DEFAULT USER_NAME(),
		  ElapsedTime int
        )



		SELECT * FROM dbqa.SourceTargetLog

--*******************************************************************
-- DETAIL LOGS BY  ATTRIBUTES , DUPLICATES AND MISSING.
--*******************************************************************


USE DM_QA
GO 

IF OBJECT_ID(N'dbqa.ErrorLog') IS NULL
    CREATE   TABLE dbqa.ErrorLog
        (
          LogID INT IDENTITY(1, 1) ,
          FlowTypes NVARCHAR(50) ,-- Data Migrations
          [Owner] NVARCHAR(50) ,-- Accounts
          Flag NVARCHAR(50) ,-- c2c ,missing or duplicates
          SourceColumn NVARCHAR(50) ,
          TargetColumn NVARCHAR(50) ,
          [Key] NVARCHAR(50) ,
          SourceValue NVARCHAR(50) ,
          TargetValue NVARCHAR(50) ,
          RunDate DATETIME DEFAULT GETDATE() ,
          ExeCutedBy NVARCHAR(50) DEFAULT USER_NAME() ,
          ElapsedTime INT
        )

		SELECT * FROM DM_QA.dbqa.ErrorLog