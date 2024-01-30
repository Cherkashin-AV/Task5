CREATE TABLE tpp_ref_account_type (
	internal_id 				BIGSERIAL PRIMARY KEY,
	value						VARCHAR UNIQUE
);
insert into tpp_ref_account_type(value)
	values ('Клиентский'), ('Внутрибанковский');

CREATE TABLE tpp_ref_product_class (
	internal_id 				BIGSERIAL PRIMARY KEY,
	value						VARCHAR UNIQUE,
	gbl_code					VARCHAR,
	gbl_name					VARCHAR,
	product_row_code			VARCHAR,
	product_row_name			VARCHAR,
	subclass_code				VARCHAR,
	subclass_name				VARCHAR
);
insert into tpp_ref_product_class(value, gbl_code, gbl_name, product_row_code, product_row_name, subclass_code, subclass_name)
	values
		('03.012.002', '03', 'Розничный бизнес', '012', 'Драг. металлы', '002', 'Хранение'),
		('02.001.005', '02', 'Розничный бизнес', '001', 'Сырье', '005', 'Продажа');

CREATE TABLE tpp_ref_product_register_type (
	internal_id 				BIGSERIAL PRIMARY KEY,
	value						VARCHAR unique,
	register_type_name			VARCHAR,
	product_class_code			VARCHAR references tpp_ref_product_class(value),
	account_type				VARCHAR references tpp_ref_account_type(value)
);

insert into tpp_ref_product_register_type(value, register_type_name, product_class_code, account_type)
	values('03.012.002_47533_ComSoLd', 'Хранение ДМ.', '03.012.002', 'Клиентский'),
		('02.001.005_45343_CoDowFF', 'Серебро. Выкуп.', '02.001.005', 'Клиентский');

CREATE TABLE tpp_product_register (
	id 						BIGSERIAL PRIMARY KEY,
	product_id				BIGINT,
	type					VARCHAR references tpp_ref_product_register_type(value),
	account_id				BIGINT,
	currency_code			VARCHAR,
	state					VARCHAR,
	account_number          VARCHAR
);

create table agreements(
	id 						BIGINT,
	agreement_id			BIGINT unique,
	primary key (id, agreement_id)
);
CREATE INDEX agreements_id ON agreements (id);

CREATE TABLE tpp_product (
	id 						BIGSERIAL PRIMARY KEY,
	agreement_id			BIGINT,
	product_code_id			BIGINT references tpp_ref_product_class(internal_id),
	client_id				BIGINT,
	type					VARCHAR,
	number					VARCHAR,
	priority				INTEGER,
	date_of_conclusion		DATE,
	start_date_time			DATE,
	end_date_time			DATE,
	days					INTEGER,
	penalty_rate			REAL,
	nso						FLOAT,
	threshold_amount		FLOAT,
	requisite_type			VARCHAR,
	interest_rate_type		VARCHAR,
	tax_rate				REAL,
	reason_close			VARCHAR,
	state					VARCHAR
	);

alter table tpp_product_register add constraint fk_tpp_product_id foreign key (product_id) references tpp_product(id);
alter table agreements add constraint fk_agreements_id foreign key (id) references tpp_product(id);
alter table agreements add constraint fk_agreements_agreement_id foreign key (agreement_id) references tpp_product(id);

create table account_pool(
	id					BIGSERIAL,
    branchCode          VARCHAR not null,
    currencyCode        VARCHAR not null,
    mdmCode             VARCHAR not null,
    priorityCode        VARCHAR not null,
    registryTypeCode    VARCHAR not null,
    accounts            JSONB

);

create unique index idx_account_pool_uniq on account_pool (branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode);

insert into account_pool
	(branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode, accounts)
	values
		('0022','800','15','00','03.012.002_47533_ComSoLd','{"accounts":["475335516415314841861", "4753321651354151", "4753352543276345"]}'),
		('0021','500','13','00','02.001.005_45343_CoDowFF','{"accounts":["453432352436453276", "45343221651354151", "4534352543276345"]}');
