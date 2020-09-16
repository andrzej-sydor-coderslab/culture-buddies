package pl.coderslab.cultureBuddies.books;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import pl.coderslab.cultureBuddies.author.Author;
import pl.coderslab.cultureBuddies.buddies.BuddyBook;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"authors", "buddies"})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String title;
    @NotEmpty
    @ManyToMany
    @JoinTable(name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns= @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;
    @OneToMany(mappedBy = "book",
            orphanRemoval = true)
    private Set<BuddyBook> buddies = new HashSet<>();
    @NotNull
    @Column(unique = true)
    private String identifier;
    @URL
    private String thumbnailLink;

    public void addAuthor(Author author) {
        if (authors == null) {
            authors = new HashSet<>();
        }
        if (author != null) {
            authors.add(author);
        }
    }
    public Set<BuddyBook> getBuddies(){
        return Objects.requireNonNullElseGet(buddies, HashSet::new);
    }
}
