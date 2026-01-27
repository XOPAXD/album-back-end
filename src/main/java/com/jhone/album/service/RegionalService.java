package com.jhone.album.service;

import com.jhone.album.dto.RegionalDTO;
import com.jhone.album.entity.Regional;
import com.jhone.album.repository.RegionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegionalService {
    @Autowired
    private RegionalRepository repository;

    @Transactional
    public void syncRegionais(List<RegionalDTO> remoteList) {
        List<Regional> localAtivos = repository.findByAtivoTrue();

        Map<Integer, Regional> localMap = localAtivos.stream()
                .collect(Collectors.toMap(Regional::getId, r -> r));

        List<Regional> toSave = new ArrayList<>();
        Set<Integer> remoteIds = remoteList.stream()
                .map(RegionalDTO::getId)
                .collect(Collectors.toSet());

        for (RegionalDTO remote : remoteList) {
            Regional local = localMap.get(remote.getId());

            if (local == null) {
                toSave.add(new Regional(remote.getId(), remote.getNome(), true));
            } else if (!local.getNome().equals(remote.getNome())) {
                local.setAtivo(false);
                toSave.add(local);
                toSave.add(new Regional(remote.getId(), remote.getNome(), true));
            }
        }

        localAtivos.stream()
                .filter(l -> !remoteIds.contains(l.getId()))
                .forEach(l -> {
                    l.setAtivo(false);
                    toSave.add(l);
                });

        repository.saveAll(toSave);
    }
}
