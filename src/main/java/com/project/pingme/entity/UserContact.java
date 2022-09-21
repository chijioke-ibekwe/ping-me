package com.project.pingme.entity;

import com.project.pingme.enums.RequestStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_contacts")
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private User contact;

    @OneToMany(mappedBy = "userContact")
    private List<ChatMessage> chatMessages;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
