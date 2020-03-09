# Park a car at a specified Parking Lot

Park a car at a specified Parking Lot

**URL** : `/parking_lot/{parkingLotId}/park`

**Method** : `POST`

**Parameters**

Name | Description 
--- | --- 
parkingLotId | Parking Lot id where to park  
integer($int64) |


**Data constraints**

`type` should be a non-null length character string

**Data example** all fields must be sent

```json
{
  "type": "25kW"
}
```



## Success Responses

**Code** : `200 OK`

**Content** : 
```json
{
    "slot": 0,
    "parking_lot_id": 0,
    "arrival_time": "2020-01-01T01:01:01.449192500Z",
    "departure_time": null,
    "type": "25kW",
    "price": null
}
```


## Error Responses

**Condition** : If the type of slot does not exist in the parking lot  

**Code** : `404 NOT FOUND`

**Content example**
```json
{
    "timestamp": "2020-01-01T01:01:01.449192500Z",
    "status": 404,
    "error": "Not Found",
    "message": "This type of slot does not exist in this parking lot",
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

**Condition** : If the Parking Lot is full for this type of car 

**Code** : `503 SERVICE UNAVAILABLE`

**Content example**
```json
{
    "timestamp": "2020-01-01T01:01:01.449192500Z",
    "status": 503,
    "error": "Service Unavailable",
    "message": "Parking lot is full for this type of car",
    "path": "/parking_lot/0/park"
}
```
