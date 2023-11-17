CREATE OR REPLACE JSON RELATIONAL DUALITY VIEW team_dv AS
SELECT JSON {
    'teamId'    : t.ID,
    'name'      : t.NAME,
    'country'   : t.COUNTRY,
    'points'    : t.POINTS,
    'drivers'    : [ SELECT JSON {'driverId' : d.ID,
                     'name'     : d.name,
                     'points'   : d.points}
                 FROM driver d
                 WHERE d.team_id = t.ID ]
 }
FROM team t;
