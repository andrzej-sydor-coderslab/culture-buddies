package pl.coderslab.cultureBuddies.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.cultureBuddies.buddies.Buddy;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.buddies WHERE e.buddy =?1")
    List<Event> findAllByBuddy(Buddy buddy);

    @Query("SELECT DISTINCT event FROM Event event LEFT JOIN FETCH event.buddies b JOIN event.buddies b2 where b2 = ?1")
    List<Event> findAllByBuddiesContains(Buddy buddy);

    @Query("SELECT DISTINCT event FROM Event event LEFT JOIN FETCH event.buddies b where event.id = ?1")
    Optional<Event> findEventWithBuddies(Long eventId);

    @Query("SELECT DISTINCT event FROM Event event LEFT JOIN FETCH event.buddies b where event.buddy.username LIKE CONCAT(?1,'%') " +
            "AND event.title LIKE CONCAT(?2,'%') AND event.address.city LIKE CONCAT(?3,'%')")
    List<Event> findByUsernameTitleAndCity(String keyUsername, String keyTitle, String keyCity);

    @Query("SELECT DISTINCT event FROM Event event LEFT JOIN FETCH event.buddies b where event.buddy.username LIKE CONCAT(?1,'%') " +
            "AND event.title LIKE CONCAT(?2,'%') AND event.address.city LIKE CONCAT(?3,'%') AND event.eventType.id = ?4")
    List<Event> findByUsernameTitleCityAndTypeId(String keyUsername, String keyTitle, String keyCity, Long typeId);

    @Query("SELECT DISTINCT event FROM Event event LEFT JOIN FETCH event.buddies b JOIN event.buddies b2 where b2 = ?1 AND event.id = ?2")
    Optional<Event> findEventByBuddies(Buddy principal, Long eventId);

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT e.*, b.* FROM events e " +
                    "LEFT JOIN buddies b on e.buddy_id = b.id " +
                    "LEFT JOIN buddies_relations br on b.id = br.buddy_id " +
                    "WHERE br.buddy_of_id = ?1 ORDER BY e.added DESC LIMIT ?2 ")
    List<Event> findRecentlyAddedEventsWithBuddies(Long buddyId, int recentlyLimit);

    List<Event> findFirst20ByOrderByDateDesc();

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT e.*, b.* FROM events e " +
                    "LEFT JOIN events_buddies eb on e.id = eb.event_id " +
                    "LEFT JOIN buddies b on e.buddy_id = b.id " +
                    "WHERE e.buddy_id =?1 ORDER BY e.added DESC LIMIT ?2 ")
    List<Event> findRecentWhereBuddyId(Long id, int limit);
}

