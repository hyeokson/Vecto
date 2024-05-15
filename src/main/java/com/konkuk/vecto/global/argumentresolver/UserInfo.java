package com.konkuk.vecto.global.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// User의 ID값을 받을 때 사용
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserInfo {
}
