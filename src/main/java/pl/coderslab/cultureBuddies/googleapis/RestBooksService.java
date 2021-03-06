package pl.coderslab.cultureBuddies.googleapis;

import javassist.tools.web.BadHttpRequest;
import pl.coderslab.cultureBuddies.exceptions.NotExistingRecordException;
import pl.coderslab.cultureBuddies.googleapis.restModel.BookFromGoogle;

import java.util.List;

public interface RestBooksService {
    List<BookFromGoogle> getGoogleBooksList(String title, String author, Integer pageNo) throws NotExistingRecordException, BadHttpRequest;

    int countMaxPage(String title, String author) throws NotExistingRecordException;
}
