package academy.devdojo.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Reactive Streams
 *  1 - Asyncrono
 *  2 - Não bloqueante
 *  3 - Backpressure
 *  Publisher - Emitir os Eventos <- (subscrive) Subscriber
 *  Subscription is created
 *
 *  Publisher chama o onSubscrive -> Subiscriber
 *  Subscripition <- (request N)  - Subscriber gerencia o Backspressure
 *
 *  Publisher -> (onNext) Subcriber
 *  1 - Executa quantas vezes foi solicitado
 *  2 - Publisher enviar tudo (onComplete)- subcriber and subscription canceled
 *  3 - Error (onError) - subcriber and subscription canceled
 * **/

@Slf4j
public class MonoTest {



    @Test
    public void monoSubscriber(){
        log.info("Teste de execução");
        String nome = "Wagner Costa";
        Mono<String> mono = Mono.just(nome).log();

        mono.subscribe();
        log.info("----------------------------------");

        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumer(){
        log.info("Teste de execução");
        String nome = "Wagner Costa";
        Mono<String> mono = Mono.just(nome).log();

        mono.subscribe(s -> log.info("Value {}",s));
        log.info("----------------------------------");

        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerError(){
        log.info("Teste de execução");
        String nome = "Wagner Costa";
        Mono<String> mono = Mono.just(nome)
                .map( s -> {throw new RuntimeException("Testing mono with error");});

        mono.subscribe(s -> log.info("Value {}",s),
                        s -> log.error("Somenthing bad happened"));

        mono.subscribe(s -> log.info("Value {}",s),
                Throwable::printStackTrace);

        log.info("----------------------------------");

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
