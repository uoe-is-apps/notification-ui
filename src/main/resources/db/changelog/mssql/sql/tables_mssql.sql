--------------------------------------------------------
--  CONDITIONALLY DROP ALL TABLES
--------------------------------------------------------

  DROP TABLE IF EXISTS "USER_NOTIFICATION_AUDIT";
  DROP TABLE IF EXISTS "TOPIC_SUBSCRIPTIONS";
  DROP TABLE IF EXISTS "TOPIC_DETAILS";
  DROP TABLE IF EXISTS "PUBLISHER_DETAILS";
  DROP TABLE IF EXISTS "SUBSCRIBER_DETAILS";
  DROP TABLE IF EXISTS "OFFICE365_SUBSCRIPTION";
  DROP TABLE IF EXISTS "NOTIFICATIONS";
  DROP TABLE IF EXISTS "NOTIFICATION_UI_ROLES";
  DROP TABLE IF EXISTS "NOTIFICATION_USERS";
  DROP TABLE IF EXISTS "NOTIFICATION_UI_USERS";
  DROP TABLE IF EXISTS "NOTIFICATION_UI_USER_ROLES";
  DROP TABLE IF EXISTS "NOTIFICATION_PUSH_STATUS";
  DROP TABLE IF EXISTS "NOTIFICATION_ERRORS";
  DROP TABLE IF EXISTS "DATABASECHANGELOG";
  DROP TABLE IF EXISTS "DATABASECHANGELOGLOCK";
  DROP TABLE IF EXISTS "QRTZ_BLOB_TRIGGERS";
  DROP TABLE IF EXISTS "QRTZ_CRON_TRIGGERS";
  DROP TABLE IF EXISTS "QRTZ_SIMPLE_TRIGGERS";
  DROP TABLE IF EXISTS "QRTZ_SIMPROP_TRIGGERS";
  DROP TABLE IF EXISTS "QRTZ_TRIGGERS";
  DROP TABLE IF EXISTS "QRTZ_JOB_DETAILS";
  DROP TABLE IF EXISTS "QRTZ_CALENDARS";
  DROP TABLE IF EXISTS "QRTZ_FIRED_TRIGGERS";
  DROP TABLE IF EXISTS "QRTZ_LOCKS";
  DROP TABLE IF EXISTS "QRTZ_PAUSED_TRIGGER_GRPS";
  DROP TABLE IF EXISTS "QRTZ_SCHEDULER_STATE";

--------------------------------------------------------
--  DDL for Table USER_NOTIFICATION_AUDIT
--------------------------------------------------------

  CREATE TABLE "USER_NOTIFICATION_AUDIT"
   ("AUDIT_ID" VARCHAR(32) PRIMARY KEY NOT NULL,
	"PUBLISHER_ID" VARCHAR(10) NOT NULL,
	"ACTION" VARCHAR(32) NOT NULL,
	"AUDIT_DATE" DATETIME NOT NULL DEFAULT GETDATE(),
	"NOTIFICATION_ID" NVARCHAR(32),
	"AUDIT_DESCRIPTION" NVARCHAR(max)
   );

--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for audit entry', 'user', user_name(), 'table', "USER_NOTIFICATION_AUDIT", 'column', "AUDIT_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for the publisher', 'user', user_name(), 'table', "USER_NOTIFICATION_AUDIT", 'column', "PUBLISHER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for audit entry', 'user', user_name(), 'table', "USER_NOTIFICATION_AUDIT", 'column', "ACTION";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Date for audit entry', 'user', user_name(), 'table', "USER_NOTIFICATION_AUDIT", 'column', "AUDIT_DATE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Notification to which the audit entry applies', 'user', user_name(), 'table', "USER_NOTIFICATION_AUDIT", 'column', "NOTIFICATION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Description of the audit entry', 'user', user_name(), 'table', "USER_NOTIFICATION_AUDIT", 'column', "AUDIT_DESCRIPTION";


--------------------------------------------------------
--  DDL for Index USER_NOTIFICATION_AUDIT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "USER_NOTIFICATION_AUDIT_PK" ON "USER_NOTIFICATION_AUDIT" ("AUDIT_ID");


--------------------------------------------------------
--  Constraints for Table USER_NOTIFICATION_AUDIT
--------------------------------------------------------

--  ALTER TABLE "USER_NOTIFICATION_AUDIT" MODIFY ("AUDIT_ID" NOT NULL ENABLE);
--  ALTER TABLE "USER_NOTIFICATION_AUDIT" MODIFY ("PUBLISHER_ID" NOT NULL ENABLE);
--  ALTER TABLE "USER_NOTIFICATION_AUDIT" MODIFY ("ACTION" NOT NULL ENABLE);
--  ALTER TABLE "USER_NOTIFICATION_AUDIT" MODIFY ("AUDIT_DATE" NOT NULL ENABLE);
--  ALTER TABLE "USER_NOTIFICATION_AUDIT" ADD CONSTRAINT "USER_NOTIFICATION_AUDIT_PK" PRIMARY KEY ("AUDIT_ID");

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------
--  DDL for Table TOPIC_SUBSCRIPTIONS
--------------------------------------------------------

  CREATE TABLE "TOPIC_SUBSCRIPTIONS"
   ("SUBSCRIPTION_ID" VARCHAR(32) PRIMARY KEY NOT NULL,
	"TOPIC" VARCHAR(32) NOT NULL,
	"SUBSCRIBER_ID" VARCHAR(32) NOT NULL,
	"STATUS" VARCHAR(1) NOT NULL,
	"LAST_UPDATED" DATETIME NOT NULL DEFAULT GETDATE()
   );


--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the subscription', 'user', user_name(), 'table', "TOPIC_SUBSCRIPTIONS", 'column', "SUBSCRIPTION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Topic subscribed to', 'user', user_name(), 'table', "TOPIC_SUBSCRIPTIONS", 'column', "TOPIC";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'ID of the subscriber', 'user', user_name(), 'table', "TOPIC_SUBSCRIPTIONS", 'column', "SUBSCRIBER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Status of subscription, (A)ctive or (I)nactive', 'user', user_name(), 'table', "TOPIC_SUBSCRIPTIONS", 'column', "STATUS";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "TOPIC_SUBSCRIPTIONS", 'column', "LAST_UPDATED";
--------------------------------------------------------
--  DDL for Index TOPIC_SUBSCRIPTIONS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TOPIC_SUBSCRIPTIONS_PK" ON "TOPIC_SUBSCRIPTIONS" ("SUBSCRIPTION_ID");

--------------------------------------------------------
--  Constraints for Table TOPIC_SUBSCRIPTIONS
--------------------------------------------------------

