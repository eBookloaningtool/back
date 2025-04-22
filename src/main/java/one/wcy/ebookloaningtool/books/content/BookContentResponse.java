package one.wcy.ebookloaningtool.books.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class BookContentResponse {
    private String bookId;
    private String contentURL;

    public BookContentResponse(String bookId, String contentURL) {
        this.bookId = bookId;
        this.contentURL = contentURL;
    }
} 