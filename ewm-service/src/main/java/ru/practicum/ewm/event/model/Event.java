package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.Location;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Events")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category; // int in DB
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "state_id")
    @Enumerated(EnumType.STRING)
    private EventStatus state;
    @Column(name = "title")
    private String title;
    @Column(name = "views")
    private Integer views;
}
