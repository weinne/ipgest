package br.com.ipgest.ipgest.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor to add global attributes to the model and view.
 * This interceptor adds the logged-in user's username, church presence status, 
 * and church ID to the model if the view is not a redirect.
 * 
 * <p>This interceptor is executed after the handler is executed, but before the view is rendered.</p>
 * 
 * <p>Attributes added to the model:</p>
 * <ul>
 *   <li>username - the username of the logged-in user</li>
 *   <li>hasChurch - a boolean indicating if the user is associated with a church</li>
 *   <li>churchId - the ID of the church the user is associated with, or null if not associated</li>
 * </ul>
 * 
 * @author 
 * @see org.springframework.web.servlet.HandlerInterceptor
 * @see br.com.ipgest.service.UserService
 */
@Component
public class GlobalAttributesInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    /**
     * Intercepts the request after the handler method has been executed and before the view is rendered.
     * Adds user-related attributes to the ModelAndView if the view name is not a redirect.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the chosen handler to execute, for type and/or instance examination
     * @param modelAndView the ModelAndView that the handler returned (can also be null)
     * @throws Exception in case of errors
     */
    @SuppressWarnings("null")
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && Objects.nonNull(modelAndView.getViewName()) && !modelAndView.getViewName().startsWith("redirect:")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                User user = authService.getLoggedInUser();
                if (user != null) {
                    modelAndView.addObject("username", user.getUsername());
                    modelAndView.addObject("hasChurch", user.getChurch() != null);
                    modelAndView.addObject("churchId", user.getChurch() != null ? user.getChurch().getId() : null);
                }
            }
        }
    }
}