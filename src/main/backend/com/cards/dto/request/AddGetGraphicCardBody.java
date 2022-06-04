package com.cards.dto.request;

import com.cards.entity.GraphicCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGetGraphicCardBody {
    private UUID cardId;
    private UUID cardPhoto;
    private Integer clockMemory;
    private String cooling;
    private String coreClock;
    private Integer cuda;
    private String manufacturer;
    private String memoryAmount;
    private String memoryBus;
    private String powerConnector;
    private Integer powerConsumption;
    private String producentCode;
    private String producentSite;
    private Integer recommendedPower;
    private String rtxSupport;
    private String size;
    private String[] supportedLibraries;
    private String technology;
    private String title;
    private String typeOfConnector;
    private String typeOfMemory;
    private String[] typeOfOutputs;

    public static AddGetGraphicCardBody convertFromEntity(GraphicCard card) {

        return new AddGetGraphicCardBody(
                card.getCardId(),
                card.getCardPhoto().getFileId(),
                card.getMemoryClock(),
                card.getCooling(),
                card.getCoreClock(),
                card.getCudaCoresAmount(),
                card.getManufacturer().name(),
                card.getMemoryAmount().getAmount(),
                card.getMemoryBus().getBus(),
                card.getPowerConnector(),
                card.getPowerConsumption(),
                card.getProducentCode(),
                card.getProducentSite(),
                card.getRecommendedPower(),
                card.getRtxSupport().toString(),
                card.getSize(),
                card.getSupportedLibraries().toArray(new String[0]),
                card.getTechnology().name(),
                card.getTitle(),
                card.getTypeOfConnector().getType(),
                card.getTypeOfMemory().name(),
                card.getTypeOfOutputs().toArray(new String[0])
        );

    }

}
