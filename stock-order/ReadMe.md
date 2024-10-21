# For build 
mvn clean install
# Run the app 
mvn spring-boot:run
### Now you have access the endpoints.
# You can find basic authentication's user and password info for Admin role at SecurityConfig file
# Don't forget,we are working with h2 db which is in-memory db.
# Thus, Even if you create the data for the tables by using curls, if the app is stopped then you will lose the data.
# Need to create the data again, after starting the app. Some example curls below:

# Creating Buy Order 
curl --location 'localhost:8080/order/createOrder' \
--header 'Content-Type: application/json' \
--header 'Authorization: ••••••' \
--header 'Cookie: JSESSIONID=166FCC80BFA32BAD6C7F33831CF76043; XSRF-TOKEN=86881778-6188-4710-b150-9ac3b242ff81' \
--data '{
"customerId": 1,
"assetName": "TRY",
"orderSide": "BUY",
"size": 1,
"price": 50.00
}'


# Creating Sell Order
curl --location 'localhost:8080/order/createOrder' \
--header 'Content-Type: application/json' \
--header 'Authorization: ••••••' \
--header 'Cookie: JSESSIONID=0D718B49A0C693ADC06745796A819008; XSRF-TOKEN=86881778-6188-4710-b150-9ac3b242ff81' \
--data '{
"customerId": 1,
"assetName": "TRY",
"orderSide": "SELL",
"size": 1,
"price": 50.00
}'

# List Orders By Customer And Dates

curl --location 'localhost:8080/order/listOrders/ByCustomerId/1/AndStartDate/2024-10-20 00:59:22/EndDate/2024-10-22 04:59:22' \
--header 'Cookie: JSESSIONID=0D718B49A0C693ADC06745796A819008; XSRF-TOKEN=86881778-6188-4710-b150-9ac3b242ff81'

# Delete Order

curl --location --request DELETE 'localhost:8080/order/deleteByOrderId/1' \
--header 'Authorization: ••••••' \
--header 'Cookie: JSESSIONID=8249CE23549302051D8272243694A25D; XSRF-TOKEN=86881778-6188-4710-b150-9ac3b242ff81'


# Asset table for storing asset of each user in TRY. Orders table for storing orders of each user in TRY.
# Transaction logs of deposit and withdrawn processes at Transactions table. 