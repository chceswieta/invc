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

#triggery
