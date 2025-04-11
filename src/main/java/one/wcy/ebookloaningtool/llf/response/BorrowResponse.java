package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import one.wcy.ebookloaningtool.utils.Response;


@Getter
@Setter
public class BorrowResponse extends Response {
    private LocalDateTime dueDate;

    public BorrowResponse(String state, LocalDateTime localDateTime) {
        super(state);
        this.dueDate = localDateTime;
    }

}
