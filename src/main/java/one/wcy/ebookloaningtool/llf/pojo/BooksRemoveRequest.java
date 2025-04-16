package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksRemoveRequest {
    private List<String> bookId;
}
