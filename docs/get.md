# Show Accessible Parking Lots

Show all Parking Lots

**URL** : `/parking_lot`

**Method** : `GET`

## Optional Parameters

Name | Description 
--- | --- 
searchString | pass an optional search string for looking up parking lots 
string |

## Success Responses

**Code** : `200 OK`

**Content** : 
```json
[
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
    },
    {
        "id": 1,
        "name": "Sophia 2",
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
]
```
