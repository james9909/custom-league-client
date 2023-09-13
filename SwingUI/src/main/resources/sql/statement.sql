CREATE TABLE IF NOT EXISTS CACHE
(
    LOCATION VARCHAR
(
    256
) NOT NULL,
    HASH VARCHAR
(
    32
) NOT NULL,
    DATA LONGVARBINARY
(
    1048576
) NOT NULL,
    PRIMARY KEY
(
    LOCATION
)
    )