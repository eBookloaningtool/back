package one.wcy.ebookloaningtool.books.popular;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class PopularBooksResponse extends Response {
    private List<String> bookId;

    public PopularBooksResponse(List<String> bookIds) {
        super("success");
        this.bookId = bookIds;
    }
} 