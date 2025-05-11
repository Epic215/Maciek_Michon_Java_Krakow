# Promotions for payment methods
## Information about project
Repository contains a solution for Ocado Technology Internship 2025.
This application parses two JSON files to optimize the payment method for each order with the given conditions.
## Technologies
- Java 21
- Maven
## How to run project
**1. Clone the repository:**
```bash
git clone 
```
**2. Move to directory:**
```bash
cd 
```
**3. Run command:**
```bash
mvn clean package
```
**4. Run command:**
```bash
java -jar target/app.jar src/main/resources/orders.json src/main/resources/paymentmethods.json
```
