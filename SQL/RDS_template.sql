CREATE SCHEMA IF NOT EXISTS projectzero;
DROP TABLE IF EXISTS projectzero.t_u_i;
DROP TABLE IF EXISTS projectzero.backorders;
DROP TABLE IF EXISTS projectzero.cart;
DROP TABLE IF EXISTS projectzero.items;
DROP TABLE IF EXISTS projectzero.transactions;
DROP TABLE IF EXISTS projectzero.manufacturers;

DROP TABLE IF EXISTS projectzero.users ;
CREATE TABLE projectzero.users (
	uid BIGSERIAL PRIMARY KEY,
	fname VARCHAR (72),
	lname VARCHAR (72),
	uname VARCHAR (216) UNIQUE NOT NULL,
	pswrd VARCHAR (216) NOT NULL,
	email VARCHAR (108) CHECK (email LIKE '%@%.___'),
	phonenum VARCHAR (36) CHECK (phonenum LIKE '%_%(___)%___-____%' OR phonenum LIKE '%___-___-____%' OR phonenum LIKE '%__________%' OR phonenum LIKE '___'),
	address VARCHAR (216),
	city VARCHAR (108),
	state VARCHAR (64),
	zip VARCHAR (10) CHECK (zip LIKE '_____-____' OR zip LIKE '_____'),
	accesslevel VARCHAR (8) NOT NULL CHECK (accesslevel IN ('Admin', 'Employee', 'Customer')),
	sid BIGINT UNIQUE
);

DROP TABLE IF EXISTS projectzero.manufacturers;

CREATE TABLE projectzero.manufacturers (
	mid BIGSERIAL PRIMARY KEY,
	mname VARCHAR (108) NOT NULL,
	address VARCHAR (216) NOT NULL,
	city VARCHAR (108) NOT NULL,
	state VARCHAR (64) NOT NULL,
	zip VARCHAR (10) NOT NULL CHECK (zip LIKE '_____-____' OR zip LIKE '_____'),
	email VARCHAR (108) CHECK (email LIKE '%@%.___'),
	phonenum VARCHAR (36) NOT NULL CHECK (phonenum LIKE '%_%(___)%___-____%' OR phonenum LIKE '%___-___-____%' OR phonenum LIKE '%__________%' OR phonenum LIKE '___%'),
	representative VARCHAR (216)
);

DROP TABLE IF EXISTS projectzero.transactions;

CREATE TABLE projectzero.transactions (
	tid BIGSERIAL PRIMARY KEY,
	uid BIGINT NOT NULL REFERENCES projectzero.users (uid),
	totalcost DOUBLE PRECISION NOT NULL CHECK (totalcost > 0.00),
	stamp VARCHAR (108) NOT NULL DEFAULT CAST( CURRENT_TIMESTAMP AS VARCHAR)
);

DROP TABLE IF EXISTS projectzero.items;

CREATE TABLE projectzero.items (
	iid BIGSERIAL PRIMARY KEY,
	unitname VARCHAR (108) NOT NULL,
	description VARCHAR, -- identical to text(unlimited length) in postgresql
	quantity BIGINT NOT NULL CHECK (quantity >= 0) DEFAULT 0,
	sellingprice DOUBLE PRECISION NOT NULL CHECK (sellingprice > 0.00),
	totalpurchases BIGINT CHECK (totalpurchases >= 0) DEFAULT 0,
	dateadded DATE NOT NULL DEFAULT CURRENT_DATE,
	unitcost DOUBLE PRECISION NOT NULL CHECK (unitcost > 0.00),
	mid BIGINT REFERENCES projectzero.manufacturers (mid)
);

DROP TABLE IF EXISTS projectzero.coupons;

CREATE TABLE projectzero.coupons (
	cid BIGSERIAL,
	cname VARCHAR (108),
	description VARCHAR (218),
	discount DOUBLE PRECISION CHECK (discount >= 0.00) DEFAULT 0,
	percentage INT CHECK (percentage >= 0 AND percentage < 100) DEFAULT 0
);


DROP TABLE IF EXISTS projectzero.t_u_i;

CREATE TABLE projectzero.t_u_i (
	tid BIGINT NOT NULL REFERENCES projectzero.transactions (tid),
	iid BIGINT NOT NULL REFERENCES projectzero.items (iid),
	quantity BIGINT NOT NULL CHECK (quantity > 0) DEFAULT 1,
	cid BIGINT DEFAULT 0
);

DROP TABLE IF EXISTS projectzero.cart;

CREATE TABLE projectzero.cart (
	uid BIGINT NOT NULL REFERENCES projectzero.users (uid),
	iid BIGINT NOT NULL REFERENCES projectzero.items (iid),
	quantity BIGINT NOT NULL CHECK (quantity > 0) DEFAULT 1,
	cid BIGINT DEFAULT 0
);

DROP TABLE IF EXISTS projectzero.backorders;

CREATE TABLE projectzero.backorders (
	uid BIGINT NOT NULL REFERENCES projectzero.users (uid),
	iid BIGINT NOT NULL REFERENCES projectzero.items (iid),
	quantity BIGINT NOT NULL CHECK (quantity > 0) DEFAULT 1,
	cid BIGINT DEFAULT 0
);