package com.blog.bloggy.alarm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmTypes {
    FRIEND_REQUEST_RECEIVED(1, "친구 추가 요청을 받았습니다. 자세한 내용을 확인하려면 여기를 클릭해주세요."),
    FRIEND_REQUEST_ACCEPTED(2, "상대방이 친구 추가 요청을 수락하였습니다. 자세한 내용을 확인하려면 여기를 클릭해주세요."),
    RECOMMENDATION_LEFT(3, "친구 한 명이 글을 남겼습니다. 자세한 내용을 확인하려면 여기를 클릭해주세요.");

    private final int category;
    private final String message;
}
