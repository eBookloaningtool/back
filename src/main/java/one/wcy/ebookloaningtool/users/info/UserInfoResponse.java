package one.wcy.ebookloaningtool.users.info;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserInfoResponse {
    private String UUID;
    private String name;
    private String email;
    private int balance;
    private LocalDate createdat;
    private List<String> borrowedBooks;
    private List<String> historicalBooks;
    private List<String> wishlist;
    private List<String> star;
    private List<String> shoppingcart;
    private List<String> transactionHistory;

    public UserInfoResponse(String UUID, String name, String email, int balance, 
                            LocalDate createdat, List<String> borrowedBooks, 
                            List<String> historicalBooks, List<String> wishlist, 
                            List<String> star, List<String> shoppingcart, 
                            List<String> transactionHistory) {
        this.UUID = UUID;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.createdat = createdat;
        this.borrowedBooks = borrowedBooks;
        this.historicalBooks = historicalBooks;
        this.wishlist = wishlist;
        this.star = star;
        this.shoppingcart = shoppingcart;
        this.transactionHistory = transactionHistory;
    }
} 