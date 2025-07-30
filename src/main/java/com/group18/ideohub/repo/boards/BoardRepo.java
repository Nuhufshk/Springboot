package com.group18.ideohub.repo.boards;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.group18.ideohub.model.boards.BoardsModel;

public interface BoardRepo extends MongoRepository<BoardsModel, String> {
    List<BoardsModel> findByUserId(String userId);

    List<BoardsModel> findByTitleContaining(String title);

    boolean existsByBoardNumber(int boardNumber);

    BoardsModel findByBoardNumber(int boardNumber);

    BoardsModel findByTitle(String title);

}
