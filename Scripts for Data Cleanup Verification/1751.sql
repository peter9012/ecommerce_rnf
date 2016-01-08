--Checking Account level CC. Diner is translated to discover so we should not see any record with Vendorid=8(diners)
		SELECT CCP.VENDORID,CCV.* FROM RFOperations.RFO_Accounts.CreditCardProfiles CCP, 
		RodanFieldsLive..AccountPaymentMethods app ,
		RFOperations.RFO_Reference.CreditCardVendors CCV
		WHERE CCP.PAYMENTPROFILEID=APP.AccountPaymentMethodID AND CCV.VENDORID=CCP.VENDORID AND CCV.VENDORID=8

		--Checking Autoship level CC. Diner is translated to discover so we should not see any record with Vendorid=8(diners)
		SELECT AP.* ,CCV.NAME FROM  RFOperations.Hybris.AutoshipPayment AP, 
		RodanFieldsLive..OrderPayments OP,
		RFOPERATIONS.HYBRIS.AUTOSHIP A,
		RFOperations.RFO_Reference.CreditCardVendors CCV
		WHERE AP.AutoshipPaymentID=OP.ORDERPAYMENTID AND A.AUTOSHIPID=AP.AUTOSHIPID AND CCV.VENDORID=AP.VENDORID AND CCV.VENDORID=8

		SELECT AP.* ,CCV.NAME FROM  RodanFieldsLive..OrderPayments OP,
		RFOPERATIONS.HYBRIS.ORDERS A,
		RFOperations.RFO_Reference.CreditCardVendors CCV
		WHERE AP.AutoshipPaymentID=OP.ORDERPAYMENTID AND A.ORDERID=AP.AUTOSHIPID AND CCV.VENDORID=AP.VENDORID AND CCV.VENDORID=8