# Remove a car from a Parking Lot

Remove a car from a Parking Lot, and returns the amount to pay.

**URL** : `/parking_lot/{parkingLotId}/park`

**Method** : `DELETE`

**Parameters**

Name | Description 
--- | --- 
parkingLotId | Parking Lot id where to park  
integer($int64) |


**Data constraints**

`type` should be a non-null length character string

`parking_lot_id` should be an integer

**Data example** all fields must be sent

```json
{
    "slot": 1,
    "parking_lot_id": 5,
    "arrival_time": "2020-03-08T19:12:31.042474300Z",
    "type": "25kW"
}
```



## Success Responses

**Code** : `200 OK`

**Content** : 
```json
{
    "slot": 1,
    "parking_lot_id": 5,
    "arrival_time": "2020-01-01T02:12:31.042474300Z",
    "departure_time": "2020-03-08T05:13:52.297646900Z",
    "type": "25kW",
    "price": 1.5
}
```


## Error Responses

**Condition** : If the type of car is not present in the parking lot  

**Code** : `404 NOT FOUND`

**Content example**
```json
{
    "timestamp": "2020-03-08T05:13:52.297646900Z",
    "status": 404,
    "error": "Not Found",
    "message": "This car is not at the specified location",
    "path": "/parking_lot/0/park"
}
```

**Condition** : If the given ID is not associated with a Parking Lot 

**Code** : `404 NOT FOUND`

**Content example**
```json
{
    "timestamp": "2020-01-01T01:01:01.449192500Z",
    "status": 404,
    "error": "Not Found",
    "message": "The given ID is not associated with a parking lot.",
    "path": "/parking_lot/5/park"
}
```

**Condition** : If the given ID is not associated with a parking lot 

**Code** : `404 NOT FOUND`

**Content example**
```json
{
    "timestamp": "2020-01-01T01:01:01.449192500Z",
    "status": 404,
    "error": "Not Found",
    "message": "The given ID is not associated with a parking lot.",
    "path": "/parking_lot/8/park"
}
```
