package uk.ac.ed.notify.service;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationUser;
import uk.ac.ed.notify.entity.NotificationUserPK;
import uk.ac.ed.notify.learn.entity.Announcements;
import uk.ac.ed.notify.learn.entity.CourseUsers;
import uk.ac.ed.notify.learn.entity.Tasks;
import uk.ac.ed.notify.learn.repository.LearnAnnouncementRepository;
import uk.ac.ed.notify.learn.repository.LearnCourseUserRepository;
import uk.ac.ed.notify.learn.repository.LearnTaskRepository;
import uk.ac.ed.notify.repository.NotificationRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import uk.ac.ed.notify.entity.AuditActions;

/**
 * Created by rgood on 06/11/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class LearnServiceTest {

	@Mock
	private LearnTaskRepository learnTaskRepository;
	
	@Mock
	LearnCourseUserRepository learnCourseUserRepository;
	
	@Mock
	LearnAnnouncementRepository learnAnnouncementRepository;
	
	@InjectMocks
    private LearnService learnService;

    Date startDate;
    Date endDate;
    
    @Before
    public void setup()
    {
    	MockitoAnnotations.initMocks(this);
    	
    	ReflectionTestUtils.setField(learnService, "notificationUrl", "http://example.com");
    	
    	startDate = new Date(new Date().getTime() - 2786567);
    	endDate = new Date(new Date().getTime() + 278656709);
    }

    @After
    public void cleanup()
    {
        
    }

       
    @Test 
    public void testProcessLearnTasks() {
    	
    	List<Notification> existingNotificationsList = new ArrayList<Notification>();
    	
    	Notification notification = new Notification();
    	notification.setNotificationId("sus-guid-id");
        notification.setBody("<p>Existing Learn Task Notification</p>");
        notification.setTopic("Learn Task");
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("Learn Task10");
        notification.setTitle("Task 1");
        notification.setUrl("http://example.com");
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);
        notification.setLastUpdated(new Date());
        
        List<NotificationUser> users = new ArrayList<NotificationUser>();
        
        NotificationUser user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","bolek"));
        users.add(user);
        
        user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","thumper"));
        users.add(user);
        
        user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","donald"));
        users.add(user);
        
        notification.setNotificationUsers(users);
        
        existingNotificationsList.add(notification);
        
        Map<Integer, String> userIdNamePair = new HashMap<Integer, String>();
        userIdNamePair.put(1, "bolek");
        userIdNamePair.put(2, "thumper");
        userIdNamePair.put(3, "donald");
        userIdNamePair.put(4, "jerry");
        
        Map<String,List<Notification>> actionsCache = new HashMap<String, List<Notification>>();
        actionsCache.put(AuditActions.CREATE_NOTIFICATION, new ArrayList<Notification>());
        actionsCache.put(AuditActions.UPDATE_NOTIFICATION, new ArrayList<Notification>());
        
        List<String> processedLearnNotificationsList = new ArrayList<String>();
        
        List<Tasks> taskList = new ArrayList<Tasks>();
        Tasks task = new Tasks();
        task.setCrsmainPk1(101);
        task.setDescription("<p>Due next week</p>");
        task.setDueDate(endDate);
        task.setPk1(10);
        task.setPriority("H");
        task.setSubject("Learn Task Due Next Week");
        task.setTaskType("C");
        taskList.add(task);
        
        task = new Tasks();
        task.setCrsmainPk1(101);
        task.setDescription("<p>Due next month</p>");
        task.setDueDate(endDate);
        task.setPk1(20);
        task.setPriority("H");
        task.setSubject("Learn Task Due Next Month");
        task.setTaskType("C");
        taskList.add(task);
        
        when(learnTaskRepository.findTasks()).thenReturn(taskList);
        
        List<CourseUsers> courseUserList = new ArrayList<CourseUsers>();
        CourseUsers courseUser = new CourseUsers();
        courseUser.setCrsmainPk1(101);
        courseUser.setPk1(1);
        courseUser.setRowStatus(0);
        courseUser.setUsersPk1(1);
        courseUserList.add(courseUser);
        
        courseUser = new CourseUsers();
        courseUser.setCrsmainPk1(101);
        courseUser.setPk1(2);
        courseUser.setRowStatus(0);
        courseUser.setUsersPk1(2);
        courseUserList.add(courseUser);
        
        when(learnCourseUserRepository.findByCrsmainPk1(anyInt())).thenReturn(courseUserList);
        
        learnService.processLearnTasks(existingNotificationsList, userIdNamePair, actionsCache, processedLearnNotificationsList);
        
        assertThat(actionsCache.get(AuditActions.UPDATE_NOTIFICATION).size(), is(1));
        assertThat(actionsCache.get(AuditActions.CREATE_NOTIFICATION).size(), is(1));	
        
        Notification updateNotification = actionsCache.get(AuditActions.UPDATE_NOTIFICATION).get(0);
        assertThat(updateNotification.getNotificationId(), is(notNullValue()));
        assertThat(updateNotification.getNotificationId(), is("sus-guid-id"));
        assertThat(updateNotification.getPublisherNotificationId(), is("Learn Task10"));
        
        assertThat(updateNotification.getNotificationUsers().size(), is(2));
        assertThat(Arrays.asList(updateNotification.getNotificationUsers().get(0).getId().getUun(), updateNotification.getNotificationUsers().get(1).getId().getUun()), hasItems("bolek", "thumper"));
    
        
        assertThat(actionsCache.get(AuditActions.CREATE_NOTIFICATION).get(0).getPublisherNotificationId(), is("Learn Task20"));
        assertThat(processedLearnNotificationsList, hasItems("Learn Task10","Learn Task20"));
    }
    
    @Test
    public void testProcessSystemAnnouncements() {
    	
    	List<Announcements> announcementList = new ArrayList<Announcements>();
    	Announcements announcements = new Announcements();
    	announcements.setPk1(1);
    	announcements.setEndDate(endDate);
    	announcements.setSubject("System announcement Monday");
    	announcements.setStartDate(startDate);
    	announcements.setCrsmainPk1(1);
    	announcements.setAnnouncement("Attention, Ola ola");
    	announcements.setAnnouncementType("S");
    	announcementList.add(announcements);
    	
    	announcements = new Announcements();
    	announcements.setPk1(2);
    	announcements.setEndDate(endDate);
    	announcements.setSubject("System announcement Tuesday");
    	announcements.setStartDate(startDate);
    	announcements.setCrsmainPk1(1);
    	announcements.setAnnouncement("Attention, Achtung achtung");
    	announcements.setAnnouncementType("S");
    	announcementList.add(announcements);
    	
    	when(learnAnnouncementRepository.findSystemAnnouncements()).thenReturn(announcementList);
    	
    	List<Notification> existingNotificationsList = new ArrayList<Notification>();
    	
    	Notification notification = new Notification();
    	notification.setNotificationId("sus-guid-id");
        notification.setBody("<p>Existing System Notification</p>");
        notification.setTopic("Learn System Announcement");
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("Learn System Announcement1");
        notification.setTitle("Yo");
        notification.setUrl("http://example.com");
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);
        notification.setLastUpdated(new Date());
        
        List<NotificationUser> users = new ArrayList<NotificationUser>();
        
        NotificationUser user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","bolek"));
        users.add(user);
        
        user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","thumper"));
        users.add(user);
        
        user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","donald"));
        users.add(user);
        
        notification.setNotificationUsers(users);
        
        existingNotificationsList.add(notification);
        
        Map<Integer, String> userIdNamePair = new HashMap<Integer, String>();
        userIdNamePair.put(1, "bolek");
        userIdNamePair.put(2, "thumper");
        userIdNamePair.put(3, "donald");
        userIdNamePair.put(4, "jerry");
        
        Map<String,List<Notification>> actionsCache = new HashMap<String, List<Notification>>();
        actionsCache.put(AuditActions.CREATE_NOTIFICATION, new ArrayList<Notification>());
        actionsCache.put(AuditActions.UPDATE_NOTIFICATION, new ArrayList<Notification>());
        
        List<String> processedLearnNotificationsList = new ArrayList<String>();
        
        learnService.processSystemAnnouncements(existingNotificationsList, userIdNamePair, actionsCache, processedLearnNotificationsList);
    	
        assertThat(actionsCache.get(AuditActions.UPDATE_NOTIFICATION).size(), is(1));
        assertThat(actionsCache.get(AuditActions.CREATE_NOTIFICATION).size(), is(1));
        
        Notification cachedNotification = actionsCache.get(AuditActions.UPDATE_NOTIFICATION).get(0);
        assertThat(cachedNotification.getPublisherNotificationId(), is("Learn System Announcement1"));
        assertThat(cachedNotification.getNotificationUsers(), hasSize(4));
        
        cachedNotification = actionsCache.get(AuditActions.CREATE_NOTIFICATION).get(0);
        assertThat(cachedNotification.getPublisherNotificationId(), is("Learn System Announcement2"));
        assertThat(cachedNotification.getNotificationUsers(), hasSize(4));
        
        assertThat(processedLearnNotificationsList, hasItems("Learn System Announcement1","Learn System Announcement1"));
    }
    
    @Test
    public void testProcessCourseAnnouncements() {
    	
       List<Notification> existingNotificationsList = new ArrayList<Notification>();
    	
    	Notification notification = new Notification();
    	notification.setNotificationId("sus-guid-id");
        notification.setBody("<p>Class on</p>");
        notification.setTopic("Learn Course Announcement");
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("Learn Course Announcement1");
        notification.setTitle("Test");
        notification.setUrl("http://example.com");
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);
        notification.setLastUpdated(new Date());
        
        List<NotificationUser> users = new ArrayList<NotificationUser>();
        
        NotificationUser user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","bolek"));
        users.add(user);
        
        user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","thumper"));
        users.add(user);
        
        user = new NotificationUser();
        user.setNotification(notification);
        user.setId(new NotificationUserPK("sus-guid-id","donald"));
        users.add(user);
        
        notification.setNotificationUsers(users);
        
        existingNotificationsList.add(notification);
        
        Map<Integer, String> userIdNamePair = new HashMap<Integer, String>();
        userIdNamePair.put(1, "bolek");
        userIdNamePair.put(2, "thumper");
        userIdNamePair.put(3, "donald");
        userIdNamePair.put(4, "jerry");
        
        Map<String,List<Notification>> actionsCache = new HashMap<String, List<Notification>>();
        actionsCache.put(AuditActions.CREATE_NOTIFICATION, new ArrayList<Notification>());
        actionsCache.put(AuditActions.UPDATE_NOTIFICATION, new ArrayList<Notification>());
        
        List<String> processedLearnNotificationsList = new ArrayList<String>();
        
    	List<Announcements> announcementList = new ArrayList<Announcements>();
    	Announcements announcements = new Announcements();
    	announcements.setPk1(1);
    	announcements.setEndDate(endDate);
    	announcements.setSubject("Course announcement Networks");
    	announcements.setStartDate(startDate);
    	announcements.setCrsmainPk1(101);
    	announcements.setAnnouncement("Class cancelled");
    	announcements.setAnnouncementType("C");
    	announcementList.add(announcements);
    	
    	announcements = new Announcements();
    	announcements.setPk1(2);
    	announcements.setEndDate(endDate);
    	announcements.setSubject("Course announcement IT");
    	announcements.setStartDate(startDate);
    	announcements.setCrsmainPk1(101);
    	announcements.setAnnouncement("New resources available");
    	announcements.setAnnouncementType("C");
    	announcementList.add(announcements);
    	
    	when(learnAnnouncementRepository.findCourseAnnouncements()).thenReturn(announcementList);
    	
    	List<CourseUsers> courseUserList = new ArrayList<CourseUsers>();
        CourseUsers courseUser = new CourseUsers();
        courseUser.setCrsmainPk1(101);
        courseUser.setPk1(1);
        courseUser.setRowStatus(0);
        courseUser.setUsersPk1(1);
        courseUserList.add(courseUser);
        
        courseUser = new CourseUsers();
        courseUser.setCrsmainPk1(101);
        courseUser.setPk1(2);
        courseUser.setRowStatus(0);
        courseUser.setUsersPk1(2);
        courseUserList.add(courseUser);
        
        when(learnCourseUserRepository.findByCrsmainPk1(anyInt())).thenReturn(courseUserList);
    	
        learnService.processCourseAnnouncements(existingNotificationsList, userIdNamePair, actionsCache, processedLearnNotificationsList);
        
        assertThat(actionsCache.get(AuditActions.UPDATE_NOTIFICATION).size(), is(1));
        assertThat(actionsCache.get(AuditActions.CREATE_NOTIFICATION).size(), is(1));	
        
        Notification updateNotification = actionsCache.get(AuditActions.UPDATE_NOTIFICATION).get(0);
        assertThat(updateNotification.getNotificationId(), is(notNullValue()));
        assertThat(updateNotification.getPublisherNotificationId(), is("Learn Course Announcement1"));
        assertThat(updateNotification.getBody(), is("Class cancelled"));
        
        assertThat(updateNotification.getNotificationUsers().size(), is(2));
        assertThat(Arrays.asList(updateNotification.getNotificationUsers().get(0).getId().getUun(), updateNotification.getNotificationUsers().get(1).getId().getUun()), hasItems("bolek", "thumper"));
    
        assertThat(actionsCache.get(AuditActions.CREATE_NOTIFICATION).get(0).getPublisherNotificationId(), is("Learn Course Announcement2"));
        assertThat(processedLearnNotificationsList, hasItems("Learn Course Announcement1","Learn Course Announcement2"));
    }
    
}
