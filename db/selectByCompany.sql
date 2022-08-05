CREATE TABLE company
(
    id   integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id         integer NOT NULL,
    name       character varying,
    company_id integer references company (id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company(id, name)
values (1, 'Company1'),
       (2, 'Company2'),
       (3, 'Company3'),
       (4, 'Company4'),
       (5, 'Company5');

insert into person(id, name, company_id)
values (1, 'Ivanov', 1),
       (2, 'Petrov', 2),
       (3, 'Sidorov', 3),
       (4, 'Vasina', 4),
       (5, 'Messi', 5),
       (6, 'Ronaldo', 2),
       (7, 'Zidan', 5);


select p.name as Имя, c.name as Компания
from person as p
         join company as c on p.company_id = c.id
where p.company_id != 5;



select c.name as Компания, count(p.company_id) as Количество_персонала
from person as p
         join company as c on p.company_id = c.id
group by c.name
having count(p.company_id) =
       (
           select count(p.company_id)
           from person as p
           group by p.company_id
           order by count(p.company_id) desc
           limit 1
       );

