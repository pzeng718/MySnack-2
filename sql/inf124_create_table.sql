create database if not exists mysnack;
use mysnack;
create table if not exists products (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    qty INTEGER NOT NULL,
    description VARCHAR(200) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

create table if not exists images(
    product_id INTEGER NOT NULL,
    imgSrc VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
	user_id INT NOT NULL AUTO_INCREMENT,
    user_email VARCHAR (50) NOT NULL,
    user_firstname VARCHAR (50) NOT NULL,
    user_lastname VARCHAR (50) NOT NULL,
    PRIMARY KEY (user_id)
) ;


CREATE TABLE IF NOT EXISTS order_user (
	order_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    created_at DATETIME NOT NULL,
    order_total_price DOUBLE NOT NULL,
    order_shipping_method VARCHAR (50) NOT NULL,
    order_shipping_address VARCHAR (200) NOT NULL,
    PRIMARY KEY (order_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS order_productdetail (
	order_id INT NOT NULL,
    id INT NOT NULL,
    selected_amount INT NOT NULL,
    PRIMARY KEY (order_id, id),
    FOREIGN KEY (order_id) REFERENCES order_user (order_id),
    FOREIGN KEY (id) REFERENCES products (id)
);

insert into products values(1, "potato chip", 5.99, 10, "Yummy chips, but do not eat too much of it", "Kay's");
insert into products values(2, "cashew", 15.99, 5, "Delicious cashew, amazing snack when you wanna eat something", "Good Farm Inc.");
insert into products values(3, "tortilla chip", 3.99, 20, "Who doesn\'t love this tortilla chip with some sauce", "Kay\'s");
insert into products values(4, "rice snack", 2.99, 15, "Rice snack is amazing!", "SnackShock inc.");
insert into products values(5, "popcorn", 2.99, 5, "Wonderful snack when watching a movie!", "SnackOkay limited");
insert into products values(6, "milk", 4.99, 15, "Very healthy, good source for calcium", "Good Farm Inc.");
insert into products values(7, "dove", 5.99, 10, "Sweet snack, sweet day", "SweetC");
insert into products values(8, "cheez-it", 4.99, 10, "Just grab it to go, it is amazing", "SnackShock inc.");
insert into products values(9, "cheetos", 1.99, 20, "Just cannot have enough of this", "SnackOkay limited");
insert into products values(10, "fritos", 1.99, 10, "Gotta love the chips", "Kay\'s");
insert into products values(11, "doritos-L", 4.99, 15, "Oh boy I love this, I can finish a bag in a hour", "Kay\'s");
insert into products values(12, "cookie", 3.99, 25, "This cookie is so delicious, wonderful snack when you need them", "SweetC");

insert into images values(1, "resource/potato_chip.jpg");
insert into images values(1, "resource/potato_chip_2.jpg");
insert into images values(2, "resource/cashew.jpg");
insert into images values(2, "resource/cashew_2.jpg");
insert into images values(3, "resource/tortilla_chip.jpg");
insert into images values(3, "resource/tortilla_chip_2.jpg");
insert into images values(4, "resource/rice_snack.jpg");
insert into images values(4, "resource/rice_snack_2.jpg");
insert into images values(5, "resource/popcorn.jpg");
insert into images values(5, "resource/popcorn_2.jpg");
insert into images values(6, "resource/milk.jpg");
insert into images values(6, "resource/milk_2.jpg");
insert into images values(7, "resource/dove.jpg");
insert into images values(7, "resource/dove_2.jpg");
insert into images values(8, "resource/cheez_it.png");
insert into images values(8, "resource/cheez_it_2.png");
insert into images values(9, "resource/cheetos.jpg");
insert into images values(9, "resource/cheetos_2.jpg");
insert into images values(10, "resource/fritos.jpg");
insert into images values(10, "resource/fritos_2.jpg");
insert into images values(11, "resource/doritos-L.png");
insert into images values(11, "resource/doritos-L_2.png");
insert into images values(12, "resource/cookie.jpg");
insert into images values(12, "resource/cookie_2.jpg");
