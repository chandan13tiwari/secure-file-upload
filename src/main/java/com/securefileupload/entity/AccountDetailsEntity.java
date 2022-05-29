package com.securefileupload.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "acc_dtls")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"usr_name"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AccountDetailsEntity {

    @Id
    @Column(name = "acc_id")
    private UUID accountId;

    @Column(name = "usr_name", unique = true)
    private String username;

    @Column(name = "psswrd")
    private String password;

    @Column(name = "acc_type")
    private String accountType;

}
