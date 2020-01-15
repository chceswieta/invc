DROP DATABASE IF EXISTS invc;

CREATE DATABASE invc;
USE invc;
CREATE TABLE client (
    clientId varchar(11) PRIMARY KEY  NOT NULL,
    name varchar(20) NOT NULL,
    surname varchar(20) NOT NULL,
    birth date NOT NULL
);
CREATE TABLE invoice (
    invoiceId int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    clientId varchar(11) NOT NULL,
    date date NOT NULL,
    total DECIMAL(6,2) NOT NULL
);
CREATE TABLE invoiceElement (
	invoiceId INT NOT NULL, 
	productId INT NOT NULL,
	number INT NOT NULL
);
CREATE TABLE product (
	productId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	productName VARCHAR(20) NOT NULL,
	number INT NOT NULL,
	price DECIMAL(6,2)	
);

ALTER TABLE invoice
ADD FOREIGN KEY (clientId) REFERENCES client(clientId) ON DELETE CASCADE;
ALTER TABLE invoiceElement
ADD FOREIGN KEY (invoiceId) REFERENCES invoice(invoiceId) ON DELETE CASCADE;
ALTER TABLE invoiceElement
ADD FOREIGN KEY (productId) REFERENCES product(productId);

#CREATE USER 'invClient'@'localhost' IDENTIFIED BY 'cpassword';
#CREATE USER 'invEmployee'@'localhost' IDENTIFIED BY 'epassword';
#CREATE USER 'invAdmin'@'localhost' IDENTIFIED BY 'apassword';

#GRANT SELECT ON invc.invoiceElement TO 'invClient'@'localhost';
#grant procedure

#GRANT SELECT ON invc.client TO 'invEmployee'@'localhost';
#GRANT SELECT, INSERT, DELETE ON invc.invoice TO 'invEmployee'@'localhost';
#GRANT SELECT, INSERT, DELETE ON invc.invoiceElement TO 'invEmployee'@'localhost';
#GRANT SELECT, UPDATE ON invc.product TO 'invEmployee'@'localhost';

#GRANT SELECT, INSERT, DELETE ON invc.invoice TO 'invAdmin'@'localhost';
#GRANT SELECT, INSERT, DELETE ON invc.invoiceElement TO 'invAdmin'@'localhost';
#GRANT SELECT, INSERT, DELETE, UPDATE ON invc.client TO 'invAdmin'@'localhost';
#GRANT SELECT, INSERT, DELETE, UPDATE ON invc.product TO 'invAdmin'@'localhost';

INSERT INTO client VALUE ('1','Gabi','Wechta','1999-10-10');
INSERT INTO invoice VALUE (1,'1',CURDATE(),0);
INSERT INTO product VALUE (1,'piwo',100,'6.51');
INSERT INTO invoiceElement VALUE (1,1,3);

###################################TRIGGERS###################################

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
From empty and Salomon will not pour.';
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

###################################PROCEDURES###################################


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

