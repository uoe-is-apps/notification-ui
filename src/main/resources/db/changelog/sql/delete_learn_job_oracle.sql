delete from QRTZ_FIRED_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail');

delete from QRTZ_SIMPLE_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail');

delete from QRTZ_TRIGGERS where trigger_name  in ('learnPullJobDetail','learnSystemPullJobDetail');

delete from QRTZ_JOB_DETAILS where job_name in ('learnPullJobDetail','learnSystemPullJobDetail');

commit;