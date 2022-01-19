## club

| club_ID       | long        |
| ------------- | ----------- |
| name          | varchar(20) |
| member_number | long        |
| expenditure   | double      |

## Student

| student_ID | long        |
| ---------- | ----------- |
| name       | varchar(20) |
| password   | varchar(20) |
|            |             |

## activity

| activity_ID  | long         |
| ------------ | ------------ |
| club_ID      | long         |
| description  | varchar(200) |
| allow_number | long         |
| date         | date         |

## member

| student_ID          | long                          |
| ------------------- | ----------------------------- |
| club_ID             | long                          |
| level               | int(0-社长，1-管理员，2-成员) |
| club_cost(应交社费) | double                        |

