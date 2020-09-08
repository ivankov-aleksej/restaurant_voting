INSERT INTO USERS (EMAIL, PASSWORD)
VALUES ('admin@gmail.com', 'admin'),
       ('user1@gmail.com', 'password'),
       ('user2@gmail.com', 'password');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 1),
       ('USER', 2),
       ('USER', 3);

INSERT INTO RESTAURANT(NAME)
VALUES ('McDonalds'),
       ('Burger King');

INSERT INTO MENU(ID, RESTAURANT_ID, ACTION_DATE)
VALUES (1, 1, '2020-08-01'),
       (2, 2, '2020-08-01'),
       (3, 1, '2020-08-02'),
       (4, 2, '2020-08-02');

INSERT INTO DISH(NAME, PRICE, MENU_ID)
VALUES ('Гамбургер', 1.04, 1),
       ('Ролл з креветками', 1.54, 1),
       ('Роял Фреш', 1.14, 2),
       ('Чикен Ролл', 1.87, 2),
       ('Гамбургер', 1.14, 3),
       ('Ролл з креветками', 1.64, 3),
       ('Роял Фреш', 1.24, 4),
       ('Чикен Ролл', 1.77, 4);

INSERT INTO VOTE(MENU_ID, USER_ID, DATE)
VALUES (1, 1, '2020-08-01'),
       (2, 2, '2020-08-01'),
       (2, 3, '2020-08-01'),
       (3, 1, '2020-08-02'),
       (3, 2, '2020-08-02'),
       (4, 3, '2020-08-02');

