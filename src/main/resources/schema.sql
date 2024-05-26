DROP TABLE IF EXISTS bike_site;
CREATE TABLE bike_site(
   id serial PRIMARY KEY,
   sno VARCHAR (500),
   sna VARCHAR (500),
   sarea VARCHAR (500),
   mday VARCHAR (500),
   ar VARCHAR (500),
   sareaen VARCHAR (500),
   snaen VARCHAR (500),
   aren VARCHAR (500),
   act VARCHAR (500),
   src_update_time VARCHAR (500),
   update_time VARCHAR (500),
   info_time VARCHAR (500),
   info_date VARCHAR (500),
   total numeric (10,0),
   available_rent_bikes numeric (10,0),
   latitude numeric (10,7),
   longitude numeric (10,7),
   available_return_bikes numeric (10,0)
);