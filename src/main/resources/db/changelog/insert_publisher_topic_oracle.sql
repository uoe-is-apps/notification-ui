update NOTIFICATION_UI_USERS set ORG_GROUP_DN='ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk' where uun='hsun1';


insert into PUBLISHER_DETAILS (PUBLISHER_ID, PUBLISHER_DESCRIPTION, PUBLISHER_KEY, PUBLISHER_TYPE, STATUS, LAST_UPDATED)
values ('euclid',	'Euclid',	'004TFE5E177023ABE05642144F00F4DD', 'PUSH', 'A', sysdate);

insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS,LAST_UPDATED)
values ('Emergency',	'Emergency Topic',	'notify-ui',	'A', sysdate);

insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS,LAST_UPDATED)
values ('Group',	'Group Topic',	'notify-ui',	'A', sysdate);

insert into TOPIC_DETAILS (TOPIC_ID,TOPIC_DESCRIPTION,PUBLISHER_ID,STATUS,LAST_UPDATED)
values ('Student',	'Student Topic',	'euclid',	'A', sysdate);





