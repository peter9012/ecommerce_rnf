

DECLARE @Email NVARCHAR(225)= 'autoconswpwcrp20170302104715@mailinator.com'


SELECT * FROM #u
SELECT 'User',  *
FROM    Hybris.dbo.users
WHERE   p_uid = @Email


SELECT * FROM #c

SELECT 'Carts', c.*
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'


SELECT * FROM #cj
SELECT 'CronJob' ,cj.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'



SELECT * FROM #j
SELECT 'Jobs', j.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 	
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'



SELECT * FROM #t
SELECT 'Triggers',  t.*
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
		LEFT JOIN Hybris.dbo.triggerscj t ON t.p_cronjob=cj.pk
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'


/*
hjmpTS 7 >8 

modifiedTS > GetDate 
p_activationtime to RunTime(DateTime2)
p_displayactivationdate to RunTime(DateTime2)

*/

SELECT * FROM Hybris.dbo.enumerationvalues WHERE pk=8796097871963



/*


DECLARE @Email NVARCHAR(225)= 'autoconswpwcrp20170302104715@mailinator.com'

SELECT  *
INTO    #u
FROM    Hybris.dbo.users
WHERE   p_uid = @Email

SELECT  c.*
INTO    #c
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'


SELECT  cj.*
INTO    #cj
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'



SELECT  j.*
INTO    #j
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 	
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'

SELECT  t.*
INTO    #t
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
		LEFT JOIN Hybris.dbo.triggerscj t ON t.p_cronjob=cj.pk
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code='b2bAcceleratorCartToOrderJob'



*/




DECLARE @Email NVARCHAR(225)= 'autoconswpwcrp20170302104715@mailinator.com'


SELECT * FROM #u

SELECT 'User',  *
FROM    Hybris.dbo.users
WHERE   p_uid = @Email


SELECT * FROM #c

SELECT 'Carts', c.*
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'



SELECT * FROM #cj
SELECT 'CronJob' ,cj.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'


SELECT * FROM #j
SELECT 'Jobs', j.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 	
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'



SELECT * FROM #t
SELECT 'Triggers',  t.*
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
		LEFT JOIN Hybris.dbo.triggerscj t ON t.p_cronjob=cj.pk
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'








/*


DECLARE @Email NVARCHAR(225)= 'autoconswpwcrp20170302104715@mailinator.com'

SELECT  *
INTO    #u
FROM    Hybris.dbo.users
WHERE   p_uid = @Email

SELECT  c.*
INTO    #c
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'


SELECT  cj.*
INTO    #cj
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'



SELECT  j.*
INTO    #j
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 	
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'

SELECT  t.*
INTO    #t
FROM    Hybris.dbo.users u
         LEFT JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        LEFT JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.pk
		LEFT JOIN Hybris.dbo.jobs j ON j.pk=cj.p_job 
		LEFT JOIN Hybris.dbo.triggerscj t ON t.p_cronjob=cj.pk
WHERE   u.p_uid = @Email AND c.p_ordertype <>'Order'AND j.p_code<>'b2bAcceleratorCartToOrderJob'

*/