-- Active: 1731302761888@@127.0.0.1@3306@bus
CREATE TABLE signup (
    id INT AUTO_INCREMENT PRIMARY KEY,     -- Unique ID for each user
    username VARCHAR(50) NOT NULL UNIQUE,  -- User's chosen username
    email VARCHAR(100) NOT NULL UNIQUE,    -- User's email address
    password VARCHAR(255) NOT NULL        -- Hashed password
);

CREATE TABLE login (
    login_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for each login entry
    email VARCHAR(100) NOT NULL,             -- Email used for login
    password VARCHAR(255) NOT NULL,          -- Password provided for login
    FOREIGN KEY (email) REFERENCES signup(email) -- Links to the `signup` table
);
select * from login;

SELECT 
    signup.id AS user_id,
    signup.username,
    signup.email,
    signup.password AS signup_password,
    login.login_id,
    login.password AS login_password
FROM 
    signup
LEFT JOIN 
    login
ON 
    signup.email = login.email;

CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique ID for each route
    route_name VARCHAR(100) NOT NULL,         -- Descriptive name for the route
    start_point VARCHAR(100) NOT NULL,        -- Starting location of the route
    end_point VARCHAR(100) NOT NULL,          -- Ending location of the route
    distance_km DECIMAL(10, 2) NOT NULL,      -- Distance of the route in kilometers
    estimated_time TIME NOT NULL,             -- Estimated travel time
    fare DECIMAL(10, 2) NOT NULL              -- Fare for the route
);
SELECT* FROM routes;


CREATE TABLE booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,    -- Unique ID for each booking
    user_id INT NOT NULL,                         -- ID of the user making the booking
    route_id INT NOT NULL,                        -- ID of the route for the booking
    booking_date DATE NOT NULL,                   -- Date of the booking
    travel_date DATE NOT NULL,                    -- Date of travel
    number_of_seats INT NOT NULL,                 -- Number of seats booked
    total_fare DECIMAL(10, 2) NOT NULL,           -- Total fare for the booking
    status ENUM('Booked', 'Cancelled') DEFAULT 'Booked', -- Status of the booking
    FOREIGN KEY (user_id) REFERENCES signup(id),  -- Links to the `signup` table
    FOREIGN KEY (route_id) REFERENCES routes(route_id)   -- Links to the `routes` table
);


SELECT 
    booking.booking_id,
    booking.user_id,
    booking.booking_date,
    booking.travel_date,
    booking.number_of_seats,
    booking.total_fare,
    booking.status,
    routes.route_name,
    routes.start_point,
    routes.end_point,
    routes.distance_km,
    routes.estimated_time,
    routes.fare
FROM 
    booking
JOIN 
    routes 
ON 
    booking.route_id = routes.route_id;

ALTER TABLE routes 
ADD COLUMN seats_available INT NOT NULL DEFAULT 40;  -- Assume 40 seats available by default

START TRANSACTION;  -- Start a transaction to ensure atomicity

-- Update the available seats for the route
UPDATE routes
JOIN booking ON routes.route_id = booking.route_id
SET routes.seats_available = routes.seats_available - booking.number_of_seats
WHERE booking.booking_id = booking_id;  -- Specify the booking_id of the booking to update

SELECT * FROM routes;