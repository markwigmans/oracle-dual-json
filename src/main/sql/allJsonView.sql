CREATE OR REPLACE JSON RELATIONAL DUALITY VIEW all_dv AS
SELECT JSON {
        'teamId'    : t.ID,
    'name'      : t.NAME,
    'country'   : t.COUNTRY,
    'points'    : t.POINTS,
    'drivers'   :   [   SELECT JSON {
                            'driverId'	: d.ID,
                            'name'     	: d.name,
                            'points'   	: d.points,
                            'races'	    : [ SELECT JSON {
                                                'positionId' : pp.ID,
                                                UNNEST ( SELECT JSON {
                                                            'raceId' : r.ID,
                                                            'name' : r.NAME,
                                                            'country' : r.COUNTRY,
                                                            'date' : r.RACE_DATE
                                                        }
                                                        FROM RACE r WHERE r.ID = pp.RACE_ID),
                                                'position' : pp."POSITION",
                                                'points'   : pp.POINTS
                                            }
                                            FROM PODIUM_POSITION pp
                                            WHERE pp.DRIVER_ID = d.ID
                                        ]
                        }
                        FROM driver d
                        WHERE d.team_id = t.ID
                    ]
}
FROM team t;
