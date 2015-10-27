package uk.ac.ed.notify.learn.repository;

import java.util.List;
import uk.ac.ed.notify.learn.entity.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hsun1
 */
public interface LearnAnnouncementRepository extends JpaRepository<Announcements, Long>{
    List<Announcements> findCourseAnnouncements();        
    List<Announcements> findSystemAnnouncements();
}

