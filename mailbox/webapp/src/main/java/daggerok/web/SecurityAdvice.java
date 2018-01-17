package daggerok.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@ControllerAdvice
public class SecurityAdvice {

  private Mono<Principal> currentUser;

  @ModelAttribute("csrfToken") Mono<CsrfToken> csrfToken(final ServerWebExchange exchange) {

    exchange.getAttributes().forEach((k, v) -> log.info("k={}, v={}", k, v));

    final CsrfToken csrfToken = exchange.getAttribute(CsrfToken.class.getName());

    return Mono.justOrEmpty(csrfToken)
               .switchIfEmpty(Mono.just(new DefaultCsrfToken(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME,
                                                             CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME,
                                                             "empty"))
                                  .doOnSuccess(token -> exchange.getAttributes()
                                                                .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME,
                                                                     token)));
  }

  @ModelAttribute("currentUser") Mono<Principal> currentUser(final Mono<Principal> currentUser) {
    return currentUser;
  }
}