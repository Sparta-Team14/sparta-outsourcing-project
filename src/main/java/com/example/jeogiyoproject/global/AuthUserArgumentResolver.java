package com.example.jeogiyoproject.global;

import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

        if (hasAuthAnnotation != isAuthUserType) {
            throw new CustomException(ErrorCode.USE_AUTH_AUTHUSER_REQUIRED);
        }

        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Long userId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("email");
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        return new AuthUser(userId, email, userRole);
    }
}
