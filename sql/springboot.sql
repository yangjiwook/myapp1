select * FROM PRODUCT;
delete from product;
commit;

--등록
insert into product values(product_product_id_seq.nextval,'컴퓨터',10,1000000);

insert into product(product_id,pname,quantity,price)values(product_product_id_seq.nextval,'모니터',20,900000);
rollback;