# Rate my desktop

## Contents

- [Description](#Description)
- [Wireframes](#Wireframes)
- [Database](#Database)

## Description

Rate-my-desktop is a website where users can post screenshots of their desktop and other users can rate them. It also contains a commenting functionality. 

As this is a final exercise of a [course](https://opinto-opas.haaga-helia.fi/course_unit/SWD4TF021) at Haaga-Helia university of applied sciences, it has some restrictions on the technologies chosen. 

Project has to be done using Spring Boot and webpages have to be rendered using Thymeleaf. I think there is some freedom on database selection, but I am going for MariaDB. I will publish this service using Docker and I will use CI-tools from Github. 


## Wireframes

Since this is a backend-course, the frontend is kept quite simple. 

![Wireframe](Docs/Wireframe/Wireframes.svg "Wireframe")

## Database

Database relation model: [Relationmodel.svg](Docs/Database/Relationmodel.svg)

### Users

*Users are stored in this table. No additional data is gathered (like email, real name etc.)*

| Field | Type | Description |
|----  |---- | -----|
| userID | int PK | id of the user |
| levelID | int FK | what is the level of the user (locked/user/admin/etc.). Refers to [UserLevels](#UserLevels) |
| username | varchar(100) | username for the user |
| password | varchar(200) | password for the user |

### UserLevels

*This table contains different access levels the users might have*

| Field | Type | Description |
|----|----|----|
| levelID | int PK | id of the level |
| level_name | varchar(100) | a clear text name for the level (seen in profile) |
| level_value | varchar(100) | a value used in the code (like 'ADMIN' or 'USER') |

### Screenshots

*This is the table used to store screenshots. Actual images are stored on disk, but this table contains a filename for finding the correct file.*

| Field | Type | Description |
|----|----|----|
| screenshotID | int PK | id for the screenshot |
| userID | int FK | used for finding the owner of the screenshot. Refers to [Users](#Users) |
| screenshot_name | varchar(100) | Give a kewl name for your screenshot |
| filename | varchar(100) | A generated name used for accessing the file from disc. |

