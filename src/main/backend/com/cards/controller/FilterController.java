package com.cards.controller;

import com.cards.dto.response.BasicFilterDTO;
import com.cards.enums.*;
import com.cards.repository.GraphicCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/filters")
@AllArgsConstructor
public class FilterController {
    private final GraphicCardRepository cardRepository;

    @GetMapping("manufacturer")
    public List<BasicFilterDTO> getManufacturers() {

        return Arrays.stream(Manufacturer.values())
                .map(manufacturer -> new BasicFilterDTO(manufacturer.name(), manufacturer.name()))
                .collect(Collectors.toList());

    }

    @GetMapping("technology")
    public List<BasicFilterDTO> getTechnologies() {

        return Arrays.stream(Technology.values())
                .map(technology -> new BasicFilterDTO(technology.name(), technology.name()))
                .collect(Collectors.toList());

    }

    @GetMapping("memory-amount")
    public List<BasicFilterDTO> getMemoryAmount() {

        return Arrays.stream(MemoryAmount.values())
                .map(memoryAmount -> new BasicFilterDTO(memoryAmount.getAmount(), memoryAmount.getAmount()))
                .collect(Collectors.toList());

    }

    @GetMapping("memory-type")
    public List<BasicFilterDTO> getMemoryType() {

        return Arrays.stream(MemoryType.values())
                .map(memoryType -> new BasicFilterDTO(memoryType.name(), memoryType.name()))
                .collect(Collectors.toList());

    }

    @GetMapping("memory-bus")
    public List<BasicFilterDTO> getMemoryBus() {

        return Arrays.stream(MemoryBus.values())
                .map(memoryBus -> new BasicFilterDTO(memoryBus.getBus(), memoryBus.getBus()))
                .collect(Collectors.toList());

    }

    @GetMapping("connector")
    public List<BasicFilterDTO> getConnectors() {

        return Arrays.stream(Connector.values())
                .map(connector -> new BasicFilterDTO(connector.getType(), connector.getType()))
                .collect(Collectors.toList());

    }

    @GetMapping("outputs")
    public List<BasicFilterDTO> getOutputs() {

        ArrayList<String> outputs = new ArrayList<>();

        cardRepository.findAll()
                .forEach(graphicCard -> outputs.addAll(graphicCard.getTypeOfOutputs()));

        return outputs.stream()
                .distinct()
                .map(output -> new BasicFilterDTO(output, output))
                .collect(Collectors.toList());

    }

}
