
--===============================================
--Validating  Cron Job Modification In PROD
--===============================================



/* ACCOUNTS LEVEL   */



/* 1.Renewal Reminder Email job */

-- Fetches all active customers  for which expiration date is 30 days after current date.
-- Validates if Consultants are eligible for autorenewal against RFO


SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(DATEADD(DAY, 30, GETDATE()) AS DATE);


--Should Create another Validation Scripts to Exclude those eneligible users If required.


 
 /* 2.autoRenewalEmailJob */

-- Fetches all Active customers  for which expiration date is today.
-- Validates if Consultants are eligible for autorenewal against RFO
-- Autorenew the eligible consultants


SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(GETDATE() AS DATE)
        AND u.p_consultantsince IS NOT NULL;

--Should Create another Validating Scripts to Exclude those eneligible  if required.
--Should Create Another Validating Scripts to Validated to renewed Consultants.




/* 3. anniversaryEmailJob */

--Fetches all customers  having expiration date today.

SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(GETDATE() AS DATE)
--AND u.p_consultantsince IS NOT NULL 
--Make Sure it is for all the Users not only Consultants.
;



/* 4.renewedConsultantshipEmailJob  */

--Fetches all customers  for which expiration date is 30 days  after current date.


SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(DATEADD(DAY, 30, GETDATE()) AS DATE);

--Make sure it is for all users.


/* 5.requestedTerminationEmailJob */

--Fetches all customers  for which expiration date is 30 days  after current date.Sends reminder email.

SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(DATEADD(DAY, 30, GETDATE()) AS DATE);




/* 6.terminationNoticeEmailJob */

--Feteches all Customer(Consultants) having status ACTIVE and expriationDate 30 days past
 --to current date (in failover codition 31 days past).
-- 1) Terminate account on EIS.
--2) Cancels autoships.
--3) Set account status SOFTTERMINATEDINVOLUNTARY in hybris and disables login.
--4) Send termination notification to consultant and sponsor.
 
 
SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(DATEADD(DAY, -30, GETDATE()) AS DATE);


--for FailOver Condition :

SELECT  COUNT(*)--should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(u.p_expirationdate AS DATE) = CAST(DATEADD(DAY, -31, GETDATE()) AS DATE);




		/* 7.fifteenDaysPSRemainderJob  */
		

		--This job generates notification for pulse enrollment.This job will pick all consultants
		--  having consultant since date 15 days before from current date and customer not enrolled in pulse. 

		
SELECT  COUNT(*)
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(ISNULL(u.p_expirationdate, '1900-01-01') AS DATE) = CAST(DATEADD(DAY,
                                                              -15, GETDATE()) AS DATE)
        AND ISNULL(u.p_enrolledaspulse, 0) <> 1;

		

--OR Alternative Validation:

SELECT  COUNT(*)--Should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(ISNULL(u.p_expirationdate, '1900-01-01') AS DATE) = CAST(DATEADD(DAY,
                                                              -15, GETDATE()) AS DATE)
        AND u.PK NOT IN ( SELECT    userpk
                          FROM      Hybris.dbo.orders
                          WHERE     p_template = 1
                                    AND p_active = 1
                                    AND currencypk = 8796125855777
                                    AND TypePkString = 8796124741714 );


									


/* 8.twentyNineDaysPSRemainderJob  */

--This job generates notification for pulse enrollment .This job will pick all consultants 
-- having consultant since date 29 days before current date and customer not enrolled in pulse. 


		
		
SELECT  COUNT(*)
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(ISNULL(u.p_expirationdate, '1900-01-01') AS DATE) = CAST(DATEADD(DAY,
                                                              -29, GETDATE()) AS DATE)
        AND ISNULL(u.p_enrolledaspulse, 0) <> 1;

		

--OR Alternative Validation:

SELECT  COUNT(*)--Should be Null 
FROM    Hybris..users u
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   CAST(ISNULL(u.p_expirationdate, '1900-01-01') AS DATE) = CAST(DATEADD(DAY,
                                                              -29, GETDATE()) AS DATE)
        AND u.PK NOT IN ( SELECT    userpk
                          FROM      Hybris.dbo.orders
                          WHERE     p_template = 1
                                    AND p_active = 1
                                    AND currencypk = 8796125855777
                                    AND TypePkString = 8796124741714 );







/* AUTOSHIP TEMPLATES LEVEL */




/* 1.Autoship CRP ProcessingJob */


--Checking if any US Templates is Going to be Picked within Cut Over Period,
--This job is used to place CRP order for consultant based on CRP autoship template.
--Process all template :
--1. Account is active. And Template is Active
--2. Scheduling date  < Today. and
--3. Scheduling Date > Today-30 and
--4. (Last Processing Date < Today -5 or null) and
--5. cc failure count <=3


