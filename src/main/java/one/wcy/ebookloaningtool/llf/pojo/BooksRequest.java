package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksRequest {
    private List<String> bookId;
}
