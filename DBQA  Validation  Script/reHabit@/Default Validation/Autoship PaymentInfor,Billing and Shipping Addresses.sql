




-- Checking Whether Autoship Template having duplicate Paymentinfos.

SELECT  *
FROM    RFOperations_02022017.Hybris.AutoshipPayment
WHERE   AutoshipPaymentID IN (
        SELECT  MAX(ap.AutoshipShippingAddressID) AutoshipPaymentID,a.AutoshipID INTO #A1
			--   select ap.*
        FROM    RFOperations_02022017.Hybris.Autoship a
                JOIN RFOperations_02022017.Hybris.AutoshipShippingAddress ap ON ap.AutoshipID = a.AutoshipID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.carts c
                         WHERE  c.p_code = a.AutoshipNumber )
                AND EXISTS ( SELECT 1
                             FROM   RFOperations_02022017.Hybris.Autoship at
                                    JOIN RFOperations_02022017.Hybris.AutoshipItem ai ON ai.AutoshipId = at.AutoshipID
                                    JOIN RFOperations_02022017.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = at.AutoshipID
                                    JOIN RFOperations_02022017.Hybris.AutoshipPayment asa ON asa.AutoShipID = at.AutoShipID
                             WHERE  at.AutoshipID = a.AutoshipID )
                AND a.Active = 1
        GROUP BY a.AutoshipID )
        AND Token IS NULL 


		
        SELECT TOP 100   apa.* ,
                ad.*
        FROM    #A1 a
                JOIN RFOperations_02022017.Hybris.Autoship at ON at.AutoshipID = a.AutoshipID --AND  a.AutoshipID=12635136 
                JOIN Hybris.dbo.carts b ON CAST(b.p_code AS BIGINT) = a.AutoshipID AND b.p_ordertype LIKE '%Template%' 
                JOIN Hybris.dbo.addresses ad ON ad.pk = b.p_paymentaddress
                                                AND ad.p_billingaddress = 1
                JOIN RFOperations_02022017.Hybris.AutoshipShippingAddress apa ON apa.AutoShipID = a.AutoshipID
                                                              AND a.AutoshipPaymentID = apa.AutoshipShippingAddressID
															  WHERE  ISNUMERIC(b.p_code)=1

SELECT * FROM Hybris.dbo.addresses WHERE pk=9315451469847

SELECT * FROM RFOperations_02022017.Hybris.AutoshipShippingAddress a 
JOIN #A1 b ON b.AutoshipID = a.AutoShipID AND a.AutoshipShippingAddressID=b.AutoshipPaymentID AND a.AutoShipID=2544899

SELECT * FROM Hybris.dbo.carts WHERE p_code='2544899'

SELECT * FROM RFOperations_02022017.Hybris.AutoshipShippingAddress WHERE AutoShipID=2544899


															
    SELECT  a.AutoshipID ,
            COUNT(a.AutoshipID) [Repeat]
			--   select ap.*
    FROM    RFOperations_02022017.Hybris.Autoship a
            JOIN RFOperations_02022017.Hybris.AutoshipShippingAddress ap ON ap.AutoshipID = a.AutoshipID 
    WHERE   EXISTS ( SELECT 1
                     FROM   Hybris.dbo.carts c
                     WHERE  c.p_code = a.AutoshipNumber )
            AND EXISTS ( SELECT 1
                         FROM   RFOperations_02022017.Hybris.Autoship at
                                JOIN RFOperations_02022017.Hybris.AutoshipItem ai ON ai.AutoshipId = at.AutoshipID
                                JOIN RFOperations_02022017.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = at.AutoshipID
                                JOIN RFOperations_02022017.Hybris.AutoshipShippingAddress asa ON asa.AutoShipID = at.AutoShipID
                         WHERE  at.AutoshipID = a.AutoshipID ) AND a.Active=1 --AND ap.Token IS NOT NULL 
    GROUP BY a.AutoshipID
    HAVING  COUNT(a.AutoshipID) > 1



    SELECT TOP 10
            a.*

            SELECT  ab.AutoShipID ,
                    COUNT(ab.AutoshipPaymentAddressID)
            FROM    RFOperations_02022017.Hybris.Autoship a
                    JOIN hybris.dbo.carts c ON c.p_code = a.AutoshipNumber
                    JOIN RFOperations_02022017.Hybris.AutoshipPaymentAddress ab ON ab.AutoShipID = a.AutoshipID
            WHERE   a.AutoshipID = 12635136
            GROUP BY ab.AutoShipID
            HAVING  COUNT(ab.AutoshipPaymentAddressID) > 1