SELECT  COUNT(*)--Should be Null After Updating Devs Scripts.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
                                AND u.p_country = 8796100624418
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'ACTIVE'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777
        AND ho.TypePkString = 8796124676178
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, -30,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE)
        AND ho.p_ccfailurecount <= 3
        AND CAST(ISNULL(ho.p_lastprocessingdate, '1900-01-01') AS DATE) < CAST(DATEADD(DAY,
                                                              -5, GETDATE()) AS DATE);

SELECT  COUNT(*) ,
        CAST(ho.p_schedulingdate AS DATE) SchedulingDate --Should be Null After Updating Devs Scripts.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
                                AND u.p_country = 8796100624418
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'ACTIVE'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777
        AND ho.TypePkString = 8796124676178
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, -30,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE)
        AND ho.p_ccfailurecount <= 3
        AND CAST(ISNULL(ho.p_lastprocessingdate, '1900-01-01') AS DATE) < CAST(DATEADD(DAY,
                                                              -5, GETDATE()) AS DATE)
GROUP BY CAST(ho.p_schedulingdate AS DATE);


		-- Checking If Any US Templates generated Orders During Cut Over Period:

SELECT  COUNT(*)
FROM    Hybris..orders a
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777 --US
        AND a.TypePkString = 8796124676178 --CRP
        AND CAST(ho.createdTS AS DATE) >= CAST(GETDATE() AS DATE);
				--Keep Change the orderCreatedDate to Cover Cut Over Period.

/* 2.Autoship PCPerks ProcessingJob */


--Checking if any US Templates is Going to be Picked within Cut Over Period:
--This job is used to place CRP order for consultant based on CRP autoship template.
--Process all template :
--1. Account is active. And Template is Active
--2. Scheduling date  < Today. and
--3. Scheduling Date > Today-30 and
--4. (Last Processing Date < Today -5 or null) and
--5. cc failure count <=4


SELECT  COUNT(*)--Should be Null After Updating Devs Scripts.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
                                AND u.p_country = 8796100624418
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'ACTIVE'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777 --US
        AND ho.TypePkString = 8796124708946 --PCPerks
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, -30,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE)
        AND ho.p_ccfailurecount <= 4
        AND CAST(ISNULL(ho.p_lastprocessingdate, '1900-01-01') AS DATE) < CAST(DATEADD(DAY,
                                                              -5, GETDATE()) AS DATE);


SELECT  COUNT(*) ,
        CAST(ho.p_schedulingdate AS DATE) SchedulingDate --Should be Same  After Updating Devs Scripts.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
                                AND u.p_country = 8796100624418
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'ACTIVE'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777 --US
        AND ho.TypePkString = 8796124708946 --PCPerks
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, -30,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE)
        AND ho.p_ccfailurecount <= 4
        AND CAST(ISNULL(ho.p_lastprocessingdate, '1900-01-01') AS DATE) < CAST(DATEADD(DAY,
                                                              -5, GETDATE()) AS DATE)
GROUP BY CAST(ho.p_schedulingdate AS DATE);



		-- Checking If Any US Templates generated Orders During Cut Over Period:
		

SELECT  COUNT(*)
FROM    Hybris..orders a
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777 --US
        AND a.TypePkString = 8796124708946 --PCPerks
        AND CAST(ho.createdTS AS DATE) >= CAST(GETDATE() AS DATE);
				--Keep Change the orderCreatedDate to Cover Cut Over Period.

		

/* 3. Autoship PULSE ProcessingJob */


--Checking if any US Templates is Going to be Picked within Cut Over Period,
--This job is used to place CRP order for consultant based on CRP autoship template.
--Process all template :
--1. Account is active. And Template is Active
--2. Scheduling date  < Today. and
--3. Scheduling Date > Today-30 and
--4. (Last Processing Date < Today -5 or null) and
--5. cc failure count <=3

SELECT  COUNT(*)--Should be Null After Updating Devs Scripts.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
                                AND u.p_country = 8796100624418
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'ACTIVE'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777 --US
        AND ho.TypePkString = 8796124741714 --PULSE
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, -30,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE)
        AND ho.p_ccfailurecount <= 3
        AND CAST(ISNULL(ho.p_lastprocessingdate, '1900-01-01') AS DATE) < CAST(DATEADD(DAY,
                                                              -5, GETDATE()) AS DATE);


SELECT  COUNT(*) ,
        CAST(ho.p_schedulingdate AS DATE) SchedulingDate --Should be Same for all  After Updating Devs Scripts.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
                                AND u.p_country = 8796100624418
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'ACTIVE'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777 --US
        AND ho.TypePkString = 8796124741714 --PULSE
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, -30,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE)
        AND ho.p_ccfailurecount <= 3
        AND CAST(ISNULL(ho.p_lastprocessingdate, '1900-01-01') AS DATE) < CAST(DATEADD(DAY,
                                                              -5, GETDATE()) AS DATE)
GROUP BY CAST(ho.p_schedulingdate AS DATE);



		-- Checking If Any US Templates generated Orders During Cut Over Period:

