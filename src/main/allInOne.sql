DROP DATABASE IF EXISTS invc;

CREATE DATABASE invc;
USE invc;
CREATE TABLE client (
    clientId varchar(11) PRIMARY KEY  NOT NULL,
    name varchar(20) NOT NULL,
    surname varchar(20) NOT NULL,
    birth date NOT NULL,
    username varchar(20)
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

CREATE TRIGGER NumberOfInvoiceElementTriggerI BEFORE INSERT ON invoiceElement FOR EACH ROW
BEGIN
	IF((SELECT DISTINCT number FROM product WHERE product.productId = new.productId) < new.number) THEN SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Number of product on invoice is bigger than number available in stock';
	END IF;
END $$

CREATE TRIGGER NumberOfInvoiceElementTriggerU BEFORE UPDATE ON invoiceElement FOR EACH ROW
BEGIN
    IF((SELECT DISTINCT number FROM product WHERE product.productId = new.productId) < new.number) THEN SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Number of product on invoice is bigger than number available in stock';
    END IF;
END $$

DELIMITER ;

###################################PROCEDURES###################################


DROP PROCEDURE IF EXISTS gen;
DROP PROCEDURE IF EXISTS num;
DROP PROCEDURE IF EXISTS addie;
DROP PROCEDURE IF EXISTS dellie;

DELIMITER $$

CREATE PROCEDURE gen(IN iId INT)
BEGIN		
	SELECT CONCAT(clientId, ' ', invoiceId, ' ', date, ' ', total) FROM invoice WHERE invoice.invoiceId = iId;
END $$

CREATE PROCEDURE num(IN user VARCHAR(20))
BEGIN
    SELECT invoiceId FROM invoice i INNER JOIN client c ON i.clientId = c.clientId WHERE c.username = user ORDER BY invoiceId;
END $$

CREATE PROCEDURE addie(IN iId INT, IN pId INT, IN n INT)
BEGIN		
	DECLARE howManyInStack INT;
	DECLARE oldInvoiceTotal, productPrice DECIMAL(6,2);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;
	SET autocommit = 0;

	START TRANSACTION;

		SET howManyInStack = (SELECT product.number FROM product WHERE product.productId = pId);
		SET oldInvoiceTotal = (SELECT invoice.total FROM invoice WHERE invoice.invoiceId = iId);
		SET productPrice = (SELECT product.price FROM product WHERE product.productId = pId);
        IF ((SELECT COUNT(*) FROM invoiceElement WHERE invoiceId = iId AND productId = pId) = 1) THEN
            UPDATE invoiceElement SET number = number + n WHERE invoiceId = iId AND productId = pId;
        ELSE INSERT INTO invoiceElement VALUE (iId, pId, n);
        END IF;
		UPDATE product SET product.number = howManyInStack - n WHERE product.productId = pId;
		UPDATE invoice SET invoice.total = oldInvoiceTotal + n * productPrice WHERE invoice.invoiceId = iId;

      	COMMIT;
END $$

CREATE PROCEDURE dellie(IN iId INT, IN pId INT)
BEGIN
    DECLARE howManyInStack, n INT;
    DECLARE oldInvoiceTotal, productPrice DECIMAL(6,2);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;
    SET autocommit = 0;

    START TRANSACTION;

    SET n = (SELECT number FROM invoiceElement WHERE invoiceElement.invoiceId = iId AND invoiceElement.productId = pId);
    SET howManyInStack = (SELECT product.number FROM product WHERE product.productId = pId);
    SET oldInvoiceTotal = (SELECT invoice.total FROM invoice WHERE invoice.invoiceId = iId);
    SET productPrice = (SELECT product.price FROM product WHERE product.productId = pId);

    UPDATE product SET product.number = howManyInStack + n WHERE product.productId = pId;
    UPDATE invoice SET invoice.total = oldInvoiceTotal - n * productPrice WHERE invoice.invoiceId = iId;

    DELETE FROM invoiceElement WHERE invoiceId = iId AND productId = pId;

    COMMIT;
END $$

DELIMITER ;


###################################MISC###################################


DROP USER IF EXISTS 'invClient'@'localhost';
DROP USER IF EXISTS 'invEmployee'@'localhost';
DROP USER IF EXISTS 'invAdmin'@'localhost';

CREATE USER 'invClient'@'localhost' IDENTIFIED BY 'cpassword';
CREATE USER 'invEmployee'@'localhost' IDENTIFIED BY 'epassword';
CREATE USER 'invAdmin'@'localhost' IDENTIFIED BY 'apassword';

GRANT SELECT ON invc.invoiceElement TO 'invClient'@'localhost';
GRANT SELECT ON invc.product TO 'invClient'@'localhost';
GRANT EXECUTE ON PROCEDURE gen TO 'invClient'@'localhost';
GRANT EXECUTE ON PROCEDURE num TO 'invClient'@'localhost';

GRANT SELECT ON invc.client TO 'invEmployee'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON invc.invoice TO 'invEmployee'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON invc.invoiceElement TO 'invEmployee'@'localhost';
GRANT SELECT, UPDATE ON invc.product TO 'invEmployee'@'localhost';
GRANT EXECUTE ON invc.* TO 'invEmployee'@'localhost';

GRANT SELECT, LOCK TABLES ON invc.* TO 'invAdmin'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON invc.invoice TO 'invAdmin'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON invc.invoiceElement TO 'invAdmin'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON invc.client TO 'invAdmin'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON invc.product TO 'invAdmin'@'localhost';
GRANT EXECUTE ON invc.* TO 'invAdmin'@'localhost';

INSERT INTO client VALUE ('9910101234','Gabi','Wechta','1999-10-10', 'invClient');
INSERT INTO client VALUE ('9910101235','Patryk','Majewski','1999-10-10', null);
INSERT INTO invoice VALUE (1,'9910101234',CURDATE(),0);
INSERT INTO invoice VALUE (2,'9910101235',CURDATE(),0);
INSERT INTO product VALUE (1,'piwo',100,'6.51');
INSERT INTO product VALUE (2,'mleko',100,'2.30');
CALL addie(1,1,3);
CALL addie(2,2,1);

