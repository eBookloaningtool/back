/**
 * Base response class for API responses.
 * Provides a common structure for all API responses with a state field
 * indicating the status of the operation.
 */
package one.wcy.ebookloaningtool.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Response {
    /**
     * State of the response, typically indicating success or failure
     * of the requested operation
     */
    protected String state;
}
