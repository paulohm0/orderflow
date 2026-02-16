package paulodev.orderflowapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import paulodev.orderflowapi.service.SseService;

@RestController
@RequestMapping("/server-sent-events")
@Slf4j
public class SseController {

    @Autowired
    private SseService sseService;

    @GetMapping(path = "/emitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter emitter() {
        // Cria uma conexão que dura 5 minutos (depois o front reconecta)
        SseEmitter emitter = new SseEmitter(300000L);
        sseService.addEmitter(emitter);
        log.info("Novo listener conectado!");
        return emitter;
    }
}
