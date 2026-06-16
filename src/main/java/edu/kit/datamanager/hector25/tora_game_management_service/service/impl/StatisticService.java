package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IClassificationDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IImageDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IStatisticService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.AccumulatedDataDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardElementDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.StatisticsDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticService implements IStatisticService {

    IClassificationDao classificationDao;
    IPlayerDao playerDao;
    IClassificationService classificationService;
    IImageDao imageDao;


    public StatisticService(IPlayerDao playerDao, IClassificationService classificationService, IClassificationDao iClassificationDao, IImageDao imageDao) {
        this.playerDao = playerDao;
        this.classificationService = classificationService;
        this.classificationDao = iClassificationDao;
        this.imageDao = imageDao;
    }

    @Override
    public LeaderboardDTO getLeaderboardByAmount(Pageable pageable){
        List<Object[]> leaderboardObjects = playerDao.getLeaderboardByAmount(pageable);
        List<LeaderboardElementDTO> leaderboardDTOs = new ArrayList<LeaderboardElementDTO>();
        int offsetForPage = pageable.getPageNumber() *  pageable.getPageSize();
        for (int i = 0; i < leaderboardObjects.size(); i++) {
            leaderboardDTOs.add(new LeaderboardElementDTO((String) leaderboardObjects.get(i)[0], offsetForPage + i, Optional.ofNullable(leaderboardObjects.get(i)[1].toString())));
        }
        return new LeaderboardDTO(pageable.getPageNumber(), leaderboardDTOs);
    }

    @Override
    public LeaderboardDTO getLeaderboardByAmountByPlayer(Player player, int pagesize){
        Integer playerRank = playerDao.getRankForAmountByPlayerId(player.getId());
        Pageable pageable = PageRequest.of((playerRank / pagesize), pagesize);

        return this.getLeaderboardByAmount(pageable);
    }

    @Override
    public LeaderboardDTO getLeaderboardByConfidence(Pageable pageable){
        List<Object[]> leaderboardObjects = playerDao.getLeaderboardByConfidence(pageable);
        List<LeaderboardElementDTO> leaderboardDTOs = new ArrayList<LeaderboardElementDTO>();
        int offsetForPage = pageable.getPageNumber() *  pageable.getPageSize();
        for (int i = 0; i < leaderboardObjects.size(); i++) {
            String score;
            if(leaderboardObjects.get(i)[1] == null){
                score = "0";
            }else{
                score = leaderboardObjects.get(i)[1].toString();
            }

            leaderboardDTOs.add(new LeaderboardElementDTO((String) leaderboardObjects.get(i)[0], offsetForPage + i, Optional.ofNullable(score)));
        }
        return new LeaderboardDTO(pageable.getPageNumber(), leaderboardDTOs);
    }

    @Override
    public LeaderboardDTO getLeaderboardByConfidenceByPlayer(Player player, int pagesize){
        Integer playerRank = playerDao.getRankForConfidenceByPlayerId(player.getId());
        Pageable pageable = PageRequest.of((playerRank / pagesize), pagesize);

        return this.getLeaderboardByAmount(pageable);
    }

    @Override
    public StatisticsDTO getAmountStatisticsByPlayerAndYear(Player player, int year) {
        List<Classification> classifications = classificationDao.findClassificationsByPlayerIdAndYear(player.getId(), year);
        boolean isLeapYear = LocalDate.of(year, 1, 1).isLeapYear();
        double[] dailyClassificationAmounts;
        if (isLeapYear){
            dailyClassificationAmounts = new double[366];
        }else {
            dailyClassificationAmounts = new double[365];
        }


        classifications.forEach(classification -> {
            int dayOfYear = classification.getCreatedAt().getDayOfYear() - 1;
            dailyClassificationAmounts[dayOfYear] ++;
        });

        return new StatisticsDTO(year, dailyClassificationAmounts);
    }

    @Override
    public StatisticsDTO getConfidenceStatisticsByPlayerAndYear(Player player, int year) {
        List<Object[]> confidenceDayMonth = classificationDao.findAvgByYearByPlayer(year, player.getId());

        double[] dailyConfidence;

        if (LocalDate.of(year, 1, 1).isLeapYear()) {
            dailyConfidence = new double[366];
        }  else {
            dailyConfidence = new double[365];
        }

        for (Object[] confidence : confidenceDayMonth) {
            LocalDate localDate = LocalDate.of(year, ((Number) confidence[1]).intValue(), ((Number) confidence[2]).intValue());
            dailyConfidence[localDate.getDayOfYear() - 1] = ((Number) confidence[0]).doubleValue();
        }


        return new StatisticsDTO(year, dailyConfidence);
    }

    @Override
    public AccumulatedDataDTO getTotalData(){
        playerDao.findAll().forEach(player -> {classificationService.generatePlayerConfidences(player.getId());}); //Updating all Confidences

        ArrayList<AccumulatedDataDTO.AccumulatedImageDataDTO> accumulatedImages = new ArrayList<>();

        imageDao.findAll().forEach(image -> {

            float weighedCountDecorated = 0;
            float weighedCountUndecorated = 0;
            float weighedCountDataError = 0;
            float weighedCountTotal = 0;
            int countTotal = 0;


            for (Classification classification : classificationService.findClassificationsForImage(image.getId())) {
                double confidence = classification.getConfidence();
                double confidenceFactor = Math.pow((confidence - 0.5) * 2, 5);

                if (classification.getIsDatasetError()) {
                    weighedCountDataError += (float) confidenceFactor;
                } else if (classification.getDecorated()) {
                    weighedCountDecorated += (float) confidenceFactor;
                } else {
                    weighedCountUndecorated += (float) confidenceFactor;
                }
                countTotal ++;
            }

            weighedCountTotal = weighedCountDecorated + weighedCountUndecorated +  weighedCountDataError;

            AccumulatedDataDTO.AccumulatedImageDataDTO.AggregatedResults results = new AccumulatedDataDTO.AccumulatedImageDataDTO.AggregatedResults(
                    weighedCountDecorated / weighedCountTotal,
                    weighedCountUndecorated / weighedCountTotal,
                    weighedCountDataError / weighedCountTotal
            );

            AccumulatedDataDTO.AccumulatedImageDataDTO accumulatedImageDataDTO = new AccumulatedDataDTO.AccumulatedImageDataDTO(image.getId(), image.getLink(), image.getCharacter(), results, countTotal);

            accumulatedImages.add(accumulatedImageDataDTO);
        });

    return new AccumulatedDataDTO(accumulatedImages);
    }
}