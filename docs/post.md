# Create a Parking Lot

Create a Parking Lot

**URL** : `/parking_lot`

**Method** : `POST`

**Data constraints**

`name` should be a non-null length character string

`layout` should not be null

`layout.available` should be a positive integer

`layout.name` should be a non-null length character string

`pricing_policy` should not be null

***Pricing policy data structure***
example:
```json  
"pricing_policy": {
   "type": "simple",
   "flat_fee": 1,
   "per_hour_fare": 0.5
 }
```

`type`: non-null String - represents the type of policy implemented (at the moment, only `simple` has been implemented)

`flat_fee`: float number that represents the fixed amount to pay

`per_hour_fare`: float number that represents the amount to pay for each hour in the parking (each started hour is due)


**Data example** all fields must be sent

```json
{
  "name": "parking Sophia 2",
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



## Success Responses

**Code** : `201 CREATED`

**Headers** : `Location: /parking_lot/{id}`

**Content** : 
```json
{
     "id": 0,
     "name": "parking Sophia 2",
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

**Condition** : If fields are missing

**Code** : `400 BAD REQUEST`

**Content example**
```json
{
    "timestamp": "2020-01-01T10:00:00.542+0000",
    "status": 400,
    "error": "Bad Request",
    "errors": [
        {
            "codes": [
                "NotNull.parkingLot.pricingPolicy",
                "NotNull.pricingPolicy",
                "NotNull.PricingPolicy",
                "NotNull"
            ],
            "arguments": [
                {
                    "codes": [
                        "parkingLot.pricingPolicy",
                        "pricingPolicy"
                    ],
                    "arguments": null,
                    "defaultMessage": "pricingPolicy",
                    "code": "pricingPolicy"
                }
            ],
            "defaultMessage": "ne peut pas être nul",
            "objectName": "parkingLot",
            "field": "pricingPolicy",
            "rejectedValue": null,
            "bindingFailure": false,
            "code": "NotNull"
        }
    ],
    "message": "Validation failed for object='parkingLot'. Error count: 1",
    "path": "/parking_lot"
}
```
