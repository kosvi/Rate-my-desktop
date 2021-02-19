# Rate my desktop

## Contents

- [Description](#Description)
- [User stories](#User-stories)
- [Wireframes](#Wireframes)
- [Database](#Database)

## Description

Rate-my-desktop is a website where users can post screenshots of their desktop and other users can rate them. It also contains a commenting functionality. 

As this is a final exercise of a [course](https://opinto-opas.haaga-helia.fi/course_unit/SWD4TA020) at Haaga-Helia university of applied sciences, it has some restrictions on the technologies chosen. ([similar description in English](https://opinto-opas.haaga-helia.fi/course_unit/SWD4TF021))

Project has to be done using Spring Boot and webpages have to be rendered using Thymeleaf. I think there is some freedom on database selection, but I am going for MariaDB. I will publish this service using Docker and I will use CI-tools from Github. 

## User stories

| id | description |
|----|----         |
| 1  | As a user, I want to be able to create account without handing personal information as I want to be sure of how my information is stored |
| 2 | As a user, I want to be able to post my screenshot to the site to get comments about it |
| 3 | As a user, I want to be able to easily find my screenshots to see their ratings and read their comments. 
| 4 | As a user, I want to be able to comments other peoples screenshots so they can read what I like about them (or don't like about them). |
| 5 | As a user, I want to be able to change my account name and password in case I think my password is compromised |
| 6 | As a user, I want to be able to delete screenshots I've uploaded in case I accidentally upload wrong pic or notice something I don't want others to see in it. 
| 7 | As a user, I want others to be able to rate my screenshot so I know it it's cool or not |
| 8 | As an admin, I want to be able to delete screenshots in case there is something inappropriate in it. |
| 9 | As an admin, I want to be able to delete individual comments in case there is something inappropriate in it. | 
| 10 | As an admin, I want to be able to lock accounts in case they keep on harrassing others |

## Wireframes

Since this is a backend-course, the frontend is kept quite simple. 

![Wireframe](Docs/Wireframe/Wireframes.svg "Wireframe")

## Database

Database relation model: [Relationmodel.svg](Docs/Database/Relationmodel.svg)

### User

*Users are stored in this table. No additional data is gathered (like email, real name etc.)*

| Field | Type | Description |
|----  |---- | -----|
| userID | int PK | id of the user |
| levelID | int FK | what is the level of the user (locked/user/admin/etc.). Refers to [UserLevels](#UserLevels) |
| username | varchar(100) | username for the user |
| password | varchar(200) | password for the user |

### UserLevel

*This table contains different access levels the users might have*

| Field | Type | Description |
|----|----|----|
| levelID | int PK | id of the level |
| level_name | varchar(100) | a clear text name for the level (seen in profile) |
| level_value | varchar(100) | a value used in the code (like 'ADMIN' or 'USER') |

### Screenshot

*This is the table used to store screenshots. Actual images are stored on disk, but this table contains a filename for finding the correct file.*

| Field | Type | Description |
|----|----|----|
| screenshotID | int PK | id for the screenshot |
| userID | int FK | used for finding the owner of the screenshot. Refers to [Users](#Users) |
| screenshot_name | varchar(100) | Give a kewl name for your screenshot |
| filename | varchar(100) | A generated name used for accessing the file from disc. |

### Comment

*This table stores comments posted on screenshots.*

| Field | Type | Description |
|----|----|----|
| commentID | int PK | id for the comment |
| screenshotID | int FK | id for the [screenshot|(#Screenshots) |
| userID | int FK | id for the [user](#Users) who posted the comment |
| comment | varchar(255) | the actual comment shown on the page |
| timestamp | datetime | date & time when the comment was posted |

### Rating 

*This table contains the ratings for the screenshots.*

| Field | Type | Description |
|----|----|----|
| ratingID | int PK | id for the rating |
| screenshotID | int FK | id for the [screenshot](#Screenshots) |
| userID | int FK | id for the [user](#Users) who gave the rating |
| rating | int | the actual value given for the screenshot (1-5) |

