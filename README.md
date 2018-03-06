# Hunger-application
The project includes three modules: an android app, a website, a desktop java app for an abstract food delivery service,
aimed at customers, couriers, stock keepers respectively.
<br/>
The source code of the Android app can be found in the "master" branch.
The app provides customers with functionalities of browsing the list of available dishes, of forming their shopping carts,
and of filling in of orders' details. The application employs such APIs as Apache HttpClient for Android to send HTTP GET and POST
requests to the server-side PHP scripts (can be found in the "php" branch) which interact with the project database (MySQL) 
and receive results in JSON, Google Play Services to determine a customer's current location, Picasso to load images into views
from the web.
<br/>
The source code of the simplest possible website for couriers is located in the "php" branch.
The website allows the people who manage the delivery to browse relevant orders and confirm receipts.
Its interaction with the project database is ensured due to PHP mediation via jQuery AJAX.
<br/>
The branch "provision-app" contains the source code of the desktop java application for stock resources management.
The app is based on JavaFX library, and is actually a CRUD, communicating with the database by means of JDBC.
