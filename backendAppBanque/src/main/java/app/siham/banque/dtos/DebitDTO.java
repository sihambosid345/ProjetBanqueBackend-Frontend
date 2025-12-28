package app.siham.banque.dtos;


import lombok.Data;
@Data
public class DebitDTO {
    private String id;
    private double amount;
    private String description;
}
