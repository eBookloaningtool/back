package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.time.LocalDateTime;


@Getter
@Setter
public class RenewResponse extends Response {
    private LocalDateTime newDueDate;

    public RenewResponse(String state, LocalDateTime localDateTime) {
        super(state);
        this.newDueDate = localDateTime;
    }

}