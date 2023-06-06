package org.amaltsev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.amaltsev.entity.enums.UserState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //позволяем БД самой генерировать первичный ключ значения
    private Long id;
    private Long telegramUserId;
    @CreationTimestamp                      // прописывает дату создания объекта в БД
    private LocalDateTime firstLoginDate;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;               // почту при регистрации
    private Boolean isActive;           // Изменится при активации аккаунта по поч
    @Enumerated(EnumType.STRING)
    private UserState state;

}