DELETE FROM PARTICIPATION_REQUESTS;
DELETE FROM EVENTS;
DELETE FROM COMPILATIONS;
DELETE FROM LOCATIONS;
DELETE FROM USERS;
DELETE FROM CATEGORIES;

ALTER TABLE PARTICIPATION_REQUESTS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE EVENTS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE COMPILATIONS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE CATEGORIES ALTER COLUMN ID RESTART WITH 1;
