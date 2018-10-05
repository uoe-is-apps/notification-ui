delete from QRTZ_FIRED_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail', 'learnPullJobTrigger','learnSystemPullJobTrigger');

delete from QRTZ_SIMPLE_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail', 'learnPullJobTrigger','learnSystemPullJobTrigger');

delete from QRTZ_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail', 'learnPullJobTrigger','learnSystemPullJobTrigger');

delete from QRTZ_FIRED_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail', 'learnPullJobTrigger','learnSystemPullJobTrigger');

delete from QRTZ_JOB_DETAILS where job_name in ('learnPullJobDetail','learnSystemPullJobDetail', 'learnPullJobTrigger','learnSystemPullJobTrigger');

delete from NOTIFICATIONS where topic like 'Learn%';

delete from TOPIC_DETAILS where topic_id in ('Learn System Announcement','Learn Course Announcement','Learn Assessment','Learn Task');

delete from PUBLISHER_DETAILS where publisher_id in ('learn');

commit;