--  ALTER TABLE "TOPIC_SUBSCRIPTIONS" MODIFY ("SUBSCRIPTION_ID" NOT NULL ENABLE);
--  ALTER TABLE "TOPIC_SUBSCRIPTIONS" MODIFY ("TOPIC" NOT NULL ENABLE);
--  ALTER TABLE "TOPIC_SUBSCRIPTIONS" MODIFY ("SUBSCRIBER_ID" NOT NULL ENABLE);
--  ALTER TABLE "TOPIC_SUBSCRIPTIONS" MODIFY ("STATUS" NOT NULL ENABLE);
--  ALTER TABLE "TOPIC_SUBSCRIPTIONS" MODIFY ("LAST_UPDATED" NOT NULL ENABLE);
--  ALTER TABLE "TOPIC_SUBSCRIPTIONS" ADD CONSTRAINT "TOPIC_SUBSCRIPTIONS_PK" PRIMARY KEY ("SUBSCRIPTION_ID");


Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('43143B913D80E7FCE053031DA8C0A360','Group','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('477C347677973001E053031DA8C086D9','Emergency','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('74A98ADFA00374D0E053191DA8C08ED9','Card Services','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('74A94B763DBD74CCE053191DA8C08A90','Exam Timetabling','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('74A94FB7A5733DDBE053191DA8C0069F','Library','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('74A94B763DBE74CCE053191DA8C08A90','DTI020 Pilot','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('74A7E23DC61374CEE053191DA8C0AD67','MBChB','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('74A7E23DC61474CEE053191DA8C0AD67','Careers Service','myed','A');
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS) values ('66BD498872CF09A9E053191DA8C05DD7','Student','myed','A');

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------


--------------------------------------------------------
--  DDL for Table PUBLISHER_DETAILS
--------------------------------------------------------

  CREATE TABLE "PUBLISHER_DETAILS"
   ("PUBLISHER_ID" VARCHAR(10) PRIMARY KEY NOT NULL,
	"PUBLISHER_DESCRIPTION" NVARCHAR(255),
	"PUBLISHER_KEY" VARCHAR(32),
	"PUBLISHER_TYPE" VARCHAR(4) NOT NULL,
	"STATUS" VARCHAR(1) NOT NULL,
	"LAST_UPDATED" DATETIME NOT NULL DEFAULT GETDATE()
   );

--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of publisher', 'user', user_name(), 'table', "PUBLISHER_DETAILS", 'column', "PUBLISHER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The description of the publisher', 'user', user_name(), 'table', "PUBLISHER_DETAILS", 'column', "PUBLISHER_DESCRIPTION";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique key for the publisher, used to validate certain publisher activities', 'user', user_name(), 'table', "PUBLISHER_DETAILS", 'column', "PUBLISHER_KEY";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Type of publisher', 'user', user_name(), 'table', "PUBLISHER_DETAILS", 'column', "PUBLISHER_TYPE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Status of publisher, (A)ctive or (I)nactive', 'user', user_name(), 'table', "PUBLISHER_DETAILS", 'column', "STATUS";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "PUBLISHER_DETAILS", 'column', "LAST_UPDATED";
--------------------------------------------------------
--  DDL for Index PUBLISHER_DETAILS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PUBLISHER_DETAILS_PK" ON "PUBLISHER_DETAILS" ("PUBLISHER_ID") ;
--------------------------------------------------------
--  Constraints for Table PUBLISHER_DETAILS
--------------------------------------------------------

--  ALTER TABLE "PUBLISHER_DETAILS" MODIFY ("PUBLISHER_ID" NOT NULL ENABLE);
--  ALTER TABLE "PUBLISHER_DETAILS" MODIFY ("PUBLISHER_TYPE" NOT NULL ENABLE);
--  ALTER TABLE "PUBLISHER_DETAILS" MODIFY ("STATUS" NOT NULL ENABLE);
--  ALTER TABLE "PUBLISHER_DETAILS" MODIFY ("LAST_UPDATED" NOT NULL ENABLE);
--  ALTER TABLE "PUBLISHER_DETAILS" ADD CONSTRAINT "PUBLISHER_DETAILS_PK" PRIMARY KEY ("PUBLISHER_ID");




Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('euclid3','Euclid','004TFE5E177023ABE05642144F00F4CC','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('notify-ui','Notification Backbone UI',null,'BOTH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('euclid','Euclid','004TFE5E177023ABE05642144F00F4DD','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('CareerSer','Careers Service','004TFE5E177023ABE05642144F11F4DD','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('DTI020','DTI020','004TFE5E177023ABE05642224F00F4DD','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('MedicalEdu','Medical Education','033TFE5E177023ABE05642144F00F4DD','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('CardSer','Card Services','004TFE5E177443ABE05642144F00F4DD','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('Library','Library','004TFE5E177023ABE05645544F00F4DD','PUSH','A');
Insert into PUBLISHER_DETAILS (PUBLISHER_ID,PUBLISHER_DESCRIPTION,PUBLISHER_KEY,PUBLISHER_TYPE,STATUS) values ('ExamTime','Exam Timetabling','004TFE5E166023ABE05642144F00F4DD','PUSH','A');




------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------
--  DDL for Table TOPIC_DETAILS
--------------------------------------------------------

  CREATE TABLE "TOPIC_DETAILS"
   ("TOPIC_ID" VARCHAR(60) PRIMARY KEY,
	"TOPIC_DESCRIPTION" NVARCHAR(255),
	"PUBLISHER_ID" VARCHAR(10),
	"STATUS" VARCHAR(1),
	"LAST_UPDATED" DATETIME DEFAULT GETDATE()
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of topic', 'user', user_name(), 'table', "TOPIC_DETAILS", 'column', "TOPIC_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The description of the topic', 'user', user_name(), 'table', "TOPIC_DETAILS", 'column', "TOPIC_DESCRIPTION";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of publisher that publishes this topic', 'user', user_name(), 'table', "TOPIC_DETAILS", 'column', "PUBLISHER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Status of topic, (A)ctive or (I)nactive', 'user', user_name(), 'table', "TOPIC_DETAILS", 'column', "STATUS";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "TOPIC_DETAILS", 'column', "LAST_UPDATED";
--------------------------------------------------------
--  DDL for Index TOPIC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TOPIC_PK" ON "TOPIC_DETAILS" ("TOPIC_ID");
--------------------------------------------------------
--  Constraints for Table TOPIC_DETAILS
--------------------------------------------------------

--  ALTER TABLE "TOPIC_DETAILS" ADD CONSTRAINT "TOPIC_PK" PRIMARY KEY ("TOPIC_ID");
--------------------------------------------------------
--  Ref Constraints for Table TOPIC_DETAILS
--------------------------------------------------------



  ALTER TABLE "TOPIC_DETAILS" ADD CONSTRAINT "TOPIC_FK" FOREIGN KEY ("PUBLISHER_ID")
	  REFERENCES "PUBLISHER_DETAILS" ("PUBLISHER_ID");



Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Careers Service','Careers Service Topic','CareerSer','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('DTI020 Pilot','DTI020 Pilot Topic','DTI020','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('MBChB','MBChB Topic','MedicalEdu','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Card Services','Card Services Topic','CardSer','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Library','Library Topic','Library','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Exam Timetabling','Exam Timetabling Topic','ExamTime','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Emergency','Emergency Topic','notify-ui','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Group','Group Topic','notify-ui','A');
Insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS) values ('Student','Student Topic','euclid','A');


------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------
--  DDL for Table SUBSCRIBER_DETAILS
--------------------------------------------------------

  CREATE TABLE "SUBSCRIBER_DETAILS"
   ("SUBSCRIBER_ID" VARCHAR(10) PRIMARY KEY,
	"SUBSCRIBER_DESCRIPTION" NVARCHAR(255),
	"SUBSCRIBER_TYPE" VARCHAR(4),
	"STATUS" VARCHAR(1),
	"LAST_UPDATED" DATETIME NOT NULL DEFAULT GETDATE()
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the subscriber', 'user', user_name(), 'table', "SUBSCRIBER_DETAILS", 'column', "SUBSCRIBER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The description of the subscriber', 'user', user_name(), 'table', "SUBSCRIBER_DETAILS", 'column', "SUBSCRIBER_DESCRIPTION";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Type of subscriber', 'user', user_name(), 'table', "SUBSCRIBER_DETAILS", 'column', "SUBSCRIBER_TYPE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Status of subscriber, (A)ctive or (I)nactive', 'user', user_name(), 'table', "SUBSCRIBER_DETAILS", 'column', "STATUS";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "SUBSCRIBER_DETAILS", 'column', "LAST_UPDATED";
--------------------------------------------------------
--  DDL for Index SUBSCRIBER_DETAILS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SUBSCRIBER_DETAILS_PK" ON "SUBSCRIBER_DETAILS" ("SUBSCRIBER_ID")  ;
--------------------------------------------------------
--  Constraints for Table SUBSCRIBER_DETAILS
--------------------------------------------------------

--  ALTER TABLE "SUBSCRIBER_DETAILS" MODIFY ("LAST_UPDATED" NOT NULL ENABLE);
--  ALTER TABLE "SUBSCRIBER_DETAILS" ADD CONSTRAINT "SUBSCRIBER_DETAILS_PK" PRIMARY KEY ("SUBSCRIBER_ID");

Insert into SUBSCRIBER_DETAILS (SUBSCRIBER_ID,SUBSCRIBER_DESCRIPTION,SUBSCRIBER_TYPE,STATUS) values ('myed','MyEd Notification Subscriber','Pull','A');


------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------


--------------------------------------------------------
--  DDL for Table OFFICE365_SUBSCRIPTION
--------------------------------------------------------

  CREATE TABLE "OFFICE365_SUBSCRIPTION"
   ("SUBSCRIPTION_ID" VARCHAR(128) PRIMARY KEY,
	"SUBSCRIPTION_RENEW" DATETIME,
	"SUBSCRIPTION_EXPIRY" DATETIME,
	"LAST_UPDATED" DATETIME DEFAULT GETDATE()
   );

--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the subscription', 'user', user_name(), 'table', "OFFICE365_SUBSCRIPTION", 'column', "SUBSCRIPTION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The date time when this subscription is renewed', 'user', user_name(), 'table', "OFFICE365_SUBSCRIPTION", 'column', "SUBSCRIPTION_RENEW";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The date time when this subscription will expire', 'user', user_name(), 'table', "OFFICE365_SUBSCRIPTION", 'column', "SUBSCRIPTION_EXPIRY";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "OFFICE365_SUBSCRIPTION", 'column', "LAST_UPDATED";
--   COMMENT ON TABLE "OFFICE365_SUBSCRIPTION"  IS 'The table stores office 365 subscription details';
--------------------------------------------------------
--  DDL for Index OFFICE365_SUBSCRIPTION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "OFFICE365_SUBSCRIPTION_PK" ON "OFFICE365_SUBSCRIPTION" ("SUBSCRIPTION_ID") ;
--------------------------------------------------------
--  Constraints for Table OFFICE365_SUBSCRIPTION
--------------------------------------------------------

--  ALTER TABLE "OFFICE365_SUBSCRIPTION" ADD CONSTRAINT "OFFICE365_SUBSCRIPTION_PK" PRIMARY KEY ("SUBSCRIPTION_ID");


------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------
--  DDL for Table NOTIFICATIONS
--------------------------------------------------------

  CREATE TABLE "NOTIFICATIONS"
   ("NOTIFICATION_ID" VARCHAR(32) PRIMARY KEY NOT NULL,
	"PUBLISHER_ID" VARCHAR(10) NOT NULL,
	"TOPIC" VARCHAR(32) NOT NULL,
	"PUBLISHER_NOTIFICATION_ID" VARCHAR(256),
	"TITLE" NVARCHAR(255) NOT NULL,
	"NOTIFICATION_BODY" NVARCHAR(max) NOT NULL,
	"NOTIFICATION_URL" VARCHAR(512),
	"START_DATE" DATETIME,
	"END_DATE" DATETIME,
	"LAST_UPDATED" DATETIME NOT NULL DEFAULT GETDATE(),
	"NOTIFICATION_GROUP" VARCHAR(255),
	"NOTIFICATION_GROUP_NAME" VARCHAR(255)
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the notification', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "NOTIFICATION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id of the publisher', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "PUBLISHER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Notification topic', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "TOPIC";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The unique id the publisher uses for the notification', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "PUBLISHER_NOTIFICATION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Notification title', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "TITLE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The body of the notification', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "NOTIFICATION_BODY";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'The url for the notification', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "NOTIFICATION_URL";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Start date for the notification', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "START_DATE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'End date for the notification', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "END_DATE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "LAST_UPDATED";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Which LDAP group to send notification to.', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "NOTIFICATION_GROUP";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Which LDAP group (name) to send notification to.', 'user', user_name(), 'table', "NOTIFICATIONS", 'column', "NOTIFICATION_GROUP_NAME";
--------------------------------------------------------
--  DDL for Index NOTIFICATIONS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NOTIFICATIONS_PK" ON "NOTIFICATIONS" ("NOTIFICATION_ID") ;
--------------------------------------------------------
--  DDL for Index END_DATE_NIND
--------------------------------------------------------

  CREATE INDEX "END_DATE_NIND" ON "NOTIFICATIONS" ("END_DATE")  ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATIONS
--------------------------------------------------------

--  ALTER TABLE "NOTIFICATIONS" MODIFY ("NOTIFICATION_ID" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATIONS" MODIFY ("PUBLISHER_ID" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATIONS" MODIFY ("TOPIC" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATIONS" MODIFY ("TITLE" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATIONS" MODIFY ("NOTIFICATION_BODY" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATIONS" MODIFY ("LAST_UPDATED" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATIONS" ADD CONSTRAINT "NOTIFICATIONS_PK" PRIMARY KEY ("NOTIFICATION_ID");


Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f664f970cb01655be2fa560000','CardSer','Card Service','CardSer001','Photo Upload required','Photo Upload required','https://www.google.com',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f464f44ae201658b5ac5f40001','DTI020','DTI020 Pilot','DTI020001','Example notification','Example notification','https://www.google.com',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f6658a8c3101658a9059f70000','notify-ui','Group',null,'test group notification','test group notification','http://www.ed.ac.uk','ou=D717,ou=P5J,ou=ISG3,ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk','Development Services');
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f664f970cb01655be878a80001','euclid','Student','euclid023','Test','Test','https://edinburgh.onlinesurveys.ac.uk/notifications-service-pilot-interns',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f664f970cb01658af8cfa70002','CardSer','Card Service','CardSer001','Upload your photo','Upload your photo','https://www.google.com',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f664f970cb01658b59db460005','Library','Library','Library001','Renewal due','Renewal due','https://www.google.com',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f464f44ae201658b08615a0000','CareerSer','Careers Service','CareerSer001','Careers talk','Careers talk','https://www.google.com',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f664f970cb01658afc79020003','MedicalEdu','MBChB','MedicalEdu001','Medical Education','Medical Education','https://www.google.com',null,null);
Insert into NOTIFICATIONS (NOTIFICATION_ID,PUBLISHER_ID,TOPIC,PUBLISHER_NOTIFICATION_ID,TITLE,NOTIFICATION_BODY, NOTIFICATION_URL,NOTIFICATION_GROUP,NOTIFICATION_GROUP_NAME) values ('402817f664f970cb01658b58068f0004','ExamTime','Exam Timetabling','ExamTime001','Exam scheduled published','Exam scheduled published','https://www.google.com',null,null);


------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------




--------------------------------------------------------
--  DDL for Table NOTIFICATION_UI_ROLES
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION_UI_ROLES"
   ("ROLE_CODE" VARCHAR(10) PRIMARY KEY,
	"ROLE_DESCRIPTION" NVARCHAR(255)
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'code for the role', 'user', user_name(), 'table', "NOTIFICATION_UI_ROLES", 'column', "ROLE_CODE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'description of the role', 'user', user_name(), 'table', "NOTIFICATION_UI_ROLES", 'column', "ROLE_DESCRIPTION";
--------------------------------------------------------
--  DDL for Index NOTIFICATION_UI_ROLES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NOTIFICATION_UI_ROLES_PK" ON "NOTIFICATION_UI_ROLES" ("ROLE_CODE") ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATION_UI_ROLES
--------------------------------------------------------

--  ALTER TABLE "NOTIFICATION_UI_ROLES" ADD CONSTRAINT "NOTIFICATION_UI_ROLES_PK" PRIMARY KEY ("ROLE_CODE");


Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('USRSUPPORT','Allows user support actions in the notification UI');
Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('SYSSUPPORT','Allows system level support actions in the notification UI');
Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('GROUP','Allow creation, and maintenance of group notifications');
Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('SUPERADMIN','Allow access to all screens');
Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('EMERGENCY','Allow creation, and maintenance of emergency notifications.');
Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('USERADMIN','Allow administration of users.');
Insert into NOTIFICATION_UI_ROLES (ROLE_CODE,ROLE_DESCRIPTION) values ('DENYACCESS','Disallow access to all screens');












--------------------------------------------------------
--  DDL for Table NOTIFICATION_USERS
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION_USERS"
   ("NOTIFICATION_ID" VARCHAR(32) NOT NULL,
	"UUN" VARCHAR(64) NOT NULL,
	"LAST_UPDATED" DATETIME DEFAULT GETDATE()
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for the notification (SYS GUID)', 'user', user_name(), 'table', "NOTIFICATION_USERS", 'column', "NOTIFICATION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'UUN to which the notification applies', 'user', user_name(), 'table', "NOTIFICATION_USERS", 'column', "UUN";
--------------------------------------------------------
--  DDL for Index NOTIFICATION_USERS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "NOTIFICATION_USERS_PK" ON "NOTIFICATION_USERS" ("NOTIFICATION_ID", "UUN")  ;
--------------------------------------------------------
--  DDL for Index END_DATE_UIND
--------------------------------------------------------

  CREATE INDEX "END_DATE_UIND" ON "NOTIFICATION_USERS" ("LAST_UPDATED")  ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATION_USERS
--------------------------------------------------------

  ALTER TABLE "NOTIFICATION_USERS" ADD CONSTRAINT "NOTIFICATION_USERS_PK" PRIMARY KEY ("NOTIFICATION_ID", "UUN");



Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f664f970cb01655be2fa560000','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f464f44ae201658b5ac5f40001','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','hjohnst6');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','isabuild');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','isappsca');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','jalliso2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','jnewton3');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','jobadmin');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','labyrinl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','labyrint');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','lbeattie');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','legalser');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','liveo365');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','mfraser5');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','miannett');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','mmcmonag');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','nonspda2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ooerlabl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ooerlabt');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','pctagent');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','pdonogh2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','perssql1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','perssql2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','perssql3');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','dma');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','lsms');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','tsms');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','awebb');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eemec');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','hsun1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','iainf');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','lo365');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','nexus');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','talks');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','to365');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','uoeas');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','asplaw');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','corehr');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eemecl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eemect');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eevecl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eevect');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','kxspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','mlang1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','msapps');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','saxfer');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','soaapp');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','spotlt');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sqldev');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','uoeast');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','acaspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','astasql');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','billlee');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','callive');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','caltest');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','csynesl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','csynest');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','devext0');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','devext1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','devext2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','devext3');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','devtech');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','dnotify');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','elelive');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eletest');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','epaydev');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','epaytst');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','gcarter');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ggranum');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','idmspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','kinetic');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','kxspda2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','lexseed');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','lnotify');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','lstaffm');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','mjones5');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','nonspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','npardoe');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','orgspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','peterpr');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ppm2000');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','r1dtlap');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','rmanley');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','rsocont');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','schneid');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sitswin');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sqlopas');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sqlserv');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sqltest');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sspauto');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','syllrpt');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','texseed');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','tnotify');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','tpogoda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','tstaffm');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','v1qqmpv');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','wwwjobs');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','acaspda2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','adobbing');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','appsreso');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','arichar1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','astalive');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','astatest');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','brobbere');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','careerhb');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','casmlive');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','casmtest');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ckonczak');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','codeview');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','corpladm');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','corplsql');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ctxsql01');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','devtalks');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','dstillwe');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','dtechiot');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','dtostack');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eemecarl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eemecart');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eevecarl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eevecart');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eevectes');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','escott12');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','escriptl');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','escriptt');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','eugxspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','examssrs');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','exchFunc');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ggiltrow');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','ghenders');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','gmckenna');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','gpennyco');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','perssql4');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','phorrock');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','pjackso1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','portfoll');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','portfolt');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','pvetliv1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','pvetliv2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','pvetliv3');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','rharris7');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','rstewa11');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','saltspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','schneid1');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','scientia');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sqlserv2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','sqltest2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','stellama');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','swdevrec');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','syllspda');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','testtalk');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','tnikolai');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','trend963');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','v1dmcke6');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','v1pmcnul');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','v1sgran6');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','gradcon2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f6658a8c3101658a9059f70000','gradconn');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f664f970cb01655be878a80001','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f664f970cb01658af8cfa70002','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f664f970cb01658b59db460005','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f464f44ae201658b08615a0000','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f664f970cb01658afc79020003','bowers2');
Insert into NOTIFICATION_USERS (NOTIFICATION_ID,UUN) values ('402817f664f970cb01658b58068f0004','bowers2');



