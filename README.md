# Toll Parking Library

Server created with SpringBoot, Lombok, and maven

## Build instructions  

### Prerequisites

- Maven3
- JDK 11+

**Building**

`mvn clean install`

**Running the server**

`mvn spring-boot:run`

or

`java -jar target/parkingtoll-0.0.1-SNAPSHOT.jar`

or 

`docker build -t fcheret/parkingtoll .`

`docker run -p 8080:8080 fcheret/parkingtoll`

The server application will spawn on http://localhost:8080

### Used technologies

- *Spring Boot*
- *Project Lombok* 

For tests:
- junit
- JaCoCo
- Mockito 



###Open Endpoints

Endpoints for viewing and manipulating the Parking Lots and park cars.

 - [Show Accessible Parking Lots](docs/get.md) : `GET /parking_lot`
 
 - [Create Parking Lot](docs/post.md) : `POST /parking_lot`
 - [Delete a Parking Lot](docs/delete.md) : `DELETE /parking_lot/{parkingLotId}`
 - [Display a Parking Lot](docs/getid.md) : `GET /parking_lot/{parkingLotId}`
 - [Update a Parking Lot](docs/putid.md) : `PUT /parking_lot/{parkingLotId}`
 - [Park a car in a Parking Lot](docs/postpark.md) : `POST /parking_lot​/{parkingLotId}​/park`
 - [Remove a car from a Parking Lot and returns the amount to pay](docs/delpark.md) : `DELETE /parking_lot​/{parkingLotId}​/park`


### Future Improvements
- relying on an actual persistent DAO
- implement different pricing policies

### Unit tests and code coverage

`mvn clean test`

100% classes, 100% lines covered in package `eu.fibane.parkingtoll`

JaCoCo report in `target/site/` folder