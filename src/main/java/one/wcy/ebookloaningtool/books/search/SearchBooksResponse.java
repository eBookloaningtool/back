package one.wcy.ebookloaningtool.books.search;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class SearchBooksResponse extends Response {
    private List<String> bookId;

    public SearchBooksResponse(String state, List<String> bookIds) {
        super(state);
        this.bookId = bookIds;
    }
    
    public SearchBooksResponse(String state) {
        super(state);
    }
} 