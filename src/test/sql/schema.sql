
  create table `company-test` (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    constraint `pk_company-test` primary key (id))
  ;

  create table `computer-test` (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    introduced                timestamp NULL,
    discontinued              timestamp NULL,
    company_id                bigint default NULL,
    constraint `pk_computer-test` primary key (id))
  ;

alter table `computer-test` add constraint `fk_computer-test_company-test_1` foreign key (company_id) references `company-test` (id) on delete restrict on update restrict;
create index `ix_computer-test_company-test_1` on `computer-test` (company_id);