package paulodev.orderflowapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    // A lista de quem está ouvindo (Navegadores, Postman...)
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Métod0 para alguém se inscrever
    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);

        // Se o cara sumir (fechar aba), removemos ele da lista
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    // Métod0 para notificar a novidade para tod0s
    public void notify(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("order-update").data(message));
            } catch (IOException e) {
                emitters.remove(emitter); // Se der erro, remove da lista
            }
        }
    }

}
