package com.jhone.album.job;

import com.jhone.album.dto.RegionalDTO;
import com.jhone.album.service.RegionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class RegionalScheduleTask {
    private final RegionalService service;
    private final RestTemplate restTemplate;
    private final String API_URL = "https://integrador-argus-api.geia.vip/v1/regionais";

    public RegionalScheduleTask(RegionalService service) {
        this.service = service;
        this.restTemplate = new RestTemplate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("Aplicação iniciada. Executando sincronização inicial de regionais...");
        executeSync();
    }

    // Roda a cada 5 minutos (300000 ms)
    @Scheduled(fixedRate = 300000)
    public void executeSync() {
        try {
            RegionalDTO[] response = restTemplate.getForObject(API_URL, RegionalDTO[].class);

            if (response != null) {
                List<RegionalDTO> remoteList = Arrays.asList(response);

                service.syncRegionais(remoteList);

                log.info("Sincronização de regionais finalizada com sucesso.");
            }
        } catch (Exception e) {
            log.error("Erro ao sincronizar regionais: " + e.getMessage());
        }
    }
}
