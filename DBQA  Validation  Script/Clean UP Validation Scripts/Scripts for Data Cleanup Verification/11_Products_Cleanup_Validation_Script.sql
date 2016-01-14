--Select * FROM RFOperations.Hybris.ProductBase WHERE productID=8796143648769
--SELECT * FROM RFOperations.Hybris.ProductBase WHERE productID=180

--Select * FROM RFOperations.Hybris.ProductsRF WHERE productID=8796143648769
--SELECT * FROM RFOperations.Hybris.ProductsRF WHERE productID=180

--Select * FROM RFOperations.Hybris.ProductIngredients WHERE productID=8796143648769
--SELECT * FROM RFOperations.Hybris.ProductIngredients WHERE productID=180

--Select * FROM RFOperations.Hybris.ProductCountry WHERE productID=8796143648769
--SELECT * FROM RFOperations.Hybris.ProductCountry WHERE productID=180
--Select * FROM RFOperations.Hybris.ProductCategory WHERE productID=8796143648769
--SELECT * FROM RFOperations.Hybris.ProductCategory WHERE productID=180

--SELECT * FROM RFOperations.Hybris.ProductPrice WHERE ProductId=180
--SELECT * FROM RFOperations.Hybris.ProductPrice WHERE ProductId=8796143648769


--SELECT * FROM RFOperations.Hybris.Category

--Get the List of Products need to delete.

--This count should be ZERO.
SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductsRF pb
        JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID AND CountryId = 236
WHERE   CountryId = 236
        AND pb.ChangedByApplication = 'Hybris'
        AND LEN(pb.productID) > 5


--Delete from ProductReplaced .
SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductReplaced pr
        JOIN RFOperations.Hybris.ProductsRF pb ON pb.productID = pr.ProductID AND LEN(pb.productID) > 5
		JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID AND CountryId = 236

SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductPrice pr
         JOIN RFOperations.Hybris.ProductsRF pb ON pb.productID = pr.ProductID AND LEN(pb.productID) > 5
		JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID AND CountryId = 236


--Delete from  [Hybris].[ProductIngredients]

SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductIngredients pr
         JOIN RFOperations.Hybris.ProductsRF pb ON pb.productID = pr.ProductID AND LEN(pb.productID) > 5
		JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID AND CountryId = 236


--Delete from  [Hybris].[ProductCategory]

SELECT  *
FROM    RFOperations.Hybris.ProductCategory pr
         JOIN RFOperations.Hybris.ProductsRF pb ON pb.productID = pr.ProductID AND LEN(pb.productID) > 5
		JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID AND CountryId = 236


SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductCountry pr
         JOIN RFOperations.Hybris.ProductsRF pb ON pb.productID = pr.ProductID AND LEN(pb.productID) > 5
		where CountryId = 236



--Delete from [Hybris].[ProductBase]

SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductBase pr
           JOIN RFOperations.Hybris.ProductsRF pb ON pb.productID = pr.ProductID AND LEN(pb.productID) > 5
		  JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID AND CountryId = 236


SELECT  COUNT(*)
FROM    RFOperations.Hybris.ProductsRF pr 
JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pr.productID AND CountryId = 236
WHERE LEN(pr.productID) > 5 

