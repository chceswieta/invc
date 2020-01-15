USE invc;

DROP PROCEDURE IF EXISTS gen;
DROP PROCEDURE IF EXISTS addie;

DELIMITER $$

CREATE PROCEDURE gen(IN invoiceId INT)
BEGIN		
	SELECT CONCAT(clientId, ' ', invoiceId, ' ', date, ' ', total) FROM invoice WHERE invoice.invoiceId = invoiceId;
END $$

CREATE PROCEDURE addie(IN invoiceId INT, IN productId INT, IN number INT)
BEGIN		
	DECLARE howManyInStack INT UNSIGNED;
	DECLARE oldInvoiceTotal, productPrice DECIMAL(6,2);
	DECLARE _rollback BOOL DEFAULT 0;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;
	SET autocommit = 0;

	START TRANSACTION;

		SET howManyInStack = (SELECT product.number FROM product WHERE product.productId = productId);
		SET oldInvoiceTotal = (SELECT invoice.total FROM invoice WHERE invoice.invoiceId = invoiceId);
		SET productPrice = (SELECT product.price FROM product WHERE product.productId = productId);

		UPDATE product SET product.number = howManyInStack - number WHERE product.productId = productId;
		UPDATE invoice SET invoice.total = oldInvoiceTotal + number * productPrice WHERE invoice.invoiceId = invoiceId;

		INSERT INTO invoiceElement VALUE (invoiceId, productId, number);

		IF _rollback THEN
        		ROLLBACK;
    		ELSE
      			COMMIT;
		END IF;
END $$

DELIMITER ;
