CREATE OR REPLACE JSON RELATIONAL DUALITY VIEW driver_dv AS
SELECT JSON {
        'driverId' : d.id,
    'name' : d.NAME,
    'points' : d.POINTS,
    UNNEST (SELECT JSON {
    'teamId' : t.ID,
    'team' : t.NAME
    }
    FROM TEAM t
    WHERE d.TEAM_ID = t.ID),
    'races' :[ SELECT JSON { 'positionId' : pp.ID,
                  UNNEST (	SELECT JSON {'raceId' : r.ID, 'name' : r.NAME, 'country' : r.COUNTRY}
                  FROM RACE r WHERE r.ID = pp.RACE_ID),
                  'position' : pp."POSITION"
                  }
              FROM PODIUM_POSITION pp
              WHERE pp.DRIVER_ID = d.ID
                  ]
                  }
              FROM DRIVER d
