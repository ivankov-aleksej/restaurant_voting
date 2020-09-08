INSERT INTO USERS (ID, EMAIL, PASSWORD)
VALUES (100, 'admin@gmail.com', 'admin'),
       (101, 'user1@gmail.com', 'password'),
       (102, 'user2@gmail.com', 'password');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('ROLE_USER', 100),
       ('ROLE_ADMIN', 100),
       ('ROLE_USER', 101),
       ('ROLE_USER', 102);

INSERT INTO RESTAURANT("ID", "NAME")
VALUES (103, 'McDonalds'),
       (104, 'Burger King');

INSERT INTO MENU(ID, RESTAURANT_ID, ACTION_DATE)
VALUES (105, 103, '2020-08-01'),
       (106, 104, '2020-08-01'),
       (107, 103, '2020-08-02'),
       (108, 104, '2020-08-02');

INSERT INTO DISH(ID, NAME, PRICE, MENU_ID)
VALUES (109, 'Гамбургер', 1.04, 105),
       (110, 'Ролл з креветками', 1.54, 105),
       (111, 'Роял Фреш', 1.14, 106),
       (112, 'Чикен Ролл', 1.87, 106),
       (113, 'Гамбургер', 1.14, 107),
       (114, 'Ролл з креветками', 1.64, 107),
       (115, 'Роял Фреш', 1.24, 108),
       (116, 'Чикен Ролл', 1.77, 108);

INSERT INTO VOTE(ID, MENU_ID, USER_ID, DATE)
VALUES (117, 105, 100, '2020-08-01'),
       (118, 106, 101, '2020-08-01'),
       (119, 106, 102, '2020-08-01'),
       (120, 107, 100, '2020-08-02'),
       (121, 107, 101, '2020-08-02'),
       (122, 108, 102, '2020-08-02');