SELECT  COUNT(*)
FROM    Hybris..orders a
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777 --US
        AND a.TypePkString = 8796124741714 --PULSE
        AND CAST(ho.createdTS AS DATE) >= CAST(GETDATE() AS DATE);
				--Keep Change the orderCreatedDate to Cover Cut Over Period.


/* 4.autoshipCCExpiryCheckEmailJob */

--Send Notice of Credit Card Expiration to customers whose autoship scheduled 10 days 
--after current date.This cron job will validate whether autoship(CRP/Pulse/PC) template's 
--Credit Card is expired or about to expire (month and year expiration).

SELECT  COUNT(ho.PK)
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..paymentinfos pp ON pp.OwnerPkString = ho.PK
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777
        AND CAST(ho.p_schedulingdate AS DATE) = CAST(DATEADD(DAY, 10,
                                                             GETDATE()) AS DATE)
        AND DATEPART(YEAR, GETDATE()) = pp.p_validtoyear
        AND DATEPART(MONTH, GETDATE()) >= pp.p_validtomonth;


		---Verify in PROD for Data Conversion or Other tables to get Expiration Month and Year.


	
	
/* 5.autoshipReplenishmentShipmentNotificationJob */

--Sending Email to Confirm CRP/PC Replenishment Order is About to Ship (5 Days)
--it  genarates notification for all consultants which are 
--having autoship date schedule in next 5 days from current date .
--Send Email to Confirm CRP/PC Replenishment Order
-- is About to Ship (5 Days)

SELECT  COUNT(ho.PK)--Should be Zero Specially for CRP and PCperks.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON ho.userpk = u.PK
        JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Value = 'Active'
        JOIN Hybris..countries c ON u.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND u.p_sourcename = 'Hybris-DM'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777
        AND CAST(ho.p_schedulingdate AS DATE) BETWEEN CAST(DATEADD(DAY, 5,
                                                              GETDATE()) AS DATE)
                                              AND     CAST(GETDATE() AS DATE);
		



		/*  6. pcPerksSoftTerminationCronJob
		*/

	--		This cronjob checks for PC autoship having :
	--1) Scheduling date 10 past to todays date
	--2) cc failure >=4

	
SELECT  COUNT(ho.PK)--Should be Zero Specially for CRP and PCperks.
FROM    Hybris..orders ho
        --JOIN Hybris..users u ON ho.userpk = u.PK
        --JOIN Hybris..vEnumerationValues v ON v.PK = u.p_accountstatus
        --                                     AND v.Value = 'Active'
        --JOIN Hybris..countries c ON u.p_country = c.PK
        --                            AND c.isocode = 'US'
        --                            AND u.p_sourcename = 'Hybris-DM'
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777
        AND ho.TypePkString = 8796124708946
        AND ho.p_ccfailurecount >= 4
        AND CAST(ho.p_schedulingdate AS DATE) < CAST(DATEADD(DAY, -10,
                                                             GETDATE()) AS DATE);
           
		   
		   /* 
		     7. AutoshipOrderDeactivateJob 
		   */   
		   
			--fetches all INACTIVE CRP/Pulse autoship order templates for 
			--which cc failure count is more than 0.    
			
			
SELECT  COUNT(ho.PK)--Should be Zero Specially for CRP and PCperks.
FROM    Hybris..orders ho
WHERE   ho.p_template = 1
        AND ho.p_active = 0
        AND ho.currencypk = 8796125855777
        AND ho.TypePkString IN ( 8796124676178, 8796124741714 )
        AND ho.p_ccfailurecount >= 0
        AND CAST(ho.p_schedulingdate AS DATE) < CAST(GETDATE() AS DATE);



		/*
		16.crpPulseSoftTerminationCronJob 
		*/
		--Condition:
			--This cronjob checks for CRP/Pulse autoships having :
			--1) cc failure >=3 
			--2) scheduling date should be from last month 
			 --Effects:
			--1) Cancels the found CRP/Pulse autoships
			--2) Sends emails to customer about CRP/Pulse cancellation. 
			
			
				
SELECT  COUNT(ho.PK)--Should be Zero Specially for CRP and PCperks.
FROM    Hybris..orders ho
WHERE   ho.p_template = 1
        AND ho.p_active = 1
        AND ho.currencypk = 8796125855777
        AND ho.TypePkString IN ( 8796124676178,--CRP TEMPLATES
                                 8796124741714 -- PULSE TEMPLATES
		 )
        AND ho.p_ccfailurecount >= 3
        AND CAST(ho.p_schedulingdate AS DATE) < '2016-01-01'; -- SHOULD BE THE FIRST DAY OF THE MONTH.

                
				
			/*
			RemoveDanglingTransientOrdersCronJob  AND AvalaraTaxRetryLaterJob BEEN EXCLUDED FOR VALIDATION.

			*/