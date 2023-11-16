CREATE OR REPLACE JSON RELATIONAL DUALITY VIEW race_dv AS
SELECT JSON {
        'raceId' : r.id,
    'name'   : r.name,
    'country': r.COUNTRY,
    'laps'   : r.laps,
    'date'   : r.race_date,
    'result' : [SELECT JSON {
        'positionId' : pp.ID,
        'position' : pp."POSITION",
        UNNEST (SELECT JSON {
            'driverId' : d.ID,
            'name' : d.NAME}
            FROM DRIVER d
            WHERE d.ID = pp.DRIVER_ID)
        }
        FROM PODIUM_POSITION pp
        WHERE pp.RACE_ID = r.ID
    ]
}
FROM RACE r