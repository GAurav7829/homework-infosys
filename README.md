# homework-infosys

## Assignment Description
A retailer offers a rewards program to its customer, awarding points based on each recorded purchase. Customer receive 2 points on every burchase over $100, plus 1 point on every purchase between $50 and $100 for every transaction.

> Flow to start services
- start api-service
- start retailer-service
- start customer-service

## Eureka Server
> API gateway for Customer and Retailer Service
- http://localhost:8761/

## retailer - product - endpoints
> Endpoints screenshots
- All endpoints for Retailer service
![screenshot](images/productServices.jpg) 
- To add new product
![screenshot](images/addNewProduct.jpg)
- To get all products list
![screenshot](images/getAllProducts.jpg)
- To fetch product by product ID
![screenshot](images/getProductById.jpg)
- To reduce Quantity when customer purchase the product, used as proxy in customer service
![screenshot](images/reduceQuantity.jpg)
- to open swagger url on port 8001, below is the link \
http://localhost:8001/swagger-ui/index.html

## customer
> Endpoints screenshots
- All endpoints for customer service
![screenshot](images/customerService.jpg)
- To fetch all Customers
![screenshot](images/findAllCustomer.jpg)
- To show all products, used proxy to fetch all products from retailer service
![screenshot](images/showAllProducts.jpg)
- To add new customer
![screenshot](images/resisterCustomer.jpg)
- To buy product
![screenshot](images/buyProduct.jpg)
- show products transaction by month
![screenshot](images/showProductByMonth.jpg)
- show last 3 months transaction
![screenshot](images/last3MonthsTransaction.jpg)
- to open swagger url on port 8002, below is the link \
http://localhost:8002/swagger-ui/index.html
