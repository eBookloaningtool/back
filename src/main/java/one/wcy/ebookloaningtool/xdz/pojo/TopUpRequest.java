package one.wcy.ebookloaningtool.xdz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request class for top-up operations.
 * Contains the amount to be added to the user's account balance.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopUpRequest {
    /**
     * The amount to be added to the user's account balance
     */
    private double amount;
}