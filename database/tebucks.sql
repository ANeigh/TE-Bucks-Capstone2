BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfer CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20) DEFAULT 'User',
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
	--CONSTRAINT amount_minimum CHECK (amount > 0.00)
);



--ROLLBACK;
COMMIT TRANSACTION;
