--########################################################################################
--		CHECKING IF ANY ORDERS FROM AUTOSHIP NOT BEEN RECORDED IN RFL AUTOSHIP LOG TABLE
--########################################################################################

        SELECT  DISTINCT
                al.AutoshipLogID ,
                ral.AutoshipLogID AS RFLAutoshipLogID ,
                al.AutoshipID ,
                ral.TemplateOrderID ,
                al.OrderId ,
                ral.NewOrderID
        FROM    RFOperations.Logging.autoshipLog al WITH ( NOLOCK )
                JOIN RodanFieldsLive.dbo.AutoshipLog ral WITH ( NOLOCK ) ON ral.TemplateOrderID = al.AutoshipID
                                                              AND ral.AutoshipLogID = al.AutoshipLogID
                                                              AND al.AutoshipID = al.OrderId
        WHERE   --ral.NewOrderID <> al.OrderId AND             	  
			   ISNULL(ral.NewOrderID,0)<>0 ; 
			 
		--4744229 Records in STG2.
		


--#######################################################################################
--		 CHECKING IF ANY MISSING RECORDS IN AUTOSHIP LOG TABEL FROM RFL TO RFO
--#######################################################################################		

		 
        IF OBJECT_ID('tempdb..#AutoshipLogs_Missing') IS NOT NULL
            DROP TABLE #AutoshipLogs_Missing	
		 
	 SELECT AutoshipLogID
	 INTO #AutoshipLogs_Missing
	 FROM  RFOperations.[ETL].[synRFLiveAutoshipLog] (NOLOCK) asl
	 JOIN RodanFieldsLive.dbo.orders a ON a.orderID=asl.TemplateOrderID AND a.OrderTypeID IN (4,5)
	  EXCEPT
	 SELECT AutoshipLogID
	 FROM  RFOperations.Logging.autoshipLog (NOLOCK)


	 SELECT COUNT(*) [Total Records] -- SHOULD BE NULL AFTER INSERTING RFO AUTOSHIP AUDIT TABLE.
	FROM  #AutoshipLogs_Missing

	SELECT * FROM RFOperations.ETL.synRFLiveAutoshipLog WHERE autoshiplogID IN (SELECT * FROM  #AutoshipLogs_Missing)
	SELECT * FROM RFOperations.Logging.autoshipLog WHERE autoshiplogID IN (SELECT * FROM  #AutoshipLogs_Missing)
	-- 508,7063 Records in STG2


	