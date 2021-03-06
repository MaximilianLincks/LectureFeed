package com.lecturefeed.entity.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EqualsAndHashCode
public class Question {

    //null means the question is not yet handled by the server
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //null means the question was send anonymous
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    private String message;
    private Integer rating = 0;
    private Long created;
    private Boolean anonymous;
    //null means the question was never closed
    private Long closed;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private Set<Participant> voters = new HashSet<>();

}
