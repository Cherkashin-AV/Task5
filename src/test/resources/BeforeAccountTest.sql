insert into "Task5".tpp_ref_product_class (value) values('class');
insert into "Task5".tpp_product (product_code_id, type, number)
	select internal_id, 'type', 'number' from "Task5".tpp_ref_product_class where value='class';
insert into "Task5".tpp_ref_product_register_type(value, product_class_code) values('type', 'class');
insert into "Task5".tpp_ref_product_register_type(value) values('type1');
insert into "Task5".tpp_product_register(product_id, type)
	select id, 'type' from "Task5".tpp_product where type='type' and number='number';
insert into "Task5".account_pool
	(branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode, accounts)
	values
		('0022','800','15','00','type','{"accounts":["475335516415314841861", "4753321651354151", "4753352543276345"]}');