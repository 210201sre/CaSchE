-- Generate Manufacturers --
INSERT INTO projectzero.manufacturers (mname, address, city, state, zip, phonenum) VALUES ('trossen','123 straight street','Chicago','IL','12345','666-777-2222');
INSERT INTO projectzero.manufacturers (mname, address, city, state, zip, phonenum) VALUES ('foxconn','456 circle drive','Dallas','TX','55555-4444','1 (630) 451-1454');


-- Generate Users --
INSERT INTO projectzero.users (uname, pswrd, accesslevel) VALUES ('password','password','Admin');
INSERT INTO projectzero.users (uname, pswrd, accesslevel) VALUES ('buyme','apizza','Employee');
INSERT INTO projectzero.users (uname, pswrd, accesslevel) VALUES ('username','password','Customer');


-- Generate Items --
INSERT INTO projectzero.items (unitname, sellingprice, unitcost, quantity, mid) VALUES ('putty','5.92','3.24',1,1);
INSERT INTO projectzero.items (unitname, sellingprice, unitcost, quantity, mid) VALUES ('purebred rock','47.99','23.84',1,1);
INSERT INTO projectzero.items (unitname, sellingprice, unitcost, quantity, mid) VALUES ('mud', 1.20, 1, 1, 1); 				-- money takes any money formatted value regardless of type 
INSERT INTO projectzero.items (unitname, sellingprice, unitcost, quantity, mid) VALUES ('battery','14.50',' 10.36', 1, 2); 	-- and strips whitespace before & after '$' and at the end
INSERT INTO projectzero.items (unitname, sellingprice, unitcost, quantity, mid) VALUES ('nickel','8.9','          4.44   ', 1, 2);

-- Generate Coupons --
INSERT INTO projectzero.coupons (cname, discount) VALUES ('$10 OFF', 10.00);

-- Generate Transactions --
INSERT INTO projectzero.transactions (uid, totalcost) VALUES (3, 153.78); -- do not total cost may not equal sellingprice * n as taxes and coupons may be applied
INSERT INTO projectzero.transactions (uid, totalcost) VALUES (2, '50.00');
INSERT INTO projectzero.transactions (uid, totalcost) VALUES (1, '1.78');
INSERT INTO projectzero.transactions (uid, totalcost) VALUES (2, '.87');

-- Generate TUI --
	-- Customer Transaction (1) --
INSERT INTO projectzero.t_u_i (tid, iid) VALUES (1, 1);
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (1, 2, 4);
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (1, 4, 1);
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (1, 3, 1);
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (1, 5, 2);
	-- Employee Transaction (2) --
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (2, 5, 1);
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (2, 1, 1);
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (2, 2, 18);
	-- Admin Transaction (3) --
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (3, 3, 3);
	-- Employee Transaction (4) --
INSERT INTO projectzero.t_u_i (tid, iid, quantity) VALUES (4, 4, 1);
