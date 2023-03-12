
BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfer CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20) DEFAULT 'USER',
	active boolean DEFAULT true,

	CONSTRAINT pk_users PRIMARY KEY (user_id),
	CONSTRAINT uq_username UNIQUE (username)
);

CREATE TABLE accounts (
	account_id serial NOT NULL,
	user_id int NOT NULL,
	balance money DEFAULT 1000.00,
	active boolean DEFAULT true,

	CONSTRAINT pk_accounts PRIMARY KEY (account_id),
	CONSTRAINT fk_account_users_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE transfer (
	transfer_id serial PRIMARY KEY,
	transfer_date date DEFAULT CURRENT_DATE,
	transfer_time time DEFAULT CURRENT_TIME,
	transfer_type varchar NOT NULL,
	status varchar NOT NULL,
	amount money NOT NULL,
	user_to varchar NOT NULL,
	user_from varchar NOT NULL,

	CONSTRAINT fk_transfer_to_user FOREIGN KEY (user_to) REFERENCES users (username),
	CONSTRAINT fk_transfer_from_user FOREIGN KEY (user_from) REFERENCES users (username)
);

INSERT INTO users (username,password_hash,role) VALUES ('user1','user1','ROLE_USER'); -- 1
INSERT INTO users (username,password_hash,role) VALUES ('user2','user2','ROLE_USER'); -- 2
INSERT INTO users (username,password_hash,role) VALUES ('user3','user3','ROLE_USER'); -- 3

INSERT INTO transfer (transfer_id, transfer_type, status, amount, user_to, user_from) VALUES (DEFAULT, 'Send', 'Approved', 100.00, 'user2', 'user1');
INSERT INTO transfer (transfer_id, transfer_type, status, amount, user_to, user_from) VALUES (DEFAULT, 'Request', 'Pending', 50.00, 'user3', 'user2');
INSERT INTO transfer (transfer_id, transfer_type, status, amount, user_to, user_from) VALUES (DEFAULT, 'Request', 'Rejected', 200.00, 'user2', 'user1');
INSERT INTO transfer (transfer_id, transfer_type, status, amount, user_to, user_from) VALUES (DEFAULT, 'Request', 'Approved', 200.00, 'user3', 'user1');



COMMIT TRANSACTION;
