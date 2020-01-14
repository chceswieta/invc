DROP PROCEDURE IF EXISTS addInvoiceElement;

DELIMITER $$

CREATE PROCEDURE addInvoiceElement(IN invoiceId INT, IN productId INT, IN number INT)
BEGIN		
	SET autocommit = 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	    BEGIN
		ROLLBACK;
		EXIT PROCEDURE;
	    END;

	START TRANSACTION;
	
	INSERT INTO invoiceElement VALUE (invoiceId, productId, number);
	UPDATE
	COMMIT;
