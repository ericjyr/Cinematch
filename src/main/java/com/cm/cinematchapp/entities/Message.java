//package com.cm.cinematchapp.entities;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.cm.cinematchapp.constants.EntityConstants;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.Setter;
//
//@Entity
//@Data
//public class Message {
//
//    @Id
//    @Column(name="message_id", nullable=false)
//        @GeneratedValue(strategy = GenerationType.AUTO)
//    @Setter(AccessLevel.NONE)
//    private Long messageId;
//
//    @Column(name="sender_id", nullable=false)
//    private Long senderId;
//
//    @Column(name="receiver_id", nullable=false)
//    private Long receiverId;
//
//    @Column(name="content", nullable=false)
//    private String content;
//
//    //timestamp;
//
//
//
//
//
//}