--------------------------------------------------------
--  DDL for Table NOTIFICATION_UI_USERS
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION_UI_USERS"
   ("UUN" VARCHAR(64) PRIMARY KEY NOT NULL,
	"ORG_GROUP_DN" VARCHAR(255)
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'uun with access to the ui', 'user', user_name(), 'table', "NOTIFICATION_UI_USERS", 'column', "UUN";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Holds the org unit to which the user has permissions to create group notifications on or under', 'user', user_name(), 'table', "NOTIFICATION_UI_USERS", 'column', "ORG_GROUP_DN";
--------------------------------------------------------
--  DDL for Index NOTIFICATION_UI_USERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NOTIFICATION_UI_USERS_PK" ON "NOTIFICATION_UI_USERS" ("UUN") ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATION_UI_USERS
--------------------------------------------------------

--  ALTER TABLE "NOTIFICATION_UI_USERS" MODIFY ("UUN" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_UI_USERS" ADD CONSTRAINT "NOTIFICATION_UI_USERS_PK" PRIMARY KEY ("UUN");


Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('hsun1','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('bowers2','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('mwarner2','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('aheyn','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('vgalt','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('kstirlin','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('swoodger','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('awheavil','ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('escott12','ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('DenyAccess','ou=CSG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('cgreer2',null);
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('ggranum',null);
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('melder2','ou=ISG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk');
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('tanyah',null);
Insert into NOTIFICATION_UI_USERS (UUN,ORG_GROUP_DN) values ('v1cmehaf',null);




--------------------------------------------------------
--  DDL for Table NOTIFICATION_UI_USER_ROLES
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION_UI_USER_ROLES"
   ("UUN" VARCHAR(64) NOT NULL,
	"ROLE_CODE" VARCHAR(10) NOT NULL
   )  ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'uun assigned the role', 'user', user_name(), 'table', "NOTIFICATION_UI_USER_ROLES", 'column', "UUN";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'role assigned to the uun', 'user', user_name(), 'table', "NOTIFICATION_UI_USER_ROLES", 'column', "ROLE_CODE";
--------------------------------------------------------
--  DDL for Index NOTIFICATION_UI_USER_ROLES_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "NOTIFICATION_UI_USER_ROLES_PK" ON "NOTIFICATION_UI_USER_ROLES" ("UUN", "ROLE_CODE") ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATION_UI_USER_ROLES
--------------------------------------------------------

--  ALTER TABLE "NOTIFICATION_UI_USER_ROLES" MODIFY ("UUN" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_UI_USER_ROLES" MODIFY ("ROLE_CODE" NOT NULL ENABLE);
  ALTER TABLE "NOTIFICATION_UI_USER_ROLES" ADD CONSTRAINT "NOTIFICATION_UI_USER_ROLES_PK" PRIMARY KEY ("UUN", "ROLE_CODE");




Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('bowers2','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('bowers2','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('bowers2','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('bowers2','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('bowers2','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('bowers2','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('vgalt','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('vgalt','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('vgalt','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('vgalt','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('kstirlin','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('kstirlin','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('kstirlin','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('kstirlin','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('kstirlin','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('swoodger','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('swoodger','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('swoodger','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('swoodger','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('swoodger','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('swoodger','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('mwarner2','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('mwarner2','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('mwarner2','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('mwarner2','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('mwarner2','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('mwarner2','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('kstirlin','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('awheavil','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('awheavil','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('awheavil','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('awheavil','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('awheavil','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('awheavil','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('escott12','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('escott12','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('escott12','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('escott12','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('escott12','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('escott12','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('DenyAccess','DENYACCESS');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('cgreer2','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('cgreer2','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('cgreer2','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('cgreer2','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('cgreer2','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('cgreer2','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('v1cmehaf','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('v1cmehaf','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('v1cmehaf','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('v1cmehaf','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('melder2','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('melder2','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('melder2','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('melder2','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('melder2','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('melder2','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('ggranum','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('ggranum','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('ggranum','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('ggranum','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('ggranum','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('ggranum','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('tanyah','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('tanyah','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('tanyah','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('tanyah','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('tanyah','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('tanyah','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('hsun1','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('hsun1','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('hsun1','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('hsun1','SYSSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('hsun1','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('hsun1','USRSUPPORT');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('aheyn','EMERGENCY');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('aheyn','GROUP');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('aheyn','SUPERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('aheyn','USERADMIN');
Insert into NOTIFICATION_UI_USER_ROLES (UUN,ROLE_CODE) values ('aheyn','USRSUPPORT');



--------------------------------------------------------
--  DDL for Table NOTIFICATION_PUSH_STATUS
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION_PUSH_STATUS"
   ("NOTIFICATION_ID" VARCHAR(32) NOT NULL,
	"SUBSCRIBER_ID" VARCHAR(32) NOT NULL,
	"PUSH_STATUS" VARCHAR(1) NOT NULL,
	"LAST_UPDATED" DATETIME NOT NULL DEFAULT GETDATE()
   ) ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for the notification', 'user', user_name(), 'table', "NOTIFICATION_PUSH_STATUS", 'column', "NOTIFICATION_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for the subscriber', 'user', user_name(), 'table', "NOTIFICATION_PUSH_STATUS", 'column', "SUBSCRIBER_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', '(P)ending or (S)ent', 'user', user_name(), 'table', "NOTIFICATION_PUSH_STATUS", 'column', "PUSH_STATUS";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Last updated date', 'user', user_name(), 'table', "NOTIFICATION_PUSH_STATUS", 'column', "LAST_UPDATED";
----------------------------------------------------------
--  DDL for Index NOTIFICATION_PUSH_STATUS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "NOTIFICATION_PUSH_STATUS_PK" ON "NOTIFICATION_PUSH_STATUS" ("NOTIFICATION_ID", "SUBSCRIBER_ID")  ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATION_PUSH_STATUS
--------------------------------------------------------

--  ALTER TABLE "NOTIFICATION_PUSH_STATUS" MODIFY ("NOTIFICATION_ID" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_PUSH_STATUS" MODIFY ("SUBSCRIBER_ID" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_PUSH_STATUS" MODIFY ("PUSH_STATUS" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_PUSH_STATUS" MODIFY ("LAST_UPDATED" NOT NULL ENABLE);
  ALTER TABLE "NOTIFICATION_PUSH_STATUS" ADD CONSTRAINT "NOTIFICATION_PUSH_STATUS_PK" PRIMARY KEY ("NOTIFICATION_ID", "SUBSCRIBER_ID");



--------------------------------------------------------
--  DDL for Table NOTIFICATION_ERRORS
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION_ERRORS"
   ("ERROR_ID" VARCHAR(32) PRIMARY KEY NOT NULL,
	"ERROR_CODE" VARCHAR(32) NOT NULL,
	"ERROR_DESCRIPTION" NVARCHAR(1024) NOT NULL,
	"ERROR_DATE" DATETIME NOT NULL DEFAULT GETDATE()
   )  ;

--   EXECUTE sp_addextendedproperty 'MS_Description', 'Unique identifier for the error', 'user', user_name(), 'table', "NOTIFICATION_ERRORS", 'column', "ERROR_ID";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Error code', 'user', user_name(), 'table', "NOTIFICATION_ERRORS", 'column', "ERROR_CODE";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Error description', 'user', user_name(), 'table', "NOTIFICATION_ERRORS", 'column', "ERROR_DESCRIPTION";
--   EXECUTE sp_addextendedproperty 'MS_Description', 'Date of error', 'user', user_name(), 'table', "NOTIFICATION_ERRORS", 'column', "ERROR_DATE";
--------------------------------------------------------
--  DDL for Index NOTIFICATION_ERRORS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NOTIFICATION_ERRORS_PK" ON "NOTIFICATION_ERRORS" ("ERROR_ID") ;
--------------------------------------------------------
--  Constraints for Table NOTIFICATION_ERRORS
--------------------------------------------------------

--  ALTER TABLE "NOTIFICATION_ERRORS" MODIFY ("ERROR_ID" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_ERRORS" MODIFY ("ERROR_CODE" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_ERRORS" MODIFY ("ERROR_DESCRIPTION" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_ERRORS" MODIFY ("ERROR_DATE" NOT NULL ENABLE);
--  ALTER TABLE "NOTIFICATION_ERRORS" ADD CONSTRAINT "NOTIFICATION_ERRORS_PK" PRIMARY KEY ("ERROR_ID");



--------------------------------------------------------
--  DDL for Table DATABASECHANGELOG
--------------------------------------------------------

  CREATE TABLE "DATABASECHANGELOG"
   ("ID" VARCHAR(255) NOT NULL,
	"AUTHOR" VARCHAR(255) NOT NULL,
	"FILENAME" VARCHAR(255) NOT NULL,
	"DATEEXECUTED" DATETIME2 (6) NOT NULL,
	"ORDEREXECUTED" BIGINT NOT NULL,
	"EXECTYPE" VARCHAR(10) NOT NULL,
	"MD5SUM" VARCHAR(35),
	"DESCRIPTION" VARCHAR(255),
	"COMMENTS" VARCHAR(255),
	"TAG" VARCHAR(255),
	"LIQUIBASE" VARCHAR(20)
   ) ;
--------------------------------------------------------
--  Constraints for Table DATABASECHANGELOG
--------------------------------------------------------

--  ALTER TABLE "DATABASECHANGELOG" MODIFY ("ID" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOG" MODIFY ("AUTHOR" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOG" MODIFY ("FILENAME" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOG" MODIFY ("DATEEXECUTED" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOG" MODIFY ("ORDEREXECUTED" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOG" MODIFY ("EXECTYPE" NOT NULL ENABLE);


--------------------------------------------------------
--  DDL for Table DATABASECHANGELOGLOCK
--------------------------------------------------------

  CREATE TABLE "DATABASECHANGELOGLOCK"
   ("ID" BIGINT PRIMARY KEY NOT NULL,
	"LOCKED" SMALLINT NOT NULL,
	"LOCKGRANTED" DATETIME2 (6),
	"LOCKEDBY" VARCHAR(255)
   );
--------------------------------------------------------
--  DDL for Index PK_DATABASECHANGELOGLOCK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_DATABASECHANGELOGLOCK" ON "DATABASECHANGELOGLOCK" ("ID") ;
--------------------------------------------------------
--  Constraints for Table DATABASECHANGELOGLOCK
--------------------------------------------------------

--  ALTER TABLE "DATABASECHANGELOGLOCK" MODIFY ("ID" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOGLOCK" MODIFY ("LOCKED" NOT NULL ENABLE);
--  ALTER TABLE "DATABASECHANGELOGLOCK" ADD CONSTRAINT "PK_DATABASECHANGELOGLOCK" PRIMARY KEY ("ID");



------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------







--------------------------------------------------------
--  DDL for Table QRTZ_JOB_DETAILS
--------------------------------------------------------

  CREATE TABLE "QRTZ_JOB_DETAILS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"JOB_NAME" VARCHAR(200) NOT NULL,
	"JOB_GROUP" VARCHAR(200) NOT NULL,
	"DESCRIPTION" VARCHAR(250),
	"JOB_CLASS_NAME" VARCHAR(250) NOT NULL,
	"IS_DURABLE" VARCHAR(1) NOT NULL,
	"IS_NONCONCURRENT" VARCHAR(1) NOT NULL,
	"IS_UPDATE_DATA" VARCHAR(1) NOT NULL,
	"REQUESTS_RECOVERY" VARCHAR(1) NOT NULL,
	"JOB_DATA" VARBINARY(max)
   )  ;
--------------------------------------------------------
--  DDL for Index QRTZ_JOB_DETAILS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_JOB_DETAILS_PK" ON "QRTZ_JOB_DETAILS" ("SCHED_NAME", "JOB_NAME", "JOB_GROUP")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_J_REQ_RECOVERY
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_J_REQ_RECOVERY" ON "QRTZ_JOB_DETAILS" ("SCHED_NAME", "REQUESTS_RECOVERY")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_J_GRP
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_J_GRP" ON "QRTZ_JOB_DETAILS" ("SCHED_NAME", "JOB_GROUP")  ;
--------------------------------------------------------
--  Constraints for Table QRTZ_JOB_DETAILS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("JOB_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("JOB_GROUP" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("JOB_CLASS_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("IS_DURABLE" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("IS_NONCONCURRENT" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("IS_UPDATE_DATA" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_JOB_DETAILS" MODIFY ("REQUESTS_RECOVERY" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_JOB_DETAILS" ADD CONSTRAINT "QRTZ_JOB_DETAILS_PK" PRIMARY KEY ("SCHED_NAME", "JOB_NAME", "JOB_GROUP");











--------------------------------------------------------
--  DDL for Table QRTZ_TRIGGERS
--------------------------------------------------------

  CREATE TABLE "QRTZ_TRIGGERS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"TRIGGER_NAME" VARCHAR(200) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL,
	"JOB_NAME" VARCHAR(200) NOT NULL,
	"JOB_GROUP" VARCHAR(200) NOT NULL,
	"DESCRIPTION" VARCHAR(250),
	"NEXT_FIRE_TIME" BIGINT,
	"PREV_FIRE_TIME" BIGINT,
	"PRIORITY" BIGINT,
	"TRIGGER_STATE" VARCHAR(16) NOT NULL,
	"TRIGGER_TYPE" VARCHAR(8) NOT NULL,
	"START_TIME" BIGINT NOT NULL,
	"END_TIME" BIGINT,
	"CALENDAR_NAME" VARCHAR(200),
	"MISFIRE_INSTR" SMALLINT,
	"JOB_DATA" VARBINARY(max)
   ) ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_J
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_J" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "JOB_NAME", "JOB_GROUP")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_JG
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_JG" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "JOB_GROUP")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_C
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_C" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "CALENDAR_NAME") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_G
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_G" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_GROUP")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_STATE
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_STATE" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_STATE")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_N_STATE
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_N_STATE" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP", "TRIGGER_STATE")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_N_G_STATE
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_N_G_STATE" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_GROUP", "TRIGGER_STATE")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_NEXT_FIRE_TIME
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_NEXT_FIRE_TIME" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "NEXT_FIRE_TIME")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_NFT_ST
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_NFT_ST" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_STATE", "NEXT_FIRE_TIME") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_NFT_MISFIRE
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_NFT_MISFIRE" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "MISFIRE_INSTR", "NEXT_FIRE_TIME") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_NFT_ST_MISFIRE
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_NFT_ST_MISFIRE" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "MISFIRE_INSTR", "NEXT_FIRE_TIME", "TRIGGER_STATE")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_T_NFT_ST_MISFIRE_GRP
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_T_NFT_ST_MISFIRE_GRP" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "MISFIRE_INSTR", "NEXT_FIRE_TIME", "TRIGGER_GROUP", "TRIGGER_STATE") ;
--------------------------------------------------------
--  DDL for Index QRTZ_TRIGGERS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_TRIGGERS_PK" ON "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP")  ;
--------------------------------------------------------
--  Constraints for Table QRTZ_TRIGGERS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("TRIGGER_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("JOB_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("JOB_GROUP" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("TRIGGER_STATE" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("TRIGGER_TYPE" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_TRIGGERS" MODIFY ("START_TIME" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_TRIGGERS" ADD CONSTRAINT "QRTZ_TRIGGERS_PK" PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");
--------------------------------------------------------
--  Ref Constraints for Table QRTZ_TRIGGERS
--------------------------------------------------------

  ALTER TABLE "QRTZ_TRIGGERS" ADD CONSTRAINT "QRTZ_TRIGGER_TO_JOBS_FK" FOREIGN KEY ("SCHED_NAME", "JOB_NAME", "JOB_GROUP")
	  REFERENCES "QRTZ_JOB_DETAILS" ("SCHED_NAME", "JOB_NAME", "JOB_GROUP");





















--------------------------------------------------------
--  DDL for Table QRTZ_BLOB_TRIGGERS
--------------------------------------------------------

  CREATE TABLE "QRTZ_BLOB_TRIGGERS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"TRIGGER_NAME" VARCHAR(200) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL,
	"BLOB_DATA" VARBINARY(max)
   ) ;
--------------------------------------------------------
--  DDL for Index QRTZ_BLOB_TRIG_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_BLOB_TRIG_PK" ON "QRTZ_BLOB_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP") ;
--------------------------------------------------------
--  Constraints for Table QRTZ_BLOB_TRIGGERS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_BLOB_TRIGGERS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_BLOB_TRIGGERS" MODIFY ("TRIGGER_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_BLOB_TRIGGERS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_BLOB_TRIGGERS" ADD CONSTRAINT "QRTZ_BLOB_TRIG_PK" PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");
--------------------------------------------------------
--  Ref Constraints for Table QRTZ_BLOB_TRIGGERS
--------------------------------------------------------

  ALTER TABLE "QRTZ_BLOB_TRIGGERS" ADD CONSTRAINT "QRTZ_BLOB_TRIG_TO_TRIG_FK" FOREIGN KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP")
	  REFERENCES "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");






--------------------------------------------------------
--  DDL for Table QRTZ_CALENDARS
--------------------------------------------------------

  CREATE TABLE "QRTZ_CALENDARS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"CALENDAR_NAME" VARCHAR(200) NOT NULL,
	"CALENDAR" VARBINARY(max) NOT NULL
   ) ;
--------------------------------------------------------
--  DDL for Index QRTZ_CALENDARS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_CALENDARS_PK" ON "QRTZ_CALENDARS" ("SCHED_NAME", "CALENDAR_NAME")  ;
--------------------------------------------------------
--  Constraints for Table QRTZ_CALENDARS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_CALENDARS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_CALENDARS" MODIFY ("CALENDAR_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_CALENDARS" MODIFY ("CALENDAR" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_CALENDARS" ADD CONSTRAINT "QRTZ_CALENDARS_PK" PRIMARY KEY ("SCHED_NAME", "CALENDAR_NAME");






--------------------------------------------------------
--  DDL for Table QRTZ_CRON_TRIGGERS
--------------------------------------------------------

  CREATE TABLE "QRTZ_CRON_TRIGGERS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"TRIGGER_NAME" VARCHAR(200) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL,
	"CRON_EXPRESSION" VARCHAR(120) NOT NULL,
	"TIME_ZONE_ID" VARCHAR(80)
   )  ;
--------------------------------------------------------
--  DDL for Index QRTZ_CRON_TRIG_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_CRON_TRIG_PK" ON "QRTZ_CRON_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP") ;
--------------------------------------------------------
--  Constraints for Table QRTZ_CRON_TRIGGERS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_CRON_TRIGGERS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_CRON_TRIGGERS" MODIFY ("TRIGGER_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_CRON_TRIGGERS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_CRON_TRIGGERS" MODIFY ("CRON_EXPRESSION" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_CRON_TRIGGERS" ADD CONSTRAINT "QRTZ_CRON_TRIG_PK" PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");
--------------------------------------------------------
--  Ref Constraints for Table QRTZ_CRON_TRIGGERS
--------------------------------------------------------

  ALTER TABLE "QRTZ_CRON_TRIGGERS" ADD CONSTRAINT "QRTZ_CRON_TRIG_TO_TRIG_FK" FOREIGN KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP")
	  REFERENCES "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");








--------------------------------------------------------
--  DDL for Table QRTZ_FIRED_TRIGGERS
--------------------------------------------------------

  CREATE TABLE "QRTZ_FIRED_TRIGGERS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"ENTRY_ID" VARCHAR(95) NOT NULL,
	"TRIGGER_NAME" VARCHAR(200) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL,
	"INSTANCE_NAME" VARCHAR(200) NOT NULL,
	"FIRED_TIME" BIGINT NOT NULL,
	"SCHED_TIME" BIGINT NOT NULL,
	"PRIORITY" BIGINT NOT NULL,
	"STATE" VARCHAR(16) NOT NULL,
	"JOB_NAME" VARCHAR(200),
	"JOB_GROUP" VARCHAR(200),
	"IS_NONCONCURRENT" VARCHAR(1),
	"REQUESTS_RECOVERY" VARCHAR(1)
   ) ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_FT_TRIG_INST_NAME
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_FT_TRIG_INST_NAME" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "INSTANCE_NAME") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_FT_INST_JOB_REQ_RCVRY
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_FT_INST_JOB_REQ_RCVRY" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "INSTANCE_NAME", "REQUESTS_RECOVERY")  ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_FT_J_G
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_FT_J_G" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "JOB_NAME", "JOB_GROUP") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_FT_JG
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_FT_JG" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "JOB_GROUP") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_FT_T_G
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_FT_T_G" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP") ;
--------------------------------------------------------
--  DDL for Index IDX_QRTZ_FT_TG
--------------------------------------------------------

  CREATE INDEX "IDX_QRTZ_FT_TG" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "TRIGGER_GROUP")  ;
--------------------------------------------------------
--  DDL for Index QRTZ_FIRED_TRIGGER_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_FIRED_TRIGGER_PK" ON "QRTZ_FIRED_TRIGGERS" ("SCHED_NAME", "ENTRY_ID")  ;
--------------------------------------------------------
--  Constraints for Table QRTZ_FIRED_TRIGGERS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("ENTRY_ID" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("TRIGGER_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("INSTANCE_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("FIRED_TIME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("SCHED_TIME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("PRIORITY" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_FIRED_TRIGGERS" MODIFY ("STATE" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_FIRED_TRIGGERS" ADD CONSTRAINT "QRTZ_FIRED_TRIGGER_PK" PRIMARY KEY ("SCHED_NAME", "ENTRY_ID");





--------------------------------------------------------
--  DDL for Table QRTZ_LOCKS
--------------------------------------------------------

  CREATE TABLE "QRTZ_LOCKS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"LOCK_NAME" VARCHAR(40) NOT NULL
   )  ;
--------------------------------------------------------
--  DDL for Index QRTZ_LOCKS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_LOCKS_PK" ON "QRTZ_LOCKS" ("SCHED_NAME", "LOCK_NAME");
--------------------------------------------------------
--  Constraints for Table QRTZ_LOCKS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_LOCKS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_LOCKS" MODIFY ("LOCK_NAME" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_LOCKS" ADD CONSTRAINT "QRTZ_LOCKS_PK" PRIMARY KEY ("SCHED_NAME", "LOCK_NAME");





--------------------------------------------------------
--  DDL for Table QRTZ_PAUSED_TRIGGER_GRPS
--------------------------------------------------------

  CREATE TABLE "QRTZ_PAUSED_TRIGGER_GRPS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL
   )  ;
--------------------------------------------------------
--  DDL for Index QRTZ_PAUSED_TRIG_GRPS_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_PAUSED_TRIG_GRPS_PK" ON "QRTZ_PAUSED_TRIGGER_GRPS" ("SCHED_NAME", "TRIGGER_GROUP")  ;
--------------------------------------------------------
--  Constraints for Table QRTZ_PAUSED_TRIGGER_GRPS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_PAUSED_TRIGGER_GRPS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_PAUSED_TRIGGER_GRPS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_PAUSED_TRIGGER_GRPS" ADD CONSTRAINT "QRTZ_PAUSED_TRIG_GRPS_PK" PRIMARY KEY ("SCHED_NAME", "TRIGGER_GROUP");




--------------------------------------------------------
--  DDL for Table QRTZ_SCHEDULER_STATE
--------------------------------------------------------

  CREATE TABLE "QRTZ_SCHEDULER_STATE"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"INSTANCE_NAME" VARCHAR(200) NOT NULL,
	"LAST_CHECKIN_TIME" BIGINT NOT NULL,
	"CHECKIN_INTERVAL" BIGINT NOT NULL
   )  ;
--------------------------------------------------------
--  DDL for Index QRTZ_SCHEDULER_STATE_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_SCHEDULER_STATE_PK" ON "QRTZ_SCHEDULER_STATE" ("SCHED_NAME", "INSTANCE_NAME");
--------------------------------------------------------
--  Constraints for Table QRTZ_SCHEDULER_STATE
--------------------------------------------------------

--  ALTER TABLE "QRTZ_SCHEDULER_STATE" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SCHEDULER_STATE" MODIFY ("INSTANCE_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SCHEDULER_STATE" MODIFY ("LAST_CHECKIN_TIME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SCHEDULER_STATE" MODIFY ("CHECKIN_INTERVAL" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_SCHEDULER_STATE" ADD CONSTRAINT "QRTZ_SCHEDULER_STATE_PK" PRIMARY KEY ("SCHED_NAME", "INSTANCE_NAME");





--------------------------------------------------------
--  DDL for Table QRTZ_SIMPLE_TRIGGERS
--------------------------------------------------------

  CREATE TABLE "QRTZ_SIMPLE_TRIGGERS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"TRIGGER_NAME" VARCHAR(200) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL,
	"REPEAT_COUNT" INT NOT NULL,
	"REPEAT_INTERVAL" BIGINT NOT NULL,
	"TIMES_TRIGGERED" BIGINT NOT NULL
   ) ;
--------------------------------------------------------
--  DDL for Index QRTZ_SIMPLE_TRIG_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_SIMPLE_TRIG_PK" ON "QRTZ_SIMPLE_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP") ;
--------------------------------------------------------
--  Constraints for Table QRTZ_SIMPLE_TRIGGERS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" MODIFY ("TRIGGER_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" MODIFY ("REPEAT_COUNT" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" MODIFY ("REPEAT_INTERVAL" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" MODIFY ("TIMES_TRIGGERED" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" ADD CONSTRAINT "QRTZ_SIMPLE_TRIG_PK" PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");
--------------------------------------------------------
--  Ref Constraints for Table QRTZ_SIMPLE_TRIGGERS
--------------------------------------------------------

  ALTER TABLE "QRTZ_SIMPLE_TRIGGERS" ADD CONSTRAINT "QRTZ_SIMPLE_TRIG_TO_TRIG_FK" FOREIGN KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP")
	  REFERENCES "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");







--------------------------------------------------------
--  DDL for Table QRTZ_SIMPROP_TRIGGERS
--------------------------------------------------------

  CREATE TABLE "QRTZ_SIMPROP_TRIGGERS"
   ("SCHED_NAME" VARCHAR(120) NOT NULL,
	"TRIGGER_NAME" VARCHAR(200) NOT NULL,
	"TRIGGER_GROUP" VARCHAR(200) NOT NULL,
	"STR_PROP_1" VARCHAR(512),
	"STR_PROP_2" VARCHAR(512),
	"STR_PROP_3" VARCHAR(512),
	"INT_PROP_1" BIGINT,
	"INT_PROP_2" BIGINT,
	"LONG_PROP_1" BIGINT,
	"LONG_PROP_2" BIGINT,
	"DEC_PROP_1" DECIMAL(13,4),
	"DEC_PROP_2" DECIMAL(13,4),
	"BOOL_PROP_1" VARCHAR(1),
	"BOOL_PROP_2" VARCHAR(1)
   )  ;
--------------------------------------------------------
--  DDL for Index QRTZ_SIMPROP_TRIG_PK
--------------------------------------------------------

--  CREATE UNIQUE INDEX "QRTZ_SIMPROP_TRIG_PK" ON "QRTZ_SIMPROP_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP")  ;
--------------------------------------------------------
--  Constraints for Table QRTZ_SIMPROP_TRIGGERS
--------------------------------------------------------

--  ALTER TABLE "QRTZ_SIMPROP_TRIGGERS" MODIFY ("SCHED_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPROP_TRIGGERS" MODIFY ("TRIGGER_NAME" NOT NULL ENABLE);
--  ALTER TABLE "QRTZ_SIMPROP_TRIGGERS" MODIFY ("TRIGGER_GROUP" NOT NULL ENABLE);
  ALTER TABLE "QRTZ_SIMPROP_TRIGGERS" ADD CONSTRAINT "QRTZ_SIMPROP_TRIG_PK" PRIMARY KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");
--------------------------------------------------------
--  Ref Constraints for Table QRTZ_SIMPROP_TRIGGERS
--------------------------------------------------------

  ALTER TABLE "QRTZ_SIMPROP_TRIGGERS" ADD CONSTRAINT "QRTZ_SIMPROP_TRIG_TO_TRIG_FK" FOREIGN KEY ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP")
	  REFERENCES "QRTZ_TRIGGERS" ("SCHED_NAME", "TRIGGER_NAME", "TRIGGER_GROUP");



Insert into QRTZ_JOB_DETAILS (SCHED_NAME,JOB_NAME,JOB_GROUP,DESCRIPTION,JOB_CLASS_NAME,IS_DURABLE,IS_NONCONCURRENT,IS_UPDATE_DATA,REQUESTS_RECOVERY) values ('schedulerFactoryBean','examSchedulerPullJobDetail','DEFAULT',null,'uk.ac.ed.notify.job.ExamSchedulerPullJob','1','1','0','0');
Insert into QRTZ_JOB_DETAILS (SCHED_NAME,JOB_NAME,JOB_GROUP,DESCRIPTION,JOB_CLASS_NAME,IS_DURABLE,IS_NONCONCURRENT,IS_UPDATE_DATA,REQUESTS_RECOVERY) values ('schedulerFactoryBean','notificationTidyupJobDetail','DEFAULT',null,'uk.ac.ed.notify.job.NotificationTidyupJob','1','1','0','0');
Insert into QRTZ_JOB_DETAILS (SCHED_NAME,JOB_NAME,JOB_GROUP,DESCRIPTION,JOB_CLASS_NAME,IS_DURABLE,IS_NONCONCURRENT,IS_UPDATE_DATA,REQUESTS_RECOVERY) values ('schedulerFactoryBean','office365PullJobDetail','DEFAULT',null,'uk.ac.ed.notify.job.Office365PullJob','1','1','0','0');


Insert into QRTZ_LOCKS (SCHED_NAME,LOCK_NAME) values ('schedulerFactoryBean','STATE_ACCESS');
Insert into QRTZ_LOCKS (SCHED_NAME,LOCK_NAME) values ('schedulerFactoryBean','TRIGGER_ACCESS');
