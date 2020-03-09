# Display a Parking Lot

Display a Parking Lot given its id

**URL** : `/parking_lot/{parkingLotId}`

**Method** : `GET`

## Parameters

Name | Description 
--- | --- 
parkingLotId | Parking Lot id to display 
integer($int64) |

## Success Responses

**Code** : `200 OK`

**Content** : 
```json
{
    "id": 0,
    "name": "parking victoria 1",
    "layout": [
        {
            "name": "standard",
            "available": 10
        },
        {
            "name": "25kW",
            "available": 10
        }
    ],
    "pricing_policy": {
        "type": "simple",
        "flat_fee": 1,
        "per_hour_fare": 0.5
    }
}
```

## Error Responses

**Condition** : If parking lot id does not exist

**Code** : `404 NOT FOUND`

**Content example**
```json
{
    "timestamp": "2020-01-01T10:00:00.542+0000",
    "status": 404,
    "error": "Not Found",
    "message": "The given ID is not associated with a parking lot.",
    "path": "/parking_lot/50"
}
```

