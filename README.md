# Bio Products Shop

## Overview

BioShop is a monolithic architecture application that uses Spring Framework for BackEnd and Thymeleaf for View Engine.
The application also uses Bootstrap and JQuery.

## Build

1. Build the project

From root dir execute:

`mvn clean install > log-file.log` 
due to the long log outputting into IntelliJ IDEA console the tests can crash. Solution: save logs in log-file.log

## Basic Functionality

    1. Users
       - register user with 3 posible roles - ROOT, ADMIN, USER
       - login/logout
       - view and edit own user profile
       - add or remove users` roles - that functionality is accessible only for the ROOT User
	   - delete profile
    
    2. Products
       - CRUD operations
       - get all products - created using RestController and AJAX request
       - get all promoted products - created using RestController and AJAX request
       - get all products in table format - only for ADMIN Users
       - get all promoted products in table format - only for ADMIN Users
       - add and remove promotion - only for ADMIN Users
    
    3. Shopping Cart
       - add to cart
       - remove from cart
       - buy all added products
    
    The shopping cart is stored in DB and its storage period is 7 days without activity.
    
    4. Orders
       - all user orders are saved in DB. The user can monitor all own orders in table format.
       
    5. Log
       - persist in DB some activities from users - like creating and editing products - used for monitoring
       - every day at 3AM the logs created more than a month ago are deleted.