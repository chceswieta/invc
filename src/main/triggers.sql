USE invc;

DROP TRIGGER IF EXISTS RightAgeTrigger;
DROP TRIGGER IF EXISTS DestroyLeftoverInvoicesTrigger;
DROP TRIGGER IF EXISTS NumberOfProductInsertTrigger;
DROP TRIGGER IF EXISTS NumberOfProductUpdateTrigger;
DROP TRIGGER IF EXISTS InvoiceFromTodayTrigger;
DROP TRIGGER IF EXISTS NumberOfInvoiceElemntTrigger;

DELIMITER $$

CREATE TRIGGER RightAgeTrigger BEFORE INSERT ON client FOR EACH ROW
BEGIN
	IF(CURDATE() - DATE(new.birth) < 180000) THEN SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Client is definitely too young.';
	END IF;
END $$

CREATE TRIGGER NumberOfProductInsertTrigger BEFORE INSERT ON product FOR EACH ROW
BEGIN
	IF(new.number < 0) THEN SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'You have tried to insert minus number of product';
	END IF;
END $$

CREATE TRIGGER NumberOfProductUpdateTrigger BEFORE UPDATE ON product FOR EACH ROW
BEGIN
	IF(new.number < 0) THEN SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'You have tried to update number of product to minus number. 
From empty and salomon will not pour.';
	END IF;
END $$

CREATE TRIGGER InvoiceFromTodayTrigger BEFORE INSERT ON invoice FOR EACH ROW
BEGIN
	IF(new.date <> CURDATE()) THEN SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'You are trying to create invoice for a day before today. Do not worry, we already called CBA.';  
	END IF;
END $$

CREATE TRIGGER NumberOfInvoiceElemntTrigger BEFORE INSERT ON invoiceElement FOR EACH ROW
BEGIN
	IF((SELECT DISTINCT number FROM product WHERE product.productId = new.productId) < new.number) THEN SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Number of product on invoice is bigger than number avalible in stack';  
	END IF;
END $$
DELIMITER ;
