Insert into SUBSCRIBER_DETAILS (SUBSCRIBER_ID,SUBSCRIBER_DESCRIPTION,SUBSCRIBER_TYPE,STATUS,LAST_UPDATED) values ('myed','MyEd Notification Subscriber','Pull','A',sysdate);
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS,LAST_UPDATED) values (sys_guid(),'Learn System Announcement','myed','A',sysdate);
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS,LAST_UPDATED) values (sys_guid(),'Learn Assessment','myed','A',sysdate);
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS,LAST_UPDATED) values (sys_guid(),'Learn Course Announcement','myed','A',sysdate);
Insert into TOPIC_SUBSCRIPTIONS (SUBSCRIPTION_ID,TOPIC,SUBSCRIBER_ID,STATUS,LAST_UPDATED) values (sys_guid(),'Learn Task','myed','A',sysdate);