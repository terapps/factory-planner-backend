package terapps.factoryplanner.controllers

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver


@Component
class ExceptionHandler : DefaultHandlerExceptionResolver() {
    override fun doResolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: Exception): ModelAndView? {
        val result = super.doResolveException(request, response, handler, ex)

        throw ex

        return result
    }

    override fun getOrder(): Int {
        return HIGHEST_PRECEDENCE
    }
}