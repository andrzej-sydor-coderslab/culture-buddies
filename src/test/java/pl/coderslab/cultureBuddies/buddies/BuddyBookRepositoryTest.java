package pl.coderslab.cultureBuddies.buddies;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderslab.cultureBuddies.author.Author;
import pl.coderslab.cultureBuddies.books.Book;
import pl.coderslab.cultureBuddies.buddyBook.BuddyBook;
import pl.coderslab.cultureBuddies.buddyBook.BuddyBookRepository;
import pl.coderslab.cultureBuddies.city.City;
import pl.coderslab.cultureBuddies.setup.SetUpDatabaseService;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
@ActiveProfiles("test")
class BuddyBookRepositoryTest {
    @Autowired
    private BuddyBookRepository testObject;
    @Autowired
    private TestEntityManager testEm;

    @Test
    public void givenBuddyAndBook_whenBookAddedToBuddy_thenShouldBeAdded() {
        final City city = new City();
        city.setName("New York");
        final City savedCity = testEm.persist(city);
        Author author = Author.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .build();
        Book book = Book.builder()
                .title("Novel")
                .identifier("8381258162")
                .buddies(new HashSet<>())
                .authors(new HashSet<>()).build();
        Buddy buddy = Buddy.builder()
                .username("bestBuddy")
                .email("test@gmail.com")
                .name("Anna")
                .lastName("Kowalska")
                .city(savedCity)
                .password("annaKowalska")
                .books(new HashSet<>())
                .build();
        final Author savedAuthor = testEm.persist(author);
        testEm.persist(buddy);
        book.addAuthor(savedAuthor);
        testEm.persist(book);
        final BuddyBook expected = buddy.addBook(book);
        testEm.persist(expected);
        testEm.merge(buddy);
        testEm.flush();
        final Optional<BuddyBook> actual = testObject.findByBookAndBuddy(book, buddy);
        assertThat(actual.get(), is(expected));
    }
}