package app.siham.banque.dtos;

import app.siham.banque.entity.TypeOp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import app.siham.banque.entity.*;

import javax.persistence.*;
import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private TypeOp type;
    private String description;
}