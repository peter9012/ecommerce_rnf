SELECT COUNT(*) -- Count should be ZERO.
FROM    RFOperations.Hybris.ProductsRF pb
        JOIN RFOperations.Hybris.ProductCountry pc ON pc.ProductID = pb.productID
WHERE   CountryId = 236
        AND pb.ChangedByApplication = 'Hybris'
        AND LEN(pb.productID) > 5

SELECT COUNT(*) -- Count should be ZERO.
FROM
RFOperations.Hybris.ProductReplaced pb JOIN RFOperations.Hybris.ProductCountry pc ON pb.productid=pc.productid
WHERE LEN(pc.productID) > 5 AND CountryId=236 and pb.ChangedByApplication = 'Hybris'
--All products should have productId length less than or equal to 5.

SELECT COUNT(*) -- Count should be ZERO.
FROM
RFOperations.Hybris.ProductPrice pb JOIN RFOperations.Hybris.ProductCountry pc ON pb.productid=pc.productid
WHERE LEN(pc.productID) > 5 AND CountryId=236 and pb.ChangedByApplication = 'Hybris'


SELECT COUNT(*) -- Count should be ZERO.
FROM
RFOperations.Hybris.ProductIngredients pb JOIN RFOperations.Hybris.ProductCountry pc ON pb.productid=pc.productid
WHERE LEN(pc.productID) > 5 AND CountryId=236 and pb.ChangedByApplication = 'Hybris'


SELECT COUNT(*) -- Count should be ZERO.
FROM
RFOperations.Hybris.ProductCategory pb JOIN RFOperations.Hybris.ProductCountry pc ON pb.productid=pc.productid
WHERE LEN(pc.productID) > 5 AND CountryId=236 and pb.ChangedByApplication = 'Hybris'



SELECT COUNT(*) -- Count should be ZERO.
FROM
RFOperations.Hybris.ProductCountry pb JOIN RFOperations.Hybris.ProductCountry pc ON pb.productid=pc.productid
WHERE LEN(pc.productID) > 5 AND CountryId=236 and pb.ChangedByApplication = 'Hybris'


SELECT COUNT(*) -- Count should be ZERO.
FROM
RFOperations.Hybris.ProductBase pb JOIN RFOperations.Hybris.ProductCountry pc ON pb.productid=pc.productid
WHERE LEN(pc.productID) > 5 AND CountryId=236 and pb.ChangedByApplication = 'Hybris'


