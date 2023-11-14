-- Creation using SQL syntax
CREATE
OR
REPLACE
JSON RELATIONAL DUALITY VIEW race_dv AS
SELECT JSON { 'raceId' : r.race_id
     , 'name' : r.name
     , 'laps' : r.laps
WITH NOUPDATE,
        'date'   : r.race_date,
        'result' :
                 [ SELECT JSON {'driverRaceMapId' : drm.driver_race_map_id,
                                'position'        : drm.position,
                                UNNEST
                                  (SELECT JSON {'driverId' : d.driver_id,
                                                'name'     : d.name}
                                     FROM driver d WITH NOINSERT UPDATE NODELETE
                                     WHERE d.driver_id = drm.driver_id)}
                     FROM driver_race_map drm WITH INSERT UPDATE DELETE
                     WHERE drm.race_id = r.race_id ]}
    FROM race r WITH INSERT UPDATE DELETE;
