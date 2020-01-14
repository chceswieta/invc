USE invc;

#DROP PROCEDURE IF EXISTS generateInvoiceHeadline;
DROP PROCEDURE IF EXISTS gen;

DELIMITER $$

#CREATE PROCEDURE generateInvoiceHeadline(IN invoiceId INT)
CREATE PROCEDURE gen(IN invoiceId INT)
BEGIN		
	SELECT CONCAT(clientId, ' ', invoiceId, ' ', date, ' ', total) FROM invoice WHERE invoice.invoiceId = invoiceId;
END $$

DELIMITER ;
