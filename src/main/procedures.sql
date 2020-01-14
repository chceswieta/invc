USE invc;

DROP PROCEDURE IF EXISTS generateInvoiceHeadline;

DELIMITER $$

CREATE PROCEDURE generateInvoiceHeadline(IN invoiceId INT)
BEGIN		
	SELECT CONCAT('invcId:', invoiceId, ' ,' FROM invoice WHERE invoice.invoiceId = invoiceId;
END $$

DELIMITER ;
