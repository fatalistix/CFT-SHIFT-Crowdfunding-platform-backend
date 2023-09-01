create keyspace if not exists balashov with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

create table balashov.test (
    key text primary key,
    value text
) with comment = 'test table';

insert into balashov.test (key, value) VALUES ('TEST STRING', 'TEST VALUE');
update balashov.test set value = 'NEW TEST VALUE' where key = 'TEST STRING' if exists;

delete from balashov.test where key = 'TEST STRING'