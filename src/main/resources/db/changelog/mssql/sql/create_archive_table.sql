
CREATE TABLE "NOTIFICATIONS_ARCHIVE"
   ("NOTIFICATION_ID" VARCHAR(32) PRIMARY KEY NOT NULL,
	"PUBLISHER_ID" VARCHAR(10) NOT NULL,
	"TOPIC" VARCHAR(32) NOT NULL,
	"PUBLISHER_NOTIFICATION_ID" VARCHAR(256),
	"TITLE" NVARCHAR(255) NOT NULL,
	"NOTIFICATION_BODY" NVARCHAR(max) NOT NULL,
	"NOTIFICATION_URL" VARCHAR(512),
	"START_DATE" DATETIME,
	"END_DATE" DATETIME,
	"LAST_UPDATED" DATETIME NOT NULL,
	"NOTIFICATION_GROUP" VARCHAR(255),
	"NOTIFICATION_GROUP_NAME" VARCHAR(255),
    "ARCHIVE_DATE" DATETIME NOT NULL DEFAULT GETDATE()
   ) ;

-- EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the notification', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "NOTIFICATION_ID";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the publisher', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "PUBLISHER_ID";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Notification topic', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "TOPIC";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id the publisher uses for the notification', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "PUBLISHER_NOTIFICATION_ID";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Notification title', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "TITLE";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'The body of the notification', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "NOTIFICATION_BODY";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'The url for the notification', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "NOTIFICATION_URL";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Start date for the notification', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "START_DATE";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'End date for the notification', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "END_DATE";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "LAST_UPDATED";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Which LDAP group to send notification to.', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "NOTIFICATION_GROUP";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Which LDAP group (name) to send notification to.', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "NOTIFICATION_GROUP_NAME";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'Date record inserted into the archive table', 'user', user_name(), 'table', "NOTIFICATIONS_ARCHIVE", 'column', "ARCHIVE_DATE";

-- CREATE UNIQUE INDEX "NOTIFICATIONS_ARCHIVE_PK" ON "NOTIFICATIONS_ARCHIVE" ("NOTIFICATION_ID") ;

CREATE TABLE "NOTIFICATION_USERS_ARCHIVE"
   ("NOTIFICATION_ID" VARCHAR(32) NOT NULL,
	"UUN" VARCHAR(64) NOT NULL,
	"LAST_UPDATED" DATETIME NOT NULL
   ) ;

-- EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for the notification (SYS GUID)', 'user', user_name(), 'table', "NOTIFICATION_USERS", 'column', "NOTIFICATION_ID";
-- EXECUTE sp_addextendedproperty 'MS_Description', 'UUN to which the notification applies', 'user', user_name(), 'table', "NOTIFICATION_USERS", 'column', "UUN";

CREATE UNIQUE INDEX "NOTIFICATION_USERS_ARCHIVE_PK" ON "NOTIFICATION_USERS_ARCHIVE" ("NOTIFICATION_ID", "UUN")  ;
-- ALTER TABLE "NOTIFICATION_USERS_ARCHIVE" ADD CONSTRAINT "NOTIFICATION_USERS_ARCHIVE_PK" PRIMARY KEY ("NOTIFICATION_ID", "UUN");


CREATE TABLE "NOTIFICATION_ARCHIVE_ITEMS"
   ("NOTIFICATION_ID" VARCHAR(32) PRIMARY KEY NOT NULL
   ) ;

-- CREATE UNIQUE INDEX "NOTIFICATION_ARCHIVE_ITEMS_PK" ON "NOTIFICATION_ARCHIVE_ITEMS" ("NOTIFICATION_ID")  ;
